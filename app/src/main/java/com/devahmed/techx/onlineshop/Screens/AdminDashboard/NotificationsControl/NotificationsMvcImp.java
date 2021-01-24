package com.devahmed.techx.onlineshop.Screens.AdminDashboard.NotificationsControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;

public class NotificationsMvcImp extends BaseObservableMvcView<NotificationsMvc.Listener> implements NotificationsMvc {

    EditText notificationTitle , notificationBody;
    Button sendNotificationBtn;
    ImageView notificationImage;
    Switch aSwitch;

    public NotificationsMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.push_notifications_fragment , parent , false));
        initViews();

        sendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSendNotificationBtnClicked();
                }
            }
        });

        notificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onNotificationImageClicked();
                }
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(Listener listener : getmListeners()){
                    listener.onSwitchModeChange(isChecked);
                }
            }
        });


    }

    private void initViews() {
        notificationTitle = findViewById(R.id.notificationTitle);
        notificationBody = findViewById(R.id.notificationBody);
        sendNotificationBtn = findViewById(R.id.sendNotificationBtn);
        notificationImage = findViewById(R.id.notificationImage);
        aSwitch = findViewById(R.id.notificationSwitchMode);
    }

    public String getTitle(){
        return notificationTitle.getText().toString();
    }

    public String getBody(){
        return notificationBody.getText().toString();
    }

    @Override
    public void activatePhotoMode() {

    }

    @Override
    public void activateBodyMode() {

    }

    public void clear(){
        notificationTitle.setText("");
        notificationBody.setText("");
    }
}
