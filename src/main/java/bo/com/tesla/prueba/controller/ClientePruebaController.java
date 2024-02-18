package bo.com.tesla.prueba.controller;

import bo.com.tesla.prueba.dto.ResponsePruebaDto;
import bo.com.tesla.prueba.entity.ClientePruebaEntity;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import bo.com.tesla.prueba.service.ClientePruebaService;
import bo.com.tesla.prueba.service.GuitarraPruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/prueba-cliente")
public class ClientePruebaController {

    @Autowired
    private ClientePruebaService clientePruebaService;

    @PostMapping(path = "/registrar")
    public ResponseEntity<?> registrar(@RequestBody ClientePruebaEntity clientePruebaEntity){
        ResponsePruebaDto re =  clientePruebaService.registrar(clientePruebaEntity);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @PutMapping(path = "/modificar")
    public ResponseEntity<?> modificar(@RequestBody ClientePruebaEntity clientePruebaEntity){
        ResponsePruebaDto re =  clientePruebaService.modificar(clientePruebaEntity);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @DeleteMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> modificar(@PathVariable Long id){
        ResponsePruebaDto re =  clientePruebaService.eliminar(id);
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
    @GetMapping(path = "/obtener-todos")
    public ResponseEntity<?> modificar(){
        ResponsePruebaDto re =  clientePruebaService.obtenerTodos();
        return new ResponseEntity<>(re, HttpStatus.OK);
    }
}
