package me.fredsun.roundrectangleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 2018/9/11.
 */

public class LineChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        LineChartView lineChartView = (LineChartView)findViewById(R.id.line_chart);
        List<Integer> integerList = new ArrayList<>();
        ArrayList<KcalData> kcalData = new ArrayList<>();
        kcalData.add(new KcalData(0, 5));
        kcalData.add(new KcalData(5, 10));
        kcalData.add(new KcalData(10, 5));
        kcalData.add(new KcalData(15, 10));
        kcalData.add(new KcalData(20, 5));
        kcalData.add(new KcalData(25, 10));
        kcalData.add(new KcalData(30, 15));
        kcalData.add(new KcalData(35, 20));
        kcalData.add(new KcalData(40, 15));
        kcalData.add(new KcalData(45, 20));
        kcalData.add(new KcalData(50, 40));
        kcalData.add(new KcalData(55, 20));
        lineChartView.init(55, 40, kcalData);
        lineChartView.startAnim();
        Log.i("test", "test");
    }
}
