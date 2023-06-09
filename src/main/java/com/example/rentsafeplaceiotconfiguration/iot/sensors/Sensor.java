package com.example.rentsafeplaceiotconfiguration.iot.sensors;

import java.util.Random;

public interface Sensor {

    Random random = new Random();
    float getValue();

    void setRandomValue(boolean isStressed);
}
