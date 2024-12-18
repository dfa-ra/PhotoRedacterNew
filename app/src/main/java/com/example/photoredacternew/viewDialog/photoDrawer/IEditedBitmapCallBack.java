package com.example.photoredacternew.viewDialog.photoDrawer;

import android.graphics.Bitmap;

/**
 * Интерфейс callback
 */
public interface IEditedBitmapCallBack {
    void onDrawnBitmap(Bitmap bitmap);
    void onCroppedBitmap(Bitmap bitmap);
}
