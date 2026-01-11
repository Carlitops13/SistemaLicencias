package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import com.licencias.util.Validador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DetalleTramiteController {

    // --- COMPONENTES TABLA LATERAL ---
    @FXML private TableView<Tramite> tblTramites;
    @FXML private TableColumn<Tramite, String> colCedula;
    @FXML private TableColumn<Tramite, String> colNombre;
    @FXML private TableColumn<Tramite, String> colEstado;
    @FXML private TextField txtFiltroRapido;

    private ObservableList<Tramite> masterData = FXCollections.observableArrayList();

    // --- DETALLE ---
    @FXML private Label lblNombre, lblCedula, lblEmail, lblEstado, lblTipoLicencia;
    @FXML private Label lblReqMedico, lblReqPago, lblReqMultas, lblObsRequisitos;
    @FXML private Label lblNotaTeorica, lblNotaPractica, lblNumLicencia;


    @FXML private Label lblFechasLicencia;

    @FXML private Button btnGenerarLicencia;

    private ITramiteDAO tramiteDAO;
// Método Para inicializar
    @FXML
    public void initialize() {
        tramiteDAO = new TramiteDAOImpl();

        // 1. Configurar columnas de la tabla lateral
        colCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSolicitante().getCedula()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSolicitante().getNombre()));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        tblTramites.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                System.out.println("DEBUG: Seleccionado en tabla -> " + newVal.getSolicitante().getNombre());

                Tramite completo = tramiteDAO.buscarPorCedula(newVal.getSolicitante().getCedula());

                if (completo != null) {
                    System.out.println("DEBUG: Datos encontrados en BD. Estado: " + completo.getEstado());
                    actualizarInterfaz(completo);
                } else {
                    System.out.println("DEBUG: ERROR - El DAO devolvió NULL para la cédula: " + newVal.getSolicitante().getCedula());
                }
            }
        });

        //  Filtro para los datos
        if (txtFiltroRapido != null) {
            Validador.limitarCedula(txtFiltroRapido);
            txtFiltroRapido.textProperty().addListener((obs, oldVal, newVal) -> {
                filtrarDatos(newVal);
            });
        }

        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        List<Tramite> lista = tramiteDAO.listarTodos();
        masterData.setAll(lista);
        tblTramites.setItems(masterData);
    }

    private void filtrarDatos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tblTramites.setItems(masterData);
        } else {
            ObservableList<Tramite> filtrada = FXCollections.observableArrayList();
            for (Tramite t : masterData) {
                if (t.getSolicitante().getCedula().contains(filtro) ||
                        t.getSolicitante().getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                    filtrada.add(t);
                }
            }
            tblTramites.setItems(filtrada);
        }
    }

    private void actualizarInterfaz(Tramite t) {
        if (t == null) return;

        // --- DATOS PERSONALES  ---
        lblNombre.setText(t.getSolicitante().getNombre());
        lblCedula.setText("Cédula: " + t.getSolicitante().getCedula());
        lblEmail.setText("Correo: " + (t.getSolicitante().getCorreo() == null ? "No registrado" : t.getSolicitante().getCorreo()));
        lblTipoLicencia.setText("TIPO: " + t.getTipoLicencia());

        // --- ESTADO Y COLOR ---
        lblEstado.setText(t.getEstado().toUpperCase());
        aplicarColorEstado(t.getEstado());

        // --- REQUISITOS  ---
        lblReqMedico.setText(t.isReqMedico() ? "✓ COMPLETADO" : "✗ PENDIENTE");
        lblReqPago.setText(t.isReqPago() ? "✓ COMPLETADO" : "✗ PENDIENTE");
        lblReqMultas.setText(t.isReqMultas() ? "✓ COMPLETADO" : "✗ PENDIENTE");
        lblObsRequisitos.setText("Obs: " + (t.getObservaciones() == null ? "Sin observaciones" : t.getObservaciones()));

        // --- NOTAS ---
        lblNotaTeorica.setText(String.format("%.2f", t.getNotaTeorica()));
        lblNotaPractica.setText(String.format("%.2f", t.getNotaPractica()));

        // --- LOGICA PANEL LICENCIA ---
        if ("LICENCIA_EMITIDA".equals(t.getEstado())) {
            lblNumLicencia.setText("Licencia: YA FUE EMITIDA");
            lblFechasLicencia.setText("El documento ya fue entregado al solicitante.");
            btnGenerarLicencia.setDisable(true);
        } else if ("APROBADO".equals(t.getEstado())) {
            lblNumLicencia.setText("Licencia: LISTA PARA EMITIR");
            lblFechasLicencia.setText("Todos los requisitos están completos.");
            btnGenerarLicencia.setDisable(false);
        } else {
            lblNumLicencia.setText("Licencia: REQUISITOS INCOMPLETOS");
            lblFechasLicencia.setText("Estado actual: " + t.getEstado());
            btnGenerarLicencia.setDisable(true);
        }
    }

    private void aplicarColorEstado(String estado) {
        String color = switch (estado) {
            case "APROBADO" -> "#27ae60";
            case "REPROBADO", "RECHAZADO" -> "#e74c3c";
            case "EN_EXAMENES" -> "#f39c12";
            default -> "#7f8c8d";
        };
        lblEstado.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 3 8; -fx-background-radius: 5; -fx-font-weight: bold;");
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) lblNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void abrirGeneradorLicencia() {
        Tramite seleccionado = tblTramites.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/generar_licencia.fxml"));
            Parent root = loader.load();
            LicenciaController controller = loader.getController();
            controller.cargarTramiteDesdeExterno(seleccionado.getSolicitante().getCedula());
            Stage stage = new Stage();
            stage.setTitle("Emisión de Licencia");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al cargar FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String msj) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msj);
        a.show();
    }
}