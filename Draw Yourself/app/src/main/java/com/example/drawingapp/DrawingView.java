package com.example.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

class DrawingView extends View {
    private Paint   paintbrush;    //The paintbrush used to draw on the canvas
    private Bitmap  canvasBitmap;   //The canvas bitmap used to create the overall drawing canvas
    private Canvas  drawingCanvas;   //The canvas that is shown on the screen, allowing the user to draw
    private Path    paintPath;    //The path the paintbrush will take when the user draws on the canvas
    private Paint bitmapPaint;  //The bitmap paint path

    /**
     * This constructor sets up the paintbrush with its properties, such as its paint style and starting colour
     * The paths and bitmap paints are also initialised here.
     * @param context
     */
    public DrawingView(Context context) {
        super(context);
        paintbrush = new Paint();
        paintbrush.setAntiAlias(true);
        paintbrush.setDither(true);
        paintbrush.setColor(0xFFFF0000);
        paintbrush.setStyle(Paint.Style.STROKE);
        paintbrush.setStrokeJoin(Paint.Join.ROUND);
        paintbrush.setStrokeCap(Paint.Cap.ROUND);
        paintbrush.setStrokeWidth(8);

        paintPath = new Path();
        bitmapPaint = new Paint();
        bitmapPaint.setColor(Color.RED);
    }

    /**
     * The methods below are used to create the drawing on the canvas.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawingCanvas = new Canvas(canvasBitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, bitmapPaint);
        canvas.drawPath(paintPath, paintbrush);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        paintPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            paintPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        paintPath.lineTo(mX, mY);
        drawingCanvas.drawPath(paintPath, paintbrush);
        paintPath.reset();
    }

    /**
     * This method is responsible for moving creating the paint affect on the canvas. When the user touches the
     * DrawingView object on the screen, cases are met to check which way the user has brushed.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * This method is used to set the paintbrush colour.
     * @param colour
     *          This colour is taken from the RGB colour picker and send to this class
     */
    public void setPaintbrushColour(int colour){
        paintPath.reset();
        this.paintbrush.setColor(colour);
    }

    /**
     * This method is called once a shake of the device is detected. It clears all of the paint from the canvas.
     */
    public void clearCanvas(){
        drawingCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();

    }
}