package com.pro.st;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class PhotoUpload extends AppCompatActivity {

    private Button btnSelect, btnUpload;
    private ImageButton backbutton;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userid;
    String photoName;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        // Null safety: redirect to login if user is not authenticated
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userid = firebaseUser.getUid();

        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnSelect.setOnClickListener(v -> SelectImage());
        btnUpload.setOnClickListener(v -> uploadImage());
        backbutton = findViewById(R.id.back_to_profile);

        backbutton.setOnClickListener(v -> {
            Intent back = new Intent(PhotoUpload.this, ProfileActivity.class);
            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(back);

        });


        Intent intent = getIntent();
        if (intent.getBooleanExtra("newPhoto", false)) {
            photoName = "ProfilePhoto";


        } else {

            photoName = UUID.randomUUID().toString();


        }

    }


    private void SelectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);


        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {


            filePath = data.getData();
            try {


                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null) {


            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref
                    = storageReference
                    .child(
                            userid + "/"
                                    + photoName);


            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {


                                progressDialog.dismiss();
                                Toast
                                        .makeText(PhotoUpload.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                changePhotoUrl();

                            })

                    .addOnFailureListener(e -> {


                        progressDialog.dismiss();
                        Toast
                                .makeText(PhotoUpload.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");


                            });
        }
    }


    public void changePhotoUrl() {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        if (photoName.equals("ProfilePhoto")) {
            storageReference.child(userid).listAll().addOnSuccessListener(listResult -> {

                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().equals("ProfilePhoto")) {

                        item.getDownloadUrl().addOnSuccessListener(uri -> {

                            url = uri.toString();
                            db.collection("Users").document(userid).update(Constants.KEY_IMAGE_URL, uri.toString());


                        });


                    }


                }


            });

        }

        backbutton.performClick();

    }


}