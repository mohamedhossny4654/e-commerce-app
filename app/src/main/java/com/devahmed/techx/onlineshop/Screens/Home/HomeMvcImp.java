package com.devahmed.techx.onlineshop.Screens.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Category;
import com.devahmed.techx.onlineshop.Models.ProductSubCategory;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Home.OffersSliderList.ProductOffersAdapter;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.CategoryAdapter;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.RecyclerTouchListener;
import com.devahmed.techx.onlineshop.Utils.StatefulRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeMvcImp extends BaseObservableMvcView<HomeMvc.Listener> implements
        HomeMvc, ProductOffersAdapter.OnOfferItemClickListener {

    private SliderView sliderView;
    private StatefulRecyclerView categoriesRecycler;
    private List<ProductSubCategory>  offersProducstList;
    private ProductOffersAdapter offersAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private FloatingActionButton addCategoryBtn;
    private ProgressBar progressBar;
    private EditText searchbarEditText;
    private TextView categoriesText;
    private NestedScrollView nestedScrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public HomeMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.fragment_home_classic , parent , false));
        addCategoryBtn = findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onAddCategoryBtnClicked();
                }
            }
        });
        searchbarEditText = findViewById(R.id.searchbar);
        searchbarEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSearchbarClicked();
                }
            }
        });
        //ProgressBar
        progressBar = findViewById(R.id.progressBar);
        categoriesText = findViewById(R.id.categoriesText);
        categoriesText.setText(getContext().getResources().getString(R.string.categories));
        //SliderView that shows offers
        sliderView = findViewById(R.id.sliderView);
        offersProducstList = new ArrayList<>();
        offersAdapter = new ProductOffersAdapter(getContext() , offersProducstList , this);
        sliderView.setSliderAdapter(offersAdapter);
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInSec(6);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        //Categories RecyclerView
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        categoriesRecycler.setLayoutManager(gridLayoutManager);
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setAdapter(categoryAdapter);
        categoriesRecycler.addOnItemTouchListener( new RecyclerTouchListener(getContext(), categoriesRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String category = categoryList.get(position).getId();
                for(Listener listener : getmListeners()){
                    listener.onCategoryClicked(category);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if(!getContext().getResources().getString(R.string.admin).equalsIgnoreCase("true")){
                    return;
                }
                for(Listener listener : getmListeners()){
                    listener.onCtegoryLongClick(categoryList.get(position));
                }
            }
        }));
        if(!getContext().getResources().getString(R.string.admin).equalsIgnoreCase("true")){
            addCategoryBtn.hide();
        }
    }

    public void showCategoriesOptionsDialog(String title , String [] options , final Category category) {
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
                            listener.onChooseCategoryEdit(category);
                        }
                        break;
                    case 1:
                        //delete
                        for(Listener listener : getmListeners()){
                            listener.onChooseCategoryDelete(category);
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    public void showOffersOptionsDialog(String title , String [] options , final ProductSubCategory subCategory) {
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
                            listener.onChooseOfferEdit(subCategory);
                        }
                        break;
                    case 1:
                        //delete
                        for(Listener listener : getmListeners()){
                            listener.onChooseOfferDelete(subCategory);
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void bindSliderData(List<ProductSubCategory> offersProducstList) {
        this.offersProducstList = offersProducstList;
        offersAdapter.setList(offersProducstList);
    }

    @Override
    public void bindCategoriesDataData(List<Category> categoryList) {
        this.categoryList = categoryList;
        categoriesRecycler.setAdapter(categoryAdapter);
        categoryAdapter.setList(categoryList);
        categoriesRecycler.scrollTo(0 , 1000);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
        categoriesText.setVisibility(View.VISIBLE);
        searchbarEditText.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnItemClicked(ProductSubCategory category) {
        for(Listener listener : getmListeners()){
            listener.onOfferCliked(category);
        }
    }

    @Override
    public void OnItemLongClicked(ProductSubCategory subCategory) {
        for(Listener listener : getmListeners()){
            listener.onOffersLongClick(subCategory);
        }
    }

    @Override
    public void onAddToCartBtnClicked(ProductSubCategory productSubCategory) {
        for(Listener listener : getmListeners()){
            listener.onAddToCartBtnClicked(productSubCategory);
        }
    }

    @Override
    public void onIncreaseBtnClicked(ProductSubCategory productSubCategory) {
        for(Listener listener : getmListeners()){
            listener.onIncreaseBtnClicked(productSubCategory);
        }
    }

    @Override
    public void onDecreaseBtnClicked(ProductSubCategory productSubCategory) {
        for(Listener listener : getmListeners()){
            listener.onDecreaseBtnClicked(productSubCategory);
        }
    }

    @Override
    public void onOkBtnClicked(ProductSubCategory productSubCategory) {
        for(Listener listener : getmListeners()){
            listener.onOkBtnClicked(productSubCategory);
        }
    }
}
