package com.devahmed.techx.onlineshop.Screens.AdminDashboard.ON_OFF;

import com.devahmed.techx.onlineshop.Models.Branch;

import java.util.List;

public interface OnOffMvc {
    interface Listener{
        void onItemClicked(Branch branch);
    }
    void bindData(List<Branch> branches);
    void showProgressbar();
    void hideProgressbar();
}
