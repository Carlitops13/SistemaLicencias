package com.licencias.controller;

import com.licencias.dao.IUsuarioDAO;
import com.licencias.dao.impl.UsuarioDAOImpl;
import com.licencias.model.Usuario;
import com.licencias.util.VentanaUtil;
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
import org.mindrot.jbcrypt.BCrypt;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.IOException;

public class MenuAdminController {

    //  BOTONES DE NAVEGACIÓN  ---
    @FXML
    private Button btnGestUsuarios;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnRequisitos;
    @FXML
    private Button btnExamenes;
    @FXML
    private Button btnLicencia;
    @FXML
    private Button btnTramites;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModoAnalista;

    // ---TABLA Y COLUMNAS ---
    @FXML
    private TableView<Usuario> tblUsuarios;

    @FXML
    private TableColumn<Usuario, String> colCedula, colNombre, colUsuario, colRol;
    @FXML
    private TableColumn<Usuario, Boolean> colEstado;
    @FXML
    private TextField txtCedula, txtNombre, txtUsuario, txtBusqueda;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private CheckBox chkActivo;


    // --- 4. VARIABLES LÓGICAS ---
    private IUsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        System.out.println("--- INICIANDO MENU ADMIN ---");

        usuarioDAO = new UsuarioDAOImpl();
        listaUsuarios = FXCollections.observableArrayList();

        if (txtCedula != null) {
            com.licencias.util.Validador.limitarCedula(txtCedula);
        }

        try {
            configurarTabla();
        } catch (Exception e) {
            System.err.println(" Error grave configurando columnas: " + e.getMessage());
            e.printStackTrace();
        }


        configurarBusqueda();


