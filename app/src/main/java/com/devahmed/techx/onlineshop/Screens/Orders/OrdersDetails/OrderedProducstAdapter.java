package com.devahmed.techx.onlineshop.Screens.Orders.OrdersDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;

import java.util.List;

public class OrderedProducstAdapter extends RecyclerView.Adapter<OrderedProducstAdapter.ViewHolder> {

    private List<Product> ProductsList;
    private List<String> countsList;

    public OrderedProducstAdapter(List<Product> ProductsList) {
        this.ProductsList = ProductsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ordered_product, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product model = ProductsList.get(position);
        Glide.with(holder.noOfItemsInCartText.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.images_placeholder)
                .into(holder.row_cart_productImage);
        holder.noOfItemsInCartText.setText("x " + countsList.get(position));
        double price = model.getPrice() * Integer.parseInt(countsList.get(position));
        holder.cartProductItemTotalPrice.setText("" + price + "-EP");
    }

    @Override
    public int getItemCount() {
        return ProductsList.size();
    }

    public void setList(List<Product> newList , List<String> countsList) {
        ProductsList = newList;
        this.countsList = countsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView row_cart_productImage;
        TextView noOfItemsInCartText , cartProductItemTotalPrice;
        public ViewHolder(View view) {
            super(view);
            row_cart_productImage = view.findViewById(R.id.row_cart_productImage);
            noOfItemsInCartText = view.findViewById(R.id.noOfItemsInCartText);
            cartProductItemTotalPrice = view.findViewById(R.id.cartProductItemTotalPrice);
        }
    }
}