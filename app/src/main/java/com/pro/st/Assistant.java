package com.pro.st;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.Random;

public class Assistant extends AppCompatActivity {

    private List<MessageItem> message_items;
    private RecyclerView recyclerView;
    private String recoverId;
    private String imageURL;
    private ImageButton sendButton;
    private EditText textMessege;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String userid;

    {
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
    }

    private HashMap<Integer, String> Questions;
    private DatabaseReference reference;
    private boolean first = true;
    private String subject = "";
    private String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asstent);
        getWindow().setBackgroundDrawableResource(R.drawable.chat_backgrund);
        reference = FirebaseDatabase.getInstance(Constants.KEY_DATA).getReference(Constants.KEY_Dating_Assistant);
        Intent intent = getIntent();
        String name = intent.getStringExtra("bioName");
        recoverId = intent.getStringExtra("bioId");
        //imageURL = intent.getStringExtra("bioImage");
        Questions = new HashMap<>();
        TextView chatName = findViewById(R.id.chat_name_assistent);
        chatName.setText(Constants.KEY_Dating_Assistant);
        recyclerView = findViewById(R.id.messages_list_assistent);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        sendButton = findViewById(R.id.button_send_assistent);
        textMessege = findViewById(R.id.text_send_assistent);
        message_items = new ArrayList<>();
        getQuestions();
        checkQuestions();

        if (intent.getExtras() == null) { // check if the user wanna talk to Assistant
            assistantMessage("hello nice to meet you ");
            sendButton.setOnClickListener(v -> {
                if (!textMessege.getText().toString().isEmpty()) {
                    String message = textMessege.getText().toString();

                    addMessage(userid, textMessege.getText().toString());
                    textMessege.setText("");

                    assistantMessage(AssistantResponse.simpleResponse(message));
                }


            });


        } else {// check if the user come to talk about author user
            if (first) {
                assistantMessage("do you wanna help to start chating with " + name);

            }

            sendButton.setOnClickListener(v -> {
                String message = textMessege.getText().toString();
                if (!message.isEmpty()) {

                    addMessage(userid, textMessege.getText().toString());
                    textMessege.setText("");
                    assistantMessage(AssistantResponse.startConversation(message + " " + first));
                    first = false;
                    search = AssistantResponse.startConversation(message + " " + false);
                    if (search.equals("startSearch") && !subject.isEmpty()) {
                        search(subject);
                    }
                    if (!Questions.isEmpty()) {
                        startAsking();

                    } else {
                        new Handler(Looper.myLooper()).postDelayed(() -> {
                            assistantMessage("mmm..., never mind");
                            getAnswers();
                        }, 700);


                    }
                }


            });


        }

    }

    private int i = 0;


    // start to Ask the Questions
    private void startAsking() {

        if (Questions.get(i) == null) {

            assistantMessage("Thanks");
            getAnswers();

        } else {

            assistantMessage(Questions.get(i));
            sendButton.setOnClickListener(v -> {

                setAnswers(i, textMessege.getText().toString());
                addMessage(userid, textMessege.getText().toString());

                textMessege.setText("");

                i++;
                startAsking();
            });


        }


    }

    //set Assistants Responses in the Chat
    public void assistantMessage(String message) {


        new Handler(Looper.myLooper()).postDelayed(() -> addMessage(Constants.KEY_Dating_Assistant, message), 700);

    }


    //add Messages to the chat
    public void addMessage(String sender, String message) {

        if (sender.equals(userid)) {
            message_items.add(new MessageItem(userid, Constants.KEY_Dating_Assistant, message));

        } else {
            message_items.add(new MessageItem(Constants.KEY_Dating_Assistant, userid, message));
        }

        MessegesAdapter messegesAdapter = new MessegesAdapter(Assistant.this, message_items, "Assistant", false);
        recyclerView.setAdapter(messegesAdapter);

    }

    //make a list of the user Answers
    private void setAnswers(int i, String data) {

        reference.child(Constants.KEY_USERS_ANSWORS).child(userid).child(String.valueOf(i)).setValue(data).addOnSuccessListener(unused -> {


        });

    }

    //check if the Questions has been answered
    private void checkQuestions() {
        reference.child(Constants.KEY_USERS_ANSWORS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    for (DataSnapshot data : snapshot.child(userid).getChildren()) {
                        int d = Integer.valueOf(data.getKey());

                        Questions.remove(d);
                        i = d + 1;


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // get Questions to ask the user
    private void getQuestions() {
        Questions.clear();
        reference.child(Constants.KEY_Questions).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("questionss ", "QuestionNr : " + data.getKey() + ",  question : " + data.getValue());
                    Questions.put(Integer.valueOf(data.getKey()), data.getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //get subject to talk about
    private void getAnswers() {
        reference.child(Constants.KEY_USERS_ANSWORS).child(recoverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {

                    int i = (int) snapshot.getChildrenCount();
                    int random = new Random().nextInt(i);
                    new Handler(Looper.myLooper()).postDelayed(() -> assistantMessage("try to speak about " + snapshot.child(String.valueOf(random)).getValue()), 700);
                    subject = snapshot.child(String.valueOf(random)).getValue().toString();
                    new Handler(Looper.myLooper()).postDelayed(() -> assistantMessage("do you want more Info about " + subject + " ?"), 1400);


                }else{
                    new Handler(Looper.myLooper()).postDelayed(() -> assistantMessage("try to talk about Animals"), 1400);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //searching about the subject in youtube
    public void search(String subject) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + subject));
        startActivity(intent);


    }


}