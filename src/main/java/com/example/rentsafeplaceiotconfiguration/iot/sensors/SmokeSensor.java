package com.example.rentsafeplaceiotconfiguration.iot.sensors;

public class SmokeSensor implements Sensor {

    private boolean isSmoke = false;

    @Override
    public float getValue() {
        return isSmoke?1:0;
    }

    @Override
    public void setRandomValue(boolean isStressed) {
        isSmoke = isStressed;
    }

}
