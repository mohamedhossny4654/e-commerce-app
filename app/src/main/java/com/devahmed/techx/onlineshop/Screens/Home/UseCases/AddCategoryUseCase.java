package com.devahmed.techx.onlineshop.Screens.Home.UseCases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryUseCase extends BaseObservableMvcView<AddCategoryUseCase.Listener> {

    public interface Listener{
        void onCategoryAddedSuccessfully();
        void onCategoryFailedToAdd();
        void onCategoryInputError(String message);
    }


    private final Activity context;

    private final String FIREBASE_PATH = "Categories";
    private final String FIRESTORAGE_PATH = "IMAGES";

    public AddCategoryUseCase(Activity context) {
        this.context = context;
    }


    public void addNewCategory(final String name   , Uri pickedImage){

        if(!isValid(name)){
            notifyInputError("Product name is not valid");
            return;
        }
        if(pickedImage == null){
            notifyInputError("You have to pick image first");
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FIRESTORAGE_PATH);
        final StorageReference imageFilePath = storageReference.child(pickedImage.getLastPathSegment());
        imageFilePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        Category product = new Category(name , imageDownloadLink );
                        //add post to firebase database
                        addPostToFirebase(product);
                        notifySuccess();
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

    public void updateCategory(final Category updatedCategory , Uri pickedImage){
        if(!isValid(updatedCategory.getTitle())){
            notifyInputError("Product name is not valid");
            return;
        }
        if(pickedImage == null){
            notifyInputError("You have to pick image first");
            System.out.println("Here");
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FIRESTORAGE_PATH);
        final StorageReference imageFilePath = storageReference.child(pickedImage.getLastPathSegment());
        imageFilePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        Category category = new Category(updatedCategory.getTitle() , imageDownloadLink );
                        category.setId(updatedCategory.getId());
                        //add post to firebase database
                        updateExistingCategory(category);
                        notifySuccess();
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
    public void updateCategoryWithOldImage(final Category updatedCategory){
        if(!isValid(updatedCategory.getTitle())){
            notifyInputError("Product name is not valid");
            return;
        }
        Category category = new Category(updatedCategory.getTitle() , updatedCategory.getImage() );
        category.setId(updatedCategory.getId());
        category.setTimeStamp(updatedCategory.getTimeStamp());
        //add post to firebase database
        updateExistingCategory(category);
    }

    public void updateExistingCategory(Category category){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = category.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(category.getId(), postValues);
        reference.updateChildren(childUpdates);
        notifySuccess();
    }

    private void addPostToFirebase(Category product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        product.setId(key);
        //add post data to firebase database
        myRef.setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            listener.onCategoryFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onCategoryAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onCategoryInputError(message);
        }
    }
}
