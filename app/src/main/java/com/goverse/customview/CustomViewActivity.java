package com.goverse.customview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.goverse.customview.animview.AnimNumberView;
import com.goverse.customview.chart.CircleChart;
import com.goverse.customview.chart.ClockChart;
import com.goverse.customview.chart.ProgressBarChart;
import com.goverse.customview.chart.RadarChart;
import com.goverse.customview.pickview.PickItemBean;
import com.goverse.customview.pickview.PickItemView;
import com.goverse.customview.pickview.RandomPickView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static com.goverse.customview.CustomViewActivity.ViewType.ANIM_VIEW;
import static com.goverse.customview.CustomViewActivity.ViewType.CIRCLE_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.CLOCK_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.PICK_VIEW;
import static com.goverse.customview.CustomViewActivity.ViewType.PROGRESSBAR_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.RADAR_CHART;

public class CustomViewActivity extends AppCompatActivity {

    public static final String TAG = "CustomViewActivity";

    public static class ViewType {
        public static final int CIRCLE_CHART = 0;
        public static final int CLOCK_CHART = 1;
        public static final int PROGRESSBAR_CHART = 2;
        public static final int RADAR_CHART = 3;
        public static final int PICK_VIEW = 4;
        public static final int ANIM_VIEW = 5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        Intent intent = getIntent();
        int viewType = intent.getIntExtra("viewType", 0);
        showView(viewType);
    }

    private void showView(int viewType) {

        if (viewType == CIRCLE_CHART) showCircleChart();
        else if (viewType == CLOCK_CHART) showClockChart();
        else if (viewType == PROGRESSBAR_CHART) showProgressBarChart();
        else if (viewType == RADAR_CHART) showRadarChart();
        else if (viewType == PICK_VIEW) showPickView();
        else if (viewType == ANIM_VIEW) showAnimView();
    }

