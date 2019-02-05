package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class ResultActivity extends AppCompatActivity {

    private ArrayList<Event> searchResult;
    private ResultAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONObject favourite;
    private  OnclickListListener onclickListListener;


    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String value = sharedPreferences.getString(key, "default_value_string");

            try {
                favourite = new JSONObject(value);
                for(Event event:searchResult){
                    event.setFavorite(favourite.has(event.getId()));
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    };

    public class OnclickListListener implements ResultAdapter.ItemAndFavListener{


        @Override
        public void selectItem(int item) {

            final Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("id", searchResult.get(item).getId());
            intent.putExtra("name", searchResult.get(item).getName());
            intent.putExtra("data",searchResult.get(item).toString());
            Log.d("selected_item",searchResult.get(item).toString());
            startActivity(intent);
        }

        @Override
        public void setAndRemoveFav(int item) {
            boolean fav = searchResult.get(item).isFavorite();
            String id =  searchResult.get(item).getId();

            if(fav){
                favourite.remove(id);
                Toast.makeText(context,  searchResult.get(item).getName()+" was removed from favourites", Toast.LENGTH_LONG).show();
            }
            else{
                JSONObject jsonEvent;
                try{
                    jsonEvent = new JSONObject(searchResult.get(item).toString());
                    Toast.makeText(context,  searchResult.get(item).getName()+" was added to favourites", Toast.LENGTH_LONG).show();

                    favourite.put(id,jsonEvent);
                }catch (JSONException e){
                    Log.d("createJson",e.toString());
                }


            }
            searchResult.get(item).setFavorite(!fav);
            // put JSONArray back into shared preferences file
            favoritesEditor.putString("favouriteEvent", favourite.toString());

            // save changes
            favoritesEditor.apply();
            adapter.notifyItemChanged(item);

        }
    }



    private void recvData(){
        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Intent.EXTRA_TEXT)) {
            searchResult.clear();
            String search_url = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this.context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, search_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("_embedded")) {
                                    JSONObject embedded = jsonObject.getJSONObject("_embedded");
                                    JSONArray resultsArray = embedded.getJSONArray("events");
                                    for (int i = 0; i < resultsArray.length(); i++) {
                                        JSONObject res = resultsArray.getJSONObject(i);
                                        String id = res.getString("id");
                                        String name = res.getString("name");
                                        String date = res.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                        JSONArray classifications = res.getJSONArray("classifications");
                                        JSONObject genreType = classifications.getJSONObject(0);
                                        String genre = genreType.getJSONObject("genre").getString("name");
                                        String type =  genreType.getJSONObject("segment").getString("name");
                                        String venue =res.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                        try {
                                            favourite = new JSONObject(favoritesSharedPreferences.getString("favouriteEvent", ""));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            favourite = new JSONObject();
                                        }

                                        searchResult.add(new Event(name,id,genre,type,date,venue, favourite.has(id)));
                                    }
                                }

                            }
                            catch (JSONException e) {
                                Log.d("recvdata",e.toString());
                            }
                            findViewById(R.id.resultprogress).setVisibility(View.GONE);
                            recyclerView = findViewById(R.id.rv_place_results);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new ResultAdapter(searchResult, onclickListListener);
                            recyclerView.setAdapter(adapter);
                            if(searchResult.size()==0){
                                findViewById(R.id.rv_place_results).setVisibility(View.GONE);
                                findViewById(R.id.search_no_results).setVisibility(View.VISIBLE);
                            }
                            else{
                                findViewById(R.id.rv_place_results).setVisibility(View.VISIBLE);
                                findViewById(R.id.search_no_results).setVisibility(View.GONE);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("debugg222",error.toString());
                    findViewById(R.id.resultprogress).setVisibility(View.GONE);
                    findViewById(R.id.rv_place_results).setVisibility(View.GONE);
                    findViewById(R.id.search_no_results).setVisibility(View.VISIBLE);
                }
            });

            queue.add(stringRequest);


        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Search Result");
        context = this;
        setContentView(R.layout.activity_result);
        searchResult = new ArrayList<>();
        onclickListListener = new OnclickListListener();
        favoritesSharedPreferences = getSharedPreferences("userfavourite", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        favoritesSharedPreferences.registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
        try {
            favourite = new JSONObject(favoritesSharedPreferences.getString("favouriteEvent", ""));
        } catch (Exception e) {
            e.printStackTrace();
            favourite = new JSONObject();
        }
        recvData();


    }
}
