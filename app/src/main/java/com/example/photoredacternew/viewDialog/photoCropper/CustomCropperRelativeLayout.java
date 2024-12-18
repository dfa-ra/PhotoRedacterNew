package com.example.photoredacternew.viewDialog.photoCropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.photoredacternew.databinding.CropperLayoutBinding;
import com.example.photoredacternew.viewDialog.photoDrawer.IEditedBitmapCallBack;

/**
 * Класс отвещающий за окно обрезки фото
 */
public class CustomCropperRelativeLayout extends RelativeLayout {

    private CropperLayoutBinding binding;
    private IEditedBitmapCallBack listener;

    public CustomCropperRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomCropperRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        binding = CropperLayoutBinding.inflate(LayoutInflater.from(context), this, true);
        actions();
    }

    private void actions(){
        binding.doEdit.setOnClickListener(view->{
            listener.onCroppedBitmap(binding.fullImageCrop.cropImage());
        });

        binding.resetAll.setOnClickListener(view -> {
            binding.fullImageCrop.reset();
        });
    }

    public View getClose(){
        return binding.exit;
    }

    public void setListener(IEditedBitmapCallBack listener) {
        this.listener = listener;
    }

    public void setImageBitmap(Bitmap bitmap){
        binding.fullImageCrop.setImageBitmap(bitmap);
    }

}
