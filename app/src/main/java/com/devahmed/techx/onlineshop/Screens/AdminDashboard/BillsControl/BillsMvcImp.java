package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl.BillsItemList.OrdersAdapter;
import com.devahmed.techx.onlineshop.Utils.BillCanvasView;

import java.util.ArrayList;
import java.util.List;

public class BillsMvcImp extends BaseObservableMvcView<BillsMvc.Listener> implements BillsMvc, OrdersAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    OrdersAdapter adapter;
    List<Order> orderList;
    ProgressBar progressBar;
    LinearLayout billContainerLinearLayout;

    public BillsMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.bills_fragment , parent ,false));

        initViews();


    }

    private void initViews() {
        recyclerView = findViewById(R.id.billsRecycler);
        orderList = new ArrayList<>();

        billContainerLinearLayout = findViewById(R.id.billContainer);
        adapter = new OrdersAdapter(orderList , this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void bindData(List<Order> orderList) {
        this.orderList = orderList;
        adapter.setList(orderList);
    }

    @Override
    public void onBillItemClicked(Order order , BillCanvasView billCanvasView) {
        for(Listener listener : getmListeners()){
            listener.onBillItemClicked(order , billCanvasView);
        }
    }

    @Override
    public void onDownloadItemClicked(Order order, BillCanvasView billCanvasView) {
        for(Listener listener : getmListeners()){
            listener.onDownloadItemClicked(order, billCanvasView);
        }
    }

    @Override
    public void onShareBtnClicked(Order order, BillCanvasView billCanvasView) {
        for(Listener listener : getmListeners()){
            listener.onShareBtnClicked(order, billCanvasView);
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

    @Override
    public void activateDownloadMode(BillCanvasView billCanvasView) {

        billContainerLinearLayout.addView(billCanvasView);
        billContainerLinearLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void activateNormalMode() {
        billContainerLinearLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        billContainerLinearLayout.removeAllViews();
    }


}
