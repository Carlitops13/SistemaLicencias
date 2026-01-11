package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class RequisitosController {

    @FXML private ListView<Tramite> lvPendientes; // Componente nuevo
    @FXML private TextField txtBusqueda;
    @FXML private Label lblNombreSolicitante;
    @FXML private CheckBox chkCertificadoMedico, chkPago, chkSinMultas;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnAprobar, btnRechazar, btnRegresar;

    private ITramiteDAO tramiteDAO;
    private Tramite tramiteActual;
    private ObservableList<Tramite> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tramiteDAO = new TramiteDAOImpl();


        lvPendientes.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tramite item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getSolicitante().getCedula() + " - " + item.getSolicitante().getNombre());


                    if ("REQUISITOS".equals(item.getEstado())) {
                        setStyle("-fx-background-color: #fab1a0; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
                    } else {

                        setStyle("");
                    }
                }
            }
        });


        lvPendientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarTramiteParaEditar(newVal.getSolicitante().getCedula());
            }
        });

        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> filtrarLista(newVal));
        deshabilitarFormulario(true);
        actualizarLista();
    }

    @FXML
    public void actualizarLista() {

        List<Tramite> todos = tramiteDAO.listarTodos();

        // Filtrar: (PENDIENTE)
        //  (REQUISITOS)
        List<Tramite> visibles = todos.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase("PENDIENTE") ||
                        t.getEstado().equalsIgnoreCase("REQUISITOS"))
                .collect(Collectors.toList());



        masterData.setAll(visibles);
        lvPendientes.setItems(masterData);
    }

    private void filtrarLista(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            lvPendientes.setItems(masterData);
        } else {
            ObservableList<Tramite> filtrada = masterData.filtered(t ->
                    t.getSolicitante().getCedula().contains(filtro) ||
                            t.getSolicitante().getNombre().toLowerCase().contains(filtro.toLowerCase())
            );
            lvPendientes.setItems(filtrada);
        }
    }

    private void cargarTramiteParaEditar(String cedula) {
        tramiteActual = tramiteDAO.buscarPorCedula(cedula);
        if (tramiteActual != null) {
            lblNombreSolicitante.setText("Solicitante: " + tramiteActual.getSolicitante().getNombre());
            chkCertificadoMedico.setSelected(tramiteActual.isReqMedico());
            chkPago.setSelected(tramiteActual.isReqPago());
            chkSinMultas.setSelected(tramiteActual.isReqMultas());
            txtObservaciones.setText(tramiteActual.getObservaciones());
            deshabilitarFormulario(false);
        }
    }



    @FXML
    public void aprobarRequisitos() {
        if (tramiteActual == null) return;
        if (!chkCertificadoMedico.isSelected() || !chkPago.isSelected() || !chkSinMultas.isSelected()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Incompleto", "Debe validar todos los requisitos.");
            return;
        }

        actualizarObjetoTramite("EN_EXAMENES");
        if (tramiteDAO.actualizarRequisitos(tramiteActual)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "¡Aprobado!", "El trámite pasó a fase de Exámenes.");
            actualizarLista();
            deshabilitarFormulario(true);
            lblNombreSolicitante.setText("Seleccione un registro");
        }
    }

    @FXML
    public void rechazarTramite() {
        if (tramiteActual == null) return;

        if (txtObservaciones.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Observación Necesaria",
                    "Debe explicar por qué no cumple (ej: Falta examen optométrico).");
            return;
        }


        actualizarObjetoTramite("REQUISITOS");

        if (tramiteDAO.actualizarRequisitos(tramiteActual)) {
            actualizarLista();
            deshabilitarFormulario(true);
            limpiarFormulario();
        }
    }
    @FXML
    private void limpiarFormulario() {
        lblNombreSolicitante.setText("Seleccione un registro");
        chkCertificadoMedico.setSelected(false);
        chkPago.setSelected(false);
        chkSinMultas.setSelected(false);
        txtObservaciones.clear();
        tramiteActual = null;
    }

    private void actualizarObjetoTramite(String nuevoEstado) {
        tramiteActual.setReqMedico(chkCertificadoMedico.isSelected());
        tramiteActual.setReqPago(chkPago.isSelected());
        tramiteActual.setReqMultas(chkSinMultas.isSelected());
        tramiteActual.setObservaciones(txtObservaciones.getText());
        tramiteActual.setEstado(nuevoEstado);
    }

    private void deshabilitarFormulario(boolean b) {
        chkCertificadoMedico.setDisable(b);
        chkPago.setDisable(b);
        chkSinMultas.setDisable(b);
        txtObservaciones.setEditable(!b);
        btnAprobar.setDisable(b);
        btnRechazar.setDisable(b);
    }

    @FXML public void cerrarVentana() { ((Stage) btnRegresar.getScene().getWindow()).close(); }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setContentText(mensaje); a.showAndWait();
    }
}