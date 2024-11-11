package com.example.photoredacternew.CustomSamples.customView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.photoredacternew.CustomSamples.GetSellColorCallBack;
import com.example.photoredacternew.databinding.ColorPaletteViewBinding;
import com.google.android.material.tabs.TabLayout;

public class CustomPaletteView extends LinearLayout implements GetSellColorCallBack {

    private GridLayout colorTable;
    private SeekBar brightnessSeekBar;
    private int selectedColor;
    private ColorPaletteViewBinding binding;

    // Конструктор с Context и AttributeSet для правильной инициализации из XML
    public CustomPaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context); // Инициализация
    }

    // Конструктор для использования только в коде (например, при динамическом добавлении вью)
    public CustomPaletteView(Context context) {
        super(context);
        init(context); // Инициализация
    }

    // Метод инициализации, который настраивает виджет
    private void init(Context context) {
        binding = ColorPaletteViewBinding.inflate(LayoutInflater.from(context), this, true);

        // Инициализация компонентов
        binding.paletteTable.setGetSellColorCallBack(this);
        binding.tabLayout.setSelectedTabIndicatorHeight(0);
        ;
        brightnessSeekBar = binding.brightnessSeekBar;


        // Настройка вкладок TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Палитра"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Обработка вкладки
                if (tab.getPosition() == 0) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        setupBrightnessSeekBar();


    }


    // Настройка SeekBar для яркости
    private void setupBrightnessSeekBar() {
        brightnessSeekBar.setMax(100);  // Установка максимальной яркости
        brightnessSeekBar.setProgress(100);  // Установка начальной яркости

        // Обработчик изменений в SeekBar
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("aa99", "color: " + selectedColor);
                selectedColor = adjustAlpha(selectedColor, progress);
                setCrawler(selectedColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    // Метод для регулировки яркости цвета
    private int adjustBrightness(int color, int brightness) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = brightness / 100f;
        return Color.HSVToColor(hsv);
    }

    private int adjustAlpha(int color, int alpha) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        return Color.HSVToColor(255 / 100 * alpha, hsv);
    }

    @Override
    public void getSelectedColor(int color) {
        selectedColor = color;
        setSeekBg(color);
        setCrawler(color);
        brightnessSeekBar.setProgress(100);
    }


    private void setSeekBg(int color){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#00000000"), color}
        );
        gradientDrawable.setCornerRadius(3);
        brightnessSeekBar.setProgressDrawable(gradientDrawable);
    }

    private void setCrawler(int color){
        ShapeDrawable seekPoint = new ShapeDrawable(new OvalShape());
        seekPoint.getPaint().setColor(color);
        seekPoint.setIntrinsicHeight(100);
        seekPoint.setIntrinsicWidth(100);

        brightnessSeekBar.setThumb(seekPoint);
    }
}
