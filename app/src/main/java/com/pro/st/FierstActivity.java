package com.pro.st;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class FierstActivity extends AppCompatActivity {
    private long pressedTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        Animation logo = AnimationUtils.loadAnimation(this, R.anim.move);
        ImageView imageView = findViewById(R.id.imageView3);
        imageView.startAnimation(logo);

        findViewById(R.id.log_in).setOnClickListener(v -> startActivity(new Intent(FierstActivity.this, LoginActivity.class)));

        findViewById(R.id.sign_up).setOnClickListener(v -> startActivity(new Intent(FierstActivity.this, SignupActivity.class)));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}









