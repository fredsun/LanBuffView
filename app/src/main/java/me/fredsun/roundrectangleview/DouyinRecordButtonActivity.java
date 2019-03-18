package me.fredsun.roundrectangleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by fred on 2019/1/26.
 */


public class DouyinRecordButtonActivity extends AppCompatActivity {

    private RecordButton recordButton;
    private boolean isBluetoothConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_record_button);
        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnRecordStateChangedListener(new RecordButton.OnRecordStateChangedListener() {
            @Override
            public void onDouYinCameraClick() {
                Toast.makeText(DouyinRecordButtonActivity.this, "拍照", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDouYinVideoStart() {
                Toast.makeText(DouyinRecordButtonActivity.this, "录制开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDouYinVideoFinish() {
                Toast.makeText(DouyinRecordButtonActivity.this, "录制结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDouYinClipStart() {
                Toast.makeText(DouyinRecordButtonActivity.this, "剪辑开始"+isBluetoothConnect, Toast.LENGTH_SHORT).show();
                if (isBluetoothConnect){
                    recordButton.startClip();
                }
            }

            @Override
            public void onDouYinClipFinish() {
                Toast.makeText(DouyinRecordButtonActivity.this, "剪辑结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDouYinClipYet() {

            }

            @Override
            public void onDouYinVideoYet() {

            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordButton.setBitmapPhoto(RecordButton.ModeCamera);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordButton.setBitmapPhoto(RecordButton.ModeVideo);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordButton.setBitmapPhoto(RecordButton.ModeClip);
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBluetoothConnect = !isBluetoothConnect;
            }
        });
    }
}