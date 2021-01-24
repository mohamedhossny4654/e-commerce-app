package com.devahmed.techx.onlineshop.Screens.AdminDashboard.UsersControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> UsersList;

    public UsersAdapter(List<User> UsersList) {
        this.UsersList = UsersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User model = UsersList.get(position);
        holder.phoneTextView.setText(model.getPhone());
        if(model.getName() == null){
            holder.nameTextView.setText("Unknown");
        }else{
            holder.nameTextView.setText(model.getName());
        }
        if(model.getIsBlocked()){
            holder.blockBtn.setImageResource(R.drawable.ic_block_24dp);
        }else{
            holder.blockBtn.setImageResource(R.drawable.ic_block_grey_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    public void setList(List<User> newList) {
        UsersList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView blockBtn;
        TextView phoneTextView , nameTextView;
        public ViewHolder(View view) {
            super(view);
            blockBtn = view.findViewById(R.id.blockBtn);
            phoneTextView = view.findViewById(R.id.phoneTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
        }
    }
}