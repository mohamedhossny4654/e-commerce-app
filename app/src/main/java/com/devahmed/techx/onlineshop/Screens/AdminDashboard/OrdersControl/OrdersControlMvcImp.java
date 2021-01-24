package com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl.OrderControlItemList.OrdersAdapter;
import java.util.ArrayList;
import java.util.List;

public class OrdersControlMvcImp extends BaseObservableMvcView<OrdersControlMvc.Listener>
        implements OrdersControlMvc , OrdersAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<Order> orderList;
    private ProgressBar progressBar;

    public OrdersControlMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.orders_control_layout , parent , false));

        initViews();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.ordersControlRecycler);
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(getContext() , orderList , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void bindData(List<Order> orders) {
        this.orderList = orders;
        adapter.setList(orders);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnItemClicked(Order order) {
        for(Listener listener : getmListeners()){
            listener.onItemClicked(order);
        }
    }

    @Override
    public void onShowBtnClicked(Order order) {
        for(Listener listener : getmListeners()){
            listener.onShowBtnClicked(order);
        }
    }

    @Override
    public void onLocationBtnClicked(Order order) {
        for(Listener listener : getmListeners()){
            listener.onShareBtnClicked(order);
        }
    }

    @Override
    public void onStatusBtnClicked(Order order) {
        for(Listener listener : getmListeners()){
            listener.onStatusBtnClicked(order);
        }
    }
}
