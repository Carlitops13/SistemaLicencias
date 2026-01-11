package com.licencias.controller;

import com.licencias.dao.ITramiteDAO;
import com.licencias.dao.impl.TramiteDAOImpl;
import com.licencias.model.Tramite;
import com.licencias.util.VentanaUtil;
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
import java.time.format.DateTimeFormatter;

public class MenuAnalistaController {


    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnRequisitos;
    @FXML
    private Button btnExamenes;
    @FXML
    private Button btnLicencia;
    @FXML
    private Button btnGestion;
    @FXML
    private Button btnCerrarSesion;
    @FXML private Button btnNuevoTramiteMain;


    @FXML private TableView<Tramite> tblTramites;


    private ITramiteDAO tramiteDAO;
    private ObservableList<Tramite> listaTramites;

    @FXML
    public void initialize() {
        System.out.println("--- INICIANDO MENU ANALISTA ---");


        tramiteDAO = new TramiteDAOImpl();
        listaTramites = FXCollections.observableArrayList();


        configurarTabla();


        cargarTramites();


        configurarNavegacion();
    }

    private void configurarTabla() {
        // Validación
        if (tblTramites == null || tblTramites.getColumns().isEmpty()) {
            System.err.println(" ALERTA: La tabla no se cargó correctamente desde el FXML.");
            return;
        }

        try {


            //  ID
            tblTramites.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));

            //  Cédula
            TableColumn<Tramite, String> colCedula = (TableColumn<Tramite, String>) tblTramites.getColumns().get(1);
            colCedula.setCellValueFactory(cellData -> {
                if (cellData.getValue().getSolicitante() != null)
                    return new SimpleStringProperty(cellData.getValue().getSolicitante().getCedula());
                return new SimpleStringProperty("N/A");
            });

            //  Nombre
            TableColumn<Tramite, String> colNombre = (TableColumn<Tramite, String>) tblTramites.getColumns().get(2);
            colNombre.setCellValueFactory(cellData -> {
                if (cellData.getValue().getSolicitante() != null)
                    return new SimpleStringProperty(cellData.getValue().getSolicitante().getNombre());
                return new SimpleStringProperty("Desconocido");
            });

            //  Tipo Licencia
            tblTramites.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("tipoLicencia"));

            //  Fecha Solicitud
            TableColumn<Tramite, String> colFecha = (TableColumn<Tramite, String>) tblTramites.getColumns().get(4);
            colFecha.setCellValueFactory(cellData -> {
                if (cellData.getValue().getFechaSolicitud() != null) {
                    return new SimpleStringProperty(
                            cellData.getValue().getFechaSolicitud().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    );
                }
                return new SimpleStringProperty("-");
            });

            // Estado
            tblTramites.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("estado"));

            System.out.println("✅ Columnas configuradas correctamente.");

        } catch (Exception e) {
            System.err.println(" Error configurando columnas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargarTramites() {
        if (tramiteDAO != null) {
            listaTramites.clear();
            listaTramites.addAll(tramiteDAO.listarTodos());
            tblTramites.setItems(listaTramites);
            System.out.println("Datos cargados en tabla: " + listaTramites.size());
        }
    }

    private void configurarNavegacion() {
        btnRegistrar.setOnAction(e -> abrirVentana("/view/formulario_solicitante.fxml", "Registro de Nuevo Solicitante"));
        btnRequisitos.setOnAction(e -> abrirVentana("/view/verificar_requisitos.fxml", "Verificación de Requisitos"));
        btnExamenes.setOnAction(e -> abrirVentana("/view/registro_examenes.fxml", "Registro de Calificaciones"));
        btnLicencia.setOnAction(e -> abrirVentana("/view/generar_licencia.fxml", "Emisión de Licencia"));
        btnGestion.setOnAction(e->abrirVentana("/view/detalle_tramite.fxml", "Gestión de Trámites"));


        // Botón Nuevo Trámite
        if (btnNuevoTramiteMain != null) {
            btnNuevoTramiteMain.setOnAction(e -> abrirVentana("/view/formulario_solicitante.fxml", "Registro de Nuevo Solicitante"));
        }
    }

    private void abrirVentana(String ruta, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);


            Scene scene = new Scene(root, 1000, 650);
            stage.setScene(scene);
            VentanaUtil.aplicarIcono(stage);

            stage.initModality(Modality.APPLICATION_MODAL);


            stage.show();
            stage.centerOnScreen();


            stage.setOnHiding(event -> cargarTramites());

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de Navegación", "No se encontró el archivo: " + ruta);
        }
    }

    @FXML
    public void cerrarSesion() {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Está seguro que desea salir?");
        confirmacion.setContentText("Volverá a la pantalla de inicio de sesión.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                // Cargar login
                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();

                // Cerrar ventana actual
                ((Stage) btnCerrarSesion.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}