package bo.com.tesla.facturaciones.computarizada.controllers;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IEntidadRecaudadorService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.facturaciones.computarizada.dto.*;
import bo.com.tesla.facturaciones.computarizada.services.IAnulacionFacturaService;
import bo.com.tesla.facturaciones.computarizada.services.IFacturaComputarizadaService;
import bo.com.tesla.recaudaciones.dto.FacturaDto;
import bo.com.tesla.recaudaciones.services.FacturaService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.recaudaciones.services.ITransaccionCobroService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import bo.com.tesla.useful.dto.ResponseDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@RestController
@RequestMapping("api/facturas")
public class FacturaController {

    private Logger logger = LoggerFactory.getLogger(FacturaController.class);
    @Value("${tesla.path.files-report}")
    private String filesReport;

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IFacturaComputarizadaService facturacionComputarizadaService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ITransaccionCobroService transaccionCobroService;

    @Autowired
    private IEntidadService entidadService;

    @Autowired
    private IAnulacionFacturaService anulacionFacturaService;

    @Autowired
    private IRecaudadoraService recaudadoraService;

    @Autowired
    private IEntidadRecaudadorService entidadRecaudadorService;

    @Secured( "ROLE_MCEVCC" )
    @PostMapping("/codigoscontroles")
    public ResponseEntity<?> postCodigoControl(@RequestBody CodigoControlDto codigoControlDto,
                                              Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if (entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = facturacionComputarizadaService.postCodigoControl(codigoControlDto, entidad.getEntidadId());
            response.put("status", true);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/codigoscontroles");
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/codigoscontroles");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/libroVentasJasper")
    public ResponseEntity<?> libroVentasJasper( @RequestBody Map body, Authentication authentication) throws BusinesException {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            ResponseDto responseDto = new ResponseDto();
            responseDto =  facturaService.findFacturaByEntidadId( Long.parseLong(body.get("entidadId")+""));
            if(!responseDto.status){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("tituloReporte", "LIBRO DE VENTAS");
            parameters.put("tituloEntidad", "ENTIDAD");
            if (body.get("fechaInicio") == null && body.get("fechaFin") == null) {
                parameters.put("tituloRangoFechas", "");
            } else if (body.get("fechaInicio") == null  && body.get("fechaFin") != null) {
                parameters.put("tituloRangoFechas","REPORTE GENERADO HASTA LA FECHA : " + body.get("fechaFin"));
            } else if (body.get("fechaInicio") != null && body.get("fechaFin") == null) {
                parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "+ body.get("fechaInicio"));
            } else {
                parameters.put("tituloRangoFechas","REPORTE GENERADO EN EL RANGO DE FECHAS : "+ body.get("fechaInicio") + "-" + body.get("fechaFin"));
            }

            parameters.put("logoQuickPay", filesReport + "/img/logo_fondo_blanco.jpg");
            if (body.get("export").equals("msexcel")) {
                parameters.put("IS_IGNORE_PAGINATION", true);
            }
            File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/libroVentas/LibroVentas.jrxml");
            JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource((List<FacturaDto>)responseDto.result);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters,ds);

            byte[] report = Util.jasperExportFormat(jasperPrint, body.get("export").toString(), filesReport);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(report.length);
            headers.setContentType(MediaType.parseMediaType("application/" + body.get("export").toString()));
            headers.set("Content-Disposition", "inline; filename=report." + body.get("export").toString());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);


        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("/libroVentasJasper/{entidadId}/{export}");
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Secured( { "ROLE_MCARF", "ROLE_MCRA", "ROLE_MCRRA",   "ROLE_MCLV", "ROLE_MCERF" } )
    @PostMapping(path = { "/entidades/{entidadId}/filters/{page}", "/filters/{page}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> postListFacturaFilter(@RequestBody FacturaFilterDto facturaFilterDto,
                                                          @PathVariable Optional<Long> entidadId,
                                                          @PathVariable int page,
                                                          Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            if(entidadId.isPresent()) {
                entidadRecaudadorService.verificarAccesoUsuarioRecaudador(usuario.getUsuarioId(), entidadId.get());
            }

            ResponseDto responseDto = new ResponseDto();
            if(!entidadId.isPresent()) {
                EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
                if(entidad == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Entidad.");
                }
                responseDto =  facturaService.findFacturaByEntidadId(entidad.getEntidadId());
                //responseDto = facturacionComputarizadaService.postFacturaLstFilter(entidad.getEntidadId(), page, facturaFilterDto, null);

            } else {
                RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
                if(recaudadorEntity == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
                }
                responseDto =  facturaService.findFacturaByEntidadId(entidadId.get());
                //responseDto = facturacionComputarizadaService.postFacturaLstFilter(entidadId.get(), page, facturaFilterDto, recaudadorEntity.getRecaudadorId());
            }
            if(responseDto != null) {

                response.put("status", true);
                response.put("message", responseDto.message);
                response.put("result", responseDto.result);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/filters/" + page);
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/filters");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
/*
    @Secured( { "ROLE_MCARF", "ROLE_MCRA", "ROLE_MCRRA",   "ROLE_MCLV", "ROLE_MCERF" } )
    @PostMapping(path = { "/entidades/{entidadId}/filters", "/filters" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> postListFacturaFilter(@RequestBody FacturaFilterDto facturaFilterDto,
                                                   @PathVariable Optional<Long> entidadId,
                                                   Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            ResponseDto responseDto = new ResponseDto();
            if(!entidadId.isPresent()) {
                EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
                if(entidad == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Entidad.");
                }
                responseDto = facturacionComputarizadaService.postFacturaLstFilter(entidad.getEntidadId(), facturaFilterDto, null);
            } else {
                RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
                if(recaudadorEntity == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
                }
                responseDto = facturacionComputarizadaService.postFacturaLstFilter(entidadId.get(), facturaFilterDto, recaudadorEntity.getRecaudadorId());
            }
            if(responseDto != null) {

                response.put("status", true);
                response.put("message", responseDto.message);
                response.put("result", responseDto.result);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/filters");
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/filters");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
*/
    @Secured( { "ROLE_MCERF" } )
    @GetMapping("/entidades/reportes/{facturaId}")
    public ResponseEntity<?> getReportFactura(@PathVariable Long facturaId,
                                              Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = facturacionComputarizadaService.getFacturaReport(entidad.getEntidadId(), null, facturaId);

//            byte[] facturaByteArray = Base64.getDecoder().decode(responseDto.report);
            byte[] facturaByteArray = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(facturaByteArray.length);
            headers.setContentType(MediaType.parseMediaType("application/pdf" ));
            headers.set("Content-Disposition", "inline; filename=report.pdf" );
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(facturaByteArray, headers, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/reportes/" + facturaId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": Ocurrio un error en el servidor, por favor intente la operacion mas tarde o consulte con su administrador." , HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/reportes/" + facturaId);
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": " + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Secured( { "ROLE_MCARC", "ROLE_MCARF", "ROLE_MCRRA" } )
    @GetMapping("/entidades/{entidadId}/reportes/{facturaId}")
    public ResponseEntity<?> getReportFacturaByEntidad(@PathVariable Long entidadId,
                                              @PathVariable Long facturaId,
                                              Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            entidadRecaudadorService.verificarAccesoUsuarioRecaudador(usuario.getUsuarioId(), entidadId);

            RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
            if(recaudadorEntity == null) {
                throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
            }

            ResponseDto responseDto = facturacionComputarizadaService.getFacturaReport(entidadId, recaudadorEntity.getRecaudadorId(), facturaId);

            //byte[] facturaByteArray = Base64.getDecoder().decode(responseDto.report);
            byte[] facturaByteArray = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(facturaByteArray.length);
            headers.setContentType(MediaType.parseMediaType("application/pdf" ));
            headers.set("Content-Disposition", "inline; filename=report.pdf" );
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(facturaByteArray, headers, HttpStatus.OK);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/" + entidadId + "/reportes/" + facturaId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/"+ entidadId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": Ocurrio un error en el servidor, por favor intente la operacion mas tarde o consulte con su administrador." , HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Secured( { "ROLE_MCRA", "ROLE_MCRRA",   "ROLE_MCEAF" } )
    @PostMapping(path = { "/entidades/{entidadId}/anulaciones/listas" , "/anulaciones/listas" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> postListFacturaAnulacion(@PathVariable Optional<Long> entidadId,
                                                      @RequestBody AnulacionFacturaLstDto anulacionFacturaLstDto,
                                                      Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean respuesta = false;
            if(!entidadId.isPresent()) {
                EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
                if(entidad == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Entidad.");
                }
                respuesta  = anulacionFacturaService.anularTransaccionFactura(entidad.getEntidadId(), anulacionFacturaLstDto, usuario);
            } else {
                RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
                if(recaudadorEntity == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
                }
                respuesta  = anulacionFacturaService.anularTransaccionFactura(entidadId.get(), anulacionFacturaLstDto, usuario);
            }

            response.put("message", "Se ha realizado la Anulación de la factura.");
            response.put("result", respuesta);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ANULACION FACTURA");
            log.setController("api/anulaciones/listas");
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            System.out.println("error: "+e.toString());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/anulaciones/listas");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            System.out.println("error: "+e.toString());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Secured( "ROLE_MCLV"  )
    @PostMapping("/librosventas")
    public ResponseEntity<?> postLibroVentasReport(@RequestBody FacturaFilterDto facturaFilterDto,
                                                   Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }

            ResponseDto responseDto = facturacionComputarizadaService.getLibroVentasReport(entidad.getEntidadId(), facturaFilterDto);
            //byte[] facturaByteArray = Base64.getDecoder().decode(responseDto.report);
            byte[] facturaByteArray = null;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(facturaByteArray.length);
            headers.setContentType(MediaType.parseMediaType("application/" + facturaFilterDto.formatFile ));
            headers.set("Content-Disposition", "inline; filename=report." + facturaFilterDto.formatFile );
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(facturaByteArray, headers, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/librosventas");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": Ocurrio un error en el servidor, por favor intente la operacion mas tarde o consulte con su administrador." , HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/reportes/\" + facturaId");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": " + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Secured( { "ROLE_MCRRA", "ROLE_MCARF", "ROLE_MCRA",   "ROLE_MCLV", "ROLE_MCERF" } )
    @GetMapping("/{facturaId}")
    public ResponseEntity<?> getFactura(@PathVariable Long facturaId,
                                        Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = facturacionComputarizadaService.getFacturaDto(entidad.getEntidadId(), facturaId);
            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("GET: api/facturas/" + facturaId);
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/entidades/reportes/" + facturaId);
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }




}
