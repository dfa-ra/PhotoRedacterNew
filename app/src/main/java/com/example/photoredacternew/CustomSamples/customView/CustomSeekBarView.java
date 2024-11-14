package com.example.photoredacternew.CustomSamples.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.TransparencySeekBarViewBinding;


public class CustomSeekBarView extends FrameLayout {
    TransparencySeekBarViewBinding binding;
    private Drawable fill;
    private Drawable border;
    LayerDrawable layerDrawable;

    public CustomSeekBarView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
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
}
