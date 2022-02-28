package com.example.saw_india.modalClasses;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class UsersDatabaseHandler {

    private DatabaseReference databaseReference;

    Context context;

    public UsersDatabaseHandler() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(User.class.getSimpleName());
    }

    public  UsersDatabaseHandler(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(User.class.getSimpleName());
        this.context = context;
    }

    public Task<Void> addUser(User user){
        return databaseReference.push().setValue(user);
    }

    public Query getDatabaseByMobileNumber(String mobileNumber){
        return databaseReference.orderByChild("mobileNumber").equalTo(mobileNumber);
    }

    public void updateUserName(final String newName) {
        getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "Not found";
                for(DataSnapshot s : snapshot.getChildren()){
                    key = s.getKey();
                }
                try {
                    databaseReference.child(key).child("name").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context , "Name updated successfully.",Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e){
                    Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserEmail(final String newEmail) {
        getDatabaseByMobileNumber(LoginCredentials.mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "Not found";
                for(DataSnapshot s : snapshot.getChildren()){
                    key = s.getKey();
                }
                try {
                    databaseReference.child(key).child("email").setValue(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Email updated successfully.",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e){
                    Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}