package com.devahmed.techx.onlineshop.Screens.AdminDashboard.ON_OFF;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.AddBranchUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.FetchBranchesUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OnOffFragment extends Fragment implements OnOffMvc.Listener, FetchBranchesUseCase.Listener, AddBranchUseCase.Listener {

    OnOffMvcImp mvcImp;
    FetchBranchesUseCase branchesUseCase;
    AddBranchUseCase addBranchUseCase;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvcImp = new OnOffMvcImp(getLayoutInflater() , null);
        branchesUseCase = new FetchBranchesUseCase(FirebaseDatabase.getInstance());
        addBranchUseCase = new AddBranchUseCase(requireActivity());
        branchesUseCase.getAllBranches();

        return mvcImp.getRootView();
    }

    @Override
    public void onItemClicked(Branch branch) {
        branch.setOpenNow(!branch.getIsOpenNow());
        addBranchUseCase.updateExistingBranch(branch);
    }

    @Override
    public void onBranchDataChange(List<Branch> dataList) {
        mvcImp.bindData(dataList);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onBranchDataCancel(DatabaseError error) {
        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        branchesUseCase.registerListener(this);
        addBranchUseCase.registerListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        branchesUseCase.unregisterListener(this);
        addBranchUseCase.unregisterListener(this);
    }


    @Override
    public void onBranchAddedSuccessfully() {

    }

    @Override
    public void onBranchFailedToAdd() {

    }

    @Override
    public void onInputError(String message) {

    }
}
