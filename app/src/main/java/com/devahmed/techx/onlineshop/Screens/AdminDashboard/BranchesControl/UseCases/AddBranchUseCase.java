package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Branch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddBranchUseCase extends BaseObservableMvcView<AddBranchUseCase.Listener> {
    private final Activity context;

    private final String FIREBASE_PATH = "Branches";
    public interface Listener {
        void onBranchAddedSuccessfully();
        void onBranchFailedToAdd();
        void onInputError(String message);
    }

    public AddBranchUseCase(Activity context) {
        this.context = context;
    }


    public void addNewBranch(Branch branch){
        addBranchToFirebase(branch);
    }

    private void addBranchToFirebase(Branch branch) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        branch.setId(key);
        //add post data to firebase database
        myRef.setValue(branch).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifySuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifyFailure();
            }
        });
    }

    public void updateExistingBranch(Branch branch){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = branch.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(branch.getId(), postValues);
        reference.updateChildren(childUpdates);
        notifySuccess();
    }

    private void notifyFailure() {
        for(Listener listener : getmListeners()){
            listener.onBranchFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onBranchAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onInputError(message);
        }
    }
}
