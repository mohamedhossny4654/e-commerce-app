package com.devahmed.techx.onlineshop.Screens.UserAccount;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;

import java.util.Calendar;

public class AccountMvcImp extends BaseObservableMvcView<AccountMvc.Listener> implements AccountMvc{

    private EditText phone , name , area , street , building, nearestLandmark , floorNo , flatNo;
    private Button pointsBtn;
    private TextView earnPointsTextView , yourInfoTextView , phoneNoTextView , yourNameTextView , yourAddressTextView
            ,areaTextView , streetNoTextView , buildingNoTextView , nearestLandmarkTextView , helpUsFindUGpsTextView;
    private ConstraintLayout pointsContainer;
    private Button useGPSBtn , logOutBtn , applyDataBtn , cartConfirmationBtn;
    private ProgressBar progressBar;
    private ImageView editBtn;
    private LinearLayout orderAtContainer;
    private RadioGroup orderAtRadioGroup;
    private RadioButton selectedRadioBtn;
    private String orderAtString = "now";
    private int mLat , mLong;
    private String GpsLocation;

    public AccountMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.account_fragment , parent , false));

        initViews();
        cartConfirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onCartConfirmBtnCLicked();
                }
            }
        });

        useGPSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onUseGPSBtnClicked();
                }
                useGPSBtn.setVisibility(View.INVISIBLE);
                helpUsFindUGpsTextView.setVisibility(View.INVISIBLE);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onLogoutBtnClicked();
                }
            }
        });

        applyDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onApplyBtnClicked();
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onEditBtnClicked();
                }
            }
        });

        orderAtRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                selectedRadioBtn = (RadioButton)group.findViewById(checkedId);
                String radioChoice = selectedRadioBtn.getText().toString().toLowerCase().trim();

                if(radioChoice.equalsIgnoreCase("Now")){
                    orderAtString = "now";
                }else{
                    showTimePicker();
                }
            }
        });
    }

    private void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //time should not be after mid night
                        if(mHour >= 0 && mHour < 8){
                            UtilsDialog dialog = new UtilsDialog(getContext());
                            dialog.showTextMessage(getContext().getResources().getString(R.string.time_alert2));
                            dialog.addCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    showTimePicker();
                                }
                            });
                            return;
                        }
                        //time should be two hours from current time
                        if(hourOfDay - mHour < 2){
                            UtilsDialog dialog = new UtilsDialog(getContext());
                            dialog.showTextMessage(getContext().getResources().getString(R.string.time_alert));
                            dialog.addCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    showTimePicker();
                                }
                            });
                            return;
                        }
                        orderAtString = hourOfDay + ":" + minute;
                        selectedRadioBtn.setText(orderAtString);
                    }
                }, mHour + 2 , mMinute, true);
        timePickerDialog.show();
    }

    private void initViews() {
        earnPointsTextView = findViewById(R.id.EarPointsAndGetDiscountsTextView);
        yourInfoTextView = findViewById(R.id.yourInfoTextView);
        phoneNoTextView = findViewById(R.id.phoneNoTextView);
        yourNameTextView = findViewById(R.id.yourNameTextView);
        flatNo = findViewById(R.id.accountflatNo);
        floorNo = findViewById(R.id.accountFloor);
        yourAddressTextView = findViewById(R.id.yourAddressTextView);
        areaTextView = findViewById(R.id.areaTextView);
        streetNoTextView = findViewById(R.id.streetNoTextView);
        buildingNoTextView = findViewById(R.id.buildingNoTextView);
        nearestLandmarkTextView = findViewById(R.id.nearestLandMarkTextView);
        helpUsFindUGpsTextView = findViewById(R.id.GPSText);
        orderAtContainer = findViewById(R.id.orderAtContainer);
        orderAtRadioGroup = findViewById(R.id.oderAtRadioGroup);
        phone = findViewById(R.id.accountPhoneNumber);
        progressBar = findViewById(R.id.progressBar);
        name = findViewById(R.id.accountName);
        cartConfirmationBtn = findViewById(R.id.cartConfirmationBtn);
        area = findViewById(R.id.accountArea);
        editBtn = findViewById(R.id.editBtn);
        street = findViewById(R.id.accountStreetNumber);
        building = findViewById(R.id.accountBuildingNumber);
        nearestLandmark = findViewById(R.id.accountUniqueSign);
        pointsBtn = findViewById(R.id.pointsBtn);
        pointsContainer = findViewById(R.id.countsContainer);
        useGPSBtn = findViewById(R.id.accoutnUseGPSBtn);
        logOutBtn = findViewById(R.id.accountLogOutBtn);
        applyDataBtn = findViewById(R.id.accountApplyDataBtn);
    }


    @Override
    public void bindData(User user) {
        this.phone.setText(user.getPhone());
        this.name.setText(user.getName());
        this.area.setText(user.getArea());
        this.street.setText(user.getStreet());
        this.building.setText(user.getBuilding());
        this.nearestLandmark.setText(user.getNearestLandmark());
        this.pointsBtn.setText(user.getPoints() + "\n" + getContext().getResources().getString(R.string._120_n_points));
        this.flatNo.setText(user.getFlat());
        this.floorNo.setText(user.getFloor());
    }

    @Override
    public void activateEditMode() {
        applyDataBtn.setVisibility(View.VISIBLE);
        logOutBtn.setVisibility(View.GONE);
        pointsContainer.setVisibility(View.GONE);
        Toast.makeText(getContext(), "activateEditMode", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void activateNormalShowMode() {
        applyDataBtn.setVisibility(View.GONE);
        logOutBtn.setVisibility(View.VISIBLE);
        pointsContainer.setVisibility(View.VISIBLE);
        cartConfirmationBtn.setVisibility(View.GONE);
        useGPSBtn.setVisibility(View.GONE);
        helpUsFindUGpsTextView.setVisibility(View.GONE);
    }

    @Override
    public void activateCartConfirmationMode() {
        pointsContainer.setVisibility(View.GONE);
        applyDataBtn.setVisibility(View.GONE);
        logOutBtn.setVisibility(View.GONE);
        cartConfirmationBtn.setVisibility(View.VISIBLE);
        orderAtContainer.setVisibility(View.VISIBLE);
        useGPSBtn.setVisibility(View.VISIBLE);
        helpUsFindUGpsTextView.setVisibility(View.VISIBLE);
        orderAtString = "now";
    }

    @Override
    public void activateAdminShowMode() {
        pointsContainer.setVisibility(View.GONE);
        applyDataBtn.setVisibility(View.GONE);
        logOutBtn.setVisibility(View.GONE);
        cartConfirmationBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.GONE);

    }

    private void enableEditText(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);

    }
    public String getOrderAtTime(){
        return orderAtString;
    }


    private void disableEditText(EditText editText){
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
        cartConfirmationBtn.setEnabled(false);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
        cartConfirmationBtn.setEnabled(true);
    }

    public void setLocation(String gpsLocation , int mLat , int mLong){
        this.GpsLocation = gpsLocation;
        this.mLat = mLat;
        this.mLong = mLong;
    }

    public User getUserData() {
        User user = new User();
        user.setPhone(phone.getText().toString());
        user.setName(name.getText().toString());
        user.setStreet(street.getText().toString());
        user.setArea(area.getText().toString());
        user.setFlat(flatNo.getText().toString());
        user.setBuilding(building.getText().toString());
        user.setnearestLandmark(nearestLandmark.getText().toString());
        user.setFloor(floorNo.getText().toString());
        user.setGpsAddress(this.GpsLocation);
        user.setxPos(this.mLat);
        user.setyPos(this.mLong);
        return user;
    }
}
