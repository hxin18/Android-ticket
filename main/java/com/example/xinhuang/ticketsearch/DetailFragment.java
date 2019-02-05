package com.example.xinhuang.ticketsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    private Boolean artist = false;
    private Boolean comming = false;
    private Boolean attractions = false;
    private Boolean detail = false;
    private Boolean venue = false;





    private EventDetail eventDetail;

    public void setEventDetail(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
    }


    public void setArtist(Boolean artist) {
        this.artist = artist;
    }

    public void setComming(Boolean comming) {
        this.comming = comming;
    }

    public void setAttractions(Boolean attractions) {
        this.attractions = attractions;
    }

    public void setDetail(Boolean detail) {
        this.detail = detail;
    }

    public void setVenue(Boolean venue) {
        this.venue = venue;
    }





    private  View view;

    private  SharedPreferences dataSharedPreference;
    private  SharedPreferences.OnSharedPreferenceChangeListener dataChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key,"");
            if(key.equals("detail")){
                try {
                    JSONObject eventD = new JSONObject(value);
                    if(eventD.has("venue")){
                        eventDetail.setVenue(eventD.getJSONArray("venue").get(0).toString());
                    }
                    if(eventD.has("attractions")){
                        JSONArray attractionArray = eventD.getJSONArray("attractions");
                        eventDetail.setAttractions(attractionArray);
                    }
                    if(eventD.has("category")){
                        JSONArray categoryArray = eventD.getJSONArray("category");
                        eventDetail.setCategory(categoryArray);
                    }
                    if(eventD.has("status")){
                        eventDetail.setStatus(eventD.getString("status"));
                    }
                    if(eventD.has("priceRange")){
                        JSONArray attractionArray = eventD.getJSONArray("priceRange");
                        eventDetail.setPriceRange(attractionArray);
                    }

                    if(eventD.has("url")){
                        eventDetail.setUrl(eventD.getString("url"));
                    }

                    if(eventD.has("seat_map")){
                        eventDetail.setUrlSeat(eventD.getString("seat_map"));
                    }

                    if(eventD.has("Time")){
                        String date = eventD.getJSONObject("Time").getString("localDate");
                        String time =  eventD.getJSONObject("Time").getString("localTime");
                        Time dat = new Time(date,time);
                        Log.d("timetime",date);
                        Log.d("timetime",time);
                        Log.d("timetime",dat.toString());
                        eventDetail.setTime(dat);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                create_table();
            }
//            Log.d("jsonstring",value);

        }
    };


    private void create_table(){
        if(eventDetail.getVenue()!=null){
            TextView et = view.findViewById(R.id.da_tv_info_address);
            et.setText(eventDetail.getVenue());
            view.findViewById(R.id.address_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.address_list).setVisibility(View.GONE);
        }

        String attractionString  = "";
        if (eventDetail.getAttractions() != null) {
            try {
            JSONArray attractionArray = eventDetail.getAttractions();

                attractionString+= attractionArray.getString(0);
            for (int i=1;i<attractionArray.length();i++){
                attractionString+=" | ";
                attractionString+=attractionArray.getString(i);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView at = view.findViewById(R.id.attraction_info);
            at.setText(attractionString);
            view.findViewById(R.id.attractions_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.attractions_list).setVisibility(View.GONE);

        }
        String categoryString = "";
        if (eventDetail.getCategory() != null) {
            try {
                JSONArray categoryArray = eventDetail.getCategory();

                categoryString+= categoryArray.getString(0);
                for (int i=1;i< categoryArray.length();i++){
                    categoryString+=" | ";
                    categoryString+= categoryArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView ct = view.findViewById(R.id.category_info);
            ct.setText(categoryString);
            view.findViewById(R.id.category_list).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.category_list).setVisibility(View.GONE);
        }

        if (eventDetail.getPriceRange() != null) {
            String priceString = "";
            try {
                JSONArray categoryArray = eventDetail.getPriceRange();

                priceString += "$";
                priceString+=categoryArray.get(0);
                priceString+=" ~ $";
                priceString+=categoryArray.get(1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView ct = view.findViewById(R.id.price_info);
            ct.setText(priceString);
            view.findViewById(R.id.price_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.price_list).setVisibility(View.GONE);
        }

        if(eventDetail.getStatus()!=null){
            TextView et = view.findViewById(R.id.status_info);
            et.setText(eventDetail.getStatus());
            view.findViewById(R.id.status_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.status_list).setVisibility(View.GONE);
        }

        if(eventDetail.getTime()!=null){
            TextView et = view.findViewById(R.id.info_time);
//            Log.d("timetime",eventDetail.getTime().toString());
            et.setText(eventDetail.getTime().toString());
            view.findViewById(R.id.list_time).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.list_time).setVisibility(View.GONE);
        }

        if(eventDetail.getUrl()!=null){
            TextView et = view.findViewById(R.id.tm_info);
            String url = "<a href='"+eventDetail.getUrl()+"'>Ticket Master</a>";
            et.setMovementMethod(LinkMovementMethod.getInstance());
            et.setLinksClickable(true);
            et.setText(Html.fromHtml(url,Build.VERSION.SDK_INT));
            view.findViewById(R.id.tm_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.tm_list).setVisibility(View.GONE);

        }
        if(eventDetail.getUrlSeat()!=null){
            TextView et = view.findViewById(R.id.sm_info);
            String url = "<a href='"+eventDetail.getUrlSeat()+"'>View Here</a>";
            et.setMovementMethod(LinkMovementMethod.getInstance());
            et.setLinksClickable(true);
            et.setText(Html.fromHtml(url,Build.VERSION.SDK_INT));
            view.findViewById(R.id.sm_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.sm_list).setVisibility(View.GONE);
        }





    }



    private SharedPreferences transmissionSharedPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean value = sharedPreferences.getBoolean(key,false);

            if(key.equals("detail")){
                detail = value;
            }

            if(key.equals("comming")){
                comming = value;
            }
            if(key.equals("venue")){
                venue = value;
            }
            if(key.equals("artist")){
                artist = value;
            }
            if(key.equals("attraction")){
                attractions = value;
            }


            if(artist&&comming&&attractions&&detail&&venue){
                view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
                view.findViewById(R.id.detail_list).setVisibility(View.VISIBLE);
            }
            else{
                view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
                view.findViewById(R.id.detail_list).setVisibility(View.GONE);
            }



        }
    };







    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist = getArguments().getBoolean("hasartists");
        attractions = getArguments().getBoolean("hasattractions");
        comming = getArguments().getBoolean("hascomming");
        detail = getArguments().getBoolean("hasdetail");
        venue = getArguments().getBoolean("hasvenue");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        this.view = view;
        context = getContext();
        transmissionSharedPreference = context.getSharedPreferences("transmitstatus", MODE_PRIVATE);
        transmissionSharedPreference .registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
        dataSharedPreference = context.getSharedPreferences("jsondata",MODE_PRIVATE);
        dataSharedPreference.registerOnSharedPreferenceChangeListener(dataChangeListener);

        Log.d("estt_details", String.valueOf(artist)+String.valueOf(attractions)+String.valueOf(comming)+String.valueOf(detail)+String.valueOf(venue));

        if(artist&&comming&&attractions&&detail&&venue){

            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
            view.findViewById(R.id.detail_list).setVisibility(View.VISIBLE);
            create_table();
        }
        else{
            view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
            view.findViewById(R.id.detail_list).setVisibility(View.GONE);
        }


        return view;
    }



}
