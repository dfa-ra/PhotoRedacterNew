package com.example.photoredacternew.editDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.photoredacternew.databinding.DialogPhotoEditBinding;
import com.example.photoredacternew.databinding.DialogPhotoViewBinding;
import com.example.photoredacternew.viewDialog.PhotoDialog;

public class EditDialog extends Dialog {

    private DialogPhotoEditBinding binding;
    private Bitmap bitmap;

    public EditDialog(@NonNull Context context, Bitmap bitmap) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        this.bitmap = bitmap;
        binding = DialogPhotoEditBinding.inflate(LayoutInflater.from(context));

        this.setContentView(binding.getRoot());
        init(context);
    }

    private void init(Context context){
        // во весь экран

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

        // закрыть диалог
        this.binding.destroy.setOnClickListener(clc -> this.dismiss());

        binding.fullImageDraw.setBitmap(bitmap);
    }

    // билдер
    public static EditDialog getInstance(Context context, Bitmap bitmap){
        return new EditDialog(context, bitmap);
    }

}
