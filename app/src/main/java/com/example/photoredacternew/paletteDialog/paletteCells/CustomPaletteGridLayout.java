package com.example.photoredacternew.paletteDialog.paletteCells;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;

import com.example.photoredacternew.paletteDialog.GetSellColorCallBack;

public class CustomPaletteGridLayout extends GridLayout{

    private int white = 0xFFFFFFFF; // белый
    private int black = 0xFF000000; // черный

    // набор основных цветов (1 в 1 с тг)
    int[] headRowsColorArray = {
            0xFF00A0D7,
            0xFF0060FC,
            0xFF4C21B5,
            0xFF972BBB,
            0xFFB72C5D,
            0xFFFC3E12,

            0xFFFE6900,
            0xFFFCAA01,
            0xFFFBC501,
            0xFFFBF943,
            0xFFD8EA35,
            0xFF75B93F
    };


    private final int columnsCount = headRowsColorArray.length + 1;
    private final int rowsCount = headRowsColorArray.length;
    private CustomPaletteCellView selectedColor = null;

    private GetSellColorCallBack listener;


    public void setListener(GetSellColorCallBack listener){
        this.listener = listener;
    }

    public CustomPaletteGridLayout(Context context) {
        super(context);
        init();
    }

    public CustomPaletteGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        removeAllViews();  // Очищаем таблицу перед добавлением
        setColumnCount(columnsCount);

        post(() ->{
            for (int j = 0; j < columnsCount; j++){
                int color = interpolateColor(black, white, (float) j / (columnsCount - 1));
                setColor(color);
            }

            for (int i = 1; i < rowsCount; i++) {
                for (int j = 0; j < columnsCount; j++){
                    int color;
                    if (columnsCount % 2 == 0) {
                        if (j <= columnsCount / 2 - 1)
                            color = interpolateColor(black, headRowsColorArray[i], (float) (j + 4) / ((float) columnsCount / 2 - 1 + 5));
                        else if (j == columnsCount / 2)
                            color = headRowsColorArray[i];
                        else
                            color = interpolateColor(headRowsColorArray[i], white, (float) (j - columnsCount / 2 + 1) / ((float) columnsCount / 2 + 1 + 3));
                    }
                    else{
                        if (j <= columnsCount / 2 - 1)
                            color = interpolateColor(black, headRowsColorArray[i], (float) (j + 4) / ((float) columnsCount / 2 - 1 + 5));
                        else if (j == columnsCount / 2)
                            color = headRowsColorArray[i];
                        else
                            color = interpolateColor(headRowsColorArray[i], white, (float) (j - columnsCount / 2) / ((float) columnsCount / 2 + 3));
                    }
                    setColor(color);
                }
            }
            getChildAt(0).callOnClick();
        });
    }

    private void setColor(int color){
        CustomPaletteCellView colorView = new CustomPaletteCellView(getContext(), color);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getWidth() / columnsCount;
        params.height = getWidth() / columnsCount;
        Log.d("aa99", getWidth() + " " + columnsCount);
        colorView.setLayoutParams(params);

        // Обработчик выбора цвета
        colorView.setOnClickListener(v -> {
            if (!colorView.isSelect()) {
                if (selectedColor != null)
                    selectedColor.click();
                colorView.click();
                selectedColor = colorView;
            }
            listener.getSelectedColor(selectedColor.getColor());
        });
        // Добавляем ячейку в таблицу
        addView(colorView);
    }

    public int getSelectedColor() {
        return selectedColor.getColor();
    }

    private int interpolateColor(int colorStart, int colorEnd, float fraction) {
        int startA = (colorStart >> 24) & 0xFF;
        int startR = (colorStart >> 16) & 0xFF;
        int startG = (colorStart >> 8) & 0xFF;
        int startB = colorStart & 0xFF;

        int endA = (colorEnd >> 24) & 0xFF;
        int endR = (colorEnd >> 16) & 0xFF;
        int endG = (colorEnd >> 8) & 0xFF;
        int endB = colorEnd & 0xFF;

        int resultA = (int) (startA + (endA - startA) * fraction);
        int resultR = (int) (startR + (endR - startR) * fraction);
        int resultG = (int) (startG + (endG - startG) * fraction);
        int resultB = (int) (startB + (endB - startB) * fraction);

        return (resultA << 24) | (resultR << 16) | (resultG << 8) | resultB;
    }


}
