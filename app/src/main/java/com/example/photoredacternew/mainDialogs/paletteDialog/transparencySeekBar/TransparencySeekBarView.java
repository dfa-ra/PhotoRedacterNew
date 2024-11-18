package com.example.photoredacternew.mainDialogs.paletteDialog.transparencySeekBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.TransparencySeekBarViewBinding;
import com.example.photoredacternew.mainDialogs.paletteDialog.CustomObservers.CollorObserver;
import com.example.photoredacternew.mainDialogs.paletteDialog.DrawColor;


public class TransparencySeekBarView extends FrameLayout {
    public TransparencySeekBarViewBinding binding;
    private Drawable fill;
    private Drawable border;
    private int new_color;
    LayerDrawable layerDrawable;

    public TransparencySeekBarView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TransparencySeekBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context){
        binding = TransparencySeekBarViewBinding.inflate(LayoutInflater.from(context), this, true);
        fill = context.getResources().getDrawable(R.drawable.custom_fill_thumb);
        border = context.getResources().getDrawable(R.drawable.custom_border_thumb);
        Drawable[] layers = { fill, border };
        layerDrawable = new LayerDrawable(layers);
    }

    public void setThumbColor(int color){
        fill.setColorFilter(color, PorterDuff.Mode.SRC); // Замените RED на нужный цвет
        binding.transparencySeekBar.setThumb(layerDrawable);
    }

    public void setSeekBarTrackColor(int color){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR, new int[]{Color.parseColor("#00000000"), color}
        );
        binding.gradientView.setBackground(gradientDrawable);
    }

    // Настройка SeekBar для яркости
    public void setupBrightnessSeekBar() {

        DrawColor.getInstance().getColor().subscribe(new CollorObserver() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                Log.d("aa99", "setupBrightnessSeekBar: " + integer);
                new_color = integer;
                setThumbColor(new_color);
            }
        });


        // Обработчик изменений в SeekBar
        binding.transparencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("aa99", "color: " + new_color);
                new_color = adjustAlpha(new_color, progress);
                DrawColor.getInstance().setColor(new_color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private int adjustAlpha(int color, int alpha) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        int alpha255 = (int) (alpha * 2.55);

        Log.d("aa99", "adjustAlpha: " + color + "  " + alpha);
        return Color.HSVToColor(alpha255, hsv);
    }


}
