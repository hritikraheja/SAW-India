package com.example.saw_india;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.LogoutBottomSheetDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Button makeDonationButton;
    static BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    final static Fragment feedsFragment = new FeedsFragment();
    final static Fragment searchNearbyFragment = new SearchNearbyFragment();
    final static Fragment needHelpFragment = new NeedHelpFragment();
    final static Fragment donateFragment = new DonationsFragment();
    final FragmentManager fragmentManager = getFragmentManager();
    static Fragment active = feedsFragment;
    static androidx.fragment.app.FragmentManager supportFragmentManager;
    Toolbar toolbar;
    public static DrawerLayout drawerLayout;
    public static NavigationView navView;

    public static MenuItem loginButton;
    public static TextView loggedInUserNameTV;
    public static TextView loggedInUserMobileNumberTV;
    public static TextView getLoggedInUserEmailTV;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.complaints:
                Intent intent = new Intent(MainActivity.this, ActiveComplaintsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
        LoginCredentials.name = sharedPreferences.getString("loggedInUserName", null);
        LoginCredentials.mobileNumber = sharedPreferences.getString("loggedInUserMobileNumber", null);
        LoginCredentials.email = sharedPreferences.getString("loggedInUserEmail", "");
        if ((LoginCredentials.mobileNumber != null)){
            loggedInUserMobileNumberTV.setText(LoginCredentials.mobileNumber);
            loggedInUserNameTV.setText(LoginCredentials.name);
            if (LoginCredentials.email != null){
                getLoggedInUserEmailTV.setText(LoginCredentials.email);
            } else {
                getLoggedInUserEmailTV.setText("");
            }
            loggedInUserNameTV.setTextSize(15);
            MainActivity.loginButton.setTitle("LOGOUT");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loggedInUserName", LoginCredentials.name);
        editor.putString("loggedInUserMobileNumber", LoginCredentials.mobileNumber);
        editor.putString("loggedInUserEmail", LoginCredentials.email);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();
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
        drawerLayout = findViewById(R.id.drawerLayout);

        navView = findViewById(R.id.navigation);
        loginButton = navView.getMenu().findItem(R.id.navBarLoginButton);
        loggedInUserNameTV = navView.getHeaderView(0).findViewById(R.id.loginName);
        loggedInUserMobileNumberTV  = navView.getHeaderView(0).findViewById(R.id.loginPhoneNumber);
        getLoggedInUserEmailTV = navView.getHeaderView(0).findViewById(R.id.loginEmail);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.navBarRequestsButton:
                        Intent i1 = new Intent(MainActivity.this, ComplaintHistoryActivity.class);
                        startActivity(i1);
                        return true;
                    case R.id.navBarDonationsButton:
                        Intent i2 = new Intent(MainActivity.this, DonationsHistoryActivity.class);
                        startActivity(i2);
                        return true;
                    case R.id.navBarAdoptButton:
                        Toast.makeText(getApplicationContext(),"Adopt Clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navBarLoginButton:
                            LogoutBottomSheetDialog bottomSheetDialog = new LogoutBottomSheetDialog();
                            bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
                        return true;
                    case R.id.navBarShareButton:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareSub = "Saw India is working to make a single platform for all animal welfare organisations across the whole country. We will be delighted if you our services a try. No stray animal will be left to die without human care.";
                        String shareBody = "Download Now!!! \n " + shareSub;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        return true;
                    case R.id.navBarRateButton:
                        Toast.makeText(getApplicationContext(),"This will lead you to PlayStore download when the app is published", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navBarSuggestButton:
                        try{
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:"));
                            String[] to = new String[]{"hritikraheja27@gmail.com"};
                            intent.putExtra(Intent.EXTRA_EMAIL, to);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions");
                            startActivity(Intent.createChooser(intent, "Send Email : "));
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });
        navView.setCheckedItem(R.id.navBarHomeButton);

        toolbar.setTitle("SAW INDIA");
        toolbar.setLogo(R.drawable.logo_for_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(true);
        System.gc();

        fragmentManager.beginTransaction().add(R.id.frame, donateFragment, "4").hide(donateFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, needHelpFragment, "3").hide(needHelpFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, searchNearbyFragment, "2").hide(searchNearbyFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame, feedsFragment, "1").commit();

        frameLayout = findViewById(R.id.frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
                        break;
                    case R.id.needHelpButton:
                        if (bottomNavigationView.getSelectedItemId() != R.id.needHelpButton) {
                                fragmentManager.beginTransaction().hide(active).show(needHelpFragment).commit();
                                active = needHelpFragment;
                                makeDonationButton.setVisibility(View.INVISIBLE);
                                makeDonationButton.setClickable(false);
                            return true;
                        }
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
}