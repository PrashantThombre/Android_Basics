package edu.sjsu.prashant.androidsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    TextView tv;
    private float val1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        tv = findViewById(R.id.tv_reading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorManager != null)
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "Highest So Far.. " + val1);
        if(mSensor != null)
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        for(float value : sensorEvent.values)
        if(sensorEvent.values[1] > val1) {
            val1 = sensorEvent.values[1];
            String strValue = String.format(Locale.US,"%.02f", val1);
            tv.setText(strValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
