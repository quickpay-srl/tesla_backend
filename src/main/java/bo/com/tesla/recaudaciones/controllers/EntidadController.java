package bo.com.tesla.recaudaciones.controllers;

import bo.com.tesla.administracion.dto.EntidadAdmDto;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IEntidadRecaudadorService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.recaudaciones.dto.EntidadDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import bo.com.tesla.recaudaciones.services.IDeudaClienteRService;
import bo.com.tesla.recaudaciones.services.IEntidadRService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("api/entidades")
@Validated
public class EntidadController {
    private Logger logger = LoggerFactory.getLogger(EntidadController.class); 
    
    
    @Autowired
    private IDeudaClienteRService iDeudaClienteRService;

    @Autowired
    private IEntidadRService iEntidadRService;

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IRecaudadoraService recaudadoraService;

    @Autowired
    private IEntidadRecaudadorService entidadRecaudadorService;

    /*********************ABM ENTIDADES**************************/
    @PostMapping("")
    public ResponseEntity<?> addUpdateEntidad(@Valid @RequestBody EntidadAdmDto entidadAdmDto,
                                              Authentication authentication,
                                              BindingResult result) {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean esModificacion = entidadAdmDto.entidadId != null;
            entidadAdmDto = iEntidadRService.addUpdateEntidad(entidadAdmDto, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", esModificacion ? "Se realizó la actualización del registro correctamente." : "Se realizó el registro correctamente.");
            response.put("result", entidadAdmDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("POST: api/entidades");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{entidadId}/{transaccion}")
    public ResponseEntity<?> setTransaccion(@PathVariable Long entidadId,
                                            @PathVariable String transaccion,
                                            Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            iEntidadRService.setTransaccion(entidadId, transaccion, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", "Se realizó la actualización de la transacción correctamente.");
            response.put("result", true);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("PUT: api/entidades/" + entidadId + "/" + transaccion);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/listas/{transaccion}")
    public ResponseEntity<?> setLstTransaccion(@RequestBody List<Long> entidadIdLst,
                                               @PathVariable String transaccion,
                                               Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            iEntidadRService.setLstTransaccion(entidadIdLst, transaccion, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", "Se realizó la actualización de registro(s) con el nuevo estado.");
            response.put("result", true);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("PUT: api/entidades/listas/" + transaccion);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    /*@PutMapping("/modificar/lsEstados/{estados}")
    public ResponseEntity<?> setLstEstado(@RequestBody List<Long> entidadIdLst,
                                               @PathVariable String estados,
                                               Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            estados = estados.equals("INACTIVAR")?"INACTIVO":estados;
            iEntidadRService.setLstEstado(entidadIdLst, estados, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", "Se realizó la actualización de registro(s) con el nuevo estado.");
            response.put("result", true);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("PUT: api/entidades/modificar/lsEstados/" + estados);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }*/

    @GetMapping("/{entidadId}")
    public ResponseEntity<?> getEntidadById(@PathVariable Long entidadId,
                                            Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadAdmDto entidadAdmDto = iEntidadRService.getEntidadById(entidadId);
            if  (entidadAdmDto != null) {
                response.put("status", true);
                response.put("message", "El registro fue encontrado.");
                response.put("result", entidadAdmDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El registro no fue encontrado.");
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("GET: api/entidades/" + entidadId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListEntidades(Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<EntidadAdmDto> entidadAdmDtoList = iEntidadRService.getAllEntidades();
            if (!entidadAdmDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", entidadAdmDtoList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El listado no fue encontrado.");
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDAD");
            log.setController("GET: api/entidades");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /*********************LOGOS**************************/

    @PostMapping("/upload/logo/{entidadId}")
    public ResponseEntity<?> uploadLogo(@RequestParam("file") MultipartFile file
            , @PathVariable Long entidadId
            , Authentication authentication) throws Exception {
        Map<String, Object> response = new HashMap<>();
        String path = null;
        SegUsuarioEntity usuario = new SegUsuarioEntity();
        try {
            usuario = this.segUsuarioService.findByLogin(authentication.getName());
            iEntidadRService.uploadLogo(file, entidadId, usuario.getUsuarioId());
            response.put("message", "Registro de logo correcto.");
            response.put("status", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ENTIDADES");
            log.setController("POST: api/entidades/upload/");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());

            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId() + "");
            response.put("status", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ENTIDADES");
            log.setController("POST: api/deudaCliente/upload");
            log.setCausa(e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);

            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            response.put("status", false);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*********************CARGADO CLIENTES POR ENTIDAD**************************/

    @Secured("ROLE_MCARC")
    @GetMapping("/{entidadId}/clientes/{campoBusqueda}/{datoCliente}")
    public ResponseEntity<?> getAllClientesByEntidadIdAndCampos(@PathVariable @NotNull Long entidadId,
                                                                @PathVariable @NotBlank String campoBusqueda,
                                                                @PathVariable @NotBlank String datoCliente,
                                                                Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (entidadId <= 0 || entidadId == null) {
            response.put("status", false);
            response.put("message", "La entidad es obligatoria.");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        if (campoBusqueda == null || campoBusqueda.length() == 0) {
            response.put("status", false);
            response.put("message", "El campo de búsqueda es obligatorio.");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        if (datoCliente == null || datoCliente.length() == 0) {
            response.put("status", false);
            response.put("message", "El dato del cliente es obligatorio");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            entidadRecaudadorService.verificarAccesoUsuarioRecaudador(usuario.getUsuarioId(), entidadId);
            iDeudaClienteRService.verifiedCampoInEnum(campoBusqueda);

            List<ClienteDto> clienteDtos = iDeudaClienteRService.getByEntidadAndDatoCliente(entidadId, campoBusqueda, datoCliente);
            if (clienteDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "No hay cliente(s) asociados a los parámetros de búsqueda");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", clienteDtos);
            response.put("message", "Cliente(s) encontrado(s)");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/" + entidadId + "/clientes/" + campoBusqueda + "/" + datoCliente);
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
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/" + entidadId + "/clientes/" + campoBusqueda + "/" + datoCliente);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error" + e.getMessage());
            this.logger.error("This is cause" + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (RequestRejectedException e) {
            this.logger.error("This is error" + e.getMessage());
            this.logger.error("This is cause" + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Secured("ROLE_MCARC")
    @GetMapping("/{entidadId}/clientes/{codigoCliente}/deudas")
    public ResponseEntity<?> getDeudasByCliente(@PathVariable @NotNull Long entidadId,
                                                @PathVariable @NotBlank String codigoCliente,
                                                Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        if (entidadId <= 0 || entidadId == null) {
            response.put("status", "false");
            response.put("message", "La entidad es obligatoria");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        if (codigoCliente == null || codigoCliente.length() == 0) {
            response.put("status", "false");
            response.put("message", "El código del cliente es obligatorio");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            entidadRecaudadorService.verificarAccesoUsuarioRecaudador(usuario.getUsuarioId(), entidadId);

            List<ServicioDeudaDto> servicioDeudaDtos = iDeudaClienteRService.getDeudasByCliente(entidadId, codigoCliente);
            if (servicioDeudaDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "No se encontraron deudas");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", servicioDeudaDtos);
            response.put("message", "Deudas encontradas para cliente: " + codigoCliente);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/" + entidadId + "/clientes/" + codigoCliente + "/deudas");
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
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/" + entidadId + "/clientes/" + codigoCliente + "/deudas");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /*********************CARGADO ENTIDADES**************************/

    @Secured( {"ROLE_MCARC", "ROLE_MCRA", "ROLE_MCRRA", "ROLE_MCARF" } )
    @GetMapping("/tipos")
    public ResponseEntity<?> getEntidadesByTipo(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            List<DominioDto> dominioDtos = iEntidadRService.getTipoEntidadByRecaudador(usuario);
            if (dominioDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "No existe Tipos Entidades encontrados");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", dominioDtos);
            response.put("message", "Tipos Entidades encontrados");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/tipos");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            e.printStackTrace();
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/findTipoEntidadPagadoras")
    public ResponseEntity<?> findTipoEntidadPagadoras(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            List<DominioDto> dominioDtos = iEntidadRService.findTipoEntidadPagadoras(usuario);
            if (dominioDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "No existe Tipos Entidades encontrados");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", dominioDtos);
            response.put("message", "Tipos Entidades encontrados");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/tipos");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            e.printStackTrace();
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Secured( {"ROLE_MCARC", "ROLE_MCRA", "ROLE_MCRRA", "ROLE_MCARF" } )
    @GetMapping("/tipos/{tipoEntidadId}")
    public ResponseEntity<?> getEntidadesByTipoEntidad(@PathVariable Long tipoEntidadId, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (tipoEntidadId <= 0 || tipoEntidadId == null) {
            response.put("status", false);
            response.put("message", "El tipo de entidad es obligatoria");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            List<EntidadDto> entidadDtos = iEntidadRService.getEntidadesByTipoEntidad(tipoEntidadId, usuario);
            if (entidadDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "No se encontraron Entidades por Tipo de Entidad.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", entidadDtos);
            response.put("message", "Se encontraron Entidades por Tipo");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades/tipos/" + tipoEntidadId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            e.printStackTrace();
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Secured( {"ROLE_MCARC", "ROLE_MCRA", "ROLE_MCRRA", "ROLE_MCARF" } )
    @GetMapping("/recaudadores")
    public ResponseEntity<?> getEntidadesByRecaudadora(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            List<EntidadDto> entidadDtos = iEntidadRService.getByRecaudadoraId(usuario);
            if (entidadDtos.isEmpty()) {
                response.put("status", false);
                response.put("result", null);
                response.put("message", "Entidades no encontradas.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            response.put("status", true);
            response.put("result", entidadDtos);
            response.put("message", "Se encontraron Entidades.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("RECAUDACIONES.ENTIDADES");
            log.setController("GET: api/entidades");
            log.setCausa(e.getCause() != null ? e.getCause().getCause() + "" : e.getCause() + "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + (e.getCause() != null ? e.getCause().getCause() + "" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


}