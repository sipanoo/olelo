package com.pro.st;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.st.fragment.ChatsFragment;
import com.pro.st.fragment.FindFragment;
import com.pro.st.fragment.MatchFragment;
import com.pro.st.R;



public class Afterlogin extends AppCompatActivity {
    private ChatsFragment chatsFragment;
    private FindFragment findFragment;
    private MatchFragment matchFragment;

    FirebaseFirestore db;
    String userid;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);

        // Null safety: redirect to login if user is not authenticated
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userid = firebaseUser.getUid();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setExitTransition(null);
        chatsFragment = new ChatsFragment();
        findFragment = new FindFragment();
        matchFragment = new MatchFragment();
        NavigationBarView bottomNavigationView = findViewById(R.id.main_Nav);
        db = FirebaseFirestore.getInstance();
        TextView textView = findViewById(R.id.fragmet_name);
        setFragment(matchFragment);
        textView.setText(R.string.text_Matching);
        Intent intent = getIntent();

        if (  intent.getBooleanExtra("backFromChat" , false)){ // default fragment

            setFragment(chatsFragment);
            textView.setText(getString(R.string.text_Chats));
        }

        FirstTimeMessage.sendFerstMessege(Constants.KEY_Dating_Assistant,userid , Constants.KEY_FIRST_START);
        bottomNavigationView.setOnItemSelectedListener(item -> {// switch fragment

            switch (item.getItemId()) {

                case  1000061:
                    setFragment(findFragment);
                    textView.setText(getString(R.string.text_Umgebung));
                    return true;
                case  1000023:
                    setFragment(chatsFragment);
                    textView.setText(getString(R.string.text_Chats));
                    return true;
                case  1000060:
                    setFragment(matchFragment);
                    textView.setText(getString(R.string.text_Matching));
                    return true;
                default:

                    return false;


            }
        });
        findViewById(R.id.my_profile).setOnClickListener(view -> startActivity(new Intent(Afterlogin.this , ProfileActivity.class)));




    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, fragment);
        fragmentTransaction.commit();


    }





}