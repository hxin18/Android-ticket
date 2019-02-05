package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private ArrayList<Event> favoriteEvent = new ArrayList<>();
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONObject favourite;
    private Context context;

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key, "default_value_string");

            try {
                favourite = new JSONObject(value);
                favoriteEvent.clear();
                Iterator<String> keys = favourite.keys();
                while(keys.hasNext()) {
                    String k = keys.next();
                    if (favourite.get(k) instanceof JSONObject) {
                        JSONObject favEvent = (JSONObject) favourite.get(k);
                        favoriteEvent.add(new Event(favEvent.getString("name"),
                                favEvent.getString("id"),
                                favEvent.getString("genre"),
                                favEvent.getString("type"),
                                favEvent.getString("date"),
                                favEvent.getString("venue"),
                                true));
                    }
                }
                Log.d("favcachemain",favoriteEvent.toString());
                mSectionsPageAdapter.setFavoriteEvent(favoriteEvent);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");



        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(),favoriteEvent);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        favourite = new JSONObject();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        context = this;
        favoritesSharedPreferences = context.getSharedPreferences("userfavourite", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        favoritesEditor.putString("favouriteEvent", new JSONObject().toString());

        // save changes
        favoritesEditor.apply();
        favoritesSharedPreferences.registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);

    }

    private void setupViewPager(ViewPager viewPager) {

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(),favoriteEvent);

        viewPager.setAdapter(mSectionsPageAdapter);

    }


}
