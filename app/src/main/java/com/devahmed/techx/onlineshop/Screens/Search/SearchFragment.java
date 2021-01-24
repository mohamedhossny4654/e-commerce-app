package com.devahmed.techx.onlineshop.Screens.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases.FetchProductsUseCase;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SearchFragment extends Fragment implements SearchMvc.Listener, FetchProductsUseCase.Listener {

    SearchMvcImp mvcImp;
    FetchProductsUseCase fetchProductsUseCase;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvcImp = new SearchMvcImp(getLayoutInflater() , null);
        fetchProductsUseCase = new FetchProductsUseCase(FirebaseDatabase.getInstance());



        return mvcImp.getRootView();
    }

    @Override
    public void onQueryTextFocusChangeListener(boolean isFocused) {

    }

    @Override
    public void onQueryTextSubmit(String query) {
        System.out.println("" + query);
        mvcImp.applyQuery(query);
    }

    @Override
    public void onQueryTextChange(String newText) {
        mvcImp.applyQuery(newText);
    }

    @Override
    public void onImageClicked(Product product) {
        UtilsDialog dialog = new UtilsDialog(requireActivity());
        dialog.showFullImageDialog(product.getImage());
    }

    @Override
    public void onAddToCartBtnClicked(Product product) {

    }

    @Override
    public void onImageLongClicked(Product product) {

    }

    @Override
    public void onIncreaseItemsBtnClicked() {

    }

    @Override
    public void onDecreaseItemsBtnClicked() {

    }
    //==============================================================================================

    @Override
    public void onProductsDataChange(List<Product> products) {
        mvcImp.bindData(products);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onProductsDataCancel(DatabaseError error) {
        Toast.makeText(requireActivity(), "Search Error" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        fetchProductsUseCase.registerListener(this);
        fetchProductsUseCase.getAllProducts();
        mvcImp.showProgressBar();
    }


    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        fetchProductsUseCase.unregisterListener(this);
    }

}
