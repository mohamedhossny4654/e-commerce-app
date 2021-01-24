package com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl.OrderControlItemList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> implements OrderListItemViewMvc.Listener, FetchUserInfoFromFirebaseUseCase.Listener {

    private OnItemClickListener mListener;
    private FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    private List<User> userList;
    private String adminBranch;
    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {

    }

    public interface OnItemClickListener {
        void OnItemClicked(Order Order);
        void onShowBtnClicked(Order order);
        void onLocationBtnClicked(Order order);
        void onStatusBtnClicked(Order order);
    }

    private List<Order> OrdersList;

    public OrdersAdapter(Context context , List<Order> OrdersList, OnItemClickListener listener) {
        this.OrdersList = OrdersList;
        this.mListener = listener;
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        userInfoFromFirebaseUseCase.registerListener(this);
        userList = new ArrayList<>();
        userInfoFromFirebaseUseCase.getAllUsers();
        SharedPreferences sharedPreferences = context.getSharedPreferences("userBranch" , 0);
        this.adminBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderListItemViewMvcImp mvcImp = new OrderListItemViewMvcImp(LayoutInflater.from(parent.getContext()), parent);
        mvcImp.registerListener(this);
        return new ViewHolder(mvcImp);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order model = OrdersList.get(position);
        for (int i = 0; i <userList.size() ; i++) {
            //if this order is related to this user
            if(model.getUserId().equals(userList.get(i).getUserId())){
                //if this user is in the same branch with this admin =>show his order for this admin
                if(adminBranch.equals(userList.get(i).getNearestBranch())){
                    holder.recyclerViewListItemMvcImp.bindData(model);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return OrdersList.size();
    }

    public void setList(List<Order> newList) {
        OrdersList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final OrderListItemViewMvcImp recyclerViewListItemMvcImp;

        public ViewHolder(OrderListItemViewMvcImp viewMvcImp) {
            super(viewMvcImp.getRootView());
            recyclerViewListItemMvcImp = viewMvcImp;
        }
    }


    @Override
    public void onItemClicked(Order model) {
        mListener.OnItemClicked(model);
    }

    @Override
    public void onShowBtnClicked(Order order) {
        mListener.onShowBtnClicked(order);
    }

    @Override
    public void onLocationBtnClicked(Order order) {
        mListener.onLocationBtnClicked(order);
    }

    @Override
    public void onStatusBtnClicked(Order order) {
        mListener.onStatusBtnClicked(order);
    }

}