package com.example.saw_india;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saw_india.modalClasses.BottomSheetDialog;
import com.example.saw_india.modalClasses.LoginCredentials;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;

public class NeedHelpFragment extends Fragment {

    private static final int CAMERA_REQUEST = 123;
    private static final int SELECT_IMAGE_REQUEST = 200;
    public static Bitmap pic;
    public static EditText nameEditText;
    public static EditText mobileNumberEditText;
    public static EditText emailEditText;
    public static EditText descriptionEditText;
    public static ImageView image;
    ImageView selectButton;
    ImageView captureButton;
    public static TextView captureText;
    Button submitButton;
    public static CheckBox checkBox;
    public static ImageView captureIcon;
    Uri imageUri;

    public static String complaineeName;
    public static String complaineeMobileNumber;
    public static String complaineeEmail;
    public static String description;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity().getApplicationContext(), requestCode, Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "You Cannot Capture Image Without Permission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                pic = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageUri);
                image.setImageBitmap(pic);
                captureText.setVisibility(View.INVISIBLE);
                captureIcon.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == SELECT_IMAGE_REQUEST) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                image.setImageURI(selectedImageUri);
                try {
                    pic = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                captureText.setVisibility(View.INVISIBLE);
                captureIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_need_help_fragment, container, false);
        image = view.findViewById(R.id.image);
        selectButton = view.findViewById(R.id.selectButton);
        captureButton = view.findViewById(R.id.captureButton);
        captureText = view.findViewById(R.id.captureText);
        submitButton = view.findViewById(R.id.submitButton);
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        mobileNumberEditText = view.findViewById(R.id.mobileNumberEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        checkBox = view.findViewById(R.id.checkbox);
        captureIcon = view.findViewById(R.id.i1);
        nameEditText.setText(LoginCredentials.name);
        mobileNumberEditText.setText(LoginCredentials.mobileNumber);
        emailEditText.setText(LoginCredentials.email);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, CAMERA_REQUEST);
                }
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), SELECT_IMAGE_REQUEST);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaineeName = nameEditText.getText().toString();
                complaineeMobileNumber = mobileNumberEditText.getText().toString();
                complaineeEmail = emailEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                if (pic == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Upload An Image First", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(complaineeName)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Name Field Cannot Be Empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(complaineeMobileNumber)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Mobile Number Field Cannot Be Empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(description)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Write Some Description About The Problem", Toast.LENGTH_SHORT).show();
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Tick The Checkbox", Toast.LENGTH_SHORT).show();
                } else if (complaineeMobileNumber.length() != 10) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter A Valid 10-Digit Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(complaineeEmail) && (!Patterns.EMAIL_ADDRESS.matcher(complaineeEmail).matches())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter A Valid Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                    bottomSheetDialog.show(MainActivity.supportFragmentManager, "Tag");
                }
            }
        });
        return view;
    }
}