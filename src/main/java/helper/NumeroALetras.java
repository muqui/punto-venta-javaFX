/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumeroALetras {

    private static final String[] UNIDADES = {
        "", "uno", "dos", "tres", "cuatro", "cinco",
        "seis", "siete", "ocho", "nueve", "diez", "once",
        "doce", "trece", "catorce", "quince", "dieciséis",
        "diecisiete", "dieciocho", "diecinueve", "veinte"
    };

    private static final String[] DECENAS = {
        "", "", "veinti", "treinta", "cuarenta", "cincuenta",
        "sesenta", "setenta", "ochenta", "noventa"
    };

    private static final String[] CENTENAS = {
        "", "ciento", "doscientos", "trescientos", "cuatrocientos",
        "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"
    };

    public static String convertir(BigDecimal numero) {
        numero = numero.setScale(2, RoundingMode.DOWN);
        long parteEntera = numero.longValue();
        int centavos = numero.remainder(BigDecimal.ONE).movePointRight(2).intValue();

        if (parteEntera == 0) {
            return "cero pesos";
        }

        String resultado = convertirNumero(parteEntera) + " pesos";

        // Si quieres agregar centavos, descomenta esta línea:
         resultado += " con " + centavos + "/100 M.N.";

        return resultado;
    }

    private static String convertirNumero(long numero) {
        if (numero == 0) return "";
        if (numero < 21) return UNIDADES[(int) numero];
        if (numero < 100) {
            if (numero <= 29) {
                return DECENAS[(int) numero / 10] + UNIDADES[(int) numero % 10];
            } else {
                return DECENAS[(int) numero / 10] + ((numero % 10 != 0) ? " y " + UNIDADES[(int) numero % 10] : "");
            }
        }
        if (numero < 1000) {
            if (numero == 100) return "cien";
            return CENTENAS[(int) numero / 100] + " " + convertirNumero(numero % 100);
        }
        if (numero < 1_000_000) {
            long miles = numero / 1000;
            long resto = numero % 1000;
            String milesTexto = (miles == 1) ? "mil" : convertirNumero(miles) + " mil";
            return milesTexto + ((resto > 0) ? " " + convertirNumero(resto) : "");
        }
        if (numero < 1_000_000_000) {
            long millones = numero / 1_000_000;
            long resto = numero % 1_000_000;
            String millonesTexto = (millones == 1) ? "un millón" : convertirNumero(millones) + " millones";
            return millonesTexto + ((resto > 0) ? " " + convertirNumero(resto) : "");
        }
        return "Número demasiado grande";
    }
}
