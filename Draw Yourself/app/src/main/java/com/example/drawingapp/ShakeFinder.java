package com.example.drawingapp;

import android.app.Activity;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * This class is used listen for a shake of the device. As it runs in the background, the UI thread is
 * not affected by any input lag. If a shake is detected, it will call the function clearCanvas(), a method
 * in the DrawingView class - which will remove all drawing done on the picture. The seismic listener
 * has been implemented in this class. Referencing to this code has been written in the report for this project.
 */
public class ShakeFinder implements Runnable , com.squareup.seismic.ShakeDetector.Listener {
    private DrawingView canvas;
    private SensorManager sensorManager;
    private com.squareup.seismic.ShakeDetector shakeDetector;
    private Activity activity;


    public ShakeFinder(DrawingView canvas, SensorManager sysManager, Activity activity){
        this.canvas = canvas;
        this.sensorManager = sysManager;
        this.shakeDetector = new com.squareup.seismic.ShakeDetector(this);
        this.activity = activity;
    }

    /**
     * This method is implemented from the Runnable interface. It is the core of the thread, and in this case,
     * it is constantly looking for a shake in the background.
     */
    @Override
    public void run() {
        shakeDetector.start(sensorManager);
    }

    /**
     * This method is taken from the Seismic library. Once a shake is detected, the canvas on the UI will be cleared.
     */
    @Override
    public void hearShake() {
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(activity, "Canvas Cleared!", Toast.LENGTH_SHORT).show();
            }
        });
        canvas.clearCanvas();
    }
}
