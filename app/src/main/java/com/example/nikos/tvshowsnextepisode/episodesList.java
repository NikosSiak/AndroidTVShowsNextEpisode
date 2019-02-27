package com.example.nikos.tvshowsnextepisode;

import android.content.DialogInterface;
import android.content.Intent;
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

        ArrayList<Show> shows = Utilities.getRunningShows(this);

        if (shows == null || shows.size() == 0) {
            Toast.makeText(this, "You have no new episodes to watch!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Collections.sort(shows,new Comparator<Show>() {
                @Override
                public int compare(Show show1, Show show2) {
                    if(Utilities.compareDates(show1.getNextEpisode(),show2.getNextEpisode())){
                        return -1;
                    }
                    else{
                        return 1;
                    }
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
                //int count = 0;
                ArrayList<Show> shows = Utilities.getAllSavedShows(getApplicationContext());
                if (shows != null && shows.size()!=0){
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                    String t = df.format(d);
                    //episodesList.setVisibility(View.INVISIBLE);
                    //progressBar.setVisibility(View.VISIBLE);
                    //wiki.wikiSearch ws = new wiki.wikiSearch(new Show(), getApplicationContext(), false);
                    for (Show s : shows){
                        //if (!Utilities.compareDates(t,s.getNextEpisode()) && !s.getNextEpisode().equals(t)) {
                            //ws = new wiki.wikiSearch(s, getApplicationContext(), false);
                            //ws.execute();
                            //count++;
                            new wiki.wikiSearch(s, getApplicationContext(), false).execute();
                        //}
                    }
                    //if (shows.size()!=0) {
                      //  while (ws.getStatus() != AsyncTask.Status.FINISHED) {
                        //}
                    //}
                    //progressBar.setVisibility(View.GONE);
                    //episodesList.setVisibility(View.VISIBLE);
                }
        }
        return true;
    }
}
