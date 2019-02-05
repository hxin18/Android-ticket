package com.example.xinhuang.ticketsearch;

public class Event {

    private String name;
    private String id;
    private String genre;
    private String type;
    private String date;
    private String venue;
    private  boolean isFavorite;


    public Event(String name, String id, String genre, String type, String date, String venue, boolean isFavorite) {
        this.name = name;
        this.id = id;
        this.genre = genre;
        this.type = type;
        this.date = date;
        this.venue = venue;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "{" +
                "name:\"" + name + '\"' +
                ", id:'" + id + '\'' +
                ", genre:'" + genre + '\'' +
                ", type:'" + type + '\'' +
                ", date:'" + date + '\'' +
                ", venue:'" + venue + '\'' +
                ", isFavorite:" + isFavorite +
                '}';
    }
}
