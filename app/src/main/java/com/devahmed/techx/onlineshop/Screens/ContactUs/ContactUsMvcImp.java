package com.devahmed.techx.onlineshop.Screens.ContactUs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;

public class ContactUsMvcImp extends BaseObservableMvcView<ContactUsMvc.Listener> implements ContactUsMvc {

    TextView phoneNumberTextView;
    public ContactUsMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.contact_us_fragment , parent , false));
        phoneNumberTextView = findViewById(R.id.phoneNumber);
        phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onPhoneNumberClicked(phoneNumberTextView.getText().toString());
                }
            }
        });
    }
}
