package com.devahmed.techx.onlineshop.Screens.Search;

import com.devahmed.techx.onlineshop.Models.Product;

import java.util.List;

public interface SearchMvc {
    public interface Listener{
       void onQueryTextFocusChangeListener(boolean isFocused);
       void onQueryTextSubmit(String query);
       void onQueryTextChange(String newText);
        void onImageClicked(Product Product);
        void onAddToCartBtnClicked(Product product);
        void onImageLongClicked(Product product);
        void onIncreaseItemsBtnClicked();
        void onDecreaseItemsBtnClicked();
    }

    void applyQuery(String query);
    void bindData(List<Product> productList);
    void showProgressBar();
    void hideProgressBar();
}
