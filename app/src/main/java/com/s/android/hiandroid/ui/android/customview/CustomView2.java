package com.s.android.hiandroid.ui.android.customview;

import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public final class CustomView2 extends View {

    private Paint paint = new Paint();
    private Paint linePaint = new Paint();
    private Paint textPaint = new Paint();
    private int[] colors = {Color.GRAY, Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.YELLOW};
    private int[] rate = {35, 5, 8, 10, 22, 20};
    private String[] text = {"first", "second", "three", "four", "five", "six"};
    private RectF rectF = new RectF();
    private Rect textRect = new Rect();
    private Path linePath = new Path();

    public CustomView2(Context context) {
        this(context, null);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        textPaint.setTextSize(50);
        textPaint.setColor(Color.RED);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float curAngle = 0;
            for (int i = 0; i < rate.length; i++) {
                // 设置颜色
                paint.setColor(colors[i]);
                // 起始绘制角度
                float startAngle = -180 + curAngle;
                // 偏移角度
                float sweepAngle = rate[i] * 360 * 0.01f;
                // 中心角度
                float centerAngle = startAngle + sweepAngle / 2;
                // 偏移量
                float diff = 10;
                float cos = (float) Math.cos(centerAngle * Math.PI / 180);
                float sin = (float) Math.sin(centerAngle * Math.PI / 180);
                float x = cos * diff;
                float y = sin * diff;
                if (centerAngle >= -180 && centerAngle < -90) {
                    // 在第三象限，左上角
                    x = x * 2;
                    y = y * 2;
                }
                rectF.left = 100 + x;
                rectF.top = 50 + y;
                rectF.right = 500 + x;
                rectF.bottom = 450 + y;
                // 绘制扇形
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
                // 半径
                float radius = (rectF.right - rectF.left) / 2;
                float startX = rectF.left + radius + radius * cos;
                float startY = rectF.top + radius + radius * sin;
                linePath.moveTo(startX, startY);
                float endX = startX + 30 * cos;
                float endY = startY + 30 * sin;
                linePath.lineTo(endX, endY);
                endX = endX + 100 * compare(cos);
                float textX = endX;
                String drawText = text[i];
                textPaint.getTextBounds(drawText, 0, drawText.length(), textRect);
                if (cos < 0) {
                    textX = endX - textRect.width();
                    // 防止无法绘制左边文字
                    if (textX < 0) {
                        endX = endX - textX;
                        textX = 0;
                    }
                }
                linePath.lineTo(endX, endY);
                // 绘制线段
                canvas.drawPath(linePath, linePaint);
                // 绘制文字
                if (endY < textRect.height()) {
                    endY = textRect.height();
                }
                canvas.drawText(drawText, textX, endY, textPaint);
                curAngle += sweepAngle;
            }
        }
    }

    private float compare(float a) {
        if (a < 0) {
            return -1;
        }
        return 1;
    }
}
