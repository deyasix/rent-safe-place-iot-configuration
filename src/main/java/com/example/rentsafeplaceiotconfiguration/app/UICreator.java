package com.example.rentsafeplaceiotconfiguration.app;

import com.example.rentsafeplaceiotconfiguration.iot.IoT;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class UICreator {
    Locale locale;
    private ResourceBundle resourceBundle;
    ToggleGroup toggleGroup;
    Label headerLabel, emailLabel, passwordLabel, addressLabel, buildingIdLabel;
    Button submitButton;
    TextField emailField, passwordField, addressField;
    RadioButton celsiusRadioButton, fahrenheitRadioButton;
    Button[] buttons = new Button[5];
    String[] sensorLabels = new String[5];
    IoT ioT;
    public UICreator(Locale locale, GridPane gridPane) {
        this.locale = locale;
        RadioButton ukrLangRadioButton = new RadioButton("УКР");
        ukrLangRadioButton.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        RadioButton engLangRadioButton = new RadioButton("ENG");
        engLangRadioButton.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        toggleGroup = new ToggleGroup();
        ukrLangRadioButton.setToggleGroup(toggleGroup);
        engLangRadioButton.setToggleGroup(toggleGroup);
        updateToggle("Login", ukrLangRadioButton, engLangRadioButton);
        this.resourceBundle = ResourceBundle.getBundle("com.example.rentsafeplaceiotconfiguration.LangBundle", this.locale);
        initializeLoginUI(gridPane);
        toggleGroup.selectToggle(ukrLangRadioButton);
        gridPane.add(ukrLangRadioButton, 2, 0);
        gridPane.add(engLangRadioButton, 3, 0);
    }

    private void updateToggle(String page, RadioButton ukrLangRadioButton, RadioButton engLangRadioButton) {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() == ukrLangRadioButton) {
                refreshLanguage(new Locale("uk", "UA"));
                switch (page) {
                    case "Login" -> updateLoginUI();
                    case "Address" -> updateAddressUi();
                    case "IoT" -> updateIoTUI();
                }
            } else if (toggleGroup.getSelectedToggle() == engLangRadioButton) {
                refreshLanguage(new Locale("en", "US"));
                switch (page) {
                    case "Login" -> updateLoginUI();
                    case "Address" -> updateAddressUi();
                    case "IoT" -> updateIoTUI();
                }
            }
        });
    }

    private void updateIoTUI() {
        celsiusRadioButton.setText(resourceBundle.getString("celsius"));
        fahrenheitRadioButton.setText(resourceBundle.getString("fahrenheit"));
        buildingIdLabel.setText(resourceBundle.getString("building_id") + PropertiesManager.read("building_id"));
        ioT.updateText(resourceBundle.getString("true"), resourceBundle.getString("false"), resourceBundle.getString("stable"),
                resourceBundle.getString("unstable"), resourceBundle.getString("dB"));
        sensorLabels[0] = (resourceBundle.getString("humidity"));
        sensorLabels[1] = (resourceBundle.getString("temperature"));
        sensorLabels[2] = (resourceBundle.getString("smoke"));
        sensorLabels[3] = (resourceBundle.getString("sound"));
        sensorLabels[4] = (resourceBundle.getString("volume"));
        for (Button button : buttons) {
            button.setText(resourceBundle.getString("stress"));
        }
    }

    private void updateAddressUi() {
        addressLabel.setText(resourceBundle.getString("address"));
        submitButton.setText(resourceBundle.getString("submit"));
        headerLabel.setText(resourceBundle.getString("select_address"));
    }

    private void initializeAddressInputUI(GridPane gridPane) {
        updateToggle("Address", (RadioButton) toggleGroup.getToggles().get(0),(RadioButton) toggleGroup.getToggles().get(1));
        gridPane.getChildren().removeAll(headerLabel, emailLabel, passwordLabel, submitButton, emailField, passwordField);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        headerLabel = new Label(resourceBundle.getString("select_address"));
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,1, 4, 1);
        addressLabel = new Label(resourceBundle.getString("address"));
        gridPane.add(addressLabel, 0, 2);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        addressField = new TextField();
        addressField.setPrefHeight(40);
        gridPane.add(addressField, 1,2, 3, 1);
        submitButton = new Button(resourceBundle.getString("submit"));
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 4, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        submitButton.setOnAction(event -> {
            if (addressField.getText().isEmpty()) {
                showAlert(gridPane.getScene().getWindow(), "Form Error!", "Please enter your address");
                return;
            }
            try {
                URL url = new URL("http://127.0.0.1:8088/realtors/buildings");
                String response = HttpConnection.request(url, "GET", "");
                JSONArray jsonArray = new JSONArray(response);
                Long id;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject building = jsonArray.getJSONObject(i);
                    if (!building.isNull("address")
                            && building.getString("address").equals(addressField.getText())) {
                        id = building.getLong("id");
                        PropertiesManager.save("building_id", String.valueOf(id));
                        initializeIoTIU(gridPane);
                    }
                }
                if (PropertiesManager.read("building_id").isEmpty()) showAlert(gridPane.getScene().getWindow(), "No such building!", "Please, enter correct address");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeLoginUI(GridPane gridPane) {
        headerLabel = new Label(resourceBundle.getString("login"));
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 1, 4, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        emailLabel = new Label(resourceBundle.getString("email"));
        gridPane.add(emailLabel, 0, 2);

        passwordLabel = new Label(resourceBundle.getString("password"));
        gridPane.add(passwordLabel, 0, 3);

        emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1,2, 3, 1);
        passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3, 3, 1);

        submitButton = new Button(resourceBundle.getString("submit"));
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 4, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        submitButton.setOnAction(event -> {
            if (emailField.getText().isEmpty()) {
                showAlert(gridPane.getScene().getWindow(), "Form Error!", "Please enter your email");
                return;
            }
            if (passwordField.getText().isEmpty()) {
                showAlert(gridPane.getScene().getWindow(), "Form Error!", "Please enter a password");
                return;
            }
            try {
                String response = HttpConnection.login(emailField.getText(), passwordField.getText());
                if (!response.isEmpty()) {
                    initializeAddressInputUI(gridPane);
                } else {
                    showAlert(gridPane.getScene().getWindow(), "Authorization Error!", "Invalid credentials");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeIoTIU(GridPane gridPane) {
        updateToggle("IoT", (RadioButton) toggleGroup.getToggles().get(0),(RadioButton) toggleGroup.getToggles().get(1));
        gridPane.getChildren().removeAll(headerLabel, addressLabel, addressField, submitButton);
        buildingIdLabel = new Label(resourceBundle.getString("building_id") + PropertiesManager.read("building_id"));
        buildingIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gridPane.add(buildingIdLabel, 0,0, 3, 1);
        GridPane.setHalignment(buildingIdLabel, HPos.LEFT);
        celsiusRadioButton = new RadioButton(resourceBundle.getString("celsius"));
        fahrenheitRadioButton = new RadioButton(resourceBundle.getString("fahrenheit"));
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(resourceBundle.getString("stress"));
        }
        sensorLabels[0] = (resourceBundle.getString("humidity"));
        sensorLabels[1] = (resourceBundle.getString("temperature"));
        sensorLabels[2] = (resourceBundle.getString("smoke"));
        sensorLabels[3] = (resourceBundle.getString("sound"));
        sensorLabels[4] = (resourceBundle.getString("volume"));
        ioT = new IoT(gridPane, celsiusRadioButton, fahrenheitRadioButton, buttons, sensorLabels, resourceBundle.getString("true"),
                resourceBundle.getString("false"), resourceBundle.getString("stable"), resourceBundle.getString("unstable"),
                resourceBundle.getString("dB"));
        ioT.run();
    }

    public void refreshLanguage(Locale locale) {
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle("com.example.rentsafeplaceiotconfiguration.LangBundle", this.locale);
    }

    private void updateLoginUI() {
        headerLabel.setText(resourceBundle.getString("login"));
        emailLabel.setText(resourceBundle.getString("email"));
        passwordLabel.setText(resourceBundle.getString("password"));
        submitButton.setText(resourceBundle.getString("submit"));
    }

    private void showAlert(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
