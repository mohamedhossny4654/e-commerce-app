package com.devahmed.techx.onlineshop.Screens.Cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.Models.DeliverCost;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.FetchBranchesUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl.UseCase.FetchMinChargesUseCase;
import com.devahmed.techx.onlineshop.Screens.Cart.UseCase.FetchCartDataUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost.UseCases.FetchDeliveryCostesUseCase;
import com.devahmed.techx.onlineshop.Screens.LocationAtFirstTime.LocationAtFirstTime;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements CartMvc.Listener, FetchCartDataUseCase.Listener, FetchUserInfoFromFirebaseUseCase.Listener, FetchDeliveryCostesUseCase.Listener, FetchMinChargesUseCase.Listener, FetchBranchesUseCase.Listener {

    CartMvcImp mvcImp;
    FetchCartDataUseCase cartDataUseCase;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    FetchDeliveryCostesUseCase deliveryCostesUseCase;
    boolean isUserBlocked = false;
    int minCharge = -1;
    List<DeliverCost> deliverCostList;
    FetchMinChargesUseCase minChargesUseCase;
    FetchBranchesUseCase branchesUseCase;
    Branch branch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mvcImp = new CartMvcImp(getLayoutInflater() , null);
        cartDataUseCase = new FetchCartDataUseCase(requireActivity() , FirebaseDatabase.getInstance());
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        deliveryCostesUseCase = new FetchDeliveryCostesUseCase(FirebaseDatabase.getInstance());
        minChargesUseCase = new FetchMinChargesUseCase(FirebaseDatabase.getInstance());
        branchesUseCase = new FetchBranchesUseCase(FirebaseDatabase.getInstance());
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userBranch" , 0);
        branchesUseCase.getBranchOfId(sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6"));
        userInfoFromFirebaseUseCase.getCurrentUser();
        return mvcImp.getRootView();
    }

    @Override
    public void onSubmitBtnClicked(int totalEarnedPoints) {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userBranch" , 0);
        if(!sharedPreferences.getBoolean("gotUserLocation?" , false)){
            Intent intent = new Intent(getActivity().getApplicationContext() , LocationAtFirstTime.class);
            startActivity(intent);
        }
        if(branch.getIsOpenNow() == false){
            UtilsDialog dialog = new UtilsDialog(getContext());
            dialog.showTextMessage(getContext().getResources().getString(R.string.sorry_closed_now) + branch.getOpenAt());
        }
        if(isUserBlocked){
            Toast.makeText(requireContext(), getContext().getResources().getString(R.string.UBlocked), Toast.LENGTH_SHORT).show();
            return;
        }
        if(mvcImp.getTotalPrice() < minCharge && minCharge != -1){
            Toast.makeText(requireContext(), getContext().getResources().getString(R.string.sorry_min_charge_is) + minCharge , Toast.LENGTH_SHORT).show();
            return;
        }
        mvcImp.updateDeliveryCostRange(this.deliverCostList);
        Bundle bundle = new Bundle();
        bundle.putString("FN" , "comingFromCart");
        bundle.putDouble("totalPrice" , mvcImp.getTotalPrice());
        bundle.putDouble("deliveryCost" , mvcImp.getDeliveryCost());

        bundle.putStringArrayList("productsIds" , mvcImp.getProductsIds());
        bundle.putIntegerArrayList("productsCounts" , mvcImp.getProductsCounts());
        bundle.putInt("totalEarnedPoints",totalEarnedPoints);
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.accountFragment ,bundle);
//        Navigator.instance(requireActivity()).navigate(R.id.accountFragment , bundle);
    }

    @Override
    public void onGoToShoppingBtnClicked() {
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.nav_home);
//        Navigator.instance(requireActivity()).navigate(R.id.nav_home);
    }

    @Override
    public void onCartItemImageClicked(Product product) {
        UtilsDialog dialog = new UtilsDialog(requireActivity());
        dialog.showFullImageDialog(product.getImage());
    }

    @Override
    public void onProductsChanged(List<Product> productList , List<String> productsCountList) {
        mvcImp.bindCartData(productList , productsCountList);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onProductsCanceled(DatabaseError error) {
        Toast.makeText(requireContext(), "CartFragment " + error.getMessage(), Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressbar();
    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        User user = null;
        for(User user1 : userList){
            user = user1;
        }
        if(user.getIsBlocked()){
            isUserBlocked = true;
        }
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {
        mvcImp.hideProgressbar();
    }

    @Override
    public void onDeliveryCostDataChange(List<DeliverCost> dataList) {
        this.deliverCostList = dataList;
        mvcImp.bindDeliveryCost(dataList);
    }

    @Override
    public void onMinChargeDataChange(Map<String, String> map) {
        if(map != null)
            minCharge = Integer.parseInt(map.get("minCharge"));
    }

    @Override
    public void onDeliveryCostDataCancel(DatabaseError error) {

    }

    @Override
    public void onMinChargeDataCancel(DatabaseError error) {

    }

    @Override
    public void onInputError(String message) {
        Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        cartDataUseCase.registerListener(this);
        userInfoFromFirebaseUseCase.registerListener(this);
        deliveryCostesUseCase.registerListener(this);
        minChargesUseCase.registerListener(this);
        branchesUseCase.registerListener(this);
        deliveryCostesUseCase.getAllDeliveryCostes();
        cartDataUseCase.getProductsAtCart();
        minChargesUseCase.getMinCharge();
        mvcImp.showProgressbar();

    }


    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        cartDataUseCase.unregisterListener(this);
        userInfoFromFirebaseUseCase.unregisterListener(this);
        deliveryCostesUseCase.unregisterListener(this);
        branchesUseCase.unregisterListener(this);
        minChargesUseCase.unregisterListener(this);
    }


    @Override
    public void onBranchDataChange(List<Branch> dataList) {
        if(dataList != null)
            this.branch = dataList.get(0);
    }

    @Override
    public void onBranchDataCancel(DatabaseError error) {

    }
}
