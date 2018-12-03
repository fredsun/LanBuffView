package me.fredsun.roundrectangleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 2018/9/10.
 */

public class PieChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_pie_chart);
        List<PieData> mDatas = new ArrayList<>();
        mDatas.add(new PieData("a", 20, Color.parseColor("#AE02A9")));
        mDatas.add(new PieData("a", 30, Color.parseColor("#00A9FA")));
        mDatas.add(new PieData("a", 50, Color.parseColor("#66BC23")));
        mDatas.add(new PieData("a", 10, Color.parseColor("#FFB123")));
        mDatas.add(new PieData("a", 50, Color.parseColor("#FE5222")));

        PieChartView pieChart = findViewById(R.id.pie_chart);
        pieChart.setmStartAngle(45);
        pieChart.setData(mDatas,160, PieChartView.COUNT);
    }
}
