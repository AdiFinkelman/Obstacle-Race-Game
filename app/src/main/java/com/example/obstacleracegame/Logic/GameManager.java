package com.example.obstacleracegame.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.obstacleracegame.Models.MenuActivity;
import com.example.obstacleracegame.Models.Record;
import com.example.obstacleracegame.SignalGenerator;
import com.example.obstacleracegame.Utilities.MySP;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class GameManager {

    int carIndex;
    private int crashes;
    private boolean flag = false;
    private final ShapeableImageView[] cars;
    private final ShapeableImageView[][] rocks;
    private final ShapeableImageView[][] coins;
    private final ShapeableImageView[] hearts;
    private final MaterialTextView main_LBL_score;
    int score = -1;
    private final int numOfRows = DataManager.getNumOfRows();
    private final int numOfColumns = DataManager.getNumOfCols();
    private final int life = DataManager.getNumOfHearts();
    private CountDownTimer timer;
    private final Context context;
    private Record record;
    private double latitude;
    private double longitude;

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
    }

    public void moveLeft() {
        if (carIndex > 0) {
            cars[carIndex].setVisibility(View.INVISIBLE);
            cars[carIndex - 1].setVisibility(View.VISIBLE);
            carIndex--;
        }
    }

    public void moveRight() {
        if (carIndex < numOfColumns - 1) {
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
                rocks[i][j].setVisibility(rocks[i - 1][j].getVisibility());
                rocks[i - 1][j].setVisibility(View.INVISIBLE);

                coins[i][j].setVisibility(coins[i - 1][j].getVisibility());
                coins[i - 1][j].setVisibility(View.INVISIBLE);
            }
        }
        collision();
    }

    private void collision() {
        if (crashes == life)
            gameOver();

        for (int i = 0; i < numOfColumns; i++) {
            crashEvent(i);
            coinEvent(i);
        }
        score++;
        updateScoreView();
    }

    private void crashEvent(int index) {
        if (cars[index].getVisibility() == View.VISIBLE && (rocks[7][index].getVisibility() == View.VISIBLE || rocks[8][index].getVisibility() == View.VISIBLE)) {
            hearts[life - crashes - 1].setVisibility(View.INVISIBLE);
            rocks[7][index].setVisibility(View.INVISIBLE);
            crashes++;
            SignalGenerator.getInstance().toast("CRASH!", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(500);
            SignalGenerator.getInstance().sound(DataManager.getSoundCrashID());
        }
    }

    private void coinEvent(int index) {
        if (cars[index].getVisibility() == View.VISIBLE && (coins[7][index].getVisibility() == View.VISIBLE || coins[8][index].getVisibility() == View.VISIBLE)) {
            coins[7][index].setVisibility(View.INVISIBLE);
            score += 5;
            SignalGenerator.getInstance().sound(DataManager.getSoundCoinID());
        }
    }

    private void updateScoreView() {
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

    private void gameOver() {
        SignalGenerator.getInstance().toast("Game Over \uD83D\uDE23", Toast.LENGTH_SHORT);
        record = setRecordLatlng();

        if (record.getLatitude() != 0.0 || record.getLongitude() != 0.0)
            MySP.getInstance().saveToJason(getScore(), record.getLatitude(), record.getLongitude());
        else
            SignalGenerator.getInstance().toast("Last known location is null", Toast.LENGTH_SHORT);
        stopTime();
        openMenuActivity();
    }

    private Record setRecordLatlng() {
        record = new Record();
        record.setScore("" + score);
        record.setTitle("Score");

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            SignalGenerator.getInstance().toast("You should permit location access", Toast.LENGTH_SHORT);
            return null;
        }

        Location currLocation = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (currLocation == null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (currLocation == null && locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            currLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (currLocation != null) {
            latitude = currLocation.getLatitude();
            longitude = currLocation.getLongitude();
        }

        record.setLatitude(latitude);
        record.setLongitude(longitude);

        return record;
    }

    public void openMenuActivity() {
        Intent intent = new Intent(context.getApplicationContext(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void stopTime() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }
}
