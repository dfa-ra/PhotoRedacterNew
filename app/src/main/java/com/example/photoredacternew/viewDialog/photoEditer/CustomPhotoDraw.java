package com.example.photoredacternew.viewDialog.photoEditer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photoredacternew.viewDialog.photoViewer.CustomPhotoView;

import java.util.ArrayList;
import java.util.List;

public class CustomPhotoDraw extends CustomPhotoView {

    private Bitmap baseBitmap;
    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private Paint paint; // кисть
    private Path path; // путь
    private int color = Color.parseColor("red");
    private float canvasScale;
    private float canvasDx;
    private float canvasDy;


    // Список для хранения всех путей
    private final List<Path> paths = new ArrayList<>();
    private final List<Paint> paints = new ArrayList<>();

    public CustomPhotoDraw(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        initNewPaint(color, 5);
    }

    private void initNewPaint(int color, int w){
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        path = new Path();

        paths.add(path);
        paints.add(paint);
    }

    @Override
    public void setImageDrawable(Drawable drawable){
        super.setImageDrawable(drawable);
        fitCanvas();
        drawBitmap = Bitmap.createBitmap((int) (drawable.getIntrinsicWidth() * canvasScale + canvasDx), (int) (drawable.getIntrinsicHeight() * canvasScale + canvasDy) , Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(drawBitmap);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("aa88", "onDraw called");
        super.onDraw(canvas);
        if (drawBitmap != null) {
            Log.d("aa88", "drawBitmap != null");
            canvas.drawBitmap(drawBitmap, 0, 0, null);
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x < canvasDx) x = canvasDx;
        else if (x > getWidth() - canvasDx) x = getWidth() - canvasDx;
        if (y < canvasDy) y = canvasDy;
        else if (y > getHeight() - canvasDy) y = getHeight() - canvasDy;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("aa88", "down: " + x + " / " + y);
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d("aa88", "move: " + x + " / " + y);
                path.lineTo(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, paint);
                Log.d("aa88", "up: " + x + " / " + y);
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
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null; // Если drawable не задано, ничего не возвращаем
        }

        Bitmap combined = Bitmap.createBitmap((int) (drawBitmap.getWidth()-canvasDx), (int) (drawBitmap.getHeight() - canvasDy), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        canvas.drawBitmap(drawBitmap, -canvasDx, -canvasDy, null);

        return combined;
    }

    private void setPaintStyle(Paint.Style style){
        paint.setStyle(style);
    }

    protected void setColor(int color){
        initNewPaint(color, 30);
        this.color = color;
    }


    // метод выравнивания фото по высоте или ширине
    protected void fitCanvas() {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();

        if (viewWidth / drawableWidth < viewHeight / drawableHeight) {
            canvasScale = viewWidth / drawableWidth;
            canvasDx = 0;
            canvasDy = (viewHeight - drawableHeight * canvasScale) / 2;
        } else {
            canvasScale = viewHeight / drawableHeight;
            canvasDx = (viewWidth - drawableWidth * canvasScale) / 2;
            canvasDy = 0;
        }
    }

}
