package com.s.android.hiandroid.ui.android.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.s.android.hiandroid.R;

public final class CustomView3 extends View {

    private Paint paint = new Paint();
    private Bitmap bitmap;
    private Matrix matrix;
    private Camera camera;
    private int degrees;
    private ObjectAnimator objectAnimator;

    private Paint arcPaint = new Paint();
    private RectF arcRectF = new RectF();
    private Rect textRect = new Rect();

    public CustomView3(Context context) {
        this(context, null);
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        matrix = new Matrix();
        camera = new Camera();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        degrees = 45;

        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.RED);
        arcPaint.setStrokeWidth(20);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setStyle(Paint.Style.STROKE);
    }

    public ObjectAnimator getObjectAnimator() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofInt(this, "degrees", 0, 180);
            objectAnimator.setDuration(2000);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.setInterpolator(new LinearInterpolator());
        }
        return objectAnimator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(bitmap.getWidth(), widthMeasureSpec),
                getDefaultSize(bitmap.getHeight(), heightMeasureSpec));
        arcRectF.right = getWidth() - 100;
        arcRectF.left = arcRectF.right - bitmap.getWidth();
        arcRectF.top = 20;
        arcRectF.bottom = bitmap.getWidth() + 20;
    }

    @SuppressWarnings("unused")
    public void setDegrees(int degrees) {
        this.degrees = degrees;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getObjectAnimator().start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (objectAnimator != null) {
            getObjectAnimator().end();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制图片循环旋转
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float bitmapWidthHalf = bitmapWidth / 2f;
        float bitmapHeightHalf = bitmapHeight / 2f;

        // 第一步 裁剪上面不动的部分
        canvas.save();
        canvas.clipRect(0, 0, bitmapWidth, bitmapHeightHalf);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
        // 第二步 旋转
        canvas.save();
        if (degrees > 90) {
            canvas.clipRect(0, 0, bitmapWidth, bitmapHeightHalf);
        } else {
            canvas.clipRect(0, bitmapHeightHalf, bitmapWidth, bitmapHeight);
        }
        matrix.reset();
        camera.save();
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-bitmapWidthHalf, -bitmapHeightHalf);
        matrix.postTranslate(bitmapWidthHalf, bitmapHeightHalf);
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();

        // 绘制圆环
        canvas.drawArc(arcRectF, -180, degrees, false, arcPaint);
        String text = String.format("%s", degrees);
        paint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, arcRectF.centerX() - textRect.centerX(), arcRectF.centerY() + textRect.centerY(), paint);
    }

}
