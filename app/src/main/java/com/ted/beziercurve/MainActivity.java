package com.ted.beziercurve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ted.bezierview.BezierCurve;

public class MainActivity extends AppCompatActivity {

    private BezierCurve mBC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBC = (BezierCurve) findViewById(R.id.bezier_curve);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBC.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Animation Show time", Toast.LENGTH_SHORT).show();
                mBC.beginAnima();
            }
        },500);
    }
}
