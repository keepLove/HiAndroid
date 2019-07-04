package com.s.android.hiandroid.ui.android.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.s.android.hiandroid.R;

import java.math.BigDecimal;

public class BooheeRulerView extends View {

    private static final String TAG = "BooheeRulerView";
    /**
     * 大小刻度宽、高
     */
    private static final float littleLineWidth = 3, bigLineWidth = 5, littleLineHeight = 60, bigLineHeight = 80;
    /**
     * 文字Paint
     */
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 刻度Paint
     */
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 光标，光标宽度、高度
     */
    private Drawable cursorDrawable;
    private int cursorWidth, cursorHeight;
    /**
     * 最大最小刻度
     */
    private int minScale, maxScale;
    /**
     * 当前选中刻度
     */
    private BigDecimal currentScale;
    /**
     * 一个间隔对应的大小，间隔宽度
     */
    private float scaleInterval;
    private float intervalWidth;
    /**
     * 间隔数量
     */
    private int intervalCount;
    /**
     * 刻度线
     */
    private Path littleLinePath = new Path();
    private Path bigLinePath = new Path();
    /**
     * x轴上移动距离
     */
    private float moveX = 0;
    private float widthHalf;
    /**
     * 手势控制
     */
    private GestureDetectorCompat gestureDetectorCompat;

    /**
     * 选中监听事件
     */
    @Nullable
    private BooheeRulerSelectedListener booheeRulerSelectedListener;

    public BooheeRulerView(Context context) {
        this(context, null);
    }

    public BooheeRulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooheeRulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyleable(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BooheeRulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStyleable(context, attrs);
    }

    private void initStyleable(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BooheeRulerView, 0, 0);
        int textColor = typedArray.getColor(R.styleable.BooheeRulerView_numTextColor, Color.parseColor("#2B2E2B"));
        int textSize = typedArray.getDimensionPixelSize(R.styleable.BooheeRulerView_numTextSize, 40);
        cursorDrawable = typedArray.getDrawable(R.styleable.BooheeRulerView_cursorDrawable);
        if (cursorDrawable == null) {
            cursorDrawable = getResources().getDrawable(R.drawable.cursor_shape);
        }
        cursorWidth = typedArray.getDimensionPixelSize(R.styleable.BooheeRulerView_cursorWidth, 8);
        cursorHeight = typedArray.getDimensionPixelSize(R.styleable.BooheeRulerView_cursorHeight, 100);
        minScale = typedArray.getInteger(R.styleable.BooheeRulerView_minScale, 0);
        maxScale = typedArray.getInteger(R.styleable.BooheeRulerView_maxScale, 100);
        currentScale = new BigDecimal(Float.toString(typedArray.getFloat(R.styleable.BooheeRulerView_currentScale, minScale)));
        scaleInterval = typedArray.getFloat(R.styleable.BooheeRulerView_scaleInterval, 0.1f);
        intervalWidth = typedArray.getDimension(R.styleable.BooheeRulerView_intervalWidth, 40);
        intervalCount = (int) (1 / scaleInterval);
        typedArray.recycle();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint.setColor(Color.parseColor("#e2e5e2"));
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float intervalWidthHalf = intervalWidth / 2;
                if (distanceX > 0) {  // 往左滑
                    if (currentScale.floatValue() == maxScale) {
                        if (moveX < 0) {
                            if (moveX + distanceX > 0) {
                                moveX = 0;
                            } else {
                                moveX += distanceX;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        if (moveX + distanceX < intervalWidthHalf) {
                            moveX += distanceX;
                        } else {
                            moveX = -intervalWidthHalf;
                            currentScale = currentScale.add(new BigDecimal(Float.toString(scaleInterval)));
                            if (booheeRulerSelectedListener != null) {
                                booheeRulerSelectedListener.onSelected(currentScale.floatValue());
                            }
                        }
                    }
                } else if (distanceX < 0) {  // 往右滑
                    if (currentScale.floatValue() == minScale) {
                        if (moveX > 0) {
                            if (moveX + distanceX < 0) {
                                moveX = 0;
                            } else {
                                moveX += distanceX;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        if (moveX + distanceX > -intervalWidthHalf) {
                            moveX += distanceX;
                        } else {
                            moveX = intervalWidthHalf;
                            currentScale = currentScale.subtract(new BigDecimal(Float.toString(scaleInterval)));
                            if (booheeRulerSelectedListener != null) {
                                booheeRulerSelectedListener.onSelected(currentScale.floatValue());
                            }
                        }
                    }
                }
                invalidate();
                return true;
            }
        });
    }

    @Keep
    public void setScaleInterval(float scaleInterval) {
        this.scaleInterval = scaleInterval;
        intervalCount = (int) (1 / scaleInterval);
        invalidate();
    }

