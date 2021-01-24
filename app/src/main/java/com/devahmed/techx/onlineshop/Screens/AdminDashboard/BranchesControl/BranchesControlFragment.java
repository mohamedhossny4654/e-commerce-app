package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.AddBranchUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.FetchBranchesUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.GetLocationUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BranchesControlFragment extends Fragment implements BranchesMvc.Listener, FetchBranchesUseCase.Listener, GetLocationUseCase.Listener, AddBranchUseCase.Listener {

    BranchesMvcImp mvcImp;
    FetchBranchesUseCase branchesUseCase;
    GetLocationUseCase locationUseCase;
    AddBranchUseCase branchUseCase;
    Branch branch;
    String MODE = null;//if CREATE_MODE  we create new branch - if EDIT_MODE -> we edit ^_^
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new BranchesMvcImp(getLayoutInflater() , null);
        branchesUseCase = new FetchBranchesUseCase(FirebaseDatabase.getInstance());
        locationUseCase = new GetLocationUseCase(requireActivity());
        branchUseCase = new AddBranchUseCase(requireActivity());
        branch = new Branch();

        return mvcImp.getRootView();
    }


    @Override
    public void onAddBranchBtnClicked() {
        mvcImp.showAddBranchDialog();
        MODE = "CREATE";

    }

    @Override
    public void onBranchItemClicked(Branch branch) {
        this.branch.setId(branch.getId());
        mvcImp.showAddBranchDialog();
        MODE = "EDIT";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userBranch" , 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userBranch" ,branch.getId());
        editor.commit();
    }

    @Override
    public void onGetLocationBtnClicked(String branchName) {
        if(branchName.isEmpty() || branchName.equals("")) {
            Toast.makeText(requireContext(), "Please enter valid branch name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            locationUseCase.checkPermission();
        }
        branch.setName(branchName);
        locationUseCase.getCurrentLocation(requireActivity());

    }

    @Override
    public void onCreateBranchBtnClicked() {
        //create new branch on firebase
        if(MODE.equals("CREATE")){
            branchUseCase.addNewBranch(branch);
        }else if(MODE.equals("EDIT")){
            branchUseCase.updateExistingBranch(branch);
        }
    }

    @Override
    public void onRangeProgressChanged(int progress) {
        branch.setAcceptedOrdersRange(progress);
    }

    @Override
    public void onBranchDataChange(List<Branch> dataList) {
        mvcImp.bindData(dataList);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onBranchDataCancel(DatabaseError error) {
        mvcImp.hideProgressbar();
    }

    @Override
    public void onLatLongLoaded(double mLat, double mLong) {
        branch.setmLat(mLat);
        branch.setmLong(mLong);
        Toast.makeText(getContext(), "lat " + mLat + " : Long " + mLong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGpsLocationLoaded(String gpsLocation) {
        branch.setGpsLocation(gpsLocation);
        mvcImp.showLocationContainer(branch);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchAddedSuccessfully() {
        Toast.makeText(requireActivity(), "Branch added successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchFailedToAdd() {
        Toast.makeText(requireActivity(), "Branch is not added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInputError(String message) {
        Toast.makeText(requireActivity(), "Error " + message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        branchesUseCase.registerListener(this);
        locationUseCase.registerListener(this);
        branchUseCase.registerListener(this);
        branchesUseCase.getAllBranches();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        branchesUseCase.unregisterListener(this);
        locationUseCase.unregisterListener(this);
        branchUseCase.unregisterListener(this);
    }

}
