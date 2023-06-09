package com.example.rentsafeplaceiotconfiguration.iot.sensors;

public class SoundSensor implements Sensor{

    private int value = 0;

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setRandomValue(boolean isStressed) {
        int max, min;
        if (isStressed) {
            max = 110;
            min = 70;
        } else {
            max = 70;
            min = 0;
        }
        value = (random.nextInt(max + 1 - min) + min);
    }

    @Override
    public String getStringValue() {
        return "Sound: " + value + " dB";
    }
}
