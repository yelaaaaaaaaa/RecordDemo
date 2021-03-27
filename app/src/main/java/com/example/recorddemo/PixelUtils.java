package com.example.recorddemo;

import android.content.Context;
import android.content.res.Resources;


public class PixelUtils {

    /**
     * dp 转 px 输入的是dp
     *
     * @param value
     * @return
     */
    public static int dp2px(Context context, float value) {
        final float scale =context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * px 转 dp
     *
     * @param value
     * @return
     */
    public static int px2dp(Context context,float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param value
     * @return
     */
    public static int sp2px(Context context,float value) {
        Resources r;
        if (context == null) {
            r = Resources.getSystem();
        } else {
            r = context.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
