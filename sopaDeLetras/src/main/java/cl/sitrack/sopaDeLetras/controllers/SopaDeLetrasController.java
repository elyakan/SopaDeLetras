package cl.sitrack.sopaDeLetras.controllers;

import cl.sitrack.sopaDeLetras.dto.CordenadasRequestDto;
import cl.sitrack.sopaDeLetras.dto.SopaResponseIdDto;
import cl.sitrack.sopaDeLetras.model.DireccionPalabra;
import cl.sitrack.sopaDeLetras.model.SopaDeLetras;
import cl.sitrack.sopaDeLetras.services.SopaDeLetrasService;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
//@RequestMapping("/api/v1/sopaDeLetras")
@RequestMapping("/alphabetSoup")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH})
public class SopaDeLetrasController {
    SopaDeLetrasService sopaDeLetrasService;

    public SopaDeLetrasController(SopaDeLetrasService sopaDeLetrasService) {
        this.sopaDeLetrasService = sopaDeLetrasService;
    }

    //The function receives a GET request, processes it and gives back a list of Todo as a response.
    @GetMapping
    public ResponseEntity<List<SopaDeLetras>> getAllSopaDeLetras() {
        List<SopaDeLetras> sopaDeLetrass = sopaDeLetrasService.getSopaDeLetras();
        return new ResponseEntity<>(sopaDeLetrass, HttpStatus.OK);
    }

    //The function receives a GET request with id in the url path, processes it and returns a SopaDeLetras with the specified Id
    @GetMapping({"/{id}", "application/json"})
    public ResponseEntity getSopaDeLetras(@PathVariable("id") UUID id) {
        System.out.println("*******************************");
        System.out.println("UUID id: "+ id );
        Optional<SopaDeLetras> sp;
        sp = sopaDeLetrasService.getSopaDeLetrasById(id);
        if (ObjectUtils.isEmpty(sp)) {
            String str = "{\"error\":\"registro no encontrado\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());
        }
        return new ResponseEntity (sopaDeLetrasService.getSopaDeLetrasById(id), HttpStatus.OK);
    }

    //palabras ya encontradas en una sopa
    @GetMapping({"/view/{id}", "application/json"})
    public ResponseEntity getSopaDeLetrasPalabra(@PathVariable("id") UUID id) {
        System.out.println("*******************************");
        System.out.println("UUID id: "+ id );
        Optional<SopaDeLetras> sp;
        sp = sopaDeLetrasService.getSopaDeLetrasById(id);
        if (ObjectUtils.isEmpty(sp)) {
            String str = "{\"error\":\"registro no encontrado\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());
        }
        return new ResponseEntity ( sopaDeLetrasService.getSopaDeLetrasById(id) , HttpStatus.OK );

    }

    //lista de palabras en una sopa
    @GetMapping({"/list/{id}", "application/json"})
    public ResponseEntity getSopaDeLetrasPalabras(@PathVariable("id") UUID id) {
        System.out.println("*******************************");
        System.out.println("UUID id: "+ id );
        Optional<SopaDeLetras> sp;
        sp = sopaDeLetrasService.getSopaDeLetrasById(id);
        if (ObjectUtils.isEmpty(sp)) {
            String str = "{\"error\":\"registro no encontrado\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());
        }
        return new ResponseEntity (sopaDeLetrasService.getSopaDeLetrasById(id), HttpStatus.OK);


    }

    //The function receives a POST request, processes it, creates a new SopaDeLetras and saves it to the database and returns a resource link to the created SopaDeLetras.
    @PostMapping
    public ResponseEntity saveSopaDeLetras(@RequestBody SopaDeLetras sopaDeLetras) {
        try {
            System.out.println("*******************************");
            System.out.println("sopaDeLetras: "+ sopaDeLetras );

            SopaDeLetras sopaDeLetras1 = sopaDeLetrasService.insert(sopaDeLetras);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("sopaDeLetras", "/alphabetSoup/" + sopaDeLetras1.getId());

            SopaResponseIdDto sopaResponseIdDtoResponse = new SopaResponseIdDto();
            sopaResponseIdDtoResponse.setId(sopaDeLetras1.getId());

            return new ResponseEntity<>(sopaResponseIdDtoResponse, httpHeaders, HttpStatus.CREATED);
        }
        catch (Exception e){
            String str = "{\"error\":\""+e.getMessage()+"\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());
        }
    }


