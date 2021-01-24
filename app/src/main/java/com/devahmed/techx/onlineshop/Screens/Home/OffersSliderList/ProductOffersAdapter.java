package com.devahmed.techx.onlineshop.Screens.Home.OffersSliderList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.Utils.CartManager;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ProductOffersAdapter extends SliderViewAdapter<ProductOffersAdapter.ViewHolder> implements OffersListItemView.Listener  {

    private OnOfferItemClickListener mListener;
    private CartManager prodctsIDCartManager , productCountCartManagaer;
    private List<String> cartProductIdList , cartProductCountList;
    private Context context;
    @Override
    public int getCount() {
        return ProductsList.size();
    }


    public interface OnOfferItemClickListener {
        void OnItemClicked(ProductSubCategory category);
        void OnItemLongClicked(ProductSubCategory subCategory);
        void onAddToCartBtnClicked(ProductSubCategory productSubCategory);
        void onIncreaseBtnClicked(ProductSubCategory productSubCategory);
        void onDecreaseBtnClicked(ProductSubCategory productSubCategory);
        void onOkBtnClicked(ProductSubCategory productSubCategory);
    }

    private List<ProductSubCategory> ProductsList;

    public ProductOffersAdapter(Context context , List<ProductSubCategory> ProductsList, OnOfferItemClickListener listener) {
        this.ProductsList = ProductsList;
        this.mListener = listener;
        this.context = context;
        prodctsIDCartManager = new CartManager(context , "productsId");
        productCountCartManagaer = new CartManager(context , "productCount");
//        prodctsIDCartManager.deleteAllKeys();
//        productCountCartManagaer.deleteAllKeys();
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        OffersListItemViewMvcImp mvcImp = new OffersListItemViewMvcImp(LayoutInflater.from(parent.getContext()), parent);
        mvcImp.registerListener(this);
        return new ViewHolder(mvcImp);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductSubCategory model = ProductsList.get(position);
        boolean showAddToCartBtn = true;
        if(model.getType() == ProductSubCategory.TYPE_PRODUCT){
            if(cartProductIdList.contains(model.getId())){
                //this items is already in the cart => no need to show add to cart btn
                int index = cartProductIdList.indexOf(model.getId());
                int countOfItems = Integer.parseInt(cartProductCountList.get(index));
                if(countOfItems > 0){
                    showAddToCartBtn = false;
                }
            }
        }
        holder.recyclerViewListItemMvcImp.bindData(model , showAddToCartBtn);
    }


    public void setList(List<ProductSubCategory> newList) {
        ProductsList = newList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends SliderViewAdapter.ViewHolder {

        private final OffersListItemViewMvcImp recyclerViewListItemMvcImp;

        public ViewHolder(OffersListItemViewMvcImp viewMvcImp) {
            super(viewMvcImp.getRootView());
            recyclerViewListItemMvcImp = viewMvcImp;
        }
    }


    @Override
    public void onItemClicked(ProductSubCategory model) {
        mListener.OnItemClicked(model);
    }

    @Override
    public void onItemLongClicked(ProductSubCategory subCategory) {
        mListener.OnItemLongClicked(subCategory);
    }

    @Override
    public void onAddToCartBtnClicked(ProductSubCategory productSubCategory) {
        mListener.onAddToCartBtnClicked(productSubCategory);
    }

    @Override
    public void onIncreaseBtnClicked(ProductSubCategory productSubCategory) {
        mListener.onIncreaseBtnClicked(productSubCategory);
    }

    @Override
    public void onDecreaseBtnClicked(ProductSubCategory productSubCategory) {
        mListener.onDecreaseBtnClicked(productSubCategory);
    }

    @Override
    public void onOkBtnClicked(ProductSubCategory productSubCategory) {
        mListener.onOkBtnClicked(productSubCategory);
    }

}