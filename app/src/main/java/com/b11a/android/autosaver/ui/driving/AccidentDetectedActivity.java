package com.b11a.android.autosaver.ui.driving;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b11a.android.autosaver.R;

import java.util.Timer;
import java.util.TimerTask;

public class AccidentDetectedActivity extends AppCompatActivity {

    private static String currentTime, latitude, longitude;

    private TextView tvTimer;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_detected);

        tvTimer = findViewById(R.id.tv_timer_accident);
        btnCancel = findViewById(R.id.btn_cancel);



        Intent intent = getIntent();
        currentTime = intent.getStringExtra("currentTime");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        Log.e("currentTime", currentTime);
        Log.e("latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));

        Timer timer = new Timer(3000, 1000);
        timer.start();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                startActivity(new Intent(AccidentDetectedActivity.this, AccidentFirstCancelledActivity.class));
            }
        });
    }

    class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

            String second = String.valueOf((getMin % (60 * 1000)) / 1000);

            tvTimer.setText(second);
        }

        @Override
        public void onFinish() {
            tvTimer.setText("0");
            Intent intent = new Intent(AccidentDetectedActivity.this, AccidentReportedActivity.class);

            intent.putExtra("currentTime", currentTime);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude", longitude);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}