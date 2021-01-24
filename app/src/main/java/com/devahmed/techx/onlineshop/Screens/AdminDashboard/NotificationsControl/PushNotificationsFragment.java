package com.devahmed.techx.onlineshop.Screens.AdminDashboard.NotificationsControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Models.Notification;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.NotificationsControl.UseCase.NotificationSenderUseCase;

public class PushNotificationsFragment extends Fragment implements NotificationsMvc.Listener, NotificationSenderUseCase.Listener {
    NotificationsMvcImp mvcImp;
    NotificationSenderUseCase notificationSenderUseCase;
    boolean withPhoto = false;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new NotificationsMvcImp(getLayoutInflater() , null);
        notificationSenderUseCase = new NotificationSenderUseCase(requireActivity());

        return mvcImp.getRootView();
    }

    @Override
    public void onSendNotificationBtnClicked() {
        Toast.makeText(requireActivity(), "Sent", Toast.LENGTH_SHORT).show();
        if(!withPhoto){
            Notification notification = new Notification();
            notification.setTitle(mvcImp.getTitle());
            notification.setBody(mvcImp.getBody());
            notificationSenderUseCase.senNotificationWithBody(notification);
            mvcImp.clear();
        }
    }

    @Override
    public void onNotificationImageClicked() {

    }

    @Override
    public void onSwitchModeChange(boolean isChecked) {
        withPhoto = isChecked;
    }

    @Override
    public void onNotificationAddedSuccessfully() {

    }

    @Override
    public void onNotificationFailedToAdd() {

    }

    @Override
    public void onInputError(String message) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        notificationSenderUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        notificationSenderUseCase.unregisterListener(this);
    }

}
