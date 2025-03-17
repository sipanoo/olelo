package com.pro.st.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pro.st.ChatActivity;
import com.pro.st.Constants;
import com.pro.st.Model.User;
import com.pro.st.R;
import java.util.List;

public class MatchAdapter extends ArrayAdapter<User> {

    List<User> users;
    Context mContext;



    public MatchAdapter(@NonNull Context context, @NonNull List<User> users) {
        super(context, R.layout.card_item, users);
        this.mContext = context;
        this.users = users;


    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        User user = users.get(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }
        TextView ort = convertView.findViewById(R.id.CardOrttext);
        TextView name = convertView.findViewById(R.id.Cardnametext);
        ImageView image = convertView.findViewById(R.id.CardImage);
        convertView.findViewById(R.id.chat_button).setOnClickListener(views -> {

            Intent chatIntent = new Intent(mContext, ChatActivity.class);
            chatIntent.putExtra(Constants.KEY_BIO_ID, user.getUserId());
            chatIntent.putExtra("bioName", user.getName());
            chatIntent.putExtra("bioImage", user.getImageURL());
            mContext.startActivity(chatIntent);


        });

        name.setText(user.getName());
        ort.setText(user.getcity());
        if (user.getImageURL().equals("default")) {
            image.setImageResource(R.drawable.users_default_image);

        } else {
            Glide.with(getContext()).load(user.getImageURL()).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    image.setBackground(resource);

                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

          //  Glide.with(getContext()).load(user.getImageURL()).into(image);

        }


        return convertView;
    }


}
