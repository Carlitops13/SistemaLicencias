package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.time.LocalDate;

public class LicenciaController {

    @FXML private TextField txtCedulaBusqueda;
    @FXML private HBox paneCarnet;
    @FXML private Button btnImprimir;

    @FXML private Label lblNumeroLicencia;
    @FXML private Label lblNombre;
    @FXML private Label lblTipo;
    @FXML private Label lblFechaVencimiento;

    private final ITramiteDAO tramiteDAO = new TramiteDAOImpl();
    private Tramite tramiteActual;

    @FXML
    public void buscarAprobado() {
        String cedula = txtCedulaBusqueda.getText().trim();
        if (cedula.isEmpty()) return;

        this.tramiteActual = tramiteDAO.buscarPorCedula(cedula);

        if (tramiteActual != null) {
            String estado = tramiteActual.getEstado();


            if ("APROBADO".equals(estado) || "LICENCIA_EMITIDA".equals(estado)) {

                lblNumeroLicencia.setText(tramiteActual.getSolicitante().getCedula());
                lblNombre.setText(tramiteActual.getSolicitante().getNombre().toUpperCase());
                lblTipo.setText(tramiteActual.getTipoLicencia());

                lblFechaVencimiento.setText(LocalDate.now().plusYears(5).toString());

                paneCarnet.setVisible(true);
                btnImprimir.setDisable(false);

                if ("LICENCIA_EMITIDA".equals(estado)) {
                    System.out.println("Modo: Reimpresión de duplicado");
                }
            } else {

                ocultarCarnet("El trámite aún está en estado: " + estado);
            }
        } else {
            ocultarCarnet("Solicitante no encontrado.");
        }
    }
    private void ocultarCarnet(String mensaje) {
        paneCarnet.setVisible(false);
        btnImprimir.setDisable(true);
        mostrarAlerta(mensaje);
    }

    @FXML
    public void imprimirLicencia() {
        if (tramiteActual == null) return;

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            // configuración de impresora
            boolean proceder = job.showPrintDialog(paneCarnet.getScene().getWindow());

            if (proceder) {

                prepararEscalaImpresion(job);


                boolean success = job.printPage(paneCarnet);

                if (success) {
                    job.endJob();


                    if ("APROBADO".equals(tramiteActual.getEstado())) {
                        if (tramiteDAO.finalizarTramite(tramiteActual.getId())) {
                            mostrarAlerta("Éxito: Licencia emitida y registrada en el sistema.");
                            cerrarVentana();
                        } else {
                            mostrarAlerta("Error: La licencia se imprimió pero no se pudo actualizar el estado en la BD.");
                        }
                    } else {
                        // Si  LICENCIA_EMITIDA, solo confirmamos la reimpresión
                        mostrarAlerta("Éxito: Duplicado de licencia impreso correctamente.");
                        cerrarVentana();
                    }
                } else {
                    mostrarAlerta("Error: Falló el proceso de impresión.");
                }


                limpiarTransformaciones();
            }
        }
    }

    private void prepararEscalaImpresion(PrinterJob job) {

        double printableWidth = job.getJobSettings().getPageLayout().getPrintableWidth();


        double nodeWidth = paneCarnet.getBoundsInLocal().getWidth();


        double scale = (printableWidth / nodeWidth) * 0.9;


        Scale escala = new Scale(scale, scale);
        paneCarnet.getTransforms().add(escala);


        double offsetX = (printableWidth - (nodeWidth * scale)) / 2;
        paneCarnet.setTranslateX(offsetX);
        paneCarnet.setTranslateY(20); // Margen superior pequeño
    }

    private void limpiarTransformaciones() {
        paneCarnet.getTransforms().clear();
        paneCarnet.setTranslateX(0);
        paneCarnet.setTranslateY(0);
    }

    @FXML
    public void cerrarVentana() {
        ((Stage) paneCarnet.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String msj) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msj);
        a.show();
    }

    public void cargarTramiteDesdeExterno(String cedula) {
        txtCedulaBusqueda.setText(cedula);
        buscarAprobado(); // Reutilizamos el método de búsqueda que ya tienes
    }


}