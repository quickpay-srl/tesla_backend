package bo.com.tesla.prueba.controller;

import bo.com.tesla.prueba.dto.ResponsePruebaDto;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import bo.com.tesla.prueba.service.GuitarraPruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/prueba-guitarra")
public class GuitarraPruebaController {

    @Autowired
    private GuitarraPruebaService guitarraPruebaService;

    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrar(@RequestBody GuitarraPruebaEntity guitarraPruebaEntity){
        ResponsePruebaDto re =  guitarraPruebaService.registrar(guitarraPruebaEntity);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @PutMapping(path = "/modificar")
    public ResponseEntity<?> modificar(@RequestBody GuitarraPruebaEntity guitarraPruebaEntity){
        ResponsePruebaDto re =  guitarraPruebaService.modificar(guitarraPruebaEntity);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @DeleteMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> modificar(@PathVariable Long id){
        ResponsePruebaDto re =  guitarraPruebaService.eliminar(id);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @GetMapping(path = "/obtener-todos")
    public ResponseEntity<?> modificar(){
        ResponsePruebaDto re =  guitarraPruebaService.obtenerTodos();
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
}
