package com.goverse.customview.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import androidx.annotation.Nullable;
import com.goverse.customview.R;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Shader.TileMode.CLAMP;

public class ProgressBarChart extends View {

    /**
     * Defination for the color gradient of each progress segment.
     */
    public static class ProgressSegment {

        public ProgressSegment(int startColor, int endColor, String text) {
            this.startColor = startColor;
            this.endColor = endColor;
            this.text = text;
        }

        /**
         * start color
         */
        public int startColor;

        /**
         * end color
         */
        public int endColor;

        public String text;
    }

    public static final class ProgressBarBean {
        public List<ProgressSegment> progressSegmentList;
        public String[] progressText;

        public ProgressBarBean(List<ProgressSegment> progressSegmentList, String[] progressText) {
            this.progressSegmentList = progressSegmentList;
            this.progressText = progressText;
        }
    }

    public static final int DEFAULT_START_PROGRESS = 0;

    public static final int DEFAULT_END_PROGRESS = 100;

    public static final int DEFAULT_BACKGROUND_ALPHA = 50;

    public static final int DEFAULT_PROGRESS_BAR_HEIGHT = 40;

    public static final int DEFAULT_PROGRESS_TEXT_SIZE = 30;

    public static final int DEFAULT_SUBSCRIPT_HEIGHT = 30;

    public static final int DEFAULT_SPACE_WIDTH = 20;

    /**
     * the space between two of segments
     */
    private int mSpaceWidth = DEFAULT_SPACE_WIDTH;

    /**
     * the color of spaceing
     */
    private int mSpaceColor = Color.WHITE;

    /**
     * max progress, default 100
     */
    private int mMaxProgress = DEFAULT_END_PROGRESS;

    /**
     * progress
     */
    private float mProgress = DEFAULT_START_PROGRESS;

    /**
     * the height of subscript
     */
    private int mSubscriptHeight = DEFAULT_SUBSCRIPT_HEIGHT;

    /**
     * the height of progressbar
     */
    private int mProgrssBarHeight = DEFAULT_PROGRESS_BAR_HEIGHT;

    /**
     * the padding from subscript to progressbar
     */
    private int mSubscriptTopPadding = 10;

    /**
     * the textSize of subscriptText which indicate the current progress
     */
    private float mSubscriptTextSize = 20;

    /**
     * the textSize of progressbar which indicate the start progress and end progress
     */
    private float mProgressTextSize = DEFAULT_PROGRESS_TEXT_SIZE;

    /**
     * the color of start progress text
     */
    private int mStartProgressTextColor;

    /**
     * the color of end progress text
     */
    private int mEndProgressTextColor;

    private int mProgressBarPaddingLeft;
    private int mProgressBarPaddingRight;
    private int mChartWidth;
    private boolean mSubscriptVisibility;
    private Pair<Float, Float> TEXT_SIZE = new Pair<>(1080F, 35F);

    /**
     * the width of each progress segment
     */
    private int mSingleSegWidth;

    private String[] progressText;

    private int mSubscriptPaddingLeft;

    private List<ProgressSegment> mProgressSegmentList = new ArrayList<>();

    public ProgressBarChart(Context context) {
        super(context);
        initView(context, null);
    }

