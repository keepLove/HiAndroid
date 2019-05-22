package com.s.android.hiandroid.ui.android.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public final class CustomView1 extends View {

    private Paint fillPaint;
    private Paint strokePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint circleRangPaint;
    private Path lovePath = new Path();
    private int[] colors = {Color.GRAY, Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.YELLOW};
    private int[] rate = {25, 10, 14, 26, 10, 15};

    public CustomView1(Context context) {
        this(context, null);
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        fillPaint = new Paint();
        strokePaint = new Paint();
        pointPaint = new Paint();
        textPaint = new Paint();
        circleRangPaint = new Paint();
        // 抗锯齿
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.RED);
        fillPaint.setStyle(Paint.Style.FILL);

        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(10);

        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(30);
        pointPaint.setStrokeCap(Paint.Cap.SQUARE);

        textPaint.setColor(Color.RED);
        textPaint.setTextSize(40);

        circleRangPaint.setAntiAlias(true);
        circleRangPaint.setStrokeWidth(60);
        circleRangPaint.setStyle(Paint.Style.STROKE);
        circleRangPaint.setStrokeCap(Paint.Cap.ROUND);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lovePath.addArc(100, 400, 300, 600, -225, 225);
            lovePath.arcTo(300, 400, 500, 600, -180, 225, false);
            lovePath.lineTo(300, 700);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆
        drawText(canvas, "Circle", 100, 40);
        canvas.drawCircle(100, 100, 50, fillPaint);
        canvas.drawCircle(100, 250, 50, strokePaint);
        // 绘制矩形
        drawText(canvas, "Rect", 250, 40);
        canvas.drawRect(200, 50, 300, 150, fillPaint);
        canvas.drawRect(200, 200, 300, 300, strokePaint);
        // 绘制四个点
        drawText(canvas, "Point", 375, 40);
        float[] points = {0, 0, 350, 100, 400, 100, 350, 150, 400, 150, 150, 50, 150, 100};
        canvas.drawPoints(points, 2 /* 跳过两个数，即前两个 0 */,
                8 /* 一共绘制 8 个数（4 个点）*/, pointPaint);
        // 绘制椭圆
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawText(canvas, "Oval", 525, 40);
            canvas.drawOval(450, 50, 600, 150, strokePaint);
        }
        // 绘制直线
        canvas.drawLine(450, 200, 600, 250, strokePaint);
        // 绘制圆角矩形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawText(canvas, "Round", 700, 40);
            canvas.drawRoundRect(650, 100, 750, 300, 50, 50, strokePaint);
        }
        drawText(canvas, "Arc", 900, 40);
        // 绘制扇形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(800, 100, 1000, 300, -110, 100, true, fillPaint);
        }
        // 绘制弧形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(800, 100, 1000, 300, 20, 140, false, fillPaint);
        }
        // 绘制不封口的弧形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(800, 100, 1000, 300, 180, 60, false, strokePaint);
        }
        // 绘制爱心
        canvas.drawPath(lovePath, fillPaint);
        // 绘制圆环
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float rateAngle = 0;
            for (int i = 0; i < rate.length; i++) {
                circleRangPaint.setColor(colors[i]);
                canvas.drawArc(600, 400, 900, 700, -180 + rateAngle, 360 * rate[i] * 0.01f, false, circleRangPaint);
                rateAngle += 360 * rate[i] * 0.01f;
            }
        }
    }

    private void drawText(Canvas canvas, String text, float x, float y) {
        canvas.drawText(text, x - textPaint.measureText(text) / 2, y, textPaint);
    }
}
