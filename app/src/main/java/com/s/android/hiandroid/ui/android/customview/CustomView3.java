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
        matrix = new Matrix();
        camera = new Camera();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        objectAnimator = ObjectAnimator.ofInt(this, "degrees", 0, 180);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        degrees = 45;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(bitmap.getHeight(), heightMeasureSpec));
    }

    @SuppressWarnings("unused")
    public void setDegrees(int degrees) {
        this.degrees = degrees;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        objectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        objectAnimator.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float bitmapWidthHalf = bitmapWidth / 2f;
        float bitmapHeightHalf = bitmapHeight / 2f;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // 第一步 裁剪上面不动的部分
        canvas.save();
        canvas.clipRect(0, 0, getWidth(), centerY);
        canvas.drawBitmap(bitmap, centerX - bitmapWidthHalf, centerY - bitmapHeightHalf, paint);
        canvas.restore();
        // 第二步 旋转
        canvas.save();
        if (degrees > 90) {
            canvas.clipRect(0, 0, getWidth(), centerY);
        } else {
            canvas.clipRect(0, centerY, getWidth(), getHeight());
        }
        matrix.reset();
        camera.save();
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, centerX - bitmapWidthHalf, centerY - bitmapHeightHalf, paint);
        canvas.restore();
    }

}
