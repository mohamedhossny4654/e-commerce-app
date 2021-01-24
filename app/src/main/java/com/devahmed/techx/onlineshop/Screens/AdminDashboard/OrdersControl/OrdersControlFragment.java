package com.devahmed.techx.onlineshop.Screens.AdminDashboard.OrdersControl;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList.UseCases.FetchOrdersUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddOrdersUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrdersControlFragment extends Fragment implements OrdersControlMvc.Listener, FetchOrdersUseCase.Listener, AddOrdersUseCase.Listener {


    OrdersControlMvcImp mvcImp;
    FetchOrdersUseCase ordersUseCase;
    AddOrdersUseCase addOrdersUseCase;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new OrdersControlMvcImp(getLayoutInflater() , null);
        ordersUseCase = new FetchOrdersUseCase(FirebaseDatabase.getInstance());
        addOrdersUseCase = new AddOrdersUseCase(requireActivity());



        return mvcImp.getRootView();
    }


    @Override
    public void onItemClicked(Order order) {
        //do nothing here
    }

    @Override
    public void onShowBtnClicked(Order order) {
        Bundle bundle = new Bundle();
        bundle.putString("FN" , "showForAdmin");
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
        bundle.putStringArrayList("ordersCounts" , ordersCounts);
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.orderDetailsFragment , bundle );
//        Navigator.instance(requireActivity()).navigate(R.id.orderDetailsFragment , bundle);
    }

    @Override
    public void onShareBtnClicked(Order order) {

    }

    @Override
    public void onStatusBtnClicked(Order order) {
        if(order.getOrderState().equals("In Progress")){
            order.setOrderState("Delivering");
            addOrdersUseCase.updateExistingOrder(order);
        }else if(order.getOrderState().equals("Delivering")){
            order.setOrderState("Done");
            addOrdersUseCase.updateExistingOrder(order);
        }
    }

    @Override
    public void onOrdersDataChange(List<Order> orderList, User user) {
        mvcImp.bindData(orderList);
        mvcImp.hideProgressbar();
        MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.orders_alert_sound);
        mediaPlayer.start();
    }

    @Override
    public void onOrdersDataCancel(DatabaseError error) {
        mvcImp.hideProgressbar();
    }

    @Override
    public void onFinishedOrdersGot(List<Order> finishedOrders) {
        //do nothing here
    }

    @Override
    public void onOrderAddedSuccessfully() {
        //do nothing here
    }

    @Override
    public void onOrderFailedToAdd() {
        //do nothing here
    }

    @Override
    public void onOrderInputError(String message) {
        //do nothing here
    }

    @Override
    public void onOrderDataUpdated(Order order) {
        //do nothing here
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        ordersUseCase.registerListener(this);
        addOrdersUseCase.registerListener(this);
        ordersUseCase.getAllNotFinishedOrders();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        ordersUseCase.unregisterListener(this);
        addOrdersUseCase.unregisterListener(this);
    }


}
