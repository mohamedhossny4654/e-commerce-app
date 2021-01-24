package com.devahmed.techx.onlineshop.Screens.AdminDashboard.HowDidYouFindUsControl;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;

import java.util.ArrayList;
import java.util.List;

public class HowMvcImp extends BaseObservableMvcView<HowMvc.Listener> implements HowMvc {

    RecyclerView recyclerView;
    HowAdapter adapter;
    List<String> stringList;
    public HowMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.how_fragment , parent, false));
        recyclerView = findViewById(R.id.howRecycler);
        stringList = new ArrayList<>();
        adapter = new HowAdapter(stringList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bindData(List<String> responses) {
        this.stringList = responses;
        adapter.setList(responses);
    }
}
