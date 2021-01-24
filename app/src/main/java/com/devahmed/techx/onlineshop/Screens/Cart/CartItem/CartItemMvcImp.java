package com.devahmed.techx.onlineshop.Screens.Cart.CartItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;

public class CartItemMvcImp extends BaseObservableMvcView<CartItemsMvc.Listener> implements CartItemsMvc{

    private Product product;
    private int counter ;
    double  totalItemPrice;
    private ImageView productImage;
    private TextView productTitleTextView , cartProductItemTotalPrice , noOfItemsInCartText;
    private ImageView delteItemBtn;
    private ImageView increaseCounterBtn , decreaseCounterBtn;
    private ConstraintLayout containerLayout;

    public CartItemMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.row_cart_item , parent, false));
        initViews();
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onProductImageClicked(product);
                }
            }
        });
        increaseCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter >= product.getMaxCount()){
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.items_finished), Toast.LENGTH_SHORT).show();
                }else{
                    counter += 1;
                    noOfItemsInCartText.setText("" + counter);
                    totalItemPrice += product.getPrice();
                    cartProductItemTotalPrice.setText("" + totalItemPrice + "-EP");
                    for(Listener listener : getmListeners()){
                        listener.onIncreaseBtnClicked(product);
                    }
                }
            }
        });
        decreaseCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter -= 1;
                if(counter <= 0){
//                    containerLayout.setVisibility(View.GONE);
                }else{
                    noOfItemsInCartText.setText("" + counter);
                    totalItemPrice -= product.getPrice();
                    cartProductItemTotalPrice.setText("" + totalItemPrice + "-EP");
                }
                for(Listener listener : getmListeners()){
                    listener.onDecreaseBtnCLicked(product);
                }
            }
        });
    }

    private void initViews() {
        productImage = findViewById(R.id.row_cart_productImage);
        productTitleTextView = findViewById(R.id.productTitleTextView);
        delteItemBtn = findViewById(R.id.delteItemBtn);
        increaseCounterBtn = findViewById(R.id.increaseCounterBtn);
        decreaseCounterBtn = findViewById(R.id.decreaseCounterBtn);
        containerLayout = findViewById(R.id.containerLayout);
        cartProductItemTotalPrice = findViewById(R.id.cartProductItemTotalPrice);
        noOfItemsInCartText = findViewById(R.id.noOfItemsInCartText);
    }

    @Override
    public void bindData(Product product, int count) {
        this.product = product;
        this.counter = count;
        this.totalItemPrice = product.getPrice() * count * 1.0;
        noOfItemsInCartText.setText("" + count);
        Glide.with(getContext())
                .load(product.getImage())
                .placeholder(R.drawable.images_placeholder)
                .into(productImage);
        productTitleTextView.setText(product.getTitle());
        cartProductItemTotalPrice.setText("" + totalItemPrice + "-EP");
    }
}
