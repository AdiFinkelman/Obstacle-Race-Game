package com.example.obstacleracegame.Models;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.obstacleracegame.Fragments.FragmentActivity;
import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.R;

public class MenuActivity extends AppCompatActivity {
    private static int mode;
    private static boolean sensorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        AppCompatButton fastBTN = findViewById(DataManager.getFastId());
        fastBTN.setOnClickListener(v -> {
            mode = DataManager.getFastMode();
            setSensorMode(false);
            openMainActivity();
        });

        AppCompatButton slowBTN = findViewById(DataManager.getSlowId());
        slowBTN.setOnClickListener(v -> {
            mode = DataManager.getSlowMode();
            setSensorMode(false);
            openMainActivity();
        });

        AppCompatButton sensorBTN = findViewById(DataManager.getSensorButtonID());
        sensorBTN.setOnClickListener(v -> {
            mode = DataManager.getFastMode();
            setSensorMode(true);
            openMainActivity();
        });

        AppCompatButton recordsBTN = findViewById(DataManager.getRecordsID());
        recordsBTN.setOnClickListener(v -> openRecordsActivity());
    }

    private void openRecordsActivity() {
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public static int getMode() {
        return mode;
    }

    public static boolean getSensorMode() {
        return sensorMode;
    }

    public static void setSensorMode(boolean mode) {
        sensorMode = mode;
    }
}