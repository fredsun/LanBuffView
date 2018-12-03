package me.fredsun.roundrectangleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by fred on 2018/9/10.
 */

public class RoundGradientProgressBarActivity  extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_gradient_progressbar);
        RoundGradientProgressBar roundGradientProgressBar = findViewById(R.id.round_gradient_progress_bar);
        roundGradientProgressBar.setDegree(Float.valueOf("40"));
//        roundGradientProgressBar.setSaveEnabled(true);

    }
}
