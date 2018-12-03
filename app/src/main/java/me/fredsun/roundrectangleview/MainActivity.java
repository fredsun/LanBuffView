package me.fredsun.roundrectangleview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RoundedRectangleVerticalView roundRectangleView;
    private RoundedRectangleHorizontalView roundRectangleHorizontalView;

    private LinkedHashMap kindsMap = new LinkedHashMap<String, Integer>();
    private ArrayList<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roundRectangleView = (me.fredsun.roundrectangleview.RoundedRectangleVerticalView)findViewById(R.id.roundRectangleView);
        roundRectangleView.setValue("30","30");
        roundRectangleView.setColor(Color.parseColor("#F7F7F7"), Color.parseColor("#FF8D00"));
        roundRectangleView.startAnimate();

        roundRectangleHorizontalView = (me.fredsun.roundrectangleview.RoundedRectangleHorizontalView)findViewById(R.id.roundRectangleHorizontalView);
        roundRectangleHorizontalView.setValue("30","30");
        roundRectangleHorizontalView.setColor(Color.parseColor("#F7F7F7"), Color.parseColor("#FF8D00"));
        roundRectangleHorizontalView.startAnimate();
    }
}
