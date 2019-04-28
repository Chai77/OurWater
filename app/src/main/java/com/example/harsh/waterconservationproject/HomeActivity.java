package com.example.harsh.waterconservationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configureProgressButton();
        configureLeaderButton();
        configureEditButton();
    }

    private void configureProgressButton(){
        Button nextButton = (Button) findViewById(R.id.progress);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
            }
        });
    }
    private void configureLeaderButton(){
        Button nextButton = (Button) findViewById(R.id.leader);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LeaderboardActivity.class));
            }
        });
    }
    private void configureEditButton(){
        Button nextButton = (Button) findViewById(R.id.edit);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LeaderboardActivity.class));
            }
        });
    }
}
