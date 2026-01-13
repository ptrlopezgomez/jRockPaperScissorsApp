module com.motivus.jdeskapp.rps {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.motivus.jdeskapp.rps to javafx.fxml;
    exports com.motivus.jdeskapp.rps;
}