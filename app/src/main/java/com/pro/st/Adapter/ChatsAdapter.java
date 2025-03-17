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
import com.pro.st.Assistant;
import com.pro.st.ChatActivity;
import com.pro.st.Constants;
import com.pro.st.Model.ChatItem;
import com.pro.st.R;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private final Context context;
    private final List<ChatItem> chatss ;


    public ChatsAdapter(Context context, List<ChatItem>  chats) {
        this.context = context;
        chatss=chats;

    }


    @NonNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ViewHolder holder, int position) {
        ChatItem chat = chatss.get(position);

        holder.chatUserName.setText(chat.getUser());
        holder.lastMessage.setText(chat.getLastmessege());

        if (chat.getImage().equals("default")) {

            holder.usersImge.setImageResource(R.drawable.users_default_image);
        } else {
            Glide.with(context).load(chat.getImage()).into(holder.usersImge);

        }

        holder.itemView.setOnClickListener(view -> {
            if (!chat.getId().equals(Constants.KEY_Dating_Assistant)) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("bioId", chat.getId());
                intent.putExtra("bioName", chat.getUser());
                intent.putExtra("bioImage", chat.getImage());

                context.startActivity(intent);
            } else {
                Intent assist = new Intent(context, Assistant.class);
                context.startActivity(assist);
            }

        });


    }


    @Override
    public int getItemCount() {
        return chatss.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chatUserName;
        private final TextView lastMessage;
        private final ImageView usersImge;

        public ViewHolder(View iteamView) {
            super(iteamView);
            chatUserName = iteamView.findViewById(R.id.chatUserName);
            lastMessage = iteamView.findViewById(R.id.lastMassage);
            usersImge = iteamView.findViewById(R.id.profile_Image);


        }


    }


}
