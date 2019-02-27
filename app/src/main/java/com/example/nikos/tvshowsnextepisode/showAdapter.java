package com.example.nikos.tvshowsnextepisode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class showAdapter extends ArrayAdapter<Show> {

    public showAdapter(Context context, int resource, ArrayList<Show> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_show,null);
        }
        Show show = getItem(position);

        if(show != null){
            TextView name = (TextView)  convertView.findViewById(R.id.list_show_name);
            TextView nextEpisode = (TextView)  convertView.findViewById(R.id.list_show_nextEpisode);

            Date d = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            String t = df.format(d);

            name.setText(show.getName());
            if (show.getNextEpisode().equals("null")){
                nextEpisode.setText("");
            }else if (show.getNextEpisode().equals(t)){
                nextEpisode.setText("Today");
            }else if (show.getNextEpisode().equals(Utilities.addADay(t))){
                nextEpisode.setText("Tomorrow");
            }else if (!Utilities.compareDates(t,show.getNextEpisode())){
                nextEpisode.setText("Older Date");
            }else {
                nextEpisode.setText(show.getNextEpisode());
            }
        }
        return convertView;
    }
}
