package com.devahmed.techx.onlineshop.Screens.ProductsShow.ProductsList;

import com.devahmed.techx.onlineshop.Models.Product;

public interface ShowProductListItemView {

        public interface Listener{
            void onImageClicked(Product product);
            void onAddToCartBtnClicked(Product product);
            void onImageLongClicked(Product product);
            void onIncreaseItemsBtnClicked(Product product);
            void onDecreaseItemsBtnClicked(Product product);
        }

        void bindData(Product product);
        void bindDataForAddedToCartProduct(Product product, int noOfItemsInCart);
        void bindDataOfFinishedItem(Product product);
    }


