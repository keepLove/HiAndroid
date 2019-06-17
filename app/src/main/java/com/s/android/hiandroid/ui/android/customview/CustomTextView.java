package com.s.android.hiandroid.ui.android.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    private int mLineY = 0;// 绘制的行坐标
    private int mViewWidth;//TextView的总宽度
    private TextPaint paint;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new TextPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        paint.setTextSize(getTextSize());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLineY = 0;
        mLineY += getTextSize();
        String text = getText().toString();
        Layout layout = getLayout();
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            // 获取TextView每行中的内容
            String lineText = text.substring(lineStart, lineEnd);
            if (hasLineFeed(lineText)) {
                canvas.drawText(lineText, 0, mLineY, paint);
            } else {
                // 最后一行不需要重绘
                if (i == lineCount - 1) {
                    canvas.drawText(lineText, 0, mLineY, paint);
                } else {
                    justify(canvas, lineText, i == 0);
                }
            }
            // 写完一行以后，高度增加一行的高度
            mLineY += getLineHeight();
        }
    }

    /**
     * 对齐两端
     */
    private void justify(Canvas canvas, String lineText, boolean firstLine) {
        float mLineX = 0;
        // 判断第一行前两格是否是空格
        if (firstLine && lineText.length() > 3 && lineText.charAt(0) == ' ' && lineText.charAt(1) == ' ') {
            String spacing = "  ";
            canvas.drawText(spacing, 0, mLineY, paint);
            mLineX = StaticLayout.getDesiredWidth(spacing, paint);
            lineText = lineText.substring(2);
        }
        lineText = lineText.trim();
        //比如说一共有5个字，中间有4个间隔，
        //那就用整个TextView的宽度 - 5个字的宽度，
        //然后除以4，填补到这4个空隙中
        float desiredWidth = StaticLayout.getDesiredWidth(lineText, paint);
        int length = lineText.length();
        float interval = (mViewWidth - desiredWidth) / (length - 1);
        for (int i = 0; i < length; i++) {
            String text = String.valueOf(lineText.charAt(i));
            canvas.drawText(text, mLineX, mLineY, paint);
            float width = StaticLayout.getDesiredWidth(text, paint);
            mLineX += (interval + width);
        }
    }

    /**
     * @return 是否需要换行
     */
    private boolean hasLineFeed(String text) {
        if (text.isEmpty()) {
            return true;
        }
        return text.charAt(text.length() - 1) == '\n';
    }
}
