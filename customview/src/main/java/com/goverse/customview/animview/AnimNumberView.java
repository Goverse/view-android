package com.goverse.customview.animview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.animation.PathInterpolatorCompat;

import java.util.ArrayList;
import java.util.List;

public class AnimNumberView extends android.view.View {

    public static final String TAG = "AnimTextView";

    public AnimNumberView(Context context) {
        super(context);
    }

    public AnimNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static final int NUM_START_MARGIN = 10;
    public static final int ANIMATION_DURATION = 600;
    public static final int ANIMATION_DELAY = 66; // 9 位数22, 6位数44
    public static final int OLD_OPACITY_DURATION = 333;
    public static final int NEW_OPACITY_DELAY = 200;
    public static final int NEW_OPACITY_DURATION = 400;
    private List<SingleText> oldSingleTextList = null;
    private List<SingleText> newSingleTextList = null;
    private int measureHeight;
    private float numStartMargin = NUM_START_MARGIN;
    private Rect oldBoundRect = new Rect();
    private Rect newBoundRect = new Rect();
    private TextAnim oldTextAnim;
    private TextAnim newTextAnim;

    public final void setText(String oldText, String newText) {
        Log.d(TAG, "oldText: " + oldText + ", newText: " + newText);
        init(oldText, newText);
        post(oldTextAnim);
        post(newTextAnim);
    }

    private int getAnimNumCount(List<SingleText> singleTextList) {

        int animNumCount = 0;
        if (singleTextList == null || singleTextList.size() == 0) return animNumCount;

        for (int i = 0; i < singleTextList.size(); i ++) {
            animNumCount += (singleTextList.get(i).keepStable ? 0 : 1);
        }

        return animNumCount;
    }

    private float getNumberStartMargin(String content) {

        float numStartMargin = NUM_START_MARGIN;
        if (content.length() >= 12 && content.length() < 18) numStartMargin = 8F;
        if (content.length() >= 18) numStartMargin = 5F;
        return numStartMargin;
    }

    private float getNumberTextSize(String content) {
        float spValue = 53.33F;
        if (content.length() >= 10 && content.length() < 18) spValue = 43.33F;
        if (content.length() >= 16) spValue = 23.33F;
        return spValue;
    }

    private void checkNeedKeepSymbol(List<SingleText> oldSingleTextList, List<SingleText> newSingleTextList) {

        if (oldSingleTextList == null || oldSingleTextList.size() == 0
                || newSingleTextList == null || newSingleTextList.size() == 0
                || oldSingleTextList.size() != newSingleTextList.size()) {
            return;
        }

        int size = Math.min(oldSingleTextList.size(), newSingleTextList.size());
        for (int i = 0; i < size; i ++) {
            SingleText oldSingleText = oldSingleTextList.get(i);
            SingleText newSingleText = newSingleTextList.get(i);
            if (oldSingleText.text == newSingleText.text) {
                oldSingleText.setKeepStable(true);
                newSingleText.setKeepStable(true);
            } else break;
        }
    }

    final static class SingleText {
        char text;
        Rect rect;
        boolean keepStable;

        public SingleText(char text, Rect rect) {
            this.text = text;
            this.rect = rect;
        }

        public void setKeepStable(boolean keepStable) {
            this.keepStable = keepStable;
        }

        public boolean isKeepStable() {
            return keepStable;
        }
    }

    private void init(String oldText, String newText) {
        initPaint(getNumberTextSize(newText));
        paint.getTextBounds(oldText, 0, oldText.length(), oldBoundRect);
        paint.getTextBounds(newText, 0, newText.length(), newBoundRect);
        oldSingleTextList = parseSingleNumList(oldText, oldBoundRect);
        newSingleTextList = parseSingleNumList(newText, newBoundRect);
        measureHeight = oldBoundRect.height();
        checkNeedKeepSymbol(oldSingleTextList, newSingleTextList);
        oldTextAnim = new TextAnim(getAnimNumCount(oldSingleTextList), ANIMATION_DURATION, getAnimationDelay(oldSingleTextList));
        newTextAnim = new TextAnim(getAnimNumCount(newSingleTextList), ANIMATION_DURATION, getAnimationDelay(newSingleTextList));
    }

