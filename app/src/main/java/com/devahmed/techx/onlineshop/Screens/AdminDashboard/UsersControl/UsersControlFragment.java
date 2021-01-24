package com.devahmed.techx.onlineshop.Screens.AdminDashboard.UsersControl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddUserUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UsersControlFragment extends Fragment implements UserControlsMvc.Listener, FetchUserInfoFromFirebaseUseCase.Listener, AddUserUseCase.Listener {


    UsersControlMvcImp mvcImp;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    AddUserUseCase userUseCase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mvcImp = new UsersControlMvcImp(getLayoutInflater() , null);
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        userUseCase = new AddUserUseCase(requireActivity());

        return mvcImp.getRootView();
    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        mvcImp.bindData(userList);
        mvcImp.hideProgressbar();
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {
        mvcImp.hideProgressbar();
    }

    @Override
    public void onUserItemClicked(User user) {
        mvcImp.showUserDialog(user);
    }

    @Override
    public void onBlockBtnClicked(User user) {
        if(user.getIsBlocked()){
            user.setIsBlocked(false);
        }else{
            user.setIsBlocked(true);
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userBranch" , 0);
        String userBranch = sharedPreferences.getString("userBranch" , "-M277YZ-fwm7UIQrJZQ6");
        user.setNearestBranch(userBranch);
        userUseCase.updateExistingUser(user);
    }

    @Override
    public void onUserInfoBtnClicked(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("FN" , "comingFromAdminShow");
        bundle.putString("userId" , user.getUserId());
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.accountFragment , bundle );

//        Navigator.instance(requireActivity()).navigate(R.id.accountFragment , bundle);
    }

    @Override
    public void onUserOrdersBtnClicked(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("FN" , "comingFromAdminShow");
        bundle.putString("userId" , user.getUserId());
        Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.ordersListFragment , bundle );

//        Navigator.instance(requireActivity()).navigate(R.id.ordersListFragment );
    }

    @Override
    public void onCallBtnClicked(User user) {
        String phone = user.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void onUserAddedSuccessfully() {

    }

    @Override
    public void onUserFailedToAdd() {

    }

    @Override
    public void onInputError(String message) {

    }

    @Override
    public void onUserDataUpdated(User user) {
        if(user.getIsBlocked()){
            Toast.makeText(requireContext(), "Blocked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(requireContext(), "Un-Blocked", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        userInfoFromFirebaseUseCase.registerListener(this);
        userInfoFromFirebaseUseCase.getAllUsers();
        userUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        userInfoFromFirebaseUseCase.unregisterListener(this);
        userUseCase.unregisterListener(this);
    }
}
