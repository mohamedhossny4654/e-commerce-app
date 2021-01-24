package com.devahmed.techx.onlineshop.Screens.Home.OffersSliderList;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.CartManager;

import java.util.List;

public class OffersListItemViewMvcImp extends BaseObservableMvcView<OffersListItemView.Listener>
        implements OffersListItemView {



    TextView mTitle , mPrice , sliderTitle2 , productPrice;
    ImageView mImage , imageView2;
    CardView subCategorySlide , productSlide;
    ProductSubCategory productSubCategory;
    LinearLayout productControlContainerLinearLayout , itemsCountControls;
    Button addToCartBtn;
    Dialog Dialog;
    private CartManager prodctsIDCartManager , productCountCartManagaer;
    private List<String> cartProductIdList , cartProductCountList;
    private int counter = 1;

    public OffersListItemViewMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.row_sliderview , parent , false));
        prodctsIDCartManager = new CartManager(getContext() , "productsId");
        productCountCartManagaer = new CartManager(getContext() , "productCount");
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
//        prodctsIDCartManager.deleteAllKeys();
//        productCountCartManagaer.deleteAllKeys();
        mTitle = findViewById(R.id.slideViewTitle);
        mImage = findViewById(R.id.slideViewImage);
        itemsCountControls = findViewById(R.id.cartControlsContainer);
        addToCartBtn = findViewById(R.id.add_to_cart_Btn);
        subCategorySlide = findViewById(R.id.subcCategorySlide);
        productSlide = findViewById(R.id.productSlide);
        sliderTitle2 =findViewById(R.id.sliderTitle2);
        imageView2 = findViewById(R.id.imageView2);
        productPrice = findViewById(R.id.productPrice);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartBtn.setVisibility(View.GONE);
//                showAddDialog();
                if(!cartProductIdList.contains(productSubCategory.getId())){
                    //add a new product to cart manager
                    prodctsIDCartManager.addToCart(productSubCategory.getId());
                    productCountCartManagaer.addToCart("1");
                }else{
                    //if the id is exist but its count is zero => just increase the counter
                    //this case happen when user increase the counter then decrease it again
                    int index = cartProductIdList.indexOf(productSubCategory.getId());
                    productCountCartManagaer.replaceKey(index , "1");
                }
                //update the lists of id's with the new data
                cartProductIdList = prodctsIDCartManager.getStoredValues();
                cartProductCountList = productCountCartManagaer.getStoredValues();
                for(Listener listener : getmListeners()){
                    listener.onAddToCartBtnClicked(productSubCategory);
                }
            }


        });

        productControlContainerLinearLayout = findViewById(R.id.productControls);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(OffersListItemView.Listener listener : getmListeners()){
                    listener.onItemClicked(productSubCategory);
                }
            }
        });
        getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!getContext().getResources().getString(R.string.admin).equalsIgnoreCase("true")){
                    return false;
                }
                for(Listener listener : getmListeners()){
                    listener.onItemLongClicked(productSubCategory);
                }
                return false;
            }
        });
    }

    @Override
    public void bindData(ProductSubCategory product , boolean showAddToCartBtn) {
        this.productSubCategory = product;
        if(product.getType() == ProductSubCategory.TYPE_SUB_CATEGORY){
            subCategorySlide.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(product.getImage()).placeholder(R.drawable.images_placeholder).into(mImage);
            mTitle.setText("" + product.getTitle());

        }else if(product.getType() == ProductSubCategory.TYPE_PRODUCT){
            productSlide.setVisibility(View.VISIBLE);
            productPrice.setText("" +product.getPrice() + "-EG");
            sliderTitle2.setText("" + product.getTitle());
            if(showAddToCartBtn == true){
                addToCartBtn.setVisibility(View.VISIBLE);
            }else{
                addToCartBtn.setVisibility(View.INVISIBLE);
            }

            Glide.with(getContext()).load(product.getImage()).placeholder(R.drawable.images_placeholder).into(imageView2);
        }

    }

}
