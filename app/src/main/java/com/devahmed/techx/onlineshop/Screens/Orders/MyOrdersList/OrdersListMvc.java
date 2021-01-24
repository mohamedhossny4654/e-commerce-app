package com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;

import java.util.List;

public interface OrdersListMvc {
    interface Listener{
        void onOrderItemClicked(Order order);
    }

    void bindData(List<Order> orderList , User  user);
    void showProgressbar();
    void hideProgressbar();
}
