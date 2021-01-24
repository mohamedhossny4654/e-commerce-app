package com.devahmed.techx.onlineshop.Screens.Cart.UseCase;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Utils.CartManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchCartDataUseCase extends BaseObservableMvcView<FetchCartDataUseCase.Listener> {

    private CartManager prodctsIDCartManager , productCountCartManagaer;
    private List<String> cartProductIdList , cartProductCountList;
    private Activity context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Product> productList;
    private List<String>  productsCountList;
    private final String FIREBASE_PATH = "Products";

    public FetchCartDataUseCase(Activity context, FirebaseDatabase database) {
        this.context = context;
        this.database = database;
        prodctsIDCartManager = new CartManager(context , "productsId");
        productCountCartManagaer = new CartManager(context , "productCount");
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
    }

    public interface Listener{
        void onProductsChanged(List<Product> productList, List<String> prodctCountsList);
        void onProductsCanceled(DatabaseError error);
    }

    public void getProductsAtCart(){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                productsCountList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Product model = postSnap.getValue(Product.class);
                    if(cartProductIdList.contains(model.getId())){
                        int index = cartProductIdList.indexOf(model.getId());
                        int counter = Integer.parseInt(cartProductCountList.get(index));
                        if(counter > 0){
                            //if this products id is exist in CartManagerPreference
                            productList.add(model);
                            productsCountList.add("" + counter);
                        }
                    }
                }
                notifyDataChange(productList , productsCountList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }

    private void notifyDataCancelled(DatabaseError databaseError) {
        for(Listener listener : getmListeners() ){
            listener.onProductsCanceled(databaseError);
        }
    }


    private void notifyDataChange(List<Product> productList , List<String> prodctCountsList) {
        for(Listener listener : getmListeners() ){
            listener.onProductsChanged(productList , prodctCountsList);
        }
    }
}
