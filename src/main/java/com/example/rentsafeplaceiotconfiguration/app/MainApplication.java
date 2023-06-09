package com.example.rentsafeplaceiotconfiguration.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rent Safe Place IoT Configuration");
        Login login = new Login();
        login.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}