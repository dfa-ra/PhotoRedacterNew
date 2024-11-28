package com.example.photoredacternew.viewDialog.photoEditer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.EditPanelLayoutBinding;

public class EditPanel extends LinearLayout{

    public EditPanelLayoutBinding binding;
    private Drawable fill;
    private Drawable border;
    private LayerDrawable layerDrawable;
    protected int selected_color;

    public EditPanel(Context context) {
        super(context);
        init(context);
    }

    public EditPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context){
        binding = EditPanelLayoutBinding.inflate(LayoutInflater.from(context), this, true);

        fill = context.getResources().getDrawable(R.drawable.custom_fill_thumb);
        border = context.getResources().getDrawable(R.drawable.custom_border_thumb);
        Drawable[] layers = { fill, border };
        layerDrawable = new LayerDrawable(layers);
        actions();
    }

    private void actions(){

    }

    protected void setSelectedColor(int color) {
        fill.setColorFilter(color, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
        fill.setColorFilter(color, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
        binding.color.setBackground(layerDrawable);
    }
}
