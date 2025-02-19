package com.example.photoredacternew.paletteDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.ColorPaletteViewBinding;
import com.example.photoredacternew.paletteDialog.paletteCells.CustomPaletteGridLayout;
import com.example.photoredacternew.paletteDialog.transparencySeekBar.TransparencySeekBarView;
import com.example.photoredacternew.viewDialog.PhotoDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

/**
 * Класс отвечающий за диалог палитры
 */
public class CustomPaletteSheet extends BottomSheetDialogFragment implements CustomPaletteGridLayout.SellColorCallBack, TransparencySeekBarView.UpdateTransparencyCallBack {
    private ColorPaletteViewBinding binding;

    // кастомное вью ползунка отвечающего за прозрачность
    private TransparencySeekBarView transparencySeekBar;

    // кастомное вью отвечающее за саму таблицу палитры
    private CustomPaletteGridLayout paletteGridLayout;

    private ColorCallBack listener;

    // итоговый выбранный цвет
    private int selectedColor;


    public CustomPaletteSheet(ColorCallBack listener){
        this.listener = listener;
    }

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

        paletteGridLayout = binding.paletteTable;
        transparencySeekBar = binding.layoutSeekBar;

        setListeners();

        binding.layoutSeekBar.setupBrightnessSeekBar();
    }

    private void setListeners(){
        paletteGridLayout.setListener(this);
        transparencySeekBar.setListner(this);
    }

    @Override
    public void onSelectedColor(int color) {
        selectedColor = color;
        transparencySeekBar.setColor(color);
    }

    @Override
    public void onUpdateTransparency(int color) {
        selectedColor = color;
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

    @Override
    public void onPause() {
        super.onPause();
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.getSelectedColor(selectedColor);
        super.onDismiss(dialog);
        Log.d("aa99", "Диалог закрыт");
    }

    public interface ColorCallBack {
        void getSelectedColor(int color);
    }

}
