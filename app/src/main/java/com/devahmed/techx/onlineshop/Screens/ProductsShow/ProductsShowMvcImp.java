package com.devahmed.techx.onlineshop.Screens.ProductsShow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.ProductsList.ShowProductsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductsShowMvcImp extends BaseObservableMvcView<ProductsShowMvc.Listener>
        implements ProductsShowMvc, ShowProductsAdapter.OnItemClickListener {

    private FloatingActionButton addNewProductBtn;
    private RecyclerView showProducstRecyclerView;
    private ShowProductsAdapter productsAdapter;
    private List<Product> mProductsList;
    private ProgressBar progressBar;


    public ProductsShowMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.fragment_show_products , parent , false));
        addNewProductBtn = findViewById(R.id.addNewProductBtn);
        progressBar = findViewById(R.id.progressBar);
        showProducstRecyclerView = findViewById(R.id.showProductsRecycler);
        showProducstRecyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));
        showProducstRecyclerView.setHasFixedSize(true);
        mProductsList = new ArrayList<>();
        productsAdapter = new ShowProductsAdapter(mProductsList , this , getContext());
        showProducstRecyclerView.setAdapter(productsAdapter);

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onAddNewProductBtnClicked();
                }
            }
        });
        if(!getContext().getResources().getString(R.string.admin).equalsIgnoreCase("true")){
            addNewProductBtn.hide();
        }
    }
    @Override
    public void showProductsOptionsDialog(String title , String [] options , final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                // the user clicked on colors[which]
                switch (index){
                    case 0:
                        //edit
                        for(Listener listener : getmListeners()){
                            listener.onChooseProductEdit(product);
                        }
                        break;
                    case 1:
                        //delete
                        for(Listener listener : getmListeners()){
                            listener.onChooseProductDelete(product);
                        }
                        break;
                }
            }
        });
        builder.show();
    }


    @Override
    public void bindProductsDataData(List<Product> productList) {
        productsAdapter.setList(productList);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onImageClicked(Product Product) {
        for(Listener listener: getmListeners()){
            listener.onProductImageClicked(Product);
        }
    }

    @Override
    public void onAddToCartBtnClicked(Product product) {
        for(Listener listener: getmListeners()){
            listener.onAddToCartBtnClicked();
        }
    }

    @Override
    public void onImageLongClicked(Product product) {
        for(Listener listener: getmListeners()){
            listener.onProductLongClicked(product);
        }
    }

    @Override
    public void onIncreaseItemsBtnClicked() {

    }

    @Override
    public void onDecreaseItemsBtnClicked() {

    }

}
