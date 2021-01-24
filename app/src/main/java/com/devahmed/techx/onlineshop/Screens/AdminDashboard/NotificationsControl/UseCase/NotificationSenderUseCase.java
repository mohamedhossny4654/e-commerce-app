package com.devahmed.techx.onlineshop.Screens.AdminDashboard.NotificationsControl.UseCase;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NotificationSenderUseCase extends BaseObservableMvcView<NotificationSenderUseCase.Listener> {


    private final Activity context;

    private final String FIREBASE_PATH = "Messages";
    private final String FIRESTORAGE_PATH = "NOTIFICATION_IMAGES";

    public interface Listener {
        void onNotificationAddedSuccessfully();
        void onNotificationFailedToAdd();
        void onInputError(String message);
    }

    public NotificationSenderUseCase(Activity context) {
        this.context = context;
    }

    public void senNotificationWithBody(Notification notification) {
        if(!isValid(notification.getTitle())){
            notifyInputError("Title is not valid");
        }
        if(!isValid(notification.getBody())){
            notifyInputError("body is not valid");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        //add post data to firebase database
        myRef.setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void sendNotificationWithPhoto(Notification notification , Uri pickedImage){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FIRESTORAGE_PATH);
        final StorageReference imageFilePath = storageReference.child(pickedImage.getLastPathSegment());
        imageFilePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        Notification notification = new Notification();
                        notification.setPhotoUrl(imageDownloadLink);
                        //add post to firebase database
                        senNotificationWithPhoto(notification);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //some thing goes wrong while uploading post
                        notifyFailure();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifyFailure();
            }
        });
    }

    public void senNotificationWithPhoto(Notification notification) {
        if(!isValid(notification.getTitle())){
            notifyInputError("Title is not valid");
        }
        if(!isValid(notification.getPhotoUrl())){
            notifyInputError("body is not valid");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        //add post data to firebase database
        myRef.setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            listener.onNotificationFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onNotificationAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onInputError(message);
        }
    }
}
