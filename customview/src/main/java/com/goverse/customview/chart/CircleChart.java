package com.goverse.customview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleChart extends View {

    private CircleBean[] circleBeans;

    public CircleChart(Context context) {
        super(context);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.circleBeans == null || this.circleBeans.length == 0) return;

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int diam = Math.min(canvas.getWidth(), canvas.getHeight());
        int centerLeft = (width - diam) / 2;
        int centerTop = (height - diam) / 2;

        int circleWidth = 50;
        int marginDegree = 2;

        RectF rectF = new RectF(centerLeft, centerTop, centerLeft + diam, centerTop + diam);
        Paint paint = new Paint();
        int layer = canvas.saveLayer(centerLeft, centerTop, centerLeft + diam, centerTop + diam, paint, Canvas.ALL_SAVE_FLAG);
        int startAngle = 90;
        for (CircleBean circleBean : circleBeans) {
            paint.setColor(circleBean.color);
            canvas.drawArc(rectF, startAngle + marginDegree, circleBean.ratio * 360 - marginDegree, true, paint);
            startAngle = (int) (startAngle + marginDegree + circleBean.ratio * 360 - marginDegree);
        }

        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        paint.setXfermode(xfermode);
        rectF.left = centerLeft + circleWidth;
        rectF.right = centerLeft + diam - circleWidth;
        rectF.top = centerTop + circleWidth;
        rectF.bottom = centerTop + diam - circleWidth;
        canvas.drawArc(rectF, 0, 360, true, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layer);
    }

    public void setData(CircleBean[] circleBeans) {
        this.circleBeans = circleBeans;
        postInvalidate();
    }

    public static final class CircleBean {

        public int color;
        public float ratio;
        public CircleBean(int color, float ratio) {
            this.color = color;
            this.ratio = ratio;
        }
    }

}
