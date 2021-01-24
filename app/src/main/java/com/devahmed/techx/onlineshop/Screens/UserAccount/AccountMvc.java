package com.devahmed.techx.onlineshop.Screens.UserAccount;

import com.devahmed.techx.onlineshop.Models.User;

public interface AccountMvc {
    interface Listener{
        void onApplyBtnClicked();
        void onUseGPSBtnClicked();
        void onLogoutBtnClicked();
        void onCartConfirmBtnCLicked();
        void onEditBtnClicked();
    }

    void bindData(User user);
    void activateEditMode();
    void activateNormalShowMode();
    void activateCartConfirmationMode();
    void activateAdminShowMode();
    void showProgressbar();
    void hideProgressbar();
}
