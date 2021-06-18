package com.goverse.customview.pickview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RandomPickView extends ViewGroup implements PickItemView.OnPickedListener {

    public static final String TAG = "RandomPickView";

    private List<PickItemBean> pickItemBeanList;

    private int circleR;

    private PickItemView.OnPickedListener onPickedListener;

    public RandomPickView(Context context) {
        super(context);
    }

    public RandomPickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomPickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void calculateCircleR() {
        circleR = getWidth() / ((getChildCount() / 6) * 2 + 1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout---left: " + left + ",top: " + top + ",right: " + right + ",bottom: " + bottom);
        calculateCircleR();
        List<CircleRect> circleRectList = calculateRandomChildViewPosition(left, top, right, bottom);
        int r = circleR / 2;
        for (int i = 0; i < circleRectList.size(); i ++) {
            CircleRect circleRect = circleRectList.get(i);
            getChildAt(i).layout(circleRect.x - r, circleRect.y - r, circleRect.x + r, circleRect.y + r);
        }
    }

    private List<CircleRect> calculateRandomChildViewPosition(int l, int t, int r, int b) {

        int centerX = (r - l) / 2;
        int centerY = (b - t) / 2;
        List<CircleRect> circleRectList = new ArrayList<>();
        circleRectList.add(new CircleRect(centerX, centerY));
        calculatePosition(circleRectList, circleRectList.get(circleIndex));
        circleIndex = 0;
        return circleRectList;
    }

    public static final class CircleRect {
        public int x;
        public int y;
        public CircleRect left;
        public CircleRect leftTop;
        public CircleRect right;
        public CircleRect rightTop;
        public CircleRect leftBottom;
        public CircleRect rightBottom;

        public CircleRect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CircleRect that = (CircleRect) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, left, leftTop, right, rightTop, leftBottom, rightBottom);
        }
    }

    int circleIndex = 0;

    private void calculatePosition(List<CircleRect> circleRectList, CircleRect circleRect) {
        Log.d(TAG, "size: " + circleRectList.size() + ", circleRect: " + circleRect.x + ", " + circleRect.y);
        if (circleRectList.size() == getChildCount()) return;
        int r = circleR / 2;
        Log.d(TAG, "r: " + r);
        int l = (int) Math.sqrt(circleR * circleR - (circleR / 2) * (circleR / 2));
        Log.d(TAG, "l: " + l);
        if (circleRect.left == null) {
            circleRect.left = new CircleRect(circleRect.x - circleR, circleRect.y);
            Log.d(TAG, "circleRect.left: " + circleRect.left.x + ", " + circleRect.left.y);
            if (!circleRectList.contains(circleRect.left)) {
                circleRectList.add(circleRect.left);
                if (circleRectList.size() == getChildCount()) return;
            }
        }
        if (circleRect.right == null) {
            circleRect.right = new CircleRect(circleRect.x + circleR, circleRect.y);
            Log.d(TAG, "circleRect.right: " + circleRect.right.x + ", " + circleRect.right.y);
            if (!circleRectList.contains(circleRect.right)){
                circleRectList.add(circleRect.right);
                if (circleRectList.size() == getChildCount()) return;
            }
        }
        if (circleRect.leftBottom == null) {
            circleRect.leftBottom = new CircleRect(circleRect.x - r, circleRect.y + l);
            Log.d(TAG, "circleRect.leftBottom: " + circleRect.leftBottom.x + ", " + circleRect.leftBottom.y);
            if (!circleRectList.contains(circleRect.leftBottom)) {
                circleRectList.add(circleRect.leftBottom);
                if (circleRectList.size() == getChildCount()) return;
            }
        }
        if (circleRect.rightBottom == null) {
            circleRect.rightBottom = new CircleRect(circleRect.x + r, circleRect.y + l);
            Log.d(TAG, "circleRect.rightBottom: " + circleRect.rightBottom.x + ", " + circleRect.rightBottom.y);
            if (!circleRectList.contains(circleRect.rightBottom)){
                circleRectList.add(circleRect.rightBottom);
                if (circleRectList.size() == getChildCount()) return;
            }
        }
        if (circleRect.leftTop == null) {
            circleRect.leftTop = new CircleRect(circleRect.x - r, circleRect.y - l);
            Log.d(TAG, "circleRect.leftTop: " + circleRect.leftTop.x + ", " + circleRect.leftTop.y);
            if (!circleRectList.contains(circleRect.leftTop)){
                circleRectList.add(circleRect.leftTop);
                if (circleRectList.size() == getChildCount()) return;
            }
        }
        if (circleRect.rightTop == null) {
            circleRect.rightTop = new CircleRect(circleRect.x + r, circleRect.y - l);
            Log.d(TAG, "circleRect.rightTop: " + circleRect.rightTop.x + ", " + circleRect.rightTop.y);
            if (!circleRectList.contains(circleRect.rightTop)){
                circleRectList.add(circleRect.rightTop);
                if (circleRectList.size() == getChildCount()) return;
            }
        }

        calculatePosition(circleRectList, circleRectList.get(++ circleIndex));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw---getHeight: " + canvas.getHeight() + ",getWidth: " + canvas.getWidth());

    }

    public List<PickItemView> getPickedItemViewList() {

        List<PickItemView> pickItemViewList = new ArrayList<>();
        for (int i = 0 ;i < getChildCount(); i ++) {
            PickItemView pickItemView = (PickItemView) getChildAt(i);
            if (pickItemView.isPicked()) pickItemViewList.add(pickItemView);
        }
        return pickItemViewList;
    }

    public void setData(List<PickItemBean> pickItemBeanList) {
        this.pickItemBeanList = pickItemBeanList;
        setUpItemView(this.pickItemBeanList);
    }

    private void setUpItemView(List<PickItemBean> pickItemBeanList) {

        if (pickItemBeanList != null && pickItemBeanList.size() != 0) {
            for (PickItemBean pickItemBean : pickItemBeanList) {
                PickItemView pickItemView = new PickItemView(getContext(), pickItemBean);
                pickItemView.setOnPickedListener(this);
                addView(pickItemView);
            }
        }
        invalidate();
    }

    public void setOnPickedListener(PickItemView.OnPickedListener onPickedListener) {
        this.onPickedListener = onPickedListener;
    }

    @Override
    public void onPicked(boolean isPicked, PickItemView pickItemView) {
        if (this.onPickedListener != null) this.onPickedListener.onPicked(isPicked, pickItemView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();
        if (maxPickedCount != 0) {
            int currentPickedCount = 0;
            boolean isInPickedArea = false;
            for (int i = 0 ;i < getChildCount(); i ++) {
                PickItemView pickItemView = (PickItemView) getChildAt(i);
                if (pickItemView.isPicked()) {
                    currentPickedCount ++;
                    if (x >= pickItemView.getLeft() && x <= pickItemView.getRight() && y >= pickItemView.getTop() && y <= pickItemView.getBottom()) isInPickedArea = true;
                }
            }
            if (currentPickedCount >= maxPickedCount && !isInPickedArea) {
                if (this.onPickedListener != null) this.onPickedListener.onPickedOutRange();
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onPickedOutRange() {

    }

    private int maxPickedCount;

    public void setMaxPickedCount(int count) {
        this.maxPickedCount = count;
    }
}
