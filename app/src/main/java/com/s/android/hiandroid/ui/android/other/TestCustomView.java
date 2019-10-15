package com.s.android.hiandroid.ui.android.other;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

public class TestCustomView extends android.support.v7.widget.AppCompatTextView {

    public TestCustomView(Context context) {
        super(context);
    }

    public TestCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("test", "onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("test", "onDraw");
    }
}
