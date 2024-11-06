package com.example.photoredacternew.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

public class CustomPaletteView extends View {
    private int[] colors;
    private float[] positions;

    public CustomPaletteView(Context context) {
        super(context);
    }

    public CustomPaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colors != null && positions != null) {
            @SuppressLint("DrawAllocation") GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setGradientRadius(100);
            gradientDrawable.setBounds(0, 0, getWidth(), getHeight());
            gradientDrawable.draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
