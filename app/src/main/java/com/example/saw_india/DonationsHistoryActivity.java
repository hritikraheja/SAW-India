package com.example.saw_india;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saw_india.modalClasses.Donation;
import com.example.saw_india.modalClasses.DonationsDatabaseHandler;
import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.RecyclerViewAdapterForDonationsHistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import pl.droidsonroids.gif.GifImageView;

public class DonationsHistoryActivity extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;
    TextView noRecordsFound;
    GifImageView loadingGif;
    int count = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        DonationsDatabaseHandler db = new DonationsDatabaseHandler();
        final ArrayList<Donation> donations = new ArrayList<>();
        db.getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Donation donation = dataSnapshot.getValue(Donation.class);
                    donations.add(donation);
                }
                if (count == 0) {
                    Collections.reverse(donations);
                    recyclerView.setAdapter(new RecyclerViewAdapterForDonationsHistory(donations));
                    if (donations.size() != 0) {
                        loadingGif.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        loadingGif.setVisibility(View.INVISIBLE);
                        noRecordsFound.setVisibility(View.VISIBLE);
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_history);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        noRecordsFound = findViewById(R.id.noRecordsFound);
        loadingGif = findViewById(R.id.loadingGif);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(DonationsHistoryActivity.this));
        loadingGif.setVisibility(View.VISIBLE);
        DonationsDatabaseHandler db = new DonationsDatabaseHandler();
        final ArrayList<Donation> donations = new ArrayList<>();
        db.getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Donation donation = dataSnapshot.getValue(Donation.class);
                    donations.add(donation);
                }
                if (count == 0) {
                    Collections.reverse(donations);
                    recyclerView.setAdapter(new RecyclerViewAdapterForDonationsHistory(donations));
                    if (donations.size() != 0) {
                        loadingGif.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        loadingGif.setVisibility(View.INVISIBLE);
                        noRecordsFound.setVisibility(View.VISIBLE);
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}