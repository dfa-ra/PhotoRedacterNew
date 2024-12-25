package com.example.photoredacternew.viewDialog.photoDrawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
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

/**
 * Класс отвещающий за основное место для рисования
 * т.е. это лайаут в котором есть само изображение и канвас на котором рисуют по верху
 */
public class CustomPhotoDraw extends CustomPhotoView {

    private Bitmap drawBitmap;
    private Paint currentPaint;
    private Path currentPath;
    private int currentColor = Color.RED;
    private float strokeWidth = 5;
    private EditTypeEvent currentEvent = EditTypeEvent.NONE;
    private EditTypeEvent oldEvent = EditTypeEvent.NONE;

    // пути и кисти для этих путей
    private final List<Path> paths = new ArrayList<>();
    private final List<Paint> paints = new ArrayList<>();

    public CustomPhotoDraw(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    // инициалиация основных параметров
    private void initPaint() {
        currentPaint = new Paint();
        currentPaint.setColor(currentColor);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(strokeWidth);
        currentPaint.setAntiAlias(true);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        createDrawBitmap(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        super.setImageDrawable(drawable);
        createDrawBitmap(drawable);
    }

    // создание битммапы для рисования
    private void createDrawBitmap(Drawable drawable) {
        if (drawable == null) return;

        drawBitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawBitmap != null) {
            canvas.save();
            canvas.concat(matrix);
            canvas.drawBitmap(drawBitmap, 0, 0, null);
            canvas.restore();
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.save();
            canvas.concat(matrix);
            canvas.drawPath(paths.get(i), paints.get(i));
            canvas.restore();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // если пальцев более одного то реализуем onTouch родителя
        if (event.getPointerCount() > 1) {
            currentEvent = EditTypeEvent.NONE;
            return super.onTouchEvent(event);
        }

        // проверка на нежелательные вызовы onTouch
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentEvent = oldEvent;
        }

        switch (currentEvent) {
            case DRAW:
                return handleDraw(event);
            case ERASE:
                return handleErase(event);
            case NONE:
            default:
                return super.onTouchEvent(event);
        }
    }

    // управление рисованием + реализация самого рисования
    private boolean handleDraw(MotionEvent event) {

        float[] touchPoint = {event.getX(), event.getY()};
        Matrix inverseMatrix = new Matrix();
        matrix.invert(inverseMatrix);
        inverseMatrix.mapPoints(touchPoint);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                currentPath.moveTo(touchPoint[0], touchPoint[1]);
                paths.add(currentPath);

                Paint newPaint = new Paint(currentPaint);
                paints.add(newPaint);
                return true;

            case MotionEvent.ACTION_MOVE:
                oldEvent = currentEvent;
                if (currentPath != null) {
                    currentPath.lineTo(touchPoint[0], touchPoint[1]);
                    invalidate();
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }


    // управление стиранием
    private boolean handleErase(MotionEvent event){
        float[] touchPoint = {event.getX(), event.getY()};
        Matrix inverseMatrix = new Matrix();
        matrix.invert(inverseMatrix);
        inverseMatrix.mapPoints(touchPoint);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                erasePathAtPoint(touchPoint[0], touchPoint[1]);
                return true;
        }
        return super.onTouchEvent(event);
    }

    // метод стирания
    private void erasePathAtPoint(float x, float y) {
        Log.d("aa88", "eraser");

        // Радиус точности попадания
        float touchRadius = 5;

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

    public void setColor(int color) {
        currentColor = color;
        initPaint();
    }

    public void setStrokeWidth(int width) {
        strokeWidth = adjustBrushSize(width);
        initPaint();
    }

    private float getScaleFromMatrix() {
        float[] values = new float[9];
        matrix.getValues(values);

        // Значения scaleX и scaleY (обычно хранятся в элементах 0 и 4 массива)
        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];

        // Возвращаем среднее значение масштаба для лучшего масштабирования
        return (scaleX + scaleY) / 2;
    }

    // изменение размера кисти
    private float adjustBrushSize(float baseBrushWidth) {
        return baseBrushWidth / getScaleFromMatrix();
    }

    // метод отчистки всего
    public void clearAll(){
        paths.clear();
        paints.clear();
        if (drawBitmap != null) {
            drawBitmap.eraseColor(Color.TRANSPARENT);
        }
        invalidate();
    }

    // отменить последнее действие рисования
    public void undoLastPath() {
        if (!paths.isEmpty() && !paints.isEmpty()) {
            paths.remove(paths.size() - 1);
            paints.remove(paints.size() - 1);
            invalidate();
        }
    }

    // собрать основной битмап и битмап для рисование в один итоговый битмап
    public Bitmap getCombinedBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap baseBitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap combinedBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the base image
        canvas.drawBitmap(baseBitmap, 0, 0, null);

        // Draw the drawn paths
        if (drawBitmap != null) {
            canvas.drawBitmap(drawBitmap, 0, 0, null);
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        return combinedBitmap;
    }


    public void setCurrentEvent(EditTypeEvent currentEvent) {
        this.currentEvent = currentEvent;
        oldEvent = currentEvent;
    }
}


