package com.perfect.bank.classes;

import java.io.*;

/*
      Les méthodes de lire bloque le programme jusqu'à une entrée valide
*/
public class Read {

    /*
     * Lit un string
     * return {String}
     */
    public static String rString() {
        boolean error = false;
        String tmp = "";
        char C = '\0';
        do {
            try {
                while ((C = (char) System.in.read()) != '\n') {

                    if (C != '\r')
                        tmp = tmp + C;

                }
                error = false;
            } catch (IOException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer une chaîne de caractère : ");
                error = true;
            }
        } while (error);
        return tmp;
    } // fin de S()

    public static byte rByte() { // Lire un entier byte
        byte x = 0;
        boolean error = false;
        do {
            try {
                x = Byte.parseByte(rString());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un entier byte : ");
                error = true;
            }
        } while (error);
        return x;
    }

    public static short rShort() { // Lire un entier short
        short x = 0;
        boolean error = false;
        do {
            try {
                x = Short.parseShort(rString());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un entier short : ");
                error = true;
            }
        } while (error);
        return x;
    }

    public static int rInt() { // Lire un entier
        int x = 0;
        boolean error = true;
        do {
            try {
                x = Integer.parseInt(rString());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un entier : ");
                error = true;
            }
        } while (error);
        return x;
    }

    public static long rLong() { // Lire un entier long
        long x = 0;
        boolean error = false;
        do {
            try {
                x = Integer.parseInt(rString());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un entier long : ");
                error = true;
            }
        } while (error);
        return x;
    }

    public static double rDouble() { // Lire un double
        double x = 0.0;
        boolean error = false;
        do {
            try {
                x = Double.valueOf(rString()).doubleValue();
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un double : ");
                error = true;
            }
            return x;
        } while (error);
    }

    public static float rFloat() { // Lire un float
        float x = 0.0f;
        boolean error = false;
        do {
            try {
                x = Double.valueOf(rString()).floatValue();
                error = false;
            } catch (NumberFormatException e) {
                System.out.println("Erreur");
                System.out.println("Veuillez s'il vous plaît rentrer un float : ");
                error = true;
            }
        } while (error);
        return x;
    }

    public static char rChar() {// Lire un caractere
        String tmp = rString();
        if (tmp.length() == 0) {
            return '\n';
        } else {
            return tmp.charAt(0);
        }
    }

}