    private void initPaint(float textSize) {
        paint = new Paint();
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(sp2px(getContext(), textSize));
    }

    private List<SingleText> parseSingleNumList(String text, Rect boundRect) {

        Log.d(TAG, "parseSingleNumList: " + text + ", boundRect: " + boundRect);
        if (TextUtils.isEmpty(text)) return null;
        List<SingleText> singleTextList = new ArrayList<>();

        Rect defaultRect = new Rect();
        paint.getTextBounds("4", 0, 1, defaultRect);
        int defaultWidth = defaultRect.width();

        for (int i = 0, left = 0, startMargin = 0; i < text.length(); i ++) {
            char c = text.charAt(i);
            Rect rect = new Rect();
            paint.getTextBounds(String.valueOf(c), 0 , 1, rect);
            rect.top = boundRect.top;
            rect.bottom = boundRect.bottom;
            int averageWidth = (c == ',' ? rect.width() : defaultWidth);
            Log.d(TAG, "parseSingleNumList: " + c + ", averageWidth: " + averageWidth + ", " + rect.width());
            int offset = (averageWidth - rect.width()) / 2;
            if (i == 0) {
                rect.left = left;
                rect.right = rect.left + averageWidth - offset;
            } else {
                startMargin += numStartMargin;
                rect.left = left + startMargin + offset;
                rect.right = rect.left + averageWidth;
            }
            Log.d(TAG, "parseSingleNumList: " + c + ", after rect: " + rect + ", startMargin: " + startMargin + ", offset: " + offset);
            singleTextList.add(new SingleText(c, rect));
            left += rect.width();
        }
        return singleTextList;
    }

