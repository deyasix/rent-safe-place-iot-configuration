package com.example.rentsafeplaceiotconfiguration.iot.sensors;

public class HumiditySensor implements Sensor{

    private int value = 0;

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setRandomValue(boolean isStressed) {
        int max, min;
        if (isStressed) {
            max = 100;
            min = 60;
        } else {
            max = 60;
            min = 30;
        }
        value = (random.nextInt(max + 1 - min) + min);
    }

    public String getStringValue() {
        return "Humidity: " + value + "%";
    }
}
