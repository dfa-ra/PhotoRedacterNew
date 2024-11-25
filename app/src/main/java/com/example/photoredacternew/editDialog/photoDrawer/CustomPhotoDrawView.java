package com.example.photoredacternew.editDialog.photoDrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class CustomPhotoDrawView extends AppCompatImageView {

    private Bitmap baseBitmap;
    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private Paint paint; // кисть
    private Path path; // путь
    private int color = 0x00FF00;

    public CustomPhotoDrawView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomPhotoDrawView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        paint.setAntiAlias(true);

        path = new Path();
    }

    public void setBitmap(Bitmap bitmap){
        baseBitmap = bitmap;

        drawBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(drawBitmap);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (baseBitmap != null){
            canvas.drawBitmap(baseBitmap, 0, 0, null);
        }

        if (drawBitmap != null){
            canvas.drawBitmap(drawBitmap, 0 ,0, null);
        }

        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, paint);
                path.reset();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void clearDraw(){
        if (drawBitmap != null){
            drawBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
        }
    }

    public Bitmap getCombinedBitmap(){
        Bitmap combined = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(combined);

        canvas.drawBitmap(baseBitmap, 0, 0, null);
        canvas.drawBitmap(drawBitmap, 0, 0,null);

        return combined;
    }

    private void setPaintStyle(Paint.Style style){
        paint.setStyle(style);
    }

    private void setColor(int color){
        this.color = color;
    }
}
