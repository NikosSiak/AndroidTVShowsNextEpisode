package com.example.nikos.tvshowsnextepisode;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//public class wiki extends AppCompatActivity implements TaskCompleted{
public class wiki extends AppCompatActivity {

    /*@Override
    public void onTaskCompleted(int count,int size,Context context){
        if (count == size){
            Toast.makeText(context,"Finished",Toast.LENGTH_SHORT).show();
        }
    }*/

    public static class wikiSearch extends AsyncTask<Void, Void, Void> {
        Document doc;
        int err = 0;
        String name;
        String link;
        ArrayList<Element> tables;
        ArrayList<Element> tds;
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String t = df.format(d);
        String ep="null";
        Context context;
        //private TaskCompleted mCallback = null;
        //int count;
        //int size;
        boolean skip;

        public wikiSearch(Show show, Context c, boolean skipToday) {
            super();
            name = show.getName();
            link = show.getLink();
            context = c;
            skip = skipToday;
        }

        /*public wikiSearch(Show show, Context c, boolean skipToday, Context taskCompletedContext,int count,int size) {
            super();
            name = show.getName();
            link = show.getLink();
            context = c;
            skip = skipToday;
            this.mCallback = (TaskCompleted) taskCompletedContext;
            this.count = count;
            this.size = size;
        }*/
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (link.length() == 0)
                    link = Utilities.createURL(name, 0);
                doc = Jsoup.connect(link).get();
                tables = doc.select("table[class=wikitable plainrowheaders wikiepisodetable]");
                for (int i=0;i<tables.size();i++){
                    tds = tables.get(i).select("td");
                    for (int j=0;j<tds.size();j++){
                        String td = tds.get(j).toString().substring(4);
                        String arr[] = td.split("<",2);
                        String arr2[] = arr[0].split(">",2);
                        td = arr2[1];
                        td = td.replaceAll("&nbsp;"," ");
                        if (Utilities.isDate(td)){
                            td = Utilities.addADay(td);
                            try{
                                if (Utilities.compareDates(t,td)){
                                    ep = new String(td);
                                    if (skip && ep.equals(t)){
                                        continue;
                                    }else {
                                        return null;
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (link.length() == 0)
                        link = Utilities.createURL(name, 1);
                    doc = Jsoup.connect(link).get();
                    tables = doc.select("table[class=wikitable plainrowheaders wikiepisodetable]");
                    for (int i=0;i<tables.size();i++){
                        tds = tables.get(i).select("td");
                        for (int j=0;j<tds.size();j++){
                            String td = tds.get(j).toString().substring(4);
                            String arr[] = td.split("<",2);
                            String arr2[] = arr[0].split(">",2);
                            td = arr2[1];
                            td = td.replaceAll("&nbsp;"," ");
                            if (Utilities.isDate(td)){
                                td = Utilities.addADay(td);
                                try{
                                    if (Utilities.compareDates(t,td)){
                                        ep = new String(td);
                                        if (skip && ep.equals(t)){
                                            continue;
                                        }else {
                                            return null;
                                        }
                                    }
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    err = 1;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            if (err == 0) {
                if (!databaseHelper.addData(link,name,ep)) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"Synced " + name,Toast.LENGTH_SHORT).show();
                }
            }
            if (err == 1) {
                Toast.makeText(context, "Invalid Show " + name, Toast.LENGTH_SHORT).show();
            }
            //if (mCallback != null){
              //  mCallback.onTaskCompleted(count,size,context);
            //}
        }
    }
}
