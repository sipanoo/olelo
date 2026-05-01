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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessegesAdapter extends RecyclerView.Adapter<MessegesAdapter.ViewHolder> {
    private static final String TAG = "MessegesAdapter";
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context context;
    private final List<MessageItem> message_itemList;
    private final String imageURL;
    private final FirebaseUser firUser;
    public boolean trans;

    // ML Kit: cached instances to avoid recreation per message (performance improvement)
    private final LanguageIdentifier languageIdentifier;
    private final Map<String, Translator> translatorCache = new HashMap<>();


    public MessegesAdapter(Context context, List<MessageItem> message_itemList, String imageURL, boolean trans) {
        this.context = context;
        this.message_itemList = message_itemList;
        this.imageURL = imageURL;
        this.trans = trans;
        this.firUser = FirebaseAuth.getInstance().getCurrentUser();
        // Initialize LanguageIdentifier once instead of per-message
        this.languageIdentifier = LanguageIdentification.getClient();
    }

    public MessegesAdapter(Context context, List<MessageItem> message_itemList) {
        this.context = context;
        this.message_itemList = message_itemList;
        this.imageURL = "default";
        this.trans = false;
        this.firUser = FirebaseAuth.getInstance().getCurrentUser();
        this.languageIdentifier = LanguageIdentification.getClient();
    }

    @NonNull
    @Override
    public MessegesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.my_messege_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.ur_messege_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessegesAdapter.ViewHolder holder, int position) {

        MessageItem message = message_itemList.get(position);

        if (holder.getItemViewType() == MSG_TYPE_LEFT) {
            if (trans) {
                identifyLanguageAndTranslate(holder, message.getTextMessege());
                holder.loading.setVisibility(View.VISIBLE);
            } else {
                holder.show_Massage.setText(message.getTextMessege());
            }
        } else {
            holder.show_Massage.setText(message.getTextMessege());
        }

        if (imageURL == null || imageURL.equals("default")) {
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

        public ViewHolder(View itemView) {
            super(itemView);
            show_Massage = itemView.findViewById(R.id.text_messege);
            usersImge = itemView.findViewById(R.id.profile_Image);
            loading = itemView.findViewById(R.id.messegLoading);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (firUser != null && message_itemList.get(position).getSender().equals(firUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    /**
     * ML Kit: Identifies the language of a message text, then translates it.
     * Uses cached LanguageIdentifier for better performance.
     */
    private void identifyLanguageAndTranslate(MessegesAdapter.ViewHolder holder, String text) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener(languageCode -> {
                Log.d(TAG, "Detected language: " + languageCode);
                if (!languageCode.equals("und")) {
                    translateMessage(holder, languageCode, text);
                } else {
                    // Fallback to English if language is undetermined
                    translateMessage(holder, "en", text);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Language identification failed", e);
                // Show original text on failure
                holder.show_Massage.setText(text);
                holder.loading.setVisibility(View.GONE);
            });
    }

    /**
     * ML Kit: Translates a message from the detected source language to the device's locale.
     * Caches Translator instances per source language to avoid recreating them for every message.
     * Downloads translation models on-demand if not already available.
     */
    public void translateMessage(MessegesAdapter.ViewHolder holder, String sourceLang, String text) {
        String targetLang = Locale.getDefault().getLanguage();

        // Use cached translator if available, otherwise create and cache a new one
        String cacheKey = sourceLang + "_" + targetLang;
        Translator translator = translatorCache.get(cacheKey);

        if (translator == null) {
            String sourceLanguageTag = TranslateLanguage.fromLanguageTag(sourceLang);
            String targetLanguageTag = TranslateLanguage.fromLanguageTag(targetLang);

            if (sourceLanguageTag == null || targetLanguageTag == null) {
                Log.w(TAG, "Unsupported language: src=" + sourceLang + " tgt=" + targetLang);
                holder.show_Massage.setText(text);
                holder.loading.setVisibility(View.GONE);
                return;
            }

            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguageTag)
                    .setTargetLanguage(targetLanguageTag)
                    .build();
            translator = Translation.getClient(options);
            translatorCache.put(cacheKey, translator);
        }

        Translator finalTranslator = translator;
        finalTranslator.downloadModelIfNeeded()
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Model download failed", task.getException());
                    holder.show_Massage.setText(text);
                    holder.loading.setVisibility(View.GONE);
                    return;
                }
                finalTranslator.translate(text)
                    .addOnSuccessListener(translatedText -> {
                        holder.show_Massage.setText("  " + text + "\n" + "|: " + translatedText);
                        holder.loading.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Translation failed: " + e.getMessage());
                        holder.show_Massage.setText(text);
                        holder.loading.setVisibility(View.GONE);
                    });
            });
    }

    /**
     * Closes all ML Kit clients to free resources.
     * Should be called when the adapter is no longer needed.
     */
    public void close() {
        languageIdentifier.close();
        for (Translator translator : translatorCache.values()) {
            translator.close();
        }
        translatorCache.clear();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        close();
    }
}
