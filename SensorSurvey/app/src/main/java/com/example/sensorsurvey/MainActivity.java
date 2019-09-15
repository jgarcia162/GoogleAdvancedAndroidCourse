package com.example.sensorsurvey;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Sensor lightSensor;

    private TextView lightSensorTextView;
    private TextView proximitySensorTextView;
    private ImageView sensorImageView;
    private ViewGroup rootView;
    float proximityLow = 0;
    float proximityMid = 0;
    float proximityMax = 0;
    float lightLow = 0;
    float lightMid = 0;
    float lightMax = 0;
    int originalImageHeightAndWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightSensorTextView = findViewById(R.id.label_light);
        proximitySensorTextView = findViewById(R.id.label_proximity);
        sensorImageView = findViewById(R.id.sensor_image_view);
        rootView = findViewById(R.id.sensor_layout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        proximityLow = proximitySensor.getMaximumRange() / 3;
        proximityMid = proximitySensor.getMaximumRange() / 2;
        proximityMax = proximitySensor.getMaximumRange();

        lightLow = lightSensor.getMaximumRange() / 3;
        lightMid = lightSensor.getMaximumRange() / 2;
        lightMax = lightSensor.getMaximumRange();

        originalImageHeightAndWidth = sensorImageView.getLayoutParams().width;

        if (lightSensor == null) {
            lightSensorTextView.setText(getString(R.string.error_no_sensor));
        }

        if (proximitySensor == null) {
            proximitySensorTextView.setText(getString(R.string.error_no_sensor));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (proximitySensor != null) {
            sensorManager.registerListener(this,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }

        if (lightSensor != null) {
            sensorManager.registerListener(
                    this,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                changeBackgroundColor(currentValue);
                lightSensorTextView.setText(getString(R.string.label_light, currentValue));
                break;
            case Sensor.TYPE_PROXIMITY:
                changeImageSize(currentValue);
                proximitySensorTextView.setText(getString(R.string.label_proximity, currentValue));
                break;
            default:

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeBackgroundColor(float currentValue) {
        @ColorInt int color = transformValueToColor(currentValue);

        if (currentValue <= lightLow) {
            getWindow().getDecorView().setBackgroundColor(color);
        } else if (currentValue > lightLow && currentValue <= lightMid) {
            getWindow().getDecorView().setBackgroundColor(color);
        } else if (currentValue > lightMid && currentValue <= lightMax) {
            getWindow().getDecorView().setBackgroundColor(color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int transformValueToColor(float currentValue) {
        long currentValue1 = (long) currentValue;
        String hexColor = String.format("#%06X", (0xFFFFFF & currentValue1));
        return Color.parseColor(hexColor);
    }

    private void changeImageSize(float currentValue) {
        int newWidthAndHeight = (int) (originalImageHeightAndWidth * currentValue) / 2;
        sensorImageView.getLayoutParams().height = newWidthAndHeight;
        sensorImageView.getLayoutParams().width = newWidthAndHeight;
        sensorImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}
