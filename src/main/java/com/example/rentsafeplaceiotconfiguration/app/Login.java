package com.example.rentsafeplaceiotconfiguration.app;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Locale;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = createLoginPane();
        createUI(gridPane);
        Scene scene = new Scene(gridPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private GridPane createLoginPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints();
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
    }

    private void createUI(GridPane gridPane) {
        new UICreator(new Locale("en", "US"), gridPane);
    }

}