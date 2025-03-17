package com.pro.st;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById(R.id.back_arrow).setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, FierstActivity.class)));
         auth= FirebaseAuth.getInstance();
        EditText name, email, gender, password, conPassword;
        name = findViewById(R.id.FullName);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.con_password);
        findViewById(R.id.sign_up).setOnClickListener(view -> {
            String txt_name = name.getText().toString();
            String txt_email = email.getText().toString();
            String txt_gender = gender.getText().toString();
            String txt_password = password.getText().toString();
            String txt_conPassword = conPassword.getText().toString();

            if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_gender) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_conPassword)) {
                Toast.makeText(SignupActivity.this, "full all Fileds pls!", Toast.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6 || !txt_password.equals(txt_conPassword)) {
                Toast.makeText(SignupActivity.this, "Password not able", Toast.LENGTH_SHORT).show();
            } else if (!txt_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                Toast.makeText(SignupActivity.this, "check ur email.", Toast.LENGTH_SHORT).show();
            } else {
                signUp(txt_name, txt_email, txt_gender, txt_password);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void signUp(final String name, String email, String gender, String password) {


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String userid = firebaseUser.getUid();
               db = FirebaseFirestore.getInstance();

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("email", email);
                hashMap.put("password", password);
                hashMap.put("username", name);
                hashMap.put("gender", gender);
                hashMap.put("imageURL","default");

                db.collection("Users").document(userid).set(hashMap).addOnSuccessListener(documentReference -> {

                    Intent intent = new Intent(SignupActivity.this, Location.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }).addOnFailureListener(e -> Log.w("Database","error",e));

            } else {
                Toast.makeText(SignupActivity.this, "You can't Sign up", Toast.LENGTH_LONG).show();
            }


        });


    }


}