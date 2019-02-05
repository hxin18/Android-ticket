package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private ArtistAdapter artistAdapter;
    private RecyclerView recyclerView;

    private Boolean artist = false;
    private Boolean comming = false;
    private Boolean attractions = false;
    private Boolean detail = false;
    private Boolean venue = false;
    private ArrayList<Attraction> commingResult;

    private  SharedPreferences dataSharedPreference;
    private  SharedPreferences.OnSharedPreferenceChangeListener dataChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key,"");
            if(key.equals("attraction")){
                try {
                    commingResult.clear();

                    JSONArray commingArr = new JSONArray(value);
                    Log.d("cardib",value);
                    for(int i = 0;i<Math.min(commingArr.length(),2);i++){
                        JSONObject cm = new JSONObject(commingArr.get(i).toString());
                        Attraction ucm = new Attraction();
                        ucm.setPics(cm.getString("pics"));
                        ucm.setFollower(cm.getString("follower"));
                        ucm.setPopularity(cm.getString("popularity"));
                        ucm.setMusic(cm.getBoolean("music"));
                        ucm.setUrl(cm.getString("url"));
                        ucm.setName(cm.getString("name"));
                        commingResult.add(ucm);
                    }
                    artistAdapter.notifyDataSetChanged();


                }
                catch (Exception e){

                    Log.d("error_here_artist",e.toString());
                }
                Log.d("error_here_artist",value);
            }

        }
    };

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




    public ArtistFragment() {
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

        context = getContext();
        commingResult = new ArrayList<>();
        String response = getArguments().getString("initialdata");

        JSONArray commingArr = null;
        try {
            commingArr = new JSONArray(response);
            for(int i = 0;i<commingArr.length();i++){

                JSONObject cm = new JSONObject(commingArr.get(i).toString());
                Attraction ucm = new Attraction();
                ucm.setPics(cm.getString("pics"));
                ucm.setFollower(cm.getString("follower"));
                ucm.setPopularity(cm.getString("popularity"));
                ucm.setMusic(cm.getBoolean("music"));
                ucm.setUrl(cm.getString("url"));
                ucm.setName(cm.getString("name"));
                commingResult.add(ucm);
            }
        } catch (JSONException e) {
            Log.d("debugforattraction",e.toString());
        }

        transmissionSharedPreference = context.getSharedPreferences("transmitstatus", MODE_PRIVATE);
        transmissionSharedPreference .registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);

        dataSharedPreference = context.getSharedPreferences("jsondata",MODE_PRIVATE);
        dataSharedPreference.registerOnSharedPreferenceChangeListener(dataChangeListener);
        artistAdapter = new ArtistAdapter(commingResult,context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        this.view = view;
        if(artist&&comming&&attractions&&detail&&venue){
            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
        }
        Log.d("initial_data",commingResult.toString());


        recyclerView = view.findViewById(R.id.artist_results);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(artistAdapter);

        artistAdapter.notifyDataSetChanged();

        Log.d("estt_artist", String.valueOf(artist)+String.valueOf(attractions)+String.valueOf(comming)+String.valueOf(detail)+String.valueOf(venue));
        return view;
    }

}
