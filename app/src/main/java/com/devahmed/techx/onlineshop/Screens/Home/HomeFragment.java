package com.devahmed.techx.onlineshop.Screens.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.Models.Category;
import com.devahmed.techx.onlineshop.Common.dependencyInjection.BaseFragment;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AddProducts.AddProductActivity;
import com.devahmed.techx.onlineshop.Screens.Home.UseCases.FetchCategoryUseCase;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.ProducstShowFragment;
import com.devahmed.techx.onlineshop.Screens.SubCategories.SubCategoriesFragment;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class HomeFragment extends BaseFragment implements FetchCategoryUseCase.Listener, HomeMvc.Listener {


    private HomeMvcImp mvcImp;
    private FetchCategoryUseCase categoryUseCase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mvcImp = getCompositionRoot().getMvcFactory().getHomeMvc(container);
        categoryUseCase = new FetchCategoryUseCase(getCompositionRoot().ConnectToFirebase());
        categoryUseCase.getOffers(getContext());
        categoryUseCase.getCategories();
        handleOnBackPressed();
        return mvcImp.getRootView();
    }


    @Override
    public void onOfferProductChange(List<ProductSubCategory> productList) {
        mvcImp.bindSliderData(productList);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onOfferProductCancelled(DatabaseError databaseError) {
        Toast.makeText(requireActivity()
                , "Error while getting Offers Products " + databaseError.getMessage()
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOfferCliked(ProductSubCategory subCategory) {
        if(subCategory.getType() == ProductSubCategory.TYPE_SUB_CATEGORY){
//            Bundle bundle = new Bundle();
//            bundle.putString("subCategory" , subCategory.getTitle());
//            Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.nav_products , bundle);
            Intent intent = new Intent(getActivity() , ProducstShowFragment.class);
            intent.putExtra("subCategory" , subCategory.getTitle());
            startActivity(intent);
        }else if(subCategory.getType() == ProductSubCategory.TYPE_PRODUCT){
            Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.cartFragment );
        }

    }

    @Override
    public void onOffersLongClick(ProductSubCategory subCategory) {
        if(getResources().getString(R.string.admin).equalsIgnoreCase("true")){
            String[] options = {"Edit", "Delete"};
            mvcImp.showOffersOptionsDialog("Choose Option" , options , subCategory);
        }

    }

    @Override
    public void onChooseOfferEdit(ProductSubCategory subCategory) {

    }

    @Override
    public void onChooseOfferDelete(ProductSubCategory subCategory) {
        //just delete it from offers not from entire app
        categoryUseCase.deleteOffer(subCategory);
    }

    @Override
    public void onProductsChanged(List<Category> categoryList) {
        mvcImp.bindCategoriesDataData(categoryList);
    }

    @Override
    public void onProductsCancelled(DatabaseError databaseError) {
        Toast.makeText(requireActivity(), "Categories Cancelled in Home Fragment " + databaseError, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClicked(String category) {
        Intent intent = new Intent(requireActivity(), SubCategoriesFragment.class);
        intent.putExtra("subCategory" , category);
        startActivity(intent);
    }

    @Override
    public void onCtegoryLongClick(Category category) {
        if(getResources().getString(R.string.admin).equalsIgnoreCase("true")) {
            String[] options = {"Edit", "Delete"};
            mvcImp.showCategoriesOptionsDialog("Choose option", options, category);
        }
    }


    @Override
    public void onChooseCategoryEdit(Category category) {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "EDIT_CATEGORY");
//        bundle.putString("categoryTimeStamp" , category.getTimeStamp().toString());
//        bundle.putString("categoryId" , category.getId());
//        bundle.putString("categoryTitle" , category.getTitle());
//        bundle.putString("categoryImage" , category.getImage());
//        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.nav_add_item ,bundle);
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        intent.putExtra("FN" , "EDIT_CATEGORY");
        intent.putExtra("categoryTimeStamp" , category.getTimeStamp().toString());
        intent.putExtra("categoryId" , category.getId());
        intent.putExtra("categoryTitle" , category.getTitle());
        intent.putExtra("categoryImage" , category.getImage());
        startActivity(intent);
    }

    @Override
    public void onChooseCategoryDelete(Category category) {
        categoryUseCase.deleteCategory(category.getId());
    }

    @Override
    public void onAddCategoryBtnClicked() {
//        Bundle bundle = new Bundle();
//        bundle.putString("FN" , "ADD_CATEGORY");
//        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.nav_add_item ,bundle);

        Intent intent = new Intent(getActivity() , AddProductActivity.class);
        intent.putExtra("FN" , "ADD_CATEGORY");
        startActivity(intent);
    }

    @Override
    public void onSearchbarClicked() {
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.searchFragment );
    }

    @Override
    public void onAddToCartBtnClicked(ProductSubCategory productSubCategory) {
        Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIncreaseBtnClicked(ProductSubCategory productSubCategory) {

    }

    @Override
    public void onDecreaseBtnClicked(ProductSubCategory productSubCategory) {

    }

    @Override
    public void onOkBtnClicked(ProductSubCategory productSubCategory) {

    }

    private void handleOnBackPressed() {
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                UtilsDialog dialog = new UtilsDialog(getActivity());
                dialog.showExitDialog();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryUseCase.registerListener(this);
        mvcImp.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryUseCase.unregisterListener(this);
        mvcImp.unregisterListener(this);
    }

}