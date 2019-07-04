package com.s.android.hiandroid.ui.android.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.s.android.hiandroid.R;

public class CustomView6 extends LinearLayout {

    public CustomView6(Context context) {
        this(context, null);
    }

    public CustomView6(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView6(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final TextView rulerNumberText = findViewById(R.id.ruler_number);
                BooheeRulerView booheeRulerView = findViewById(R.id.boohee_ruler);
                booheeRulerView.setBooheeRulerSelectedListener(new BooheeRulerView.BooheeRulerSelectedListener() {
                    @Override
                    public void onSelected(float number) {
                        Log.e("BooheeRulerView", "onSelected: " + number);
                        rulerNumberText.setText(String.valueOf(number));
                    }
                });
            }
        });
    }
}
