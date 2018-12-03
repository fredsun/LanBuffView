package me.fredsun.roundrectangleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 2018/9/10.
 */

public class PieChartHollowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_hollow);
        List<PieData> mDatas = new ArrayList<>();
        mDatas.add(new PieData("a", 0, Color.parseColor("#AE02A9")));
        mDatas.add(new PieData("a", 0, Color.parseColor("#00A9FA")));
        mDatas.add(new PieData("a", 1, Color.parseColor("#66BC23")));
        mDatas.add(new PieData("a", 1, Color.parseColor("#FFB123")));
        mDatas.add(new PieData("a", 0, Color.parseColor("#FE5222")));

        PieChartHollowView pieChart = findViewById(R.id.pie_chart_hollow);
        pieChart.setmStartAngle(-90);
        pieChart.setData(mDatas,2, PieChartHollowView.PART);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}
