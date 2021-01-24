package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl.BillsItemList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.BillCanvasView;
import com.devahmed.techx.onlineshop.Utils.TimeStampFormatter;

import java.util.List;

public class BillsItemMvcImp extends BaseObservableMvcView<BillsItemMvc.Listener> implements BillsItemMvc {

    ImageButton downloadBill , shareBill;
    TextView billNumberText , dateText;
    LinearLayout billContainerLinearLayout;
    Order order;
    BillCanvasView billCanvasView , temp;

    public BillsItemMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.bill_row_item , parent ,false));

        initViews();
        billContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onBillItemClicked(order ,temp);
                }
            }
        });

        downloadBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onDownloadItemClicked(order , temp );
                }
            }
        });

    }

    private void initViews() {
        downloadBill = findViewById(R.id.billDownloadBtn);
        billContainerLinearLayout = findViewById(R.id.billContainer);
        billNumberText = findViewById(R.id.billNumber);
        dateText = findViewById(R.id.dateText);
    }


    @Override
    public void bindData(Order order , List<Product> productList , User user , int position) {
        this.order = order;
        this.billCanvasView = new BillCanvasView(getContext() , order , productList , user);
        this.temp = new BillCanvasView(getContext() , order , productList , user);
        //this line add canvasview to linearlayout wich is bad !!!
//        billContainerLinearLayout.addView(billCanvasView);
        //instead we show only date of this bill with its position
        dateText.setText(TimeStampFormatter.timeStampToString((long)order.getTimeStamp()));
        billNumberText.setText("#" + position);
    }

}
