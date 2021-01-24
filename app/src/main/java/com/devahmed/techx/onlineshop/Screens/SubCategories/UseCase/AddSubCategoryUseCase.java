package com.devahmed.techx.onlineshop.Screens.SubCategories.UseCase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddSubCategoryUseCase extends BaseObservableMvcView<AddSubCategoryUseCase.Listener> {
    public interface Listener {
        void onSubCategoryAddedSuccessfully();
        void onSubCategoryFailedToAdd();
        void onSubCategoryInputError(String message);
    }
    private final Activity context;

    private final String FIREBASE_PATH = "Sub-Categories";
    private final String FIRESTORAGE_PATH = "IMAGES";

    public AddSubCategoryUseCase(Activity context) {
        this.context = context;
    }

    public void addSubCategoryToFirebase(final String name, Uri pickedImage , final String category
            , final boolean isOffer){
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
                        SubCategory subCategory = new SubCategory(name , imageDownloadLink );
                        //add post to firebase database
                        subCategory.setCategory(category);
                        subCategory.setInOffer(isOffer);
                        addPostToFirebase(subCategory);
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
    public void updateSubCategory(final SubCategory updatedSubCategory, Uri pickedImage){
        if(!isValid(updatedSubCategory.getTitle())){
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
                        SubCategory subCategory = new SubCategory( updatedSubCategory.getTitle(), imageDownloadLink );
                        //add post to firebase database
                        subCategory.setId(updatedSubCategory.getId());
                        subCategory.setCategory(updatedSubCategory.getCategory());
                        subCategory.setInOffer(updatedSubCategory.isInOffer());
                        updateExistingSubCategory(subCategory);
                        updateProductSubCategory(subCategory);
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

    public void updateSubCategoryWithOldImage(final SubCategory updatedSubCategory){
        if(!isValid(updatedSubCategory.getTitle())){
            notifyInputError("Product name is not valid");
            return;
        }

        SubCategory subCategory = new SubCategory( updatedSubCategory.getTitle(), updatedSubCategory.getImage() );
        //add post to firebase database
        subCategory.setId(updatedSubCategory.getId());
        subCategory.setCategory(updatedSubCategory.getCategory());
        subCategory.setInOffer(updatedSubCategory.isInOffer());
        updateExistingSubCategory(subCategory);
        updateProductSubCategory(subCategory);

    }

    private void addPostToFirebase(SubCategory product) {
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
        if(product.isInOffer()){
            //add new ProductSubCategory object to firebase
            ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(product.getId());
            productSubCategory.setImage(product.getImage());
            productSubCategory.setSubCategory(product.getTitle());
            productSubCategory.setType(ProductSubCategory.TYPE_SUB_CATEGORY);
            productSubCategory.setPrice(0);
            productSubCategory.setTitle(product.getTitle());
            addProductSubCategoryToFirebase(productSubCategory);
        }else{
            //delete this productSubCategory If exist
            myRef = database.getReference("ProductSubCategory").child(product.getId());
            myRef.removeValue();
        }
    }

    private void addProductSubCategoryToFirebase(ProductSubCategory productSubCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ProductSubCategory").push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        productSubCategory.setId(key);
        //add post data to firebase database
        myRef.setValue(productSubCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void updateExistingSubCategory(SubCategory subCategory){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = subCategory.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(subCategory.getId(), postValues);
        reference.updateChildren(childUpdates);
        notifySuccess();
    }

    public void updateProductSubCategory(SubCategory subCategory){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        if(subCategory.isInOffer()){
            //add new ProductSubCategory object to firebase
            ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(subCategory.getId());
            productSubCategory.setImage(subCategory.getImage());
            productSubCategory.setSubCategory(subCategory.getTitle());
            productSubCategory.setType(ProductSubCategory.TYPE_SUB_CATEGORY);
            productSubCategory.setPrice(0);
            productSubCategory.setTitle(subCategory.getTitle());
            addProductSubCategoryToFirebase(productSubCategory);
        }else{
            //delete this productSubCategory If exist
            myRef = database.getReference("ProductSubCategory").child(subCategory.getId());
            myRef.removeValue();
        }
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
            listener.onSubCategoryFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onSubCategoryAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onSubCategoryInputError(message);
        }
    }
}
