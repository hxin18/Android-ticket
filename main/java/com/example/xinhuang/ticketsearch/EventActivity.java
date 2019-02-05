package com.example.xinhuang.ticketsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class EventActivity extends AppCompatActivity {

    private DetailPagerAdapter detailPagerAdapter;
    private ViewPager viewPager;

    private Menu menu;
    private String name;
    private String id;
    private String type;
    private JSONArray attraction;
    private String venue;
    private JSONObject attraction_info;
    private JSONObject artist_info;
    private Boolean receive;
    private static int hash;
    private  int hashcode;


    private  EventDetail eventDetails;
    private  Venue venueDetail;

    private ArrayList<Attraction> attractionList;



    private SharedPreferences dataSharedPreference;
    private android.content.SharedPreferences.Editor dataEditor;

    private ArrayList<UpComming> commingResult;


    private SharedPreferences transmissionSharedPreference;
    private android.content.SharedPreferences.Editor transmissionEditor;

    private SharedPreferences artistSharedPreference;
    private android.content.SharedPreferences.Editor artistEditor;

    private SharedPreferences favoritesSharedPreferences;
    private android.content.SharedPreferences.Editor favoritesEditor;
    private JSONObject favourite;
    private JSONObject data;
    private static final String API_BASE_URL = "http://hxserverhw8.us-west-1.elasticbeanstalk.com";


    private SharedPreferences requestSharedPreference;
    private android.content.SharedPreferences.Editor requestEditor;

    private ActionBar actionBar;




    // twitter
    private final static String twitterHashtags = "CSCI571 EventSearch";
    private String twitterText;

    public void setTwitterText() {
        twitterText = "Check out " + name + " located at " + eventDetails.getVenue() + ". Website: " + eventDetails.getUrl();
    }

    public void openTweetLink() {
        String twitterLink = "https://twitter.com/intent/tweet?text=" + twitterText + "&hashtags=" + twitterHashtags;
        Uri uri = Uri.parse(twitterLink);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private  SharedPreferences.OnSharedPreferenceChangeListener artistChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(hash != hashcode)
                return;
            Boolean value = sharedPreferences.getBoolean(key,false);

                Boolean found = false;
                for (int i = 0; i < attraction.length(); i++) {
                    try {
                        if (attraction.get(i).toString().equals(key)) {
                            found = true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (found && value) {
                    try {
                        artist_info.put(key, found);
//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (artist_info.length() == attraction.length() && artist_info.length() > 0) {

                    transmissionEditor.putBoolean("artist", true);
                    transmissionEditor.apply();
                    JSONArray tosend = new JSONArray();
                    for (int i = 0; i < attractionList.size(); i++) {
                        tosend.put(attractionList.get(i).toJsonObject());

                    }

                    detailPagerAdapter.setAttractionResult(tosend);
                    dataEditor.putString("attraction", tosend.toString());
                    dataEditor.apply();

                } else {
                    transmissionEditor.putBoolean("artist", false);
                    transmissionEditor.apply();

                }
            }

    };

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(hash != hashcode)
                return;
           Boolean value = sharedPreferences.getBoolean(key,false);
           Boolean found = false;

           for(int i = 0; i<attraction.length();i++){
               try {
                   if(attraction.get(i).toString().equals(key)){
                       found=true;
                       break;
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
           if(found&&value){
               try {
                   attraction_info.put(key,found);
//
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

           if(key.equals("detail")){
               Log.d("detail_s",String.valueOf(hash )+" "+String.valueOf(hashcode));
               if(value){
                   Log.d("detail_s",String.valueOf(value));
                   recvVenue();
                   recvAttraction();
                   recvUpcomming();
                   if(type.equals("Music")){
                        recvArtist();
                   }
                    else{
                       detailPagerAdapter.setHasartist(true);
                       transmissionEditor.putBoolean("artist",true);
                       transmissionEditor.apply();
                       JSONArray tosend = new JSONArray();
                       for(int i = 0;i<attractionList.size();i++){
                           tosend.put(attractionList.get(i).toJsonObject());
                       }
                       detailPagerAdapter.setAttractionResult(tosend);
                       dataEditor.putString("attraction",tosend.toString());
                       dataEditor.apply();
                    }
                   transmissionEditor.putBoolean("detail",true);
                   transmissionEditor.apply();
               }
               else{
                   transmissionEditor.putBoolean("detail",false);
                   transmissionEditor.apply();
               }
           }

            if(key.equals("venue")){
               if(value){
                   transmissionEditor.putBoolean("venue",true);
                   transmissionEditor.apply();
               }
               else{
                   transmissionEditor.putBoolean("venue",false);
                   transmissionEditor.apply();
               }


            }


            if(key.equals("comming")){
                if(value){
                    transmissionEditor.putBoolean("comming",true);
                    transmissionEditor.apply();
                    JSONArray tosend = new JSONArray();
                    for(int i = 0;i<attractionList.size();i++){
                        tosend.put(attractionList.get(i).toJsonObject());
                    }
                    detailPagerAdapter.setAttractionResult(tosend);
                    dataEditor.putString("attraction",tosend.toString());
                    dataEditor.apply();
                }
                else{
                    transmissionEditor.putBoolean("comming",false);
                    transmissionEditor.apply();
                }

            }


            if(attraction_info.length()==attraction.length()&&attraction_info.length()>0){

                transmissionEditor.putBoolean("attraction",true);
                transmissionEditor.apply();

            }
            else{
                transmissionEditor.putBoolean("attraction",false);
                transmissionEditor.apply();

            }

        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        setTitle(name);
        getMenuInflater().inflate(R.menu.details_menu, menu);
        favoritesSharedPreferences = getSharedPreferences("userfavourite", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();


        try {
            favourite = new JSONObject(favoritesSharedPreferences.getString("favouriteEvent", ""));
        } catch (Exception e) {
            e.printStackTrace();
            favourite = new JSONObject();
        }



        if (favourite.has(this.id)) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.heart_fill_red);
        } else {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.heart_fill_white);
        }

        return true;
    }

    public void AddorDelFav(){
        if (favourite.has(this.id)) {
            favourite.remove(this.id);
            Toast.makeText(this,  name+" was remove from favourites", Toast.LENGTH_LONG).show();
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.heart_fill_white);
        } else {
            try {
                favourite.put(this.id,this.data);
            }
            catch (JSONException e){

            }
            Toast.makeText(this,  name+" was added to favourites", Toast.LENGTH_LONG).show();
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.heart_fill_red);
        }
        favoritesEditor.putString("favouriteEvent", favourite.toString());

        // save changes
        favoritesEditor.apply();

    }

    private void recvVenue(){
        if(hash != hashcode)
            return;


        Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath("venue")
                .appendQueryParameter("name", venue)
                .build();
        // Instantiate the RequestQueue.
        requestEditor.putBoolean("venue",false);
        requestEditor.apply();
        dataEditor.putString("venue","{}");
        dataEditor.apply();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject eventDetail;
                try{
                    eventDetail = new JSONObject(response);

                    requestEditor.putBoolean("venue",true);
                    requestEditor.apply();

                    dataEditor.putString("venue",response);
                    dataEditor.apply();

                    JSONObject eventD = new JSONObject(response);
                    if(eventD.has("name")){
                        venueDetail.setName(eventD.getString("name"));
                    }
                    if(eventD.has("address")){
                        venueDetail.setAddress(eventD.getString("address"));
                    }
                    if(eventD.has("phone")){
                        venueDetail.setPhoneNumer(eventD.getString("phone"));
                    }
                    if(eventD.has("openhour")){
                        venueDetail.setOpenHour(eventD.getString("openhour"));
                    }
                    if(eventD.has("generalrule")){
                        venueDetail.setGeneralRule(eventD.getString("generalrule"));
                    }
                    if(eventD.has("childrule")){
                        venueDetail.setChildRule(eventD.getString("childrule"));
                    }
                    if(eventD.has("state_city")){
                        venueDetail.setCity(eventD.getString("state_city"));
                    }
                    if(eventD.has("location")){

                        venueDetail.setLocation(eventD.getJSONObject("location"));
                    }
                    detailPagerAdapter.setVenue(venueDetail);

                }
                catch (JSONException e){
                    dataEditor.putString("venue","{}");
                    dataEditor.apply();

                    eventDetail = new JSONObject();
                }

            detailPagerAdapter.setHasvenue(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);


    }
    private void recvAttraction(){
        if(hash != hashcode)
            return;

        attraction_info = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);
        try{
            for(int i = 0; i < attraction.length();i++){
                Log.d("search_attraction", attraction.get(i).toString()+" "+String.valueOf(this.hashCode())+" "+" "+String.valueOf(hash));
                String artist = attraction.get(i).toString();
                Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                        .appendPath("pic")
                        .appendQueryParameter("name", artist)
                        .build();
                Attraction temp = attractionList.get(i);
                requestEditor.putBoolean(artist,false);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject eventDetail;
                        try{
                            eventDetail = new JSONObject(response);
                            temp.setPics(response);
                            requestEditor.putBoolean(artist,true);
                            requestEditor.apply();
                        }
                        catch (JSONException e){
                            Log.d("detailsearchart",e.toString());
                            eventDetail = new JSONObject();
                        }
                        detailPagerAdapter.setHasattractions(true);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("detailsearch",error.toString());
                    }
                });
                queue.add(stringRequest);

            }

        }
        catch (JSONException e){
            Log.d("detailsearchart",e.toString());
        }



    }
    private void recvArtist(){
        if(hash != hashcode)
            return;

        transmissionEditor.putBoolean("artist",false);
        transmissionEditor.apply();
        artist_info = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);
        try{
            for(int i = 0; i < attraction.length();i++){
                String artist = attraction.get(i).toString();
                Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                        .appendPath("artist")
                        .appendQueryParameter("name", artist)
                        .build();
                artistEditor.putBoolean(artist,false);
                artistEditor.apply();
                Attraction temp = attractionList.get(i);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject eventDetail;
                        try{
                            eventDetail = new JSONObject(response);

                            artistEditor.putBoolean(artist,true);
                            artistEditor.apply();
                            temp.setUrl(eventDetail.getString("url"));
                            temp.setPopularity(String.valueOf(eventDetail.getInt("popularity")));
                            temp.setFollower(String.valueOf(eventDetail.getInt("follower")));

                        }
                        catch (JSONException e){
                            Log.d("detailsearcharttt",e.toString());
                            eventDetail = new JSONObject();
                        }

                        detailPagerAdapter.setHasartist(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("detailsearch",error.toString());
                    }
                });
                queue.add(stringRequest);

            }

        }
        catch (JSONException e){
            Log.d("detailsearchart",e.toString());
        }


    }
    private void recvUpcomming(){
        if(hash != hashcode)
            return;

        Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath("comming")
                .appendQueryParameter("name", venue)
                .build();
        // Instantiate the RequestQueue.
        requestEditor.putBoolean("comming",false);
        requestEditor.apply();
        dataEditor.putString("comming","{}");
        dataEditor.apply();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject eventDetail;
                try{
                    eventDetail = new JSONObject(response);
                    dataEditor.putString("comming",response);
                    dataEditor.apply();
                    requestEditor.putBoolean("comming",true);
                    requestEditor.apply();
                    commingResult.clear();
                    JSONObject eventD = new JSONObject(response);
                    JSONArray commingArr = eventD.getJSONArray("comming");

                    detailPagerAdapter.setCommingResult(commingArr);

                }
                catch (Exception e){

                    eventDetail = new JSONObject();
                }
                detailPagerAdapter.setHascomming(true);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("detailsearch",error.toString());
            }
        });
        queue.add(stringRequest);

    }


    private void recvData(){
        if(hash != hashcode)
            return;
        eventDetails = new EventDetail();
        Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath("details")
                .appendQueryParameter("id", id)
                .build();
        // Instantiate the RequestQueue.
        requestEditor.putBoolean("detail",false);
        requestEditor.apply();
        dataEditor.putString("detail","{}");
        dataEditor.apply();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url.toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject eventDetail;
                            try{
                                eventDetail = new JSONObject(response);
                                venue = eventDetail.getJSONArray("venue").get(0).toString();

                                attraction = eventDetail.getJSONArray("attractions");
                                Log.d("search_attraction",attraction.toString());
                                for(int i = 0; i< attraction.length();i++){
                                    Attraction temp = new Attraction();
                                    if(!type.equals("Music")){
                                        temp.setMusic(false);
                                        temp.setFollower("");
                                        temp.setName(attraction.get(i).toString());
                                        temp.setPopularity("");
                                        temp.setUrl("");
                                        temp.setPics("{fig:[]}");
                                    }else{
                                        temp.setMusic(true);
                                        temp.setFollower("");
                                        temp.setName(attraction.get(i).toString());
                                        temp.setPopularity("");
                                        temp.setUrl("");
                                        temp.setPics("{fig:[]}");
                                    }
                                    attractionList.add(temp);
                                }
                                requestEditor.putBoolean("detail",true);
                                requestEditor.apply();

                                dataEditor.putString("detail",response);
                                dataEditor.apply();



                                JSONObject eventD = new JSONObject(response);
                                if(eventD.has("venue")){
                                    eventDetails.setVenue(eventD.getJSONArray("venue").get(0).toString());
                                }
                                if(eventD.has("attractions")){

                                    JSONArray attractionArray = eventD.getJSONArray("attractions");
                                    eventDetails.setAttractions(attractionArray);
                                }

                                if(eventD.has("category")){
                                    JSONArray categoryArray = eventD.getJSONArray("category");
                                    eventDetails.setCategory(categoryArray);
                                }
                                if(eventD.has("status")){
                                    eventDetails.setStatus(eventD.getString("status"));
                                }

                                if(eventD.has("url")){
                                    eventDetails.setUrl(eventD.getString("url"));
                                }

                                if(eventD.has("seat_map")){
                                    eventDetails.setUrlSeat(eventD.getString("seat_map"));
                                }

                                if(eventD.has("priceRange")){
                                    JSONArray attractionArray = eventD.getJSONArray("priceRange");
                                    eventDetails.setPriceRange(attractionArray);
                                }
                                if(eventD.has("Time")){
                                    String date = eventD.getJSONObject("Time").getString("localDate");
                                    String time =  eventD.getJSONObject("Time").getString("localTime");
                                    Time dat = new Time(date,time);
                                    eventDetails.setTime(dat);
                                }

                                setTwitterText();
                                detailPagerAdapter.setEventDetail(eventDetails);

                            }
                            catch (JSONException e){
                                Log.d("detailsearch",e.toString());
                                eventDetail = new JSONObject();
                            }
                            detailPagerAdapter.setHasdetail(true);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("detailsearch",error.toString());
                }
            });
            queue.add(stringRequest);
        }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_favorite:
                AddorDelFav();
                return true;
            case R.id.tweet:
                openTweetLink();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        detailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.da_vp_container);
        viewPager.setAdapter(detailPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        commingResult = new ArrayList<>();
        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra("name")) {
            this.name = callingIntent.getStringExtra("name");
        }
        if (callingIntent.hasExtra("id")) {
            this.id = callingIntent.getStringExtra("id");
        }
        if (callingIntent.hasExtra("data")) {
            try{
                this.data = new JSONObject( callingIntent.getStringExtra("data"));
                this.type = data.getString("type");

            } catch (Exception e){
                this.data = new JSONObject();
                this.type = "";
            }
        }


        transmissionSharedPreference = getSharedPreferences("transmitstatus", MODE_PRIVATE);
        transmissionEditor = transmissionSharedPreference.edit();
        transmissionEditor.putBoolean("transaction",false);
        transmissionEditor.apply();
        attraction = new JSONArray();
        attractionList = new ArrayList<>();


        attraction_info = new JSONObject();
        artist_info = new JSONObject();

        eventDetails = new EventDetail();
        venueDetail = new Venue();


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        hashcode = this.hashCode();
        hash = this.hashCode();

        requestSharedPreference = getSharedPreferences("requeststatus",MODE_PRIVATE);
        requestEditor = requestSharedPreference.edit();
        requestSharedPreference.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        artistSharedPreference = getSharedPreferences("artiststatus",MODE_PRIVATE);
        artistEditor = artistSharedPreference.edit();
        artistSharedPreference .registerOnSharedPreferenceChangeListener(artistChangeListener);


        dataSharedPreference = getSharedPreferences("jsondata",MODE_PRIVATE);
        dataEditor = dataSharedPreference.edit();

//        dataEditor.putString("attraction","[]");
//        dataEditor.apply();
//        dataEditor.putString("detail","{}");
//        dataEditor.apply();
//        dataEditor.putString("artist","[]");
//        dataEditor.apply();
//        dataEditor.putString("comming","[]");
//        dataEditor.apply();
//        dataEditor.putString("venue","{}");
//        dataEditor.apply();

        transmissionEditor.putBoolean("venue",false);

        transmissionEditor.putBoolean("attraction",false);

        transmissionEditor.putBoolean("artist",false);

        transmissionEditor.putBoolean("detail",false);
        transmissionEditor.putBoolean("comming",false);
        transmissionEditor.apply();

        recvData();



    }


    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

}
