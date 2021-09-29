package com.example.saw_india.modalClasses;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.saw_india.R;
import com.example.saw_india.SearchNearbyFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vanillaplacepicker.data.VanillaAddress;
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;
import com.vanillaplacepicker.utils.KeyUtils;
import com.vanillaplacepicker.utils.MapType;
import com.vanillaplacepicker.utils.PickerLanguage;
import com.vanillaplacepicker.utils.PickerType;

public class BottomSheetDialog extends BottomSheetDialogFragment implements OnMapReadyCallback {

    MapView map;
    Button submitButton;
    CheckBox checkBox;
    ImageView locateButton;
    GoogleMap googleMap;
    private static final int PLACE_PICKER_REQUEST_CODE = 3;

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
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = SearchNearbyFragment.myLat;
                double lng = SearchNearbyFragment.myLng;
                Intent intent = new VanillaPlacePicker.Builder(getActivity().getApplicationContext())
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()){
                    Toast.makeText(getActivity().getApplicationContext(), "Please Read The Checkbox And Tick It.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "All Done", Toast.LENGTH_SHORT).show();
                    BottomSheetDialog.this.dismiss();
                }
            }
        });
        return view;
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
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(addressLocation).title("Animal's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
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
