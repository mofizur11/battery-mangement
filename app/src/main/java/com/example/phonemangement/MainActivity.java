package com.example.phonemangement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver batteryBroadcast;
    IntentFilter intentFilter;


    TextView level, voltage, health, batteryType, chargingSource, temperature, chargingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        level = findViewById(R.id.textLevel);
        voltage = findViewById(R.id.textVoltage);
        health = findViewById(R.id.textHealth);
        batteryType = findViewById(R.id.textType);
        chargingSource = findViewById(R.id.textChargingSorce);
        temperature = findViewById(R.id.textTemp);
        chargingStatus = findViewById(R.id.textChargingStatus);

        intentFilterAndBroadcast();
    }

    private void intentFilterAndBroadcast() {

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {

                    level.setText(String.valueOf(intent.getIntExtra("level", 0)) + "%");
                    float voltTemp = (float) (intent.getIntExtra("voltage", 0) * 0.001);
                    voltage.setText(voltTemp + " V");

                    setHealth(intent);

                    batteryType.setText(intent.getStringExtra("technology"));

                    getChargingSource(intent);
                    float tempTemp = (float) intent.getIntExtra("temperature", -1) / 10;
                    temperature.setText(tempTemp + " °C");

                    setChargingStatus(intent);
                }
            }
        };
    }

    private void setChargingStatus(Intent intent) {
        int statusTemp = intent.getIntExtra("status", -1);

        switch (statusTemp)
        {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                chargingStatus.setText("Unknown");
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                chargingStatus.setText("Charging");
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                chargingStatus.setText("Dischargings");
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                chargingStatus.setText("Not Charging");
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                chargingStatus.setText("Full");
                break;
            default:
                chargingStatus.setText("Null ");
        }
    }

    private void getChargingSource(Intent intent) {
        int sourceTemp = intent.getIntExtra("plugged", -1);
        switch (sourceTemp) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                chargingSource.setText("AC");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                chargingSource.setText("USB");
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                chargingSource.setText("WireLess");
                break;

            default:
                chargingSource.setText("Null");
        }
    }

    private void setHealth(Intent intent) {
        int val = intent.getIntExtra("health", 0);

        switch (val) {
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                health.setText("Unknown");
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                health.setText("Good");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                health.setText("Overheat");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                health.setText("Dead");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                health.setText("Over Voltage");
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                health.setText("Unspecified Failure");
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                health.setText("Cold");
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(batteryBroadcast, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryBroadcast);
    }
}