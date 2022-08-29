package com.aom.sopaDePalabra.api.service;

import com.aom.sopaDePalabra.api.dto.*;
import com.aom.sopaDePalabra.clases.SopaDePalabra;
import com.aom.sopaDePalabra.clases.Sopas;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class SopasService {

    @Autowired
    Sopas sopas;

    public SopaDePalabra crarSopa(SopaRequestDto sopaRequestDto) throws Exception{
        try{
            return this.sopas.guardarSopa(sopaRequestDto);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<SopaResponseIdDto> getListaSopas(){

        List<SopaResponseIdDto> listSopaResponseId = new ArrayList<SopaResponseIdDto>();
        List<SopaDePalabra> lis = this.sopas.getAllListSopas();
        lis.forEach( i  -> {
            SopaResponseIdDto srId = new SopaResponseIdDto();
            srId.setId(i.getId());
            listSopaResponseId.add(srId);
        });
        return listSopaResponseId;
    }

    public List<PalabrasEnSopaResponseDto> getLIstaDePalabrasDeLaSopa(String id){

        List<PalabrasEnSopaResponseDto> listPalabras = new ArrayList<PalabrasEnSopaResponseDto>();

        List<SopaDePalabra> listSopasPalabras = this.sopas.getListSopas();
        listSopasPalabras.forEach( s -> {
            if(s.getId().equals(id)){
                List<String> lp = s.getPalabrasDeLaSopa();
                lp.forEach( p -> {
                    String pTemp = p;
                    PalabrasEnSopaResponseDto pRespTemp = new PalabrasEnSopaResponseDto();
                    pRespTemp.setPalabra(p);
                    listPalabras.add(pRespTemp);
                } );
            }
        });

        return listPalabras;
    }

    public List<PalabrasEncontradasResponseDto> getLIstaDePalabrasEncontradas(String id){

        List<PalabrasEncontradasResponseDto> listPalabras = new ArrayList<PalabrasEncontradasResponseDto>();

        List<SopaDePalabra> listSopasPalabras = this.sopas.getListSopas();
        listSopasPalabras.forEach( s -> {
            if(s.getId().equals(id)){
                List<String> lp = s.getPalabrasEncontradas();
                lp.forEach( p -> {
                    String pTemp = p;
                    PalabrasEncontradasResponseDto pRespTemp = new PalabrasEncontradasResponseDto();
                    pRespTemp.setPalabra(p);
                    listPalabras.add(pRespTemp);
                } );
            }
        });
        return listPalabras;
    }

    public String visualizarSopa(String id){

        String sopaString = "";
        SopaDePalabra sopa = this.sopas.getSopaById(id);
        ArrayList<ArrayList<String>> sopaLetras = sopa.getSopa();
        for ( ArrayList<String> list : sopaLetras){
            for (String letra: list){
                sopaString += letra + " ";
            }
            sopaString += "\n";
        }
        return sopaString;
    }

    public String indicarEncontrarPalabra(String id, CordenadasRequestDto coordenadas) throws Exception{
        return this.sopas.indicarEncontrarPalabra(id, coordenadas);
    }
}
