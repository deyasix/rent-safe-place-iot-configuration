package com.example.rentsafeplaceiotconfiguration.iot.sensors;

public class VolumeSensor implements Sensor{

    private boolean isVolumeChanged = false;

    @Override
    public float getValue() {
        return isVolumeChanged?1:0;
    }

    @Override
    public void setRandomValue(boolean isStressed) {
        isVolumeChanged = isStressed;
    }

}
