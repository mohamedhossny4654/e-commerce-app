package com.devahmed.techx.onlineshop.Screens.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.Screens.Splash.SplashActivity;
import com.devahmed.techx.onlineshop.Utils.LanguageManager;

import java.util.Locale;

public class SettingsFragment extends Fragment implements SettingsMvc.Listener {


    SettingsMvcImp mvcImp;
    LanguageManager languageManager;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mvcImp = new SettingsMvcImp(getLayoutInflater() , null);


        return mvcImp.getRootView();
    }

    @Override
    public void onChangLanguageBtnClicked(boolean isChecked) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String language = preferences.getString("language" , "en");
        if(isChecked){
            preferences.edit().putString("language" , "ar").commit();
            language = "ar";
        }else{
            preferences.edit().putString("language" , "en").commit();
            language = "en";
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        requireActivity().getResources().updateConfiguration(config,requireActivity().getResources().getDisplayMetrics());
        Intent intent = new Intent(requireActivity() , SplashActivity.class);
        startActivity(intent);
        getActivity().finish();
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
