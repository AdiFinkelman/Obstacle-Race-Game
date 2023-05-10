package com.example.obstacleracegame.Logic;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.obstacleracegame.Fragments.MapFragment;
import com.example.obstacleracegame.Interfaces.LocationCallback;
import com.example.obstacleracegame.Models.MenuActivity;
import com.example.obstacleracegame.Models.RecordsList;
import com.example.obstacleracegame.SignalGenerator;
import com.example.obstacleracegame.Utilities.MySP;
import com.example.obstacleracegame.Utilities.StepDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
    //    public AlertDialog.Builder alert;
    private Context context;
    private RecordsList recordList;
    private StepDetector stepDetector;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private MapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

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
        this.recordList = new RecordsList();
    }

    public void moveLeft(View view) {
        if (carIndex > 0) {
            cars[carIndex].setVisibility(View.INVISIBLE);
            cars[carIndex - 1].setVisibility(View.VISIBLE);
            carIndex--;
        }
    }

    public void moveRight(View view) {
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
        if (crashes >= life)
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
        //Log.d("CURRENT LOCATION", mapFragment.currentLocation.getLatitude() + "");
        Log.d("SAVE TO JASON", getScore() + "");
        MySP.getInstance().saveToJason(getScore(), mapFragment.getLat(), mapFragment.getLon());
        stopTime();
        // stopLocationUpdates();
        openMenuActivity();
    }

//    public void getDeviceLocation() {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
//
//        try {
//            if (mapFragment.locationPermissionGranted) {
//                Task location = fusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: success");
//                            mapFragment.currentLocation = (Location) task.getResult();
//                            Log.d("LATITUDE", "" + currentLocation.getLatitude());
//                            setLat(currentLocation.getLatitude());
//                            setLon(currentLocation.getLongitude());
//                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            gMap.addMarker(new MarkerOptions()
//                                    .position(latLng));
//                            moveCamera(latLng, DEFAULT_ZOOM);
//                            //        gMap.animateCamera(CameraUpdateFactory.zoomTo(0.0f));
//
//                        } else {
//                            Log.d(TAG, "onComplete: location is null");
//                            SignalGenerator.getInstance().toast("Cant get location", Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
//            }
//
//        } catch (SecurityException e) {
//            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
//        }
//    }

//    private void stopLocationUpdates() {
//        fusedLocationProviderClient.removeLocationUpdates((com.google.android.gms.location.LocationCallback) locationCallback);
//    }

//    public void startLocationUpdates() {
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//    }

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
