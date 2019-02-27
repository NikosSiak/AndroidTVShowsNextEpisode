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
