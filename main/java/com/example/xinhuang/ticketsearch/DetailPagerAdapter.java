package com.example.xinhuang.ticketsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class DetailPagerAdapter extends FragmentPagerAdapter {


    private Boolean hasartist = false;
    private Boolean hascomming = false;
    private Boolean hasattractions = false;
    private Boolean hasdetail = false;
    private Boolean hasvenue = false;



    private EventDetail eventDetail = new EventDetail();


    public void setAttractionResult(JSONArray attractionResult) {
        this.attractionResult = attractionResult;

    }

    private JSONArray attractionResult=new JSONArray();
    private JSONArray commingResult = new JSONArray();

    private Venue venue = new Venue();

    public void setCommingResult(JSONArray commingResult) {
        this.commingResult = commingResult;
    }


    public void setEventDetail(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
    }

    public void setHasartist(Boolean hasartist) {
        this.hasartist = hasartist;
    }

    public void setHascomming(Boolean hascomming) {
        this.hascomming = hascomming;
    }

    public void setHasattractions(Boolean hasattractions) {
        this.hasattractions = hasattractions;
    }

    public void setHasdetail(Boolean hasdetail) {
        this.hasdetail = hasdetail;
    }

    public void setHasvenue(Boolean hasvenue) {
        this.hasvenue = hasvenue;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public DetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 1:
                ArtistFragment artistFragment = new ArtistFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("initialdata",attractionResult.toString());
                artistFragment.setArguments(bundle2);
                artistFragment.setArtist(hasartist);
                artistFragment.setAttractions(hasattractions);
                artistFragment.setDetail(hasdetail);
                artistFragment.setComming(hascomming);
                artistFragment.setVenue(hasvenue);
                return  artistFragment;
            case 2:
                VenueFragment venueFragment = new VenueFragment();
                venueFragment.setArtist(hasartist);
                venueFragment.setAttractions(hasattractions);
                venueFragment.setDetail(hasdetail);
                venueFragment.setComming(hascomming);
                venueFragment.setVenue(hasvenue);
                venueFragment.setVenueDetail(venue);
                return venueFragment;
            case 3:
                UpCommingFragment upCommingFragment = new UpCommingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("initialdata",commingResult.toString());
                upCommingFragment.setArguments(bundle);;
                upCommingFragment.setArtist(hasartist);
                upCommingFragment.setAttractions(hasattractions);
                upCommingFragment.setDetail(hasdetail);
                upCommingFragment.setComming(hascomming);
                upCommingFragment.setVenue(hasvenue);
                return upCommingFragment;
            default:
                DetailFragment detailFragment = new DetailFragment();
                Bundle bundledetail = new Bundle();

                bundledetail.putBoolean("hasartists",hasartist);
                bundledetail.putBoolean("hasattractions",hasattractions);
                bundledetail.putBoolean("hasdetail",hasdetail);
                bundledetail.putBoolean("hascomming",hascomming);
                bundledetail.putBoolean("hasvenue",hasvenue);
                detailFragment.setArguments(bundledetail);
                detailFragment.setEventDetail(eventDetail);
                return detailFragment;
        }


    }

    @Override
    public int getCount() {
        return 4;
    }
}
