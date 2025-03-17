package com.pro.st.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.pro.st.Model.MessageItem;
import com.pro.st.R;
import java.util.List;
import java.util.Locale;

public class MessegesAdapter extends RecyclerView.Adapter<MessegesAdapter.ViewHolder> {
    private static final String TAG = "MessegesAdapter";
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context context;
    private final List<MessageItem> message_itemList;
    private String imageURL;
    FirebaseUser firUser;
    Translator Translator;
    public boolean trans;


    public MessegesAdapter(Context context, List<MessageItem> message_itemList, String imageURL, boolean trans) {
        this.context = context;
        this.message_itemList = message_itemList;
        this.imageURL = imageURL;
        this.trans = trans;
    }

    public MessegesAdapter(Context context, List<MessageItem> message_itemList) {

        this.context = context;
        this.message_itemList = message_itemList;

    }

    @NonNull
    @Override
    public MessegesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.my_messege_item, parent, false);
            Log.d(TAG, "my_messege_item");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.ur_messege_item, parent, false);
            Log.d(TAG, "ur_messege_item");
        }
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MessegesAdapter.ViewHolder holder, int position) {

        MessageItem messag = message_itemList.get(position);

        if (holder.getItemViewType() == 0) {

            if (trans) {

                identifyLanguageWithStringInput(holder,messag.getTextMessege());
                //translator(holder, messeg.getTextMessege());
                    holder.loading.setVisibility(View.VISIBLE);
            } else {
                holder.show_Massage.setText(messag.getTextMessege());

            }

        } else {

            holder.show_Massage.setText(messag.getTextMessege());

        }

        if (imageURL.equals("default")) {
            holder.usersImge.setImageResource(R.drawable.users_default_image);


        } else if (imageURL.equals("Assistant")) {


            holder.usersImge.setImageResource(R.mipmap.button_assistant);


        } else {

            Glide.with(context).load(imageURL).into(holder.usersImge);

        }

    }

    @Override
    public int getItemCount() {
        return message_itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView show_Massage;
        private final ImageView usersImge;
        private final ProgressBar loading;

        public ViewHolder(View iteamView) {
            super(iteamView);
            show_Massage = iteamView.findViewById(R.id.text_messege);
            usersImge = iteamView.findViewById(R.id.profile_Image);
            loading = iteamView.findViewById(R.id.messegLoading);

        }

    }

    @Override
    public int getItemViewType(int position) {
        firUser = FirebaseAuth.getInstance().getCurrentUser();
        if (message_itemList.get(position).getSender().equals(firUser.getUid())) {

            return MSG_TYPE_RIGHT;

        } else {


            return MSG_TYPE_LEFT;

        }
    }

    private void identifyLanguageWithStringInput(MessegesAdapter.ViewHolder holder,String text) {

        LanguageIdentifier languageIdentifier =
                LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(text).addOnSuccessListener(languageCode -> {
            Log.d(TAG, languageCode);
            if (!languageCode.equals("und")) {

                    translator(holder,languageCode, text);

            }else {
                translator(holder,"en", text);

            }
        }).addOnFailureListener(
                e -> {

                });

    }


    public void translator(MessegesAdapter.ViewHolder holder, String lng,String text) {

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.fromLanguageTag(lng))
                        .setTargetLanguage(Locale.getDefault().getLanguage())
                        .build();
        Translator =
                Translation.getClient(options);
        Translator.downloadModelIfNeeded().addOnCompleteListener(task -> Translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {

                holder.show_Massage.setText("  " + text + "\n" + "|: " + s);
                holder.loading.setVisibility(View.GONE);
            }


        }).addOnFailureListener(e -> {

            Log.i(TAG, "Language : " + e);
            // holder.show_Massage.setText(text);

        }));


    }


}
