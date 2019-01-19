package me.fredsun.roundrectangleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
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
        List<Integer> kcalData = new ArrayList<>();
        ArrayList<KcalData> kcalChartData = new ArrayList<>();

//        kcalData.add(0, 40);
//        kcalData.add(1, 99);
//        kcalData.add(2, 82);
//        kcalData.add(3, 37);
//        kcalData.add(4, 12);
//        kcalData.add(5, 2);
//        kcalData.add(6, 11);
//        kcalData.add(7, 5);
//        kcalData.add(8, 0);
        kcalData.add(0, 0);
        kcalData.add(1, 1);
        kcalData.add(2, 999);
        Integer maxCalory;
        if (kcalData.size() == 0){
            maxCalory = 0;
            kcalChartData.add(new KcalData(0,0));
        }else {
            maxCalory =(int)((Collections.max(kcalData) * 1.2f));
            for (int i =0; i<kcalData.size();i++){
                kcalChartData.add(new KcalData((i+1)*5, kcalData.get(i)));
            }
        }

        int durTime = kcalChartData.size();
        int intTenTotalTime = Math.round(durTime);

        lineChartView.init(intTenTotalTime, maxCalory, kcalChartData);

        lineChartView.startAnim();
        Log.i("test", "test");
    }
}
