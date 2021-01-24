package com.devahmed.techx.onlineshop.Screens.ProductsShow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AddProducts.AddProductActivity;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases.FetchProductsUseCase;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProducstShowFragment extends AppCompatActivity
        implements ProductsShowMvc.Listener , FetchProductsUseCase.Listener{

    private ProductsShowMvcImp mvcImp;
    private FetchProductsUseCase fetchProductsUseCase;
    String subCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mvcImp = new ProductsShowMvcImp(getLayoutInflater() , null);
        subCategory = getIntent().getExtras().getString("subCategory");//getArguments().getString("subCategory");
        System.out.println("subcategory is " + subCategory);
        fetchProductsUseCase = new FetchProductsUseCase(FirebaseDatabase.getInstance());
        fetchProductsUseCase.getProductsOfCategory(subCategory , this);

        setContentView( mvcImp.getRootView());
    }

    @Override
    public void onProductImageClicked(Product product) {
        UtilsDialog dialog = new UtilsDialog(this);
        dialog.showFullImageDialog(product.getImage());
    }

    @Override
    public void onAddToCartBtnClicked() {
        Toast.makeText(this, this.getResources().getString(R.string.Added), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddNewProductBtnClicked() {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "ADD_PRODUCT");
//        bundle.putString("subCategory" , subCategory);
//        Navigation.findNavController(this , R.id.nav_host_fragment).navigate(R.id.nav_add_item , bundle );

        Intent intent = new Intent(getApplicationContext() , AddProductActivity.class);
        intent.putExtra("FN" , "ADD_PRODUCT");
        intent.putExtra("subCategory" , subCategory);
        startActivity(intent);
//        Navigator.instance(this).navigate(R.id.nav_add_item, bundle);
    }

    @Override
    public void onProductLongClicked(Product product) {
        if(getResources().getString(R.string.admin).equalsIgnoreCase("true")) {
            String[] options = {"Edit", "Delete"};
            mvcImp.showProductsOptionsDialog("Choose Option", options, product);
        }
    }

    @Override
    public void onChooseProductEdit(Product product) {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "EDIT_PRODUCT");
//        bundle.putString("productId" , product.getId());
//        bundle.putString("productImage" , product.getImage());
//        bundle.putString("productTitle" , product.getTitle());
//        bundle.putDouble("productPrice" , product.getPrice());
//        bundle.putString("productSubCategory" , product.getSubCategory());
//        bundle.putString("productTimeStamp" , product.getTimeStamp().toString());
//        bundle.putInt("productCount" , product.getMaxCount());
//        bundle.putInt("productPoints" , product.getPoints());
//        bundle.putString("productBranch" , product.getBranch());
//        Navigation.findNavController(this , R.id.nav_host_fragment).navigate(R.id.nav_add_item , bundle );

        Intent intent = new Intent(ProducstShowFragment.this, AddProductActivity.class);
        intent.putExtra("FN" , "EDIT_PRODUCT");
        intent.putExtra("productId" , product.getId());
        intent.putExtra("productImage" , product.getImage());
        intent.putExtra("productTitle" , product.getTitle());
        intent.putExtra("productPrice" , product.getPrice());
        intent.putExtra("productSubCategory" , product.getSubCategory());
        intent.putExtra("productTimeStamp" , product.getTimeStamp().toString());
        intent.putExtra("productCount" , product.getMaxCount());
        intent.putExtra("productPoints" , product.getPoints());
        intent.putExtra("productBranch" , product.getBranch());
        startActivity(intent);
    }

    @Override
    public void onChooseProductDelete(Product product) {
        fetchProductsUseCase.deleteCategory(product.getId());
    }

    @Override
    public void onProductsDataChange(List<Product> products) {
        mvcImp.bindProductsDataData(products);
        mvcImp.hideProgressbar();

    }

    @Override
    public void onProductsDataCancel(DatabaseError error) {
        Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        fetchProductsUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        fetchProductsUseCase.unregisterListener(this);
    }

}