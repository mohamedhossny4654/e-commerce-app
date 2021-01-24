package com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Models.DeliverCost;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.DeliverCost.UseCases.FetchDeliveryCostesUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DeliverCostFragment extends Fragment implements DeliveryCostMvc.Listener, FetchDeliveryCostesUseCase.Listener {

    DeliveryCostMvcImp mvcImp;
    FetchDeliveryCostesUseCase deliveryCostsUseCase;
    List<DeliverCost> deliverCostList;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new DeliveryCostMvcImp(getLayoutInflater() , null);
        deliveryCostsUseCase = new FetchDeliveryCostesUseCase(FirebaseDatabase.getInstance());


        return mvcImp.getRootView();
    }

    @Override
    public void onSubmitBtnClicked() {
        List<DeliverCost> deliverCosts = mvcImp.getDeliverCostList();

        int from1 ,from2 , from3 , to1 , to2 , to3 , price1 , price2 , price3;
        from1 = deliverCosts.get(0).getFrom(); to1 = deliverCosts.get(0).getTo(); price1 = deliverCosts.get(0).getPrice();
        from2 = deliverCosts.get(1).getFrom(); to2 = deliverCosts.get(1).getTo(); price2 = deliverCosts.get(1).getPrice();
        from3 = deliverCosts.get(2).getFrom(); to3 = deliverCosts.get(2).getTo(); price3 = deliverCosts.get(2).getPrice();
        if(from1 > from2 || from1 > from3 || from2 > from3){
            Toast.makeText(requireContext(), "Error in \"from\" value", Toast.LENGTH_SHORT).show();
            return;
        }
        if(from1 > to1 || from2 > to2 || from3 > to3){
            Toast.makeText(requireContext(), "Error \"from\" value must be less than \"to\"", Toast.LENGTH_SHORT).show();
            return;
        }
        if(to1 > to2 || to1 > to3 || to2 > to3){
            Toast.makeText(requireContext(), "Error in \"To\" value", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i <3 ; i++) {
            DeliverCost cost = deliverCosts.get(i);
            deliveryCostsUseCase.updateExistingDeliverCost(cost);
        }
        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show();

    }

    @Override

    public void onDeliveryCostDataChange(List<DeliverCost> dataList) {
        mvcImp.bindData(dataList);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onDeliveryCostDataCancel(DatabaseError error) {
        Toast.makeText(requireContext(), "Error " + error.getMessage() , Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        deliveryCostsUseCase.registerListener(this);
        deliveryCostsUseCase.getAllDeliveryCostes();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        deliveryCostsUseCase.unregisterListener(this);
    }

}
