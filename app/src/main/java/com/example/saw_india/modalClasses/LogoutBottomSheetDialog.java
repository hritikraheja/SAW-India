package com.example.saw_india.modalClasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.example.saw_india.LoginActivity;
import com.example.saw_india.MainActivity;
import com.example.saw_india.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.content.Context.MODE_PRIVATE;

public class LogoutBottomSheetDialog extends BottomSheetDialogFragment {

    Button noButton;
    Button yesButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_bottom_sheet_layout, container, false);
        noButton = view.findViewById(R.id.noButton);
        yesButton = view.findViewById(R.id.yesButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginCredentials.name = null;
                LoginCredentials.mobileNumber = null;
                LoginCredentials.email = null;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loggedInUserName", null);
                editor.putString("loggedInUserMobileNumber", null);
                editor.putString("loggedInUserEmail", null);
                editor.apply();
                dismiss();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity().getApplicationContext(), "Logged out successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
