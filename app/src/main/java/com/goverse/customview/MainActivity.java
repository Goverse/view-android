package com.goverse.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.goverse.customview.recyclercard.RecyclerCardActivity;
import com.goverse.customview.recyclercard.RecyclerCardLayout;

import static com.goverse.customview.CustomViewActivity.ViewType.ANIM_VIEW;
import static com.goverse.customview.CustomViewActivity.ViewType.CIRCLE_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.CLOCK_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.PICK_VIEW;
import static com.goverse.customview.CustomViewActivity.ViewType.PROGRESSBAR_CHART;
import static com.goverse.customview.CustomViewActivity.ViewType.RADAR_CHART;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Integer data[] = {CIRCLE_CHART, CLOCK_CHART, PROGRESSBAR_CHART, RADAR_CHART, PICK_VIEW, ANIM_VIEW};
    private String text[] = {"circle_chart", "clock_chart", "progressbar_chart", "radar_chart", "pick_view", "anim_view", "recycler_card"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, text));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick:" + i);
                if (i < data.length) {
                    Intent intent = new Intent(MainActivity.this, CustomViewActivity.class);
                    intent.putExtra("viewType", data[i]);
                    startActivity(intent);
                } else {
                    if (i == data.length) {
                        Intent intent = new Intent(MainActivity.this, RecyclerCardActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}