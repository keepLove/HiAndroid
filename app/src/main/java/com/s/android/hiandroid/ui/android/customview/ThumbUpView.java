package com.s.android.hiandroid.ui.android.customview;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.s.android.hiandroid.R;

public final class ThumbUpView extends View {

    private static final String TAG = "CustomView5";
    private Matrix matrix = new Matrix();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect textRect = new Rect();

    /**
     * 图片文字绘制位置计算
     */
    private RectF bitmapRectF = new RectF();
    private RectF textRectF = new RectF();
    private RectF pointRectF = new RectF();
    /**
     * 拇指选中和未选中图片
     */
    private Bitmap thumbSelectBitmap;
    private Bitmap thumbUnSelectBitmap;
    /**
     * 上面点的图片
     */
    private Bitmap pointBitmap;
    /**
     * 点赞数
     */
    private int number = 0;
    /**
     * 上一个点赞数
     */
    private int lastNumber = 0;
    /**
     * 拇指和文字的间隔
     */
    private int interval = 20;
    private float textSize = 40;
    private ObjectAnimator thumbAnimator;
    /**
     * 图片放大倍数
     */
    private float bitmapScale = 1;
    /**
     * 拇指上面的点放大倍数
     */
    private float pointScale = 0;
    /**
     * 文字Y轴上滑动的距离和透明度
     */
    private float textScrollY = 0;
    private int textAlpha = 0;

