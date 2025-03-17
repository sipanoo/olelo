package com.pro.st;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    FirebaseAuth auth;
    EditText email, password;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setEnterTransition(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById(R.id.back_arrow).setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, FierstActivity.class)));

        findViewById(R.id.facebook_login).setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, FacebookActivity.class)));

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        logIn = findViewById(R.id.Log_in);

        logIn.setOnClickListener(view -> {
            String email_txt = email.getText().toString();
            String password_txt = password.getText().toString();

            if (email_txt.isEmpty() || password_txt.isEmpty()) {
                Toast.makeText(LoginActivity.this, "please Enter email and password", Toast.LENGTH_SHORT).show();


            } else {

                auth.signInWithEmailAndPassword(email_txt, password_txt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, Afterlogin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Can't Login", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }


        });

    }


}