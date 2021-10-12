package com.example.saw_india.modalClasses;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saw_india.R;
import com.example.saw_india.SearchNearbyFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

class RecyclerViewAdapterForActiveComplaintActivity extends RecyclerView.Adapter<RecyclerViewAdapterForActiveComplaintActivity.RecyclerViewHolderForActiveComplaints> {

    LinkedList<Complaint> complaints;

    public RecyclerViewAdapterForActiveComplaintActivity(LinkedList<Complaint> complaints) {
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public RecyclerViewHolderForActiveComplaints onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_layout_for_complaints_history, parent, false);
        return new RecyclerViewHolderForActiveComplaints(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderForActiveComplaints holder, int position) {
        final Complaint complaint = complaints.get(position);
        try {
            Glide.with(holder.image).load(complaint.getImageUrl()).into(holder.image);
        } catch (Exception e){
            holder.image.setImageResource(R.drawable.ic_warning);
        }
        holder.dateAndTimeTV.setText(complaint.getDateAndTime());
        holder.statusTV.setText("Status : " + complaint.getStatus());
        if (complaint.getStatus().compareTo("Rejected") == 0){
            holder.statusTV.setTextColor(Color.RED);
        }
        holder.latitudeTV.setText("Latitude : " + complaint.getAnimalLocationLat());
        holder.longitudeTV.setText("Longitude : " + complaint.getAnimalLocationLng());
        if (complaint.getAllocatedHelper().getHelperMobileNumber().compareTo("null") == 0){
            holder.allocatedHelperDetails.setText("Not Assigned Yet");
        } else {
            holder.allocatedHelperDetails.setText(complaint.getAllocatedHelper().getHelperName() + "\n(" + complaint.getAllocatedHelper().getHelperOrganisationName() + ")");
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v){
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_for_complaint_details_dialog);
                dialog.setCancelable(true);
                ImageView image = dialog.findViewById(R.id.image);
                MapView map = dialog.findViewById(R.id.map);
                TextView descriptionTV = dialog.findViewById(R.id.descriptionTV);
                TextView dateAndTimeTV = dialog.findViewById(R.id.dateAndTimeTV);
                final TextView complaineeDetailsTV = dialog.findViewById(R.id.complaineeDetailsTV);
                TextView helperDetailsTV = dialog.findViewById(R.id.helperDetailsTV);
                TextView statusTV = dialog.findViewById(R.id.statusTV);
                TextView closeButton = dialog.findViewById(R.id.closeButton);
                Glide.with(image.getContext()).load(complaint.getImageUrl()).into(image);
                descriptionTV.setText(complaint.getComplaintDescription());
                dateAndTimeTV.setText(complaint.getDateAndTime());
                MapsInitializer.initialize(v.getContext());
                map.onCreate(dialog.onSaveInstanceState());
                map.onResume();
                map.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(false);
                        LatLng animalLocation = new LatLng(complaint.getAnimalLocationLat(), complaint.getAnimalLocationLng());
                        googleMap.addMarker(new MarkerOptions().position(animalLocation).title("Animals Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(animalLocation));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                    }
                });
                complaineeDetailsTV.setText(complaint.getComplaineeName() + "\n" + complaint.getComplaineeMobileNumber()+"\n"+complaint.getComplaineeEmail());
                if (complaint.getAllocatedHelper().getHelperMobileNumber().compareTo("null")==0){
                    helperDetailsTV.setText("Not Assigned Yet");
                } else {
                    Helper helper = complaint.getAllocatedHelper();
                    helperDetailsTV.setText(helper.getHelperName() + "\n" +
                            helper.getHelperMobileNumber()+"\n"+
                            helper.getHelperEmail() + "\n" + "(" +
                            helper.getHelperOrganisationName() + ")");
                }
                statusTV.setText("Status : " + complaint.getStatus());
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    static class RecyclerViewHolderForActiveComplaints extends RecyclerView.ViewHolder{

        ImageView image;
        TextView statusTV;
        TextView latitudeTV;
        TextView longitudeTV;
        TextView allocatedHelperDetails;
        TextView dateAndTimeTV;
        ConstraintLayout card;

        public RecyclerViewHolderForActiveComplaints(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            statusTV = itemView.findViewById(R.id.statusTV);
            latitudeTV = itemView.findViewById(R.id.latitudeTV);
            longitudeTV = itemView.findViewById(R.id.longitudeTV);
            allocatedHelperDetails = itemView.findViewById(R.id.allocatedHelperDetails);
            dateAndTimeTV = itemView.findViewById(R.id.dateAndTimeTV);
            card = itemView.findViewById(R.id.card);
        }
    }

}
