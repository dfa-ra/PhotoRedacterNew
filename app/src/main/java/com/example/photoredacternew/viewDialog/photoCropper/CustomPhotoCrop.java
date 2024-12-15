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

public class CustomPhotoCrop extends CustomPhotoView {
    private RectF cropRect; // Прямоугольник обрезки
    private ChangeFrame movingEdge = null; // Сторона или угол, которую перемещаем
    private boolean startCropFlag = false;
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

        // Отрисовка прямоугольника
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        canvas.drawRect(cropRect, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            startCropFlag = false;
            return super.onTouchEvent(event); // Handle zoom and scroll in parent class
        }

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
    public void setImageBitmap(Bitmap bitmap) {
        setLimit_xy(50, 50);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        super.setImageDrawable(drawable);
        cropRect = new RectF(getDx(), getDy(), getDx() + getDrawable().getIntrinsicWidth() * getCurrentScale(), getDy() + getDrawable().getIntrinsicHeight() * getCurrentScale()); // Инициализация прямоугольника
    }

    public void reset(){
        fitImageView();
        cropRect = new RectF(getDx(), getDy(), getDx() + getDrawable().getIntrinsicWidth() * getCurrentScale(), getDy() + getDrawable().getIntrinsicHeight() * getCurrentScale()); // Инициализация прямоугольника
    }
}
