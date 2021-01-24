package com.devahmed.techx.onlineshop.Screens.SubCategories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.Models.SubCategory;
import com.devahmed.techx.onlineshop.R;

import java.util.List;

public class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

    private List<SubCategory> SubCategorysList;

    public SubcategoriesAdapter(List<SubCategory> SubCategorysList) {
        this.SubCategorysList = SubCategorysList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subcategory, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubCategory model = SubCategorysList.get(position);
        holder.textView.setText("" + model.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.images_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return SubCategorysList.size();
    }

    public void setList(List<SubCategory> newList) {
        SubCategorysList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  imageView;
        TextView textView;
        CardView container;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.subcategoryTitle);
            imageView = view.findViewById(R.id.subcategoryImage);
            container = view.findViewById(R.id.subCategoryContainer);
        }
    }
}