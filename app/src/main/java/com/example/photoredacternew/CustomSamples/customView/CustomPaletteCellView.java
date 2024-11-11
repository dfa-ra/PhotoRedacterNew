package com.example.photoredacternew.CustomSamples.customView;
import android.util.AttributeSet;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomPaletteCellView extends View {
    private boolean isSelect = false;
    private int color;
    private int alpha = 0;
    private Paint border;

    public CustomPaletteCellView(Context context, @Nullable AttributeSet attrs, int color) {
        super(context, attrs);
        this.color = color;
        init();
    }

    public CustomPaletteCellView(Context context, int color) {
        super(context);
        this.color = color;
        init();
    }

    private void init(){
        setBackgroundColor(color);
        border = new Paint();
        if (getBrightness(color) >= 170)
            border.setColor(Color.BLACK);
        else
            border.setColor(Color.WHITE);
        border.setStrokeWidth(8f);
        border.setStyle(Paint.Style.STROKE);
        border.setAlpha(alpha);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(2, 2, getWidth() - 2, getHeight() - 1, border);
    }

    public void click(){
        toggleBorder();
    }
    private void toggleBorder() {
        int targetAlpha = isSelect ? 0 : 255;
        ValueAnimator animator = ValueAnimator.ofInt(alpha, targetAlpha);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            alpha = (int) animation.getAnimatedValue();
            border.setAlpha(alpha);
            invalidate();
        });
        animator.start();

        isSelect = !isSelect;
    }

    public int getColor() {
        if (isSelect) return color;
        return -1;
    }

    public static int getBrightness(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return (int) (0.299 * red + 0.587 * green + 0.114 * blue);
    }

    public boolean isSelect() {
        return isSelect;
    }
}
