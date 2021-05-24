package com.b11a.android.autosaver.ui.driving;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.b11a.android.autosaver.MainActivity;
import com.b11a.android.autosaver.R;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.b11a.android.autosaver.ConstantsKt.kPrefs;
import static com.b11a.android.autosaver.ConstantsKt.kServerURL;

public class AccidentReportedActivity extends AppCompatActivity implements SensorEventListener {

    private Context context = this;

    private static String currentTime, latitude, longitude;

    SensorManager sensorManager;
    Sensor sensorAccelerometer;

    SmsManager smsManager = SmsManager.getDefault();
    private String userToken;

    private String userURL = kServerURL("users/userinfos");
    private OkHttpClient client = new OkHttpClient();

    private static double firstLongitude, firstLatitude, nowLongitude, nowLatitude;
    private boolean isReported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_reported);

        userToken = kPrefs(context).getString("userToken", "");

        Intent intent = getIntent();
        currentTime = intent.getStringExtra("currentTime");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        Log.e("currentTime2", currentTime);
        Log.e("latitude2", String.valueOf(latitude));
        Log.e("longitude2", String.valueOf(longitude));

        Timer timer = new Timer(10000, 1000);
        timer.start();
        isReported = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccidentReportedActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                firstLongitude = location.getLongitude();
                firstLatitude = location.getLatitude();
                nowLongitude = location.getLongitude();
                nowLatitude = location.getLatitude();
            }

            Log.e("1", String.valueOf(firstLongitude));
            Log.e("2", String.valueOf(firstLatitude));
            Log.e("3", String.valueOf(nowLongitude));
            Log.e("4", String.valueOf(nowLatitude));

            final LocationListener gpsLocationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if (isReported) {
                        firstLongitude = 0;
                        firstLatitude = 0;
                        nowLongitude = 0;
                        nowLatitude = 0;
                    } else {
                        nowLongitude = location.getLongitude();
                        nowLatitude = location.getLatitude();

                        if (firstLatitude != nowLatitude || firstLongitude != nowLongitude) {
                            timer.cancel();
                            isReported = true;
                            Intent intent = new Intent(context, AccidentSecondCancelledActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    Log.e("nowLongitude", String.valueOf(nowLongitude));
                    Log.e("nowLatitude", String.valueOf(nowLatitude));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);

        }

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

            String second = String.valueOf((getMin % (60 * 1000)) / 1000);

        }

        @Override
        public void onFinish() {
            // 문자 및 전화

            Request request = new Request.Builder()
                    .url(userURL)
                    .header("Authorization", userToken)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    JSONObject jsonObject;
                    String responseString = Objects.requireNonNull(response.body()).string();

                    try {
                        jsonObject = new JSONObject(responseString);

                        if(jsonObject.has("emergency")) {
                            String telNo = jsonObject.getString("emergency").split("/")[1];
                            String msg = "사고가 발생했습니다." + "- 일시 : " + currentTime + "- 위치 : 위도 " + latitude + " 경도 " + longitude + "혈액형 : " + jsonObject.getString("blood");
                            sendSMS("119", msg);
                            sendSMS(telNo, msg);

                            Toast.makeText(getApplicationContext(), "문자 전송 완료", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent callPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:/119"));
                    startActivity(callPhone);
                }
            });

            // 119 전화
//            String telNo = "010-0000-0000";
//            Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:/" + telNo));
//            startActivity(call);

            // 원래는 119
//            sendSMS("000-0000-0000", "사고가 발생했습니다. \n"
//                    + "- 일시 : " + currentTime + "\n" + "- 위치 : 위도 " + latitude + " 경도 " + longitude + "\n" + "혈액형 : ");

            // 보호
//            sendSMS("000-0000-0000", "사고가 발생했습니다. \n"
//                    + "- 일시 : " + currentTime + "\n" + "- 위치 : 위도 " + latitude + " 경도 " + longitude + "\n" + "혈액형 : ");자

            Log.e("report", "사고가 발생했습니다."
                    + "- 일시 : " + currentTime + "- 위치 : 위도 " + latitude + " 경도 " + longitude + " 혈액형 : ");
            isReported = true;

        }
    }

    public void sendSMS(String phoneNumber, String text) {
        smsManager.sendTextMessage(phoneNumber, null, text, null, null);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}