    @Keep
    public void setCurrentScale(float currentScale) {
        this.currentScale = new BigDecimal(Float.toString(currentScale));
        invalidate();
    }

    public void setBooheeRulerSelectedListener(@Nullable BooheeRulerSelectedListener booheeRulerSelectedListener) {
        this.booheeRulerSelectedListener = booheeRulerSelectedListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetectorCompat.onTouchEvent(event);
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        cursorDrawable.setBounds((getMeasuredWidth() - cursorWidth) / 2, 0,
                (getMeasuredWidth() + cursorWidth) / 2, cursorHeight);
        widthHalf = getMeasuredWidth() / 2f;
//        heightHalf = getMeasuredHeight() / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint.setStrokeWidth(1);
        canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
        initPath(canvas);
        linePaint.setStrokeWidth(littleLineWidth);
        canvas.drawPath(littleLinePath, linePaint);
        linePaint.setStrokeWidth(bigLineWidth);
        canvas.drawPath(bigLinePath, linePaint);
        cursorDrawable.draw(canvas);
    }

    private void initPath(Canvas canvas) {
        littleLinePath.reset();
        littleLinePath.moveTo(widthHalf, 0);
        bigLinePath.reset();
        bigLinePath.moveTo(widthHalf, 0);
        int currentScaleNum = currentScale.intValue();
        // 当diff大于0，当前值不为整数，当diff为0，当前值为整数
        BigDecimal diff = currentScale.subtract(new BigDecimal(Float.toString(currentScaleNum)));
        float width = 0;
        if (moveX >= 0) {
            // 绘制第一条线
            float firstX = intervalWidth - moveX;
            width += firstX;
            littleLinePath.moveTo(widthHalf + width, 0);
            bigLinePath.moveTo(widthHalf + width, 0);
            if (diff.floatValue() > 0) {
                // 剩余刻度，剩余刻度为0，下一刻度就是整数值
                BigDecimal bigDecimal1 = new BigDecimal("1");
                int residueScale = bigDecimal1.subtract(diff).divide(new BigDecimal(Float.toString(scaleInterval)), BigDecimal.ROUND_HALF_UP)
                        .subtract(bigDecimal1).intValue();
//                int residueScale = (int) ((1 - diff) / scaleInterval) - 1;
                if (residueScale == 0) {
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum++;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                } else {
                    littleLinePath.rLineTo(0, littleLineHeight);
                    residueScale--;
                    if (residueScale > 0) {
                        for (int i = 0; i < residueScale; i++) {
                            width += intervalWidth;
                            littleLinePath.moveTo(widthHalf + width, 0);
                            littleLinePath.rLineTo(0, littleLineHeight);
                        }
                    }
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf + width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum++;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                }
            } else {
                if (moveX == 0) {
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf, bigLineHeight + 100, textPaint);
                }
                if (currentScaleNum < maxScale) {
                    littleLinePath.rLineTo(0, littleLineHeight);
                    int length = intervalCount - 2;
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            width += intervalWidth;
                            littleLinePath.moveTo(widthHalf + width, 0);
                            littleLinePath.rLineTo(0, littleLineHeight);
                        }
                    }
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf + width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum++;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                }
            }
            // 循环绘制
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                if (currentScaleNum >= maxScale) {
                    break;
                }
                if (width >= widthHalf) {
                    break;
                }
                if (i % (intervalCount) == 0) {
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf + width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum++;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                } else {
                    width += intervalWidth;
                    littleLinePath.moveTo(widthHalf + width, 0);
                    littleLinePath.rLineTo(0, littleLineHeight);
                }
            }
            // 绘制左边刻度
            width = 0;
            currentScaleNum = currentScale.intValue();
            width += moveX;
            littleLinePath.moveTo(widthHalf - width, 0);
            bigLinePath.moveTo(widthHalf - width, 0);
            if (diff.floatValue() > 0) {
                int residueScale = diff.divide(new BigDecimal(Float.toString(scaleInterval)), BigDecimal.ROUND_HALF_UP).intValue();
//                int residueScale = (int) ((diff) / scaleInterval) - 1;
                littleLinePath.rLineTo(0, littleLineHeight);
                residueScale--;
                if (residueScale > 0) {
                    for (int i = 0; i < residueScale; i++) {
                        width += intervalWidth;
                        littleLinePath.moveTo(widthHalf - width, 0);
                        littleLinePath.rLineTo(0, littleLineHeight);
                    }
                }
                width += intervalWidth;
                bigLinePath.moveTo(widthHalf - width, 0);
                bigLinePath.rLineTo(0, bigLineHeight);
                canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
            } else {
                bigLinePath.rLineTo(0, bigLineHeight);
                canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
            }
            // 循环绘制
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                if (currentScaleNum <= minScale) {
                    break;
                }
                if (width >= widthHalf) {
                    break;
                }
                if (i % (intervalCount) == 0) {
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf - width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum--;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
                } else {
                    width += intervalWidth;
                    littleLinePath.moveTo(widthHalf - width, 0);
                    littleLinePath.rLineTo(0, littleLineHeight);
                }
            }
        } else {
            // 绘制第一条线
            float firstX = -moveX;
            width += firstX;
            littleLinePath.moveTo(widthHalf + width, 0);
            bigLinePath.moveTo(widthHalf + width, 0);
            if (diff.floatValue() > 0) {
                // 剩余刻度，剩余刻度为0，下一刻度就是整数值
                BigDecimal bigDecimal1 = new BigDecimal("1");
                int residueScale = bigDecimal1.subtract(diff).divide(new BigDecimal(Float.toString(scaleInterval)), BigDecimal.ROUND_HALF_UP)
                        .intValue();
//                int residueScale = (int) ((1 - diff) / scaleInterval);
                littleLinePath.rLineTo(0, littleLineHeight);
                residueScale--;
                if (residueScale > 0) {
                    for (int i = 0; i < residueScale; i++) {
                        width += intervalWidth;
                        littleLinePath.moveTo(widthHalf + width, 0);
                        littleLinePath.rLineTo(0, littleLineHeight);
                    }
                }
                width += intervalWidth;
                bigLinePath.moveTo(widthHalf + width, 0);
                bigLinePath.rLineTo(0, bigLineHeight);
                currentScaleNum++;
                canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
            } else {
                canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                bigLinePath.rLineTo(0, bigLineHeight);
            }
            // 循环绘制
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                if (currentScaleNum >= maxScale) {
                    break;
                }
                if (width >= widthHalf) {
                    break;
                }
                if (i % (intervalCount) == 0) {
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf + width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum++;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf + width, bigLineHeight + 100, textPaint);
                } else {
                    width += intervalWidth;
                    littleLinePath.moveTo(widthHalf + width, 0);
                    littleLinePath.rLineTo(0, littleLineHeight);
                }
            }
            // 绘制左边刻度
            width = 0;
            currentScaleNum = currentScale.intValue();
            width += intervalWidth + moveX;
            littleLinePath.moveTo(widthHalf - width, 0);
            bigLinePath.moveTo(widthHalf - width, 0);
            if (diff.floatValue() > 0) {
                BigDecimal bigDecimal1 = new BigDecimal("1");
                int residueScale = diff.divide(new BigDecimal(Float.toString(scaleInterval)), BigDecimal.ROUND_HALF_UP)
                        .subtract(bigDecimal1).intValue();
//                int residueScale = (int) ((diff) / scaleInterval) - 1;
                if (residueScale == 0) {
                    bigLinePath.rLineTo(0, bigLineHeight);
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
                } else {
                    littleLinePath.rLineTo(0, littleLineHeight);
                    residueScale--;
                    if (residueScale > 0) {
                        for (int i = 0; i < residueScale; i++) {
                            width += intervalWidth;
                            littleLinePath.moveTo(widthHalf - width, 0);
                            littleLinePath.rLineTo(0, littleLineHeight);
                        }
                    }
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf - width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
                }
            } else {
                littleLinePath.rLineTo(0, littleLineHeight);
                int length = intervalCount - 2;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        width += intervalWidth;
                        littleLinePath.moveTo(widthHalf - width, 0);
                        littleLinePath.rLineTo(0, littleLineHeight);
                    }
                }
                width += intervalWidth;
                bigLinePath.moveTo(widthHalf - width, 0);
                bigLinePath.rLineTo(0, bigLineHeight);
                currentScaleNum--;
                canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
            }
            // 循环绘制
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                if (currentScaleNum <= minScale) {
                    break;
                }
                if (width >= widthHalf) {
                    break;
                }
                if (i % (intervalCount) == 0) {
                    width += intervalWidth;
                    bigLinePath.moveTo(widthHalf - width, 0);
                    bigLinePath.rLineTo(0, bigLineHeight);
                    currentScaleNum--;
                    canvas.drawText(String.valueOf(currentScaleNum), widthHalf - width, bigLineHeight + 100, textPaint);
                } else {
                    width += intervalWidth;
                    littleLinePath.moveTo(widthHalf - width, 0);
                    littleLinePath.rLineTo(0, littleLineHeight);
                }
            }
        }
    }

    /**
     * 尺子选中监听
     */
    public interface BooheeRulerSelectedListener {

        /**
         * 选中
         *
         * @param number 当前选中数字
         */
        void onSelected(float number);
    }
}
