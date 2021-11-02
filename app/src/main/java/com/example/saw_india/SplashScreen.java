package com.example.saw_india;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saw_india.modalClasses.LoginCredentials;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = findViewById(R.id.logo);
        title = findViewById(R.id.t1);
        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
        LoginCredentials.name = sharedPreferences.getString("loggedInUserName", null);
        LoginCredentials.mobileNumber = sharedPreferences.getString("loggedInUserMobileNumber", null);
        LoginCredentials.email = sharedPreferences.getString("loggedInUserEmail", "");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(LoginCredentials.mobileNumber != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
}