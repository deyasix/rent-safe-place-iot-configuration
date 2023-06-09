package com.example.rentsafeplaceiotconfiguration.iot.sensors;

public class TemperatureSensor implements Sensor{

    enum TemperatureMeasure {
        C, F
    }
    private TemperatureMeasure measurement = TemperatureMeasure.C;

    private int value = 0;

    public void setMeasurement(Character measurement) {
        if (measurement.equals('C')) {
            this.measurement = TemperatureMeasure.C;
        } else {
            this.measurement = TemperatureMeasure.F;
        }
    }

    public Character getMeasurement(){
        return measurement.toString().charAt(0);
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setRandomValue(boolean isStressed) {
        int min;
        int max;
        if (isStressed) {
            if (measurement.equals(TemperatureMeasure.C)) {
                min = 30;
                max = 1000;
            } else {
                min = 85;
                max = 1830;
            }
        } else {
            if (measurement.equals(TemperatureMeasure.C)) {
                min = 15;
                max = 30;
            } else {
                min = 60;
                max = 85;
            }
        }


        value = (random.nextInt(max + 1 - min) + min);
    }

}
