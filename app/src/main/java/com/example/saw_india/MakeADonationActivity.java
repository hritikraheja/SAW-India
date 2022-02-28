package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saw_india.modalClasses.Donation;
import com.example.saw_india.modalClasses.DonationsDatabaseHandler;
import com.example.saw_india.modalClasses.LoginCredentials;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MakeADonationActivity extends AppCompatActivity implements PaymentResultListener {

    ImageView backButton;
    EditText nameEditText;
    EditText mobileNumberEditText;
    EditText emailEditText;
    EditText amountEditText;
    Button donateButton;
    Button amountButton11;
    Button amountButton21;
    Button amountButton51;
    Button amountButton101;

    public static String name;
    public static String mobileNumber;
    public static String email;
    String amountInString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_donation);

        backButton = findViewById(R.id.backButton);
        nameEditText = findViewById(R.id.nameEditText);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        emailEditText = findViewById(R.id.emailEditText);
        amountEditText = findViewById(R.id.amountEditText);
        donateButton = findViewById(R.id.donateButton);
        amountButton11 = findViewById(R.id.amountButton11);
        amountButton21 = findViewById(R.id.amountButton21);
        amountButton51 = findViewById(R.id.amountButton51);
        amountButton101 = findViewById(R.id.amountButton101);

        if (LoginCredentials.mobileNumber != null){
            nameEditText.setText(LoginCredentials.name);
            mobileNumberEditText.setText(LoginCredentials.mobileNumber);
            if (LoginCredentials.email != null){
                emailEditText.setText(LoginCredentials.email);
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        amountButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountEditText.setText("11");
            }
        });

        amountButton21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountEditText.setText("21");
            }
        });

        amountButton51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountEditText.setText("51");
            }
        });

        amountButton101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountEditText.setText("101");
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    name = nameEditText.getText().toString();
                    mobileNumber = mobileNumberEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    amountInString = amountEditText.getText().toString();
                    int amount = Math.round(Float.parseFloat(amountInString) * 100);
                    Checkout checkout = new Checkout();
                    checkout.setKeyID(getString(R.string.RAZORPAY_API_KEY));
                    checkout.setImage(R.drawable.final_logo_saw);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", "SAW, INDIA");
                        object.put("desciption", "Every drop makes an ocean what it is. Thanks for your help!");
                        object.put("theme.color", "#FF8C00");
                        object.put("amount", amount);
                        JSONObject preFill = new JSONObject();
                        preFill.put("email", email);
                        preFill.put("contact", mobileNumber);
                        preFill.put("name", name);
                        object.put("prefill", preFill);
                        object.put("currency", "INR");
                        checkout.open(MakeADonationActivity.this, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Enter All Details Carefully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void showDialog(Dialog dialog){dialog.show();}

    @Override
    public void onPaymentSuccess(final String paymentId) {
        try {
            final Dialog successDialog = new Dialog(MakeADonationActivity.this);
            successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            successDialog.setCancelable(false);
            successDialog.setContentView(R.layout.layout_for_transaction_successful_dialog);
            successDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            Button continueButtonInSuccessDialog = successDialog.findViewById(R.id.continueButton);
            DonationsDatabaseHandler donationsDatabaseHandler = new DonationsDatabaseHandler();
            Donation currentDonation = new Donation(name,
                    mobileNumber,
                    email,
                    Calendar.getInstance().getTime().toString(),
                    amountInString,
                    paymentId,"Success");
            donationsDatabaseHandler.addDonation(currentDonation).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Not Added To Database.", Toast.LENGTH_SHORT).show();
                }
            });
            TextView transactionId = successDialog.findViewById(R.id.t3);
            transactionId.setText("Payment Id : " + paymentId);
            continueButtonInSuccessDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    successDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Thanks For Your Help.\nYour money will reduce one's suffering.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            });
            showDialog(successDialog);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String paymentId) {
        DonationsDatabaseHandler donationsDatabaseHandler = new DonationsDatabaseHandler();
        Donation currentDonation = new Donation(name,
                mobileNumber,
                email,
                Calendar.getInstance().getTime().toString(),
                amountInString,
                "Not Generated","Failed");
        donationsDatabaseHandler.addDonation(currentDonation).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Not Added To Database.", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Session Aborted", Toast.LENGTH_SHORT).show();
    }
}