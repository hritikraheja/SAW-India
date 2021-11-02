package com.example.saw_india.modalClasses;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HelpersDatabaseHandler {

    private DatabaseReference databaseReference;

    public HelpersDatabaseHandler() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(Helper.class.getSimpleName() + "s");
    }

    public Query getDatabaseByPlaceId(String placeId){
        return databaseReference.orderByChild("placeId").equalTo(placeId);
    }
}
