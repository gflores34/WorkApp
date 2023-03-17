module app.workapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires java.sql;

    opens app.workapp to javafx.fxml;
    exports app.workapp;
}