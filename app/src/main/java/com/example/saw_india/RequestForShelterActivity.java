package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.ShelterRequest;
import com.example.saw_india.modalClasses.ShelterRequestsDatabaseHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vanillaplacepicker.data.VanillaAddress;
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;
import com.vanillaplacepicker.utils.KeyUtils;
import com.vanillaplacepicker.utils.MapType;
import com.vanillaplacepicker.utils.PickerLanguage;
import com.vanillaplacepicker.utils.PickerType;

import java.util.LinkedList;

public class RequestForShelterActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLACE_PICKER_REQUEST_CODE = 1;
    MapView map;
    ImageView locateButton;
    CheckBox checkbox;
    Button submitButton;
    ImageView backButton;
    GoogleMap googleMap;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_shelter);
        backButton = findViewById(R.id.backButton);
        map = findViewById(R.id.map);
        checkbox = findViewById(R.id.checkbox);
        submitButton = findViewById(R.id.submitButton);
        locateButton = findViewById(R.id.locateButton);
        latitude = SearchNearbyFragment.myLat;
        longitude = SearchNearbyFragment.myLng;

        map.onCreate(savedInstanceState);
        if ((longitude == 0) && (latitude == 0)){
            Places.initialize(getApplicationContext(), getString(R.string.GOOGLE_PLACES_API_KEY));
            final FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            @SuppressLint("MissingPermission") final Task<Location> location = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationTokenSource().getToken());
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location myLocation = task.getResult();
                        if (myLocation==null){
                            Toast.makeText(getApplicationContext(),"Unable To Fetch Your Last Known Location", Toast.LENGTH_SHORT).show();
                        } else {
                            SearchNearbyFragment.myLat = myLocation.getLatitude();
                            SearchNearbyFragment.myLng = myLocation.getLongitude();
                            latitude = myLocation.getLatitude();
                            longitude = myLocation.getLongitude();
                            map.getMapAsync(new OnMapReadyCallback() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onMapReady(@NonNull final GoogleMap gMap) {
                                    googleMap = gMap;
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                                            LatLng location = new LatLng(latitude, longitude);
                                            googleMap.addMarker(new MarkerOptions().position(location).title("Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                                            googleMap.getUiSettings().setScrollGesturesEnabled(false);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        } else {
            map.getMapAsync(this);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = SearchNearbyFragment.myLat;
                double lng = SearchNearbyFragment.myLng;
                Intent intent = new VanillaPlacePicker.Builder(v.getContext())
                        .with(PickerType.MAP)
                        .withLocation(lat, lng)
                        .setPickerLanguage(PickerLanguage.ENGLISH)
                        .setCountry("IN")
                        .enableShowMapAfterSearchResult(true)
                        .setMapType(MapType.NORMAL)
                        .setMapPinDrawable(R.drawable.pin)
                        .build();

                startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkbox.isChecked()){
                    Toast.makeText(v.getContext(), "Please Read The Checkbox And Tick It.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wait...", Toast.LENGTH_SHORT).show();
                    final ShelterRequest shelterRequest = new ShelterRequest(LoginCredentials.name,
                            LoginCredentials.mobileNumber,
                            LoginCredentials.email,
                            latitude,
                            longitude);
                    final ShelterRequestsDatabaseHandler databaseHandler = new ShelterRequestsDatabaseHandler();
                    databaseHandler.getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final LinkedList<ShelterRequest> shelterRequests = new LinkedList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ShelterRequest request = dataSnapshot.getValue(ShelterRequest.class);
                                shelterRequests.add(request);
                            }
                            if (shelterRequests.size() != 0){
                                Toast.makeText(getApplicationContext(), "Only one shelter request can be placed with one account.", Toast.LENGTH_SHORT).show();
                                checkbox.setChecked(false);
                            } else {
                                databaseHandler.addShelterRequest(shelterRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Request submitted successfully", Toast.LENGTH_SHORT).show();
                                        checkbox.setChecked(false);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            VanillaAddress address = (VanillaAddress) data.getSerializableExtra(KeyUtils.SELECTED_PLACE);
            LatLng addressLocation = new LatLng(address.getLatitude(), address.getLongitude());
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(addressLocation).title("Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}