package com.example.photoredacternew.viewDialog.photoEditer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
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

    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private Paint paint; // кисть
    private Path path; // путь
    private int color = Color.parseColor("red");
    private int width = 5;
    private float canvasScale;
    private float canvasDx;
    private float canvasDy;
    private EditTypeEvent typeEvent = EditTypeEvent.NONE;

    // Список для хранения всех путей
    private final List<Path> paths = new ArrayList<>();
    private final List<Paint> paints = new ArrayList<>();

    public CustomPhotoDraw(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
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

        Log.d("aa88", "paths.size(): " + paths.size() + " : " + paints.size());
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    private void init(){
        initNewPaint(color);
    }

    private void initNewPaint(int color){
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);

    }

    public void setImageBitmap(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        super.setImageDrawable(drawable);
        fitCanvas();
        drawBitmap = Bitmap.createBitmap((int) (drawable.getIntrinsicWidth() * canvasScale + canvasDx), (int) (drawable.getIntrinsicHeight() * canvasScale + canvasDy) , Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(drawBitmap);

        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (typeEvent){
            case DRAW: return drawPaths(event);
            case ERASE: return erasePaths(event);
            case CROP:
            case NONE:
                return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean erasePaths(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        if (x < canvasDx) x = canvasDx;
        else if (x > getWidth() - canvasDx) x = getWidth() - canvasDx;
        if (y < canvasDy) y = canvasDy;
        else if (y > getHeight() - canvasDy) y = getHeight() - canvasDy;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                erasePathAtPoint(x, y);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean drawPaths(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        if (x < canvasDx) x = canvasDx;
        else if (x > getWidth() - canvasDx) x = getWidth() - canvasDx;
        if (y < canvasDy) y = canvasDy;
        else if (y > getHeight() - canvasDy) y = getHeight() - canvasDy;


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                paints.add(paint);
                paths.add(path);
                Log.d("aa88", "down: " + x + " / " + y);
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d("aa88", "move: " + x + " / " + y);
                path.lineTo(x, y);
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void erasePathAtPoint(float x, float y) {
        Log.d("aa88", "eraser");

        // Радиус точности попадания
        float touchRadius = 20;

        for (int i = paths.size() - 1; i >= 0; i--) {
            Path path = paths.get(i);

            // Используем PathMeasure для анализа пути
            PathMeasure pathMeasure = new PathMeasure(path, false);
            float[] pos = new float[2]; // Координаты точки на пути
            float[] tan = new float[2]; // Тангент в этой точке

            boolean intersects = false;

            // Проверяем каждый сегмент пути
            do {
                float pathLength = pathMeasure.getLength();
                for (float distance = 0; distance <= pathLength; distance += 1) {
                    pathMeasure.getPosTan(distance, pos, tan);

                    // Считаем расстояние от точки до сегмента пути
                    float dx = pos[0] - x;
                    float dy = pos[1] - y;
                    float distanceToPath = (float) Math.sqrt(dx * dx + dy * dy);

                    if (distanceToPath <= touchRadius) {
                        intersects = true;
                        break;
                    }
                }
            } while (pathMeasure.nextContour());

            // Если пересечение найдено, удаляем путь
            if (intersects) {
                paths.remove(i);
                paints.remove(i);
                invalidate();
                return;
            }
        }
    }

    public void clearDraw(){
        paths.clear();
        paints.clear();
        invalidate();
    }

    public void deleteLastPath(){
        if (paints.isEmpty() || paths.isEmpty()) return;
        paints.remove(paints.size() - 1);
        paths.remove(paths.size() - 1);
        invalidate();
    }

    public Bitmap getCombinedBitmap(){

        for (int i = 0; i < paths.size(); i++)
            drawCanvas.drawPath(paths.get(i), paints.get(i));

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
        initNewPaint(color);
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

    public void setWidth(int width) {
        this.width = width;
        initNewPaint(color);
    }

    protected void setType(EditTypeEvent type){
        this.typeEvent = type;
    }

}
