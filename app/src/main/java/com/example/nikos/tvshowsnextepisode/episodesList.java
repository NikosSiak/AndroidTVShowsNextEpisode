package com.example.nikos.tvshowsnextepisode;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class episodesList extends AppCompatActivity{

    private ListView episodesList;
    private SwipeRefreshLayout swipeRefresh;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_list);

        episodesList = (ListView)findViewById(R.id.nextEpisodeList);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefreshepisodes);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                finish();
                startActivity(getIntent());
            }
        });

        Cursor data = databaseHelper.getData();
        ArrayList<Show> shows = new ArrayList<>();
        while(data.moveToNext()){
            if (!data.getString(2).equals("null")) {
                shows.add(new Show(data.getString(1), data.getString(0), data.getString(2)));
            }
        }

        if (shows.size() == 0) {
            Toast.makeText(this, "You have no new episodes to watch!", Toast.LENGTH_SHORT).show();
        } else {
            Collections.sort(shows,new Comparator<Show>() {
                @Override
                public int compare(Show show1, Show show2) {
                    if (show1.getNextEpisode().equals(show2.getNextEpisode())) {
                        return 0;
                    }
                    if(Utilities.compareDates(show1.getNextEpisode(),show2.getNextEpisode())){
                        return -1;
                    }
                    return 1;
                }
            });
            showAdapter na = new showAdapter(this, R.layout.item_show, shows);
            episodesList.setAdapter(na);

            episodesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final Show show = (Show) episodesList.getItemAtPosition(i);
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                    String t = df.format(d);
                    if (show.getNextEpisode().equals(t) || !Utilities.compareDates(t,show.getNextEpisode())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(episodesList.this);
                        builder.setTitle("Find next episode?");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new wiki.wikiSearch(show,getApplicationContext(),true).execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find_new_episodes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.find_new_episodes:
                Cursor data = databaseHelper.getData();
                ArrayList<Show> shows = new ArrayList<>();
                while(data.moveToNext()){
                    shows.add(new Show(data.getString(1), data.getString(0), data.getString(2)));
                }
                if (shows.size()!=0){
                    for (Show s : shows){
                            new wiki.wikiSearch(s, getApplicationContext(), false).execute();
                    }
                }
        }
        return true;
    }
}
