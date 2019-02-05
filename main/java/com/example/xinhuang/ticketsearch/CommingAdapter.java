package com.example.xinhuang.ticketsearch;

import android.os.Build;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

public class CommingAdapter extends  RecyclerView.Adapter<CommingAdapter.CommingHolder> {


    private ArrayList<UpComming> commingEvent=new ArrayList<>();
    private SelectListener selectListener;

    public CommingAdapter(ArrayList<UpComming> commingEvent, SelectListener selectListener) {

        this.commingEvent = commingEvent;
        this.selectListener = selectListener;
    }

    public interface SelectListener{
        void selectItem(int item);
    }

    @NonNull
    @Override
    public CommingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comming_list_view, viewGroup, false);
        CommingHolder holder = new CommingHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommingHolder commingHolder, int i) {
        UpComming cm = commingEvent.get(i);
        commingHolder.getArtist().setText(cm.getArtist());
        commingHolder.getTime().setText(cm.getDate().toString());
        commingHolder.getType().setText("Type: "+cm.getType());
        commingHolder.getEventName().setText(cm.getTitle());

    }

    @Override
    public int getItemCount() {

        return commingEvent.size();
    }


    public class CommingHolder extends RecyclerView.ViewHolder {



        private TextView eventName;

        private TextView artist;
        private TextView time;
        private TextView type;

        private  LinearLayout click;


        public TextView getEventName() {
            return eventName;
        }

        public TextView getArtist() {
            return artist;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getType() {
            return type;
        }


        public CommingHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.comming_name);
            artist = itemView.findViewById(R.id.comming_artist);
            time = itemView.findViewById(R.id.comming_date);
            type = itemView.findViewById(R.id.comming_type);
            click = itemView.findViewById(R.id.uc_click);
            click.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        selectListener.selectItem(position);
                    });

        }


    }
}
