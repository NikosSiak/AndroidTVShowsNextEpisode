package com.example.nikos.tvshowsnextepisode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button showEpisodesList;
    Button myShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showEpisodesList = (Button)findViewById(R.id.episodesButton);
        myShows = (Button)findViewById(R.id.showsButton);

        showEpisodesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,episodesList.class));
            }
        });

        myShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,showsList.class));
            }
        });
    }
}
