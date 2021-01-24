package com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl;

public interface MinChargeMvc {

    interface Listener{
        void onUpdateBtnClicked(int newValue);
    }

    void bindData(int value);

}
