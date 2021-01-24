package com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;


public class OrderListMvcImp extends BaseObservableMvcView<OrdersListMvc.Listener> implements OrdersListMvc {


    private RecyclerView recyclerView;
    private List<Order> orderList;
    private OrdersAdapter adapter;
    private ProgressBar progressBar;
    public OrderListMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.orders_list_fragment , parent ,false));
        recyclerView = findViewById(R.id.ordersRecycler);
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for(Listener listener : getmListeners()){
                    listener.onOrderItemClicked(orderList.get(position));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void bindData(List<Order> orderList, User user) {
        if(orderList != null){
            this.orderList = orderList;
            adapter.setList(orderList);
        }
        if(user != null){
            //bind user data
        }
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }
}
