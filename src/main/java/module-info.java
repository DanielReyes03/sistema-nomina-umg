module com.example.sistemanomina {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires mysql.connector.j;

    opens com.example.sistemanomina to javafx.fxml;
    opens com.example.sistemanomina.controller to javafx.fxml;
    exports com.example.sistemanomina;
}