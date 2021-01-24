package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl.UseCase;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
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

public class FetchStringesUseCase extends BaseObservableMvcView<FetchStringesUseCase.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<String> productList;

    private final String FIREBASE_PATH = "BillCounter";

    public interface Listener {
        void onStringDataChange(List<String> dataList);

        void onStringDataCancel(DatabaseError error);
    }

    public FetchStringesUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getBillCounter() {
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    String model = postSnap.getValue(String.class);
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
    public void updateBillCounter(String newCount){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("1" , newCount);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("1", postValues);
        reference.updateChildren(childUpdates);
    }
    private void notifyDataCancelled(DatabaseError databaseError) {
        for (Listener listener : getmListeners()) {
            listener.onStringDataCancel(databaseError);
        }
    }

    private void notifyDataChange(List<String> dataList) {
        for (Listener listener : getmListeners()) {
            listener.onStringDataChange(dataList);
        }
    }
}