        try {
            System.out.println("Cargando usuarios desde BD...");
            cargarUsuarios();
            System.out.println(" Usuarios cargados en memoria: " + listaUsuarios.size());
        } catch (Exception e) {
            System.err.println(" No se pudo conectar a la BD.");
        }


        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleUsuario(newSelection);
            }
        });

        cmbRol.getItems().addAll("ADMIN", "ANALISTA");
        configurarNavegacion();

        if (btnModoAnalista != null) {
            btnModoAnalista.setOnAction(e -> abrirModoAnalista());
        }
    }

    private void configurarBusqueda() {
        FilteredList<Usuario> datosFiltrados = new FilteredList<>(listaUsuarios, b -> true);

        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            datosFiltrados.setPredicate(usuario -> {
                // Si no hay texto, mostrar todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // por nombre
                if (usuario.getNombre() != null && usuario.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // por cedula
                else if (usuario.getCedula() != null && usuario.getCedula().contains(lowerCaseFilter)) {
                    return true;
                }
                // usuario
                else if (usuario.getUsername() != null && usuario.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // por rol
                else if (usuario.getRol() != null && usuario.getRol().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // por rol
                else {

                    String estadoTexto = usuario.isEstado() ? "activo" : "no activo";
                    if (estadoTexto.contains(lowerCaseFilter)) {
                        return true;
                    }
                }

                return false;
            });
        });

        SortedList<Usuario> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tblUsuarios.comparatorProperty());
        tblUsuarios.setItems(datosOrdenados);
    }

    private void abrirModoAnalista() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_analista.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana
            Stage stageAnalista = new Stage();
            stageAnalista.setTitle("Modo Operativo - Analista");
            stageAnalista.setScene(new Scene(root));

            Stage stageAdmin = (Stage) btnModoAnalista.getScene().getWindow();
            stageAdmin.hide();

            stageAnalista.showAndWait(); // Esperamos a que el analista cierre

            stageAdmin.show();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el panel de analista.");
            e.printStackTrace();
        }
    }


    private void configurarNavegacion() {

        btnRegistrar.setOnAction(e -> abrirVentana("/view/formulario_solicitante.fxml", "Registro de Nuevo Solicitante"));

        btnRequisitos.setOnAction(e -> abrirVentana("/view/verificar_requisitos.fxml", "Verificación de Requisitos"));
        btnExamenes.setOnAction(e -> abrirVentana("/view/registro_examenes.fxml", "Registro de Calificaciones"));
        btnLicencia.setOnAction(e -> abrirVentana("/view/generar_licencia.fxml", "Emisión de Licencia"));
        btnTramites.setOnAction(e -> abrirVentana("/view/detalle_tramite.fxml", "Bandeja de Trámites"));

        if (btnReportes != null) {
            btnReportes.setOnAction(e -> abrirVentana("/view/reportes.fxml", "Reportes y Estadísticas de Licencias"));
        }


        if (btnNuevo != null) {
            btnNuevo.setOnAction(e -> abrirVentana("/view/formulario_solicitante.fxml", "Registro de Nuevo Solicitante"));
        }
    }

    private void abrirVentana(String ruta, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);

            VentanaUtil.aplicarIcono(stage);

            Scene scene = new Scene(root, 1000, 650);
            stage.setScene(scene);


            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMaximized(false);
            stage.setResizable(true);

            stage.show();
            stage.centerOnScreen();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista: " + ruta);
            e.printStackTrace();
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


                ((Stage) btnCerrarSesion.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarDetalleUsuario(Usuario u) {
        txtCedula.setText(u.getCedula());
        txtCedula.setDisable(true);

        txtNombre.setText(u.getNombre());
        txtUsuario.setText(u.getUsername());
        txtUsuario.setDisable(false);

        cmbRol.setValue(u.getRol());
        chkActivo.setSelected(u.isEstado());

        txtPassword.clear();
        txtPassword.setPromptText("Dejar vacío para mantener actual");
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioDAO.listarTodos());
        // tblUsuarios.setItems(listaUsuarios);
    }

    private void configurarTabla() {
        // Verificar que la tabla tenga las 5 columnas esperadas
        if (tblUsuarios.getColumns().size() < 5) {
            System.err.println(" Error: La tabla no tiene suficientes columnas.");
            return;
        }

        //  CÉDULA
        tblUsuarios.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cedula"));

        //  NOMBRE
        tblUsuarios.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //  USUARIO
        tblUsuarios.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("username"));

        //  ROL
        tblUsuarios.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("rol"));

        //  ESTADO
        TableColumn<Usuario, Boolean> columnaEstado = (TableColumn<Usuario, Boolean>) tblUsuarios.getColumns().get(4);

        //  Obtener el valor (true/false)
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        //
        columnaEstado.setCellFactory(column -> new TableCell<Usuario, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item) {
                        setText("ACTIVO");
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setText("NO ACTIVO");
                        setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    @FXML
    public void guardarUsuario() {
        // 1. Validar que los campos no estén vacíos
        if (!validarCampos()) return;


        String cedula = txtCedula.getText().trim();
        String password = txtPassword.getText();


        // Validar Cédula (10 dígitos)
        if (!com.licencias.util.Validador.esCedulaValida(cedula)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Cédula Incorrecta",
                    "La cédula del administrador/analista debe tener exactamente 10 dígitos.");
            return;
        }


        if (password.length() < 4) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seguridad",
                    "La contraseña debe tener al menos 4 caracteres.");
            return;
        }


        Usuario u = new Usuario();
        u.setCedula(cedula);
        u.setNombre(txtNombre.getText().trim().toUpperCase()); // Normalizamos a mayúsculas
        u.setUsername(txtUsuario.getText().trim());
        u.setRol(cmbRol.getValue());
        u.setEstado(chkActivo.isSelected());


        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        u.setPassword(hash);


        if (usuarioDAO.crear(u)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario creado correctamente.");
            limpiarFormulario();
            cargarUsuarios();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudo crear el usuario.\nEs posible que la cédula o el nombre de usuario ya existan.");
        }
    }

    @FXML
    public void actualizar() {
        Usuario seleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un usuario para actualizar.");
            return;
        }


        seleccionado.setNombre(txtNombre.getText());
        seleccionado.setUsername(txtUsuario.getText()); // ¡Capturamos el cambio de usuario!
        seleccionado.setRol(cmbRol.getValue());
        seleccionado.setEstado(chkActivo.isSelected());


        String nuevaPass = txtPassword.getText();
        if (!nuevaPass.isEmpty()) {

            String hash = BCrypt.hashpw(nuevaPass, BCrypt.gensalt());
            seleccionado.setPassword(hash);
        } else {

            seleccionado.setPassword(null);
        }

        //  Enviamos a la BD
        if (usuarioDAO.actualizar(seleccionado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado correctamente.");
            limpiarFormulario();
            cargarUsuarios();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar. Verifique que el nombre de usuario no esté duplicado.");
        }

        seleccionado.setEstado(chkActivo.isSelected());
        if (chkActivo.isSelected()) {
            seleccionado.setIntentosFallidos(0);
        }
        usuarioDAO.actualizar(seleccionado);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean validarCampos() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() ||
                cmbRol.getValue() == null) {

            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Por favor, complete todos los campos obligatorios.");
            return false;
        }
        return true;
    }
    @FXML
    public void limpiarFormulario() {
        txtCedula.clear();
        txtNombre.clear();
        txtUsuario.clear();
        txtPassword.clear();
        cmbRol.getSelectionModel().clearSelection();
        chkActivo.setSelected(true);
        tblUsuarios.getSelectionModel().clearSelection();
        txtCedula.setDisable(false);
        txtUsuario.setDisable(false);
    }
}