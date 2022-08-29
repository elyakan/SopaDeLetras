package com.aom.sopaDePalabra.api.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.aom.sopaDePalabra.api.dto.*;
import com.aom.sopaDePalabra.api.service.SopasService;
import com.aom.sopaDePalabra.clases.SopaDePalabra;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicTreeUI;
import java.util.List;

@RestController
@RequestMapping("/alphabetSoup")
@CrossOrigin("*")
@Api(tags="Sopa Controller", value = "Sopa Controller", description = "Gestiona sopas de palabras")
public class SopasController {

    @Autowired
    SopasService sopasService;

    @GetMapping("")
    @ApiOperation(value = "Bienvenido(a) a la API", notes = "Mensaje de bienvenido(a) a la API")
    public ResponseEntity index() {
        return ok("Bienvenido/a a Sopa de palabras");
    }

    @PostMapping("")
    @ApiOperation(value = "Crea una sopa de palabra", notes = "Retorna una sopa de palabras")
    public ResponseEntity<?> crearSopaDeLetras(@RequestBody SopaRequestDto sopa) throws Exception{

        SopaDePalabra sopaDePalabra = this.sopasService.crarSopa(sopa);

        SopaResponseIdDto sopaResponseIdDtoResponse = new SopaResponseIdDto();
        sopaResponseIdDtoResponse.setId(sopaDePalabra.getId());
        return new ResponseEntity<>(sopaResponseIdDtoResponse, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @ApiOperation(value = "Lista las sopa de palabras", notes = "Se listan las sopas de palabras creadas")
    public ResponseEntity<?> getListSopas() throws Exception{
        List<SopaResponseIdDto>  listSopasResponseDto = this.sopasService.getListaSopas();
        return new ResponseEntity<>(listSopasResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/list/{id}")
    @ApiOperation(value = "Visualiza palabras de una sopa", notes = "Se listan las palabras que se encuentran en una sopa.")
    public ResponseEntity<?> visualizarPalabrasEnSopa(@PathVariable String id) throws Exception{

        List<PalabrasEnSopaResponseDto> listResponse = this.sopasService.getLIstaDePalabrasDeLaSopa(id);
        return new ResponseEntity<List<PalabrasEnSopaResponseDto>>(listResponse, HttpStatus.CREATED);
    }

    @GetMapping("/palabras-encontradas/{id}")
    @ApiOperation(value = "Visualiza palabras encontradas en una sopa", notes = "Se listan las palabras que se se hayan encontrado en una sopa.")
    public ResponseEntity<?> visualizarPalabrasEncontradas(@PathVariable String id) throws Exception{

        List<PalabrasEncontradasResponseDto> listResponse = this.sopasService.getLIstaDePalabrasEncontradas(id);
        return new ResponseEntity<List<PalabrasEncontradasResponseDto>>(listResponse, HttpStatus.CREATED);
    }

    @GetMapping("/view/{id}")
    @ApiOperation(value = "Visualiza una sopa de letras", notes = "Se visualiza una sopa de letras.")
    public ResponseEntity visualizarSopa(@PathVariable String id) throws Exception{

        String sopa = this.sopasService.visualizarSopa(id);
        //return new ResponseEntity<List<PalabrasEncontradasResponseDto>>(listResponse, HttpStatus.CREATED);
        return ok(sopa);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Especificar coordenadas de una palabra", notes = "Se especifican las coordenadas inicial y final de una palabra encontrada.")
    public ResponseEntity indicarEncontrarPalabra(@PathVariable String id, @RequestBody CordenadasRequestDto coordenadas) throws Exception {
        String msj = this.sopasService.indicarEncontrarPalabra(id, coordenadas);
        return ok(msj);
    }

}
