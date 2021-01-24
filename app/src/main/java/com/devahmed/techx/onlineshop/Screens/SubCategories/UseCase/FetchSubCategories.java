package com.devahmed.techx.onlineshop.Screens.SubCategories.UseCase;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchSubCategories extends BaseObservableMvcView<FetchSubCategories.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<SubCategory>  subCategoryList;
    private final String FIREBASE_PATH = "Sub-Categories";
    public interface Listener{
        void onSubCategorySuccess(List<SubCategory> subCategories);
        void onSubCategoryCanceled(DatabaseError error);
    }

    public FetchSubCategories(FirebaseDatabase database) {
        this.database = database;
    }

    public void getSubCategories(String category){
        databaseReference = database.getReference("Sub-Categories");
        databaseReference.orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subCategoryList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    SubCategory model = postSnap.getValue(SubCategory.class);
                    subCategoryList.add(model);
                }
//                Collections.reverse(subCategoryList);
                notifyCategoryChange(subCategoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCategoryCancelled(databaseError);
            }
        });
    }

    public void deleteCategory(String SubcategoryID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(FIREBASE_PATH).push();
        reference = database.getReference(FIREBASE_PATH);
        reference.child(SubcategoryID).removeValue();
    }

    private void notifyCategoryChange(List<SubCategory> subCategoryList) {
        for(Listener listener : getmListeners()){
            listener.onSubCategorySuccess(subCategoryList);
        }
    }

    private void notifyCategoryCancelled(DatabaseError error) {
        for(Listener listener : getmListeners()){
            listener.onSubCategoryCanceled(error);
        }
    }
}