    private void showAnimView() {

        EditText etOldNum = findViewById(R.id.et_old);
        EditText etNewNum = findViewById(R.id.et_new);
        findViewById(R.id.layout_anim).setVisibility(View.VISIBLE);
        AnimNumberView animNumberView = findViewById(R.id.anim_number_view);
        Editable oldEditable = etOldNum.getText();
        Editable newEditable = etNewNum.getText();

        if (oldEditable.length() == 0 || newEditable.length() == 0) {
            Toast.makeText(this, "请输入数字 ！！！", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            long oldValue = Long.parseLong(oldEditable.toString());
            long newValue = Long.parseLong(newEditable.toString());
            NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.getDefault());
            numberInstance.setMaximumFractionDigits(1);
            numberInstance.setMinimumFractionDigits(0);
            String formatNewValue = numberInstance.format(newValue);
            animNumberView.setText(numberInstance.format(oldValue), formatNewValue);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "数字超出范围 ！！！", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCircleChart() {
        CircleChart circleChart = findViewById(R.id.circleChart);
        circleChart.setVisibility(View.VISIBLE);
        CircleChart.CircleBean[] circleBeans = new CircleChart.CircleBean[4];
        circleBeans[0] = new CircleChart.CircleBean(Color.parseColor("#FF38D7D5"), 0.3f);
        circleBeans[1] = new CircleChart.CircleBean(Color.parseColor("#FF2AD181"), 0.2f);
        circleBeans[2] = new CircleChart.CircleBean(Color.parseColor("#FFFF9125"), 0.25f);
        circleBeans[3] = new CircleChart.CircleBean(Color.parseColor("#FFFFD547"), 0.25f);
        circleChart.setData(circleBeans);
    }

    private void showPickView() {
        RandomPickView randomPickView = findViewById(R.id.randomPickView);
        randomPickView.setVisibility(View.VISIBLE);
        randomPickView.setMaxPickedCount(3);
        randomPickView.setOnPickedListener(new PickItemView.OnPickedListener() {
            @Override
            public void onPicked(boolean isPicked, PickItemView pickItemView) {

            }

            @Override
            public void onPickedOutRange() {
                Toast.makeText(CustomViewActivity.this, "超出范围", Toast.LENGTH_LONG).show();
            }
        });
        List<PickItemBean> pickItemBeanList = new ArrayList<>();
        pickItemBeanList.add(new PickItemBean(0, "000", 0));
        pickItemBeanList.add(new PickItemBean(1, "100", 100));
        pickItemBeanList.add(new PickItemBean(2, "200", 200));
        pickItemBeanList.add(new PickItemBean(3, "300", 300));
        pickItemBeanList.add(new PickItemBean(4, "400", 400));
        pickItemBeanList.add(new PickItemBean(5, "500", 500));
        pickItemBeanList.add(new PickItemBean(6, "600", 600));
        pickItemBeanList.add(new PickItemBean(7, "700", 700));
        pickItemBeanList.add(new PickItemBean(8, "800", 800));
        pickItemBeanList.add(new PickItemBean(9, "900", 900));
        pickItemBeanList.add(new PickItemBean(10, "1000", 900));
        pickItemBeanList.add(new PickItemBean(11, "1100", 900));
        pickItemBeanList.add(new PickItemBean(12, "1200", 900));
        pickItemBeanList.add(new PickItemBean(13, "1300", 900));
        randomPickView.setData(pickItemBeanList);
    }

    private void showClockChart() {
        ClockChart clockChart = findViewById(R.id.clockChart);
        clockChart.setVisibility(View.VISIBLE);
        ClockChart.ClockBean clockBean = new ClockChart.ClockBean();
        clockBean.score = 100;
        clockBean.lastScore = 40;
        clockBean.level = "优秀";
        clockBean.date = "8月23日 18:00";
        clockBean.userPercent = "超越了56%的用户";
        clockChart.setData(clockBean);
    }

    private void showRadarChart() {
        RadarChart radarChart = findViewById(R.id.radarChart);
        radarChart.setVisibility(View.VISIBLE);
        List<RadarChart.Indicate> indicateList = new ArrayList<>();
        indicateList.add(new RadarChart.Indicate("力量", 0.4F, true));
        indicateList.add(new RadarChart.Indicate("体能", 0.8F, false));
        indicateList.add(new RadarChart.Indicate("体型", 0.5F, true));
        indicateList.add(new RadarChart.Indicate("心肺", 0.3F, false));
        indicateList.add(new RadarChart.Indicate("灵敏", 0.6F, true));
        indicateList.add(new RadarChart.Indicate("平衡", 0.7F, false));
        indicateList.add(new RadarChart.Indicate("柔韧", 1F, true));
        radarChart.setData(indicateList);
    }

    private void showProgressBarChart() {
       ProgressBarChart progressBarChart = findViewById(R.id.progressBarChart);
        progressBarChart.setVisibility(View.VISIBLE);
        progressBarChart.setMaxProgress(10);
        progressBarChart.setProgress(160F);
        String[] progressText = new String[]{"59", "81", "168", "198"};
        ProgressBarChart.ProgressBarBean progressBarBean = new ProgressBarChart.ProgressBarBean(setProgressChart(), progressText);
        progressBarChart.setData(progressBarBean);
    }

    private List<ProgressBarChart.ProgressSegment> setProgressChart() {
        List<ProgressBarChart.ProgressSegment> mProgressSegmentList = new ArrayList<>();
        mProgressSegmentList.add(new ProgressBarChart.ProgressSegment(Color.parseColor("#FFFF5C4E"), Color.parseColor("#FFFF5C4E"), "差"));
        mProgressSegmentList.add(new ProgressBarChart.ProgressSegment(Color.parseColor("#FFFF9125"), Color.parseColor("#FFFF9125"), "较差"));
        mProgressSegmentList.add(new ProgressBarChart.ProgressSegment(Color.parseColor("#FFFFD547"), Color.parseColor("#FFFFD547"), "中等"));
        mProgressSegmentList.add(new ProgressBarChart.ProgressSegment(Color.parseColor("#FF00DA7B"), Color.parseColor("#FF00DA7B"), "良好"));
        mProgressSegmentList.add(new ProgressBarChart.ProgressSegment(Color.parseColor("#FF38D7D5"), Color.parseColor("#FF38D7D5"), "优秀"));
        return mProgressSegmentList;
    }
}