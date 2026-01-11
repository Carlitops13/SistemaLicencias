package com.licencias.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class Validador {


    public static void limitarCedula(TextField campo) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();


            if (!text.matches("\\d*")) {
                return null;
            }


            if (text.length() > 10) {
                return null;
            }

            return change;
        };

        campo.setTextFormatter(new TextFormatter<>(filter));
    }


    public static void limitarTelefono(TextField campo) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();
            if (text.matches("\\d*") && text.length() <= 10) { // 10 dígitos para celular convencional
                return change;
            }
            return null;
        };
        campo.setTextFormatter(new TextFormatter<>(filter));
    }

    public static boolean esCedulaValida(String cedula) {
        return cedula != null && cedula.length() == 10;
    }





    public static boolean esCorreoValido(String correo) {
        if (correo == null || correo.isEmpty()) return false;
        // Esta expresión verifica que haya texto + @ + texto + . + extensión
        return correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }


}