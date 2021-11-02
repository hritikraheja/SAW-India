package com.example.saw_india.modalClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShelterRequestsDatabaseHandler {

    private DatabaseReference databaseReference;

    public ShelterRequestsDatabaseHandler() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(ShelterRequest.class.getSimpleName() + "s");
    }

    public Task<Void> addShelterRequest(ShelterRequest shelterRequest){
        return databaseReference.push().setValue(shelterRequest);
    }

    public Query getDatabaseByMobileNumber(String mobileNumber){
        return databaseReference.orderByChild("personMobileNumber").equalTo(mobileNumber);
    }
}
