package com.devahmed.techx.onlineshop.Screens.AdminDashboard.HowDidYouFindUsControl.UseCases;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchStringesUseCase extends BaseObservableMvcView<FetchStringesUseCase.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<String> productList;

    private final String FIREBASE_PATH = "HowDidYouHearAboutUs";

    public interface Listener {
        void onStringDataChange(List<String> dataList);

        void onStringDataCancel(DatabaseError error);
    }

    public FetchStringesUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getAllStringes() {
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
