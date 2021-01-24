package com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl.UseCase.FetchMinChargesUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MinChargeFragment extends Fragment implements MinChargeMvc.Listener, FetchMinChargesUseCase.Listener {


    MinChargeMvcImp mvcImp;
    FetchMinChargesUseCase minChargesUseCase;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new MinChargeMvcImp(getLayoutInflater() , null);
        minChargesUseCase = new FetchMinChargesUseCase(FirebaseDatabase.getInstance());

        return mvcImp.getRootView();
    }


    @Override
    public void onUpdateBtnClicked(int newValue) {
        minChargesUseCase.updateExistingMinCharge("" + newValue);
        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMinChargeDataChange(Map<String , String> mapData) {
        if(!mapData.isEmpty()){
            mvcImp.bindData(Integer.parseInt(mapData.get("minCharge")));
        }

    }

    @Override
    public void onMinChargeDataCancel(DatabaseError error) {

    }

    @Override
    public void onInputError(String message) {
        Toast.makeText(requireContext(), "" + message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        minChargesUseCase.registerListener(this);
        minChargesUseCase.getMinCharge();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        minChargesUseCase.unregisterListener(this);
    }


}
