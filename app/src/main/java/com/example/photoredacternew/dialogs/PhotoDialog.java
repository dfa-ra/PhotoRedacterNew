package com.example.photoredacternew.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.photoredacternew.CustomSamples.customView.CustomPaletteSheet;
import com.example.photoredacternew.databinding.DialogPhotoViewBinding;

/**
 * далог для рассматривания изображений
 */

public class PhotoDialog extends Dialog {

    private final DialogPhotoViewBinding binding;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhotoDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.context = context;
        // во весь экран
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        this.binding = DialogPhotoViewBinding.inflate(LayoutInflater.from(getContext()));
        this.setContentView(binding.getRoot());
    }

    @Override
    public void show() {
        super.show();

        //попрятать навигацию и все лишнее
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // отобразить загрузку
        this.binding.loadAnimation.setVisibility(View.VISIBLE);

        // закрыть диалог
        this.binding.destroy.setOnClickListener(clc -> this.dismiss());

        // кнопка открытия палитры
        binding.edit.setOnClickListener(view -> {
            binding.destroy.setVisibility(View.GONE);
            binding.edit.setVisibility(View.GONE);
            binding.editPanel.setVisibility(View.VISIBLE);
        });
    }

    public void open () {
        this.show();
    }

    // отрисовать изображение
    public void drawPhoto(BitmapDrawable photo) {
        Log.d("aa99", "start drawPhoto");
        // спрятать загрузку
        this.binding.loadAnimation.setVisibility(View.GONE);

        // отрисовать фотку
        this.binding.fullImageView.setVisibility(View.VISIBLE);
        this.binding.fullImageView.setImageDrawable(photo);


        Log.d("aa99", "end drawPhoto");
    }

    // билдер
    public static PhotoDialog getInstance(Context context){
        return new PhotoDialog(context);
    }

}
