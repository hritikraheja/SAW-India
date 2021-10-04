package com.example.saw_india.modalClasses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.example.saw_india.MainActivity;
import com.example.saw_india.MakeADonationActivity;
import com.example.saw_india.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;

public class LoginBottomSheetLayoutForDonationsPage extends BottomSheetDialogFragment {

    Button noButton;
    Button yesButton;
    TextView nameTV;
    TextView numberTV;
    TextView emailTV;
    User newUser;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_bottom_sheet_layout_for_donations_page, container, false);
        noButton = view.findViewById(R.id.noButton);
        yesButton = view.findViewById(R.id.yesButton);
        nameTV = view.findViewById(R.id.t4);
        numberTV = view.findViewById(R.id.t5);
        emailTV = view.findViewById(R.id.t6);
        nameTV.setText("Name : " + MakeADonationActivity.name);
        numberTV.setText("Mobile Number : " + MakeADonationActivity.mobileNumber);
        if(!MakeADonationActivity.email.isEmpty()) {
            emailTV.setText("Email : " + MakeADonationActivity.email);
        } else {
            emailTV.setVisibility(View.INVISIBLE);
        }
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UsersDatabaseHandler databaseHandler = new UsersDatabaseHandler();
                databaseHandler.getDatabaseByMobileNumber(MakeADonationActivity.mobileNumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LinkedList<User> a = new LinkedList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            a.add(user);
                        }
                        if (a.size()==0){
                            if (count == 0) {
                                newUser = new User(MakeADonationActivity.name, MakeADonationActivity.mobileNumber, MakeADonationActivity.email);
                                databaseHandler.addUser(newUser);
                                count++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Error", error.getMessage());
                    }
                });
                MainActivity.loggedInUserName = MakeADonationActivity.name;
                MainActivity.loggedInUserMobileNumber = MakeADonationActivity.mobileNumber;
                MainActivity.getLoggedInUserEmail = MakeADonationActivity.email;
                MainActivity.loggedInUserNameTV.setText(MakeADonationActivity.name);
                MainActivity.loggedInUserMobileNumberTV.setText(MakeADonationActivity.mobileNumber);
                if (MakeADonationActivity.email != null){
                    MainActivity.getLoggedInUserEmailTV.setText(MakeADonationActivity.email);
                } else {
                    MainActivity.getLoggedInUserEmailTV.setText("");
                }
                MainActivity.loggedInUserNameTV.setTextSize(15);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loggedInUserName", MainActivity.loggedInUserName);
                editor.putString("loggedInUserMobileNumber", MainActivity.loggedInUserMobileNumber);
                editor.putString("loggedInUserEmail", MainActivity.getLoggedInUserEmail);
                editor.apply();
                MainActivity.drawerLayout.closeDrawer(GravityCompat.START);
                getActivity().finish();
                Toast.makeText(getActivity().getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}