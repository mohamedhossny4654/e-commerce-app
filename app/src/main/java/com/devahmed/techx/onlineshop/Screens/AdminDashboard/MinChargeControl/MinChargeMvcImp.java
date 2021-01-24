package com.devahmed.techx.onlineshop.Screens.AdminDashboard.MinChargeControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;

public class MinChargeMvcImp extends BaseObservableMvcView<MinChargeMvc.Listener> implements MinChargeMvc {

    EditText minChargeText;
    Button minChargeButton;
    public MinChargeMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.min_charge_fragment , parent , false));

        initViews();
        minChargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onUpdateBtnClicked(Integer.parseInt(minChargeText.getText().toString()));
                }
            }
        });
        minChargeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minChargeText.setText("");
            }
        });
        minChargeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                minChargeText.setText("");
            }
        });
    }

    private void initViews() {
        minChargeText = findViewById(R.id.minChargeText);
        minChargeButton = findViewById(R.id.minChargeUpdateBtn);
    }

    @Override
    public void bindData(int value) {
        this.minChargeText.setText("" + value + "-EG");
    }
}
