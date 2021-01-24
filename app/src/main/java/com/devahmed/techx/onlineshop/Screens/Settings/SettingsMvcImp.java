package com.devahmed.techx.onlineshop.Screens.Settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;

public class SettingsMvcImp extends BaseObservableMvcView<SettingsMvc.Listener> implements SettingsMvc {

    Switch aSwitchLanguage;

    public SettingsMvcImp(LayoutInflater inflater , ViewGroup parent) {

        setRootView(inflater.inflate(R.layout.settings_fragment , parent ,false));
        aSwitchLanguage = findViewById(R.id.switchLanguage);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String language = sharedPreferences.getString("language", "en");
        if(language.equals("en")){
            aSwitchLanguage.setChecked(false);
            aSwitchLanguage.setText("English");
        }else if(language.equals("ar")){
            aSwitchLanguage.setChecked(true);
            aSwitchLanguage.setText("Arabic");
        }
        aSwitchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(Listener listener : getmListeners()){
                    listener.onChangLanguageBtnClicked(isChecked);
                }
                if(isChecked){
                    aSwitchLanguage.setText("Arabic");
                }else{
                    aSwitchLanguage.setText("English");
                }
            }
        });


    }

    @Override
    public void bindData(String language) {

    }
}
