package com.aom.sopaDePalabra;

import com.aom.sopaDePalabra.clases.SopaDePalabra;
import com.aom.sopaDePalabra.clases.Sopas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class consoleCommandLineRunner implements CommandLineRunner {


    @Autowired
    Sopas sopas;

    @Autowired
    SopaDePalabra sopaDePalabra;

    @Override
    public void run(String... args) throws Exception {

        //sopaDePalabra.obtenerPalabrasAleatoriasDelDicconario(5);
        //sopaDePalabra.colocarPalabrasEnLaSopa();

        //System.out.println(sopaDePalabra.generarDireccionAleatoria());

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        this.printMenu();
        choice = scanner.nextInt();
        while (choice != 7){
            switch (choice){
                case 1:
                    System.out.println("Configuracion");

                    System.out.println(sopaDePalabra.getId());

                    System.out.println("");
                    System.out.println("Alto: " + sopaDePalabra.getAlto());
                    System.out.println("Ancho: " + sopaDePalabra.getAncho());
                    System.out.println("Buscar de izquierda a derecha: " + sopaDePalabra.isIzq_der());
                    System.out.println("Buscar de derecha a izquierda: " + sopaDePalabra.isDer_izq());
                    System.out.println("Buscar de arriba hacia abajo: " + sopaDePalabra.isUp_down());
                    System.out.println("Buscar de abajo hacia arriba: " + sopaDePalabra.isDown_up());
                    System.out.println("Buscar diagonal: " + sopaDePalabra.isDiagonal());
                    break;
                case 2:
                    System.out.println("Cambiar dimensiones de la sopa");

                    System.out.println("");
                    String msjEntradaAlto = "Entre el alto de sopa: ";
                    System.out.print(msjEntradaAlto);
                    Integer altoSopa = scanner.nextInt();
                    while ( altoSopa < 15 || altoSopa > 80){
                        System.out.println("El alto de la sopa debe de ser entre 15 y 80");

                        System.out.println("");
                        System.out.print(msjEntradaAlto);
                        altoSopa = scanner.nextInt();
                    }
                    this.sopaDePalabra.setAlto(altoSopa);

                    String msjEntradaAncho = "Entre el ancho de sopa: ";
                    System.out.print(msjEntradaAlto);
                    Integer anchoSopa = scanner.nextInt();
                    while ( anchoSopa < 15 || anchoSopa > 80){
                        System.out.println("El ancho de la sopa debe de ser entre 15 y 80");

                        System.out.println("");
                        System.out.print(msjEntradaAncho);
                        anchoSopa = scanner.nextInt();
                    }
                    this.sopaDePalabra.setAncho(anchoSopa);
                    //this.sopaDePalabra.llenarSopa();
                    break;
                case 3:
                    System.out.println("Cambiar configuracion de direccion de palabras");

                    System.out.println();
                    System.out.print("Poder buscar de Izquiera a dereda (1 - true) ( otro - false): ");
                    String entrada = scanner.next();
                    sopaDePalabra.setIzq_der(  (entrada == "") ? true : ( this.isNumeric(entrada) && Integer.parseInt(entrada) == 1 ) ? true : false  );

                    System.out.print("Poder buscar de derecha a izquierda (1 - true) ( otro - false): ");
                    entrada = scanner.next();
                    sopaDePalabra.setDer_izq(  (entrada == "") ? false : ( this.isNumeric(entrada) && Integer.parseInt(entrada) == 1 ) ? true : false  );

                    System.out.print("Poder buscar de arriba a abajo (1 - true) ( otro - false): ");
                    entrada = scanner.next();
                    sopaDePalabra.setUp_down(  (entrada == "") ? true : ( this.isNumeric(entrada) && Integer.parseInt(entrada) == 1 ) ? true : false  );

                    System.out.print("Poder buscar de abajo a arriba (1 - true) ( otro - false): ");
                    entrada = scanner.next();
                    sopaDePalabra.setDown_up(  (entrada == "") ? false : ( this.isNumeric(entrada) && Integer.parseInt(entrada) == 1 ) ? true : false  );

                    System.out.print("Poder buscar diagonal (1 - true) ( otro - false): ");
                    entrada = scanner.next();
                    sopaDePalabra.setDiagonal(  (entrada == "") ? false : ( this.isNumeric(entrada) && Integer.parseInt(entrada) == 1 ) ? true : false  );
                    break;
                case 4:
                    System.out.println("Listas de sopas");

                    System.out.println();
                    List<SopaDePalabra> listSopas = sopas.getListSopas();

                    Integer num = 1;
                    for (SopaDePalabra sopa: listSopas ) {
                        System.out.println( num+"- "+sopa.getId());
                        num++;
                    }
                    break;
                case 5:
                    System.out.println("Entra el número de la sopa que quiere encontrar palabras");
                    Integer numero_sopa = scanner.nextInt();

                    System.out.println("");
                    SopaDePalabra sopa = this.sopas.getSopaByPosicion(numero_sopa-1);
                    this.sopas.mostrarSopaEnConsola(sopa);

                    break;
                case 6:
                    System.out.println("Indicar palabra encontrada: ");

                    System.out.println("Entra el número de la sopa que quiere vizualizar");
                    Integer num_sopa = scanner.nextInt();
                    SopaDePalabra sopaADefinirCordenadas = this.sopas.getSopaByPosicion(num_sopa-1);

                    System.out.println("");
                    String mensajeCoordenadaInicial = "Entre coordenada inicial (f-c): ";
                    System.out.print(mensajeCoordenadaInicial);
                    String conrdenada_inicial = scanner.next();
                    while (!this.sopaDePalabra.coordenadasValidas(sopaADefinirCordenadas, conrdenada_inicial)){
                        System.out.print("Cordenada inválida, intente de nuevo") ;

                        System.out.println();
                        System.out.print(mensajeCoordenadaInicial);
                        conrdenada_inicial = scanner.next();
                    }

                    String mensajeCoordenadaFinal = "Entre coordenada final (f-c): ";
                    System.out.print(mensajeCoordenadaFinal);
                    String conrdenada_final = scanner.next();
                    while (!this.sopaDePalabra.coordenadasValidas(sopaADefinirCordenadas, conrdenada_final)){
                        System.out.print("Cordenada inválida, intente de nuevo") ;

                        System.out.println();
                        System.out.print(mensajeCoordenadaFinal);
                        conrdenada_final = scanner.next();
                    }

                    try{

                        String palabra = sopaDePalabra.indicarPalabraEncontrada(sopaADefinirCordenadas, conrdenada_inicial, conrdenada_final, false);
                        if(!palabra.equals("palabra no encontrada")){
                            palabra = sopaDePalabra.indicarPalabraEncontrada(sopaADefinirCordenadas, conrdenada_inicial, conrdenada_final, true);
                        }
                        System.out.println(palabra);
                        /*if(palabra.length() > 0 ){
                            System.out.println("Palabra: "+ palabra + " encontrada." );
                        }else{
                            System.out.println("Palabra no encontrada");
                        }*/

                    }catch (Exception e){
                        System.out.println("\n");
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Parametro invalido");
                    break;

            }

            this.printMenu();
            choice = scanner.nextInt();
        }
        System.out.println("Bye");

        /*System.out.print("OPCIONES:");
        System.out.println();



        System.out.print("Entre el alto de sopa: ");
        sopaDePalabra.setAlto(scanner.nextInt());
        System.out.print("Entre el ancho de sopa: ");
        sopaDePalabra.setAncho(scanner.nextInt());

        this.sopaDePalabra.llenarSopa();

        System.out.println("\n");
        System.out.println("El ato es " + sopaDePalabra.getAlto() + " y el ancho es " + sopaDePalabra.getAncho());

        this.sopaDePalabra.mostrarSopaEnConsola();

        System.out.println("\n");
        System.out.println("Indicar palabra encontrada: ");
        System.out.print("Entre coordenada inicial (f-c): ");
        sopaDePalabra.setCordenadaStart(scanner.next());
        System.out.print("Entre coordenada final (f-c): ");
        sopaDePalabra.setCordenadaFinish(scanner.next());

        try{
            sopaDePalabra.indicarPalabraEncontrada();
        }catch (Exception e){
            System.out.println("\n");
            System.out.println(e.getMessage());
        }


        System.out.println("\n");
        this.sopaDePalabra.mostrarSopaEnConsola();*/
    }

    public void printMenu(){
        System.out.println("\n");
        System.out.println("Opciones");
        System.out.println();
        System.out.println("Por favor seleccione la opción: \n"
                    + "1- Ver Configuracion inicial\n"
                    + "2- Cambiar dimensiones de la sopa \n"
                    + "3- Cambiar configuracion de direccion de palabras \n"
                    + "4- Ver listas de sopas \n"
                    + "5- Ver sopa de palabras \n"
                    + "6- Indicar cordenadas de palabra \n"
                    + "7- Salir \n");
    }

    public boolean isNumeric(String num){
        try {
            Integer.parseInt(num.toString());
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
