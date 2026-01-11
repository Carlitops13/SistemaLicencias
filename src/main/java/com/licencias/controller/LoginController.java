package com.licencias.controller;

import com.licencias.dao.IUsuarioDAO;
import com.licencias.dao.impl.UsuarioDAOImpl;
import com.licencias.model.Usuario;
import com.licencias.service.LoginService;
import com.licencias.util.VentanaUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRol;
    @FXML private Button btnLogin;

    private LoginService loginService;
    private IUsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        loginService = new LoginService();
        usuarioDAO = new UsuarioDAOImpl();
        cmbRol.getItems().addAll("ADMIN", "ANALISTA");
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String username = txtUsuario.getText();
        String password = txtPassword.getText();
        String rolSeleccionado = cmbRol.getValue();

        if (username.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Complete todos los datos.");
            return;
        }

        Usuario usuario = loginService.encontrarUsuario(username);

        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El usuario no existe.");
            return;
        }

        if (!usuario.isEstado()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Cuenta Bloqueada",
                    "Su cuenta está desactivada. Contacte al administrador.");
            return;
        }

        if (loginService.verificarPassword(password, usuario.getPassword())) {

            if (!usuario.getRol().equals(rolSeleccionado)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Rol", "No tiene permisos para este rol.");
                return;
            }

            usuario.setIntentosFallidos(0);


            if (!usuarioDAO.actualizarIntentos(usuario)) {
                System.err.println("Error al resetear intentos en la base de datos.");
            }

            abrirMenuPrincipal(usuario.getRol());

        } else {

            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);
// bloqueo
            if (intentos >= 3) {
                usuario.setEstado(false);


                if (usuarioDAO.actualizar(usuario)) {
                    mostrarAlerta(Alert.AlertType.ERROR, "BLOQUEO DE SEGURIDAD",
                            "Has superado los 3 intentos. Su cuenta ha sido bloqueada. Contacte al administrador.");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo bloquear la cuenta en la base de datos.");
                }
            } else {

                if (usuarioDAO.actualizarIntentos(usuario)) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Clave Incorrecta",
                            "Intento " + intentos + " de 3. Al tercero se bloqueará la cuenta.");
                }
            }
        }
    }

    private void abrirMenuPrincipal(String rol) {
        try {
            String rutaFXML = rol.equals("ADMIN") ? "/view/menu_admin.fxml" : "/view/menu_analista.fxml";
            String titulo = rol.equals("ADMIN") ? "Panel de Administrador" : "Panel de Analista";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
            double boundsWidth = screen.getVisualBounds().getWidth();
            double boundsHeight = screen.getVisualBounds().getHeight();

            Scene scene = new Scene(root, boundsWidth * 0.95, boundsHeight * 0.95);

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setMaximized(true);
            VentanaUtil.aplicarIcono(stage);

            Stage stageActual = (Stage) btnLogin.getScene().getWindow();
            stageActual.close();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar la ventana: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}