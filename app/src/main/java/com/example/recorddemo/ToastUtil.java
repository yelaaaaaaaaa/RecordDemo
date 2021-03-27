package com.example.recorddemo;

import android.widget.Toast;



/**
 * UI通用方法类
 */
public class ToastUtil {

    private static Toast mToast;

    public static final void toastLongMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(App.instance().getApplicationContext(), message,
                        Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }


    public static final void toastShortMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(App.instance().getApplicationContext(), message,
                        Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }


}
