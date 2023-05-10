package com.example.obstacleracegame.Models;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obstacleracegame.Interfaces.StepCallback;
import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.Logic.GameManager;
import com.example.obstacleracegame.R;
import com.example.obstacleracegame.Utilities.StepDetector;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class SensorActivity extends AppCompatActivity {

    private ShapeableImageView[] cars;
    private ShapeableImageView[][] rocks;
    private ShapeableImageView[][] coins;
    private ShapeableImageView[] hearts;
    private MaterialTextView score;
    private StepDetector stepDetector;
    private GameManager gameManager;
    private ExtendedFloatingActionButton leftBTN;
    private ExtendedFloatingActionButton rightBTN;
    private ShapeableImageView car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initStepDetector();
        gameManager = new GameManager(cars, rocks, coins, hearts, this.getApplicationContext(), score);
        hideButtons();
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepX() {
                if (stepDetector.getStepsX() > 0)
                    gameManager.moveLeft(car);

                else if (stepDetector.getStepsX() < 0)
                    gameManager.moveRight(car);
            }

            @Override
            public void stepY() {
                //pass
            }

            @Override
            public void stepZ() {
                // pass
            }
        });
    }

    private void hideButtons() {
        leftBTN = findViewById(DataManager.getLeftBTNID());
        rightBTN = findViewById(DataManager.getRightBTNID());
        leftBTN.hide();
        rightBTN.hide();
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
        car = findViewById(R.id.main_IMG_car2);
    }
}
