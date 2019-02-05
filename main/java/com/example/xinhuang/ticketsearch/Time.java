package com.example.xinhuang.ticketsearch;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time{
    private String date;
    private String time;

    public Time(String date, String time) {
        this.date = date;
        this.time = time;


    }

    public Time(String date) {
        this.date = date;
    }

    public Date getDate() {
        Date dates = null;

        try {
            dates = new SimpleDateFormat("yyyy-MM-dd").parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        return dates;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        String res = "";
        SimpleDateFormat origin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        try {
            res += sdf.format(origin.parse(date));

        } catch (ParseException e) {
            Log.d("timetime",e.toString());

        }
        if(time!=null&&!time.equals("null")){
            res += " "+time;
        }
        return res;
    }
}