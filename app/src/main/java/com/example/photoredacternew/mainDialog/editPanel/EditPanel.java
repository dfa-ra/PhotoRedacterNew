package com.example.photoredacternew.mainDialog.editPanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.mainDialog.DialogsManager;
import com.example.photoredacternew.paletteDialog.CustomPaletteSheet;
import com.example.photoredacternew.databinding.EditPanelLayoutBinding;

public class EditPanel extends LinearLayout implements CustomPaletteSheet.ColorCallBack {

    private EditPanelLayoutBinding binding;
    private Drawable fill;
    private Drawable border;
    LayerDrawable layerDrawable;
    private int selected_color;

    CustomPaletteSheet customPaletteSheet;
    public EditPanel(Context context) {
        super(context);
        init(context);
    }

    public EditPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View getClose(){
        return binding.exit;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context){
        binding = EditPanelLayoutBinding.inflate(LayoutInflater.from(context), this, true);
        customPaletteSheet = new CustomPaletteSheet(this);

        fill = context.getResources().getDrawable(R.drawable.custom_fill_thumb);
        border = context.getResources().getDrawable(R.drawable.custom_border_thumb);
        Drawable[] layers = { fill, border };
        layerDrawable = new LayerDrawable(layers);
        actions();
    }

    private void actions(){
        binding.color.setOnClickListener(view -> {
            Log.d("aa99", "color.setOnClickListener");
            DialogsManager.getInstanceWithActivity().showDialog(customPaletteSheet);
        });
    }

    @Override
    public void getSelectedColor(int color) {
        fill.setColorFilter(color, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
        fill.setColorFilter(color, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
        binding.color.setBackground(layerDrawable);
    }
}
