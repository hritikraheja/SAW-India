package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int PLACE_PICKER_REQUEST_CODE = 3;

    Button makeDonationButton;
    static BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    final static Fragment feedsFragment = new FeedsFragment();
    final static Fragment searchNearbyFragment = new SearchNearbyFragment();
    final static Fragment needHelpFragment = new NeedHelpFragment();
    final static Fragment donateFragment = new DonationsFragment();
    final FragmentManager fragmentManager = getFragmentManager();
    static Fragment active = feedsFragment;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_nav_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.complaints:
//                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);
                CancellationTokenSource cts = new CancellationTokenSource();
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                Task<Location> location = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, cts.getToken());
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null){
                            Toast.makeText(getApplicationContext(), "Not Able To Get Location", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeDonationButton = findViewById(R.id.makeADonationButton);
        makeDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeADonationActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SAW INDIA");
        toolbar.setLogo(R.drawable.logo_for_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        System.gc();

        fragmentManager.beginTransaction().add(R.id.frame, donateFragment, "4").hide(donateFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, needHelpFragment, "3").hide(needHelpFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, searchNearbyFragment, "2").hide(searchNearbyFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, feedsFragment, "1").commit();

        frameLayout = findViewById(R.id.frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.feedsButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.feedsButton) {
                            fragmentManager.beginTransaction().hide(active).show(feedsFragment).commit();
                            active = feedsFragment;
                            makeDonationButton.setVisibility(View.INVISIBLE);
                            makeDonationButton.setClickable(false);
                            return true;
                        }
                        break;
                    case R.id.searchNearbyButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.searchNearbyButton) {
                            fragmentManager.beginTransaction().hide(active).show(searchNearbyFragment).commit();
                            active = searchNearbyFragment;
                            makeDonationButton.setVisibility(View.INVISIBLE);
                            makeDonationButton.setClickable(false);
                            return true;
                        }
                        Toast.makeText(getApplicationContext(), "Search Nearby button clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.needHelpButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.needHelpButton) {
                            fragmentManager.beginTransaction().hide(active).show(needHelpFragment).commit();
                            active = needHelpFragment;
                            makeDonationButton.setVisibility(View.INVISIBLE);
                            makeDonationButton.setClickable(false);
                            return true;
                        }
                        Toast.makeText(getApplicationContext(), "Need Help button clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.donateButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.donateButton) {
                            fragmentManager.beginTransaction().hide(active).show(donateFragment).commit();
                            active = donateFragment;
                            makeDonationButton.setVisibility(View.VISIBLE);
                            makeDonationButton.setClickable(true);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "Status");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Location", "Enabled");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Location", "Disabled");
    }
}