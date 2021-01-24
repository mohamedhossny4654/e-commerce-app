package com.devahmed.techx.onlineshop.Screens.Orders.MyOrdersList;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> OrdersList , otherOne;
    public OrdersAdapter(List<Order> OrdersList) {
        this.OrdersList = OrdersList;
        otherOne = OrdersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order model = otherOne.get(position);
        holder.statusText.setText("status : " + model.getOrderState());
        holder.orderID.setText("Order ID : " + model.getId().substring(0 , 5));
        holder.orderDate.setText("Date : " + timeStampToString((long) model.getTimeStamp()));
        holder.orderPrice.setText("Total Price : " + model.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        int size = otherOne.size();
        return size;
    }

    public void setList(List<Order> newList) {
        OrdersList = newList;
        otherOne = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderPrice , orderID , orderDate , statusText;
        public ViewHolder(View view) {
            super(view);
            statusText = view.findViewById(R.id.statusText);
            orderID = view.findViewById(R.id.orderIdText);
            orderDate = view.findViewById(R.id.orderDateText);
            orderPrice = view.findViewById(R.id.orderPriceText);
        }
    }

    //FN to conver timestamp object from long data type into a readable string
    private String timeStampToString(long timeStamp){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date = DateFormat.format("dd/MM/yyyy" , calendar).toString();
        String time = DateFormat.format("HH:MM" , calendar).toString();
        return date + "     at      " + time;
    }
}