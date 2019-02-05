package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";


    public void setFavoriteEvent(ArrayList<Event> favoriteEvent) {
        this.favoriteEvent = favoriteEvent;
    }

    private ArrayList<Event>  favoriteEvent = new ArrayList<>();;
    private ResultAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONObject favourite;
    private OnclickListListener onclickListListener;
    private View view;
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

                } catch (Exception e) {

                    e.printStackTrace();
                }


            adapter.notifyDataSetChanged();
            if(favoriteEvent.size()==0){
                view.findViewById(R.id.rv_favorites).setVisibility(View.GONE);
                view.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.rv_favorites).setVisibility(View.VISIBLE);
                view.findViewById(R.id.no_favorites).setVisibility(View.GONE);
            }
            }
    };

    public class OnclickListListener implements ResultAdapter.ItemAndFavListener{


        @Override
        public void selectItem(int item) {

            final Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra("id", favoriteEvent.get(item).getId());
            intent.putExtra("name", favoriteEvent.get(item).getName());
            intent.putExtra("data",favoriteEvent.get(item).toString());
            startActivity(intent);
        }

        @Override
        public void setAndRemoveFav(int item) {
            boolean fav = favoriteEvent.get(item).isFavorite();
            String id =  favoriteEvent.get(item).getId();

            if(fav){
                favourite.remove(id);
                favoriteEvent.get(item).setFavorite(false);
                Toast.makeText(context,  favoriteEvent.get(item).getName()+" was removed from favourites", Toast.LENGTH_LONG).show();
                favoriteEvent.remove(item);

            }
           // put JSONArray back into shared preferences file

            favoritesEditor.putString("favouriteEvent", favourite.toString());

            // save changes
            favoritesEditor.apply();
            adapter.notifyDataSetChanged();
            if(favoriteEvent.size()==0){
                view.findViewById(R.id.rv_favorites).setVisibility(View.GONE);
                view.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.rv_favorites).setVisibility(View.VISIBLE);
                view.findViewById(R.id.no_favorites).setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getContext();
        onclickListListener = new OnclickListListener();
        favoritesSharedPreferences = context.getSharedPreferences("userfavourite", MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();
        favoritesSharedPreferences.registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        this.view = view;
        recyclerView = view.findViewById(R.id.rv_favorites);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ResultAdapter(favoriteEvent, onclickListListener);
        recyclerView.setAdapter(adapter);
        if(favoriteEvent.size()==0){
            view.findViewById(R.id.rv_favorites).setVisibility(View.GONE);
            view.findViewById(R.id.no_favorites).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.rv_favorites).setVisibility(View.VISIBLE);
            view.findViewById(R.id.no_favorites).setVisibility(View.GONE);
        }


        return view;
    }
}
