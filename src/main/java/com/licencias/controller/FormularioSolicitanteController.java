package com.licencias.controller;

import com.licencias.dao.ISolicitanteDAO;
import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.SolicitanteDAOImpl;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Solicitante;
import com.licencias.model.Tramite;
import com.licencias.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FormularioSolicitanteController {

    // --- ELEMENTOS VISUALES ---
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtFecha;


    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private DatePicker dpFechaNacimiento;

    @FXML private ComboBox<String> cmbTipoLicencia;
    @FXML private Button btnGuardar, btnLimpiar, btnRegresar;


    private ISolicitanteDAO solicitanteDAO;
    private ITramiteDAO tramiteDAO;

    @FXML
    public void initialize() {
        solicitanteDAO = new SolicitanteDAOImpl();
        tramiteDAO = new TramiteDAOImpl();

        // Validadores de texto
        com.licencias.util.Validador.limitarCedula(txtCedula);
        com.licencias.util.Validador.limitarTelefono(txtTelefono);
        txtCorreo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                txtCorreo.setText(newValue.replace(" ", ""));
            }
        });

        cmbTipoLicencia.getItems().addAll("A", "B", "C", "D", "E");
        txtFecha.setText(LocalDate.now().toString());


        dpFechaNacimiento.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date != null && date.isAfter(LocalDate.now().minusYears(18))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Opcional: ponerlas en rosado suave
                }
            }
        });


        dpFechaNacimiento.setValue(LocalDate.now().minusYears(20));

        dpFechaNacimiento.setEditable(true);
        dpFechaNacimiento.getEditor().setPromptText("dd/mm/aaaa");
    }

    @FXML
    public void guardarSolicitante() {

        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipoLicencia = cmbTipoLicencia.getValue();
        LocalDate nacimiento = dpFechaNacimiento.getValue();
        String correo = txtCorreo.getText().trim();

        // ---  CAMPOS VACÍOS ---
        if (cedula.isEmpty() || nombre.isEmpty() || tipoLicencia == null || nacimiento == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos",
                    "Por favor, complete todos los campos:\n" +
                            "- Cédula\n" +
                            "- Nombre completo\n" +
                            "- Tipo de Licencia\n" +
                            "- Fecha de Nacimiento");
            return;
        }
        if (!com.licencias.util.Validador.esCorreoValido(correo)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Correo Inválido",
                    "El formato del correo electrónico no es correcto.\nEjemplo: usuario@dominio.com");
            txtCorreo.requestFocus(); // Pone el foco en el error
            return;
        }

        // --- BLOQUEO DE DUPLICADOS  ---
        Solicitante solicitanteExistente = solicitanteDAO.buscarPorCedula(cedula);
        if (solicitanteExistente != null) {
            Tramite tramitePrevio = tramiteDAO.buscarPorCedula(cedula);
            if (tramitePrevio != null) {
                mostrarAlerta(Alert.AlertType.ERROR, "CIUDADANO YA REGISTRADO",
                        "La cédula " + cedula + " ya tiene un registro en el sistema.");
                return;
            }
        }
        //
        try {
            btnGuardar.setDisable(true);


            Solicitante solicitante;
            if (solicitanteExistente == null) {
                solicitante = new Solicitante();
                solicitante.setCedula(cedula);
                solicitante.setNombre(txtNombre.getText().toUpperCase());
                solicitante.setTelefono(txtTelefono.getText());
                solicitante.setCorreo(txtCorreo.getText());
                solicitante.setFechaNacimiento(dpFechaNacimiento.getValue());
                solicitanteDAO.crear(solicitante);

                solicitante = solicitanteDAO.buscarPorCedula(cedula);
            } else {
                solicitante = solicitanteExistente;
            }


            Tramite nuevo = new Tramite();
            nuevo.setSolicitante(solicitante);
            nuevo.setTipoLicencia(cmbTipoLicencia.getValue());

            Usuario admin = new Usuario();
            admin.setId(1);
            nuevo.setUsuario(admin);

            if (tramiteDAO.crear(nuevo)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Trámite inicial registrado correctamente.");
                limpiarFormulario();
            }

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo completar: " + e.getMessage());
        } finally {
            btnGuardar.setDisable(false);
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtNombre.clear();
        txtCedula.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        dpFechaNacimiento.setValue(null);
        cmbTipoLicencia.getSelectionModel().clearSelection();
    }




    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}