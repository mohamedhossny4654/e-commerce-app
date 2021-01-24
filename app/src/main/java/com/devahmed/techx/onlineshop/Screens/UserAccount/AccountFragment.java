package com.devahmed.techx.onlineshop.Screens.UserAccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.GetLocationUseCase;
import com.devahmed.techx.onlineshop.Screens.LoginRegister.LoginActivity;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases.FetchProductsUseCase;
import com.devahmed.techx.onlineshop.Screens.Splash.SplashActivity;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddOrdersUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddUserUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.devahmed.techx.onlineshop.Utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends Fragment implements AccountMvc.Listener, AddUserUseCase.Listener
        , FetchUserInfoFromFirebaseUseCase.Listener, AddOrdersUseCase.Listener, GetLocationUseCase.Listener, FetchProductsUseCase.Listener {

    AccountMvcImp mvcImp;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    AddUserUseCase addUserUseCase;
    AddOrdersUseCase addOrdersUseCase;
    GetLocationUseCase locationUseCase;
    FetchProductsUseCase productsUseCase;
    String FN;
    User user;
    double mLat = -1, mLong = -1 ;
    String userLocation = "-1";
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mvcImp = new AccountMvcImp(getLayoutInflater(), null);
        addUserUseCase = new AddUserUseCase(requireActivity());
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        addOrdersUseCase = new AddOrdersUseCase(requireActivity());
        locationUseCase = new GetLocationUseCase(requireActivity());
        productsUseCase = new FetchProductsUseCase(FirebaseDatabase.getInstance());


        return mvcImp.getRootView();
    }

    @Override
    public void onApplyBtnClicked() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = mvcImp.getUserData();
        user.setUserId(currentUser.getUid());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userBranch" , 0);
        String userBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
        user.setNearestBranch(userBranch);
        addUserUseCase.updateExistingUser(user);
    }

    @Override
    public void onLatLongLoaded(double mLat, double mLong) {
        this.mLat = mLat;
        this.mLong = mLong;
    }

    @Override
    public void onGpsLocationLoaded(String gpsLocation) {
        this.userLocation = gpsLocation;
        mvcImp.hideProgressbar();
        Toast.makeText(getContext(), "Thank you we got your location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onUserAddedSuccessfully() {

    }

    @Override
    public void onUserFailedToAdd() {

    }

    @Override
    public void onInputError(String message) {

    }

    @Override
    public void onUserDataUpdated(User user) {
        mvcImp.activateNormalShowMode();
    }

    @Override
    public void onUseGPSBtnClicked() {
        mvcImp.showProgressbar();
        locationUseCase.getCurrentLocation(requireActivity());
    }

    @Override
    public void onLogoutBtnClicked() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity() , SplashActivity.class));
    }

    @Override
    public void onCartConfirmBtnCLicked() {
        if(!userDataIsValid()){
            return;
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        int userPoints = user.getPoints();
        user = mvcImp.getUserData();
        if(mLong != -1) user.setyPos(mLong); if(mLat != -1) user.setxPos(mLat); if(!userLocation.equals("-1")) user.setGpsAddress(userLocation);
        if(user != null && !user.getIsBlocked()){
            user.setUserId(currentUser.getUid());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("userBranch" , 0);
            String userBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
            user.setNearestBranch(userBranch);
            addUserUseCase.updateExistingUser(user);
        }
        user.setPoints(userPoints);
        user.setxPos(this.mLat);
        user.setyPos(this.mLong);
        user.setGpsAddress(this.userLocation);
        //after that add new order with this data
        Order order = new Order();
        order.setTotalPrice(getArguments().getDouble("totalPrice") - getArguments().getDouble("deliveryCost"));
        order.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setDeliveryCost( (int) getArguments().getDouble("deliveryCost"));
        order.setDiscount(0);
        order.setOrderAtTime(mvcImp.getOrderAtTime());
        List<String> productsIds = getArguments().getStringArrayList("productsIds");
        List<Integer> productsCounts = getArguments().getIntegerArrayList("productsCounts");
        //reduce number of these items in the store
        productsUseCase.updateProductsCounts(productsIds , productsCounts);
        Map<String , Integer> map = buildMap(productsIds , productsCounts);
        order.setOrderedItemsWithCounts(map);
        order.setOrderState("In Progress");
        order.setTimeStamp(ServerValue.TIMESTAMP);
        addOrdersUseCase.addNewOrder(order);
        //send this data to order details page
        Bundle bundle = new Bundle();
        bundle.putString("orderId" , order.getId());
        bundle.putString("orderState" , order.getOrderState());
        bundle.putInt("totalItems" , order.getOrderedItemsWithCounts().size());
        bundle.putDouble("deliveryCost" , order.getDeliveryCost());
        bundle.putDouble("totalPrice" , order.getTotalPrice());
        bundle.putString("userId" , order.getUserId());
        ArrayList<String> orderedItems = new ArrayList<>();
        for (String productId : order.getOrderedItemsWithCounts().keySet()) {
            orderedItems.add(productId);
        }
        bundle.putStringArrayList("orderedProducts" , orderedItems);
        ArrayList<String> ordersCounts = new ArrayList<>();
        for (Integer keys : order.getOrderedItemsWithCounts().values()){
            ordersCounts.add(String.valueOf(keys));
        }
        //Delete all the data from the cart to clear it
        deleteAllCartData();
        //add earned points to user account data on firebase
        addPointsForUser(user);
        bundle.putString("FN" , "dontShowCancelBtn");
        bundle.putStringArrayList("ordersCounts" , ordersCounts);
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.orderDetailsFragment , bundle);

//        Navigator.instance(requireActivity()).navigate(R.id.orderDetailsFragment , bundle);
    }

    private void addPointsForUser(User user) {
        user.setPoints(user.getPoints() + getArguments().getInt("totalEarnedPoints"));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userBranch" , 0);
        String userBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
        user.setNearestBranch(userBranch);
        addUserUseCase.updateExistingUser(user);
    }

    private boolean userDataIsValid() {
        User user = mvcImp.getUserData();
        if(!isValid(user.getName())){
            Toast.makeText(getContext(), "User name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getPhone())){
            Toast.makeText(getContext(), "phone is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getArea())){
            Toast.makeText(getContext(), "Area is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getStreet())){
            Toast.makeText(getContext(), "street no. is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getBuilding())){
            Toast.makeText(getContext(), "building no. is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getFloor())){
            Toast.makeText(getContext(), "Floor no. is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValid(user.getFlat())){
            Toast.makeText(getContext(), "Flat no. is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isValid(String text){
        if(text.isEmpty() || text.length() < 1){
            return false;
        }
        return true;
    }
    private void deleteAllCartData() {
        CartManager prodctsIDCartManager , productCountCartManagaer;
        prodctsIDCartManager = new CartManager(getContext() , "productsId");
        productCountCartManagaer = new CartManager(getContext() , "productCount");
        prodctsIDCartManager.deleteAllKeys();
        productCountCartManagaer.deleteAllKeys();
    }

    private Map<String , Integer> buildMap (List<String> list , List<Integer> integers) {
        Map<String , Integer> map = new HashMap<>();
        for(int i = 0; i < list.size() ; i++){
            map.put( list.get(i) , integers.get(i));
        }
        return map;
    }

    @Override
    public void onEditBtnClicked() {
        mvcImp.activateEditMode();
    }

    @Override
    public void onOrderAddedSuccessfully() {
        Toast.makeText(requireActivity(), "user added successfully ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderFailedToAdd() {
        Toast.makeText(requireActivity(), "Failed to add user ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderInputError(String message) {
        Toast.makeText(requireActivity(), "Error " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderDataUpdated(Order order) {

    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        for(User _user : userList){
            user = _user;
        }
        mvcImp.hideProgressbar();
        mvcImp.bindData(user);

    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {
        Toast.makeText(requireActivity(), "Failed to get User Data " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductsDataChange(List<Product> products) {

    }

    @Override
    public void onProductsDataCancel(DatabaseError error) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        addUserUseCase.registerListener(this);
        userInfoFromFirebaseUseCase.registerListener(this);
        addOrdersUseCase.registerListener(this);
        locationUseCase.registerListener(this);
        productsUseCase.registerListener(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            //user never signed up before
            Intent intent = new Intent(requireActivity() , LoginActivity.class);
            intent.putExtra("FN" , "signUp");
            startActivity(intent);
        }else{
            //means that user is signed up before
            if(getArguments() != null) {
                //means that user is coming from cart
                FN = getArguments().getString("FN");
                if(FN.equals("comingFromCart")){
                    //get only the current user data
                    userInfoFromFirebaseUseCase.getCurrentUser();
                    mvcImp.activateCartConfirmationMode();
                }else if(FN.equals("comingFromAdminShow")){
                    //get this specific user to show his data to the admin
                    String specefiedUserID = getArguments().getString("userId");
                    userInfoFromFirebaseUseCase.getUserOfID(specefiedUserID);
                    mvcImp.activateAdminShowMode();
                }

            }else{
                //means that user is not coming from any where
                userInfoFromFirebaseUseCase.getCurrentUser();
                mvcImp.activateNormalShowMode();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        addUserUseCase.unregisterListener(this);
        userInfoFromFirebaseUseCase.unregisterListener(this);
        addOrdersUseCase.unregisterListener(this);
        locationUseCase.unregisterListener(this);
        productsUseCase.unregisterListener(this);
    }


}
