package com.pro.st;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplishScreen extends AppCompatActivity {

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splish_screen);
        getWindow().setExitTransition(null);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_logo);
        ImageView image = findViewById(R.id.logowhite);
        image.startAnimation(animation);
        TextView textView = findViewById(R.id.textlogo);
        textView.startAnimation(animation);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.text_logo);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.zoomout_logo);
        new Handler().postDelayed(() -> {

            ImageView logowhite = findViewById(R.id.logowhite);
            ImageView logow = findViewById(R.id.logow);
            ImageView logopink = findViewById(R.id.logopink);
            logow.setVisibility(View.VISIBLE);
            logopink.setVisibility(View.VISIBLE);
            logow.startAnimation(animation1);
            logowhite.startAnimation(animation2);
            logowhite.setVisibility(View.GONE);
            TextView textView1 = findViewById(R.id.textlogo);
            textView1.setVisibility(View.GONE);
            next();

        }, 2000);


    }





    private void next() {

        new Handler().postDelayed(() -> {

            Intent intent;
            if (firebaseUser != null) {

                intent = new Intent(SplishScreen.this, Location.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {

                intent = new Intent(SplishScreen.this, FierstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplishScreen.this).toBundle());
            }



        }, 600);


    }


}