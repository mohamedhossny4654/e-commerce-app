package com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList.UseCases.FetchOrdersUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrdersListFragment extends Fragment implements OrdersListMvc.Listener, FetchOrdersUseCase.Listener {


    OrderListMvcImp mvcImp;
    FetchOrdersUseCase ordersUseCase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mvcImp = new OrderListMvcImp(getLayoutInflater() , null);
        ordersUseCase = new FetchOrdersUseCase(FirebaseDatabase.getInstance());

        return mvcImp.getRootView();
    }


    @Override
    public void onOrderItemClicked(Order order) {
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
        bundle.putStringArrayList("ordersCounts" , ordersCounts);
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.orderDetailsFragment , bundle );
//        Navigator.instance(requireActivity()).navigate(R.id.orderDetailsFragment , bundle);
    }

    @Override
    public void onOrdersDataChange(List<Order> orderList , User user) {
        mvcImp.bindData(orderList , user);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onOrdersDataCancel(DatabaseError error) {
        Toast.makeText(requireContext(), "Can't get the order data from server", Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressbar();
    }

    @Override
    public void onFinishedOrdersGot(List<Order> finishedOrders) {

    }


    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        ordersUseCase.registerListener(this);
        if(getArguments() != null){
            if(getArguments().getString("FN").equals("comingFromAdminShow")){
                //get orders of specific user for admin show functionality
                ordersUseCase.getOrdersOfUser(getArguments().getString("userId"));
            }
        }else{
            //just get all orders of the current user
            ordersUseCase.getAllCurrentUserOrders();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        ordersUseCase.unregisterListener(this);
    }
}
