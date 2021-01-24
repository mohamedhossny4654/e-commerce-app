package com.devahmed.techx.onlineshop.Screens.SubCategories;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AddProducts.AddProductActivity;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.ProducstShowFragment;
import com.devahmed.techx.onlineshop.Screens.SubCategories.UseCase.FetchSubCategories;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SubCategoriesFragment extends AppCompatActivity implements FetchSubCategories.Listener, SubCategoriesMvc.Listener {

    SubCategoryMvcImp mvcImp;
    FetchSubCategories useCase;
    String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvcImp = new SubCategoryMvcImp(getLayoutInflater() , null);

        category = getIntent().getExtras().getString("subCategory");
        System.out.println("subCategory is " + category);
        useCase = new FetchSubCategories(FirebaseDatabase.getInstance());
        useCase.getSubCategories(category);
        setContentView(mvcImp.getRootView());
    }

    @Override
    public void onSubCategoryClicked(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("subCategory" , title);
        Intent intent = new Intent(getApplicationContext(), ProducstShowFragment.class);
        intent.putExtra("subCategory" , title);
        startActivity(intent);
    }


    @Override
    public void onAddSubCategoryBtnClicked() {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "ADD_SUBCATEGORY");
//        bundle.putString("category" , category);
//        Navigation.findNavController(this , R.id.nav_host_fragment).navigate(R.id.nav_add_item , bundle);
        Intent intent = new Intent(getApplicationContext() , AddProductActivity.class);
        intent.putExtra("FN" , "ADD_SUBCATEGORY");
        intent.putExtra("category" , category);
        startActivity(intent);
    }

    @Override
    public void onSubCategoryLongClicked(SubCategory subCategory) {
        if(getResources().getString(R.string.admin).equalsIgnoreCase("true")) {
            String[] options = {"Edit", "Delete"};
            mvcImp.showCategoriesOptionsDialog("Choose option", options, subCategory);
        }
    }

    @Override
    public void onChooseSubCategoryEdit(SubCategory subCategory) {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "EDIT_SUBCATEGORY");
//        bundle.putString("subCategoryId" , subCategory.getId());
//        bundle.putString("subCategoryTitle" , subCategory.getTitle());
//        bundle.putString("subCategoryImage" , subCategory.getImage());
//        bundle.putString("subCategoryCategory" , subCategory.getCategory());
//        bundle.putBoolean("subCategoryInOffer" , subCategory.isInOffer());
//        Navigation.findNavController(this , R.id.nav_host_fragment).navigate(R.id.nav_add_item ,bundle);
        Intent intent = new Intent(SubCategoriesFragment.this, AddProductActivity.class);
        intent.putExtra("FN" , "EDIT_SUBCATEGORY");
        intent.putExtra("subCategoryId" , subCategory.getId());
        intent.putExtra("subCategoryTitle" , subCategory.getTitle());
        intent.putExtra("subCategoryImage" , subCategory.getImage());
        intent.putExtra("subCategoryCategory" , subCategory.getCategory());
        intent.putExtra("subCategoryInOffer" , subCategory.isInOffer());
        startActivity(intent);
    }

    @Override
    public void onChooseSubCategoryDelete(SubCategory category) {
        useCase.deleteCategory(category.getId());
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubCategorySuccess(List<SubCategory> subCategories) {
        mvcImp.bindSubCategoriesData(subCategories);
        mvcImp.hideProgressbar();

    }

    @Override
    public void onSubCategoryCanceled(DatabaseError error) {
        Toast.makeText(this, "SubCategories Error  " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        useCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        useCase.unregisterListener(this);
    }

}
