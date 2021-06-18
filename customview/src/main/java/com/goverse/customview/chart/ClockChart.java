package com.goverse.customview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.goverse.customview.R;

public class ClockChart extends FrameLayout {

    public static final int CLOCK_DEGREE_COUNT = 200;

    public static final int CLOCK_SCALE_WIDTH = 10;

    public static final int CLOCK_SCALE_SEG_WIDTH = 15;

    public static final int CLOCK_SCALE_SEG_POINT_RADIUS = 3;

    public static final int CLOCK_SCALE_SEG_COUNT = 5;

    public static final int CLOCK_SCALE_SEG_CIRCLE_RADIUS = 6;

    private final int[] SCALE_COLOR_ARRAY = new int[] {R.color.color_chart_seg_scale_0, R.color.color_chart_seg_scale_1, R.color.color_chart_seg_scale_2, R.color.color_chart_seg_scale_3, R.color.color_chart_seg_scale_4};

    private final int SCALE_COLOR_DEFAULT = R.color.color_chart_seg_scale_default;

    private View contentView;

    private ClockBean clockBean;

    public ClockChart(Context context) {
        super(context);
        init(context);
    }

    public ClockChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClockChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        contentView = LayoutInflater.from(context).inflate(R.layout.chart_clock, this);
        setWillNotDraw(false);
    }

    private void setContentView(ClockBean clockBean) {
        this.clockBean = clockBean;
        TextView tvDate = contentView.findViewById(R.id.tv_date);
        tvDate.setText(clockBean.date);
        TextView tvScore = contentView.findViewById(R.id.tv_score);
        tvScore.setText(clockBean.score + "");
        TextView tvLevelPercent = contentView.findViewById(R.id.tv_level_percent);
        tvLevelPercent.setText(clockBean.userPercent);
        TextView tvLevel = contentView.findViewById(R.id.tv_level);
        tvLevel.setText(clockBean.level);
        TextView tvTrend = contentView.findViewById(R.id.tv_trend);
        tvTrend.setText((clockBean.lastScore - clockBean.score) + "");
        ImageView ivArrow = contentView.findViewById(R.id.iv_arrow);
        ivArrow.setImageResource(R.drawable.small);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (clockBean != null) onDrawBackground(canvas);
        super.onDraw(canvas);
    }

    private void onDrawBackground(Canvas canvas) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int diameter = Math.min(width, height);
        int cirCleCenterX = width / 2;
        int cirCleCenterY = height / 2;

        int scaleTop = (height - diameter) / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        float degrees = 360F / CLOCK_DEGREE_COUNT;
        int segIndex = CLOCK_DEGREE_COUNT / CLOCK_SCALE_SEG_COUNT;
        int indexScale = (int) (this.clockBean.score / 100F * CLOCK_DEGREE_COUNT);
        for (int i = 0; i < CLOCK_DEGREE_COUNT; i++) {

            if (i % segIndex == 0) {
                paint.setColor(getContext().getColor((i > indexScale) ? SCALE_COLOR_DEFAULT : SCALE_COLOR_ARRAY[i / segIndex]));
                paint.setStrokeWidth(3);
                canvas.drawLine(cirCleCenterX, scaleTop, cirCleCenterX, scaleTop + dp2px(getContext(), CLOCK_SCALE_SEG_WIDTH), paint);
                canvas.drawCircle(cirCleCenterX, scaleTop + dp2px(getContext(), CLOCK_SCALE_SEG_WIDTH + CLOCK_SCALE_SEG_POINT_RADIUS), CLOCK_SCALE_SEG_CIRCLE_RADIUS, paint);
            } else {
                paint.setStrokeWidth(1);
                canvas.drawLine(cirCleCenterX, scaleTop, cirCleCenterX, scaleTop + dp2px(getContext(), CLOCK_SCALE_WIDTH), paint);
            }
            canvas.rotate(degrees, cirCleCenterX, cirCleCenterY);
        }
    }

    public void setData(ClockBean clockBean) {
        setContentView(clockBean);;
        postInvalidate();
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static class ClockBean {
        public String date;
        public int score;
        public int lastScore;
        public String userPercent;
        public String level;
    }
}
