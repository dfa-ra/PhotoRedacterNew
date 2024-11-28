package com.example.photoredacternew.viewDialog.photoEditer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.photoredacternew.DialogsManager;
import com.example.photoredacternew.databinding.DrawerLayoutBinding;
import com.example.photoredacternew.paletteDialog.CustomPaletteSheet;

public class CustomDrawerRelativeLayout extends RelativeLayout implements CustomPaletteSheet.ColorCallBack {
    private DrawerLayoutBinding binding;

    private CustomPaletteSheet customPaletteSheet;
    private GetEditedBitmapCallBack listener;
    private int selected_color = 0;



    public CustomDrawerRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomDrawerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        binding = DrawerLayoutBinding.inflate(LayoutInflater.from(context), this, true);
        customPaletteSheet = new CustomPaletteSheet(this);
        actions();
    }

    private void actions(){
        binding.editPanel.binding.color.setOnClickListener(view -> {
            Log.d("aa88", "color.setOnClickListener");
            DialogsManager.getInstanceWithActivity().showDialog(customPaletteSheet);
        });

        binding.editPanel.binding.doEdit.setOnClickListener(view->{
            listener.getEditedBitmap(binding.fullImageDraw.getCombinedBitmap());
        });
    }

    @Override
    public void getSelectedColor(int color) {
        binding.editPanel.setSelectedColor(color);
        binding.fullImageDraw.setColor(color);
        selected_color = color;
    }

    public void setImageDrawable(Drawable drawable){
        binding.fullImageDraw.setImageDrawable(drawable);
    }

    public void setListener(GetEditedBitmapCallBack listener) {
        this.listener = listener;
    }

    public View getClose(){
        return binding.editPanel.binding.exit;
    }
}
