package com.devahmed.techx.onlineshop.Screens.AdminDashboard.UsersControl;

import com.devahmed.techx.onlineshop.Models.User;

import java.util.List;

public interface UserControlsMvc {

    interface Listener{
        void onUserItemClicked(User user);
        void onBlockBtnClicked(User user);
        void onUserInfoBtnClicked(User user);
        void onUserOrdersBtnClicked(User user);
        void onCallBtnClicked(User user);
    }

    void bindData(List<User> userList);
    void showProgressbar();
    void hideProgressbar();
    void showUserDialog(User user);
}
