package com.example.photoredacternew.mainDialogs;

import android.app.Dialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.photoredacternew.mainDialogs.paletteDialog.paletteCells.CustomPaletteSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogsManager {
    private static DialogsManager dialogsManager = null;
    private FragmentActivity activity;

    private DialogsManager(FragmentActivity activity){
        this.activity = activity;
    }

    public static DialogsManager getInstance(FragmentActivity activity){
        if (dialogsManager == null) dialogsManager = new DialogsManager(activity);
        return dialogsManager;
    }
    public static DialogsManager getInstanceWithActivity(){
        return dialogsManager;
    }

    public void showDialog(Dialog dialog){
        if (activity != null){
            dialog.show();
        }
    }

    public void dismissDialog(Dialog dialog){
        if (dialog != null && activity != null){
            dialog.dismiss();
        }
    }

    public void showDialog(BottomSheetDialogFragment dialog){
        if (activity != null){
            dialog.show(activity.getSupportFragmentManager(), dialog.getTag());
        }
    }

    public void dismissDialog(BottomSheetDialogFragment dialog){
        if (dialog != null && activity != null){
            dialog.dismiss();
        }
    }
}
