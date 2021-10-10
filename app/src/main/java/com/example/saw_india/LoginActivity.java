package com.example.saw_india;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    ImageView backButton;
    EditText phoneNumberEditText;
    Button nextButton;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ccp = findViewById(R.id.ccp);
        backButton = findViewById(R.id.backButton);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        nextButton = findViewById(R.id.nextButton);
        ccp.setDefaultCountryUsingPhoneCode(91);
        ccp.hideNameCode(true);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = phoneNumberEditText.getText().toString();
                String countryCode = ccp.getSelectedCountryCode();
                if (TextUtils.isEmpty(mobileNumber)){
                    Toast.makeText(getApplicationContext(), "Enter your mobile number.", Toast.LENGTH_SHORT).show();
                } else if (mobileNumber.length()!=10){
                    Toast.makeText(getApplicationContext(), "Enter a valid 10-digit mobile number.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, OTPAuthenticationActivity.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    intent.putExtra("countryCode", countryCode);
                    startActivity(intent);
                }
            }
        });
    }
}