    public ThumbUpView(Context context) {
        this(context, null);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ThumbUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 获取图片
        thumbSelectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        thumbUnSelectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        // text paint 处理
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        // 文字的宽高
        String textNumber = String.valueOf(number);
        textPaint.getTextBounds(textNumber, 0, textNumber.length(), textRect);
        // 点击，点赞处理
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(!v.isSelected());
                if (isSelected()) {
                    ++number;
                } else {
                    --number;
                }
                thumbAnimator.start();
            }
        });
        // 拇指缩小放大再缩小放大
        Keyframe keyframe1 = Keyframe.ofFloat(0f, 1f);
        Keyframe keyframe2 = Keyframe.ofFloat(0.3f, 0.7f);
        Keyframe keyframe3 = Keyframe.ofFloat(0.7f, 1.1f);
        Keyframe keyframe4 = Keyframe.ofFloat(0.8f, 0.9f);
        Keyframe keyframe5 = Keyframe.ofFloat(1f, 1f);
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofKeyframe("bitmapScale",
                keyframe1, keyframe2, keyframe3, keyframe4, keyframe5);
        // 拇指点展开
        Keyframe pointKeyframe1 = Keyframe.ofFloat(0f, 0f);
        Keyframe pointKeyframe2 = Keyframe.ofFloat(0.3f, 0f);
        Keyframe pointKeyframe3 = Keyframe.ofFloat(1f, 1f);
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe("pointScale",
                pointKeyframe1, pointKeyframe2, pointKeyframe3);
        // 文字滚动
        PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("textScrollY",
                0, textRect.height());
        // 文字透明度
        PropertyValuesHolder propertyValuesHolder4 = PropertyValuesHolder.ofInt("textAlpha",
                0, 255);
        thumbAnimator = ObjectAnimator.ofPropertyValuesHolder(this, propertyValuesHolder1, propertyValuesHolder2,
                propertyValuesHolder3, propertyValuesHolder4);
        thumbAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lastNumber = number;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @Keep
    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Keep
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Keep
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.lastNumber = number;
        this.number = number;
        invalidate();
    }

    @Keep
    public void setPointScale(float pointScale) {
        this.pointScale = pointScale;
    }

    @Keep
    public void setTextScrollY(float textScrollY) {
        this.textScrollY = textScrollY;
        invalidate();
    }

    @Keep
    public void setTextAlpha(int textAlpha) {
        this.textAlpha = textAlpha;
    }

    @Keep
    public void setBitmapScale(float bitmapScale) {
        this.bitmapScale = bitmapScale;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 图片的宽高
        int pointBitmapWidth = pointBitmap.getWidth();
        int pointBitmapHeight = pointBitmap.getHeight();
        int thumbSelectBitmapHeight = thumbSelectBitmap.getHeight();
        int bitmapWidth = (thumbSelectBitmap.getWidth());
        int bitmapHeight = ((thumbSelectBitmapHeight + pointBitmapHeight));
        // 文字的宽高，如果为9需要10的空间
        String textNumber = String.valueOf(number + 1);
        textPaint.getTextBounds(textNumber, 0, textNumber.length(), textRect);
        // 最终的宽高
        int resolveWidth = (int) (bitmapWidth + interval + textPaint.measureText(textNumber));
        int resolveHeight = Math.max(bitmapHeight, textRect.height());
        setMeasuredDimension(resolveSize(resolveWidth, widthMeasureSpec),
                resolveSize(resolveHeight, heightMeasureSpec));
        // 计算图片的位置
        bitmapRectF.top = (getMeasuredHeight() - thumbSelectBitmapHeight) * 0.5f;
        bitmapRectF.bottom = bitmapRectF.top + thumbSelectBitmapHeight;
        bitmapRectF.left = (getMeasuredWidth() - resolveWidth) * 0.5f;
        bitmapRectF.right = bitmapRectF.left + bitmapWidth;
        // 计算拇指点的位置
        pointRectF.left = bitmapRectF.centerX() - pointBitmapWidth / 2f;
        pointRectF.right = pointRectF.left + pointBitmapWidth;
        pointRectF.bottom = bitmapRectF.top + pointBitmapHeight / 2f;
        pointRectF.top = pointRectF.bottom - pointBitmapHeight;
    }

    /**
     * 计算文字的位置
     */
    private void measureTextRect() {
        // 如果为9需要10的空间
        String textNumber = String.valueOf(Math.max(number + 1, lastNumber + 1));
        textPaint.getTextBounds(textNumber, 0, textNumber.length(), textRect);
        textRectF.top = (getMeasuredHeight() - textRect.height()) * 0.5f;
        textRectF.bottom = textRectF.top + textRect.height();
        textRectF.left = bitmapRectF.right + interval;
        textRectF.right = textRectF.left + textPaint.measureText(textNumber);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制拇指图片
        canvas.save();
        matrix.reset();
        matrix.postScale(bitmapScale, bitmapScale, bitmapRectF.centerX(), bitmapRectF.centerY());
        canvas.concat(matrix);
        canvas.drawBitmap(getDrawBitmap(), bitmapRectF.left, bitmapRectF.top, paint);
        canvas.restore();
        // 绘制拇指上的点
        if (isSelected()) {
            canvas.save();
            matrix.reset();
            matrix.postScale(pointScale, pointScale, pointRectF.centerX(), pointRectF.centerY());
            canvas.concat(matrix);
            canvas.drawBitmap(pointBitmap, pointRectF.left, pointRectF.top, paint);
            canvas.restore();
        }
        // 绘制文字
        drawText(canvas);
    }

    /**
     * 绘制文字
     * 1，计算出相同的部分和不同的部分
     * 2，绘制相同的部分
     * 3，绘制不同的部分，设置y轴移动、透明度
     */
    private void drawText(Canvas canvas) {
        measureTextRect();
        String lastNumText = String.valueOf(lastNumber);
        String numText = String.valueOf(number);
        // 计算相同部分和不同的部分
        int minTextLength = Math.min(lastNumText.length(), numText.length());
        int identicalAt = 0;
        for (int i = 0; i < minTextLength; i++) {
            if (lastNumText.charAt(i) != numText.charAt(i)) {
                identicalAt = i;
                break;
            }
        }
        // 相同的部分值
        String identicalText = null;
        if (identicalAt > 0) {
            identicalText = lastNumText.substring(0, identicalAt);
        }
        // 不同的部分值
        String unidenticalLastNumText = lastNumText.substring(identicalAt);
        String unidenticalNumText = numText.substring(identicalAt);
        Log.e(TAG, "drawText: identicalText=" + identicalText + ",unidenticalLastNumText=" +
                unidenticalLastNumText + ",unidenticalNumText=" + unidenticalNumText);
        // 绘制相同的部分
        if (identicalText != null) {
            canvas.drawText(identicalText, textRectF.right - textPaint.measureText(unidenticalLastNumText),
                    textRectF.bottom, textPaint);
        }
        canvas.save();
        matrix.reset();
        // 当前值比原值大，向上滚动
        if (number > lastNumber) {
            matrix.postTranslate(0, -textScrollY);
        } else {
            matrix.postTranslate(0, textScrollY);
        }
        canvas.concat(matrix);
        // 绘制原来的部分，滚动至不可见
        textPaint.setAlpha(255 - textAlpha);
        canvas.drawText(unidenticalLastNumText, textRectF.right, textRectF.bottom, textPaint);
        // 绘制不同的部分，放到上面或下面，方便滚动
        textPaint.setAlpha(textAlpha);
        // 当前值比原值大，向上滚动
        if (number > lastNumber) {
            canvas.drawText(unidenticalNumText, textRectF.right, textRectF.bottom + textRect.height(), textPaint);
        } else {
            canvas.drawText(unidenticalNumText, textRectF.right, textRectF.bottom - textRect.height(), textPaint);
        }
        textPaint.setAlpha(255);
        canvas.restore();
    }

    /**
     * @return 根据是否选中选出相应的图片
     */
    private Bitmap getDrawBitmap() {
        if (isSelected()) {
            return thumbSelectBitmap;
        } else {
            return thumbUnSelectBitmap;
        }
    }
}
