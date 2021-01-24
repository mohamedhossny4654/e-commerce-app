package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Utils.BillCanvasView;

import java.util.List;

public interface BillsMvc {

    interface Listener{
        void onBillItemClicked(Order order , BillCanvasView billCanvasView);
        void onDownloadItemClicked(Order order, BillCanvasView billCanvasView);
        void onShareBtnClicked(Order order, BillCanvasView billCanvasView);
    }

    void bindData(List<Order> orderList);
    void showProgressbar();
    void hideProgressbar();
    void activateDownloadMode(BillCanvasView billCanvasView);
    void activateNormalMode();

}
