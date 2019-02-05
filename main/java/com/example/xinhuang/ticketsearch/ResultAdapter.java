package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultAdapter extends  RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private ArrayList<Event> searchResult;
    private Map<String,String> iconUrl;
    private ItemAndFavListener itemAndFavListener;
    private Context context;

    public interface ItemAndFavListener{
        void selectItem(int item);
        void setAndRemoveFav(int item);
    }

    public ResultAdapter(ArrayList<Event> searchResult, ItemAndFavListener itemAndFavListener ) {
        this.searchResult = searchResult;
        this.iconUrl = new HashMap<>();
        this.iconUrl.put("Music","http://csci571.com/hw/hw9/images/android/music_icon.png");
        this.iconUrl.put("Sports","http://csci571.com/hw/hw9/images/android/sport_icon.png");
        this.iconUrl.put("Arts & Theatre", "http://csci571.com/hw/hw9/images/android/art_icon.png");
        this.iconUrl.put("Miscellaneous", "http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
        this.iconUrl.put("Film","http://csci571.com/hw/hw9/images/android/film_icon.png");
        this.itemAndFavListener = itemAndFavListener;
    }


    @NonNull
    @Override
    public ResultAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_list_view, viewGroup, false);
        ResultHolder holder = new ResultHolder(v);
        context = viewGroup.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultHolder resultHolder, int i) {
        Event event = this.searchResult.get(i);
        resultHolder.getEventName().setText(event.getName());
        resultHolder.getVenue().setText(event.getVenue());
        resultHolder.getTime().setText(event.getDate());
        if(event.isFavorite()){
            resultHolder.getFavIcon().setImageResource(R.drawable.heart_fill_red);
        }
        else{
            resultHolder.getFavIcon().setImageResource(R.drawable.heart_outline_black);
        }
        Picasso.get().load(this.iconUrl.get(event.getType())).resize(400, 400).centerInside().into(resultHolder.icon);

    }

    @Override
    public int getItemCount() {
        return searchResult.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder {



        private TextView eventName;
        private TextView venue;
        private TextView time;
        private ImageView icon;
        private ImageView favIcon;
        private LinearLayout details;



        public ResultHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_result_name);
            venue = itemView.findViewById(R.id.event_result_artist);
            time = itemView.findViewById(R.id.event_result_time);
            icon = itemView.findViewById(R.id.event_result_icon);
            favIcon = itemView.findViewById(R.id.event_favorite_toggle);
            details = itemView.findViewById(R.id.event_clickable_surface);
            details.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        itemAndFavListener.selectItem(position);
                    });
            favIcon.setOnClickListener(
                    v ->{
                        int position = getAdapterPosition();
                        itemAndFavListener.setAndRemoveFav(position);
                    }


            );
        }

        public TextView getEventName() {
            return eventName;
        }

        public TextView getVenue() {
            return venue;
        }

        public TextView getTime() {
            return time;
        }

        public ImageView getIcon() {
            return icon;
        }

        public ImageView getFavIcon() {
            return favIcon;
        }
    }
}
