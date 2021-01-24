package com.devahmed.techx.onlineshop.Screens.Orders.OrdersDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases.FetchProductsUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddOrdersUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.devahmed.techx.onlineshop.Utils.CartManager;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailsFragment extends Fragment implements OrderDetailsMvc.Listener
        , FetchProductsUseCase.Listener, FetchUserInfoFromFirebaseUseCase.Listener, AddOrdersUseCase.Listener {


    OrderDetailsMvcImp mvcImp;
    FetchProductsUseCase productsUseCase;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    ArrayList<String> ordersCounts;
    ArrayList<String> orderedProducts;
    AddOrdersUseCase addOrdersUseCase;
    String orderId;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new OrderDetailsMvcImp(getLayoutInflater() , null);
        productsUseCase = new FetchProductsUseCase(FirebaseDatabase.getInstance());
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        addOrdersUseCase = new AddOrdersUseCase(requireActivity());
        getOrderData();




        return mvcImp.getRootView();
    }


    private void getOrderData() {
        if(getArguments() != null){
            String userId = getArguments().getString("userId");
            String orderState = getArguments().getString("orderState");
            orderId = getArguments().getString("orderId");
            int totalItems = getArguments().getInt("totalItems");
            double deliveryCost = getArguments().getDouble("deliveryCost");
            double totalPrice = getArguments().getDouble("totalPrice");

            orderedProducts = getArguments().getStringArrayList("orderedProducts");
            ordersCounts = getArguments().getStringArrayList("ordersCounts");
            if(orderState.equalsIgnoreCase("In Progress")){
                mvcImp.cancelOrderBtn.setVisibility(View.VISIBLE);
                mvcImp.editOrder.setVisibility(View.VISIBLE);

            }
            String FN  = getArguments().getString("FN");
            if(FN != null){
                if(FN.equals("showForAdmin"))
                    mvcImp.showAdminControls();
                if(orderState.equalsIgnoreCase("In Progress")){
                    //If we coming from cart we dont show this cancel btn
                    if(FN.equals("dontShowCancelBtn")){
                        mvcImp.cancelOrderBtn.setVisibility(View.INVISIBLE);
                        mvcImp.editOrder.setVisibility(View.INVISIBLE);
                    }
                }
            }

            mvcImp.bindOrderState(orderState);
            mvcImp.bindPaymentsData(totalItems , deliveryCost , totalPrice );
            productsUseCase.getCategoriesWithIDs(orderedProducts);
            userInfoFromFirebaseUseCase.getUserOfID(userId);
        }
    }

    @Override
    public void onCallUserBtnClicked(User user) {
        String phone = user.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void onShowLocationBtnClicked(User user) {
        Toast.makeText(getActivity(), "x : " + user.getxPos() + " Y : " + user.getyPos(), Toast.LENGTH_SHORT).show();
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", user.getxPos(), user.getyPos());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onCancelOrderBtnClicked() {
        addOrdersUseCase.deleteOrderOfId(orderId);
        Toast.makeText(getContext(), "Order Canceled", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.ordersListFragment );
    }

    @Override
    public void onEditOrderBtnClicked() {
        addOrdersUseCase.deleteOrderOfId(orderId);
        //add this order data to cart manager and navigate to cart
        CartManager prodctsIDCartManager , productCountCartManagaer;
        List<String> cartProductIdList , cartProductCountList;
        prodctsIDCartManager = new CartManager(getContext() , "productsId");
        productCountCartManagaer = new CartManager(getContext() , "productCount");
        cartProductIdList = prodctsIDCartManager.getStoredValues();
        cartProductCountList = productCountCartManagaer.getStoredValues();

        for (int i = 0; i < orderedProducts.size(); i++) {
            if(!cartProductIdList.contains(orderedProducts.get(i))){
                //add a new product to cart manager
                prodctsIDCartManager.addToCart(orderedProducts.get(i));
                productCountCartManagaer.addToCart(ordersCounts.get(i));
            }else{
                //if the id is exist but its count is zero => just increase the counter
                //this case happen when user increase the counter then decrease it again
                int index = cartProductIdList.indexOf(orderedProducts.get(i));
                productCountCartManagaer.replaceKey(index , ordersCounts.get(i));
            }
        }

        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.cartFragment);


    }

    @Override
    public void onProductsDataChange(List<Product> products) {
        mvcImp.bindOrderedItemsData(products , ordersCounts);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onProductsDataCancel(DatabaseError error) {
        mvcImp.hideProgressBar();
    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        User _user = null;
        for(User user : userList){
            _user = user;
        }
        mvcImp.bindUserData(_user);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {
        mvcImp.hideProgressBar();
    }

    private void handleOnBackPressed() {
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.nav_home);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        productsUseCase.registerListener(this);
        userInfoFromFirebaseUseCase.registerListener(this);
        addOrdersUseCase.registerListener(this);
        handleOnBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        productsUseCase.unregisterListener(this);
        userInfoFromFirebaseUseCase.unregisterListener(this);
        addOrdersUseCase.unregisterListener(this);
    }


    @Override
    public void onOrderAddedSuccessfully() {

    }

    @Override
    public void onOrderFailedToAdd() {

    }

    @Override
    public void onOrderInputError(String message) {

    }

    @Override
    public void onOrderDataUpdated(Order order) {

    }
}
