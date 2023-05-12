package com.example.obstacleracegame.Models;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obstacleracegame.Interfaces.StepCallback;
import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.Logic.GameManager;
import com.example.obstacleracegame.R;
import com.example.obstacleracegame.SignalGenerator;
import com.example.obstacleracegame.Utilities.StepDetector;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] cars;
    private ShapeableImageView[][] rocks;
    private ShapeableImageView[][] coins;
    private ShapeableImageView[] hearts;
    private MaterialTextView score;
    private GameManager gameManager;
    private StepDetector stepDetector;
    private final boolean sensorMode = MenuActivity.getSensorMode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        gameManager = new GameManager(cars, rocks, coins, hearts, this.getApplicationContext(), score);

        //setSensorMode(sensorMode);
        if (sensorMode)
            playSensor();
    }

    private void playSensor() {
        findViewById(DataManager.getLeftBTNID()).setVisibility(View.INVISIBLE);
        findViewById(DataManager.getRightBTNID()).setVisibility(View.INVISIBLE);
        StepCallback stepCallback = new StepCallback() {
            @Override
            public void stepLeft() {
                gameManager.moveLeft(null);
            }

            @Override
            public void stepRight() {
                gameManager.moveRight(null);
            }

        };
        stepDetector = new StepDetector(this.getApplicationContext(), stepCallback);
        stepDetector.start();
    }

    private void findView() {
        int rows = DataManager.getNumOfRows();
        int cols = DataManager.getNumOfCols();
        int numOfHearts = DataManager.getNumOfHearts();
        int numOfCars = DataManager.getNumOfCols();

        rocks = new ShapeableImageView[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                rocks[i][j] = findViewById(DataManager.getRocksID(i, j));
        }

        coins = new ShapeableImageView[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                coins[i][j] = findViewById(DataManager.getCoinsID(i, j));
        }

        hearts = new ShapeableImageView[numOfHearts];
        for (int i = 0; i < numOfHearts; i++)
            hearts[i] = findViewById(DataManager.getHeartsID(i));

        cars = new ShapeableImageView[numOfCars];
        for (int i = 0; i < numOfCars; i++)
            cars[i] = findViewById(DataManager.getCarsID(i));

        score = findViewById(R.id.main_LBL_score);
    }

    public void moveLeft(View view) {
        if (!sensorMode)
            gameManager.moveLeft(view);
    }

    public void moveRight(View view) {
        if (!sensorMode)
            gameManager.moveRight(view);
    }

    @Override
    protected void onPause() {
        gameManager.stopTime();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameManager.startTime();
        if (stepDetector != null)
            stepDetector.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        gameManager.stopTime();
        SignalGenerator.releaseMediaPlayer();
        stepDetector.stop();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        gameManager.stopTime();
        super.onStop();
    }
}