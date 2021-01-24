package com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl;

import com.devahmed.techx.onlineshop.Models.Order;

import java.util.List;

public interface OrdersControlMvc {
    interface Listener{
        void onItemClicked(Order order);
        void onShowBtnClicked(Order order);
        void onShareBtnClicked(Order order);
        void onStatusBtnClicked(Order order);
    }

    void bindData(List<Order> orders );
    void hideProgressbar();
    void showProgressbar();
}
