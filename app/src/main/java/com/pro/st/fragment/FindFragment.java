package com.pro.st.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pro.st.Adapter.FindAdapter;
import com.pro.st.Constants;
import com.pro.st.R;
import com.pro.st.Model.User;

import java.util.ArrayList;
import java.util.List;


public class FindFragment extends Fragment {
    private static final String TAG = "findFragment";
    private RecyclerView recyclerView;
    private FindAdapter findAdapter;
    private List<User> users;
    public FirebaseFirestore db;
    private View view;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        this.view = view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        users = new ArrayList<>();

        // Get current user for filtering
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

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
                            if (!document.getId().equals(Constants.KEY_Dating_Assistant) && !document.getId().equals(currentUserId)) {
                                User user = new User(document.getId(), document.getString(Constants.KEY_USER_NAME), document.getString(Constants.KEY_CITY), document.getString(Constants.KEY_IMAGE_URL));
                                users.add(user);
                            }
                        }

                        findAdapter = new FindAdapter(getContext(), users);
                        recyclerView.setAdapter(findAdapter);
                        view.findViewById(R.id.find_progress_bar).setVisibility(View.GONE);

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }).addOnFailureListener(e -> Log.w("Database", "error", e));

    }


}