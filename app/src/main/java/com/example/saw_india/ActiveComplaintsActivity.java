package com.example.saw_india;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saw_india.R;
import com.example.saw_india.modalClasses.Complaint;
import com.example.saw_india.modalClasses.ComplaintsDatabaseHandler;
import com.example.saw_india.modalClasses.LoginCredentials;
import com.example.saw_india.modalClasses.RecyclerViewAdapterForComplaintHistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.LinkedList;

import pl.droidsonroids.gif.GifImageView;

public class ActiveComplaintsActivity extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;
    TextView noRecordsFound;
    GifImageView loadingGif;
    int count = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        ComplaintsDatabaseHandler db = new ComplaintsDatabaseHandler();
        final LinkedList<Complaint> complaints = new LinkedList<>();
        db.getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    if (complaint.getStatus().compareTo("Rescued") == 0) {
                        complaints.add(complaint);
                    }
                }
                if (count == 0) {
                    Collections.reverse(complaints);
                    recyclerView.setAdapter(new RecyclerViewAdapterForComplaintHistory(complaints));
                    if (complaints.size() != 0) {
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
        setContentView(R.layout.activity_active_complaints);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(ActiveComplaintsActivity.this));
        loadingGif.setVisibility(View.VISIBLE);
        ComplaintsDatabaseHandler db = new ComplaintsDatabaseHandler();
        final LinkedList<Complaint> complaints = new LinkedList<>();
        db.getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    if (complaint.getStatus().compareTo("Rescued") != 0 && complaint.getStatus().compareTo("Rejected") != 0) {
                        complaints.add(complaint);
                    }
                }
                if (count == 0) {
                    Collections.reverse(complaints);
                    recyclerView.setAdapter(new RecyclerViewAdapterForComplaintHistory(complaints));
                    if (complaints.size() != 0) {
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