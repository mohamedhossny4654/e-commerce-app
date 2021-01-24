package com.devahmed.techx.onlineshop.Screens.ContactUs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ContactUsFragment extends Fragment implements ContactUsMvc.Listener {

    ContactUsMvcImp mvcImp;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new ContactUsMvcImp(getLayoutInflater() ,null);


        return mvcImp.getRootView();
    }


    @Override
    public void onFaceBookBtnClicked() {

    }

    @Override
    public void onWhatsAppBtnClicked() {

    }

    @Override
    public void onInstaGramBtnClicked() {

    }

    @Override
    public void onTwitterBtnClicked() {

    }

    @Override
    public void onPhoneNumberClicked(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
    }
}
