package com.example.drawingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage implements Runnable {
    private Activity context;
    private View view;

    public SaveImage(Activity activity, View view) {
        this.context = activity;
        this.view = view;
    }

    /**
     * This method is used to save an image. It is ran in the background so the user is still able to draw on the canvas
     * whilst the image is being saved - not letting the app slow down
     */
    @Override
    public void run() {
        File file = saveImage(context, view);
        if (file != null){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Image Saved", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * This method saves is called on a new file in order to save an image. It uses a FileOutputStream in order to
     * move an image file into the directory named 'DrawYourself'.
     * @param context
     *          The MainActivity context of the app
     * @param drawView
     *          The layout used that will be converted into a bitmap to then be saved
     * @return
     *          The new file object
     */
    private File saveImage(Context context, View drawView) {
        File pictureDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DrawYourself");

        //Creating the directory for the images on the device
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }
        //Creating the image name, using the system's current time in order for a unique name every time.
        String filename = pictureDirectory.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = screenshot(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictureFile;
    }

    /**
     * This method converts a view on the user interface into a bitmap. It creates a new bitmap with the view
     * used on the user interface, and is then placed onto a canvas. That canvas is then layered with the background
     * of the view (the users picture). The final bitmap is then returned to the 'saveImage' method.
     * @param view
     *          The view on the user interface that is to be turned into a bitmap
     * @return
     *          THe final bitmap image taken from the view
     */
    private Bitmap screenshot(View view) {
        Bitmap viewBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(viewBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return viewBitmap;
    }
}
