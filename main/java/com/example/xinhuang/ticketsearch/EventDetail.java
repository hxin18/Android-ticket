package com.example.xinhuang.ticketsearch;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDetail {

    private JSONArray attractions;

    public JSONArray getAttractions() {
        return attractions;
    }

    public void setAttractions(JSONArray attractions) {
        this.attractions = attractions;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public JSONArray getCategory() {
        return category;
    }

    public void setCategory(JSONArray category) {
        this.category = category;
    }

    public JSONArray getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(JSONArray priceRange) {
        this.priceRange = priceRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlSeat() {
        return urlSeat;
    }

    public void setUrlSeat(String urlSeat) {
        this.urlSeat = urlSeat;
    }

    private String venue;
    private Time time;
    private JSONArray category;
    private JSONArray priceRange;
    private String status;
    private String url;
    private String urlSeat;

    public EventDetail() {
    }






}
