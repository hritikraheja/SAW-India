package com.example.saw_india.modalClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UsersDatabaseHandler {

    private DatabaseReference databaseReference;

    public UsersDatabaseHandler() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(User.class.getSimpleName());
    }

    public Task<Void> addUser(User user){
        return databaseReference.push().setValue(user);
    }

    public Query getDatabaseByMobileNumber(String mobileNumber){
        return databaseReference.orderByChild("mobileNumber").equalTo(mobileNumber);
    }
}