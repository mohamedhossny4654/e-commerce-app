package com.devahmed.techx.onlineshop.Screens.AddProducts;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.Models.Category;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.devahmed.techx.onlineshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddProductViewMvcImp extends BaseObservableMvcView<AddProductMvc.Listener> implements AddProductMvc {


    private FloatingActionButton _addBtn;
    private TextView addpost_pickfromgallerytxtview , addpost_pickfromcameratxtview , addpost_dialogtitle , priceTextView;
    private EditText _productName, _productPrice , pointsText , countText;
    private ProgressBar _progressbar;
    private ImageView _pickfromgallery , _pickfromcamera , addpost_fullviewImageView , cancelImageBtn;
    private CheckBox checkbox_offer;
    private LinearLayout branchesContainer , countsContainer;
    private RadioGroup branchedRadioGroup;
    private String selectedBranch = "";
    private List<Branch> branchList;
    private Product product;
    public AddProductViewMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.fragment_add_product, parent , false ));
        addpost_dialogtitle = findViewById(R.id.addpost_dialogtitle);
        priceTextView = findViewById(R.id.priceTextView);
        //Change the text of dialog title
        addpost_dialogtitle.setText("Add New Product");
        _progressbar = findViewById(R.id.addpost_progressbar);
        addpost_pickfromgallerytxtview = findViewById(R.id.addpost_pickfromgallerytxtview);
        addpost_pickfromgallerytxtview.setVisibility(View.VISIBLE);
        addpost_pickfromcameratxtview = findViewById(R.id.addpost_pickfromcameratxtview);
        addpost_pickfromcameratxtview.setVisibility(View.VISIBLE);
        _addBtn = findViewById(R.id.addpost_addbtn);
        branchedRadioGroup = findViewById(R.id.branchesRadioGroup);
        _productName = findViewById(R.id.addpost_recipename);
        pointsText = findViewById(R.id.pointsText);
        countText = findViewById(R.id.countText);
        branchesContainer = findViewById(R.id.branchedContainer);
        countsContainer = findViewById(R.id.countsContainer);
        _productPrice = findViewById(R.id.addpost_recipequantities);
        _pickfromcamera = findViewById(R.id.addpost_pickfromcameraimage);
        _pickfromgallery = findViewById(R.id.addpost_pickfromgalleryimage);
        addpost_fullviewImageView = findViewById(R.id.addpost_fullviewImageView);
        cancelImageBtn = findViewById(R.id.cancelImageBtn);
        checkbox_offer = findViewById(R.id.checkbox_offer);

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onPublishBtnClicked();
                }
            }
        });

        _pickfromcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onCameraBtnCLicked();
                }
            }
        });

        _pickfromgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onGalleryImageClicked();
                }
            }
        });

        branchedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
//                boolean isChecked = checkedRadioButton.isChecked();
                if(branchList != null){
                    for(Branch branch : branchList){
                        if(branch.getName().equals(checkedRadioButton.getText().toString())){
                            selectedBranch = branch.getId();
                        }
                    }
                }
            }
        });
        cancelImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addpost_fullviewImageView.setVisibility(View.GONE);
            }
        });
    }

    public boolean isOfferChecked(){
        return checkbox_offer.isChecked();
    }

    public String getTitle(){
        return _productName.getText().toString();
    }
    public String getPrice(){
        return _productPrice.getText().toString();
    }

    @Override
    public void showProgressBar() {
        _progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        _progressbar.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showAddBtn() {
        _addBtn.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideAddBtn() {
        _addBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getSelectedBranch() {
        return selectedBranch;
    }

    @Override
    public int getCount() {
        if(countText.getText().toString().isEmpty()){
            return -1;
        }
        return Integer.parseInt(countText.getText().toString());
    }

    @Override
    public int getPoints() {
        if(pointsText.getText().toString().isEmpty()){
            return -1;
        }
        return Integer.parseInt(pointsText.getText().toString());
    }

    @Override
    public void bindFullImage(Uri image) {
        addpost_fullviewImageView.setVisibility(View.VISIBLE);
        addpost_fullviewImageView.setImageURI(image);
    }

    @Override
    public void bindBranchedData(List<Branch> branchesList) {
        this.branchList = branchesList;
        for (int i = 0; i <branchesList.size() ; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(branchesList.get(i).getName());
            branchedRadioGroup.addView(radioButton);
        }
        //invoke this when we want to edit some product so we get the data of desired product and its branch
        if(this.product != null){
            for(Branch branch : branchList){
                if(branch.getId().equals(this.product.getBranch())){
                    int index = branchList.indexOf(branch);
                    RadioButton radioButton = (RadioButton) (branchedRadioGroup.findViewById(index));
                    radioButton.setChecked(true);
                    this.selectedBranch = branch.getId();
                    break;
                }
            }
        }

    }


    @Override
    public void clearData() {
        addpost_fullviewImageView.setImageURI(null);
        _productName.setText("");
        _productPrice.setText("");
        pointsText.setText("");
        countText.setText("");
        checkbox_offer.setChecked(false);
        addpost_fullviewImageView.setVisibility(View.GONE);
    }

    public void activateCategoryMode(Category category){
        priceTextView.setVisibility(View.GONE);
        _productPrice.setVisibility(View.GONE);
        checkbox_offer.setVisibility(View.GONE);
        if(category != null){
            _productName.setText(category.getTitle());
            addpost_fullviewImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(category.getImage())
                    .into(addpost_fullviewImageView);
        }
    }

    public void activateSubCategoryMode(SubCategory subCategory){
        priceTextView.setVisibility(View.GONE);
        _productPrice.setVisibility(View.GONE);
        if(subCategory != null){
            checkbox_offer.setChecked(subCategory.isInOffer());
            _productName.setText(subCategory.getTitle());
            addpost_fullviewImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(subCategory.getImage())
                    .into(addpost_fullviewImageView);
        }
    }

    public void activateProductMode(Product product){
        this.product = product;
        checkbox_offer.setVisibility(View.VISIBLE);
        countsContainer.setVisibility(View.VISIBLE);
        branchesContainer.setVisibility(View.VISIBLE);
        pointsText.setVisibility(View.VISIBLE);
        if(product != null ){
            _productPrice.setText("" + product.getPrice());
            _productName.setText(product.getTitle());
            countText.setText("" + product.getMaxCount());
            pointsText.setText("" + product.getPoints());
            branchedRadioGroup.clearCheck();
            addpost_fullviewImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(product.getImage())
                    .into(addpost_fullviewImageView);
        }
    }
}
