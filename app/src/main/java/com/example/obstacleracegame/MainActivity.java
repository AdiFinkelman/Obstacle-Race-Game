package com.example.obstacleracegame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] cars;
    private ShapeableImageView[][] rocks;
    private ExtendedFloatingActionButton leftBTN;
    private ExtendedFloatingActionButton rightBTN;
    private ShapeableImageView[] hearts;
//    private GameManager gameManager;
    public final int DELAY = 500;
    long startTime = 0;
    private CountDownTimer timer;
    private int numOfRows = 6;
    private int numOfColumns = 3;
    private int life = 3;
    private int crushes = 0;
    private boolean flag = false;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alert = new AlertDialog.Builder(this);

        findView();
        startTime();
    }

    private void findView() {
        hearts = new ShapeableImageView[] {
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        cars = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_car0),
                findViewById(R.id.main_IMG_car1),
                findViewById(R.id.main_IMG_car2)
        };
        rocks = new ShapeableImageView[6][3];
        rocks[0][0] = findViewById(R.id.main_mat0_0);
        rocks[0][1] = findViewById(R.id.main_mat0_1);
        rocks[0][2] = findViewById(R.id.main_mat0_2);

        rocks[1][0] = findViewById(R.id.main_mat1_0);
        rocks[1][1] = findViewById(R.id.main_mat1_1);
        rocks[1][2] = findViewById(R.id.main_mat1_2);

        rocks[2][0] = findViewById(R.id.main_mat2_0);
        rocks[2][1] = findViewById(R.id.main_mat2_1);
        rocks[2][2] = findViewById(R.id.main_mat2_2);

        rocks[3][0] = findViewById(R.id.main_mat3_0);
        rocks[3][1] = findViewById(R.id.main_mat3_1);
        rocks[3][2] = findViewById(R.id.main_mat3_2);

        rocks[4][0] = findViewById(R.id.main_mat4_0);
        rocks[4][1] = findViewById(R.id.main_mat4_1);
        rocks[4][2] = findViewById(R.id.main_mat4_2);

        rocks[5][0] = findViewById(R.id.main_mat5_0);
        rocks[5][1] = findViewById(R.id.main_mat5_1);
        rocks[5][2] = findViewById(R.id.main_mat5_2);

        leftBTN = findViewById(R.id.main_BTN_left);
        rightBTN = findViewById(R.id.main_BTN_right);
    }

    public void moveLeft(View view) {
        if(cars[1].getVisibility() == View.VISIBLE) {
            cars[1].setVisibility(View.INVISIBLE);
            cars[0].setVisibility(View.VISIBLE);
        }

        else if (cars[2].getVisibility() == View.VISIBLE) {
            cars[2].setVisibility(View.INVISIBLE);
            cars[1].setVisibility(View.VISIBLE);
        }
    }

    public void moveRight(View view) {
        if(cars[1].getVisibility() == View.VISIBLE) {
            cars[1].setVisibility(View.INVISIBLE);
            cars[2].setVisibility(View.VISIBLE);
        }

        else if (cars[0].getVisibility() == View.VISIBLE) {
            cars[0].setVisibility(View.INVISIBLE);
            cars[1].setVisibility(View.VISIBLE);
        }
    }

    public void randomRock() {
        Random rand = new Random();
        int num = rand.nextInt(3);
        if (!flag) {
            rocks[0][num].setVisibility(View.VISIBLE);
            flag = true;
        } else {
            flag = false;
        }
    }

    public void siftDown() {
        for (int i = numOfRows - 1; i > 0; i--) {
            for (int j = 0; j < numOfColumns; j++) {
                rocks[i][j].setVisibility(rocks[i-1][j].getVisibility());
                rocks[i-1][j].setVisibility(View.INVISIBLE);
            }
        }

        collision();
    }

    private void startTime() {
        if (timer == null) {
            timer = new CountDownTimer(99999999, DELAY) {
                @Override
                public void onTick(long millisUntilFinished) {
                    randomRock();
                    siftDown();
                }
                @Override
                public void onFinish() {
                    timer.cancel();
                }
            }.start();
        }
    }

    private void collision() {
        if (crushes >= life)
            gameOver();

        for (int i = 0; i < numOfColumns; i++) {
            if (cars[i].getVisibility() == View.VISIBLE && rocks[5][i].getVisibility() == View.VISIBLE) {
                hearts[life - crushes - 1].setVisibility(View.INVISIBLE);
                crushes++;
                Toast.makeText(this, "CRUSH!", Toast.LENGTH_SHORT).show();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
            }
        }
    }

    private void gameOver() {
        stopTime();
        alert.setMessage("press RACE to play again");
        alert.setCancelable(false);
        alert.setPositiveButton("RACE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });

        AlertDialog message = alert.create();
        message.setTitle("Game Over");
        message.show();
    }

    private void initGame() {
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                rocks[i][j].setVisibility(View.INVISIBLE);
            }
        }
        for (int i = 0; i < life; i++) {
            hearts[i].setVisibility(View.VISIBLE);
        }
        crushes = 0;
        timer.start();
    }

    private void stopTime() {
        timer.cancel();
    }


}