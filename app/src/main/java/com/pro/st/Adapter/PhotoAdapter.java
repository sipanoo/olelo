package com.pro.st.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pro.st.Model.User;
import com.pro.st.PhotoUpload;
import com.pro.st.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


    private final Context context;
    private final List<User> imageURLs;
    private final int VIEW_TYPE_DEFAULT = 0;
    private final int VIEW_TYPE_IMAGE = 1;
    private boolean bio;

    public PhotoAdapter(Context context, List<User> imageURLs, boolean bio) {

        this.imageURLs = imageURLs;
        this.context = context;
        this.bio = bio;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_IMAGE) {

            view = LayoutInflater.from(context).inflate(R.layout.my_images_item, parent, false);

        } else {


            view = LayoutInflater.from(context).inflate(R.layout.default_images_item, parent, false);
        }


        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < Math.min(imageURLs.size(), 3)) {
            User images = imageURLs.get(position);
            Glide.with(context).load(images.getImageURL()).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    holder.myImages.setBackground(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });


        }
        if (holder.getItemViewType() == VIEW_TYPE_DEFAULT) {
            holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, PhotoUpload.class)));

        }

    }

    @Override
    public int getItemCount() {
        int RECENT_PHOTO_LIMIT = 3;
        if (!bio){

            return Math.min(imageURLs.size(), RECENT_PHOTO_LIMIT) + 1;
        }else {
            return Math.min(imageURLs.size(), RECENT_PHOTO_LIMIT) ;

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (!bio) {
            return (position == imageURLs.size()) ? VIEW_TYPE_DEFAULT : VIEW_TYPE_IMAGE;
        } else {
            return VIEW_TYPE_IMAGE;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView myImages;

        public ViewHolder(View iteamView) {
            super(iteamView);
            myImages = iteamView.findViewById(R.id.my_image_items);

        }

    }


}
