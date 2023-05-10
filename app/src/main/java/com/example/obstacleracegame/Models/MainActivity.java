package com.example.obstacleracegame.Models;

import android.location.LocationRequest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obstacleracegame.Interfaces.LocationCallback;
import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.Logic.GameManager;
import com.example.obstacleracegame.R;
import com.example.obstacleracegame.SignalGenerator;
import com.example.obstacleracegame.Utilities.MySP;
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
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //alert = new AlertDialog.Builder(this);

        findView();

//        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
//                .setWaitForAccurateLocation(false)
//                .setMinUpdateIntervalMillis(500)
//                .setMaxUpdateDelayMillis(1000)
//                .build();
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//
//                }
//            }
//        };
        gameManager = new GameManager(cars, rocks, coins, hearts, this.getApplicationContext(), score);
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
        gameManager.moveLeft(view);
    }

    public void moveRight(View view) {
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
//        stepDetector.start();
        super.onResume();
        //gameManager.startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        gameManager.stopTime();
        SignalGenerator.releaseMediaPlayer();
        MySP.getInstance().saveToJason(gameManager.getScore(), 0.0, 0.0);
        //      stepDetector.stop();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        gameManager.stopTime();
        //      stepDetector.stop();
        super.onStop();
    }

}