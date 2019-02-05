package com.example.xinhuang.ticketsearch;

import org.json.JSONObject;

public class Venue {

    private String name;
    private String address;
    private String city;
    private String phoneNumer;
    private String openHour;
    private String generalRule ;
    private String childRule;


    private JSONObject location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public JSONObject getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location;
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumer() {
        return phoneNumer;
    }

    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumer='" + phoneNumer + '\'' +
                ", openHour='" + openHour + '\'' +
                ", generalRule='" + generalRule + '\'' +
                ", childRule='" + childRule + '\'' +
                ", location=" + location +
                '}';
    }
}
