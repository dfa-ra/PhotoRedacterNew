package com.example.photoredacternew.viewDialog.photoEditer.customView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

@SuppressLint("AppCompatCustomView")
public class VerticalSeekBar extends SeekBar {

    private boolean active = false;
    private GetWidthCallBack listener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // Поворачиваем холст на 90 градусов против часовой стрелки
        canvas.rotate(-90);
        canvas.translate(-getHeight(), 0);

        // Отрисовываем SeekBar
        super.onDraw(canvas);
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        // Принудительное обновление позиции thumb
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Меняем местами ширину и высоту
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
        invalidate(); // Обновляем отрисовку thumb
    }


    @Override
    public boolean performClick() {
        // Для обеспечения корректной работы Accessibility
        return super.performClick();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || !active) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                // Получаем Y-координату события
                float y = event.getY();

                // Преобразуем Y в значение прогресса
                float scale = 1 - (y / (float) getHeight()); // Инвертируем ось
                scale = Math.max(0f, Math.min(scale, 1f)); // Ограничиваем в диапазоне [0, 1]

                int progress = (int) (scale * getMax()); // Преобразуем в значение прогресса
                setProgress(progress); // Устанавливаем прогресс
                listener.getWidth(progress);
                performClick(); // Для Accessibility
                return true;

            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return super.onTouchEvent(event);
    }


    // Анимация для выдвижения/защелкивания SeekBar
    public void animateSeekBar(VerticalSeekBar seekBar, float targetTranslationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(seekBar, "translationX", targetTranslationX);
        animator.setDuration(200); // продолжительность анимации
        animator.start();
    }

    public void activateSeekBar(){
        if (getTranslationX() <= -15f) {
            // Если SeekBar частично скрыт, показываем его полностью
            animateSeekBar(this,  0f);
            this.active = true;
        }
    }

    public void deactivateSeekBar(){
        if (getTranslationX() == 0f) {
            // Если SeekBar частично скрыт, показываем его полностью
            animateSeekBar(this,  -60f);
            this.active = false;
        }
    }

    public void hideSeekBar(){
        animateSeekBar(this,  -130f);
        this.active = false;
    }

    public void showSeekBar(){
        animateSeekBar(this,  0f);
        this.active = true;
    }


    public void setListener(GetWidthCallBack listener) {
        this.listener = listener;
    }

    public interface GetWidthCallBack{
        void getWidth(int width);
    }

}
