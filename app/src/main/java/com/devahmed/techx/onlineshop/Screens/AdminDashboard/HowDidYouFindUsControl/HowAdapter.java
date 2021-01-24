package com.devahmed.techx.onlineshop.Screens.AdminDashboard.HowDidYouFindUsControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.R;

import java.util.List;

public class HowAdapter extends RecyclerView.Adapter<HowAdapter.ViewHolder> {

    private List<String> StringsList;

    public HowAdapter(List<String> StringsList) {
        this.StringsList = StringsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_how, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String model = StringsList.get(position);
        holder.how_text.setText(model);
    }

    @Override
    public int getItemCount() {
        return StringsList.size();
    }

    public void setList(List<String> newList) {
        StringsList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView how_text;
        public ViewHolder(View view) {
            super(view);
            how_text = view.findViewById(R.id.how_text);
        }
    }
}