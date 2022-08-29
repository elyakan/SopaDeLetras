package com.aom.sopaDePalabra.clases;

import com.aom.sopaDePalabra.api.dto.SopaRequestDto;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@AllArgsConstructor
@Component
public class SopaDePalabra {

    private final String banco = "abcdefghijklmnopqrstuvwxyz";

    private String id;
    private Integer ancho;
    private Integer alto;
    private ArrayList<ArrayList<String>> sopa;
    private ArrayList<String> palabrasEncontradas;
    private ArrayList<String> palabrasDeLaSopa;

    private boolean izq_der;
    private boolean der_izq;
    private boolean up_down;
    private boolean down_up;
    private boolean diagonal;
    private DireccionPalabra direccionPalabra;

    SopaDePalabra(){
        this.setAlto(5);
        this.setAncho(5);
        this.setIzq_der(true);
        this.setUp_down(true);
    }

    /**
     * Construye una sopa de palabra, con los datos de {@link SopaRequestDto} pasado por parámetro
     * @param sopaRequestDto {@link SopaRequestDto}
     * @return {@link SopaDePalabra}
     * @throws IOException
     * @throws Exception
     */
    public SopaDePalabra armarSopa(SopaRequestDto sopaRequestDto) throws IOException, Exception{

        SopaDePalabra sopa = new SopaDePalabra();
        sopa.setId(java.util.UUID.randomUUID().toString());

        if(sopaRequestDto.getH() >= 15 && sopaRequestDto.getH() <= 80)
            sopa.setAlto(sopaRequestDto.getH());
        else
            throw new Exception("El alto debe estar entre 15 y 80");

        if(sopaRequestDto.getW() >= 15 && sopaRequestDto.getW() <= 80)
            sopa.setAncho(sopaRequestDto.getW());
        else
            throw new Exception("El ancho debe estar entre 15 y 80");

        sopa.setPalabrasEncontradas(new ArrayList<String>());

        System.out.println("cantidad de palabras "+ this.cantidadDePalabras(sopa));
        ArrayList<String> palabrasDeLaSopa = this.obtenerPalabrasAleatoriasDelDicconario( this.cantidadDePalabras(sopa) );

        sopa.setPalabrasDeLaSopa(palabrasDeLaSopa);
        sopa.setSopa(this.llenarSopaVacia(sopaRequestDto.getH(), sopaRequestDto.getW()));
        sopa.setIzq_der(sopaRequestDto.getLtr());
        sopa.setDer_izq(sopaRequestDto.getRtl());
        sopa.setUp_down(sopaRequestDto.getTtb());
        sopa.setDown_up(sopaRequestDto.getBtt());
        sopa.setDiagonal(sopaRequestDto.getD());

        sopa = this.colocarPalabrasEnLaSopa(palabrasDeLaSopa, sopa);
        return sopa;
    }

    /**
     * Calcula y devuelve la cantidad de palabras en la {@link SopaDePalabra}, proporcionalmente a la cantidad de letras
     * @param sopa
     * @return
     */
    public int cantidadDePalabras(SopaDePalabra sopa){
        Integer cantLetrasDeSopa = sopa.getAlto() * sopa.getAncho();
        Integer cantPalagras = cantLetrasDeSopa/25;
        return cantPalagras;
    }

    /**
     * Obteniene una letra aleatoria del banco definico de letras
     * @return {@link String} letra
     */
    public int letraAleatorioEnRango() {
        return ThreadLocalRandom.current().nextInt(0, this.getBanco().length());
    }


