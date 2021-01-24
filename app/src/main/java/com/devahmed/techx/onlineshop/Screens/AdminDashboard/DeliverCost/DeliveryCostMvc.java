package com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost;

import com.devahmed.techx.onlineshop.Models.DeliverCost;

import java.util.List;

public interface DeliveryCostMvc {
    interface Listener{
        void onSubmitBtnClicked();
    }

    void bindData(List<DeliverCost>  deliverCosts);
    void showProgressBar();
    void hideProgressBar();

}