    //The function receives a PUT request, updates the SopaDeLetras with the specified Id and returns the updated SopaDeLetras
/*
    @PutMapping({"/{id}"})
    public ResponseEntity<SopaDeLetras> updateSopaDeLetras(@PathVariable("id") UUID id, @RequestBody SopaDeLetras sopaDeLetras) {
        sopaDeLetrasService.updateSopaDeLetras(id, sopaDeLetras);
        return new ResponseEntity (sopaDeLetrasService.getSopaDeLetrasById(id), HttpStatus.OK);
    }
*/
    /**
     * Indicar que se ha encontrado una palabra en una {@link SopaDeLetras} segun el {@link String} id parasado por parametros
     * y el {@link CordenadasRequestDto} coordenadas
     * @param id {@uuid UUID}
     * @param cordenadasRequestDto {@link CordenadasRequestDto}
     * @return {@sopaDeLetrasService sopaDeLetrasService}
     */

    @PutMapping({"/{id}"})
    public ResponseEntity updatePalabraEncontrada(@PathVariable("id") UUID id, @RequestBody CordenadasRequestDto cordenadasRequestDto) throws Exception {

        Optional<SopaDeLetras> sopaDeLetras;
        sopaDeLetras = sopaDeLetrasService.getSopaDeLetrasById(id);

        String conrdenada_inicial = cordenadasRequestDto.getSr().toString()+"-"+cordenadasRequestDto.getSc();
        String conrdenada_final = cordenadasRequestDto.getEr().toString()+"-"+cordenadasRequestDto.getEc();
        System.out.println("*******************************");
        System.out.println("conrdenada_inicial: "+ conrdenada_inicial );
        System.out.println("conrdenada_final: "+ conrdenada_final );

        //valida coordenadas inicial
        while (!coordenadasValidas(sopaDeLetras, conrdenada_inicial)){
            System.out.print("Coordenada inválida") ;
            String str = "{\"error\":\"Coordenada inicial inválida\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());

        }
        //valida coordenada final
        while (!coordenadasValidas(sopaDeLetras, conrdenada_final)){
            System.out.print("Coordenada final inválida") ;
            String str = "{\"error\":\"Coordenada final inválida\"}";
            JSONObject jsonObject = new JSONObject(str);
            return ResponseEntity.badRequest().body(jsonObject.toMap());

        }

        String palabra = indicarPalabraEncontrada(sopaDeLetras, conrdenada_inicial, conrdenada_final, false);
        if(!palabra.equals("palabra no encontrada")){
            palabra = indicarPalabraEncontrada(sopaDeLetras, conrdenada_inicial, conrdenada_final, true);
        }

        //sopaDeLetrasService.updateSopaDeLetras(id, sopaDeLetras);
        return new ResponseEntity (sopaDeLetras, HttpStatus.OK);
    }
    /**
     * Busca y devuelve una palabra en la {@link SopaDeLetras}. Si la encuenla la convierte a mayuscula.
     * @param sopa {@link SopaDeLetras}
     * @param coordenadaInicial {@link String} coordenada inicial
     * @param coordenadaFinal {@link String} coordenada final
     * @param palabra_encontrada {@link Boolean} condició para definir si vuelve a ejecutar el código para convertir la palabra a mayuscula
     * @return {@link String} palabra encontrada o mensaje de no encontrada
     * @throws Exception
     */
    public String indicarPalabraEncontrada(Optional<SopaDeLetras> sopa, String coordenadaInicial, String coordenadaFinal, boolean palabra_encontrada) throws Exception{

        if(!obtenerDireccionCordenadas(sopa, coordenadaInicial, coordenadaFinal))
            throw new Exception("Coordenadas invalidas");

        HashMap<String, Integer> cordenadas = obtenerCordenadas(coordenadaInicial, coordenadaFinal);

        String palabra = null;
        //buscar de isquierda a derecha
        if(sopa.get().getDireccionPalabra().equals(DireccionPalabra.HOR_IZQ_DER)){
            palabra = buscarIzqDer(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"), cordenadas.get("cFinal"), palabra_encontrada);
        }

        //buscar de derecha a izquierda
        if(sopa.get().getDireccionPalabra().equals(DireccionPalabra.HOR_DER_IZQ)){
            palabra = buscarDerIzq(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"),  cordenadas.get("cFinal"), palabra_encontrada);
        }

        //buscar de arriba a abajo
        if(sopa.get().getDireccionPalabra().equals(DireccionPalabra.VERT_UP_DOWN)){
            palabra = buscarArribaAbajo(sopa, cordenadas.get("cInicial"), cordenadas.get("fInicial"), cordenadas.get("fFinal"), palabra_encontrada);
        }
        //buscar de abajo a arriba
        if(sopa.get().getDireccionPalabra().equals(DireccionPalabra.VERT_DOWN_UP)){
            palabra = buscarAbajoAArriba(sopa, cordenadas.get("cInicial"), cordenadas.get("fInicial"), cordenadas.get("fFinal"), palabra_encontrada);
        }

        if( sopa.get().getDireccionPalabra().toString().indexOf("DIAG") != -1 ){
            palabra = buscarDiagonal(sopa, cordenadas.get("fInicial"), cordenadas.get("cInicial"), cordenadas.get("fFinal"), cordenadas.get("cFinal"), palabra_encontrada);
        }

        String msj = "palabra no encontrada";
        if(sopa.get().getPalabrasDeLaSopa().contains(palabra) && !sopa.get().getPalabrasEncontradas().contains(palabra)){
            sopa.get().setPalabrasEncontradas(sopa.get().getPalabrasEncontradas()+palabra) ;
            msj = "La palabra "+ palabra + " es correcta";
        }else{
            if(sopa.get().getPalabrasDeLaSopa().contains(palabra) && palabra_encontrada){
                msj = "La palabra "+ palabra + " es correcta";
            }
        }
        return msj;
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la horizontal de la {@link SopaDeLetras} de izquierda a derecha.
     * @param sopa {@link SopaDeLetras} sopa de palabra
     * @param fInicial {@link Integer} fila coordenada inicial
     * @param cInicial {@link Integer} columna coordenada final
     * @param cFinal {@link Integer} columna coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarIzqDer(Optional<SopaDeLetras> sopa, Integer fInicial, Integer cInicial, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        /*
        if(sopa.get().isLtr()){
            String palabra = "";
            for(int i = 0; i < sopa.get().getSopa().size(); i++ ){
                ArrayList<String> arrayLetras = sopa.get().getSopa().get(i);
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
        */
        throw new Exception("No puede buscar palabras de izquierda a derecha.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la horizontal de la {@link SopaDeLetras} de derecha a izquierda.
     * @param sopa {@link SopaDeLetras}
     * @param fInicial {@link Integer} fila coordenada inicial
     * @param cInicial {@link Integer} columna coordenada final
     * @param cFinal {@link Integer} columna coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDerIzq(Optional<SopaDeLetras> sopa, Integer fInicial, Integer cInicial, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras de derecha a izquierda. ");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la vertical de la {@link SopaDeLetras} de arriba hacia abajo.
     * @param sopa {@link SopaDeLetras}
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarArribaAbajo(Optional<SopaDeLetras> sopa, Integer cInicial, Integer fInicial, Integer fFinal, boolean palabra_encontrada ) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras de arriba hacia abajo.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la vertical de la {@link SopaDeLetras} de abajo hacia arriba.
     * @param sopa {@link SopaDeLetras}
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarAbajoAArriba(Optional<SopaDeLetras> sopa, Integer cInicial, Integer fInicial, Integer fFinal, boolean palabra_encontrada ) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras de abajo hacia arriba.");
    }

    /**
     * LLama el método correspondiente diagonal a visualizar segun direccion de las cordenadas
     * @param sopa {@link SopaDeLetras}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonal(Optional<SopaDeLetras> sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada ) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Visualiza y convierte a mayúscula una palabra en la diagonal de la {@link SopaDeLetras} de arriba hacia abajo y de izquierda a derecha.
     * @param sopa {@link SopaDeLetras}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalUpDownIzqDer(SopaDeLetras sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDeLetras} de abajo hacia arriba y de derecha a izquierda.
     * @param sopa {@link SopaDeLetras}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalDownUpDerIzq(SopaDeLetras sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        /*
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
        }*/
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDeLetras} de arriba hacia abajo y de derecha a izquierda.
     * @param sopa {@link SopaDeLetras}
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalUpDownDerIzq(SopaDeLetras sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras diagonales.");
    }

    /**
     * Devuelve y convierte a mayúscula una palabra en la diagonal de la {@link SopaDeLetras} de abajo hacia arriba y de izquierda a derecha.
     * @param sopa
     * @param fInicial {@link Integer} fila de la coordenada inicial
     * @param cInicial {@link Integer} columna coordenada inicial
     * @param fFinal {@link Integer} fila la coordenada final
     * @param cFinal {@link Integer} colummna de la coordenada final
     * @param palabra_encontrada {@link Boolean}, se usa para volver a recorrer el ciclo pero convietiendo la palabra encontrada a mayuscula
     * @return {@link String} palabra
     */
    public String buscarDiagonalDownUpIzqDer(SopaDeLetras sopa, Integer fInicial, Integer cInicial, Integer fFinal, Integer cFinal, boolean palabra_encontrada) throws Exception{
        /*
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
        */
        throw new Exception("No puede buscar palabras diagonales.");
    }

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

    public boolean obtenerDireccionCordenadas(Optional<SopaDeLetras> sopa, String cordenadaInicial, String coordenadaFinal){

        HashMap<String, Integer> coordenadas = this.obtenerCordenadas(cordenadaInicial, coordenadaFinal);

        //horizontal isq-der
        if( ( coordenadas.get("fInicial") >= 0 && coordenadas.get("fInicial") < sopa.get().getW() )
                && (coordenadas.get("fInicial") == coordenadas.get("fFinal"))
                && ( coordenadas.get("cInicial") < coordenadas.get("cFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.HOR_IZQ_DER);
            return true;
        }
        //horizontal der-izq
        if( ( coordenadas.get("fInicial") >= 0 && coordenadas.get("fInicial") < sopa.get().getW() )
                && (coordenadas.get("fInicial") == coordenadas.get("fFinal"))
                && ( coordenadas.get("cInicial") > coordenadas.get("cFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.HOR_DER_IZQ);
            return true;
        }

        //horizontal up-dow
        if( ( coordenadas.get("cInicial") >= 0 && coordenadas.get("cInicial") < sopa.get().getH() )
                && (coordenadas.get("cInicial") == coordenadas.get("cFinal"))
                && ( coordenadas.get("fFinal") > coordenadas.get("fInicial") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.VERT_UP_DOWN);
            return true;
        }

        //horizontal dow-up
        if( ( coordenadas.get("cInicial") >= 0 && coordenadas.get("cInicial") < sopa.get().getH() )
                && (coordenadas.get("cInicial") == coordenadas.get("cFinal"))
                && ( coordenadas.get("fFinal") < coordenadas.get("fInicial") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.VERT_DOWN_UP);
            return true;
        }

        //DIAGONAL
        //DIAGONAL-UP-DOWN-IZQ-DER
        if( ( coordenadas.get("fFinal") - coordenadas.get("fInicial") ) == ( coordenadas.get("cFinal") - coordenadas.get("cInicial"))
                && (coordenadas.get("cInicial") < coordenadas.get("cFinal") )
                && (coordenadas.get("fInicial") < coordenadas.get("fFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.DIAG_UP_DOWN_IZQ_DER);
            return true;
        }
        //DIAGONAL-UP-DOWN-DER-IZQ
        if( ( coordenadas.get("fFinal") - coordenadas.get("fInicial") ) == ( coordenadas.get("cInicial") - coordenadas.get("cFinal"))
                && (coordenadas.get("cInicial") > coordenadas.get("cFinal") )
                && (coordenadas.get("fInicial") < coordenadas.get("fFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.DIAG_UP_DOWN_DER_IZQ);
            return true;
        }
        //DIAGONAL-DOWN-UP-IZQ-DER
        if( ( coordenadas.get("fInicial") - coordenadas.get("fFinal") ) == ( coordenadas.get("cFinal") - coordenadas.get("cInicial"))
                && (coordenadas.get("fInicial") > coordenadas.get("fFinal") )
                && (coordenadas.get("cInicial") < coordenadas.get("cFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.DIAG_DOWN_UP_IZQ_DER);
            return true;
        }
        //DIAGONAL-DOWN-UP-DER-IZQ
        if( ( coordenadas.get("fInicial") - coordenadas.get("fFinal") ) == ( coordenadas.get("cInicial") - coordenadas.get("cFinal"))
                && (coordenadas.get("fInicial") > coordenadas.get("fFinal") )
                && (coordenadas.get("cInicial") > coordenadas.get("cFinal") ) ){
            sopa.get().setDireccionPalabra(DireccionPalabra.DIAG_DOWN_UP_DER_IZQ);
            return true;
        }

        return false;
    }
    /**
     * Chequa que una coordenada sea una coordenada válida de la {@link SopaDeLetras}
     * @param sopa {@link SopaDeLetras}
     * @param cordenada {@link String}
     * @return {@link Boolean}
     */
    public boolean coordenadasValidas(Optional<SopaDeLetras> sopa, String cordenada){
        String[] aCordenada = obtenerFilaColumDeCoordenada(cordenada);
        Integer inicial = Integer.valueOf(aCordenada[0]);
        Integer fnicial = Integer.valueOf(aCordenada[1]);

        if( ( inicial >= 0 && inicial <= Integer.valueOf(sopa.get().getH()) ) && fnicial >= 0 && fnicial <= Integer.valueOf(sopa.get().getW()) )
            return true;
        else
            return false;
    }
    public String[] obtenerFilaColumDeCoordenada(String cordenada){
        String[] aCordenada = cordenada.split("-");
        return aCordenada;
    }

    //The function receives a DELETE request, deletes the SopaDeLetras with the specified Id.
    @DeleteMapping({"/{id}"})
    public ResponseEntity<SopaDeLetras> deleteSopaDeLetras(@PathVariable("id") UUID id) {
        sopaDeLetrasService.deleteSopaDeLetras(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



