package com.example.wenzty.wuziqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private WuziqiView mWuziqiView;
    private Button mreStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWuziqiView = (WuziqiView) findViewById(R.id.wuziqi);

        mreStart = (Button) findViewById(R.id.restart);
        mreStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWuziqiView.start();
            }
        });

    }


}