    public ProgressBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ProgressBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.progressChart);
        mSubscriptVisibility = array.getBoolean(R.styleable.progressChart_subscriptVisibility, true);
        mSpaceWidth = (int) array.getDimension(R.styleable.progressChart_spaceWidth, DEFAULT_SPACE_WIDTH);
        mSpaceColor = array.getColor(R.styleable.progressChart_spaceColor, Color.WHITE);
        mMaxProgress = array.getInt(R.styleable.progressChart_maxProgress, DEFAULT_END_PROGRESS);
        mProgress = array.getInt(R.styleable.progressChart_progress, DEFAULT_START_PROGRESS);
        mSubscriptHeight = (int) array.getDimension(R.styleable.progressChart_subscriptHeight, DEFAULT_SUBSCRIPT_HEIGHT);
        mProgrssBarHeight = (int) array.getDimension(R.styleable.progressChart_progrssBarHeight, DEFAULT_PROGRESS_BAR_HEIGHT);
        mSubscriptTopPadding = (int) array.getDimension(R.styleable.progressChart_subscriptTopPadding, 10);
        mProgressBarPaddingLeft = (int) array.getDimension(R.styleable.progressChart_progrssBarPaddingLeft, 0);
        mProgressBarPaddingRight = (int) array.getDimension(R.styleable.progressChart_progrssBarPaddingRight, 0);
        mSubscriptTextSize = array.getDimension(R.styleable.progressChart_subscriptTextSize, 20);
        mProgressTextSize = array.getDimension(R.styleable.progressChart_progressTextSize, DEFAULT_PROGRESS_TEXT_SIZE);
        mStartProgressTextColor = array.getColor(R.styleable.progressChart_startProgressTextColor, Color.parseColor("#4D000000"));
        mEndProgressTextColor = array.getColor(R.styleable.progressChart_endProgressTextColor, Color.parseColor("#4D000000"));
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCanvas(canvas);
    }

    private void drawCanvas(Canvas canvas) {

        if (mProgressSegmentList== null || mProgressSegmentList.size() == 0) {
            return;
        }

        drawProgressBackground(canvas);
        drawProgress(canvas);
    }

    /**
     * draw progress background
     * @param canvas canvas
     */
    private void drawProgressBackground(Canvas canvas) {

        mSubscriptPaddingLeft = mSubscriptHeight * 2;
        mProgressBarPaddingLeft += mSubscriptPaddingLeft / 2;
        mChartWidth = canvas.getWidth() - mProgressBarPaddingLeft * 2 - mProgressBarPaddingRight;
        int top = mSubscriptHeight;
        mSingleSegWidth = (mChartWidth - (mProgressSegmentList.size() - 1) * mSpaceWidth) / mProgressSegmentList.size();
        int singleSegStartLeft = mProgressBarPaddingLeft + mSubscriptPaddingLeft / 2;
        Paint paint = new Paint();
        for (int i = 0; i < mProgressSegmentList.size(); i ++) {
            float right = mSingleSegWidth * (i + 1) + mSpaceWidth * i;
            ProgressSegment progressSegment = mProgressSegmentList.get(i);
            paint.setShader(new LinearGradient(singleSegStartLeft, top, right, top + mProgrssBarHeight, new int[]{progressSegment.startColor, progressSegment.endColor}, null,  CLAMP));
            drawSegment(i, paint, canvas);
            paint.reset();
        }
    }

    private void drawSegment(int i, Paint paint, Canvas canvas) {
        int top = mSubscriptHeight;
        int singleSegWidth = ((mChartWidth - (mProgressSegmentList.size() - 1) * mSpaceWidth) / mProgressSegmentList.size());
        int singleSegStartLeft = ((singleSegWidth + mSpaceWidth) * i) + mProgressBarPaddingLeft;
        int singleSegStartRight = (singleSegWidth * (i + 1) + mSpaceWidth * i) + mProgressBarPaddingLeft;

        int radio_r = mProgrssBarHeight / 5;
        int radio_R = radio_r * 2;
        if (i == 0) {
            Path path = new Path();
            path.addArc(new RectF(singleSegStartLeft, top, radio_R + singleSegStartLeft, top + radio_R), 180, 90);
            path.lineTo(singleSegWidth + singleSegStartLeft, top);
            path.lineTo(singleSegWidth + singleSegStartLeft, top + mProgrssBarHeight);
            path.lineTo(radio_r + singleSegStartLeft, top + mProgrssBarHeight);
            path.addArc(new RectF(singleSegStartLeft, top + mProgrssBarHeight - radio_R, radio_R + singleSegStartLeft, top + mProgrssBarHeight), 90, 90);
            path.lineTo(singleSegStartLeft, top + radio_r);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(path, paint);
        } else if (i == mProgressSegmentList.size() - 1) {
            Path path = new Path();
            path.moveTo(mChartWidth - mProgrssBarHeight / 2 + mProgressBarPaddingLeft, top);
            path.addArc(new RectF(mChartWidth - mProgrssBarHeight + mProgressBarPaddingLeft, top, mChartWidth + mProgressBarPaddingLeft , top + mProgrssBarHeight), 270, 180);
            path.moveTo(mChartWidth - mProgrssBarHeight / 2 + mProgressBarPaddingLeft, top + mProgrssBarHeight);
            path.lineTo(singleSegStartLeft, top + mProgrssBarHeight);
            path.lineTo(singleSegStartLeft, top);
            path.lineTo(mChartWidth - mProgrssBarHeight / 2 + mProgressBarPaddingLeft, top);
            canvas.drawPath(path, paint);
        } else {
            canvas.drawRect(singleSegStartLeft, top, singleSegStartRight, top + mProgrssBarHeight, paint);
        }

        // draw text inside progressbar
        paint.setShader(null);
        paint.setTextAlign(Paint.Align.CENTER);
        float textSize = getContext().getResources().getDisplayMetrics().widthPixels / TEXT_SIZE.first * TEXT_SIZE.second;
        paint.setTextSize(textSize);
        String text = mProgressSegmentList.get(i).text;
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        float textLeft = singleSegStartLeft + singleSegWidth / 2F;
        float textTop = top + (mProgrssBarHeight - rect.height() / 2F) + rect.bottom;
        paint.setColor(getContext().getColor(R.color.color_progress_bar_chart_text_color));
        canvas.drawText(text, textLeft, textTop, paint);

        // draw text below progressbar
        if (progressText != null && i < progressText.length) {
            String pbText = progressText[i];
            paint.getTextBounds(pbText, 0, pbText.length(), rect);
            float pbTextLeft = singleSegStartLeft + singleSegWidth;
            float pbTextTop = top + mProgrssBarHeight + rect.height() * 2;
            paint.setColor(getContext().getColor(R.color.color_progress_bar_chart_progress_text_color));
            canvas.drawText(pbText, pbTextLeft, pbTextTop, paint);
        }

    }

    private void drawProgress(Canvas canvas) {
        int progressWidth = (int) ((mProgress / mMaxProgress) * (mChartWidth - mSpaceWidth * (mProgressSegmentList.size() - 1)));
        int lastWidth = progressWidth % mSingleSegWidth;
        drawSubScript(lastWidth, canvas);
    }

    private void drawSubScript(float overWidth, Canvas canvas) {

        int subScriptTop = 0;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        int index = -1;
        float diff = -1;
        for (int i = 0; i< this.progressText.length; i ++) {

            float progressStart = Float.valueOf(progressText[i]);
            if ((i == 0 && mProgress <= progressStart)) {
                index = 0;
                diff = (mProgress / progressStart) * mSingleSegWidth;
                break;
            } else if (i == this.progressText.length - 1 && mProgress >= progressStart) {
                index = this.progressText.length;
                diff = mSingleSegWidth / 2;
                break;
            } else if ((i < this.progressText.length - 1) && mProgress > progressStart && mProgress < Float.valueOf(progressText[i + 1])) {
                index = i + 1;
                diff = (mProgress - progressStart) / (Float.valueOf(progressText[i + 1]) - progressStart) * mSingleSegWidth;
                break;
            }

        }
        paint.setColor(getSubScriptColor(index));
        float progressX = index * mSingleSegWidth + diff + mProgressBarPaddingLeft;
        Path path = new Path();
        path.moveTo(progressX - mSubscriptHeight / 2, subScriptTop);
        path.lineTo(progressX, mSubscriptHeight + subScriptTop);
        path.lineTo(progressX + mSubscriptHeight / 2, subScriptTop);
        path.lineTo(progressX - mSubscriptHeight / 2, subScriptTop);
        canvas.drawPath(path, paint);
    }

    private int getSubScriptColor(int index) {
        ProgressSegment progressSegment = mProgressSegmentList.get(index);
        return progressSegment.startColor;
    }

    private int getProgressSegmentPos() {
        float progressWidth = (mProgress / mMaxProgress) * (mChartWidth - mSpaceWidth * (mProgressSegmentList.size() - 1));
        int progressSegCount = (int) (progressWidth / mSingleSegWidth);
        int overWidth = (int) (progressWidth % mSingleSegWidth);
        if (progressSegCount == 0) {
            return 0;
        } else {
            if (overWidth == 0) {
                return progressSegCount - 1;
            } else {
                return progressSegCount;
            }
        }
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        requestLayout();
    }

    public void setMaxProgress(int mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
        requestLayout();
    }

    public void setData(ProgressBarBean progressBarBean) {
        if (progressBarBean != null) {
            this.mProgressSegmentList = progressBarBean.progressSegmentList;
            this.progressText = progressBarBean.progressText;
            requestLayout();
        }
    }

}
