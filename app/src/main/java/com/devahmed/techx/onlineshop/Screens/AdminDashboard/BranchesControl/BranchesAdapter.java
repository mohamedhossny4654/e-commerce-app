package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.R;

import java.util.List;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.ViewHolder>  {

    private List<Branch> BranchsList;
    private String adminBranch;
    public BranchesAdapter(Context context , List<Branch> BranchsList) {
        this.BranchsList = BranchsList;
        SharedPreferences sharedPreferences = context.getSharedPreferences("userBranch" , 0);
        this.adminBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_branch_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Branch model = BranchsList.get(position);
        if(model.getId().equals(adminBranch)){
            holder.branchNameTextView.setTextColor(Color.GREEN);
            holder.branchLocationTextView.setTextColor(Color.GREEN);
        }else{
            holder.branchNameTextView.setTextColor(Color.BLACK);
            holder.branchLocationTextView.setTextColor(Color.BLACK);
        }
        holder.branchLocationTextView.setText(model.getmLat() + " : " + model.getmLong());
        holder.branchNameTextView.setText(model.getName() + "\t\t " + model.getAcceptedOrdersRange() + "-Km");
    }

    @Override
    public int getItemCount() {
        return BranchsList.size();
    }

    public void setList(List<Branch> newList) {
        BranchsList = newList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchNameTextView , branchLocationTextView;
        public ViewHolder(View view) {
            super(view);
            branchLocationTextView = view.findViewById(R.id.branchLocationTextView);
            branchNameTextView = view.findViewById(R.id.branchNameTextView);
        }
    }
}