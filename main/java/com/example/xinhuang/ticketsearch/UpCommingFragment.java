package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;


public class UpCommingFragment extends Fragment {


    private Spinner sortSpinner;
    private Spinner orderSpinner;
    private Context context;

    private RecyclerView recyclerView;


    private Boolean artist = false;
    private Boolean comming = false;
    private Boolean attractions = false;
    private Boolean detail = false;
    private Boolean venue = false;

    private Boolean ascending = true;

    private ArrayList<UpComming> commingResult;
    private ArrayList<UpComming> showList;
    private ArrayList<UpComming> originList;
    private CommingAdapter commingAdapter;
    private int current_select = 0;

    public class OnclickListener implements CommingAdapter.SelectListener{

        @Override
        public void selectItem(int item) {


            Uri uri = Uri.parse(showList.get(item).getUrl());

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            startActivity(intent);

        }
    };



    public void setCommingResult(ArrayList<UpComming> commingResult) {
        this.commingResult = commingResult;
        Log.d("jsonstringcomming",commingResult.toString());
    }

    private  SharedPreferences dataSharedPreference;
    private  SharedPreferences.OnSharedPreferenceChangeListener dataChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key,"");
            if(key.equals("comming")){
                try {
                    commingResult.clear();
                    JSONObject eventD = new JSONObject(value);
                    JSONArray commingArr = eventD.getJSONArray("comming");
                    for(int i = 0;i<commingArr.length();i++){
                        JSONObject cm = commingArr.getJSONObject(i);
                        UpComming ucm = new UpComming();
                        ucm.setTitle(cm.getString("displayName"));
                        ucm.setDate(new Time(cm.getJSONObject("date").getString("date"),cm.getJSONObject("date").getString("time")));
                        ucm.setArtist(cm.getJSONArray("artists").get(0).toString());
                        ucm.setType(cm.getString("type"));
                        ucm.setUrl(cm.getString("url"));
                        ucm.setId(i);
                        commingResult.add(ucm);
                    }
                    commingAdapter.notifyDataSetChanged();
                    if(commingResult.size()>0){
                    view.findViewById(R.id.comming_results).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
                        view.findViewById(R.id.search_no_records).setVisibility(View.GONE);
                        sortSpinner.setEnabled(true);
                    }
                    else{
                        sortSpinner.setEnabled(false);
                        view.findViewById(R.id.comming_results).setVisibility(View.GONE);
                        view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
                        view.findViewById(R.id.search_no_records).setVisibility(View.VISIBLE);

                    }

                }
                    catch (Exception e){

                        Log.d("jsonstringcomming",e.toString());
                    }

            }

        }
    };


    Comparator<UpComming> comparatorByDefault = new Comparator<UpComming>() {
        @Override
        public int compare(UpComming up1, UpComming up2) {
            return up1.getId()-up2.getId();
        }
    };


    Comparator<UpComming> comparatorByName = new Comparator<UpComming>() {
        @Override
        public int compare(UpComming up1, UpComming up2) {
            return up1.getTitle().compareTo(up2.getTitle());
        }
    };

    Comparator<UpComming> comparatorByType = new Comparator<UpComming>() {
        @Override
        public int compare(UpComming up1, UpComming up2) {
            return up1.getType().compareTo(up2.getType());
        }
    };

    Comparator<UpComming> comparatorByDate = new Comparator<UpComming>() {
        @Override
        public int compare(UpComming up1, UpComming up2) {
            Log.d("incompare",up1.getDate().getDate().toString());
            Log.d("incompare",up2.getDate().getDate().toString());
            return up1.getDate().getDate().compareTo(up2.getDate().getDate());
        }
    };

    Comparator<UpComming> comparatorByArtist = new Comparator<UpComming>() {
        @Override
        public int compare(UpComming up1, UpComming up2) {
            return up1.getArtist().compareTo(up2.getArtist());
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
                view.findViewById(R.id.comming_results).setVisibility(View.VISIBLE);
                view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
            }
            else{
                view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
                view.findViewById(R.id.comming_results).setVisibility(View.GONE);
                view.findViewById(R.id.comming_list).setVisibility(View.GONE);
            }



        }
    };



    public UpCommingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpCommingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpCommingFragment newInstance(String param1, String param2) {
        UpCommingFragment fragment = new UpCommingFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_comming, container, false);
        this.view = view;
        if(artist&&comming&&attractions&&detail&&venue){
            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
            view.findViewById(R.id.comming_results).setVisibility(View.VISIBLE);
            view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.resultprogress).setVisibility(View.VISIBLE);
            view.findViewById(R.id.comming_results).setVisibility(View.GONE);
            view.findViewById(R.id.comming_list).setVisibility(View.GONE);
        }

        context = getContext();
        transmissionSharedPreference = context.getSharedPreferences("transmitstatus", MODE_PRIVATE);
        transmissionSharedPreference .registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);

        dataSharedPreference = context.getSharedPreferences("jsondata",MODE_PRIVATE);
        dataSharedPreference.registerOnSharedPreferenceChangeListener(dataChangeListener);

        sortSpinner = view.findViewById(R.id.sp_type);
        orderSpinner= view.findViewById(R.id.sp_sort);




        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int order = orderSpinner.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        orderSpinner.setSelection(0);
                        orderSpinner.setEnabled(false);
                        Collections.sort(showList,comparatorByDefault);
                        if(order == 1 )
                            Collections.reverse(showList);
                        commingAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        orderSpinner.setEnabled(true);
                        Collections.sort(showList,comparatorByName);
                        if(order == 1 )
                            Collections.reverse(showList);
                        commingAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        orderSpinner.setEnabled(true);
                        Collections.sort(showList,comparatorByDate);
