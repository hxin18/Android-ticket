package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {


    private ArrayList<Attraction> teamArtist;
    private Context context;

    public ArtistAdapter(ArrayList<Attraction> teamArtist,Context context) {
        this.teamArtist = teamArtist;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_list_view, viewGroup, false);
        ArtistAdapter.ArtistHolder holder = new ArtistAdapter.ArtistHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder artistHolder, int i) {
        Attraction attraction = teamArtist.get(i);
        if(attraction.isMusic()){
            artistHolder.getTable().setVisibility(View.VISIBLE);
        }else{
            artistHolder.getTable().setVisibility(View.GONE);
        }
        if(attraction.getFollower().trim().length()==0){
            artistHolder.getTable().setVisibility(View.GONE);
        }
        artistHolder.getTitle().setText(attraction.getName());
        artistHolder.getName().setText(attraction.getName());
        if(attraction.getFollower().trim().length()>0){
            artistHolder.getFollower().setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(attraction.getFollower())));
        }
        artistHolder.getPopularity().setText(attraction.getPopularity());
        String url = "<a href='"+attraction.getUrl()+"'>Sportify</a>";
        artistHolder.getUrl().setMovementMethod(LinkMovementMethod.getInstance());
        artistHolder.getUrl().setLinksClickable(true);
        artistHolder.getUrl().setText(Html.fromHtml(url,Build.VERSION.SDK_INT));
        ArrayList<String> picUrl = new ArrayList<>();
        try {
            JSONArray pic = new JSONObject(attraction.getPics()).getJSONArray("fig");
            for(int j = 0; j < pic.length();j++){
                picUrl.add(pic.getString(j));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PictureAdapter figAdapter = new PictureAdapter(picUrl);

        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        artistHolder.getRecyclerView().setLayoutManager(layoutManager);

        artistHolder.getRecyclerView().setAdapter(figAdapter);
    }

    @Override
    public int getItemCount() {
        return teamArtist.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {


        private TextView title;
        private TextView name;
        private TextView popularity;
        private TextView follower;
        private TextView url;
        private LinearLayout table;
        private RecyclerView recyclerView;




        public ArtistHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.artist_name);
            name = itemView.findViewById(R.id.artist_name_tab);
            popularity = itemView.findViewById(R.id.popularity);
            follower = itemView.findViewById(R.id.follower);
            url = itemView.findViewById(R.id.artist_url);
            table = itemView.findViewById(R.id.artist_table);
            recyclerView = itemView.findViewById(R.id.artist_fig);

        }


        public TextView getName() {
            return name;
        }

        public TextView getPopularity() {
            return popularity;
        }

        public TextView getFollower() {
            return follower;
        }

        public TextView getUrl() {
            return url;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public TextView getTitle() {
            return title;
        }

        public LinearLayout getTable() {
            return table;
        }
    }
}
