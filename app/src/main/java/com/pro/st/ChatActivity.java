package com.pro.st;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.st.Adapter.MessegesAdapter;
import com.pro.st.Model.MessageItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private MessegesAdapter messegesAdapter;
    private   List<MessageItem> message_items;
    private RecyclerView recyclerView;
    private String recoverId;
    private String imageURL;
    private EditText textMessege;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);

        // Null safety: redirect to login if user is not authenticated
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userid = firebaseUser.getUid();

        getWindow().setBackgroundDrawableResource(R.drawable.chat_backgrund);
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.KEY_BIO_NAME);
        recoverId = intent.getStringExtra(Constants.KEY_BIO_ID);
        imageURL = intent.getStringExtra(Constants.KEY_BIO_IMAGE);
        TextView chatName = findViewById(R.id.chatName);
        chatName.setText(name);
        recyclerView = findViewById(R.id.messageslist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ImageButton sendButton = findViewById(R.id.button_send);
        textMessege = findViewById(R.id.text_send);

        sendButton.setOnClickListener(view -> {

            String messege = textMessege.getText().toString();

            if (!messege.isEmpty() ) {

                sendMessege(userid, recoverId, messege);

            } else {
                Toast.makeText(ChatActivity.this, "can't send messege", Toast.LENGTH_SHORT).show();

            }
            textMessege.setText("");


        });

        redMesseges(userid, recoverId, imageURL, false);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.translator) {
                redMesseges(userid, recoverId, imageURL, !messegesAdapter.trans);
            }
            return true;
        });



        findViewById(R.id.datingAs).setOnClickListener(v -> {
          Intent startAssistant =new Intent(ChatActivity.this , Assistant.class);

          startAssistant.putExtras(intent);
            startActivity(startAssistant);

        });

        findViewById(R.id.back_chat).setOnClickListener(v -> {


            Intent back = new Intent(ChatActivity.this , Afterlogin.class);
            back.putExtra("backFromChat" , true);

            startActivity(back);


        });



    }


    public void sendMessege(String sender, String recover, String messege) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(Constants.KEY_DATA).getReference();
        HashMap<String, Object> hashMap  = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("recover", recover);
        hashMap.put("textMessege", messege);
        hashMap.put("time", Time.getTime());
        databaseReference.child(Constants.KEY_CONVERSATIONS).child(userid).child(recoverId).push().setValue(hashMap);
        databaseReference.child(Constants.KEY_CONVERSATIONS).child(userid).child(recoverId).child(Constants.KEY_LAST_MESSAGE).setValue(messege);
        databaseReference.child(Constants.KEY_CONVERSATIONS).child(recoverId).child(userid).push().setValue(hashMap);
        databaseReference.child(Constants.KEY_CONVERSATIONS).child(recoverId).child(userid).child(Constants.KEY_LAST_MESSAGE).setValue(messege);


    }

    private void redMesseges(final String myid, final String recoverId, final String imageURL, boolean trans) {

        message_items = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance(Constants.KEY_DATA).getReference("Chats").child(userid);
        reference.child(recoverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message_items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals(Constants.KEY_LAST_MESSAGE)){
                        MessageItem messeges = dataSnapshot.getValue(MessageItem.class);
                        assert messeges != null;
                        if (messeges.getRecover().equals(myid) && messeges.getSender().equals(recoverId) || messeges.getRecover().equals(recoverId) && messeges.getSender().equals(myid)) {
                            message_items.add(messeges);
                        }
                        messegesAdapter = new MessegesAdapter(ChatActivity.this, message_items, imageURL, trans);
                        recyclerView.setAdapter(messegesAdapter);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read messages: " + error.getMessage());
            }
        });


    }













}