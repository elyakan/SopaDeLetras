package com.aom.sopaDePalabra.clases;

import com.aom.sopaDePalabra.api.dto.CordenadasRequestDto;
import com.aom.sopaDePalabra.api.dto.SopaRequestDto;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class Sopas {

    @Autowired
    SopaDePalabra sopaDePalabra;

    private Integer id;
    private List<SopaDePalabra> listSopas;

    Sopas(){
        this.listSopas = new ArrayList<SopaDePalabra>();
    }

    /**
     * Guarda una {@link SopaDePalabra} en la {@link List}<{@link SopaDePalabra}>
     * @param sopaRequestDto {@link SopaRequestDto}
     * @return {@link SopaRequestDto}
     * @throws IOException
     * @throws Exception
     */
    public SopaDePalabra guardarSopa(SopaRequestDto sopaRequestDto) throws IOException, Exception {
        SopaDePalabra sopa = this.sopaDePalabra.armarSopa(sopaRequestDto);
        this.listSopas.add(sopa);
        return sopa;
    }

    /**
     * Devuelve una {@link List}<{@link SopaDePalabra}>
     * @return {@link List}<{@link SopaDePalabra}>
     */
    public List<SopaDePalabra> getAllListSopas(){
        return this.getListSopas();
    }

    /**
     * Obtiene una {@link SopaDePalabra} segun el {@link Integer} index de la posicion en la lista
     * @param index {@link Integer}
     * @return {@link SopaDePalabra}
     * @throws Exception
     */
    public SopaDePalabra getSopaByPosicion(Integer index) throws Exception{
        if(this.getListSopas().size() >0 ){
            SopaDePalabra sopa = this.getListSopas().get(index);
            return sopa;
        }
        throw new Exception("No existen sopas");
    }

    /**
     * Muetra una {@link SopaDePalabra} en consola
     * @param sopa {@link SopaDePalabra}
     */
    public void mostrarSopaEnConsola(SopaDePalabra sopa){
        this.sopaDePalabra.mostrarSopaEnConsola(sopa);
    }

    /**
     * Indicar que se ha encontrado una palabra en una {@link SopaDePalabra} segun el {@link String} id parasado por parametros
     * y el {@link CordenadasRequestDto} coordenadas
     * @param id {@link String}
     * @param coordenadas {@link CordenadasRequestDto}
     * @return {@link String}
     * @throws Exception
     */
    public String indicarEncontrarPalabra(String id, CordenadasRequestDto coordenadas) throws Exception {

        String conrdenada_inicial = coordenadas.getSr().toString()+"-"+coordenadas.getSc();
        String conrdenada_final = coordenadas.getEr().toString()+"-"+coordenadas.getEc();

        List<SopaDePalabra> listSopasPalabras = this.getAllListSopas();

        for (SopaDePalabra sopa: listSopasPalabras) {
            if(sopa.getId().equals(id)){
                String palabra = this.sopaDePalabra.indicarPalabraEncontrada(sopa, conrdenada_inicial, conrdenada_final, false);
                if(!palabra.equals("palabra no encontrada")){
                    palabra = this.sopaDePalabra.indicarPalabraEncontrada(sopa, conrdenada_inicial, conrdenada_final, true);
                    return palabra;
                }
            }
        }
        return "Palabra no encontrada";
    }

    public SopaDePalabra getSopaById(String id){
        List<SopaDePalabra> list = this.getListSopas();

        return list.stream().filter(s -> s.getId().equals(id))
                            .findFirst().orElse(null);
    }
}
