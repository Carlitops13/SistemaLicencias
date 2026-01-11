package com.licencias.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class VentanaUtil {
    public static void aplicarIcono(Stage stage) {
        try {

            stage.getIcons().add(new Image(VentanaUtil.class.getResourceAsStream("/images/licencia.png")));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
}