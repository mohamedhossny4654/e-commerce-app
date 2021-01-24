package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases;

import androidx.annotation.NonNull;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Branch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchBranchesUseCase extends BaseObservableMvcView<FetchBranchesUseCase.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Branch> productList;

    private final String FIREBASE_PATH = "Branches";
    public interface Listener{
        void onBranchDataChange(List<Branch> dataList);
        void onBranchDataCancel(DatabaseError error);
    }

    public FetchBranchesUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getAllBranches(){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Branch model = postSnap.getValue(Branch.class);
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

    public void getBranchOfId(String id){
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Branch model = postSnap.getValue(Branch.class);
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
    private void notifyDataCancelled(DatabaseError databaseError) {
        for(Listener listener : getmListeners()){
            listener.onBranchDataCancel(databaseError);
        }
    }

    private void notifyDataChange(List<Branch> dataList) {
        for(Listener listener : getmListeners()){
            listener.onBranchDataChange(dataList);
        }
    }
}
