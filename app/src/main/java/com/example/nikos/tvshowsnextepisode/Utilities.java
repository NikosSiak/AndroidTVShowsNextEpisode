package com.example.nikos.tvshowsnextepisode;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {

    public  static final String FILE_EXTENSION = ".bin";

    public static boolean saveShow(Context context, Show show){
        String fileName = String.valueOf(show.getName()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream oos;

        try{
            fos = context.openFileOutput(fileName,context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(show);
            oos.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<Show> getAllSavedShows(Context context){
        ArrayList<Show> shows = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();

        File filesDir = context.getFilesDir();

        for (String file : filesDir.list()){
            if (file.endsWith(FILE_EXTENSION)){
                files.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for (int i=0; i<files.size(); i++){
            try{
                fis = context.openFileInput(files.get(i));
                ois = new ObjectInputStream(fis);
                shows.add((Show)ois.readObject());
                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }
        return shows;
    }

    public static ArrayList<Show> getRunningShows(Context context){
        ArrayList<Show> shows = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();

        File filesDir = context.getFilesDir();

        for (String file : filesDir.list()){
            if (file.endsWith(FILE_EXTENSION)){
                files.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for (int i=0; i<files.size(); i++){
            try{
                fis = context.openFileInput(files.get(i));
                ois = new ObjectInputStream(fis);
                Show s = (Show)ois.readObject();
                if (!s.getNextEpisode().equals("null")) {
                    shows.add(s);
                }
                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }
        return shows;
    }

    public static Show getShowByName(Context context,String fileName){
        File file = new File(context.getFilesDir(),fileName);
        Show show;
        if (file.exists()){
            FileInputStream fis;
            ObjectInputStream ois;
            try{
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);
                show = (Show) ois.readObject();
                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
            return show;
        }
        return null;
    }
    public static void deleteShow(Context context,String fileName){
        File dir = context.getFilesDir();
        File file = new File(dir,fileName);
        if (file.exists()){
            file.delete();
        }
    }

    public static String createURL(String show, int i) {
        if (i == 0) {
            return "https://en.wikipedia.org/wiki/List_of_" + show.replace(" ", "_") + "_episodes";
        } else {
            return "https://en.wikipedia.org/wiki/" + show.replace(" ", "_") + "_(TV_series)";
        }
    }

    public static boolean isDate(String td){
        String[] months={"January","February","March","April","May","June","July","August",
                "September","October","November","December"};
        String arr[] = td.split(" ",2);

        String month = arr[0];
        for (int i=0;i<12;i++){
            if (month.equals(months[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean compareDates(String today, String epDate){

        if (!epDate.equals("null")) {
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                    "September", "October", "November", "December"};
            String[] t;
            String[] e;

            t = today.split(" ");
            e = epDate.split(" ");

            t[1] = t[1].substring(0, t[1].length() - 1);
            e[1] = e[1].substring(0, e[1].length() - 1);

            if (Integer.parseInt(e[2]) == Integer.parseInt(t[2])) {
                int indexEp = -1;
                int indexT = -1;
                for (int i = 0; i < 12; i++) {
                    if (months[i].equals(e[0])) {
                        indexEp = i;
                        break;
                    }
                }
                for (int i = 0; i < 12; i++) {
                    if (months[i].equals(t[0])) {
                        indexT = i;
                        break;
                    }
                }
                if (indexEp > indexT) {
                    return true;
                } else if (indexEp == indexT) {
                    if (Integer.parseInt(e[1]) >= Integer.parseInt(t[1])) {
                        return true;
                    }
                }
            } else if (Integer.parseInt(e[2]) > Integer.parseInt(t[2])) {
                return true;
            }
        }
        return false;
    }

    public static String addADay(String day) {
        String[] arr = day.split(" ");
        arr[1] = arr[1].substring(0, arr[1].length() - 1);

        if (arr[0].equals("December") && arr[1].equals("31")){
            arr[0] = "January ";
            arr[1] = "1, ";
            arr[2] = String.valueOf(Integer.parseInt(arr[2]) + 1);
            StringBuilder builder = new StringBuilder();
            for (String s : arr){
                builder.append(s);
            }
            return builder.toString();
        }
        if (Integer.parseInt(arr[1]) < 28) {
            arr[0] = arr[0] + " ";
            arr[1] = String.valueOf(Integer.parseInt(arr[1]) + 1) + ", ";
            StringBuilder builder = new StringBuilder();
            for (String s : arr){
                builder.append(s);
            }
            return builder.toString();
        } else if (arr[0].equals("February")) {
            int febDays = 28;
            int year = Integer.parseInt(arr[2]);
            if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
                febDays = 29;
            }
            if (Integer.parseInt(arr[1]) == febDays) {
                arr[1] = "1, ";
                arr[0] = "March ";
            } else {
                arr[0] = arr[0] + " ";
                arr[1] = "29, ";
            }
            StringBuilder builder = new StringBuilder();
            for (String s : arr){
                builder.append(s);
            }
            return builder.toString();
        }else {
            if (Integer.parseInt(arr[1]) < 30) {
                arr[0] = arr[0] + " ";
                arr[1] = String.valueOf(Integer.parseInt(arr[1]) + 1) + ", ";
                StringBuilder builder = new StringBuilder();
                for (String s : arr){
                    builder.append(s);
                }
                return builder.toString();
            } else if (arr[0].equals("January") || arr[0].equals("March") || arr[0].equals("May") || arr[0].equals("July")
                    || arr[0].equals("August") || arr[0].equals("October") || arr[0].equals("December")) {
                if (Integer.parseInt(arr[1]) == 30) {
                    arr[0] = arr[0] + " ";
                    arr[1] = String.valueOf(Integer.parseInt(arr[1]) + 1) + ", ";
                    StringBuilder builder = new StringBuilder();
                    for (String s : arr){
                        builder.append(s);
                    }
                    return builder.toString();
                }else {
                    String[] months={"January","February","March","April","May","June","July","August",
                            "September","October","November","December"};
                    int index = -1;
                    for (int i=0;i<12;i++){
                        if (months[i].equals(arr[0])){
                            index = i;
                            break;
                        }
                    }
                    arr[1] = "1, ";
                    arr[0] = months[index+1] + " ";
                    StringBuilder builder = new StringBuilder();
                    for (String s : arr){
                        builder.append(s);
                    }
                    return builder.toString();
                }
            }else{
                String[] months={"January","February","March","April","May","June","July","August",
                        "September","October","November","December"};
                int index = -1;
                for (int i=0;i<12;i++){
                    if (months[i].equals(arr[0])){
                        index = i;
                        break;
                    }
                }
                arr[1] = "1, ";
                arr[0] = months[index+1] + " ";
                StringBuilder builder = new StringBuilder();
                for (String s : arr){
                    builder.append(s);
                }
                return builder.toString();
            }
        }
    }
}
