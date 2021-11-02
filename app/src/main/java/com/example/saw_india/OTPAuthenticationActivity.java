package com.example.saw_india;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.User;
import com.example.saw_india.modalClasses.UsersDatabaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hololo.library.otpview.OTPView;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class OTPAuthenticationActivity extends AppCompatActivity {

    String mobileNumber;
    String countryCode;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    FirebaseAuth mAuth;
    Button verifyButton;
    OTPView otpView;
    ProgressBar progressBar;
    ImageView backButton;
    private String verificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_authentication);

        mAuth = FirebaseAuth.getInstance();
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                final String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    otpView.setOtp(code);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(OTPAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        Intent receivingIntent = getIntent();
        mobileNumber = receivingIntent.getStringExtra("mobileNumber");
        countryCode = receivingIntent.getStringExtra("countryCode");
        verifyButton = findViewById(R.id.verifyButton);
        otpView = findViewById(R.id.otpView);
        otpView.setFocusableInTouchMode(false);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        t1.setText("Verification In Progress. Enter the OTP sent to \n+" + countryCode + " " + mobileNumber);
        final String fullMobileNumber = "+" + countryCode + mobileNumber;
        sendVerificationCode(fullMobileNumber);
        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                t3.setText("(00:" + (int) (millisUntilFinished / 1000 + 1) + ")");
            }

            @Override
            public void onFinish() {
                t2.setText("Didn't received OTP?");
                t2.setTextSize(20);
                t3.setVisibility(View.INVISIBLE);
                t3.setClickable(false);
                t4.setVisibility(View.VISIBLE);
                t4.setClickable(true);
                t4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendVerificationCode(fullMobileNumber);
                    }
                });
            }
        };

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpView.getOtp();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(OTPAuthenticationActivity.this, "Code Cannot Be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(code);
                }
            }
        });
        countDownTimer.start();
    }

    private void sendVerificationCode(String number) {
        try {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)
                            .setTimeout(30L, TimeUnit.SECONDS)
                            .setActivity(this)
                            .setCallbacks(mCallBack)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            Toast.makeText(OTPAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
            Toast.makeText(OTPAuthenticationActivity.this, "Wait....", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(OTPAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UsersDatabaseHandler databaseHandler = new UsersDatabaseHandler();
                            databaseHandler.getDatabaseByMobileNumber(mobileNumber).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    LinkedList<User> a = new LinkedList<>();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        User user = dataSnapshot.getValue(User.class);
                                        a.add(user);
                                    }
                                    if (a.size() == 0) {
                                        Intent i = new Intent(OTPAuthenticationActivity.this, LoginNameAndEmailActivity.class);
                                        i.putExtra("mobileNumber", mobileNumber);
                                        i.putExtra("countryCode", countryCode);
                                        startActivity(i);
                                    } else {
                                        LoginCredentials.name = a.get(0).getName();
                                        LoginCredentials.mobileNumber = a.get(0).getMobileNumber();
                                        LoginCredentials.email = a.get(0).getEmail();
                                        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("loggedInUserName", LoginCredentials.name);
                                        editor.putString("loggedInUserMobileNumber", LoginCredentials.mobileNumber);
                                        editor.putString("loggedInUserEmail", LoginCredentials.email);
                                        editor.apply();
                                        Intent i = new Intent(OTPAuthenticationActivity.this, MainActivity.class);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("Error", error.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(OTPAuthenticationActivity.this, "INVALID OTP", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}