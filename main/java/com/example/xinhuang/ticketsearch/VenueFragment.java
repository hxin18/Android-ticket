package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class VenueFragment extends Fragment implements OnMapReadyCallback {
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
    private SupportMapFragment mapFragment;

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

    public void setVenueDetail(Venue venueDetail) {
        this.venueDetail = venueDetail;
    }

    private Venue venueDetail;

    private  View view;

    private  SharedPreferences dataSharedPreference;
    private  SharedPreferences.OnSharedPreferenceChangeListener dataChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key,"");
            if(key.equals("venue")){
                try {
                    JSONObject eventD = new JSONObject(value);
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



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                create_table();
            }

        }
    };


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
            }
            else{
                view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
            }
        }
    };


    private void create_table(){
        if(venueDetail.getName()!=null){
            TextView et = view.findViewById(R.id.name_info);
            et.setText(venueDetail.getName());
            view.findViewById(R.id.name_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.name_list).setVisibility(View.GONE);
        }

        if(venueDetail.getCity()!=null){
            TextView et = view.findViewById(R.id.city_info);
            et.setText(venueDetail.getCity());
            view.findViewById(R.id.city_list).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.city_list).setVisibility(View.GONE);

        }

        if(venueDetail.getAddress()!=null){
            TextView et = view.findViewById(R.id.address_info);
            et.setText(venueDetail.getAddress());
            view.findViewById(R.id.address_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.address_list).setVisibility(View.GONE);
        }

        if(venueDetail.getPhoneNumer()!=null){
            TextView et = view.findViewById(R.id.phone_info);
            et.setText(venueDetail.getPhoneNumer());
            view.findViewById(R.id.phone_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.phone_list).setVisibility(View.GONE);
        }

        if(venueDetail.getOpenHour()!=null){
            TextView et = view.findViewById(R.id.open_info);
            et.setText(venueDetail.getOpenHour());
            view.findViewById(R.id.open_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.open_list).setVisibility(View.GONE);
        }

        if(venueDetail.getGeneralRule()!=null){
            TextView et = view.findViewById(R.id.general_info);
            et.setText(venueDetail.getGeneralRule());
            view.findViewById(R.id.general_list).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.general_list).setVisibility(View.GONE);
        }

        if(venueDetail.getChildRule()!=null){
            TextView et = view.findViewById(R.id.child_info);
            et.setText(venueDetail.getChildRule());
            view.findViewById(R.id.child_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.child_list).setVisibility(View.GONE);
        }

        view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
        view.findViewById(R.id.venue_list).setVisibility(View.VISIBLE);

        mapFragment.getMapAsync(this);


    }



    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getContext();
        transmissionSharedPreference = context.getSharedPreferences("transmitstatus", MODE_PRIVATE);
        transmissionSharedPreference .registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
        dataSharedPreference = context.getSharedPreferences("jsondata",MODE_PRIVATE);
        dataSharedPreference.registerOnSharedPreferenceChangeListener(dataChangeListener);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        this.view = view;
        if(artist&&comming&&attractions&&detail&&venue){
            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.da_mf_map);
        create_table();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(venueDetail.getLocation()!=null){

            try {
                double destinationLocationLat = venueDetail.getLocation().getDouble("lat");
                double destinationLocationLng = venueDetail.getLocation().getDouble("lng");
                LatLng destinationLocation = new LatLng(destinationLocationLat, destinationLocationLng);
                googleMap.addMarker(new MarkerOptions().position(destinationLocation));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLocation));
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
            } catch (JSONException e) {
                Log.d("ggmap",e.toString());
            }
        }


    }
}