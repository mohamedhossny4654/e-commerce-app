package com.devahmed.techx.onlineshop.Screens.AdminDashboard.ON_OFF;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;

public class OnOffMvcImp extends BaseObservableMvcView<OnOffMvc.Listener> implements OnOffMvc {

    RecyclerView recyclerView;
    List<Branch> branches;
    OnOffAdapter adapter;
    ProgressBar progressBar;
    public OnOffMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.on_off_fragment , parent , false));

        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler);
        branches = new ArrayList<>();
        adapter = new OnOffAdapter(branches);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            for(Listener listener : getmListeners()){
                    listener.onItemClicked(branches.get(position));
                }
                adapter.setList(branches);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void bindData(List<Branch> branches) {
        this.branches = branches;
        adapter.setList(branches);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }
}
