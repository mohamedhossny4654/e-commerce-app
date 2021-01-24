package com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl.UseCase;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FetchMinChargesUseCase extends BaseObservableMvcView<FetchMinChargesUseCase.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private final String FIREBASE_PATH = "MinCharge";

    public interface Listener {
        void onMinChargeDataChange(Map<String, String> map);

        void onMinChargeDataCancel(DatabaseError error);

        void onInputError(String message);
    }

    public FetchMinChargesUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getMinCharge() {
        databaseReference = database.getReference(FIREBASE_PATH);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = (Map) postSnapshot.getValue();
                    if (map != null) {
                        String message = map.get("minCharge");
                        notifyDataChange(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }
    public void updateExistingMinCharge(String minCharge){
        try{
            if(!(((Integer)Integer.parseInt(minCharge)) instanceof Integer)){
                notifyInputError("Price is not valid");
                return;
            }
        }catch (Exception e){
            notifyInputError("Enter valid price");
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("minCharge" , minCharge);
        postValues.put("id" , "1");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("1", postValues);
        reference.updateChildren(childUpdates);
    }

    private void notifyInputError(String price_is_not_valid) {
        for(Listener listener :getmListeners()){
            listener.onInputError(price_is_not_valid);
        }
    }

    private void notifyDataCancelled(DatabaseError databaseError) {
        for (Listener listener : getmListeners()) {
            listener.onMinChargeDataCancel(databaseError);
        }
    }

    private void notifyDataChange(Map<String, String> map) {
        for (Listener listener : getmListeners()) {
            listener.onMinChargeDataChange(map);
        }
    }
}
