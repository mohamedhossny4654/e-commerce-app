package com.devahmed.techx.onlineshop.Screens.Orders.OrdersDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailsMvcImp extends BaseObservableMvcView<OrderDetailsMvc.Listener> implements OrderDetailsMvc {

    RecyclerView orderedItemsRecyclerView;
    List<Product> productList;
    List<String> ordersCounts;
    OrderedProducstAdapter adapter;
    CircleImageView inprogrressImageView , deliveringImageView , deliveredImage;
    View line1 , line2;
    LinearLayout adminControls;
    TextView totalItemsText, itemsPriceText , deliveryCostPrice
            , totalPriceText , areaText , streetText
            , buildingText , uniqueSignText , yourOrderTextView , yourAddressTextView;
    TextView orderStateText1 ,orderStateText2 , orderStateText3 ,thankYouTextView
             , ItemsCountTextView , ItemsPriceTextView , deliveryCostTextView , totalPriceTextView;
    ProgressBar progressBar;
    Button callUser ,showUserLocation, cancelOrderBtn , editOrder;
    User user;


    public OrderDetailsMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.order_details_fragment , parent ,false));
        initViews();
        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onCallUserBtnClicked(user);
                }
            }
        });

        cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onCancelOrderBtnClicked();
                }
            }
        });

        editOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        showUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onShowLocationBtnClicked(user);
                }
            }
        });
    }

    private void initViews() {
        orderedItemsRecyclerView = findViewById(R.id.myOrderedItemRecycler);
        progressBar = findViewById(R.id.progressBar);
        totalItemsText = findViewById(R.id.totalItemsText);
        itemsPriceText = findViewById(R.id.itemsPriceText);
        cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
        showUserLocation = findViewById(R.id.showUserLocationBtn);
        adminControls = findViewById(R.id.adminControls);
        orderStateText1 = findViewById(R.id.orderStateText1);
        orderStateText2 = findViewById(R.id.orderStateText2);
        orderStateText3 = findViewById(R.id.orderStateText3);
        ItemsCountTextView = findViewById(R.id.itemsCountTextView);
        editOrder = findViewById(R.id.editOrderBtn);
        thankYouTextView = findViewById(R.id.thankYouTextView);
        ItemsPriceTextView = findViewById(R.id.itemsPriceTextView);
        deliveryCostTextView = findViewById(R.id.deliveryCostTextViews);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        yourOrderTextView  = findViewById(R.id.yourOrderTextView);
        yourAddressTextView = findViewById(R.id.yourAddressTextView);
        inprogrressImageView = findViewById(R.id.inprogressImage);
        callUser = findViewById(R.id.callUserBtnJustForAdmin);
        deliveringImageView = findViewById(R.id.deliveringImage);
        deliveredImage = findViewById(R.id.deliveredImage);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        deliveryCostPrice = findViewById(R.id.deliveryCostPrice);
        totalPriceText = findViewById(R.id.totalPriceText);
        areaText = findViewById(R.id.areaText);
        ordersCounts = new ArrayList<>();
        streetText = findViewById(R.id.streetText);
        buildingText = findViewById(R.id.buildingText);
        uniqueSignText = findViewById(R.id.uniqueSignText);
        productList = new ArrayList<>();
        adapter = new OrderedProducstAdapter(productList);
        orderedItemsRecyclerView.setHasFixedSize(true);
        orderedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
        orderedItemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void bindOrderedItemsData(List<Product> productList , List<String> productCounts) {
        this.productList = productList;
        this.ordersCounts = productCounts;
        adapter.setList(productList , productCounts);
    }

    @Override
    public void bindUserData(User user) {
        this.user = user;
        areaText.setText(getContext().getResources().getString(R.string.area) + " : " + user.getArea());
        streetText.setText(getContext().getResources().getString(R.string.street_number)+ " : " + user.getStreet());
        buildingText.setText(getContext().getResources().getString(R.string.building_number)+ " : " + user.getFlat());
        uniqueSignText.setText(getContext().getResources().getString(R.string.nearest_landmark)+ " : "+ user.getNearestLandmark());
    }

    @Override
    public void bindPaymentsData(int totalItems, double deliveryCost, double totalPrice) {
        totalItemsText.setText("" + totalItems);
        deliveryCostPrice.setText("" + deliveryCost);
        itemsPriceText.setText("" +totalPrice);
        totalPriceText.setText("" + (totalPrice + deliveryCost));
    }

    @Override
    public void bindOrderState(String orderState) {

        if(orderState.equals("In Progress")){
            orderState = getContext().getString(R.string.inProgress);
            inprogrressImageView.setImageResource(R.drawable.ic_inprogress_24dp);
            inprogrressImageView.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
        }else if(orderState.equals("Delivering")){
            orderState = getContext().getString(R.string.delivering);
            cancelOrderBtn.setVisibility(View.GONE);
            editOrder.setVisibility(View.GONE);
            deliveringImageView.setImageResource(R.drawable.ic_delivering_24dp);
            inprogrressImageView.setImageResource(R.drawable.ic_delivering_24dp);
            deliveringImageView.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            inprogrressImageView.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            line1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }else if(orderState.equals("Done")){
            orderState = getContext().getString(R.string.done);
            cancelOrderBtn.setVisibility(View.GONE);
            editOrder.setVisibility(View.GONE);
            deliveringImageView.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            inprogrressImageView.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            deliveredImage.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            deliveredImage.setImageResource(R.drawable.ic_delivered_24dp);
            deliveringImageView.setImageResource(R.drawable.ic_delivering_24dp);
            inprogrressImageView.setImageResource(R.drawable.ic_inprogress_24dp);
            line1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            line2.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }
        orderStateText1.setText(getContext().getResources().getString(R.string.your_order_is_in_progress)+ " : " + orderState);
        orderStateText2.setText(getContext().getResources().getString(R.string.your_order)+ " : "+ orderState);
        orderStateText3.setText(getContext().getResources().getString(R.string.your_order_is_in_progress)+ " : " + orderState);

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAdminControls() {
        //show call user btn
        adminControls.setVisibility(View.VISIBLE);
        showUserLocation.setVisibility(View.VISIBLE);
        callUser.setVisibility(View.VISIBLE);
        cancelOrderBtn.setVisibility(View.GONE);
        editOrder.setVisibility(View.GONE);
    }


}
