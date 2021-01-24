package com.devahmed.techx.onlineshop.Screens.AddProducts.AddProductUseCase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProductUseCase extends BaseObservableMvcView<AddProductUseCase.Listener> {
    private final Activity context;

    private final String FIREBASE_PATH = "Products";
    private final String FIRESTORAGE_PATH = "IMAGES";
    public interface Listener {
        void onProductAddedSuccessfully();
        void onProductFailedToAdd();
        void onInputError(String message);
    }

    public AddProductUseCase(Activity context) {
        this.context = context;
    }

    public void addNewProduct(final String name, final String price, Uri pickedImage
            , final String subCategory, final boolean isOffer
            , final int count, final int points, final String selectedBranch){
            if(!isValid(selectedBranch)){
                notifyInputError("selected branch is not valid");
                return;
            }
            if(!isValid(name)){
                notifyInputError("Product name is not valid");
                return;
            }
            if(pickedImage == null){
                notifyInputError("You have to pick image first");
                return;
            }
            if(price.isEmpty()){
                notifyInputError("Enter the price");
                return;
            }
            try{
                if(!(((Double)Double.parseDouble(price)) instanceof Double)){
                    notifyInputError("Price is not valid");
                    return;
                }
            }catch (Exception e){
                notifyInputError("Enter valid price");
                return;
            }
            try{
                if(!(((Integer)points) instanceof Integer)){
                    notifyInputError("Price is not valid");
                    return;
                }
            }catch (Exception e){
                notifyInputError("Enter valid price");
                return;
            }
            try{
                if(!(((Integer)count) instanceof Integer)){
                    notifyInputError("Price is not valid");
                    return;
                }
            }catch (Exception e){
                notifyInputError("Enter valid price");
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
                            Product product = new Product(name , Double.parseDouble(price) , imageDownloadLink , subCategory);
                            product.setPoints(points);
                            product.setMaxCount(count);
                            product.setBranch(selectedBranch);
                            //add post to firebase database
                            addProductToFirebase(product , isOffer);

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


    private void addProductToFirebase(Product product , boolean isOffer) {
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
        if(isOffer){
            //if admin choose to add this product to offers we create a new ProductSuCategory Object in firebase
            ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(product.getId());
            productSubCategory.setTitle(product.getTitle());
            productSubCategory.setPrice(product.getPrice());
            productSubCategory.setType(ProductSubCategory.TYPE_PRODUCT);
            product.setSubCategory(product.getSubCategory());
            productSubCategory.setImage(product.getImage());
            addProductSubCategoryToFirebase(productSubCategory);
        }
    }
    private void addProductSubCategoryToFirebase(ProductSubCategory productSubCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ProductSubCategory").push();
        //get post unique ID & update post key
        String key = myRef.getKey();
//        productSubCategory.setId(key);
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

    boolean isValid(String text){
        if(!text.isEmpty() || !text.trim().equals("") || text == null){
            if( (text.length() >= 4) ){
                return true;
            }
        }
        return false;
    }

    public void updateProduct(final Product updatedProduct, Uri pickedImage , final boolean isOffer){
        if(!isValid(updatedProduct.getTitle())){
            notifyInputError("Product name is not valid");
            return;
        }
        if(pickedImage == null){
            notifyInputError("You have to pick image first");
            return;
        }
        if(updatedProduct.getPrice() == 0){
            notifyInputError("Enter the price");
            return;
        }
        try{
            if(!(((Double)updatedProduct.getPrice()) instanceof Double)){
                notifyInputError("Price is not valid");
                return;
            }
        }catch (Exception e){
            notifyInputError("Enter valid price");
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
                        //add post to firebase database
                        updatedProduct.setImage(imageDownloadLink);
                        updateExistingProduct(updatedProduct);
                        updateProductSubCategory(updatedProduct ,isOffer );
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


    public void updateProductWithOldImage(final Product updatedProduct , boolean isOffer){
        if(!isValid(updatedProduct.getTitle())){
            notifyInputError("Product name is not valid");
            return;
        }
        if(updatedProduct.getPrice() == 0){
            notifyInputError("Enter the price");
            return;
        }
        try{
            if(!(((Double)updatedProduct.getPrice()) instanceof Double)){
                notifyInputError("Price is not valid");
                return;
            }
        }catch (Exception e){
            notifyInputError("Enter valid price");
            return;
        }

        //add post to firebase database
        updatedProduct.setImage(updatedProduct.getImage());
        updateExistingProduct(updatedProduct);
        updateProductSubCategory(updatedProduct , isOffer);
    }

    public void updateExistingProduct(Product product){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = product.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(product.getId(), postValues);
        reference.updateChildren(childUpdates);
        notifySuccess();
    }

    private void updateProductSubCategory(Product product , boolean isOffer){
        if(isOffer){
            //if admin choose to add this product to offers we create a new ProductSuCategory Object in firebase
            ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(product.getId());
            productSubCategory.setTitle(product.getTitle());
            productSubCategory.setPrice(product.getPrice());
            productSubCategory.setType(ProductSubCategory.TYPE_PRODUCT);
            product.setSubCategory(product.getSubCategory());
            productSubCategory.setImage(product.getImage());
            addProductSubCategoryToFirebase(productSubCategory);
        }
    }

    private void notifyFailure() {
        for(Listener listener : getmListeners()){
            listener.onProductFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onProductAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onInputError(message);
        }
    }
}