    private Paint paint = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        drawOldText(canvas);
        drawNewText(canvas);
    }

    private int getAnimationDelay(List<SingleText> singleTextList) {

        int needAnimCount = 0;
        for (SingleText singleText : singleTextList) {
            if (!singleText.keepStable) needAnimCount ++;
        }

        int delay;
        if (needAnimCount <= 3) {
            delay = ANIMATION_DELAY;
        } else if (needAnimCount <= 6) {
            delay = 44;
        } else if (needAnimCount <= 9) {
            delay = 22;
        } else if (needAnimCount <= 12) {
            delay = 11;
        } else if (needAnimCount <= 15) {
            delay = 5;
        } else {
            delay = 2;
        }

        return delay;
    }

    private void drawOldText(Canvas canvas) {

        if (oldSingleTextList == null || oldSingleTextList.size() == 0) return;

        for (int i = oldSingleTextList.size() - 1 ; i > -1; i --) {

            SingleText singleText = oldSingleTextList.get(i);
            boolean hasAnimationStart = oldTextAnim.hasAnimationStart(oldSingleTextList.size() - 1 - i);
            if (!hasAnimationStart || singleText.isKeepStable()) {
                paint.setAlpha(255);
                canvas.drawText(String.valueOf(singleText.text), 0, 1, singleText.rect.left, singleText.rect.height() - singleText.rect.bottom, paint);
                continue;
            }
            float animInterpolation = oldTextAnim.getAnimInterpolation(oldSingleTextList.size() - 1 - i);
            float offset = animInterpolation * oldBoundRect.height();
            int alpha = 255 - (int) ((oldTextAnim.getCurrAnimationDuration(oldSingleTextList.size() - 1 - i) * 1F / OLD_OPACITY_DURATION ) * 255F);
            if (alpha < 0) alpha = 0;
            paint.setAlpha(alpha);
            canvas.drawText(String.valueOf(singleText.text), 0, 1, singleText.rect.left, singleText.rect.height() - singleText.rect.bottom - offset, paint);
        }
    }

    private void drawNewText(Canvas canvas) {

        if (newSingleTextList == null || newSingleTextList.size() == 0) return;

        for (int i = newSingleTextList.size() - 1 ; i > -1; i --) {

            SingleText singleText = newSingleTextList.get(i);
            boolean hasAnimationStart = newTextAnim.hasAnimationStart(newSingleTextList.size() - 1 - i);
            if (!hasAnimationStart || singleText.isKeepStable()) {
                paint.setAlpha(0);
                canvas.drawText(String.valueOf(singleText.text), 0, 1, singleText.rect.left, singleText.rect.height() - singleText.rect.bottom, paint);
                continue;
            }

            float animInterpolation = newTextAnim.getAnimInterpolation(newSingleTextList.size() - 1 - i);

            float offset = animInterpolation * newBoundRect.height();

            int alpha = 0;
            int currAnimationDuration = oldTextAnim.getCurrAnimationDuration(oldSingleTextList.size() - 1 - i);
            if (currAnimationDuration >= NEW_OPACITY_DELAY) {
                alpha = (int) (((currAnimationDuration - NEW_OPACITY_DELAY) * 1F / NEW_OPACITY_DURATION ) * 255F);
            }
            paint.setAlpha(alpha);
            canvas.drawText(String.valueOf(singleText.text), 0, 1, singleText.rect.left, singleText.rect.height() - singleText.rect.bottom + newBoundRect.height() - offset, paint);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "onMeasure---widthMeasureSize: " + widthMeasureSize + ", widthMeasureMode: " + widthMeasureMode);
        Log.d(TAG, "onMeasure---heightMeasureSize: " + heightMeasureSize + ", heightMeasureMode: " + heightMeasureMode);

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            heightMeasureSize = measureHeight;
        }
        Log.d(TAG, "onMeasure---measureTextHeight: " + heightMeasureSize);

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                heightMeasureSize);
    }

    final class TextAnim implements Runnable {

        private float animRatio = 0F;
        private Animator animator;
        private int count;
        private int durationInMs;
        private int delayInMs;
        private int totalDurationInMs;
        private Interpolator animInterpolator = PathInterpolatorCompat.create(0.21F, 0F, 0.36F, 1F);
        public TextAnim(int count, int durationInMs, int delayInMs) {
            this.count = count;
            this.durationInMs = durationInMs;
            this.delayInMs = delayInMs;
            animator = ObjectAnimator.ofFloat(0F, 1F);
            animator.setDuration(this.totalDurationInMs = calculateAnimationDuration());
            animator.setInterpolator(new LinearInterpolator() {
                @Override
                public float getInterpolation(float input) {
                    animRatio = super.getInterpolation(input);
                    invalidate();
                    return animRatio;
                }
            });
        }

        public float getAnimInterpolation(int i) {
            float currentDurationInMs = this.totalDurationInMs * animRatio;
            return animInterpolator.getInterpolation((currentDurationInMs - delayInMs * i) / durationInMs);
        }

        public int getCurrAnimationDuration(int i) {
            int currentDurationInMs = (int) (this.totalDurationInMs * animRatio) - delayInMs * i;
            return currentDurationInMs > durationInMs ? durationInMs : currentDurationInMs;
        }

        private boolean hasAnimationStart(int i) {
            float currentDurationInMs = this.totalDurationInMs * animRatio;
            return currentDurationInMs >= delayInMs * i;
        }

        private int calculateAnimationDuration() {
            int animationDuration = durationInMs;
            for (int i = 0; i < count; i ++) {
                animationDuration += i * delayInMs;
            }
            return animationDuration;
        }

        public void stopAnim() {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }

        @Override
        public void run() {
            stopAnim();
            animator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(null);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity * spValue + 0.5f);
    }
}
