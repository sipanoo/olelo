package com.pro.st;


import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class FirstTimeMessage {
    private static final String TAG = "FirstTimeMessage";
    public static void sendFerstMessege(String sender, String recover, String messege) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(Constants.KEY_DATA).getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.KEY_SANDER_ID, sender);
        hashMap.put(Constants.KEY_RECEIVER_ID, recover);
        hashMap.put(Constants.KEY_MESSAGE, messege);
        hashMap.put("time", System.currentTimeMillis());
        databaseReference.child(Constants.KEY_CONVERSATIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(recover).child(Constants.KEY_Dating_Assistant).hasChild(Constants.KEY_FIRST_MESSAGE)) {

                    databaseReference.child(Constants.KEY_CONVERSATIONS).child(recover).child(Constants.KEY_Dating_Assistant).child(Constants.KEY_FIRST_MESSAGE).setValue(hashMap);
                    databaseReference.child(Constants.KEY_CONVERSATIONS).child(recover).child(Constants.KEY_Dating_Assistant).child(Constants.KEY_LAST_MESSAGE).setValue(messege);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
