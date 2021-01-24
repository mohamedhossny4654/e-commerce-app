package com.devahmed.techx.onlineshop.Screens.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.ProductsList.ShowProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchMvcImp extends BaseObservableMvcView<SearchMvc.Listener> implements SearchMvc, ShowProductsAdapter.OnItemClickListener {

    private SearchView searchView;
    private List<Product> productList , tempSearchList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ShowProductsAdapter adapter;

    public SearchMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.search_fragment , parent , false));
        initViews();
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        searchView.onActionViewExpanded();
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                for(Listener listener : getmListeners()){
                    listener.onQueryTextFocusChangeListener(hasFocus);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Listener listener : getmListeners()){
                    listener.onQueryTextSubmit(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for(Listener listener : getmListeners()){
                    listener.onQueryTextChange(newText);
                }
                return false;
            }
        });
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.searchRecycler);
        productList = new ArrayList<>();
        tempSearchList = new ArrayList<>();
        adapter = new ShowProductsAdapter(tempSearchList , this , getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void applyQuery(String query) {
        query = query.toLowerCase();
        tempSearchList = new ArrayList<>();
        for(Product product : productList){
            if(product.getTitle().toLowerCase().contains(query)){
                tempSearchList.add(product);
            }
        }
        adapter.setList(tempSearchList);
    }

    @Override
    public void bindData(List<Product> productList) {
        this.productList = productList;
        this.tempSearchList = productList;
        adapter.setList(productList);
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
    public void onImageClicked(Product product) {
        for(Listener listener : getmListeners()){
            listener.onImageClicked(product);
        }
    }

    @Override
    public void onAddToCartBtnClicked(Product product) {
        for(Listener listener : getmListeners()){
            listener.onAddToCartBtnClicked(product);
        }
    }

    @Override
    public void onImageLongClicked(Product product) {
        for(Listener listener : getmListeners()){
            listener.onImageLongClicked(product);
        }
    }

    @Override
    public void onIncreaseItemsBtnClicked() {
        for(Listener listener : getmListeners()){
            listener.onIncreaseItemsBtnClicked();
        }
    }

    @Override
    public void onDecreaseItemsBtnClicked() {
        for(Listener listener : getmListeners()){
            listener.onDecreaseItemsBtnClicked();
        }
    }
}
