package com.example.obstacleracegame.Logic;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.obstacleracegame.Models.MainActivity;
import com.example.obstacleracegame.Models.MenuActivity;
import com.example.obstacleracegame.R;
import com.example.obstacleracegame.SignalGenerator;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class GameManager {

    int carIndex;
    private int crashes;
    private boolean flag = false;
    private ShapeableImageView[] cars;
    private ShapeableImageView[][] rocks;
    private ShapeableImageView[][] coins;
    private ShapeableImageView[] hearts;
    private MaterialTextView main_LBL_score;
    int score = -1;
    private int numOfRows = DataManager.getNumOfRows();
    private int numOfColumns = DataManager.getNumOfCols();
    private int life = DataManager.getNumOfHearts();
    private CountDownTimer timer;
    public AlertDialog.Builder alert;
    private MediaPlayer mediaPlayer;
    private Context context;

    public GameManager(ShapeableImageView[] cars, ShapeableImageView[][] rocks, ShapeableImageView[][] coins, ShapeableImageView[] hearts, Context context, MaterialTextView main_LBL_score) {
        this.rocks = rocks;
        this.coins = coins;
        this.cars = cars;
        this.hearts = hearts;
        carIndex = DataManager.getCarStartPosition();
        this.crashes = 0;
        this.main_LBL_score = main_LBL_score;
        startTime();
        this.context = context;
        initMediaPlayer(context);
    }

    private void initMediaPlayer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.sound_crash);
        }
    }

    public void moveLeft(View view) {
        if(carIndex > 0) {
            cars[carIndex].setVisibility(View.INVISIBLE);
            cars[carIndex - 1].setVisibility(View.VISIBLE);
            carIndex--;
        }
    }

    public void moveRight(View view) {
        if(carIndex < numOfColumns - 1) {
            cars[carIndex].setVisibility(View.INVISIBLE);
            cars[carIndex + 1].setVisibility(View.VISIBLE);
            carIndex++;
        }
    }

    public void randomObstacle() {
        Random rand = new Random();
        int col = rand.nextInt(5);
        int type = rand.nextInt(4);
        if (!flag) {
            if (type == 3)
                coins[0][col].setVisibility(View.VISIBLE);
            else
                rocks[0][col].setVisibility(View.VISIBLE);
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

                coins[i][j].setVisibility(coins[i-1][j].getVisibility());
                coins[i-1][j].setVisibility(View.INVISIBLE);
            }
        }
        collision();
    }

    private void collision() {
        if (crashes >= life)
            gameOver();

        for (int i = 0; i < numOfColumns; i++) {
            crashEvent(i);
            coinEvent(i);
        }
        score++;
        updateScore();
    }

    private void crashEvent(int index) {
        if (cars[index].getVisibility() == View.VISIBLE && (rocks[7][index].getVisibility() == View.VISIBLE || rocks[8][index].getVisibility() == View.VISIBLE)) {
            hearts[life - crashes - 1].setVisibility(View.INVISIBLE);
            rocks[7][index].setVisibility(View.INVISIBLE);
            crashes++;
            SignalGenerator.getInstance().toast("CRASH!", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(500);
            crashSoundEvent();
        }
    }

    private void crashSoundEvent() {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, R.raw.sound_crash);
        mediaPlayer.start();
    }

    private void coinEvent(int index) {
        if (cars[index].getVisibility() == View.VISIBLE && (coins[7][index].getVisibility() == View.VISIBLE || coins[8][index].getVisibility() == View.VISIBLE)) {
            coins[7][index].setVisibility(View.INVISIBLE);
            score += 5;
            coinSoundEvent();
        }
    }

    private void coinSoundEvent() {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, R.raw.sound_coin);
        mediaPlayer.start();
    }

    private void updateScore() {
        if (score < 10)
            main_LBL_score.setText("00" + score);
        else if (score < 100)
            main_LBL_score.setText("0" + score);
        else
            main_LBL_score.setText("" + score);
    }

    public void startTime() {
        if (timer == null) {
            timer = new CountDownTimer(99999999, getDelayType()) {
                @Override
                public void onTick(long millisUntilFinished) {
                    randomObstacle();
                    siftDown();
                    getScore();
                }
                @Override
                public void onFinish() {
                    timer.cancel();
                }
            }.start();
        }
    }

    private long getDelayType() {
        return MenuActivity.getMode();
    }
    public int getScore() {
        return score;
    }

//    public void initGame() {
//        for (int i = 0; i < numOfRows; i++) {
//            for (int j = 0; j < numOfColumns; j++) {
//                rocks[i][j].setVisibility(View.INVISIBLE);
//                coins[i][j].setVisibility(View.INVISIBLE);
//            }
//        }
//        for (int i = 0; i < life; i++) {
//            hearts[i].setVisibility(View.VISIBLE);
//        }
//
//        for (int i = 0; i < numOfColumns; i++) {
//            cars[i].setVisibility(View.INVISIBLE);
//        }
//        crashes = 0;
//        carIndex = 2;
//        score = 0;
//        cars[carIndex].setVisibility(View.VISIBLE);
//
//        startTime();
//    }

    private void gameOver() {
        stopTime();
        openMenuActivity();
//        alert.setMessage("press RACE to play again");
//        alert.setCancelable(false);
//        alert.setPositiveButton("RACE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                initGame();
//            }
//        });
//
//        AlertDialog message = alert.create();
//        message.setTitle("Game Over");
//        message.show();
       }

    public void openMenuActivity() {
        Intent intent = new Intent(context.getApplicationContext(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //initGame();
        context.startActivity(intent);
    }

    public void stopTime() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
