package com.example.photoredacternew.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.drawerlayout.widget.DrawerLayout;

public class CustomPhotoView extends AppCompatImageView {

    private Matrix matrix;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private float minScale = 2f;
    private float maxScale = 10.0f;
    private float doubleTapZoomScale = 0.1f;

    private boolean isZoomed = false;

    private float currentScale = 2.0f;

    public CustomPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context){
        matrix = new Matrix();

        setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("aa99","onDraw");
        super.onDraw(canvas);
        if (getDrawable() != null){
            canvas.save();
            canvas.concat(matrix);
            getDrawable().setBounds(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
            getDrawable().draw(canvas);
            canvas.restore();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("aa99", "onTouchEvent: " + event.getAction());
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        fitImageView();
    }

    private void fitImageView(){
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();

        float scale, dx, dy;
        if (viewWidth / drawableWidth < viewHeight / drawableHeight){
            scale = viewWidth / drawableWidth;
            currentScale = scale;
            dx = 0;
            dy = (viewHeight - drawableHeight * scale) / 2;
        }
        else {
            scale = viewHeight / drawableHeight;
            currentScale = scale;
            dx = (viewWidth - drawableWidth * scale) / 2;
            dy = 0;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        setImageMatrix(matrix);
    }


    private void checkBounds(){
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        RectF bounds = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(bounds);

        float offsetX = 0, offsetY = 0;
        if (bounds.width() > getWidth()) {
            if (bounds.left > 0) offsetX = -bounds.left;
            else if (bounds.right < getWidth()) offsetX = getWidth() - bounds.right;
        }
        else {
            offsetX = (getWidth() - bounds.width()) / 2 - bounds.left;
        }
        if (bounds.height() > getHeight()) {
            if (bounds.top > 0) offsetY = -bounds.top;
            else if (bounds.bottom < getHeight()) offsetY = getHeight() - bounds.bottom;
        }
        else {
            offsetY = (getHeight() - bounds.height()) / 2 - bounds.top;
        }
        matrix.postTranslate(offsetX, offsetY);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScale = currentScale * scaleFactor;

            Log.d("aa99", "1) scaleFactor: " + scaleFactor + " currentScale: " + currentScale);

            // Ограничиваем новый масштаб в пределах minScale и maxScale
            if (newScale > minScale && newScale < maxScale) {
                matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                currentScale = newScale;
                checkBounds();
                setImageMatrix(matrix);
                Log.d("aa99", "3) currentScale после обновления: " + currentScale);
            }
            // Ограничиваем изображение в пределах видимой области
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            matrix.postTranslate(-distanceX, -distanceY);
            checkBounds();
            setImageMatrix(matrix);
            return true;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            animateZoom(e.getX(), e.getY());
            return true;
        }
    }

    // Метод для плавной анимации зума
    private void animateZoom(float x, float y) {
        float startScale = currentScale;
        float endScale = isZoomed ? 1f : doubleTapZoomScale;
        isZoomed = !isZoomed;

        ValueAnimator animator = ValueAnimator.ofFloat(startScale, endScale);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            float scaleFactor = animatedValue / startScale;

            matrix.set(getImageMatrix());
            matrix.postScale(scaleFactor, scaleFactor, x, y);
            checkBounds();
            setImageMatrix(matrix);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentScale = endScale;
            }
        });

        animator.start();
    }
}
