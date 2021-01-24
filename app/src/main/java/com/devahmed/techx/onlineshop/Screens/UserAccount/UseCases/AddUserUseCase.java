package com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class AddUserUseCase extends BaseObservableMvcView<AddUserUseCase.Listener> {

    private final Activity context;

    private final String FIREBASE_PATH = "Users";

    public interface Listener {
        void onUserAddedSuccessfully();
        void onUserFailedToAdd();
        void onInputError(String message);
        void onUserDataUpdated(User user);
    }

    public AddUserUseCase(Activity context) {
        this.context = context;
    }

    public void addNewUser(User user){
        if(!isUserValid(user)){
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        user.setUserId(user.getUserId());
        //add post data to firebase database
        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void addNewUser(String uid , String phoneNumber){
        User user = new User();
        user.setPhone(phoneNumber);
        String token = FirebaseInstanceId.getInstance().getToken();
        user.setDevice_token(token);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        user.setUserId(uid);
        //add post data to firebase database
        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private boolean isUserValid(User user) {
        if(!isValid(user.getPhone())){
            notifyInputError("Phone is not valid");
            return false;
        }
        if(!isValid(user.getArea())){
            notifyInputError("Area is not valid");
            return false;
        }
        if(!isValid(user.getFlat())){
            notifyInputError("Flat is not valid");
            return false;
        }
        if(!isValid(user.getStreet())){
            notifyInputError("Street is not valid");
            return false;
        }
        if(!isValid(user.getNearestLandmark())){
            notifyInputError("Unique Sign is not valid");
            return false;
        }
        return true;
    }
    //used only when get user location for first time to delete extra user data
    public void updateExistingUserAndDeleteExtraUser(final User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(user.getUserId(), postValues);
        reference.updateChildren(childUpdates);
        notifyUserUpdated(user);
        //delete extra user
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User model = snapshot.getValue(User.class);
                    if(model.getUserId().equals(user.getUserId())){
                        snapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void updateExistingUser(final User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(user.getUserId(), postValues);
        reference.updateChildren(childUpdates);
        notifyUserUpdated(user);
    }

    boolean isValid(String text){
        if(!text.isEmpty() || !text.trim().equals("")){
            if( (text.length() >= 4) ){
                return true;
            }
        }
        return false;
    }

    private void notifyFailure() {
        for(Listener listener : getmListeners()){
            listener.onUserFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onUserAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onInputError(message);
        }
    }
    private void notifyUserUpdated(User user){
        for(Listener listener : getmListeners()){
            listener.onUserDataUpdated(user);
        }
    }

}
