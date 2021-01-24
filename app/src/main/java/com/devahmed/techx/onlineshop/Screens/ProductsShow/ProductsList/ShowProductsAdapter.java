package com.devahmed.techx.onlineshop.Screens.ProductsShow.ProductsList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Utils.CartManager;

import java.util.List;

public class ShowProductsAdapter extends RecyclerView.Adapter<ShowProductsAdapter.ViewHolder>
        implements ShowProductListItemView.Listener {

    private OnItemClickListener mListener;
    private CartManager prodctsIDCartManager , productCountCartManagaer;
    private List<String> cartProductIdList , cartProductCountList;
    private int count;
    private static int position;
    @Override
    public void onImageClicked(Product product) {
        mListener.onImageClicked(product);
    }

    @Override
    public void onImageLongClicked(Product product) {
        mListener.onImageLongClicked(product);
    }

    @Override
    public void onIncreaseItemsBtnClicked(Product product) {
        //if products is already exist in the cart => just increase its counter
        int index = cartProductIdList.indexOf(product.getId());
        int newKey = Integer.parseInt(cartProductCountList.get(index)) + 1;
        productCountCartManagaer.replaceKey(index , "" + newKey);
        //update the lists of id's with the new data
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
        mListener.onIncreaseItemsBtnClicked();
    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onDecreaseItemsBtnClicked(Product product) {
        //decrease the counter in CartManager
        int index = cartProductIdList.indexOf(product.getId());
        int newKey = Integer.parseInt(cartProductCountList.get(index)) - 1;
        productCountCartManagaer.replaceKey(index , "" + newKey);
        //update the lists of id's with the new data
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();

        mListener.onDecreaseItemsBtnClicked();
    }

    @Override
    public void onAddToCartBtnClicked(Product product) {
        //invoke the method
        mListener.onAddToCartBtnClicked(product);

        if(!cartProductIdList.contains(product.getId())){
            //add a new product to cart manager
            prodctsIDCartManager.addToCart(product.getId());
            productCountCartManagaer.addToCart("1");
        }else{
            //if the id is exist but its count is zero => just increase the counter
            //this case happen when user increase the counter then decrease it again
            int index = cartProductIdList.indexOf(product.getId());
            productCountCartManagaer.replaceKey(index , "1");
        }
        //update the lists of id's with the new data
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
    }

    public interface OnItemClickListener {
        void onImageClicked(Product Product);
        void onAddToCartBtnClicked(Product product);
        void onImageLongClicked(Product product);
        void onIncreaseItemsBtnClicked();
        void onDecreaseItemsBtnClicked();
    }

    private List<Product> ProductsList;

    public ShowProductsAdapter(List<Product> ProductsList, OnItemClickListener listener , Context context) {
        this.ProductsList = ProductsList;
        this.mListener = listener;
        prodctsIDCartManager = new CartManager(context , "productsId");
        productCountCartManagaer = new CartManager(context , "productCount");
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShowProductsListItemViewMvcImp mvcImp = new ShowProductsListItemViewMvcImp(LayoutInflater.from(parent.getContext()), parent);
        mvcImp.registerListener(this);
        return new ViewHolder(mvcImp);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        this.position = position;
        Product model = ProductsList.get(position);
        //if we found this product id in the cart ... we show it in a different way
        if(cartProductIdList.contains(model.getId())){
            int index = cartProductIdList.indexOf(model.getId());
            int counter = Integer.parseInt(cartProductCountList.get(index));
            if( counter > 0){
                //if the counter is more than 0 then show the number if not => show add to cart btn only
                holder.recyclerViewListItemMvcImp.bindDataForAddedToCartProduct(model
                        , Integer.valueOf(cartProductCountList.get(index)));
            }
        }
        count = model.getMaxCount();
        System.out.println("count of " + model.getTitle() + "  is " + count);
        if(count <= 0){
            //bind that this item is not available now according to count leak in store
            holder.recyclerViewListItemMvcImp.bindData(model);
        }else{
            //bind a default product row data
            holder.recyclerViewListItemMvcImp.bindData(model);
        }
    }


    @Override
    public int getItemCount() {
        return ProductsList.size();
    }

    public void setList(List<Product> newList) {
        ProductsList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ShowProductsListItemViewMvcImp recyclerViewListItemMvcImp;

        public ViewHolder(ShowProductsListItemViewMvcImp viewMvcImp) {
            super(viewMvcImp.getRootView());
            recyclerViewListItemMvcImp = viewMvcImp;
        }
    }

}