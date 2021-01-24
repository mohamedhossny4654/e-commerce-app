package com.devahmed.techx.onlineshop.Screens.Home.categoryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Models.Category;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.StatefulRecyclerView;

import java.util.List;

public class CategoryAdapter extends StatefulRecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> CategorysList;

    public CategoryAdapter(List<Category> CategorysList) {
        this.CategorysList = CategorysList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category model = CategorysList.get(position);
        holder.textView.setText("" + model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getImage())
                .placeholder(R.drawable.images_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return CategorysList.size();
    }

    public void setList(List<Category> newList) {
        CategorysList = newList;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.categoryImage);
            textView = view.findViewById(R.id.categoryTitle);
        }
    }
}