package com.example.obstacleracegame.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obstacleracegame.Interfaces.Map_Callback;
import com.example.obstacleracegame.Logic.DataManager;
import com.example.obstacleracegame.Models.MenuActivity;
import com.example.obstacleracegame.R;
import com.google.android.gms.maps.model.LatLng;

public class FragmentActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;

    Map_Callback map_callback = new Map_Callback() {
        @Override
        public void recordClick(double latitude, double longitude) {
            mapFragment.moveCamera(new LatLng(latitude, longitude), DataManager.getDefaultZoom());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_records);

        initFragments();
        listFragment.setMap_callback(map_callback);
        beginTransactions();
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }

    private void initFragments() {
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
    }

    public void openRecordActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openRecordActivity();
    }
}
