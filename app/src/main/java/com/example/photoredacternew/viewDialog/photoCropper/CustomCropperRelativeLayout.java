package com.example.photoredacternew.viewDialog.photoCropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.photoredacternew.databinding.CropperLayoutBinding;
import com.example.photoredacternew.viewDialog.photoDrawer.GetEditedBitmapCallBack;

public class CustomCropperRelativeLayout extends RelativeLayout {

    private CropperLayoutBinding binding;
    private GetEditedBitmapCallBack listener;

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
            listener.getCroppedBitmap(binding.fullImageCrop.cropImage());
        });

        binding.resetAll.setOnClickListener(view -> {
            binding.fullImageCrop.reset();
        });
    }

    public View getClose(){
        return binding.exit;
    }

    public void setListener(GetEditedBitmapCallBack listener) {
        this.listener = listener;
    }

    public void setImageBitmap(Bitmap bitmap){
        binding.fullImageCrop.setImageBitmap(bitmap);
    }

}
