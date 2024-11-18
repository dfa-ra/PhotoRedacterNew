package com.example.photoredacternew.mainDialogs.paletteDialog;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class DrawColor extends ViewModel {
    private static DrawColor drawColor = null;
    private BehaviorSubject<Integer> color = BehaviorSubject.create();

    private DrawColor(){

    }

    public static DrawColor getInstance(){
        if (drawColor == null) drawColor = new DrawColor();
        return drawColor;
    }

    public void setColor(Integer color) {

        Log.d("aa99", "set color: " + color);
        this.color.onNext(color);
    }

    public BehaviorSubject<Integer> getColor() {
        return color;
    }
}
