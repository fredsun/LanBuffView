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
//        kcalData.add(new KcalData(0, 5));
//        kcalData.add(new KcalData(5, 10));
//        kcalData.add(new KcalData(10, 5));
//        kcalData.add(new KcalData(15, 10));
//        kcalData.add(new KcalData(20, 5));
//        kcalData.add(new KcalData(25, 10));
//        kcalData.add(new KcalData(30, 15));
//        kcalData.add(new KcalData(35, 20));
//        kcalData.add(new KcalData(40, 15));
//        kcalData.add(new KcalData(45, 20));
//        kcalData.add(new KcalData(50, 40));
//        kcalData.add(new KcalData(55, 20));
//        lineChartView.init(55, 40, kcalData);

//        integerList.add(0);
//        integerList.add(5);
//        integerList.add(11);
//        integerList.add(37);
//        integerList.add(82);
//        integerList.add(99);
//        integerList.add(40);
//        int maxValue = 0;
//        for (int i=0; i < integerList.size(); i++){
//            int currentValue = 0;
//            if (i==0 || integerList.get(i) < integerList.get(i-1)){
//                currentValue = 0;
//            }else {
//                currentValue = integerList.get(i) - integerList.get(i-1);
//            }
//            if (currentValue > maxValue){
//                maxValue = currentValue;
//            }
//            kcalData.add(new KcalData(i, currentValue));
//        }

//        kcalData.add(new KcalData(0, 0));
//        kcalData.add(new KcalData(1, 5));
//        kcalData.add(new KcalData(2, 11));
//        kcalData.add(new KcalData(3, 37));
//        kcalData.add(new KcalData(4, 82));
//        kcalData.add(new KcalData(5, 99));
//        kcalData.add(new KcalData(6, 40));

//        kcalData.add(new KcalData(0, 40));
//        kcalData.add(new KcalData(1, 99));
//        kcalData.add(new KcalData(2, 82));
//        kcalData.add(new KcalData(3, 37));
//        kcalData.add(new KcalData(4, 11));
//        kcalData.add(new KcalData(5, 5));
//        kcalData.add(new KcalData(6, 0));
        kcalData.add(new KcalData(0,0));

        lineChartView.init(0, 0, kcalData);

        lineChartView.startAnim();
        Log.i("test", "test");
    }
}
