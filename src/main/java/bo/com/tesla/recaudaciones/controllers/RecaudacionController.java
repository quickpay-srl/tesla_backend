package bo.com.tesla.recaudaciones.controllers;

import bo.com.tesla.facturaciones.computarizada.controllers.FacturaController;
import bo.com.tesla.facturaciones.computarizada.dto.FacturaFilterDto;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.useful.config.BusinesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("rec/v1")
public class RecaudacionController {
    private Logger logger = LoggerFactory.getLogger(RecaudacionController.class);

    @Autowired
    private EntidadController entidadController;

    @Autowired
    private CobroClienteController cobroClienteController;

    @Autowired
    private FacturaController facturaController;

    @Secured("ROLE_MCARC")
    @GetMapping("/entidades/recaudadores") // lista empresas
    public ResponseEntity<?> getEntidadesByRecaudadora(Authentication authentication) {
        return entidadController.getEntidadesByRecaudadora(authentication);
    }

    @Secured("ROLE_MCARC") // lista cliente por empresa y codigo de cliente y ci
    @GetMapping("/entidades/{entidadId}/clientes/{campoBusqueda}/{datoCliente}")
    public ResponseEntity<?> getAllClientesByEntidadIdAndCampos(@PathVariable @NotNull Long entidadId,
                                                                @PathVariable @NotBlank String campoBusqueda,
                                                                @PathVariable @NotBlank String datoCliente,
                                                                Authentication authentication) {
        return entidadController.getAllClientesByEntidadIdAndCampos(entidadId,
                campoBusqueda,
                datoCliente,
                authentication);
    }

    @Secured("ROLE_MCARC") //
    @GetMapping("/entidades/{entidadId}/clientes/{codigoCliente}/deudas") // lista deudas por empresa y cod cliente
    public ResponseEntity<?> getDeudasByCliente(@PathVariable @NotNull Long entidadId,
                                                @PathVariable @NotBlank String codigoCliente,
                                                Authentication authentication) {
        return entidadController.getDeudasByCliente(entidadId,
                codigoCliente,
                authentication);
    }

    @Secured("ROLE_MCARC")
    @PostMapping("/cobros") // registrar cobro de un cliente
    public ResponseEntity<?> postCobrarDeudas(@Valid @RequestBody ClienteDto clienteDto,
                                              Authentication authentication) {
        return cobroClienteController.postCobrarDeudas(clienteDto, authentication);

    }

    @Secured( {"ROLE_MCARC", "ROLE_MCARF"} )
    @GetMapping("/entidades/{entidadId}/reportes/{facturaId}")
    public ResponseEntity<?> getReportFacturaByEntidad(@PathVariable Long entidadId,
                                                       @PathVariable Long facturaId,
                                                       Authentication authentication) {
        return facturaController.getReportFacturaByEntidad(entidadId, facturaId, authentication);
    }

    @Secured( {"ROLE_MCARC", "ROLE_MCARF"} )
    @PostMapping("/entidades/{entidadId}/filters/{page}")
    public ResponseEntity<?> postListFacturaFilter(@RequestBody FacturaFilterDto facturaFilterDto,
                                                   @PathVariable Long entidadId,
                                                   @PathVariable int page,
                                                   Authentication authentication) {

        return facturaController.postListFacturaFilter(facturaFilterDto, Optional.ofNullable(entidadId), page, authentication);
    }
}
