package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteController {

    @FXML private DatePicker dpInicio, dpFin;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextField txtCedulaFiltro;
    @FXML private Label lblTotalTramites, lblTotalEmitidas;

    @FXML private TableView<Tramite> tblReporte;
    @FXML private TableColumn<Tramite, String> colFecha, colCedula, colNombre, colTipo, colEstado;

    private ITramiteDAO tramiteDAO = new TramiteDAOImpl();
    private ObservableList<Tramite> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Todos", "PENDIENTE", "REQUISITOS", "EN_EXAMENES", "APROBADO", "REPROBADO", "LICENCIA_EMITIDA"
        ));
        cmbEstado.setValue("Todos");

        configurarColumnas();
        cargarDatosBase();
    }

    private void configurarColumnas() {
        colFecha.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getFechaSolicitud().toLocalDate().toString()));
        colCedula.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSolicitante().getCedula()));
        colNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSolicitante().getNombre()));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoLicencia"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarDatosBase() {
        masterData.setAll(tramiteDAO.listarTodos());
        tblReporte.setItems(masterData);
        actualizarTotales(masterData);
    }

    @FXML
    private void buscarReporte() {
        List<Tramite> filtrados = masterData.stream()
                .filter(this::filtrarPorFecha)
                .filter(this::filtrarPorEstado)
                .filter(this::filtrarPorCedula)
                .collect(Collectors.toList());

        tblReporte.setItems(FXCollections.observableArrayList(filtrados));
        actualizarTotales(filtrados);
    }



    @FXML
    private void exportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Básico");
        fileChooser.setInitialFileName("reporte_licencias_simple_" + LocalDate.now() + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));

        File file = fileChooser.showSaveDialog(tblReporte.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Cabecera simplificada
                writer.println("Fecha;Cedula;Solicitante;Tipo;Estado");

                for (Tramite t : tblReporte.getItems()) {
                    // Solo datos básicos
                    writer.println(String.format("%s;%s;%s;%s;%s",
                            t.getFechaSolicitud().toLocalDate(),
                            t.getSolicitante().getCedula(),
                            t.getSolicitante().getNombre(),
                            t.getTipoLicencia(),
                            t.getEstado()
                    ));
                }

                mostrarAlerta("Éxito", "Reporte básico exportado correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private boolean filtrarPorFecha(Tramite t) {
        LocalDate inicio = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();
        LocalDate f = t.getFechaSolicitud().toLocalDate();
        return (inicio == null || !f.isBefore(inicio)) && (fin == null || !f.isAfter(fin));
    }

    private boolean filtrarPorEstado(Tramite t) {
        return cmbEstado.getValue().equals("Todos") || t.getEstado().equals(cmbEstado.getValue());
    }

    private boolean filtrarPorCedula(Tramite t) {
        return txtCedulaFiltro.getText().isEmpty() || t.getSolicitante().getCedula().contains(txtCedulaFiltro.getText());
    }

    private void actualizarTotales(List<Tramite> lista) {
        lblTotalTramites.setText(String.valueOf(lista.size()));
        lblTotalEmitidas.setText(String.valueOf(lista.stream().filter(t -> t.getEstado().equals("LICENCIA_EMITIDA")).count()));
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setContentText(msg);
        a.showAndWait();
    }
}