package com.pro.st.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pro.st.BioActivity;
import com.pro.st.R;
import com.pro.st.Model.User;
import java.util.List;

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder> {


    private final Context context;
    private final List<User> users;

    public FindAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getName());
        holder.ort.setText(user.getcity());
        if(user.getImageURL().equals("default")){
          holder.usersImge.setImageResource(R.drawable.users_default_image);
        }else{

           Glide.with(context).load(user.getImageURL()).into(holder.usersImge);

        }

       holder.itemView.setOnClickListener(view ->{
           Intent intent = new Intent(context, BioActivity.class);
           intent.putExtra("bioId" , user.getUserId());
           intent.putExtra("bioName" , user.getName());
           intent.putExtra("bioImage" , user.getImageURL());

          context.startActivity(intent);



       } );


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView ort;
        private final ImageView usersImge;
        public ViewHolder(View iteamView) {
            super(iteamView);
            username = iteamView.findViewById(R.id.nametext);
            ort = iteamView.findViewById(R.id.ortText);
            usersImge=iteamView.findViewById(R.id.users_image);

        }

    }
}
