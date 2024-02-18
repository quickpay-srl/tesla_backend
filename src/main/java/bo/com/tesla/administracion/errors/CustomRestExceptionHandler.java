package bo.com.tesla.administracion.errors;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.security.services.ILogSistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ILogSistemaService logSistemaService;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField()+": " +error.getDefaultMessage());
        }
        for(ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName()+": " +error.getDefaultMessage());
        }

        String concatErrors = errors.size() > 0 ? String.join("; ", errors) : null;

        LogSistemaEntity log=new LogSistemaEntity();
        log.setModulo("Validacion");
        log.setController("Validacion");
        log.setCausa("Validacion de api");
        log.setMensaje(errors.toString());
        log.setUsuarioCreacion(1L);
        log.setFechaCreacion(new Date());
        logSistemaService.save(log);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error: " + log.getLogSistemaId()+" - "+ concatErrors , errors);

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
}
