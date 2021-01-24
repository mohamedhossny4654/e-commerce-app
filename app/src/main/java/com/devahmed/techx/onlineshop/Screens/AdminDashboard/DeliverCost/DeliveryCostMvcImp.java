package com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.DeliverCost;
import com.devahmed.techx.onlineshop.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryCostMvcImp extends BaseObservableMvcView<DeliveryCostMvc.Listener> implements DeliveryCostMvc {

    List<DeliverCost> deliverCostList;
    RecyclerView recyclerView;
    DeliverCostAdapter adapter;
    Button submitBtn;
    ProgressBar progressBar;
    public DeliveryCostMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.delivery_cost_fragment , parent  , false));

        initViews();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSubmitBtnClicked();
                }
            }
        });
        submitBtn.setEnabled(false);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.deliveryCostRecyclerView);
        deliverCostList = new ArrayList<>();
        submitBtn = findViewById(R.id.deliveryCostSubmitBtn);
        progressBar = findViewById(R.id.progressBar);
        adapter = new DeliverCostAdapter(deliverCostList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bindData(List<DeliverCost> deliverCosts) {
        this.deliverCostList = deliverCosts;
        submitBtn.setEnabled(true);
        adapter.setList(deliverCostList);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public List<DeliverCost> getDeliverCostList(){
        return adapter.getDeliverCostsList();
    }
}
