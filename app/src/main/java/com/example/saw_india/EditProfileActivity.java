package com.example.saw_india;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.UsersDatabaseHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backButton;
    TextView nameTV;
    TextView mobileNumberTV;
    TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        backButton = findViewById(R.id.backButton);
        nameTV = findViewById(R.id.nameTV);
        mobileNumberTV = findViewById(R.id.mobileNumberTV);
        emailTV = findViewById(R.id.emailTV);

        final UsersDatabaseHandler databaseHandler = new UsersDatabaseHandler(this);
        nameTV.setText(LoginCredentials.name);
        mobileNumberTV.setText(LoginCredentials.mobileNumber);
        if (LoginCredentials.email!=null)
        emailTV.setText(LoginCredentials.email);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_for_edit_details_dialog);
                dialog.show();
                final TextInputEditText inputEditText = dialog.findViewById(R.id.editText);
                TextInputLayout layout = dialog.findViewById(R.id.aLayout);
                Button updateButton = dialog.findViewById(R.id.updateButton);
                inputEditText.setText(LoginCredentials.name);
                layout.setHint("Name");
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = inputEditText.getText().toString();
                        if(newName.equals(LoginCredentials.name)){
                            Toast.makeText(v.getContext(), "This name is same as the original name.", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseHandler.updateUserName(newName);
                            LoginCredentials.name = newName;
                            nameTV.setText(newName);
                            SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUserName", newName);
                            editor.apply();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        emailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_for_edit_details_dialog);
                dialog.show();
                final TextInputEditText inputEditText = dialog.findViewById(R.id.editText);
                TextInputLayout layout = dialog.findViewById(R.id.aLayout);
                TextView prompt = dialog.findViewById(R.id.prompt);
                Button updateButton = dialog.findViewById(R.id.updateButton);
                inputEditText.setText(LoginCredentials.email);
                layout.setHint("Email");
                prompt.setText("Note : This email will be displayed to other users and complainers.");
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newEmail = inputEditText.getText().toString();
                        if(newEmail.equals(LoginCredentials.email)){
                            Toast.makeText(v.getContext(), "This email is same as the original email.", Toast.LENGTH_SHORT).show();
                        }else if(!isValidEmail(newEmail)){
                            Toast.makeText(v.getContext(), "Enter a valid email address.", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseHandler.updateUserEmail(newEmail);
                            LoginCredentials.email = newEmail;
                            emailTV.setText(newEmail);
                            SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUserEmail", newEmail);
                            editor.apply();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}