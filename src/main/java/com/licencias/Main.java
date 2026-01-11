package com.licencias;

import com.licencias.util.VentanaUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {

    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            primaryStage.setTitle("Sistema de Licencias - Acceso");
            primaryStage.setScene(scene);
            VentanaUtil.aplicarIcono(primaryStage);


            primaryStage.setResizable(false);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println(" Ocurrió un error crítico al iniciar la aplicación:");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        launch(args);
    }
}

