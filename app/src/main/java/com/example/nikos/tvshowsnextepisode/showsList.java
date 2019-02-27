package com.example.nikos.tvshowsnextepisode;

import android.app.Application;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class showsList extends AppCompatActivity {

    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_list);
        listView = (ListView) findViewById(R.id.listView);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefreshshows);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_show:
                addShow();
        }
        return true;
    }

    public void addShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Show");

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(this);
        final EditText link = new EditText(this);

        name.setHint("Name");
        link.setHint("Link");
        name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        link.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        layout.addView(name);
        layout.addView(link);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = name.getText().toString();
                String newLink = link.getText().toString();
                if (newName.length() != 0) {
                    Show show = new Show(newName,newLink);
                    new wiki.wikiSearch(show,getApplicationContext(),false).execute();
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(null);

        ArrayList<Show> shows = Utilities.getAllSavedShows(this);

        if (shows == null || shows.size() == 0) {
            Toast.makeText(this, "You have no saved shows!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Collections.sort(shows, new Comparator<Show>() {
                @Override
                public int compare(Show show1, Show show2) {
                    return show1.getName().compareTo(show2.getName());
                }
            });
            showAdapter na = new showAdapter(this, R.layout.item_show, shows);
            listView.setAdapter(na);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Show show = (Show) listView.getItemAtPosition(i);
                    removeShow(show);
                    return false;
                }
            });
        }


    }

    public void removeShow(Show show){
        final String fileName = show.getName() + Utilities.FILE_EXTENSION;
        AlertDialog.Builder dialog =  new AlertDialog.Builder(this)
                .setTitle("Remove")
                .setMessage("You are about to remove " + show.getName() + ", are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.deleteShow(getApplicationContext(),fileName);
                        Toast.makeText(getApplicationContext(), "show is removed", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("no",null);
        dialog.show();
    }
}