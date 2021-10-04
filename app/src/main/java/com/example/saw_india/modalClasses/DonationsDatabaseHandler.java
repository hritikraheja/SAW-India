package com.example.saw_india.modalClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DonationsDatabaseHandler {

    private DatabaseReference databaseReference;

    public DonationsDatabaseHandler() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Donation.class.getSimpleName() + "s");
    }

    public Task<Void> addDonation(Donation donation){
        return databaseReference.push().setValue(donation);
    }

    public Query getDatabaseByMobileNumber(String mobileNumber){
        return databaseReference.orderByChild("donorMobileNumber").equalTo(mobileNumber);
    }
}