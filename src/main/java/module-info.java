module com.example.rentsafeplaceiotconfiguration {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.rentsafeplaceiotconfiguration to javafx.fxml;
    exports com.example.rentsafeplaceiotconfiguration.app;
    opens com.example.rentsafeplaceiotconfiguration.app to javafx.fxml;
    exports com.example.rentsafeplaceiotconfiguration.iot;
    opens com.example.rentsafeplaceiotconfiguration.iot to javafx.fxml;
}