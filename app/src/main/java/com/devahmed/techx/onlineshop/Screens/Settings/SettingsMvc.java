package com.devahmed.techx.onlineshop.Screens.Settings;

public interface SettingsMvc {
    interface Listener{
        void onChangLanguageBtnClicked(boolean isChecked);
    }
    void bindData(String language);
}
