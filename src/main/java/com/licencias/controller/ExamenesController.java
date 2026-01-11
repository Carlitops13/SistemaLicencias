package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class ExamenesController {

    @FXML private TableView<Tramite> tblEspera;
    @FXML private TableColumn<Tramite, String> colCedula, colNombre;
    @FXML private TextField txtFiltro, txtNotaTeorica, txtNotaPractica;
    @FXML private Label lblNombreSolicitante, lblTipoLicencia, lblPromedio, lblEstado;
    @FXML private Button btnGuardar;

    private ITramiteDAO tramiteDAO;
    private ObservableList<Tramite> listaEspera = FXCollections.observableArrayList();
    private Tramite tramiteSeleccionado;

    @FXML
    public void initialize() {
        tramiteDAO = new TramiteDAOImpl();

        // Configurar columnas
        colCedula.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSolicitante().getCedula()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSolicitante().getNombre()));

        // --- ESTILO VISUAL PARA NOTAR LOS REPROBSDOS ---
        tblEspera.setRowFactory(tv -> new TableRow<Tramite>() {
            @Override
            protected void updateItem(Tramite item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("REPROBADO".equals(item.getEstado())) {
                    //COLOR ROJO
                    setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                } else {
                    setStyle("");
                }
            }
        });

        // Acción para seleccionar un registro
        tblEspera.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarSolicitante(newVal);
            }
        });

        // Filtro
        txtFiltro.textProperty().addListener((obs, old, newVal) -> filtrarLista(newVal));

        actualizarLista();
        configurarValidacionNotas(txtNotaTeorica);
        configurarValidacionNotas(txtNotaPractica);
    }


    @FXML
    public void actualizarLista() {
        // Ahora incluimos tanto a los nuevos como a los que fallaron
        listaEspera.setAll(tramiteDAO.listarTodos().stream()
                .filter(t -> "EN_EXAMENES".equals(t.getEstado()) || "REPROBADO".equals(t.getEstado()))
                .collect(Collectors.toList()));
        tblEspera.setItems(listaEspera);
    }

    private void cargarSolicitante(Tramite t) {
        this.tramiteSeleccionado = t;
        lblNombreSolicitante.setText(t.getSolicitante().getNombre());
        lblTipoLicencia.setText("Tipo: " + t.getTipoLicencia());

        // Habilita campos de las notas del exámen
        txtNotaTeorica.setDisable(false);
        txtNotaPractica.setDisable(false);
        btnGuardar.setDisable(false);

        txtNotaTeorica.requestFocus();
    }

    @FXML
    public void guardarNotas() {
        try {
            double n1 = Double.parseDouble(txtNotaTeorica.getText());
            double n2 = Double.parseDouble(txtNotaPractica.getText());
            double promedio = (n1 + n2) / 2.0;

            String resultado = (promedio >= 14.0) ? "APROBADO" : "REPROBADO";

            tramiteSeleccionado.setNotaTeorica(n1);
            tramiteSeleccionado.setNotaPractica(n2);
            tramiteSeleccionado.setEstado(resultado);

            if (tramiteDAO.actualizarNotas(tramiteSeleccionado)) {
                if (resultado.equals("APROBADO")) {
                    mostrarAlerta("¡Felicidades!", "El solicitante ha aprobado. Pasará a la bandeja de emisión de licencias.");
                } else {
                    mostrarAlerta("Resultado: REPROBADO", "El solicitante puede volver a intentar el examen según el reglamento.");
                }

                actualizarLista(); // Esto refresca la lista. Los APROBADOS se van, los REPROBADOS se quedan.
                limpiarFormulario();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Asegúrese de ingresar notas válidas.");
        }
    }

    private void filtrarLista(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tblEspera.setItems(listaEspera);
        } else {
            ObservableList<Tramite> filtrada = listaEspera.filtered(t ->
                    t.getSolicitante().getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                            t.getSolicitante().getCedula().contains(filtro));
            tblEspera.setItems(filtrada);
        }
    }

    private void limpiarFormulario() {
        lblNombreSolicitante.setText("Seleccione un solicitante");
        lblTipoLicencia.setText("Tipo: -");
        txtNotaTeorica.clear();
        txtNotaPractica.clear();
        txtNotaTeorica.setDisable(true);
        txtNotaPractica.setDisable(true);
        btnGuardar.setDisable(true);
    }

    private void mostrarAlerta(String titulo, String msj) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setContentText(msj);
        a.show();
    }

    private void configurarValidacionNotas(TextField textField) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    double nota = Double.parseDouble(newVal.replace(",", "."));
                    if (nota < 0 || nota > 20) {
                        textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                        btnGuardar.setDisable(true);
                    } else {
                        textField.setStyle("");
                        btnGuardar.setDisable(false);
                    }
                } catch (NumberFormatException e) {
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                    btnGuardar.setDisable(true);
                }
            }
        });
    }

    @FXML public void cerrarVentana() {
        ((Stage) lblEstado.getScene().getWindow()).close();
    }
}