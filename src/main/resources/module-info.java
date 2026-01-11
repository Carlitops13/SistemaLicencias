module com.licencias { // Corregido 'modele' a 'module' y simplificado el nombre
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Esto permite a JavaFX entrar en tu paquete 'view' para leer los FXML
    opens com.licencias to javafx.fxml;

    // Si tienes controladores en otro paquete (ej. com.licencias.controller),
    // necesitarás agregar: opens com.licencias.controller to javafx.fxml;

    exports com.licencias;
}