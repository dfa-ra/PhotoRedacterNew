package com.example.photoredacternew.CustomSamples.CustomLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.photoredacternew.CustomSamples.customView.CustomPaletteSheet;
import com.example.photoredacternew.databinding.EditPanelLayoutBinding;

public class EditPanel extends LinearLayout {

    private EditPanelLayoutBinding binding;

    CustomPaletteSheet customPaletteSheet;
    public EditPanel(Context context) {
        super(context);
        init(context);
    }

    public EditPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        binding = EditPanelLayoutBinding.inflate(LayoutInflater.from(context), this, true);
        customPaletteSheet = new CustomPaletteSheet();

        actions(context);

    }

    private void actions(Context context){
        binding.color.setOnClickListener(view -> {
            Log.d("aa99", "color.setOnClickListener");
            if (context instanceof FragmentActivity) {
                // Показать фрагмент в активности
                FragmentActivity activity = (FragmentActivity) context;
                if (!customPaletteSheet.isAdded()) {
                    customPaletteSheet.show(activity.getSupportFragmentManager(), customPaletteSheet.getTag());
                }
            } else {
                Log.e("PhotoDialog", "Контекст не является FragmentActivity, фрагмент не может быть показан");
            }
        });
    }
}
