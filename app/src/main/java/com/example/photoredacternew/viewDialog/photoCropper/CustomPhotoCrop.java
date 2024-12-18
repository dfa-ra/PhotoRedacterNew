package com.example.photoredacternew.viewDialog.photoCropper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.photoredacternew.viewDialog.photoViewer.CustomPhotoView;


/**
 * Класс оттвечающий за основной лайаути на котором отображается фото при обрезани
 * т.е. именно то место куда вставляется само фото
 */
public class CustomPhotoCrop extends CustomPhotoView {

    private RectF cropRect; // Прямоугольник обрезки
    private ChangeFrame movingEdge = null; // Сторона или угол, которую перемещаем

    private boolean startCropFlag = false; // нужен чтобы обрабатывать ложные нажатия

    public CustomPhotoCrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCropTools();
    }

    private void initCropTools() {
        cropRect = new RectF(0, 0, 0, 0); // Инициализация прямоугольника
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Отрисовка затемнения за пределами cropRect
        Paint dimPaint = new Paint();
        dimPaint.setColor(Color.BLACK);
        dimPaint.setAlpha(150); // Полупрозрачный черный цвет (0-255)

        // Левые, верхние, правые и нижние затемненные области
        canvas.drawRect(0, 0, canvas.getWidth(), cropRect.top, dimPaint); // Верхняя область
        canvas.drawRect(0, cropRect.top, cropRect.left, cropRect.bottom, dimPaint); // Левая область
        canvas.drawRect(cropRect.right, cropRect.top, canvas.getWidth(), cropRect.bottom, dimPaint); // Правая область
        canvas.drawRect(0, cropRect.bottom, canvas.getWidth(), canvas.getHeight(), dimPaint); // Нижняя область

        // Отрисовка прямоугольника обрезки
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(15);
        borderPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        canvas.drawRect(cropRect, borderPaint);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // если болле одного палььца то мы реализуем onTouch родителя
        if (event.getPointerCount() > 1) {
            startCropFlag = false;
            return super.onTouchEvent(event);
        }

        // если один палец то мы меняем рамку
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                movingEdge = findNearbyEdge(event.getX(), event.getY());
                startCropFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!startCropFlag) break;
                if (movingEdge != null) {
                    adjustRect(event.getX(), event.getY());
                    invalidate(); // Перерисовка
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!startCropFlag) break;
                movingEdge = null; // Отпустили точку
                startCropFlag = false;
                break;
        }

        return true;


    }


    private ChangeFrame findNearbyEdge(float x, float y) {
        // Радиус касания для выбора точки
        float touchRadius = 50;

        // выбор действия с рамкой
        if (Math.hypot(cropRect.left - x, cropRect.top - y) <= touchRadius) {
            return ChangeFrame.TOP_LEFT;
        } else if (Math.hypot(cropRect.right - x, cropRect.top - y) <= touchRadius) {
            return ChangeFrame.TOP_RIGHT;
        } else if (Math.hypot(cropRect.right - x, cropRect.bottom - y) <= touchRadius) {
            return ChangeFrame.BOTTOM_RIGHT;
        } else if (Math.hypot(cropRect.left - x, cropRect.bottom - y) <= touchRadius) {
            return ChangeFrame.BOTTOM_LEFT;
        } else if (Math.abs(cropRect.left - x) <= touchRadius && y > cropRect.top && y < cropRect.bottom) {
            return ChangeFrame.LEFT;
        } else if (Math.abs(cropRect.right - x) <= touchRadius && y > cropRect.top && y < cropRect.bottom) {
            return ChangeFrame.RIGHT;
        } else if (Math.abs(cropRect.top - y) <= touchRadius && x > cropRect.left && x < cropRect.right) {
            return ChangeFrame.TOP;
        } else if (Math.abs(cropRect.bottom - y) <= touchRadius && x > cropRect.left && x < cropRect.right) {
            return ChangeFrame.BOTTOM;
        } else if (
                Math.hypot(cropRect.left - x, cropRect.bottom - y) <= Math.hypot(cropRect.right - x, cropRect.bottom - y) &&
                Math.hypot(cropRect.left - x, cropRect.bottom - y) <= Math.hypot(cropRect.right - x, cropRect.top - y) &&
                Math.hypot(cropRect.left - x, cropRect.bottom - y) <= Math.hypot(cropRect.left - x, cropRect.top - y)
        ){
            return ChangeFrame.NEW_POINT_LEFT_BOTTOM;
        }else if (
                Math.hypot(cropRect.right - x, cropRect.bottom - y) <= Math.hypot(cropRect.left - x, cropRect.bottom - y) &&
                Math.hypot(cropRect.right - x, cropRect.bottom - y) <= Math.hypot(cropRect.right - x, cropRect.top - y) &&
                Math.hypot(cropRect.right - x, cropRect.bottom - y) <= Math.hypot(cropRect.left - x, cropRect.top - y)
        ){
            return ChangeFrame.NEW_POINT_RIGHT_BOTTOM;
        }else if (
                Math.hypot(cropRect.left - x, cropRect.top - y) <= Math.hypot(cropRect.right - x, cropRect.bottom - y) &&
                Math.hypot(cropRect.left - x, cropRect.top - y) <= Math.hypot(cropRect.right - x, cropRect.top - y) &&
                Math.hypot(cropRect.left - x, cropRect.top - y) <= Math.hypot(cropRect.left - x, cropRect.bottom - y)
        ){
            return ChangeFrame.NEW_POINT_LEFT_TOP;
        }else if (
                Math.hypot(cropRect.right - x, cropRect.top - y) <= Math.hypot(cropRect.right - x, cropRect.bottom - y) &&
                Math.hypot(cropRect.right - x, cropRect.top - y) <= Math.hypot(cropRect.left - x, cropRect.bottom - y) &&
                Math.hypot(cropRect.right - x, cropRect.top - y) <= Math.hypot(cropRect.left - x, cropRect.top - y)
        ){
            return ChangeFrame.NEW_POINT_RIGHT_TOP;
        }
        return null;
    }

    // изменение рамки обрезания
    private void adjustRect(float x, float y) {
        if (movingEdge != null) {
            switch (movingEdge) {
                case TOP_LEFT:
                case NEW_POINT_LEFT_TOP:
                    cropRect.left = x;
                    cropRect.top = y;
                    break;
                case TOP_RIGHT:
                case NEW_POINT_RIGHT_TOP:

                    cropRect.right = x;
                    cropRect.top = y;
                    break;
                case BOTTOM_RIGHT:
                case NEW_POINT_RIGHT_BOTTOM:

                    cropRect.right = x;
                    cropRect.bottom = y;
                    break;
                case BOTTOM_LEFT:
                case NEW_POINT_LEFT_BOTTOM:
                    cropRect.left = x;
                    cropRect.bottom = y;
                    break;
                case LEFT:
                    cropRect.left = x;
                    break;
                case RIGHT:
                    cropRect.right = x;
                    break;
                case TOP:
                    cropRect.top = y;
                    break;
                case BOTTOM:
                    cropRect.bottom = y;
                    break;
            }
        }
        // Убедитесь, что прямоугольник корректный
        cropRect.sort();
    }

    // реализация самого обрезания изображения
    public Bitmap cropImage() {
        Drawable drawable = getDrawable();
        if (!(drawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        float[] values = new float[9];
        matrix.getValues(values);

        float scale = values[Matrix.MSCALE_X];
        float dx = values[Matrix.MTRANS_X];
        float dy = values[Matrix.MTRANS_Y];

        // Преобразуем координаты прямоугольника в координаты исходного изображения
        int left = (int) ((cropRect.left - dx) / scale);
        int top = (int) ((cropRect.top - dy) / scale);
        int right = (int) ((cropRect.right - dx) / scale);
        int bottom = (int) ((cropRect.bottom - dy) / scale);

        // Проверяем границы
        left = Math.max(0, left);
        top = Math.max(0, top);
        right = Math.min(bitmap.getWidth(), right);
        bottom = Math.min(bitmap.getHeight(), bottom);

        if (right <= left || bottom <= top) {
            return null; // Неверные границы обрезки
        }

        // Создаем обрезанное изображение
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);
    }

    @Override
    // установка изображения в основной лайаут
    public void setImageBitmap(Bitmap bitmap) {
        setLimit_xy(50, 50);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        super.setImageDrawable(drawable);
        cropRect = new RectF(getDx(), getDy(), getDx() + getDrawable().getIntrinsicWidth() * getCurrentScale(), getDy() + getDrawable().getIntrinsicHeight() * getCurrentScale()); // Инициализация прямоугольника
    }

    // сбор всех изменений
    public void reset(){
        fitImageView();
        cropRect = new RectF(getDx(), getDy(), getDx() + getDrawable().getIntrinsicWidth() * getCurrentScale(), getDy() + getDrawable().getIntrinsicHeight() * getCurrentScale()); // Инициализация прямоугольника
    }
}
