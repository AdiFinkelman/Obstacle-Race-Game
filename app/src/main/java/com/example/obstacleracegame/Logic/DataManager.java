package com.example.obstacleracegame.Logic;

import com.example.obstacleracegame.R;

public class DataManager {

    private static final int ROWS = 9;
    private static final int COLS = 5;
    private static final int HEARTS_NUM = 3;
    private static final int CAR_START_POSITION = COLS / 2;

    private static int[][] rocksID = {
            {R.id.main_mat0_0, R.id.main_mat0_1, R.id.main_mat0_2, R.id.main_mat0_3, R.id.main_mat0_4},
            {R.id.main_mat1_0, R.id.main_mat1_1, R.id.main_mat1_2, R.id.main_mat1_3, R.id.main_mat1_4},
            {R.id.main_mat2_0, R.id.main_mat2_1, R.id.main_mat2_2, R.id.main_mat2_3, R.id.main_mat2_4},
            {R.id.main_mat3_0, R.id.main_mat3_1, R.id.main_mat3_2, R.id.main_mat3_3, R.id.main_mat3_4},
            {R.id.main_mat4_0, R.id.main_mat4_1, R.id.main_mat4_2, R.id.main_mat4_3, R.id.main_mat4_4},
            {R.id.main_mat5_0, R.id.main_mat5_1, R.id.main_mat5_2, R.id.main_mat5_3, R.id.main_mat5_4},
            {R.id.main_mat6_0, R.id.main_mat6_1, R.id.main_mat6_2, R.id.main_mat6_3, R.id.main_mat6_4},
            {R.id.main_mat7_0, R.id.main_mat7_1, R.id.main_mat7_2, R.id.main_mat7_3, R.id.main_mat7_4},
            {R.id.main_mat8_0, R.id.main_mat8_1, R.id.main_mat8_2, R.id.main_mat8_3, R.id.main_mat8_4}
    };

    private static int[][] coinsID = {
            {R.id.main_coin0_0, R.id.main_coin0_1, R.id.main_coin0_2, R.id.main_coin0_3, R.id.main_coin0_4},
            {R.id.main_coin1_0, R.id.main_coin1_1, R.id.main_coin1_2, R.id.main_coin1_3, R.id.main_coin1_4},
            {R.id.main_coin2_0, R.id.main_coin2_1, R.id.main_coin2_2, R.id.main_coin2_3, R.id.main_coin2_4},
            {R.id.main_coin3_0, R.id.main_coin3_1, R.id.main_coin3_2, R.id.main_coin3_3, R.id.main_coin3_4},
            {R.id.main_coin4_0, R.id.main_coin4_1, R.id.main_coin4_2, R.id.main_coin4_3, R.id.main_coin4_4},
            {R.id.main_coin5_0, R.id.main_coin5_1, R.id.main_coin5_2, R.id.main_coin5_3, R.id.main_coin5_4},
            {R.id.main_coin6_0, R.id.main_coin6_1, R.id.main_coin6_2, R.id.main_coin6_3, R.id.main_coin6_4},
            {R.id.main_coin7_0, R.id.main_coin7_1, R.id.main_coin7_2, R.id.main_coin7_3, R.id.main_coin7_4},
            {R.id.main_coin8_0, R.id.main_coin8_1, R.id.main_coin8_2, R.id.main_coin8_3, R.id.main_coin8_4}
    };

    private static int[] heartsID = {
            R.id.main_IMG_heart1, R.id.main_IMG_heart2, R.id.main_IMG_heart3
    };

    private static int[] carsID = {
            R.id.main_IMG_car0, R.id.main_IMG_car1, R.id.main_IMG_car2, R.id.main_IMG_car3, R.id.main_IMG_car4
    };
    private static int leftBTNID = R.id.main_BTN_left;
    private static int rightBTNID = R.id.main_BTN_right;
    private static int fastButtonID = R.id.menu_BTN_fastGame;
    private static int slowButtonID = R.id.menu_BTN_slowGame;
    private static int sensorButtonID = R.id.menu_BTN_SensorGame;
    private static int recordsButtonID = R.id.menu_BTN_Records;
    private static int soundCrashID = R.raw.sound_crash;
    private static int soundCoinID = R.raw.sound_coin;
    private static final float DEFAULT_ZOOM = 12.0f;

    public static int getRocksID(int rowIndex, int colIndex) {
        return rocksID[rowIndex][colIndex];
    }

    public static int getCoinsID(int rowIndex, int colIndex) {
        return coinsID[rowIndex][colIndex];
    }

    public static int getHeartsID(int index) {
        return heartsID[index];
    }

    public static int getCarsID(int index) {
        return carsID[index];
    }

    public static int getFastId() {
        return fastButtonID;
    }

    public static int getSlowId() {
        return slowButtonID;
    }

    public static int getSensorButtonID() {
        return sensorButtonID;
    }

    public static int getRecordsID() {
        return recordsButtonID;
    }

    public static int getNumOfRows() {
        return ROWS;
    }

    public static int getNumOfCols() {
        return COLS;
    }

    public static int getNumOfHearts() {
        return HEARTS_NUM;
    }

    public static int getCarStartPosition() {
        return CAR_START_POSITION;
    }

    public static int getLeftBTNID() {
        return leftBTNID;
    }

    public static int getRightBTNID() {
        return rightBTNID;
    }

    public static int getSoundCrashID() {
        return soundCrashID;
    }

    public static int getSoundCoinID() {
        return soundCoinID;
    }

    public static float getDefaultZoom() {
        return DEFAULT_ZOOM;
    }
}
