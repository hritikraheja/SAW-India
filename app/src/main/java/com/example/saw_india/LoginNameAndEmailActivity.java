package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saw_india.modalClasses.User;
import com.example.saw_india.modalClasses.UsersDatabaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.UserDataHandler;

public class LoginNameAndEmailActivity extends AppCompatActivity {

    ImageView backButton;
    EditText nameEditText;
    EditText emailEditText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name_and_email);
        Intent receivingIntent = getIntent();
        final String mobileNumber = receivingIntent.getStringExtra("mobileNumber");
        String countryCode = receivingIntent.getStringExtra("countryCode");
        final UsersDatabaseHandler databaseHandler = new UsersDatabaseHandler();
        backButton = findViewById(R.id.backButton);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerButton = findViewById(R.id.registerButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Name field cannot be empty.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(email) &&(!Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.isEmpty()){
                        email = null;
                    }
                    User newUser = new User(name, mobileNumber, email);
                    final String finalEmail = email;
                    databaseHandler.addUser(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            MainActivity.loggedInUserName = name;
                            MainActivity.loggedInUserMobileNumber = mobileNumber;
                            MainActivity.getLoggedInUserEmail = finalEmail;
                            MainActivity.loggedInUserNameTV.setText(name);
                            MainActivity.loggedInUserMobileNumberTV.setText(mobileNumber);
                            if (finalEmail != null){
                                MainActivity.getLoggedInUserEmailTV.setText(finalEmail);
                            } else {
                                MainActivity.getLoggedInUserEmailTV.setText("");
                            }
                            MainActivity.loggedInUserNameTV.setTextSize(15);
                            SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUserName", MainActivity.loggedInUserName);
                            editor.putString("loggedInUserMobileNumber", MainActivity.loggedInUserMobileNumber);
                            editor.putString("loggedInUserEmail", MainActivity.getLoggedInUserEmail);
                            editor.apply();
                            MainActivity.drawerLayout.closeDrawer(GravityCompat.START);
                            Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            }
        });
    }
}