package com.s.android.hiandroid.ui.android.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class CustomView5 extends LinearLayout {

    public CustomView5(Context context) {
        this(context, null);
    }

    public CustomView5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        final ThumbUpView thumbUpView = new ThumbUpView(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1000);
        addView(thumbUpView, layoutParams);
        final EditText editText = new EditText(context);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        editText.setHint("设置点赞数量");
        Button button = new Button(context);
        button.setText("确定");
        button.setGravity(Gravity.CENTER);
        addView(editText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(button, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbUpView.setNumber(Integer.parseInt(editText.getText().toString()));
            }
        });
    }
}
