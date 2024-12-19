package com.example.photoredacternew.viewDialog.photoDrawer;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.photoredacternew.DialogsManager;
import com.example.photoredacternew.R;
import com.example.photoredacternew.databinding.DrawerLayoutBinding;
import com.example.photoredacternew.paletteDialog.CustomPaletteSheet;
import com.example.photoredacternew.viewDialog.photoDrawer.customView.VerticalSeekBar;


/**
 * Класс отвечающий за окно рисования на изображении
 */
public class CustomDrawerRelativeLayout extends RelativeLayout implements CustomPaletteSheet.ColorCallBack, VerticalSeekBar.GetWidthCallBack {
    private DrawerLayoutBinding binding;

    // диалог палитры
    private CustomPaletteSheet customPaletteSheet;

    private IEditedBitmapCallBack listener;

    // флаг глазика для видимости худа
    private boolean eye_closed = false;



    public CustomDrawerRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomDrawerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // инициализация оновных полей
    private void init(Context context){
        binding = DrawerLayoutBinding.inflate(LayoutInflater.from(context), this, true);
        customPaletteSheet = new CustomPaletteSheet(this);
        binding.seekBar.setListener(this);
        actions();
    }

    // метод всех действий на окне
    private void actions(){

        // выбор цвета
        binding.editPanel.binding.color.setOnClickListener(view -> {
            Log.d("aa88", "color.setOnClickListener");
            DialogsManager.getInstanceWithActivity().showDialog(customPaletteSheet);
            binding.editPanel.binding.pencil.callOnClick();
            binding.seekBar.deactivateSeekBar();
            binding.widthView.setVisibility(GONE);
        });

        // применить изменения
        binding.doEdit.setOnClickListener(view->{
            listener.onDrawnBitmap(binding.fullImageDraw.getCombinedBitmap());
            binding.seekBar.deactivateSeekBar();
            binding.widthView.setVisibility(GONE);
        });

        // начать рисовать
        binding.editPanel.binding.pencil.setOnClickListener(view -> {
            binding.editPanel.binding.eraser.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
            binding.editPanel.binding.pencil.animate().scaleX(1.25f).scaleY(1.25f).setDuration(150).start();
            binding.fullImageDraw.setCurrentEvent(EditTypeEvent.DRAW);
            binding.seekBar.deactivateSeekBar();
            binding.widthView.setVisibility(GONE);
        });

        // начать стирать
        binding.editPanel.binding.eraser.setOnClickListener(view -> {
            binding.editPanel.binding.eraser.animate().scaleX(1.25f).scaleY(1.25f).setDuration(150).start();
            binding.editPanel.binding.pencil.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
            binding.fullImageDraw.setCurrentEvent(EditTypeEvent.ERASE);
            binding.seekBar.deactivateSeekBar();
            binding.widthView.setVisibility(GONE);
        });

        // убрать или проявить худ
        binding.eye.setOnClickListener(view -> {
            if (!eye_closed) {
                binding.eye.setBackgroundResource(R.drawable.eye_closed);
                binding.editPanel.hideEditPanel();
                binding.seekBar.hideSeekBar();
            }
            else {
                binding.eye.setBackgroundResource(R.drawable.eye);
                binding.editPanel.showEditPanel();
                binding.seekBar.showSeekBar();
            }

            eye_closed = !eye_closed;
        });

        // отчистить всё
        binding.clearAll.setOnClickListener(view -> {
            binding.fullImageDraw.clearAll();
        });

        // отменить последней нарисованный путь
        binding.back.setOnClickListener(view -> {
            binding.fullImageDraw.undoLastPath();
        });

        // изменить толщину кисти
        binding.seekBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.seekBar.activateSeekBar();
                binding.widthView.setVisibility(VISIBLE);
                return false;
            }
        });
    }

    @Override
    public void getSelectedColor(int color) {
        binding.editPanel.setSelectedColor(color);
        binding.fullImageDraw.setColor(color);
    }

    public void setImageBitmap(Bitmap bitmap){
        binding.fullImageDraw.setImageBitmap(bitmap);
    }

    public void setListener(IEditedBitmapCallBack listener) {
        this.listener = listener;
    }

    public View getClose(){
        return binding.exit;
    }

    public void closeFullImageDraw(){
        binding.fullImageDraw.clearAll();
    }

    @Override
    public void getWidth(int width) {
        binding.widthView.setScaleX((1 + (float) width / 50));
        binding.widthView.setScaleY((1 + (float) width / 50));
        Log.d("aa88", "width: " + (int)(2 * 5  * (1 + (float) width / 50)));
        binding.fullImageDraw.setStrokeWidth((int)(2 * 5 * (1 + (float) width / 50)));
    }
}
