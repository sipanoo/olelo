package com.pro.st.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.pro.st.Adapter.MatchAdapter;
import com.pro.st.BioActivity;
import com.pro.st.Constants;
import com.pro.st.Model.User;
import com.pro.st.R;

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {

    private static final String TAG = "MatchFragment";

    public FirebaseUser firebaseUser;
    public SwipeFlingAdapterView flingContainer;
    public MatchAdapter matchAdapter;
    public List<User> users;
    public FirebaseFirestore db;
    String id;
    String name;
    String img;
    Context context;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_match, container, false);
        this.view = view;
        flingContainer = view.findViewById(R.id.frame);

        // Get current user for filtering
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            id = currentUser.getUid();
        }

        users = new ArrayList<>();

        getAllUsers();

        return view;
    }


    public void getAllUsers() {

        db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        if (!document.getId().equals(Constants.KEY_Dating_Assistant) && !document.getId().equals(id)){
                            User user = new User(document.getId(), document.getString(Constants.KEY_USER_NAME), document.getString(Constants.KEY_CITY), document.getString(Constants.KEY_IMAGE_URL));
                            users.add(user);

                        }


                        }
                        matchAdapter = new MatchAdapter(context, users);
                        flingContainer.setAdapter(matchAdapter);
                        matchAdapter.notifyDataSetChanged();
                        updateSwipeCard();
                        view.findViewById(R.id.match_progress_bar).setVisibility(View.GONE);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }).addOnFailureListener(e -> Log.w("Database", "error", e));
    }


    private void updateSwipeCard() {


        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                users.remove(0);
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                User obj = (User) dataObject;
                id = obj.getUserId();
                name = obj.getName();
                img = obj.getImageURL();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                User obj = (User) dataObject;
                id = obj.getUserId();
                name = obj.getName();
                img = obj.getImageURL();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.like).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.dislike).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
        flingContainer.setOnItemClickListener((itemPosition, dataObject) -> {

            User obj = (User) dataObject;
            id = obj.getUserId();
            name = obj.getName();
            img = obj.getImageURL();
            Log.d(TAG, "userId " + obj.getUserId() + "   Name   " + obj.getName() + "Image " + obj.getImageURL());
            Intent intent = new Intent(getActivity(), BioActivity.class);
            intent.putExtra("bioId", id);
            intent.putExtra("bioName", name);
            intent.putExtra("bioImage", img);

            startActivity(intent);

        });

    }


}