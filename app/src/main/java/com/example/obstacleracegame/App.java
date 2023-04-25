package com.example.obstacleracegame;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SignalGenerator.init(this);
    }

}
