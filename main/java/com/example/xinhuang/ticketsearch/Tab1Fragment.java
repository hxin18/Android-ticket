package com.example.xinhuang.ticketsearch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;


public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private static final String API_BASE_URL = "http://hxserverhw8.us-west-1.elasticbeanstalk.com";

    private LocationManager locationManager;

    private  Button searchButton;
    private  Button resetButton;

    private AutoCompleteTextView keyword_editor;
    private  TextView input_key_word_err;

    private Spinner category_spinner;
    private Spinner unit_spinner;
    private EditText distant_editor;

    private EditText location_editor;
    private TextView input_location_err;

    private RadioButton here;
    private RadioButton other;
    private RadioGroup rg;

    private Context context;

    private double lat =34.0294;
    private double lng = -118.2871;

    private static ArrayList<String> Country = new ArrayList<String>();
    public void resetForm() {
        this.keyword_editor.setText("");

        this.category_spinner.setSelection(0);

        this.distant_editor.setText("");
        this.here.setChecked(true);

        this.location_editor.setText("");
        input_location_err.setVisibility(View.GONE);
        input_key_word_err.setVisibility(View.GONE);

    }






    public void click_search() {
        String keyword = keyword_editor.getText().toString();
        Log.d("debuggersb", keyword);
        if (keyword.trim().length() == 0){
            input_key_word_err.setVisibility(View.VISIBLE);
            if(other.isChecked()&&location_editor.getText().toString().trim().length()==0){
                input_location_err.setVisibility(View.VISIBLE);
            }
            else{
                input_location_err.setVisibility(View.GONE);
            }
            Toast.makeText(context, "Please fix all field with errors", Toast.LENGTH_LONG).show();


        }
        else{
            if(other.isChecked()&&location_editor.getText().toString().trim().length()==0){
                input_location_err.setVisibility(View.VISIBLE);
                return;
            }
            else{
                input_location_err.setVisibility(View.GONE);
            }
            input_key_word_err.setVisibility(View.GONE);
            String category = this.category_spinner.getSelectedItem().toString();
            String unit = this.unit_spinner.getSelectedItem().toString();
            String distance = this.distant_editor.getText().toString();
            if(this.distant_editor.getText().toString().trim().length()==0){
                distance = "10";
            }

            String isUserInput = String.valueOf(other.isChecked());
            String location = this.location_editor.getText().toString();
            JSONObject user_loc = new JSONObject();
            try {
                user_loc.put("lat",lat);
                user_loc.put("lng",lng);
            }
            catch (JSONException e) {

            }


            final Intent intent = new Intent(getActivity(), ResultActivity.class);
            Uri search_url = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath("search")
                    .appendQueryParameter("keyword", keyword)
                    .appendQueryParameter("category", category)
                    .appendQueryParameter("distance", distance)
                    .appendQueryParameter("isUserInput",isUserInput)
                    .appendQueryParameter("location", location)
                    .appendQueryParameter("geoJson",user_loc.toString())
                    .appendQueryParameter("unit",unit)
                    .build();
            intent.putExtra(Intent.EXTRA_TEXT, search_url.toString());
            startActivity(intent);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        context = getContext();

        this.input_key_word_err = view.findViewById(R.id.error_keyword);
        this.keyword_editor = (AutoCompleteTextView)view.findViewById(R.id.keyword);
        this.keyword_editor.setThreshold(1);
        this.keyword_editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                view.showDropDown();
                } }
            });


        this.category_spinner = view.findViewById(R.id.category);
        this.unit_spinner = view.findViewById(R.id.unit);

        this.distant_editor = view.findViewById(R.id.edit_distance);

        this.here = (RadioButton) view.findViewById(R.id.radio1);
        this.other = (RadioButton) view.findViewById(R.id.radio2);
        this.location_editor = view.findViewById(R.id.edit_location);
        this.location_editor.setEnabled(false);
        this.input_location_err = view.findViewById(R.id.error_location);
        this.searchButton = (Button) view.findViewById(R.id.button_search);
        this.resetButton = (Button) view.findViewById(R.id.button_clear);
        this.rg = view.findViewById(R.id.ratio_group);
        this.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("cjeckl",String.valueOf(checkedId));
                if(checkedId == R.id.radio1){
                    location_editor.setText("");
                    location_editor.setEnabled(false);
                    input_location_err.setVisibility(View.GONE);
                }else{
                    location_editor.setEnabled(true);
                }

            }
        });




        Observable<String> obs = RxTextView.textChanges(this.keyword_editor).filter(charSequence -> charSequence.length()>0).debounce(200, TimeUnit.MILLISECONDS).map(charSequence ->  charSequence.toString());

        obs.subscribe(string -> {
            RequestQueue queue = Volley.newRequestQueue(this.context);
            Uri autoCompleteUrl =  Uri.parse(API_BASE_URL).buildUpon()
                                    .appendPath("autocomplete")
                                    .appendQueryParameter("word",string).build();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, autoCompleteUrl.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            JSONParser parser = new JSONParser();
                            try{
                                JSONArray jsonarray = new JSONArray(response);
                                Country.clear();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String name = jsonobject.getString("name");
                                    Country.add(name);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,Country);
                                keyword_editor.setAdapter(adapter);
                                Log.d(TAG,Country.toString());

                            }
                            catch (JSONException e){
                                Log.d(TAG,e.toString());
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("debugg222",error.toString());
                }
            });

            queue.add(stringRequest);


        });
        //stuff about locations
        locationManager = null;
        if (context != null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        else {
            Toast.makeText(getContext(), "Errors: API Failure", Toast.LENGTH_SHORT).show();
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (getActivity() != null) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                Toast.makeText(getContext(), "Errors: API Failure", Toast.LENGTH_SHORT).show();
            }
            Log.d("debugg222","1 if");
        }
        else {
            Location location;
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
                else {
                    Log.d("debugg222","3 if");
                    Toast.makeText(getContext(), "Errors: Cannot get location", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Log.d("debugg222","2 if");
                Toast.makeText(getContext(), "Errors: Cannot get location", Toast.LENGTH_SHORT).show();
            }
        }




        this.resetButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetForm();
            }
        });



        this.searchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_search();
            }
        });


        return view;
    }

}
