package com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchProductsUseCase extends BaseObservableMvcView<FetchProductsUseCase.Listener> {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Product> productList;

    private final String FIREBASE_PATH = "Products";
    public interface Listener{
        void onProductsDataChange(List<Product> products);
        void onProductsDataCancel(DatabaseError error);
    }

    public FetchProductsUseCase(FirebaseDatabase database) {
        this.database = database;
    }
    //subCategory
    /*
    * subCategory has a name , image and List<SubCategory> subcategories;
    * sub-Category has - name , image and List<Product>
    * each subCategory has some sub-categories
    * which means that
    * */
    public void getProductsOfCategory(String subCategory , Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userBranch", 0);
        final String userBranch = sharedPreferences.getString("userBranch" , "-MBmkpc5lGedp8KOau7g");
        System.out.println("user branch is " + userBranch);
        databaseReference = database.getReference("Products");
        databaseReference.orderByChild("subCategory").equalTo( subCategory ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    if(model.getBranch().equals(userBranch)){
                        productList.add(model);
                    }
                }
                // Sorting
                Collections.sort(productList);
//                Collections.reverse(productList);
                notifyDataChange(productList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }
    public void getAllProducts(){
        databaseReference = database.getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    productList.add(model);
                }
                Collections.reverse(productList);
                notifyDataChange(productList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }
    public void getCategoriesWithIDs(final ArrayList<String> ids){
        databaseReference = database.getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    if(ids.contains(model.getId())){
                        productList.add(model);
                    }
                }
                Collections.reverse(productList);
                notifyDataChange(productList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }
    public void updateProductsCounts(final List<String> ids , final List<Integer> countList){
        databaseReference = database.getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    if(ids.contains(model.getId())){
                        int index = ids.indexOf(model.getId());
                        //update products Counts
                        model.setMaxCount(model.getMaxCount() - countList.get(index));
                        updateExistingProduct(model);
                        //list of updated products
                        productList.add(model);
                    }
                }
                //delete related offers of productSubCategory
                for (int i = 0; i < productList.size(); i++) {
                    if(productList.get(i).getMaxCount() <= 0){
                        final Product product = productList.get(i);
                        databaseReference = database.getReference("ProductSubCategory");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                                    ProductSubCategory model = postSnap.getValue(ProductSubCategory.class);
                                    if(model.getId().equals(product.getId())){
                                        postSnap.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }

    public void updateExistingProduct(Product product){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = product.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(product.getId(), postValues);
        reference.updateChildren(childUpdates);
    }

    public void deleteCategory(String productId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH).push();
        reference = database.getReference(FIREBASE_PATH);
        reference.child(productId).removeValue();
    }

    private void notifyDataCancelled(DatabaseError databaseError) {
        for(Listener listener : getmListeners()){
            listener.onProductsDataCancel(databaseError);
        }
    }

    private void notifyDataChange(List<Product> productList) {
        for(Listener listener : getmListeners()){
            listener.onProductsDataChange(productList);
        }
    }
}
