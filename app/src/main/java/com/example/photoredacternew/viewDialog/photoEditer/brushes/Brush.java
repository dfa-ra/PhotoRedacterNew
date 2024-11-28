package com.example.photoredacternew.viewDialog.photoEditer.brushes;

import android.graphics.Paint;

public class Brush extends Paint {
    public Brush(int color, Paint.Style style, int width){
        setColor(color);
        setStyle(style);
        setStrokeWidth(width);
    }
}
