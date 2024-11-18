package com.example.photoredacternew.mainDialogs.editPanel;

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
import androidx.fragment.app.FragmentActivity;

import com.example.photoredacternew.R;
import com.example.photoredacternew.mainDialogs.DialogsManager;
import com.example.photoredacternew.mainDialogs.paletteDialog.CustomObservers.CollorObserver;
import com.example.photoredacternew.mainDialogs.paletteDialog.DrawColor;
import com.example.photoredacternew.mainDialogs.paletteDialog.paletteCells.CustomPaletteSheet;
import com.example.photoredacternew.databinding.EditPanelLayoutBinding;

import io.reactivex.rxjava3.annotations.NonNull;

public class EditPanel extends LinearLayout {

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
        customPaletteSheet = new CustomPaletteSheet();

        fill = context.getResources().getDrawable(R.drawable.custom_fill_thumb);
        border = context.getResources().getDrawable(R.drawable.custom_border_thumb);
        Drawable[] layers = { fill, border };
        layerDrawable = new LayerDrawable(layers);
        actions();
        observer();
    }

    private void actions(){
        binding.color.setOnClickListener(view -> {
            Log.d("aa99", "color.setOnClickListener");
            DialogsManager.getInstanceWithActivity().showDialog(customPaletteSheet);
        });
    }

    private void observer(){
        DrawColor.getInstance().getColor().subscribe(new CollorObserver() {
            @Override
            public void onNext(@NonNull Integer integer) {
                fill.setColorFilter(integer, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
                fill.setColorFilter(integer, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
                binding.color.setBackground(layerDrawable);
            }
        });
    }

}
