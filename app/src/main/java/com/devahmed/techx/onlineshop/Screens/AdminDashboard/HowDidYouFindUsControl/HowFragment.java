package com.devahmed.techx.onlineshop.Screens.AdminDashboard.HowDidYouFindUsControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Screens.AdminDashboard.HowDidYouFindUsControl.UseCases.FetchStringesUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HowFragment extends Fragment implements HowMvc.Listener, FetchStringesUseCase.Listener {


    HowMvcImp mvcImp;
    FetchStringesUseCase stringesUseCase;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new HowMvcImp(getLayoutInflater() , null);
        stringesUseCase = new FetchStringesUseCase(FirebaseDatabase.getInstance());

        stringesUseCase.getAllStringes();

        return mvcImp.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        stringesUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        stringesUseCase.unregisterListener(this);
    }

    @Override
    public void onStringDataChange(List<String> dataList) {
        mvcImp.bindData(dataList);
    }

    @Override
    public void onStringDataCancel(DatabaseError error) {

    }
}
