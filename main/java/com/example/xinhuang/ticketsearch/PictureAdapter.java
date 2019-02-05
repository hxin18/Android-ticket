package com.example.xinhuang.ticketsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {
    private ArrayList<String> picUrl;
    private Context context;

    public PictureAdapter(ArrayList<String> picUrl) {
        this.picUrl = picUrl;

    }

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fig_list_view, viewGroup, false);
        PictureAdapter.PictureHolder holder = new PictureAdapter.PictureHolder(v);
        context = viewGroup.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder pictureHolder, int i) {
        if(i<8){
        String url = picUrl.get(i);
//        Glide.with(context).load(url).into(pictureHolder.getImg());
            Log.d("pic_debug",url);
        Picasso.get().load(url).fit().centerCrop().into(pictureHolder.getImg());
        }

    }

    @Override
    public int getItemCount() {
        return Math.min(picUrl.size(),8);
    }

    public class PictureHolder extends RecyclerView.ViewHolder {

        public ImageView getImg() {
            return img;
        }

        private ImageView img;

        public PictureHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.da_pf_rv_iv_photo);
        }


    }

}
