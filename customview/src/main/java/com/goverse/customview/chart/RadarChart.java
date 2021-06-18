package com.goverse.customview.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.goverse.customview.R;

import java.util.List;

public class RadarChart extends View {

    public static final int CIRCLE_POINT_RADIUS = 8;

    private final Pair<Float, Float> TEXT_SIZE = new Pair<>(1080F, 40F);

    public RadarChart(Context context) {
        super(context);
        init(context);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    private String getLongestText(List<Indicate> indicateList) {

        String longestText = "";
        if (indicateList != null && indicateList.size() != 0) {
            for (Indicate indicate : indicateList) {
                if (!TextUtils.isEmpty(indicate.text)) {
                    int length = indicate.text.length();
                    if (length > longestText.length()) longestText = indicate.text;
                }
            }
        }
        return longestText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (indicateList == null) return;

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Paint paint = new Paint();
        float screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        paint.setTextSize(TEXT_SIZE.first / screenWidth * TEXT_SIZE.second);

        String longestText = getLongestText(indicateList);
        Rect rect = new Rect();
        paint.getTextBounds(longestText, 0, longestText.length(), rect);
        int diameter = Math.min(width, height) - CIRCLE_POINT_RADIUS * 2 - rect.width() * 2 - rect.height() * 4;
        int cirCleCenterX = width / 2;
        int cirCleCenterY = height / 2;

        int ovalLeft = (width - diameter) / 2;
        int ovalRight = ovalLeft + diameter;
        int ovalTop = (height - diameter) / 2;
        int ovalBottom = ovalTop + diameter;

        // draw circle content

        paint.setAntiAlias(true);
        paint.setColor(getContext().getColor(R.color.color_radar_chart_circle_content_color));
        canvas.drawOval(ovalLeft, ovalTop, ovalRight, ovalBottom, paint);

        // draw circle
        paint.setColor(getContext().getColor(R.color.color_radar_chart_circle_color));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        int space = diameter / 10;
        int circleLeft = ovalLeft;
        int circleRight = ovalRight;
        int circleTop = ovalTop;
        int circleBottom = ovalBottom;
        for (int i = 0; i < 5; i ++) {
            canvas.drawOval(circleLeft, circleTop, circleRight, circleBottom, paint);
            circleLeft += space;
            circleTop += space;
            circleRight -= space;
            circleBottom -= space;
        }

        float degrees = 360F / indicateList.size();
        Pair<Float, Float>[] rotationCirclePairs = new Pair[indicateList.size()];

        // draw radar rect
        Path path = new Path();

        // draw scale and point
        for (int i = 0; i < indicateList.size(); i++) {
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            // draw point outside
            canvas.drawCircle(cirCleCenterX, ovalTop, CIRCLE_POINT_RADIUS, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(cirCleCenterX, ovalTop, cirCleCenterX, cirCleCenterY, paint);

            // calculate position
            // 点A(x1,y1)
            // 圆心（x0,y0)
            // 旋转角度angle
            // 旋转弧度 a=Math.PI/180*angle
            // newX=x0+(x1-x0)*Math.cos(a)-(y1-y0)*Math.sin(a);
            // newY=y1+(x1-x0)*Math.sin(a)+(y1-y0)*Math.cos(a);

            int scaleX = cirCleCenterX;
            int scaleY = (int)(cirCleCenterY - ((indicateList.get(i).percent) * diameter / 2));
            float[] rotationScalePos = getRotationPos(degrees * i, scaleX, scaleY, cirCleCenterX, cirCleCenterY);
            float rotationScaleX = rotationScalePos[0];
            float rotationScaleY = rotationScalePos[1];

            if (i == 0) path.moveTo(rotationScaleX, rotationScaleY);
            path.lineTo(rotationScaleX, rotationScaleY);

            float[] rotationPos = getRotationPos(degrees * i, cirCleCenterX, ovalTop, cirCleCenterX, cirCleCenterY);
            rotationCirclePairs[i] = new Pair<>(rotationPos[0], rotationPos[1]);
            canvas.rotate(degrees, cirCleCenterX, cirCleCenterY);
        }
        path.lineTo(cirCleCenterX, (int)(cirCleCenterY - ((indicateList.get(0).percent) * diameter / 2)));
        paint.setColor(getContext().getColor(R.color.color_radar_chart_rect_content_color));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
        paint.setColor(getContext().getColor(R.color.color_radar_chart_rect_color));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        // draw text
        paint.setTextSize(TEXT_SIZE.first / screenWidth * TEXT_SIZE.second);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);

        Bitmap upBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_arrow_up);
        Bitmap downBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_arrow_down);

