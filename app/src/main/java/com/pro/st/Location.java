package com.pro.st;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class Location extends AppCompatActivity {
    private static final String TAG = Location.class.getSimpleName();
    private android.location.Location lastLocation;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userid;

    {
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        isGooglePlayServicesAvailable(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        }
                );


        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {

            if (location != null) {
                lastLocation = location;
                getAddress();
            } else {
                Log.d(TAG, " requires the Google Play Store, but it is missing");

            }


        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {

            if (location != null) {
                lastLocation = location;
                getAddress();
            } else {
                Log.d(TAG, " requires the Google Play Store, but it is missing");

            }


        });



    }

    private void getAddress() {


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;


        try {

            addresses = geocoder.getFromLocation(
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude(),

                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.

            Log.e(TAG, "errorMessage", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.

            Log.e(TAG, "errorMessage" + ". " +
                    "Latitude = " + lastLocation.getLatitude() +
                    ", Longitude = " + lastLocation.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {


            Log.e(TAG, "errorMessage");


        } else {
            Address address = addresses.get(0);

            setLocation(address.getLocality());

        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void setLocation(String city) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("city", city);


        db.collection("Users").document(userid).update(data).addOnCompleteListener(task -> {
            Intent intent = new Intent(Location.this, Afterlogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });


    }


    public void isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                Objects.requireNonNull(googleApiAvailability.getErrorDialog(activity, status, 2404)).show();


            }

        }
    }


}