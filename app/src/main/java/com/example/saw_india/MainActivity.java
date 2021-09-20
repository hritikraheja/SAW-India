package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.PaymentResultListener;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    static BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    final static Fragment feedsFragment = new FeedsFragment();
    final static Fragment searchNearbyFragment = new SearchNearbyFragment();
    final static Fragment needHelpFragment = new NeedHelpFragment();
    final static Fragment donateFragment = new DonationsFragment();
    final FragmentManager fragmentManager = getFragmentManager();
    static Fragment active = feedsFragment;
    Toolbar toolbar;
    int isFeedsFragmentLoaded;
    int b;
    int c;
    int d;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.complaints:
                Toast.makeText(getApplicationContext(), "This will show the number of active complaints", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//                startActivity(intent);
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
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SAW INDIA");
        toolbar.setLogo(R.drawable.logo_for_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        System.gc();

        fragmentManager.beginTransaction().add(R.id.frame, donateFragment, "4").hide(donateFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.frame, needHelpFragment, "3").hide(needHelpFragment).commit();
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
                            return true;
                        }
                        break;
                    case R.id.searchNearbyButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.searchNearbyButton) {
                            fragmentManager.beginTransaction().hide(active).show(searchNearbyFragment).commit();
                            active = searchNearbyFragment;
                            return true;
                        }
                        Toast.makeText(getApplicationContext(), "Search Nearby button clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.needHelpButton:
                        Toast.makeText(getApplicationContext(), "Need Help button clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.donateButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.donateButton) {
                            fragmentManager.beginTransaction().hide(active).show(donateFragment).commit();
                            active = donateFragment;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }


    void showDialog(Dialog dialog){dialog.show();}

    @Override
    public void onPaymentSuccess(String s) {
        try {
            Dialog successDialog = new Dialog(MainActivity.this);
            successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            successDialog.setCancelable(false);
            successDialog.setContentView(R.layout.layout_for_transaction_successful_dialog);
            successDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            Button continueButtonInSuccessDialog = successDialog.findViewById(R.id.continueButton);
            continueButtonInSuccessDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                }
            });
            showDialog(successDialog);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Session Aborted", Toast.LENGTH_SHORT).show();
    }
}