package com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl.OrderControlItemList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.TimeStampFormatter;


public class OrderListItemViewMvcImp extends BaseObservableMvcView<OrderListItemViewMvc.Listener> implements OrderListItemViewMvc {


    private TextView orderId , orderDate ;
    private Button orderStatusBtn , orderShowBtn, orderShareBtn;
    private Order order;
    public OrderListItemViewMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.row_order_control , parent ,false));

        iniViews();

        orderStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onStatusBtnClicked(order);
                }
            }
        });

        orderShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onShowBtnClicked(order);
                }
            }
        });

        orderShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onLocationBtnClicked(order);
                }
            }
        });
    }

    private void iniViews() {
        orderId = findViewById(R.id.orderIdText);
        orderDate = findViewById(R.id.orderDateText);
        orderStatusBtn = findViewById(R.id.statusBtn);
        orderShowBtn = findViewById(R.id.showOrderBtn);
        orderShareBtn = findViewById(R.id.locationOrderBtn);
    }

    @Override
    public void bindData(Order order) {
        this.order = order;
        this.orderId.setText("Order delivery time : " + order.getOrderAtTime().toUpperCase());
        this.orderDate.setText("Order Date : " + TimeStampFormatter.timeStampToString((long)order.getTimeStamp()));
        if(order.getOrderState().equals("In Progress")){
            orderStatusBtn.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        }else{
            orderStatusBtn.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        this.orderStatusBtn.setText(order.getOrderState());
    }
}
