package com.pro.st.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pro.st.Adapter.ChatsAdapter;
import com.pro.st.Constants;
import com.pro.st.Model.ChatItem;
import com.pro.st.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private RecyclerView recyclerView;
    private ChatsAdapter chatsAdapter;
    private List<ChatItem> chat_items;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String userid=firebaseUser.getUid();
    private View view;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recylerViewChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chat_items = new ArrayList<>();

        getAllChats();
        this.view = view;
        return view;


    }

    public void getAllChats() {
        List<String> ides = new ArrayList<>();
        //"https://olelo-84b3f-default-rtdb.europe-west1.firebasedatabase.app"
        DatabaseReference reference = FirebaseDatabase.getInstance(Constants.KEY_DATA).getReference(Constants.KEY_CONVERSATIONS).child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    getAll(dataSnapshot.getKey() , dataSnapshot.child(Constants.KEY_LAST_MESSAGE).getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void getAll(String user,String lastmessage) {

        chat_items.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (Objects.equals(document.getString(Constants.KEY_USER_ID), user)) {
                                ChatItem chats = new ChatItem(user, document.getString(Constants.KEY_USER_NAME), document.getString(Constants.KEY_IMAGE_URL), lastmessage);
                                chat_items.add(chats);


                            }

                        }


                        chatsAdapter = new ChatsAdapter(getActivity(), chat_items);
                        recyclerView.setAdapter(chatsAdapter);
                        view.findViewById(R.id.chat_progress_bar).setVisibility(View.GONE);
                    }
                }).addOnFailureListener(e -> Log.w("Database", "error", e));


    }


}