    /**
     * Llena la {@link SopaDePalabra} inicialmente con el caracter "-"
     * @param alto {@link Integer} alto de la {@link SopaDePalabra}
     * @param ancho {@link Integer} ancho de la {@link SopaDePalabra}
     * @return
     */
    public ArrayList<ArrayList<String>> llenarSopaVacia(Integer alto, Integer ancho){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < alto; i++) {
            ArrayList<String> listString = new ArrayList<String>();
            for (int j = 0; j < ancho; j++){
                listString.add("-");
            }
            list.add(listString);
        }
        return list;
    }

    /**
     * Pinta en consola la {@link SopaDePalabra} de palabra
     * @param sopa {@link SopaDePalabra}
     */
    public void mostrarSopaEnConsola(SopaDePalabra sopa){
        for ( ArrayList<String> list : sopa.getSopa()) {
            for (String letra: list){
                System.out.print(letra);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Chequea que la busqueda esté habilitada en diferentes direcciones
     * @param fStart {@link Integer} valor x coordenada inicial
     * @param cStart {@link Integer} valor y coordenada inicial
     * @param fFinish {@link Integer} valor x coordenada final
     * @param cFinish {@link Integer} valor y coordenada final
     * @throws Exception
     */
    public void chequearDir(Integer fStart, Integer cStart, Integer fFinish, Integer cFinish) throws Exception{

        // <--------- no habilitada
        if(fStart == fFinish && cFinish < cStart && !this.isDer_izq() ){
            throw new Exception("La búsqueda de derecha a izquierda no esta habilitada");
        }
        // ---------> no habilitada
        if( fFinish == fStart && cFinish > cStart && !this.isIzq_der() ){
            throw new Exception("La búsqueda de izquierda a derecha no esta habilitada");
        }

        // up - down
        if(cStart == cFinish && fStart < fFinish && !this.isDown_up()){
            throw new Exception("La búsqueda de arriba a abajo no esta habilitada");
        }

        // down - up
        if(cStart == cFinish && fStart > fFinish && !this.isUp_down()){
            throw new Exception("La búsqueda de arriba a abajo no esta habilitada");
        }
    }

    /**
     * Devuelve un {@link String[]} de la cordenada pasada como parámetro, haciendole split en el caracter '-'
     * @param cordenada {@link String} cordenada de la {@link SopaDePalabra}
     * @return {@link String[]}
     */
    public String[] obtenerFilaColumDeCoordenada(String cordenada){
        String[] aCordenada = cordenada.split("-");
        return aCordenada;
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la horizontal de la {@link SopaDePalabra} de izquierda a derecha.
     * @param sopa {@link SopaDePalabra} sopa de palabra
     * @param fInicial {@link Integer} fila coordenada inicial
     * @param cInicial {@link Integer} columna coordenada final
     * @param cFinal {@link Integer} columna coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarIzqDer(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        if(sopa.isIzq_der()){
            String palabra = "";
            for(int i = 0; i < sopa.getSopa().size(); i++ ){
                ArrayList<String> arrayLetras = sopa.getSopa().get(i);
                for(int j = 0; j < arrayLetras.size() ; j++ ){
                    if(i==fInicial && (j >= cInicial && j <= cFinal) ){
                        String letra = arrayLetras.get(j);
                        palabra += letra;
                        if(palabra_encontrada){
                            this.convertirLetraMyusculaEnSopa(sopa, i,j, letra);
                        }
                    }
                }
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras de izquierda a derecha.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la horizontal de la {@link SopaDePalabra} de derecha a izquierda.
     * @param sopa {@link SopaDePalabra}
     * @param fInicial {@link Integer} fila coordenada inicial
     * @param cInicial {@link Integer} columna coordenada final
     * @param cFinal {@link Integer} columna coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDerIzq(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        if(sopa.isDer_izq()){
            String palabra = "";
            for(int i = sopa.getSopa().size()-1 ; i >= 0 ; i-- ){
                ArrayList<String> arrayLetras = this.obtenerFila(sopa, i); //this.getSopa().get(i);
                for(int j = 0; j < arrayLetras.size() ; j++ ){
                    if(i==fInicial && (j <= cInicial && j >= cFinal) ){
                        String letra = arrayLetras.get(j);
                        palabra += letra;
                        if(palabra_encontrada){
                            this.convertirLetraMyusculaEnSopa(sopa, i,j, letra);
                        }
                    }
                }
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras de derecha a izquierda. ");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la vertical de la {@link SopaDePalabra} de arriba hacia abajo.
     * @param sopa {@link SopaDePalabra}
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarArribaAbajo(SopaDePalabra sopa, Integer cInicial, Integer fInicial, Integer fFinal, boolean palabra_encontrada ) throws Exception{
        if(sopa.isUp_down()){
            String palabra = "";

            while (fInicial <= fFinal){
                ArrayList<String> arrayLetras = sopa.getSopa().get(fInicial);
                String letra = arrayLetras.get(cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial++;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras de arriba hacia abajo.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la vertical de la {@link SopaDePalabra} de abajo hacia arriba.
     * @param sopa {@link SopaDePalabra}
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarAbajoAArriba(SopaDePalabra sopa, Integer cInicial, Integer fInicial, Integer fFinal, boolean palabra_encontrada ) throws Exception{
        if(sopa.isDown_up()){
            String palabra = "";

            while (fInicial >= fFinal){
                ArrayList<String> arrayLetras = sopa.getSopa().get(fInicial);
                String letra = arrayLetras.get(cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial--;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras de abajo hacia arriba.");
    }

    /**
     * LLama el método correspondiente diagonal a visualizar segun direccion de las cordenadas
     * @param sopa {@link SopaDePalabra}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonal(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        if(sopa.isDiagonal()){
            if(sopa.getDireccionPalabra().toString().equals("DIAG_UP_DOWN_IZQ_DER")){
                return this.buscarDiagonalUpDownIzqDer(sopa, fInicial, cInicial, fFinal, cFinal, palabra_encontrada);
            }
            if(sopa.getDireccionPalabra().toString().equals("DIAG_UP_DOWN_DER_IZQ")){
                return this.buscarDiagonalUpDownDerIzq(sopa, fInicial, cInicial, fFinal, cFinal, palabra_encontrada);
            }
            if(sopa.getDireccionPalabra().toString().equals("DIAG_DOWN_UP_IZQ_DER")){
                return this.buscarDiagonalDownUpIzqDer(sopa, fInicial, cInicial, fFinal, cFinal, palabra_encontrada);
            }
            if(sopa.getDireccionPalabra().toString().equals("DIAG_DOWN_UP_DER_IZQ")){
                return this.buscarDiagonalDownUpDerIzq(sopa, fInicial, cInicial, fFinal, cFinal, palabra_encontrada);
            }
        }
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la diagonal de la {@link SopaDePalabra} de arriba hacia abajo y de izquierda a derecha.
     * @param sopa {@link SopaDePalabra}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalUpDownIzqDer(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        if(sopa.isDiagonal()){
            String palabra = "";
            while ( (fInicial <= fFinal) && (cInicial <= cFinal) ){
                String letra = this.obtenerLetraColumna(this.obtenerFila(sopa, fInicial), cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial++;
                cInicial++;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDePalabra} de abajo hacia arriba y de derecha a izquierda.
     * @param sopa {@link SopaDePalabra}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalDownUpDerIzq(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        if(sopa.isDiagonal()){
            String palabra = "";
            while ( (fInicial >= fFinal) && (cInicial >= cFinal) ){
                String letra = this.obtenerLetraColumna(this.obtenerFila(sopa, fInicial), cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial--;
                cInicial--;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDePalabra} de arriba hacia abajo y de derecha a izquierda.
     * @param sopa {@link SopaDePalabra}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalUpDownDerIzq(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        if(sopa.isDiagonal()){
            String palabra = "";
            while ( (fInicial <= fFinal) && (cInicial >= cFinal) ){
                String letra = this.obtenerLetraColumna(this.obtenerFila(sopa, fInicial), cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial++;
                cInicial--;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDePalabra} de abajo hacia arriba y de izquierda a derecha.
     * @param sopa
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalDownUpIzqDer(SopaDePalabra sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        if(sopa.isDiagonal()){
            String palabra = "";
            while ( (fInicial >= fFinal) && (cInicial <= cFinal) ){
                String letra = this.obtenerLetraColumna(this.obtenerFila(sopa, fInicial), cInicial);
                palabra += letra;
                if(palabra_encontrada){
                    this.convertirLetraMyusculaEnSopa(sopa, fInicial, cInicial, letra);
                }
                fInicial--;
                cInicial++;
            }
            return palabra;
        }
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Obtiene y un {@link ArrayList}<{@link String}> con las letras de una fila
     * @param fila {@link Integer}
     * @return {@link ArrayList}<{@link String}> con las letras de una fila
     */
    public ArrayList<String> obtenerFila(SopaDePalabra sopa, Integer fila){
        return sopa.getSopa().get(fila);
    }

    /**
     * Devuelve la letra de una columna de una {@link SopaDePalabra}
     * @param arrayLetras {@link ArrayList}<{@link String}> array de string de letras de una fila
     * @param columna {@link Integer} posición de la columna
     * @return {@link String} letra de la posición
     */
    public String obtenerLetraColumna(ArrayList<String> arrayLetras, Integer columna){
        return arrayLetras.get(columna);
    }

    /**
     * Convierte un {@link String} letra pasada como parametro a mayuscula y modifica su posicion en la {@link SopaDePalabra}
     * @param sopa {@link SopaDePalabra}
     * @param fila {@link int} fila de la {@link SopaDePalabra}
     * @param col {@link int} columna de la {@link SopaDePalabra}
     * @param letra {@link String} letra a convertir a mayuscula
     */
    void convertirLetraMyusculaEnSopa(SopaDePalabra sopa, int fila, int col, String letra ){
        sopa.getSopa().get(fila).set(col, letra.toUpperCase());
    }

    /**
     * Develve un {@link HashMap}<{@link String},{@link Integer}> con la X y Y inicial y final de unas {@link String} coordenadas pasada por parámetro
     * @param cordenadaInicial
     * @param coordenadaFinal
     * @return {@link HashMap}<{@link String},{@link Integer}>
     */
    public HashMap<String, Integer> obtenerCordenadas(String cordenadaInicial, String coordenadaFinal){

        String[] cordInicial = this.obtenerFilaColumDeCoordenada(cordenadaInicial);
        Integer fInicial = Integer.valueOf(cordInicial[0]);
        Integer cInicial = Integer.valueOf(cordInicial[1]);

        String[] cordFinal = this.obtenerFilaColumDeCoordenada(coordenadaFinal);
        Integer fFinal = Integer.valueOf(cordFinal[0]);
        Integer cFinal = Integer.valueOf(cordFinal[1]);

        HashMap<String, Integer> cordenadas = new HashMap<String, Integer>();
        cordenadas.put("fInicial", fInicial);
        cordenadas.put("cInicial", cInicial);
        cordenadas.put("fFinal", fFinal);
        cordenadas.put("cFinal", cFinal);

        return cordenadas;
    }


    /**
     * Chequa que una coordenada sea una coordenada válida de la {@link SopaDePalabra}
     * @param sopa {@link SopaDePalabra}
     * @param cordenada {@link String}
     * @return {@link Boolean}
     */
    public boolean coordenadasValidas(SopaDePalabra sopa, String cordenada){
        String[] aCordenada = this.obtenerFilaColumDeCoordenada(cordenada);
        Integer inicial = Integer.valueOf(aCordenada[0]);
        Integer fnicial = Integer.valueOf(aCordenada[1]);

        if( ( inicial >= 0 && inicial < sopa.getAlto()) && inicial >= 0 && inicial < sopa.getAncho() )
            return true;
        else
            return false;
    }

    /**
     * Chequea la dirrecion de busqueda dada una {@link SopaDePalabra} y un {@link String} coordenada inicial y {@link String} coordenada final
     * @param sopa {@link SopaDePalabra}
     * @param cordenadaInicial {@link String} coordenada inicial
     * @param coordenadaFinal {@link String} coordenada final
     * @return
     */
    public boolean obtenerDireccionCordenadas(SopaDePalabra sopa, String cordenadaInicial, String coordenadaFinal){

        HashMap<String, Integer> coordenadas = this.obtenerCordenadas(cordenadaInicial, coordenadaFinal);

        //horizontal isq-der
        if( ( coordenadas.get("fInicial") >= 0 && coordenadas.get("fInicial") < sopa.getAncho() )
                && (coordenadas.get("fInicial") == coordenadas.get("fFinal"))
                && ( coordenadas.get("cInicial") < coordenadas.get("cFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.HOR_IZQ_DER);
            return true;
        }
        //horizontal der-izq
        if( ( coordenadas.get("fInicial") >= 0 && coordenadas.get("fInicial") < sopa.getAncho() )
                && (coordenadas.get("fInicial") == coordenadas.get("fFinal"))
                && ( coordenadas.get("cInicial") > coordenadas.get("cFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.HOR_DER_IZQ);
            return true;
        }

        //horizontal up-dow
        if( ( coordenadas.get("cInicial") >= 0 && coordenadas.get("cInicial") < sopa.getAlto() )
                && (coordenadas.get("cInicial") == coordenadas.get("cFinal"))
                && ( coordenadas.get("fFinal") > coordenadas.get("fInicial") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.VERT_UP_DOWN);
            return true;
        }

        //horizontal dow-up
        if( ( coordenadas.get("cInicial") >= 0 && coordenadas.get("cInicial") < sopa.getAlto() )
                && (coordenadas.get("cInicial") == coordenadas.get("cFinal"))
                && ( coordenadas.get("fFinal") < coordenadas.get("fInicial") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.VERT_DOWN_UP);
            return true;
        }

        //DIAGONAL
        //DIAGONAL-UP-DOWN-IZQ-DER
        if( ( coordenadas.get("fFinal") - coordenadas.get("fInicial") ) == ( coordenadas.get("cFinal") - coordenadas.get("cInicial"))
                && (coordenadas.get("cInicial") < coordenadas.get("cFinal") )
                && (coordenadas.get("fInicial") < coordenadas.get("fFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.DIAG_UP_DOWN_IZQ_DER);
            return true;
        }
        //DIAGONAL-UP-DOWN-DER-IZQ
        if( ( coordenadas.get("fFinal") - coordenadas.get("fInicial") ) == ( coordenadas.get("cInicial") - coordenadas.get("cFinal"))
                && (coordenadas.get("cInicial") > coordenadas.get("cFinal") )
                && (coordenadas.get("fInicial") < coordenadas.get("fFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.DIAG_UP_DOWN_DER_IZQ);
            return true;
        }
        //DIAGONAL-DOWN-UP-IZQ-DER
        if( ( coordenadas.get("fInicial") - coordenadas.get("fFinal") ) == ( coordenadas.get("cFinal") - coordenadas.get("cInicial"))
             && (coordenadas.get("fInicial") > coordenadas.get("fFinal") )
             && (coordenadas.get("cInicial") < coordenadas.get("cFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.DIAG_DOWN_UP_IZQ_DER);
            return true;
        }
        //DIAGONAL-DOWN-UP-DER-IZQ
        if( ( coordenadas.get("fInicial") - coordenadas.get("fFinal") ) == ( coordenadas.get("cInicial") - coordenadas.get("cFinal"))
                && (coordenadas.get("fInicial") > coordenadas.get("fFinal") )
                && (coordenadas.get("cInicial") > coordenadas.get("cFinal") ) ){
            sopa.setDireccionPalabra(DireccionPalabra.DIAG_DOWN_UP_DER_IZQ);
            return true;
        }

        return false;
    }

    /**
     * Busca y devuelve una palabra en la {@link SopaDePalabra}. Si la encuenla la convierte a mayuscula.
     * @param sopa {@link SopaDePalabra}
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param coordenadaFinal {@link String} coordenada final
     * @param palabra_encontrada {@link Boolean} condició para definir si vuelve a ejecutar el código para convertir la palabra a mayuscula
     * @return {@link String} palabra encontrada o mensaje de no encontrada
     * @throws Exception
     */
    public String indicarPalabraEncontrada(SopaDePalabra sopa, String coordenadaInicial, String coordenadaFinal, boolean palabra_encontrada) throws Exception{

        if(!this.obtenerDireccionCordenadas(sopa, coordenadaInicial, coordenadaFinal))
            throw new Exception("Coordenadas invalidas");

        HashMap<String, Integer> cordenadas = this.obtenerCordenadas(coordenadaInicial, coordenadaFinal);

        String palabra = null;
        //buscar de isquierda a derecha
        if(sopa.getDireccionPalabra().equals(DireccionPalabra.HOR_IZQ_DER)){
            palabra = this.buscarIzqDer(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"), cordenadas.get("cFinal"), palabra_encontrada);
        }

        //buscar de derecha a izquierda
        if(sopa.getDireccionPalabra().equals(DireccionPalabra.HOR_DER_IZQ)){
            palabra = this.buscarDerIzq(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"),  cordenadas.get("cFinal"), palabra_encontrada);
        }

        //buscar de arriba a abajo
        if(sopa.getDireccionPalabra().equals(DireccionPalabra.VERT_UP_DOWN)){
            palabra = this.buscarArribaAbajo(sopa, cordenadas.get("cInicial"), cordenadas.get("fInicial"), cordenadas.get("fFinal"), palabra_encontrada);
        }
        //buscar de abajo a arriba
        if(sopa.getDireccionPalabra().equals(DireccionPalabra.VERT_DOWN_UP)){
            palabra = this.buscarAbajoAArriba(sopa, cordenadas.get("cInicial"), cordenadas.get("fInicial"), cordenadas.get("fFinal"), palabra_encontrada);
        }

        if( sopa.getDireccionPalabra().toString().indexOf("DIAG") != -1 ){
            palabra = this.buscarDiagonal(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"), cordenadas.get("fFinal"), cordenadas.get("cFinal"), palabra_encontrada);
        }

        String msj = "palabra no encontrada";
        if(sopa.getPalabrasDeLaSopa().contains(palabra) && !sopa.getPalabrasEncontradas().contains(palabra)){
            sopa.getPalabrasEncontradas().add(palabra);
            msj = "La palabra "+ palabra + " es correcta";
        }else{
            if(sopa.getPalabrasDeLaSopa().contains(palabra) && palabra_encontrada){
                msj = "La palabra "+ palabra + " es correcta";
            }
        }
        return msj;
    }

    /**
     * Genera posiciones aleatoria del diccionario de palabras
     * @param numMaximo {@link Integer} número máximo de cantidad de palabras del diccionario
     * @param cantNum {@link Integer} Cantidad de palabras a buscar en el diccionario
     * @return {@link ArrayList} con los numeros de lineas seleccionadas
     */
    public ArrayList generarPosicionesAleatoriasDelDiccionario(Integer numMaximo, Integer cantNum){

        ArrayList numeros = new ArrayList();
        int numero;
        for (int i = 0; i<cantNum; i++){
            numero = (int)(Math.random() * numMaximo + 1);
            if(numeros.contains(numero)){
                i--;
            }else{
                numeros.add(numero);
            }
        }
        return numeros;
    }

    /**
     * Genera y devuelve una coordenada aleatoria
     * @param alto {@link Integer } alto de la {@link SopaDePalabra}
     * @param ancho {@link Integer } ancho de la {@link SopaDePalabra}
     * @return {@link String} coordenada generada
     */
    public String generarCordenadaAleatoria(Integer alto, Integer ancho){

        Integer x = 0;
        for (int i = 0; i< alto; i++){
            x = (int)(Math.random() * i);
        }
        Integer y = 0;
        for (int j = 0; j< ancho; j++){
            y = (int)(Math.random() * j);
        }
        String cordenada = x.toString() +"-"+ y.toString();
        return cordenada;
    }


    /**
     * Obtiene y devuelve la X de una coordenada
     * @param coordenada {@link String} coordenada
     * @return {@link Integer}
     */
    public Integer getXCoordenada(String coordenada){
        String[] aCordenada = coordenada.split("-");
        return Integer.valueOf(aCordenada[0]);
    }

    /**
     * Obtiene y devuelve la Y de una coordenada
     * @param coordenada {@link String} coordenada
     * @return {@link Integer}
     */
    public Integer getYCoordenada(String coordenada){
        String[] aCordenada = coordenada.split("-");
        return Integer.valueOf(aCordenada[1]);
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} de izquierda a derecha
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDeIzqDer(String coordenadaInicial, String palabra, SopaDePalabra sopa){
        if(this.cabePalabraDeIzqADer(palabra, this.getXCoordenada(coordenadaInicial), this.getYCoordenada(coordenadaInicial), sopa)){

            Integer y = this.getYCoordenada(coordenadaInicial);
            for (int i = 0; i < palabra.length(); i++){
                char letra = palabra.charAt(i);
                sopa.getSopa().get(this.getXCoordenada(coordenadaInicial)).set(y,Character.toString(letra));
                y++;
            }
            return sopa;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} de derecha a izquierda
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDeDerIzq(String coordenadaInicial, String palabra, SopaDePalabra sopa){
        if(this.cabePalabraDeADerIzq(palabra, this.getXCoordenada(coordenadaInicial), this.getYCoordenada(coordenadaInicial), sopa )){

            Integer y = this.getYCoordenada(coordenadaInicial);
            Integer posPalaba = 0;
            for (int i = 0; i < palabra.length(); i++){
                char letra = palabra.charAt(posPalaba);
                sopa.getSopa().get(this.getXCoordenada(coordenadaInicial)).set(y,Character.toString(letra));
                y--;
                posPalaba++;
            }
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} de arriba hacia abajo
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDeUpDown(String coordenadaInicial, String palabra, SopaDePalabra sopa){

        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for (int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x++;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} de abajo hacia arriba
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDeDownUp(String coordenadaInicial, String palabra, SopaDePalabra sopa){
        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for (int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x--;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} diagonal de arriba hacia abajo y de izquierda a derecha
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDIAGUpDownIzqDer(String coordenadaInicial, String palabra, SopaDePalabra sopa){

        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for( int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x++;
            y++;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} diagonal de arriba hacia abajo y derecha a izquierda
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDIAGUpDownDerIzq(String coordenadaInicial, String palabra, SopaDePalabra sopa){

        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for( int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x++;
            y--;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} diagonal de abajo hacia arriba y de izquierda a derecha
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDIAGDownUpIzqDer(String coordenadaInicial, String palabra, SopaDePalabra sopa){

        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for( int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x--;
            y++;
        }
        return sopa;
    }

    /**
     * Escribe la palabra en la {@link SopaDePalabra} diagonal de abajo hacia arriba y de derecha a izquierda
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escribir
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra escibirPalabraDIAGDownUpDerIzq(String coordenadaInicial, String palabra, SopaDePalabra sopa){

        Integer x = this.getXCoordenada(coordenadaInicial);
        Integer y = this.getYCoordenada(coordenadaInicial);

        for( int i = 0; i < palabra.length(); i++){
            char letra = palabra.charAt(i);
            sopa.getSopa().get(x).set(y, Character.toString(letra));
            x--;
            y--;
        }
        return sopa;
    }

    /**
     * Selecciona la direccion en que se va a escibir la palabra segun una direccion aleatoria
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param palabra {@link String} palabra a escibir
     * @param sopa {@link SopaDePalabra}
     * @param direccionPalabra {@link DireccionPalabra} direccion a escribir
     * @return
     */
    public SopaDePalabra escibirPalabraEnSopa(String coordenadaInicial, String palabra, SopaDePalabra sopa, DireccionPalabra direccionPalabra){

        if(direccionPalabra.equals(DireccionPalabra.HOR_IZQ_DER)){
            return this.escibirPalabraDeIzqDer(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.HOR_DER_IZQ)){
            return this.escibirPalabraDeDerIzq(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.VERT_UP_DOWN)){
            return this.escibirPalabraDeUpDown(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.VERT_DOWN_UP)){
            return this.escibirPalabraDeDownUp(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_UP_DOWN_IZQ_DER)){
            return this.escibirPalabraDIAGUpDownIzqDer(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_UP_DOWN_DER_IZQ)){
            return this.escibirPalabraDIAGUpDownDerIzq(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_DOWN_UP_IZQ_DER)){
            return this.escibirPalabraDIAGDownUpIzqDer(coordenadaInicial, palabra, sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_DOWN_UP_DER_IZQ)){
            return this.escibirPalabraDIAGDownUpDerIzq(coordenadaInicial, palabra, sopa);
        }
        return null;
    }

    /**
     * Genera una sirecion aleatoria
     * @param sopa {@link SopaDePalabra}
     * @return {@link DireccionPalabra} dirección de la palabra
     */
    public DireccionPalabra generarDireccionAleatoria(SopaDePalabra sopa){

        boolean ok = false;
        while (!ok){
            int direccion = new Random().nextInt(DireccionPalabra.values().length);
            DireccionPalabra direccionPalabra = DireccionPalabra.values()[direccion];

            if( (sopa.isIzq_der() && direccionPalabra.equals(DireccionPalabra.HOR_IZQ_DER) )
                || ( sopa.isDer_izq() && direccionPalabra.equals(DireccionPalabra.HOR_DER_IZQ) )
                || ( sopa.isUp_down() && direccionPalabra.equals(DireccionPalabra.VERT_UP_DOWN) )
                || ( sopa.isDown_up() && direccionPalabra.equals(DireccionPalabra.VERT_DOWN_UP) )
                || ( sopa.isDiagonal() && direccionPalabra.toString().indexOf("DIAG") !=1 )
                ){
                ok = true;
            }

            if(ok){
                return DireccionPalabra.values()[direccion];
            }
        }
        return null;
    }

    /**
     * Chequea que quepa una palabra en la sopa segun la direccion y la coordenada
     * @param coordenada {@link String} coordenada
     * @param palab {@link String} palabra
     * @param sopa {@link SopaDePalabra}
     * @param direccionPalabra {@link DireccionPalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabra(String coordenada, String palab, SopaDePalabra sopa, DireccionPalabra direccionPalabra){

        if(direccionPalabra.equals(DireccionPalabra.HOR_IZQ_DER)){
            return this.cabePalabraDeIzqADer(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.HOR_DER_IZQ)){
            return this.cabePalabraDeADerIzq(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.VERT_UP_DOWN)){
            return this.cabePalabraDeUpDown(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.VERT_DOWN_UP)){
            return this.cabePalabraDeDownUp(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_UP_DOWN_IZQ_DER)){
            return this.cabePalabraDeDIAGUpDownIzqDer(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_UP_DOWN_DER_IZQ)){
            return this.cabePalabraDeDIAGUpDownDerIzq(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_DOWN_UP_IZQ_DER)){
            return this.cabePalabraDeDIAGDownUpIzqDer(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }

        if(direccionPalabra.equals(DireccionPalabra.DIAG_DOWN_UP_DER_IZQ)){
            return this.cabePalabraDeDIAGDownUpDerIzq(palab, this.getXCoordenada(coordenada), this.getYCoordenada(coordenada), sopa);
        }
        return false;
    }

    public boolean cabePalabraDeIzqADer(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){
        if(palabra.length() <= (sopa.getAncho() - yInicial)){
            ArrayList<String> columna = sopa.getSopa().get(xInicial);
            for (int i = 0; i < palabra.length(); i++ ){
                if(columna.get(yInicial) != "-" && (!columna.get(yInicial).equals(palabra.charAt(i)) ) ){
                    return false;
                }
                yInicial++;
            }
            return true;
        }
        return false;
    }

    /**
     * Chequea que quepa una palabra de derecha a izquierda
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeADerIzq(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){
        if(palabra.length() <= ( 0 + yInicial)){

            ArrayList<String> columna = sopa.getSopa().get(xInicial);
            Integer posPal = 0;
            for (int i = 0; i < palabra.length(); i++){
                if(columna.get(yInicial) != "-" && (!columna.get(i).equals(palabra.charAt(posPal)) ) ){
                    return false;
                }
                yInicial--;
                posPal++;
            }
            return true;
        }
        return false;
    }

    /**
     * Chequea que quepa una palabra de izquierda a derecha
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeUpDown(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){
        if(palabra.length() <= ( sopa.getAlto() - xInicial)){

            Integer posPal = 0;
            for (int i = 0; i < palabra.length(); i++){
                ArrayList<String> letras = sopa.getSopa().get(xInicial);
                for( int y = 0; y < letras.size(); y++ ){
                    if(y == yInicial){
                        if(letras.get(y) != "-" && (!letras.get(y).equals(palabra.charAt(posPal)) ) ){
                            return false;
                        }
                    }
                }
                yInicial++;
                posPal++;
            }
            return true;
        }
        return false;
    }

    /**
     * Chequea que quepa una palabra de arriba hacia abajo
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeDownUp(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){
        if(palabra.length() <= ( xInicial+1-0 )){

            Integer posPal = 0;
            for (int i = 0; i < palabra.length(); i++){
                if(sopa.getSopa().get(xInicial).get(yInicial) != "-"  && (!sopa.getSopa().get(xInicial).get(yInicial).equals(palabra.charAt(posPal)) ) ){
                    return false;
                }
                posPal++;
            }
            return true;
        }
        return false;
    }

    /**
     * Chequea que quepa una palabra diagonalmente de arriba hacia abajo y de izquierda a derecha
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeDIAGUpDownIzqDer(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){

        for( int i = 0; i < palabra.length(); i++){
            if(!this.chequeoSiPosicionVacia(sopa, xInicial, yInicial)){
                return false;
            }
            xInicial++;
            yInicial++;
        }
        return true;
    }

    /**
     * Chequea que quepa una palabra diagonalmente de arriba hacia abajo y de a derecha a izquierda
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeDIAGUpDownDerIzq(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){

        for( int i = 0; i < palabra.length(); i++){
            if(!this.chequeoSiPosicionVacia(sopa, xInicial, yInicial)){
                return false;
            }
            xInicial++;
            yInicial--;
        }
        return true;
    }

    /**
     * Chequea que quepa una palabra diagonalmente de abajo hacia harriba y de izquierda a derecha
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeDIAGDownUpIzqDer(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){

        for( int i = 0; i < palabra.length(); i++){
            if(!this.chequeoSiPosicionVacia(sopa, xInicial, yInicial)){
                return false;
            }
            xInicial--;
            yInicial++;
        }
        return true;
    }

    /**
     * Chequea que quepa una palabra diagonalmente de abajo hacia arriba y de derecha a izquierda
     * @param palabra {@link String}
     * @param xInicial {@link Integer}
     * @param yInicial {@link Integer}
     * @param sopa {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean cabePalabraDeDIAGDownUpDerIzq(String palabra, Integer xInicial, Integer yInicial, SopaDePalabra sopa){

        for( int i = 0; i < palabra.length(); i++){
            if(!this.chequeoSiPosicionVacia(sopa, xInicial, yInicial)){
                return false;
            }
            xInicial--;
            yInicial--;
        }
        return true;
    }

    /**
     * Chequea si una posición de la {@link SopaDePalabra} tiene el caracter "-"
     * @param sopa {@link SopaDePalabra}
     * @param x {@link Integer}
     * @param y {@link Integer}
     * @return {@link Boolean}
     */
    public boolean chequeoSiPosicionVacia(SopaDePalabra sopa, Integer x, Integer y){
        if(this.isXValida(sopa, x) ){
            if(this.isYValida(sopa, y)){
                if(sopa.getSopa().get(x).get(y) != "-" ){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Chequea que X sea valida
     * @param sopa {@link SopaDePalabra}
     * @param x {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean isXValida(SopaDePalabra sopa, Integer x){
        if(x < sopa.getAlto() && x >= 0){
            return true;
        }
        return false;
    }

    /**
     * Chequea que Y sea valida
     * @param sopa {@link SopaDePalabra}
     * @param y {@link SopaDePalabra}
     * @return {@link Boolean}
     */
    public boolean isYValida(SopaDePalabra sopa, Integer y){
        if(y < sopa.getAncho() && y >= 0 ){
            return true;
        }
        return false;
    }

    /**
     * Rellena el resto de la {@link SopaDePalabra}, es decir donde estan los caracteres "-" con letras aleatorias
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra rellenarPalabrasEnPosicionesVacias(SopaDePalabra sopa){
        for(int i = 0; i < sopa.getAlto(); i++) {
            for (int j = 0; j < sopa.getAncho(); j++){
                //ArrayList<String> letras =
                if(sopa.getSopa().get(i).get(j) == "-" ){
                    char letra = this.getBanco().charAt(this.letraAleatorioEnRango());
                    sopa.getSopa().get(i).set(j, Character.toString(letra));
                }
            }
        }
        return sopa;
    }

    /**
     * Optiene un {@link ArrayList}<{@link String}> con palabras aleatorias del diccionario de palabra
     * @param cantPalabras {@link Integer}
     * @return {@link ArrayList}<{@link String}>
     */
    public ArrayList<String> obtenerPalabrasAleatoriasDelDicconario(Integer cantPalabras) {

        try{
            Integer cantPalabrasDelDiccionario = 80383; //Cantidad de palabras del diccionario;

            //posiciones  de palabras las palabras a obtendre del diccionario
            ArrayList numeros = this.generarPosicionesAleatoriasDelDiccionario(cantPalabrasDelDiccionario, cantPalabras);

            ArrayList<String> palabras = new ArrayList<String>();

            //Directorio donde esta el diccionario de palabras
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/static/palabras.txt"));

            Integer linea = 1; //cantidad de lineas
            String sCadena = "";
            while ((sCadena = bf.readLine())!=null) {
                if(numeros.contains(linea)){
                    palabras.add(sCadena);
                }
                linea++;
            }
            return palabras;

        }catch (FileNotFoundException e){
            System.out.println("Error: Fichero no encontrado");
            System.out.println(e.getMessage());
        }catch(Exception e) {
            System.out.println("Error de lectura del fichero");
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Coloca palabras en la {@link SopaDePalabra}
     * @param palabrasDeLaSopa {@link ArrayList}<{@link String}>
     * @param sopa {@link SopaDePalabra}
     * @return {@link SopaDePalabra}
     */
    public SopaDePalabra colocarPalabrasEnLaSopa(ArrayList<String> palabrasDeLaSopa, SopaDePalabra sopa){

        for ( String palab : palabrasDeLaSopa) {

            boolean cabePalabra = false;
            do {
                //cordenada inicial para poner palabra
                String coordenada = this.generarCordenadaAleatoria(sopa.getAlto(), sopa.getAncho());
                DireccionPalabra direccionPalabra = this.generarDireccionAleatoria(sopa);

                cabePalabra = this.cabePalabra(coordenada, palab, sopa, direccionPalabra);
                if(cabePalabra){
                    this.escibirPalabraEnSopa(coordenada, palab, sopa, direccionPalabra);
                }
            }while(cabePalabra == false);
        }
        this.rellenarPalabrasEnPosicionesVacias(sopa);
        return sopa;
    }
}
