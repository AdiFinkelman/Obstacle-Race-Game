package com.example.obstacleracegame.Models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.R;

public class MenuActivity extends AppCompatActivity {
    private AppCompatButton fastBTN;
    private AppCompatButton slowBTN;
    private static final int FAST_MODE = 500;
    private static final int SLOW_MODE = 1000;
    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        fastBTN = findViewById(DataManager.getFastId());
        fastBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = FAST_MODE;
                openMainActivity();
            }
        });

        slowBTN = findViewById(DataManager.getSlowId());
        slowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = SLOW_MODE;
                openMainActivity();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static int getMode() {
        return mode;
    }
}