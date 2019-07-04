package com.s.android.hiandroid.utils;

import android.util.DisplayMetrics;

public final class ResourceUtils {

    private ResourceUtils() {
    }

    public static float px2dp(float pxNum) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.density * pxNum;
    }

    public static float px2sp(float pxNum) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.scaledDensity * pxNum;
    }

    public static float sp2px(float spNum) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return spNum / displayMetrics.scaledDensity;
    }

    public static float dp2px(float dpNum) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.density * dpNum + 0.5f;
    }

}
