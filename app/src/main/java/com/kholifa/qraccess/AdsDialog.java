package com.kholifa.qraccess;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;


public class AdsDialog {

    private static AdsDialog instance;
    private AppCompatDialog dialog;

    public static AdsDialog getInstance() {
        if (instance == null) {
            instance = new AdsDialog();
        }
        return instance;
    }

    public void showLoader(Activity activity) {
        dialog = new AppCompatDialog(activity);
        dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ads_loader_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismissLoader() {
        try {
            if (dialog == null) {
                Log.w("msg", "");
            } else {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
