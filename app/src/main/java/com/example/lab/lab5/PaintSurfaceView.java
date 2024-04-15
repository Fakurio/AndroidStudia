package com.example.lab.lab5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;


public class PaintSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private SurfaceView surfaceViewLayout;
    private SurfaceHolder holder;
    private Paint paint;
    private Path path;
    private DrawingThread drawingThread;
    public Boolean isThreadRunning = false;
    private float startX, startY, endX, endY;
    private Canvas myCanvas;
    private Bitmap myBitmap;
    private Matrix identityMatrix;

    public PaintSurfaceView(Context context, SurfaceView surfaceViewLayout) {
        super(context);
        this.surfaceViewLayout = surfaceViewLayout;
        init();
    }

    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        holder = surfaceViewLayout.getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        setFocusable(true);
        surfaceViewLayout.setOnTouchListener(this);
    }

    public void changeDrawingColor(int color) {
           paint.setColor(color);
           path = new Path();
    }

    public void clearSurface() {
        myCanvas.drawColor(Color.WHITE);
        path.reset();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                path.moveTo(startX, startY);
                drawingThread.setDrawCircle(true);
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                path.lineTo(endX, endY);
                startX = endX;
                startY = endY;
                break;
            case MotionEvent.ACTION_UP:
                drawingThread.setDrawCircle(true);
                break;
        }
        return true;
    }

    private class DrawingThread extends Thread {
        private boolean drawCircle;
        public void setDrawCircle(boolean drawCircle) {
            this.drawCircle = drawCircle;
        }

        private void drawCircle() {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            myCanvas.drawCircle(startX, startY, 20, paint);
            paint.setStyle(Paint.Style.STROKE);
            drawCircle = false;
        }

        private void drawLine(Canvas canvas) {
            myCanvas.drawPath(path, paint);
            canvas.drawBitmap(myBitmap, identityMatrix, null);
        }
        public void run() {
            while(isThreadRunning) {
                Canvas canvas = null;
                try {
                    if(!holder.getSurface().isValid()) {
                        continue;
                    }
                    canvas = holder.lockCanvas();
                    if(isThreadRunning) {
                        if(drawCircle) {
                            drawCircle();
                        }
                        drawLine(canvas);
                    }
                } finally {
                    if(canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    private void startDrawing() {
        if(!isThreadRunning) {
            Log.i("Rysowanie", "Wątek start");
            isThreadRunning = true;
            drawingThread = new DrawingThread();
            drawingThread.start();
        }
    }

    private void stopDrawing() {
        Log.i("Rysowanie", "Ubijam wątek");
        isThreadRunning = false;
        try {
            if(drawingThread != null) {
                drawingThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        myBitmap = Bitmap.createBitmap(surfaceViewLayout.getWidth(), surfaceViewLayout.getHeight(),
                Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas();
        myCanvas.setBitmap(myBitmap);
        myCanvas.drawARGB(255,255,255,255);
        identityMatrix = new Matrix();
        startDrawing();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stopDrawing();
    }
}
