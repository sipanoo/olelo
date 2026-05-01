package com.pro.st;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pro.st.Adapter.PhotoAdapter;
import com.pro.st.Model.User;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db;
    private ImageView image;
    private TextView name;
    private PhotoAdapter photoAdapter;
    private ArrayList<User> images;
    private RecyclerView recyclerView;
    private StorageReference listRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Null safety: redirect to login if user is not authenticated
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userid = firebaseUser.getUid();
        recyclerView = findViewById(R.id.my_Photos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(4);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));
        images = new ArrayList<>();
        listRef = storage.getReference().child(userid);
        user = FirebaseAuth.getInstance().getCurrentUser();
        name = findViewById(R.id.profile_name);
        image = findViewById(R.id.my_Image);

        findViewById(R.id.signout).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            startActivity(new Intent(ProfileActivity.this, FierstActivity.class));
            finishAffinity();

        });

        if (userid != null) {
            getAll();
            getAllPhotos();


        }
        findViewById(R.id.back_profile).setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, Afterlogin.class)));

        Toolbar toolbar = findViewById(R.id.my_profile_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.newPhoto) {

                Intent intent = new Intent(ProfileActivity.this, PhotoUpload.class);
                intent.putExtra("newPhoto", true);
                startActivity(intent);


            }
            return true;

        });


        findViewById(R.id.profil_details).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileDetails.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());


        });

        findViewById(R.id.contact_Us).setOnClickListener(v -> {

            Intent intent = new Intent(ProfileActivity.this, ContactUs.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());

        });



    }


    public void getAll() {

        db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS).document(user.getUid()).addSnapshotListener((value, error) -> {
            if (value != null) {
                User user = new User(value.getString(Constants.KEY_USER_NAME), value.getString(Constants.KEY_IMAGE_URL));
                name.setText(user.getName());


                Glide.with(getApplicationContext()).load(user.getImageURL()).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        image.setBackground(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }

        });


    }


    public void getAllPhotos() {
        images.clear();
        listRef.listAll().addOnSuccessListener(listResult -> {

            for (StorageReference item : listResult.getItems()) {
                if (!item.getName().equals("ProfilePhoto")) {

                    item.getDownloadUrl()
                            .addOnSuccessListener(uri -> {


                                User imageURL = new User(uri.toString());
                                images.add(imageURL);

                                photoAdapter = new PhotoAdapter(ProfileActivity.this, images, false);
                                recyclerView.setAdapter(photoAdapter);

                            });
                }

            }
            photoAdapter = new PhotoAdapter(ProfileActivity.this, images, false);
            recyclerView.setAdapter(photoAdapter);


        });


    }


}