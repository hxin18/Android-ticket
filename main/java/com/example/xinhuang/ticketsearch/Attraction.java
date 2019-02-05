package com.example.xinhuang.ticketsearch;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Attraction {

    private boolean music;
    private String name;
    private String follower;
    private String url;
    private String popularity;
    private String pics;


    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPics() { return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public JSONObject toJsonObject(){
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("music",music);
            jobj.put("name",name);
            jobj.put("follower",follower);
            jobj.put("url",url);
            jobj.put("popularity",popularity);
            jobj.put("pics",pics);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jobj;

    }

    @Override
    public String toString() {
        return "{" +
                "music:" + music +
                ", name:\"" + name + '\"' +
                ", follower:'" + follower + '\'' +
                ", url:'" + url + '\'' +
                ", popularity:'" + popularity + '\'' +
                ", pics:" + pics +
                '}';
    }
}
