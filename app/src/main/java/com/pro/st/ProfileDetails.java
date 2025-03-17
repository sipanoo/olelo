package com.pro.st;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProfileDetails extends AppCompatActivity {


    FirebaseFirestore db;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    String userid;

    {
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        EditText addBio, birthday,interestedIn, education, relationship;
        addBio = findViewById(R.id.add_bio);
        birthday = findViewById(R.id.birthday);
        interestedIn = findViewById(R.id.interested_in);
        education = findViewById(R.id.education);
        relationship = findViewById(R.id.relationship);
        findViewById(R.id.save).setOnClickListener(view -> {
            String txt_bio = addBio.getText().toString();
            String txt_birthday = birthday.getText().toString();
            String txt_intersted_in = interestedIn.getText().toString();
            String txt_education = education.getText().toString();
            String txt_relationship = relationship.getText().toString();

            if (TextUtils.isEmpty(txt_bio) || TextUtils.isEmpty(txt_birthday) || TextUtils.isEmpty(txt_intersted_in) || TextUtils.isEmpty(txt_education) || TextUtils.isEmpty(txt_relationship)) {
                Toast.makeText(ProfileDetails.this, "full all Fileds pls!", Toast.LENGTH_SHORT).show();
            }else {

                saveDetails(txt_bio,txt_birthday,txt_intersted_in,txt_education,txt_relationship);

            }

        });


    }





    public void saveDetails( String addBio,String  birthday,String interestedIn, String education, String relationship) {

        db = FirebaseFirestore.getInstance();


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put( "bio",addBio);
        hashMap.put("birthday", birthday);
        hashMap.put("interestedIn" , interestedIn);
        hashMap.put("education" , education);
        hashMap.put("relationship", relationship);

        db.collection(Constants.KEY_COLLECTION_USERS).document(userid).collection(Constants.KEY_COLLECTION_ID).document("myYEzPOfGKRAJiKWTiR0").set(hashMap).addOnCompleteListener(documentReference-> {

            Toast.makeText(ProfileDetails.this, "adding is completed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileDetails.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ProfileDetails.this).toBundle());

        });


    }









}