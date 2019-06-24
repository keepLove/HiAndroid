package com.s.android.hiandroid.ui.android.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.s.android.hiandroid.R;

public final class CustomView4 extends View {

    private static final String TAG = "CustomView4";

    /**
     * 动画开始状态
     */
    private static final int ANIMATE_START = 0;
    /**
     * 动画运行状态
     */
    private static final int ANIMATE_RUN = 1;
    /**
     * 动画结束状态
     */
    private static final int ANIMATE_FINISH = 2;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private RectF bitmapRectF = new RectF();
    private Matrix matrix;
    private Camera camera;
    private ObjectAnimator objectAnimator1;
    private ObjectAnimator objectAnimator2;
    private ObjectAnimator objectAnimator3;
    /**
     * 动画角度
     */
    private int degrees1;
    private int degrees2;
    private int degrees3;
    /**
     * 动画状态
     */
    private int animator1Status = ANIMATE_START;
    private int animator2Status = ANIMATE_START;
    private int animator3Status = ANIMATE_START;

    public CustomView4(Context context) {
        this(context, null);
    }

    public CustomView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        matrix = new Matrix();
        camera = new Camera();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        objectAnimator1 = ObjectAnimator.ofInt(this, "degrees1", 0, -45);
        objectAnimator1.setDuration(1000);
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animator1Status = ANIMATE_RUN;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator1Status = ANIMATE_FINISH;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        objectAnimator2 = ObjectAnimator.ofInt(this, "degrees2", 0, -270);
        objectAnimator2.setDuration(3000);
        objectAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animator2Status = ANIMATE_RUN;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator2Status = ANIMATE_FINISH;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        objectAnimator3 = ObjectAnimator.ofInt(this, "degrees3", 0, 30);
        objectAnimator3.setDuration(1000);
        objectAnimator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animator3Status = ANIMATE_RUN;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator3Status = ANIMATE_FINISH;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        setMeasuredDimension(resolveSize(bitmapWidth, widthMeasureSpec),
                resolveSize(bitmapHeight, heightMeasureSpec));
        bitmapRectF.top = (getMeasuredHeight() - bitmapHeight) * 0.5f;
        bitmapRectF.bottom = bitmapRectF.top + bitmapHeight;
        bitmapRectF.left = (getMeasuredWidth() - bitmapWidth) * 0.5f;
        bitmapRectF.right = bitmapRectF.left + bitmapWidth;
    }

    @Keep
    public void setDegrees1(int degrees1) {
        this.degrees1 = degrees1;
        invalidate();
    }

    @Keep
    public void setDegrees2(int degrees2) {
        this.degrees2 = degrees2;
        invalidate();
    }

    @Keep
    public void setDegrees3(int degrees3) {
        this.degrees3 = degrees3;
        invalidate();
    }

    /**
     * 恢复初始化
     */
    private void restore() {
        degrees1 = 0;
        degrees2 = 0;
        degrees3 = 0;
        animator1Status = ANIMATE_START;
        animator2Status = ANIMATE_START;
        animator3Status = ANIMATE_START;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        objectAnimator1.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        objectAnimator1.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 裁剪左边不动的部分
        canvas.save();
        if (degrees2 != 0) {
            canvas.rotate(degrees2, bitmapRectF.centerX(), bitmapRectF.centerY());
        }
        canvas.clipRect(0, 0, bitmapRectF.centerX(), getMeasuredHeight());
        if (degrees3 != 0) {
            matrix.reset();
            camera.save();
            camera.rotateY(degrees3);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-bitmapRectF.centerX(), -bitmapRectF.centerY());
            matrix.postTranslate(bitmapRectF.centerX(), bitmapRectF.centerY());
            canvas.concat(matrix);
        }
        if (degrees2 != 0) {
            canvas.rotate(-degrees2, bitmapRectF.centerX(), bitmapRectF.centerY());
        }
        canvas.drawBitmap(bitmap, bitmapRectF.left, bitmapRectF.top, paint);
        canvas.restore();
        // 旋转右边
        canvas.save();
        if (degrees2 != 0) {
            canvas.rotate(degrees2, bitmapRectF.centerX(), bitmapRectF.centerY());
        }
        canvas.clipRect(bitmapRectF.centerX(), 0, getMeasuredWidth(), getMeasuredHeight());
        matrix.reset();
        camera.save();
        camera.rotateY(degrees1);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-bitmapRectF.centerX(), -bitmapRectF.centerY());
        matrix.postTranslate(bitmapRectF.centerX(), bitmapRectF.centerY());
        canvas.concat(matrix);
        if (degrees2 != 0) {
            canvas.rotate(-degrees2, bitmapRectF.centerX(), bitmapRectF.centerY());
        }
        canvas.drawBitmap(bitmap, bitmapRectF.left, bitmapRectF.top, paint);
        canvas.restore();
        // 旋转画布
        if (animator1Status == ANIMATE_FINISH &&
                animator2Status == ANIMATE_START &&
                animator3Status == ANIMATE_START) {
            objectAnimator2.start();
            Log.e(TAG, "onDraw:  objectAnimator2.start();");
        }
        // 最后翻转
        if (animator1Status == ANIMATE_FINISH &&
                animator2Status == ANIMATE_FINISH &&
                animator3Status == ANIMATE_START) {
            objectAnimator3.start();
            Log.e(TAG, "onDraw:  objectAnimator3.start();");
        }
        // 循环
        if (animator1Status == ANIMATE_FINISH &&
                animator2Status == ANIMATE_FINISH &&
                animator3Status == ANIMATE_FINISH) {
            restore();
            objectAnimator1.start();
            Log.e(TAG, "onDraw:  objectAnimator1.start();");
        }
    }

}
