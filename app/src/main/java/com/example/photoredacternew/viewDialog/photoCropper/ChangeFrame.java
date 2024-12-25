package com.example.photoredacternew.viewDialog.photoCropper;

/**
 * Класс констант для определения вида изменения камки обрезки фото
 */
public enum ChangeFrame {
    // стороны
    TOP,
    LEFT,
    RIGHT,
    BOTTOM,
    // углы
    TOP_RIGHT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    TOP_LEFT,
    // перемещение угла в новую точку нажатия
    NEW_POINT_RIGHT_TOP,
    NEW_POINT_LEFT_BOTTOM,
    NEW_POINT_RIGHT_BOTTOM,
    NEW_POINT_LEFT_TOP;
}
