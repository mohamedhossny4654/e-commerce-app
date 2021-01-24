package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BillsControl.BillsItemList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.Screens.ProductsShow.UseCases.FetchProductsUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.devahmed.techx.onlineshop.Utils.BillCanvasView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> implements BillsItemMvc.Listener, FetchUserInfoFromFirebaseUseCase.Listener, FetchProductsUseCase.Listener {

    private OnItemClickListener mListener;
    FetchProductsUseCase productsUseCase;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    List<User> userList;
    List<Product> productList;
    private List<Order> OrdersList;
    @Override
    public void onBillItemClicked(Order order , BillCanvasView billCanvasView) {
        mListener.onBillItemClicked(order , billCanvasView);
    }


    @Override
    public void onDownloadItemClicked(Order order, BillCanvasView billCanvasView) {
        mListener.onDownloadItemClicked(order, billCanvasView);
    }

    @Override
    public void onShareBtnClicked(Order order, BillCanvasView billCanvasView) {
        mListener.onShareBtnClicked(order, billCanvasView);
    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {

    }

    @Override
    public void onProductsDataChange(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @Override
    public void onProductsDataCancel(DatabaseError error) {

    }

    public interface OnItemClickListener {
        void onBillItemClicked(Order order , BillCanvasView billCanvasView);
        void onDownloadItemClicked(Order order, BillCanvasView billCanvasView);
        void onShareBtnClicked(Order order, BillCanvasView billCanvasView);
    }



    public OrdersAdapter(List<Order> OrdersList, OnItemClickListener listener) {
        this.OrdersList = OrdersList;
        this.mListener = listener;
        this.productList = new ArrayList<>();
        this.userList = new ArrayList<>();
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        productsUseCase = new FetchProductsUseCase(FirebaseDatabase.getInstance());
        productsUseCase.registerListener(this);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BillsItemMvcImp mvcImp = new BillsItemMvcImp(LayoutInflater.from(parent.getContext()), parent);
        userInfoFromFirebaseUseCase.registerListener(this);
        mvcImp.registerListener(this);
        return new ViewHolder(mvcImp);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order model = OrdersList.get(position);
        User user = null;
        List<Product> usedProductsList = new ArrayList<>();//the list that contains products that is related to this order
        ArrayList<String> orderedItems = new ArrayList<>();
        for (String productId : model.getOrderedItemsWithCounts().keySet()) {
            orderedItems.add(productId);
        }
        for(int i = 0; i < userList.size(); i++){
            for (int j = 0; j < OrdersList.size(); j++) {
                //get the user that is related with this order
                if(userList.get(i).getUserId().equals(OrdersList.get(j).getUserId())){
                    user = userList.get(i);
                }
            }
        }
        //loop through all products and if find matching between orderedItemsIDs & products ID => add it to userProductsList
        for (int i = 0; i <productList.size() ; i++) {
            for (int j = 0; j <orderedItems.size() ; j++) {
                if(productList.get(i).getId().equals(orderedItems.get(j))){
                    usedProductsList.add(productList.get(i));
                }
            }
        }
        holder.recyclerViewListItemMvcImp.bindData(model , usedProductsList , user , position);
    }

    @Override
    public int getItemCount() {
        return OrdersList.size();
    }

    public void setList(List<Order> newList) {
        OrdersList = newList;
        for(Order order : newList){
            ArrayList<String> orderedItems = new ArrayList<>();
            for (String productId : order.getOrderedItemsWithCounts().keySet()) {
                orderedItems.add(productId);
            }
            productsUseCase.getAllProducts();
            userInfoFromFirebaseUseCase.getUserOfID(order.getUserId());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final BillsItemMvcImp recyclerViewListItemMvcImp;

        public ViewHolder(BillsItemMvcImp viewMvcImp) {
            super(viewMvcImp.getRootView());
            recyclerViewListItemMvcImp = viewMvcImp;
        }
    }



}