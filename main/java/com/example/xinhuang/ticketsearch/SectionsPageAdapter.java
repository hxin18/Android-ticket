package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by User on 2/28/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();

//    public void addFragment(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }

    private String tabTitles[] = new String[]{"Search", "Favourite"};

    public void setFavoriteEvent(ArrayList<Event> favoriteEvent) {
        this.favoriteEvent = favoriteEvent;
    }

    private ArrayList<Event> favoriteEvent = new ArrayList<>();



    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public SectionsPageAdapter(FragmentManager fm,ArrayList<Event>  favoriteEvent) {
        super(fm);
        this.favoriteEvent = favoriteEvent;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                Tab1Fragment tab1Fragment = new Tab1Fragment();
                return  tab1Fragment;
            default:
                Tab2Fragment tab2Fragment = new Tab2Fragment();
                Log.d("favcache",favoriteEvent.toString());
                tab2Fragment.setFavoriteEvent(favoriteEvent);
                return  tab2Fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
