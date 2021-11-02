package com.example.saw_india.modalClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saw_india.MainActivity;
import com.example.saw_india.NeedHelpFragment;
import com.example.saw_india.R;
import com.example.saw_india.SearchNearbyFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vanillaplacepicker.data.VanillaAddress;
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;
import com.vanillaplacepicker.utils.KeyUtils;
import com.vanillaplacepicker.utils.MapType;
import com.vanillaplacepicker.utils.PickerLanguage;
import com.vanillaplacepicker.utils.PickerType;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class BottomSheetDialog extends BottomSheetDialogFragment implements OnMapReadyCallback {

    MapView map;
    Button submitButton;
    CheckBox checkBox;
    ImageView locateButton;
    GoogleMap googleMap;
    Uri imageUrl;
    ProgressDialog progressDialog;
    static double animalLocationLat;
    static double animalLocationLng;
    private static final int PLACE_PICKER_REQUEST_CODE = 3;

    MainActivity mActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.mActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        map = view.findViewById(R.id.map);
        submitButton = view.findViewById(R.id.submitButton);
        checkBox = view.findViewById(R.id.checkbox);
        locateButton = view.findViewById(R.id.locateButton);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
        animalLocationLng = SearchNearbyFragment.myLng;
        animalLocationLat = SearchNearbyFragment.myLat;
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = SearchNearbyFragment.myLat;
                double lng = SearchNearbyFragment.myLng;
                Intent intent = new VanillaPlacePicker.Builder(mActivity.getApplicationContext())
                        .with(PickerType.MAP)
                        .withLocation(lat, lng)
                        .setPickerLanguage(PickerLanguage.ENGLISH)
                        .setCountry("IN")
                        .enableShowMapAfterSearchResult(true)
                        .setMapType(MapType.NORMAL)
                        .setMapPinDrawable(R.drawable.pin)
                        .build();

                startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!checkBox.isChecked()){
                    Toast.makeText(getActivity(), "Please Read The Checkbox And Tick It.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        progressDialog = new ProgressDialog(v.getContext(), R.style.AppCompatAlertDialogStyle);
                        progressDialog.setTitle("Uploading Image");
                        progressDialog.setMessage("This will take some time depending on image size.");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();
                        final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        NeedHelpFragment.pic.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                        final byte[] data = baos.toByteArray();
                        ref.putBytes(data).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                final Task<Uri> t = task.getResult().getStorage().getDownloadUrl();
                                t.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri;
                                        ComplaintsDatabaseHandler databaseHandler = new ComplaintsDatabaseHandler();
                                        databaseHandler.addComplaintToDatabase(new Complaint(NeedHelpFragment.complaineeName,
                                                NeedHelpFragment.complaineeMobileNumber,
                                                NeedHelpFragment.complaineeEmail,
                                                animalLocationLat,
                                                animalLocationLng,
                                                imageUrl.toString(),
                                                NeedHelpFragment.description)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                NeedHelpFragment.descriptionEditText.setText("");
                                                NeedHelpFragment.image.setImageDrawable(null);
                                                NeedHelpFragment.captureText.setVisibility(View.VISIBLE);
                                                NeedHelpFragment.captureIcon.setVisibility(View.VISIBLE);
                                                NeedHelpFragment.checkBox.setChecked(false);
                                                progressDialog.dismiss();
                                                Toast.makeText(v.getContext(), "Complaint Registered Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(v.getContext(), "Unable to register compliant due to some issues.", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                });
                            }
                        });

                        BottomSheetDialog.this.dismiss();
                    } catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng location = new LatLng(SearchNearbyFragment.myLat, SearchNearbyFragment.myLng);
        googleMap.addMarker(new MarkerOptions().position(location).title("Animal's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == getActivity().RESULT_OK){
            VanillaAddress address = (VanillaAddress) data.getSerializableExtra(KeyUtils.SELECTED_PLACE);
            LatLng addressLocation = new LatLng(address.getLatitude(), address.getLongitude());
            animalLocationLat = address.getLatitude();
            animalLocationLng = address.getLongitude();
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(addressLocation).title("Animal's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(animalLocationLat, animalLocationLng),15));
        }
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}
