package com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList.UseCases;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchOrdersUseCase extends BaseObservableMvcView<FetchOrdersUseCase.Listener> {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Order> orderList;
    User user = null;
    private final String FIREBASE_PATH = "Orders";

    public interface Listener{
        void onOrdersDataChange(List<Order> orderList , User user);
        void onOrdersDataCancel(DatabaseError error);
        void onFinishedOrdersGot(List<Order> finishedOrders);
    }

    public FetchOrdersUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getOrdersOfUser(final String userID){
         final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = database.getReference(FIREBASE_PATH);
        orderList = new ArrayList<>();
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Order model = postSnap.getValue(Order.class);
                    //get only the orders of the cureent user
                    if(model.getUserId().equals(userID)){
                        orderList.add(model);
                    }
                }
                Collections.reverse(orderList);
                notifyDataChange(orderList , user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
        //get related user data
        databaseReference = database.getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    User model = postSnap.getValue(User.class);
                    user = model;
                }
                notifyDataChange(orderList , user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }

    public void getAllCurrentUserOrders(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = database.getReference(FIREBASE_PATH);
        orderList = new ArrayList<>();
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Order model = postSnap.getValue(Order.class);
                    //get only the orders of the current user
                    if(model.getUserId().equals(uid)){
                        orderList.add(model);
                    }

                }
                Collections.reverse(orderList);
                notifyDataChange(orderList , user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
        //get related user data
        databaseReference = database.getReference("Users");
        databaseReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    User model = postSnap.getValue(User.class);
                        user = model;
                }
                notifyDataChange(orderList , user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }
    public void getAllNotFinishedOrders(){
        databaseReference = database.getReference(FIREBASE_PATH);
        orderList = new ArrayList<>();
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Order model = postSnap.getValue(Order.class);
                    if(model.getOrderState().equals("Done")){
                        continue;
                    }
                        orderList.add(model);
                }
                Collections.reverse(orderList);
                notifyDataChange(orderList , user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }

    public void getAllFinishedOrders(){
        databaseReference = database.getReference(FIREBASE_PATH);
        orderList = new ArrayList<>();
        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Order model = postSnap.getValue(Order.class);
                    if(model.getOrderState().equals("Done")){
                        orderList.add(model);
                    }

                }
                Collections.reverse(orderList);
                notifyFinishedOrders(orderList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyDataCancelled(databaseError);
            }
        });
    }

    private void notifyDataCancelled(DatabaseError databaseError) {
        for(Listener listener : getmListeners()){
            listener.onOrdersDataCancel(databaseError);
        }
    }

    private void notifyDataChange(List<Order> orders , User user) {
        for(Listener listener : getmListeners()){
            listener.onOrdersDataChange(orders , user);
        }
    }
    private void notifyFinishedOrders(List<Order> finishedOrders){
        for(Listener listener : getmListeners()){
            listener.onFinishedOrdersGot(finishedOrders);
        }
    }

}
