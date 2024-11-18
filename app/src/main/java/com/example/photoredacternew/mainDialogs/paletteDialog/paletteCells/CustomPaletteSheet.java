package com.example.photoredacternew.mainDialogs.paletteDialog.paletteCells;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.ColorPaletteViewBinding;
import com.example.photoredacternew.mainDialogs.paletteDialog.DrawColor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class CustomPaletteSheet extends BottomSheetDialogFragment implements GetSellColorCallBack {

    private SeekBar transparencySeekBar;
    private int selectedColor;
    private ColorPaletteViewBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ColorPaletteViewBinding.inflate(inflater, container, false);
        init(getContext());
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.NoBottomSpaceBottomSheetDialog;
    }


    // Метод инициализации, который настраивает виджет
    private void init(Context context) {
        binding = ColorPaletteViewBinding.inflate(LayoutInflater.from(context));

        // Инициализация компонентов
        binding.paletteTable.setGetSellColorCallBack(this);
        binding.tabLayout.setSelectedTabIndicatorHeight(0);

        transparencySeekBar = binding.layoutSeekBar.binding.transparencySeekBar;


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

        binding.layoutSeekBar.setupBrightnessSeekBar();

    }

    // Метод для регулировки яркости цвета
    private int adjustBrightness(int color, int brightness) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = brightness / 100f;
        return Color.HSVToColor(hsv);
    }

    @Override
    public void getSelectedColor(int color) {
        Log.d("aa99", "color: " + color);
        binding.layoutSeekBar.setSeekBarTrackColor(color);
        DrawColor.getInstance().setColor(color);
        transparencySeekBar.setProgress(100);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    public int getSelectedColor() {
        return selectedColor;
    }

    @Override
    public void onPause() {
        super.onPause();
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("aa99", "Диалог закрыт");
    }
}
