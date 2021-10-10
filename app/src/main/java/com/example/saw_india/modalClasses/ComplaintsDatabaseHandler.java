package com.example.saw_india.modalClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ComplaintsDatabaseHandler {

    private DatabaseReference databaseReference;

    public ComplaintsDatabaseHandler() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(Complaint.class.getSimpleName() + "s");
    }

    public Task<Void> addComplaintToDatabase(Complaint complaint){
        return databaseReference.push().setValue(complaint);
    }

    public Query getDatabaseByMobileNumber(String mobileNumber){
        return databaseReference.orderByChild("complaineeMobileNumber").equalTo(mobileNumber);
    }
}