        for (int i = 0; i < rotationCirclePairs.length; i ++) {
            Indicate indicate = indicateList.get(i);

            float textX = rotationCirclePairs[i].first;
            float textY = rotationCirclePairs[i].second;

            paint.getTextBounds(indicate.text, 0, indicate.text.length(), rect);
            int textHeight = rect.height();
            float textSpace = rect.height() / 2 + textHeight;
            if (rotationCirclePairs[i].first == cirCleCenterX && rotationCirclePairs[i].second < cirCleCenterY) { // 顶点
                textX = rotationCirclePairs[i].first - rect.width() / 2;
                textY = rotationCirclePairs[i].second - rect.height();
            } else if (rotationCirclePairs[i].first < cirCleCenterX && rotationCirclePairs[i].second < cirCleCenterY) { // 左上
                textX = rotationCirclePairs[i].first - rect.width() - textSpace;
                textY = rotationCirclePairs[i].second - rect.height();
            } else if (rotationCirclePairs[i].first > cirCleCenterX && rotationCirclePairs[i].second < cirCleCenterY) { // 右上
                textX = rotationCirclePairs[i].first + textSpace;
                textY = rotationCirclePairs[i].second - rect.height() ;
            } else if (rotationCirclePairs[i].first < cirCleCenterX && rotationCirclePairs[i].second > cirCleCenterY) { // 左下
                textX = rotationCirclePairs[i].first - rect.width() - textSpace - rect.height();
                textY = rotationCirclePairs[i].second + rect.height();
            } else if (rotationCirclePairs[i].first > cirCleCenterX && rotationCirclePairs[i].second > cirCleCenterY) { // 右下
                textX = rotationCirclePairs[i].first + rect.height();
                textY = rotationCirclePairs[i].second + rect.height();
            } else if (rotationCirclePairs[i].first == cirCleCenterX && rotationCirclePairs[i].second > cirCleCenterY) { // 底点
                textX = rotationCirclePairs[i].first - rect.width() / 2;
                textY = rotationCirclePairs[i].second + rect.height() + textSpace;
            }
            paint.setColor(Color.BLACK);
            canvas.drawText(indicate.text, textX, textY, paint);
            paint.setColor(getContext().getColor(indicate.upward ? R.color.color_radar_chart_trend_circle_up_color : R.color.color_radar_chart_trend_circle_down_color));
            float circleX = textX + rect.width() + textHeight;
            float circleY = textY - textHeight / 2;
            float radius = textHeight / 2;
            canvas.drawCircle(circleX, circleY, radius, paint);
            paint.setColor(Color.WHITE);
            float arrowSpace = textHeight / 4;
            RectF bitmapRect = new RectF(circleX - radius + arrowSpace, circleY - radius + arrowSpace, circleX + radius - arrowSpace, circleY + radius - arrowSpace);
            canvas.drawBitmap(indicate.upward ? upBitmap : downBitmap, null, bitmapRect, paint);
        }
    }

    private float[] getRotationPos(float degree, int x, int y, int cirCleX, int circleY) {
        float a = (float) ((degree * Math.PI) / 180F); ;
        float rotationX = (float) (x + (x - cirCleX) * Math.cos(a) - (y - circleY) * Math.sin(a));
        float rotationY = (float) (circleY + (x - cirCleX) * Math.sin(a) + (y - circleY) * Math.cos(a));
        return new float[]{rotationX, rotationY};
    }

    private List<Indicate> indicateList;

    public void setData(List<Indicate> indicateList) {

        if (indicateList != null && indicateList.size() > 0) {
            this.indicateList = indicateList;
            postInvalidate();
        }

    }

    public static final class Indicate {
        public String text;
        public float percent;
        public boolean upward;
        public Indicate(String text, float percent, boolean upward) {
            this.text = text;
            this.percent = percent;
            this.upward = upward;
        }
    }

}
