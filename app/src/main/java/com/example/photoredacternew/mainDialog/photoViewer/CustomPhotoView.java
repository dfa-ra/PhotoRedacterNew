package com.example.photoredacternew.mainDialog.photoViewer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class CustomPhotoView extends AppCompatImageView {

    private Matrix matrix;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float minScale = 0f;
    private float maxScale = 0f;
    private float doubleTapZoomScale = 0f;
    private boolean isZoomed = false;
    private float currentScale = 0f;

    public CustomPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context) {
        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() != null) {
            canvas.save();
            canvas.concat(matrix);
            getDrawable().setBounds(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
            getDrawable().draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }


    // переопределяем setImageDrawable для того чтобы при добавлении фото оно выровнялось
    // по ширине или высоте экрана... так же определяем размеры максимума и минимума увеличения
    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable != null) {
            fitImageView();
            minScale = currentScale;
            maxScale = currentScale * 4;
            doubleTapZoomScale = currentScale; // на сколько увеличивается зум при двойном нажатии
        }
    }

    // метод выравнивания фото по высоте или ширине
    private void fitImageView() {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();

        float scale, dx, dy;
        if (viewWidth / drawableWidth < viewHeight / drawableHeight) {
            scale = viewWidth / drawableWidth;
            dx = 0;
            dy = (viewHeight - drawableHeight * scale) / 2;
        } else {
            scale = viewHeight / drawableHeight;
            dx = (viewWidth - drawableWidth * scale) / 2;
            dy = 0;
        }

        currentScale = scale;  // Устанавливаем начальный масштаб
        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        setImageMatrix(matrix);  // Устанавливаем матрицу только один раз
    }

    // проверка на всякие ограничения
    private void checkBounds() {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        RectF bounds = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(bounds);

        float offsetX = 0, offsetY = 0;
        if (bounds.width() > getWidth()) {
            if (bounds.left > 0) offsetX = -bounds.left;
            else if (bounds.right < getWidth()) offsetX = getWidth() - bounds.right;
        } else {
            offsetX = (getWidth() - bounds.width()) / 2 - bounds.left;
        }
        if (bounds.height() > getHeight()) {
            if (bounds.top > 0) offsetY = -bounds.top;
            else if (bounds.bottom < getHeight()) offsetY = getHeight() - bounds.bottom;
        } else {
            offsetY = (getHeight() - bounds.height()) / 2 - bounds.top;
        }
        matrix.postTranslate(offsetX, offsetY);
    }

    // Listener зума с помощью 2 пальцев
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScale = currentScale * scaleFactor;

            if (newScale > minScale && newScale < maxScale) {
                matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                currentScale = newScale;
                checkBounds();
                setImageMatrix(matrix);
            }

            Log.d("aa99","currentScale: " + currentScale);
            return true;
        }
    }

    // скалиррование двоййным надатием и скрол фото
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            matrix.postTranslate(-distanceX, -distanceY);
            checkBounds();
            setImageMatrix(matrix);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            animateZoom(e.getX(), e.getY());
            return true;
        }
    }

    // анимация зума
    private void animateZoom(float focusX, float focusY) {
        float startScale = currentScale;
        float endScale = isZoomed ? minScale : currentScale + doubleTapZoomScale;
        isZoomed = !isZoomed;

        ValueAnimator animator = ValueAnimator.ofFloat(startScale, endScale);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            Log.d("aa99","-----------");
            float animatedValue = (float) animation.getAnimatedValue();
            Log.d("aa99", "animatedValue: " + animatedValue);
            Log.d("aa99", "startScale: " + startScale);
            float scaleFactor = animatedValue / currentScale;
            Log.d("aa99", "scaleFactor: " + scaleFactor);


            // Устанавливаем текущую матрицу и масштабируем относительно точки нажатия
            if (animatedValue > minScale && animatedValue < maxScale) {
                matrix.set(getImageMatrix());
                matrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                currentScale = animatedValue;
                checkBounds();
                setImageMatrix(matrix);
            }
            Log.d("aa99","currentScale: " + currentScale);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isZoomed) {
                    fitImageView();
                }
            }
        });

        animator.start();
    }
}
