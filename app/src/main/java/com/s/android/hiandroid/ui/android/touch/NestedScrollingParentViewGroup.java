package com.s.android.hiandroid.ui.android.touch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import org.jetbrains.annotations.NotNull;

public class NestedScrollingParentViewGroup extends LinearLayout implements NestedScrollingParent {

    private static final int FLYING_FROM_CHILD_TO_PARENT = 0;
    private static final int FLYING_FROM_PARENT_TO_CHILD = 2;

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private NestedScrollingChildView nestedScrollingChildView;
    private int imageHeight;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;
    private int TOUCH_SLOP;
    private int mCurFlyingType;

    private boolean mIsSetFlying;
    private boolean mIsChildFlying;
    private boolean mIsBeingDragged;
    private int mLastY;
    private int mLastMotionY;

    public NestedScrollingParentViewGroup(Context context) {
        this(context, null);
    }

    public NestedScrollingParentViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingParentViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NestedScrollingParentViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mScroller = new Scroller(getContext());
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        TOUCH_SLOP = viewConfiguration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final ImageView imageView = (ImageView) getChildAt(0);
        nestedScrollingChildView = (NestedScrollingChildView) getChildAt(2);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imageHeight <= 0) {
                    imageHeight = imageView.getMeasuredHeight();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int pointCount = ev.getPointerCount();
        if (pointCount > 1) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsSetFlying = false;
                mIsChildFlying = false;
                initOrResetVelocityTracker();
                resetScroller();
                break;
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getScrollY() > 0 && getScrollY() < imageHeight && mVelocityTracker != null) {
                    //处理连接处的父控件fling事件
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) mVelocityTracker.getYVelocity();
                    mCurFlyingType = yVelocity > 0 ? FLYING_FROM_CHILD_TO_PARENT : FLYING_FROM_PARENT_TO_CHILD;
                    recycleVelocityTracker();
                    parentFling(-yVelocity);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 拦截落在不可滑动子View的MOVE事件
                final int y = (int) ev.getY();
                final int yDiff = Math.abs(y - mLastMotionY);
                boolean isInNestedChildViewArea = isTouchNestedInnerView((int) ev.getRawX(), (int) ev.getRawY());
                if (yDiff > TOUCH_SLOP && !isInNestedChildViewArea) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastY == 0) {
                    mLastY = (int) event.getY();
                    return true;
                }
                int y = (int) event.getY();
                int dy = y - mLastY;
                mLastY = y;
                scrollBy(0, -dy);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastY = 0;
                break;
        }
        return true;
    }

    /**
     * 判断当前位置是否在nestedScrollingChildView上
     */
    private boolean isTouchNestedInnerView(int x, int y) {
        if (nestedScrollingChildView.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] location = new int[2];
        nestedScrollingChildView.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + nestedScrollingChildView.getMeasuredWidth();
        int bottom = top + nestedScrollingChildView.getMeasuredHeight();
        return y >= top && y <= bottom && x >= left && x <= right;
    }

    @Override
    public boolean onStartNestedScroll(@NotNull View child, @NotNull View target, int nestedScrollAxes) {
        return target instanceof NestedScrollingChildView && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(@NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedPreScroll(@NotNull View target, int dx, int dy, @NotNull int[] consumed) {
        int scrollY = getScrollY();
        // 下滑
        if (dy > 0) {
            // 显示ImageView
            if (scrollY > 0 && nestedScrollingChildView.getScrollY() == 0) {
                scrollBy(0, -dy);
                consumed[1] = dy;
            }
        } else if (dy < 0) {
            // 上滑
            // 隐藏ImageView
            if (scrollY < imageHeight) {
                scrollBy(0, -dy);
                consumed[1] = dy;
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > imageHeight) {
            y = imageHeight;
        }
        super.scrollTo(x, y);
    }

    @Override
    public boolean onNestedFling(@NotNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NotNull View target, float velocityX, float velocityY) {
        if (target instanceof NestedScrollingChildView && nestedScrollingChildView.getScrollY() == 0 && getScrollY() == imageHeight) {
            mCurFlyingType = FLYING_FROM_CHILD_TO_PARENT;
            parentFling(velocityY);
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int currY = mScroller.getCurrY();
        switch (mCurFlyingType) {
            case FLYING_FROM_CHILD_TO_PARENT:
                scrollTo(0, currY);
                invalidate();
                if (!mIsSetFlying) {
                    mIsSetFlying = true;
                    parentFling(-mScroller.getCurrVelocity());
                }
                break;
            case FLYING_FROM_PARENT_TO_CHILD:
                scrollTo(0, currY);
                invalidate();
                if (currY >= imageHeight && !mIsChildFlying) {
                    mIsChildFlying = true;
                    nestedScrollingChildView.fling((int) mScroller.getCurrVelocity());
                }
                if (getScrollY() < imageHeight && !mIsSetFlying) {
                    mIsSetFlying = true;
                    parentFling(mScroller.getCurrVelocity());
                }
                break;
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onNestedScrollAccepted(@NotNull View child, @NotNull View target, int axes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NotNull View child) {
        nestedScrollingParentHelper.onStopNestedScroll(child);
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void resetScroller() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    private void parentFling(float velocityY) {
        mScroller.fling(0, getScrollY(), 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }
}
