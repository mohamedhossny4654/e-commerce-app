package com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.DeliverCost;
import com.devahmed.techx.onlineshop.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverCostAdapter extends RecyclerView.Adapter<DeliverCostAdapter.ViewHolder> {

    private List<DeliverCost> DeliverCostsList;

    private List<ViewHolder> holders;

    public DeliverCostAdapter(List<DeliverCost> DeliverCostsList) {
        this.DeliverCostsList = DeliverCostsList;
        this.holders = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_delivery_cost, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holders.add(holder);
        if(DeliverCostsList != null && ! DeliverCostsList.isEmpty()){
            DeliverCost model = DeliverCostsList.get(position);
            holder.from.setText( "" + model.getFrom());
            holder.to.setText("" + model.getTo());
            holder.price.setText("" + model.getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return DeliverCostsList.size();//DeliverCostsList.size();
    }

    public List<DeliverCost> getDeliverCostsList(){
        for (int i = 0; i <DeliverCostsList.size() ; i++){
            DeliverCost cost = DeliverCostsList.get(i);
            cost.setFrom(Integer.parseInt(holders.get(i).from.getText().toString()));
            cost.setTo(Integer.parseInt(holders.get(i).to.getText().toString()));
            cost.setPrice(Integer.parseInt(holders.get(i).price.getText().toString()));
        }
        return this.DeliverCostsList;
    }

    public void setList(List<DeliverCost> newList) {
        DeliverCostsList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText from , to , price;
        public ViewHolder(View view) {
            super(view);
            from = view.findViewById(R.id.from);
            to = view.findViewById(R.id.to);
            price = view.findViewById(R.id.price);
        }
    }
}