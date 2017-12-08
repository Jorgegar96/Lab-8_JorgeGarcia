/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.pkg1_jorgegarcia;

import java.util.Scanner;

/**
 *
 * @author JorgeLuis
 */
public class Lab8_JorgeGarcia {

    static Scanner sc = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        char resp = 's';
        do {
            String[] pieza = new String[2];
            boolean juego = true;
            boolean jugador = false; //jugador 1 => true, jugador 2 => false
            String[][] tablero = new String[12][12];
            tablero = llenarTablero(tablero);
            while (juego == true) {
                int xCoordenada = 0;
                int yCoordenada = 0;
                if (jugador == true) {
                    jugador = false;
                } else {
                    jugador = true;
                }
                //imprimirTablero(tablero, 0, 0);
                if (jugador == true) {
                    pieza[0] = "a";
                    pieza[1] = "0";
                } else {
                    pieza[0] = "d";
                    pieza[1] = "K";
                }
                boolean seleccion = false;
                boolean coor_correctas = false;
                boolean alrededor = false;
                while (seleccion == false || coor_correctas == false || alrededor == true) {
                    imprimirTablero(tablero, 0, 0);
                    String player = "";
                    if (jugador == true) {
                        player = "1";
                    } else {
                        player = "2";
                    }
                    System.out.println("Jugador " + player);
                    System.out.println("Ingrese las coordenadas validas "
                            + "de la pieza (no rodeada) que desea mover(x,y): "
                            + "(ej. 6,2 ) sin espacios");
                    String pieza_pos = sc.nextLine();
                    coor_correctas = coordenadasCorrectas(pieza_pos);
                    //System.out.println(coor_correctas);
                    if (coor_correctas == true) {
                        int[] coordenadas = new int[2];
                        String[] token = pieza_pos.split(",");
                        coordenadas = asignarNumeros(token);
                        xCoordenada = coordenadas[0];
                        yCoordenada = coordenadas[1];
                        seleccion = piezaPropia(xCoordenada, yCoordenada, pieza, tablero);
                        //System.out.println(seleccion);
                        if (seleccion == true) {
                            alrededor = piezaRodeada(xCoordenada, yCoordenada, tablero);
                            //System.out.println(alrededor);
                        }
                    }
                }
                String[][] tableroSeleccionado = new String[12][12];
                tableroSeleccionado = tableroTemp(tablero, xCoordenada, yCoordenada);
                //imprimirTablero(tableroSeleccionado, 0, 0);
                boolean casilla = true;
                boolean obstaculo = true;
                boolean movimiento = false;
                coor_correctas = false;
                while (casilla == true || obstaculo == true || coor_correctas == false || movimiento == false) {
                    imprimirTablero(tableroSeleccionado, 0, 0);
                    System.out.println("Ingrese las coordenadas validas "
                            + "de la casilla a la cual desea mover(x,y): "
                            + "(ej. 6,2 ) sin espacios (ingrese 0,0 "
                            + "para seleccionar otra pieza)");
                    String casilla_pos = sc.nextLine();
                    coor_correctas = coordenadasCorrectas(casilla_pos);
                    //System.out.println(coor_correctas);
                    if (coor_correctas == true) {
                        int[] coordenadas = new int[2];
                        String[] token = casilla_pos.split(",");
                        coordenadas = asignarNumeros(token);
                        int xCasilla = coordenadas[0];
                        int yCasilla = coordenadas[1];
                        casilla = casillaOcupada(xCasilla, yCasilla, tablero);
                        //System.out.println(casilla);
                        if (casilla == false) {
                            if (xCoordenada == xCasilla && yCoordenada == yCasilla) {
                                movimiento = false;
                            } else {
                                movimiento = movimientoCorrecto(xCoordenada, yCoordenada, xCasilla, yCasilla, tablero);
                            }
                            //System.out.println(movimiento);
                            if (movimiento == true) {
                                obstaculo = hayObstaculo(xCoordenada, yCoordenada, xCasilla, yCasilla, tablero);
                                //System.out.println(obstaculo);
                                if (obstaculo == false) {
                                    tablero = actualizar(tablero, xCoordenada, yCoordenada, xCasilla, yCasilla);
                                }
                            }
                        }
                    }
                    if (casilla_pos.equals("0,0")) {
                        casilla = false;
                        obstaculo = false;
                        coor_correctas = true;
                        movimiento = true;
                        if (jugador == true) {
                            jugador = false;
                        } else {
                            jugador = true;
                        }
                    }

                }
                tablero = comer(tablero, pieza); //metodo en proceso
                String[] defensores = {"K", "d"};
                String[] atacantes = {"a", "0"};
                tablero = comer(tablero, defensores);
                tablero = comer(tablero, atacantes);
                juego = enJuego(tablero);
            }
            System.out.println("Desea volver a jugar?");
            resp = sc.next().charAt(0);
        } while (resp == 's' || resp == 'S');
    }

    public static String[][] llenarTablero(String[][] tablero) {
        int coordenadas_hor = 1;
        int coordenadas_ver = 1;
        for (int x = 0; x < tablero.length; x++) {
            for (int y = 0; y < tablero.length; y++) {
                if (y == 0 && x > 0) {
                    tablero[x][y] = coordenadas_ver + "";
                    coordenadas_ver++;
                } else if (x == 0 && y > 0) {
                    tablero[x][y] = coordenadas_hor + "";
                    coordenadas_hor++;
                } else if ((x == 1 || x == tablero.length - 1) && (y > 3 && y < 9)) {
                    tablero[x][y] = "a";
                } else if ((y == 1 || y == tablero.length - 1) && (x > 3 && x < 9)) {
                    tablero[x][y] = "a";
                } else if ((x == 2 && y == 6) || (x == 6 && y == 2)
                        || (x == tablero.length - 2 && y == 6) || (x == 6
                        && y == tablero.length - 2)) {
                    tablero[x][y] = "a";
                } else if (x == 6 && (y > 2 && y < tablero.length - 2)
                        || y == 6 && (x > 2 && x < tablero.length - 2)) {
                    tablero[x][y] = "d";
                } else {
                    tablero[x][y] = "-";
                }
            }
        }
        tablero[6][6] = "K";
        return tablero;
    }

    public static void imprimirTablero(String[][] tablero, int posx, int posy) {

        if (posx == tablero.length - 1 && posy == tablero.length - 1) {
            System.out.print(tablero[posx][posy]);
            if (tablero[posx][posy].length() == 1) {
                System.out.print("    ");
            } else if (tablero[posx][posy].length() == 2) {
                System.out.print("   ");
            } else if (tablero[posx][posy].length() == 3) {
                System.out.print("  ");
            }
            System.out.println("");
        } else if (posy == tablero.length - 1) {
            System.out.print(tablero[posx][posy]);
            if (tablero[posx][posy].length() == 1) {
                System.out.print("    ");
            } else if (tablero[posx][posy].length() == 2) {
                System.out.print("   ");
            } else if (tablero[posx][posy].length() == 3) {
                System.out.print("  ");
            }
            System.out.println("");
            imprimirTablero(tablero, posx + 1, 0);
        } else {
            System.out.print(tablero[posx][posy]);
            if (tablero[posx][posy].length() == 1) {
                System.out.print("    ");
            } else if (tablero[posx][posy].length() == 2) {
                System.out.print("   ");
            } else if (tablero[posx][posy].length() == 3) {
                System.out.print("  ");
            }
            imprimirTablero(tablero, posx, posy + 1);
        }
        /*for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                if (tablero[i][j].length() == 1){
                    System.out.print("    ");
                }else{
                    System.out.print("   ");
                }
                System.out.print( tablero[i][j]);
                
            }
            System.out.println("");
        }*/
    }

    public static boolean coordenadasCorrectas(String pieza) {
        boolean val = false;//validacion 
        if (pieza.length() > 5) {
            return false;
        }
        int cont_coma = 0;
        for (int x = 0; x < pieza.length(); x++) {
            if (Character.isDigit(pieza.charAt(x)) || pieza.charAt(x) == ',') {
                val = true;
            } else if ((x == 0 || x == pieza.length() - 1) && pieza.charAt(x) == ',') {
                return false;
            }
            if (pieza.charAt(x) == ',') {
                cont_coma++;
            }
            if (cont_coma > 1) {
                return false;
            }
        }
        if (cont_coma == 0) {
            return false;
        } else {
            String[] token = pieza.split(",");
            if (token.length > 1) {
                try {
                    if (Integer.parseInt(token[0]) > 11 || Integer.parseInt(token[1]) > 11
                            || Integer.parseInt(token[0]) < 1 || Integer.parseInt(token[1]) < 1) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return val;
    }

    public static int[] asignarNumeros(String[] token) {
        int[] coordenadas = new int[2];
        try {
            if (token.length > 0) {
                coordenadas[0] = Integer.parseInt(token[0]);
                if (token.length > 1) {
                    coordenadas[1] = Integer.parseInt(token[1]);
                }else{
                    coordenadas[0] = 0;
                }
                return coordenadas;
            }else{
                
            }
        } catch (NumberFormatException e) {
            coordenadas[0] = 0;
            coordenadas[1] = 1;
        }
        return coordenadas;
    }

    public static boolean piezaPropia(int xCoordenada, int yCoordenada, String[] pieza, String[][] tablero) {
        boolean val = false; //validacion
        if (tablero[yCoordenada][xCoordenada].equals(pieza[0])
                || tablero[yCoordenada][xCoordenada].equals(pieza[1])) {
            val = true;
        } else {
            val = false;
        }
        return val;
    }

    public static boolean piezaRodeada(int xCoordenada, int yCoordenada, String[][] tablero) {
        boolean val = true;
        int arriba = yCoordenada + 1;
        int abajo = yCoordenada - 1;
        int derecha = xCoordenada + 1;
        int izquierda = xCoordenada - 1;
        if (abajo < tablero.length) {
            if (tablero[abajo][xCoordenada].charAt(0) == '-') {
                return false;
            }
        }
        if (derecha < tablero.length) {
            if (tablero[yCoordenada][derecha].charAt(0) == '-') {
                return false;
            }
        }
        if (tablero[arriba][xCoordenada].charAt(0) == '-') {
            return false;
        }
        if (tablero[yCoordenada][izquierda].charAt(0) == '-') {
            return false;
        }
        return val;
    }

    public static String[][] tableroTemp(String[][] tableroOrg, int xCoordenada, int yCoordenada) {
        String[][] tableroTemp = new String[12][12];
        for (int x = 0; x < tableroOrg.length; x++) {
            for (int y = 0; y < tableroOrg.length; y++) {
                tableroTemp[x][y] = tableroOrg[x][y];
                if (x == yCoordenada && y == xCoordenada) {
                    tableroTemp[x][y] = tableroTemp[x][y] + "}";
                } else if (y == xCoordenada - 1 && x == yCoordenada) {
                    tableroTemp[x][y] += "   {";
                }
            }
        }
        return tableroTemp;
    }

    public static boolean casillaOcupada(int xCoordenada, int yCoordenada, String[][] tablero) {
        if (tablero[yCoordenada][xCoordenada].equals("-") && xCoordenada != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean movimientoCorrecto(int xCoordenada, int yCoordenada, int xCasilla, int yCasilla, String[][] tablero) {
        int xDelta = xCoordenada - xCasilla;
        int yDelta = yCoordenada - yCasilla;
        if (xDelta == 0 || yDelta == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hayObstaculo(int xCoordenada, int yCoordenada, int xCasilla, int yCasilla, String[][] tablero) {
        boolean val = false;
        int xDelta = xCasilla - xCoordenada;
        int yDelta = yCasilla - yCoordenada;
        int movCasillax = 0, movCasillay = 0, sumx = 0, sumy = 0, lim = 0;
        if (xDelta < 0) {
            movCasillax = -1;
            lim = xDelta;
        } else if (xDelta > 0) {
            movCasillax = 1;
            lim = xDelta;
        } else {
            movCasillax = 0;
        }
        if (yDelta < 0) {
            movCasillay = -1;
            lim = yDelta;
        } else if (yDelta > 0) {
            movCasillay = 1;
            lim = yDelta;
        } else {
            movCasillay = 0;
        }
        sumx = movCasillax;
        sumy = movCasillay;
        for (int x = 0; x < Math.abs(lim); x++) {
            if (tablero[yCoordenada + movCasillay][xCoordenada + movCasillax].charAt(0) != '-') {
                return true;
            } else {
                movCasillax += sumx;
                movCasillay += sumy;
            }
        }
        return val;
    }

    public static String[][] actualizar(String[][] tablero, int xCoordenada, int yCoordenada, int xCasilla, int yCasilla) {
        String salvar = tablero[yCoordenada][xCoordenada];
        tablero[yCoordenada][xCoordenada] = "-";
        tablero[yCasilla][xCasilla] = salvar;
        return tablero;
    }

    public static String[][] comer(String[][] tablero, String[] pieza) {
        System.out.println("Se llamo al metodo comer para las piezas " + pieza[0] + " y " + pieza[1]);
        boolean comer = false;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                if (tablero[i][j].charAt(0) == pieza[0].charAt(0)
                        || tablero[i][j].charAt(0) == pieza[1].charAt(0)) {
                    //System.out.println("check ");
                    for (int x = 1; x < 5; x++) {
                        if (x + i < tablero.length) {
                            if (x == 1) {
                                if (tablero[i + x][j].charAt(0) != pieza[0].charAt(0)
                                        && tablero[i + x][j].charAt(0) != pieza[1].charAt(0)
                                        && tablero[i + x][j].charAt(0) != '-') {
                                    comer = true;
                                    //System.out.println(comer);
                                } else {
                                    break;
                                }
                            } else {
                                if (comer == true) {
                                    if (tablero[i + x][j].charAt(0) == pieza[0].charAt(0)
                                            || tablero[i + x][j].charAt(0) == pieza[1].charAt(0)) {
                                        for (int k = 1; k < x; k++) {
                                            tablero[i + k][j] = "-";
                                        }
                                        break;
                                    } else if (tablero[i + x][j].charAt(0) == '-') {
                                        break;
                                    } else if (tablero[i + x][j].charAt(0) != pieza[0].charAt(0)
                                            && tablero[i + x][j].charAt(0) != pieza[1].charAt(0)) {
                                        comer = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                if (tablero[i][j].charAt(0) == pieza[0].charAt(0)
                        || tablero[i][j].charAt(0) == pieza[1].charAt(0)) {
                    //System.out.println("check ");
                    for (int x = 1; x < 5; x++) {
                        if (x + j < tablero.length) {
                            if (x == 1) {
                                if (tablero[i][j + x].charAt(0) != pieza[0].charAt(0)
                                        && tablero[i][j + x].charAt(0) != pieza[1].charAt(0)
                                        && tablero[i][j + x].charAt(0) != '-') {
                                    comer = true;
                                    //System.out.println(comer);
                                } else {
                                    break;
                                }
                            } else {
                                if (comer == true) {
                                    if (tablero[i][j + x].charAt(0) == pieza[0].charAt(0)
                                            || tablero[i][j + x].charAt(0) == pieza[1].charAt(0)) {
                                        for (int k = 1; k < x; k++) {
                                            tablero[i][j + k] = "-";
                                        }
                                        break;
                                    } else if (tablero[i][j + x].charAt(0) == '-') {
                                        break;
                                    } else if (tablero[i][j + x].charAt(0) != pieza[0].charAt(0)
                                            && tablero[i][j + x].charAt(0) != pieza[1].charAt(0)) {
                                        comer = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tablero;
    }

    public static boolean enJuego(String[][] tablero) {
        boolean val = false;
        boolean hayRey = false;
        for (int x = 0; x < tablero.length; x++) {
            for (int y = 0; y < tablero.length; y++) {
                if (tablero[x][y].charAt(0) == 'K') {
                    hayRey = true;
                    if (x != 1 && x != tablero.length - 1 && y != 1
                            && y != tablero.length - 1) {
                        val = true;
                    } else if (x != 1 || x != tablero.length - 1 || y != 1
                            || y != tablero.length - 1) {
                        val = false;
                        imprimirTablero(tablero, 0, 0);
                        System.out.println("Enhorabuena jugador 2!");
                    }
                }
            }
        }
        if (hayRey == false) {
            imprimirTablero(tablero, 0, 0);
            System.out.println("Enhorabuena jugador 1!");
        }
        return val;
    }
}
