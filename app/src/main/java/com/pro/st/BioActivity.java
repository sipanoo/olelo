package com.pro.st;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pro.st.Adapter.PhotoAdapter;
import com.pro.st.Model.User;

import java.util.ArrayList;


public class BioActivity extends AppCompatActivity {
    private static final String TAG = "BioActivity";

    private TextView bioDetail;
    private PhotoAdapter photoAdapter;
    private ArrayList<User> images;
    private RecyclerView recyclerView;
    private StorageReference listRef;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        ImageView image = findViewById(R.id.bioImage);
        Intent intent = getIntent();
        String bioId = intent.getStringExtra("bioId");
        String bioName = intent.getStringExtra("bioName");
        String bioImage = intent.getStringExtra("bioImage");
        TextView bioName1 = findViewById(R.id.bioname);
        bioDetail = findViewById(R.id.bioDetail);
        bioName1.setText(bioName);
        TextView interested = findViewById(R.id.interested);
        recyclerView = findViewById(R.id.bio_Photos);
        TextView relationship = findViewById(R.id.relationship);
        TextView education = findViewById(R.id.education);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(4);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(BioActivity.this, LinearLayoutManager.HORIZONTAL, false));
        images = new ArrayList<>();
        listRef = storage.getReference().child(bioId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.KEY_COLLECTION_USERS).document(bioId).collection(Constants.KEY_COLLECTION_ID).document("myYEzPOfGKRAJiKWTiR0").addSnapshotListener((value, error) -> {

            bioDetail.setText(value.getString("Bio"));
            interested.setText("Interested In \n" +value.getString("interestedIn"));
            education.setText(" Education \n" + value.getString("education"));
            relationship.setText("Relationship \n" + value.getString("relationship") );

        });

        findViewById(R.id.back_bio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BioActivity.this , Afterlogin.class));
            }
        });


        if (bioImage.equals("default")) {

            image.setImageResource(R.drawable.users_default_image);


        } else {

            Glide.with(BioActivity.this).load(bioImage).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    image.setBackground(resource);

                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

        findViewById(R.id.chat_button).setOnClickListener(view -> {

            Intent chatIntent = new Intent(this, ChatActivity.class);
            chatIntent.putExtras(intent);
            startActivity(chatIntent);


        });
        getAllPhotos();

    }


    public void getAllPhotos() {  // to get all user favorites photo
        images.clear();
        listRef.listAll().addOnSuccessListener(listResult -> {

            for (StorageReference item : listResult.getItems()) {
                if (!item.getName().equals("ProfilePhoto")) {

                    item.getDownloadUrl()
                            .addOnSuccessListener(uri -> {


                                User imageURL = new User(uri.toString());
                                images.add(imageURL);

                                photoAdapter = new PhotoAdapter(BioActivity.this, images, true);
                                recyclerView.setAdapter(photoAdapter);

                            });
                }

            }


        });


    }


}