//                        for(int i = 0; i<showList.size();i++){
//                            Log.d("daybug111",showList.get(i).getDate().toString());
//                            Log.d("daybug222",showList.get(i).getDate().getDate().toString());
//                        }
                        if(order == 1 )
                            Collections.reverse(showList);
                        commingAdapter.notifyDataSetChanged();    // ascending (lowest rating first)
                        break;
                    case 3:
                        orderSpinner.setEnabled(true);
                        Collections.sort(showList,comparatorByArtist);
                        if(order == 1 )
                            Collections.reverse(showList);
                        commingAdapter.notifyDataSetChanged();    // ascending (lowest rating first)
                        // descending (newest review first)
                        break;
                    case 4:
                        orderSpinner.setEnabled(true);
                        Collections.sort(showList,comparatorByType);
                        if(order == 1 )
                            Collections.reverse(showList);
                        commingAdapter.notifyDataSetChanged();    // ascending (lowest rating first)
                        break;   // ascending (oldest review first)
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });


        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(current_select != 0){
                            Collections.reverse(showList);
                            current_select = 0;
                        }
                        commingAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        if(current_select == 0){
                            Collections.reverse(showList);
                            current_select = 1;
                        }
                        commingAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        commingResult = new ArrayList<>();
        showList = new ArrayList<>();
        String response = getArguments().getString("initialdata");
        JSONArray commingArr = null;
        try {
            commingArr = new JSONArray(response);

            for(int i = 0;i<commingArr.length();i++){
                JSONObject cm = commingArr.getJSONObject(i);
                UpComming ucm = new UpComming();
                ucm.setTitle(cm.getString("displayName"));
                Log.d("datedebug",cm.getJSONObject("date").toString());
                ucm.setDate( new Time(cm.getJSONObject("date").getString("date"),cm.getJSONObject("date").getString("time")));
                ucm.setArtist(cm.getJSONArray("artists").get(0).toString());
                ucm.setType(cm.getString("type"));
                ucm.setUrl(cm.getString("url"));
                ucm.setId(i);
                commingResult.add(ucm);
            }

            if(commingResult.size()>5)
                showList = new ArrayList<>(commingResult.subList(0,5));
            else
                showList = new ArrayList<>(commingResult);


        } catch (Exception e) {
            e.printStackTrace();
        }


        commingAdapter = new CommingAdapter(showList,new OnclickListener());
        recyclerView = view.findViewById(R.id.comming_results);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commingAdapter);

        if(commingResult.size()>0){
            view.findViewById(R.id.comming_results).setVisibility(View.VISIBLE);
            view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
            view.findViewById(R.id.search_no_records).setVisibility(View.GONE);
            sortSpinner.setEnabled(true);
        }
        else{
            sortSpinner.setEnabled(false);
            view.findViewById(R.id.comming_results).setVisibility(View.GONE);
            view.findViewById(R.id.comming_list).setVisibility(View.VISIBLE);
            view.findViewById(R.id.resultprogress).setVisibility(View.GONE);
            view.findViewById(R.id.search_no_records).setVisibility(View.VISIBLE);

        }

        return view;
    }

}
