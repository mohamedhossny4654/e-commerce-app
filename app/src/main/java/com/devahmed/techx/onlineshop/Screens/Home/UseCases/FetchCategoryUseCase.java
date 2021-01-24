package com.devahmed.techx.onlineshop.Screens.Home.UseCases;

import android.content.Context;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Category;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchCategoryUseCase extends BaseObservableMvcView<FetchCategoryUseCase.Listener> {

    private FirebaseDatabase database;
    private DatabaseReference  offersDatabaseReference , cateegoriesDatabaseReference;
    private List<ProductSubCategory>  mOfferProductsList;
    private List<Category> mCategoriesList;
    List<Product> brancProducts;
    private final String FIREBASE_PATH = "Categories";

    public interface Listener{
        public void onOfferProductChange(List<ProductSubCategory> productList);
        public void onOfferProductCancelled(DatabaseError databaseError);
        public void onProductsChanged(List<Category> categoryList);
        public void onProductsCancelled(DatabaseError databaseError);
    }

    public FetchCategoryUseCase(FirebaseDatabase database) {
        this.database = database;
    }


    public void getCategories(){
        cateegoriesDatabaseReference = database.getReference("Categories");
        cateegoriesDatabaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCategoriesList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Category model = postSnap.getValue(Category.class);
                    mCategoriesList.add(model);
                }
//                Collections.reverse(mCategoriesList);
                notifyCategoryChange(mCategoriesList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCategoryCancelled(databaseError);
            }
        });

    }
    public void deleteCategory(String categoryID){
        //delete categories
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH).push();
        reference = database.getReference(FIREBASE_PATH);
        reference.child(categoryID).removeValue();
        //delete subCategories
        final List<String> containedSubCategories = new ArrayList<>();
        reference = database.getReference("Sub-Categories");
        reference.orderByChild("category").equalTo(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    SubCategory model = postSnap.getValue(SubCategory.class);
                    containedSubCategories.add(model.getTitle());
                    postSnap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //delete products
        reference = database.getReference("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    if(containedSubCategories.contains(model.getSubCategory())){
                        postSnap.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getOffers(Context context){
        offersDatabaseReference = database.getReference("ProductSubCategory");
        offersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mOfferProductsList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    ProductSubCategory model = postSnap.getValue(ProductSubCategory.class);
                    mOfferProductsList.add(model);
                }
                Collections.reverse(mOfferProductsList);
                notifyOffersChange(mOfferProductsList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyOffersCancelled(databaseError);
            }
        });
    }

    public void deleteSubCategory(final ProductSubCategory productSubCategory){
        final String offerID = productSubCategory.getId();
        offersDatabaseReference = database.getReference("Sub-Categories");
        offersDatabaseReference.child(offerID).removeValue();
        //delete related products objects
        offersDatabaseReference = database.getReference("Products");
        offersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    //delete Related productSubCategory Object in firebase
                    if(model.getSubCategory().equals(productSubCategory.getSubCategory())){
                        postSnap.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        offersDatabaseReference = database.getReference("ProductSubCategory");
        offersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    ProductSubCategory model = postSnap.getValue(ProductSubCategory.class);
                    //delete Related productSubCategory Object in firebase
                    if(model.getId().equals(offerID)){
                        postSnap.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteOffer(ProductSubCategory productSubCategory){
        final String offerID = productSubCategory.getId();
        offersDatabaseReference = database.getReference("ProductSubCategory");
        offersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    ProductSubCategory model = postSnap.getValue(ProductSubCategory.class);
                    //delete Related productSubCategory Object in firebase
                    if(model.getId().equals(offerID)){
                        postSnap.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void notifyOffersChange(List<ProductSubCategory> mProductsList){
        for(Listener listener : getmListeners()){
            listener.onOfferProductChange(mProductsList);
        }
    }
    public void notifyOffersCancelled(DatabaseError error){
        for(Listener listener : getmListeners()){
            listener.onOfferProductCancelled(error);
        }
    }

    public void notifyCategoryChange(List<Category> mCategoriesList){
        for(Listener listener : getmListeners()){
            listener.onProductsChanged(mCategoriesList);
        }
    }
    public void notifyCategoryCancelled(DatabaseError error){
        for(Listener listener : getmListeners()){
            listener.onProductsCancelled(error);
        }
    }
}
