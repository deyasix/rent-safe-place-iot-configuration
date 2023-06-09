package com.example.rentsafeplaceiotconfiguration.iot;

import com.example.rentsafeplaceiotconfiguration.app.HttpConnection;
import com.example.rentsafeplaceiotconfiguration.app.PropertiesManager;
import com.example.rentsafeplaceiotconfiguration.iot.sensors.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

public class IoT {
    private final Sensor[] sensors = new Sensor[] {new HumiditySensor(), new TemperatureSensor(), new SmokeSensor(), new SoundSensor(),
    new VolumeSensor()};
    private final Label[] labels = new Label[sensors.length];
    private final boolean[] isStressed = new boolean[sensors.length];
    //private LocalDateTime lastTime;
    private final HashMap<String, LocalDateTime> lastTimes = new HashMap<>();
    String[] values;
    String tru, fals, stable, unstable, dB;
    public IoT(GridPane gridPane, RadioButton celsiusRadioButton, RadioButton fahrenheitRadioButton, Button[] buttons, String[] values,
               String tru, String fals, String stable, String unstable, String dB) {
        this.values = values;
        this.tru = tru;
        this.fals = fals;
        this.stable = stable;
        this.unstable = unstable;
        this.dB = dB;
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label();
            buttons[i].setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gridPane.add(labels[i], 1,i + 1,2,1);
            gridPane.add(buttons[i], 3, i + 1);
            GridPane.setHalignment(labels[i], HPos.LEFT);
            int finalI = i;
            buttons[i].setOnAction (event -> {
                isStressed[finalI] = !isStressed[finalI];
                buttons[finalI].setTextFill(isStressed[finalI]?Color.RED:Color.BLACK);
                labels[finalI].setTextFill(isStressed[finalI]?Color.RED:Color.BLACK);
            });
        }
        celsiusRadioButton.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        fahrenheitRadioButton.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        ToggleGroup toggleGroup = new ToggleGroup();
        celsiusRadioButton.setToggleGroup(toggleGroup);
        fahrenheitRadioButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(toggleGroup.getToggles().get(0));
        gridPane.add(celsiusRadioButton, 5, 0);
        gridPane.add(fahrenheitRadioButton, 5, 1);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                TemperatureSensor temperatureSensor = (TemperatureSensor) Arrays.stream(sensors)
                        .filter(sensor -> sensor.getClass().getSimpleName().equals("TemperatureSensor")).findFirst().get();
                Character measurement = toggleGroup.getSelectedToggle().equals(celsiusRadioButton)?'C':'F';
                temperatureSensor.setMeasurement(measurement);
            }
        });

    }

    public void updateText(String tru, String fals, String stable, String unstable, String dB) {
        this.tru = tru;
        this.fals = fals;
        this.stable = stable;
        this.unstable = unstable;
        this.dB = dB;
    }

    public void setUI(String[] values, String tru, String fals, String stable, String unstable, String dB) {
        for (int i = 0; i < values.length; i++) {
            switch (sensors[i].getClass().getSimpleName()) {
                case("HumiditySensor") -> {
                    labels[i].setText(values[i] + sensors[i].getValue() + "%");
                }
                case("SmokeSensor") -> {
                    labels[i].setText(values[i] + (sensors[i].getValue()==1?tru:fals));
                }
                case("SoundSensor") -> {
                    labels[i].setText(values[i] + sensors[i].getValue() + " " + dB);
                }
                case("TemperatureSensor") -> {
                    labels[i].setText(values[i] +  sensors[i].getValue() + " °" + ((TemperatureSensor) sensors[i]).getMeasurement() );
                }
                case("VolumeSensor") -> {
                    labels[i].setText(values[i] + (sensors[i].getValue()==1?unstable:stable));
                }
            }
            labels[i].setFont(Font.font("Arial", FontWeight.MEDIUM, 22));
        }
    }

    private void setGetValues(boolean toChecked) {
        for (int i = 0; i < sensors.length; i++) {
            sensors[i].setRandomValue(isStressed[i]);
        }
        Platform.runLater(() -> {
            setUI(values, tru, fals, stable, unstable, dB);
            if (toChecked) {
                try {
                    checkSensors();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void run() {
        Timeline timeline = new Timeline();
        KeyFrame initialKeyFrame = new KeyFrame(Duration.ZERO, event -> setGetValues(false));
        timeline.getKeyFrames().add(initialKeyFrame);
        KeyFrame repeatedKeyFrame = new KeyFrame(Duration.seconds(1), event -> {
            setGetValues(true);
        });
        timeline.getKeyFrames().add(repeatedKeyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void checkSensors() throws MalformedURLException {
        for (Sensor sensor : sensors) {
            float value = sensor.getValue();
            String message = "";
            String type = "";
            switch(sensor.getClass().getSimpleName()) {
                case("HumiditySensor") -> {
                    if (value > 60f) {
                        message = "Humidity is high";
                        type = "Humidity";
                    }
                }
                case("SmokeSensor") -> {
                    if (value == 1f) {
                        message = "Smoke detected";
                        type = "Smoke";
                    }
                }
                case("SoundSensor") -> {
                    if (value > 70f) {
                        message = "Loud sound detected";
                        type = "Sound";
                    }
                }
                case("TemperatureSensor") -> {
                    Character measurement = ((TemperatureSensor) sensor).getMeasurement();
                    if ((value > 30f && measurement.equals('C')) || (value > 85f && measurement.equals('F'))) {
                        message = "Temperature is high";
                        type = "Temperature";
                    }
                }
                case("VolumeSensor") -> {
                    if (value == 1f) {
                        message = "Room volume change detected";
                        type = "Volume";
                    }
                }
            }
            if (!type.isBlank() && !message.isBlank()) doRequest(message, type);
        }
    }

    private void doRequest(String message, String type) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (lastTimes.get(type) == null || currentTime.isAfter(lastTimes.get(type).plusMinutes(30))) {
            try {
                URL url = new URL("http://127.0.0.1:8088/warnings");
                JSONObject jsonObject = new JSONObject();
                JSONObject building = new JSONObject();
                building.put("id", PropertiesManager.read("building_id"));
                jsonObject.put("building", building);
                jsonObject.put("type", type);
                jsonObject.put("message", message);
                jsonObject.put("time", currentTime);
                String response = HttpConnection.request(url, "POST", jsonObject.toString());
                System.out.println(response);
                lastTimes.put(type, LocalDateTime.now());
            } catch (Exception ignored) {

            }

        }
    }
}
