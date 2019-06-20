package com.example.drawingapp;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity  {

    LinearLayout image;
    Button takePicture, colourPicker;
    DrawingView drawingPad;  //A DrawingView object responsible for creating the canvas and allowing the user to draw
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {    //These are the manifest permissions held in an array
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising the buttons and images on the layout
        image = (LinearLayout) findViewById(R.id.image);;
        takePicture = (Button) findViewById(R.id.takePicturebtn);
        drawingPad = new DrawingView(this);
        colourPicker = (Button) findViewById(R.id.colorPickerBtn);

        //This method ensures the correct storage permissions are in place in order to save the images
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);

        LinearLayout mDrawingPad = (LinearLayout) findViewById(R.id.view_drawing_pad);
        mDrawingPad.addView(drawingPad);

        //Creating the ShakeFinder object thread which will constantly be looking out for a shake of the device.
        ShakeFinder shaker = new ShakeFinder(drawingPad, (SensorManager)getSystemService(SENSOR_SERVICE), this);
        shaker.run();
    }

    //This method ensures the appropriate permissions are in place to move files around the device
    private static void verifyStoragePermissions(MainActivity activity) {
        int userPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (userPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * This method is used to initiate the SaveImage object to run. This means that a file
     * can be saved in a background thread, as to not slow down the UI thread.
     * @param v
     *          This activities view
     */
    public void saveImage(View v){
        ConstraintLayout savingLayout = (ConstraintLayout)findViewById(R.id.saveImageLayout);
       SaveImage save = new SaveImage(this, savingLayout);
       save.run();
    }



    /**
     * This method is used to open the colour picker created with the AmbilWarnaDialog object. This is a library
     * taken from github and allows the user to choose from an RGB colour spectrum board - allowing for a more
     * complete experience. Appropriate referencing has been made to this code within the report document.
     */
    public void openColourPicker(View view){
        AmbilWarnaDialog colourChart = new AmbilWarnaDialog(this, 0xFFFFFF, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                drawingPad.setPaintbrushColour(color);
            }
        });
        colourChart.show();
    }

    /**
     * This method is called when the user clicks on the 'take picture' button. A new camera intent is created
     * and the activity is started.
     * @param view
     */
    public void takePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    /**
     * This method is overridden after the user has finished using the camera. The data saved in the Intent
     * is then extracted into the Bitmap named 'photo'. The drawing pad objects background is then set to the image.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    { Bitmap photo = (Bitmap) data.getExtras().get("data");

        BitmapDrawable finalPhoto = new BitmapDrawable(this.getResources(),photo);
        drawingPad.setBackground(finalPhoto);
    }



}
