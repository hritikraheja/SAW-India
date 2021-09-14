package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DonationsFragment extends Fragment {

    Button payButton;
    EditText amountEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_donations_fragment, container, false);
        amountEditText = view.findViewById(R.id.amountEditText);
        payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String amountInString = amountEditText.getText().toString();
                    int amount = Math.round(Float.parseFloat(amountInString) * 100);
                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_iifDyEQ5Tenyca");
                    checkout.setImage(R.drawable.final_logo_saw);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", "SAW, INDIA");
                        object.put("desciption", "Every drop makes an ocean what it is. Thanks for your help!");
                        object.put("theme.color", "#FF8C00");
                        object.put("currency", "INR");
                        object.put("amount", amount);
                        object.put("prefill.contact", "6398945909");
                        object.put("prefill.email", "hritikraheja27@gmail.com");
                        checkout.open(getActivity(), object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Enter A Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}