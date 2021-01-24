package com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchUserInfoFromFirebaseUseCase extends BaseObservableMvcView<FetchUserInfoFromFirebaseUseCase.Listener> {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<User> userList;
    private final String FIREBASE_PATH = "Users";
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    User user = null;
    public interface Listener{
        void onUserDataGotSuccessfully(List<User> userList);
        void onUserDataCanceled(DatabaseError error);
    }

    public FetchUserInfoFromFirebaseUseCase(FirebaseDatabase database) {
        this.database = database;
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            this.currentUserId = user.getUid();
        }
    }

    public User getCurrentUser(){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    User model = postSnap.getValue(User.class);
                    userList.add(model);
                    user = model;
                }
                    notifyCategoryChange(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCategoryCancelled(databaseError);
            }
        });
        return user;
    }

    public void getUserOfID(String id){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.orderByChild("userId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    User model = postSnap.getValue(User.class);
                    userList.add(model);
                    user = model;
                }
                notifyCategoryChange(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCategoryCancelled(databaseError);
            }
        });
    }

    public void getAllUsers(){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    User model = postSnap.getValue(User.class);
                    System.out.println("blocked = " + model.getIsBlocked());
                    userList.add(model);
                }
                notifyCategoryChange(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCategoryCancelled(databaseError);
            }
        });
    }


    private void notifyCategoryCancelled(DatabaseError databaseError) {
        for(Listener listener: getmListeners()){
            listener.onUserDataCanceled(databaseError);
        }
    }

    private void notifyCategoryChange(List<User> userList) {
        for(Listener listener: getmListeners()){
            listener.onUserDataGotSuccessfully(userList);
        }
    }
}
