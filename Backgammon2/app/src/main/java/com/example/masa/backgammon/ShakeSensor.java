package com.example.masa.backgammon;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class ShakeSensor implements SensorEventListener {

    //Minimum movement force to consider.
    private static int shakeTreshold = 10;
    // Minimum times in a shake gesture that the direction of movement needs to change.
    private static int minDirectionChange = 3;
    //Maximum pause between movements.
    private static int shakeTimeout = 1000;
    //How much time between two shakes to be considered one single shake
    private static int minTimeBetweenShakes = 400;

    public static int getShakeTreshold() {
        return shakeTreshold;
    }

    public static void setShakeTreshold(int shakeTreshold) {
        ShakeSensor.shakeTreshold = shakeTreshold;
    }

    public static int getMinDirectionChange() {
        return minDirectionChange;
    }

    public static void setMinDirectionChange(int minDirectionChange) {
        ShakeSensor.minDirectionChange = minDirectionChange;
    }

    public static int getShakeTimeout() {
        return shakeTimeout;
    }

    public static void setShakeTimeout(int shakeTimeout) {
        ShakeSensor.shakeTimeout = shakeTimeout;
    }

    public static int getMinTimeBetweenShakes() {
        return minTimeBetweenShakes;
    }

    public static void setMinTimeBetweenShakes(int minTimeBetweenShakes) {
        ShakeSensor.minTimeBetweenShakes = minTimeBetweenShakes;
    }

    //Time when the gesture started.
    private long firstDirChange = 0;
    //Time when the last movement started.
    private long lastDirChange;

    //How many movements are considered so far.
    private int dirChangeCount = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    //ShakeListener that is called when shake is detected.
    private ShakeListener shakeListener;
    Timer shakeTimer=new Timer();
    long lastshake;




    public void setShakeListener(ShakeListener listener) {
        this.shakeListener = listener;
    }

    private void setTimer() {
        shakeTimer = new Timer();
        shakeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                shakeListener.onShakeFinished();
            }
        }, minTimeBetweenShakes);
    }


    private synchronized void stillShaking() {
        shakeTimer.cancel();
        setTimer();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

       float movement = calculateMovement(sensorEvent);
        if (movement > shakeTreshold) {
            long current = System.currentTimeMillis();
            if (firstDirChange == 0) {
                firstDirChange = current;
                lastDirChange = current;
            }

            if ( (current - lastDirChange) < shakeTimeout) {
              // store movement data
                lastDirChange = current;
                dirChangeCount++;
                setLastValues(sensorEvent);


                // check how many movements are so far
                if (dirChangeCount >= minDirectionChange) {
                    //System.out.println("Shaking " + (now - lastshake));

                    if (current - lastshake > minTimeBetweenShakes) {
                        shakeListener.onShakeStarted();
                        setTimer();
                    } else {
                        stillShaking();
                    }
                    lastshake = current;
                    resetValues();

                }

            } else {
                resetValues();
            }
        }
    }

    private void setLastValues(SensorEvent sensorEvent) {
        lastX = sensorEvent.values[0];
        lastY = sensorEvent.values[1];
        lastZ = sensorEvent.values[2];
    }

    private float calculateMovement(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float movement = Math.abs(x + y + z - lastX - lastY - lastZ);
        return movement;
    }


    private void resetValues() {
        firstDirChange = 0;
        dirChangeCount = 0;
        lastDirChange = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
