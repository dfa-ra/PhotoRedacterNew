package com.example.photoredacternew.viewDialog.photoDrawer;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.EditPanelLayoutBinding;

/**
 * Класс лайаута основной панели редактирования
 *
 */
public class EditPanel extends LinearLayout {
    public EditPanelLayoutBinding binding;
    private Drawable fill;
    private LayerDrawable layerDrawable;

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
        Drawable border = context.getResources().getDrawable(R.drawable.custom_border_thumb);
        Drawable[] layers = { fill, border};
        layerDrawable = new LayerDrawable(layers);
        actions();
    }

    private void actions(){

    }

    protected void setSelectedColor(int color) {
        fill.setColorFilter(color, PorterDuff.Mode.SRC);
        binding.color.setBackground(layerDrawable);
    }

    // Анимация для выдвижения/защелкивания SeekBar
    public void animateEditPanel(EditPanel editPanel, float targetTranslationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(editPanel, "translationX", targetTranslationX);
        animator.setDuration(200);
        animator.start();
    }

    public void hideEditPanel(){
        animateEditPanel(this,  130f);
    }

    public void showEditPanel(){
        animateEditPanel(this,  0f);
    }

}
