package com.devahmed.techx.onlineshop.Screens.LocationAtFirstTime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.FetchBranchesUseCase;
import com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl.UseCases.GetLocationUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.AddUserUseCase;
import com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases.FetchUserInfoFromFirebaseUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LocationAtFirstTime extends AppCompatActivity implements GetLocationUseCase.Listener, FetchBranchesUseCase.Listener, AddUserUseCase.Listener, FetchUserInfoFromFirebaseUseCase.Listener {
    Button getLocationButton;
    ProgressBar progressBar;
    TextView weNeedYourLocationTextView;
    GetLocationUseCase locationUseCase;
    FetchBranchesUseCase branchesUseCase;
    FetchUserInfoFromFirebaseUseCase userInfoFromFirebaseUseCase;
    AddUserUseCase userUseCase;
    List<Branch> dataList;
    String userGpsLocation = "";
    Location userLocation;
    boolean stepOneApprove = false, stepTwoApprove = false;
    User currentUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_at_first_time);
        locationUseCase = new GetLocationUseCase(this);
        branchesUseCase = new FetchBranchesUseCase(FirebaseDatabase.getInstance());
        userInfoFromFirebaseUseCase = new FetchUserInfoFromFirebaseUseCase(FirebaseDatabase.getInstance());
        userUseCase = new AddUserUseCase(this);
        weNeedYourLocationTextView = findViewById(R.id.weNeedYourLocationText);
        weNeedYourLocationTextView.setText(getResources().getString(R.string.we_nee_your_location));
        progressBar = findViewById(R.id.progressBar);
        getLocationButton = findViewById(R.id.getLocationBtn);
        getLocationButton.setText(getResources().getString(R.string.get_location));
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null || dataList == null) return;
                if(ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LocationAtFirstTime.this , new String[] {Manifest.permission.ACCESS_FINE_LOCATION} , 1);
                }else{
                     locationUseCase.getCurrentLocation(LocationAtFirstTime.this);
                     progressBar.setVisibility(View.VISIBLE);
                     getLocationButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onLatLongLoaded(double mLat, double mLong) {
        userLocation = new Location("userProvider");
        userLocation.setLatitude(mLat);
        userLocation.setLongitude(mLong);
        locationUseCase.getGpsLocation();
//        Toast.makeText(getApplicationContext(), "lat " + mLat + " : " + "long " + mLong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGpsLocationLoaded(String gpsLocation) {
        Toast.makeText(this, "" + gpsLocation, Toast.LENGTH_SHORT).show();
        userGpsLocation = gpsLocation;
        checkIfUserCanGoHome();
    }

    private void checkIfUserCanGoHome() {
        int acceptedBranchIndex = -1;
        for (int i = 0; i < dataList.size(); i++) {
            String[] branch = dataList.get(i).getGpsLocation().split(",");
            String[] userGpsLocations = userGpsLocation.split(",");
            //check if the user is in the same country as the branch
            if (true) {
                //user is in the same city => we then check the distance
                stepOneApprove = true;
                Location branchLocation = new Location("providerNA");
                branchLocation.setLatitude(dataList.get(i).getmLat());
                branchLocation.setLongitude(dataList.get(i).getmLong());
                double acceptedRangeInMeters = dataList.get(i).getAcceptedOrdersRange() * 1000;
                if (userLocation.distanceTo(branchLocation) <= acceptedRangeInMeters) {
                   //user can go he is approved
                    //link the user with this branch
                    stepTwoApprove = true;
                    acceptedBranchIndex = i;
                    break;
                }
            }
        }
        //if user is in the same city with an accepted distance
        //we link him to some branch
        System.out.println("check distance = " + stepTwoApprove + " check city = " + stepOneApprove);
        if (stepOneApprove && stepTwoApprove) {
            if (acceptedBranchIndex != -1) {
                currentUser.setNearestBranch(dataList.get(acceptedBranchIndex).getId());
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userBranch" , 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userBranch" , dataList.get(acceptedBranchIndex).getId());
                editor.commit();
                currentUser.setyPos(userLocation.getLatitude());
                currentUser.setyPos(userLocation.getLongitude());
                currentUser.setGpsAddress(userGpsLocation);
                userUseCase.updateExistingUserAndDeleteExtraUser(currentUser);
            }
        } else {
            weNeedYourLocationTextView.setTextColor(Color.RED);
            weNeedYourLocationTextView.setText("sorry you are not allowed to use this app\"");
            System.out.println("sorry you are not allowed to use this app");
            progressBar.setVisibility(View.INVISIBLE);
            getLocationButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error " + message, Toast.LENGTH_SHORT).show();
        //checkIfUserCanGoHomeWithoutCityNameCheck();
    }


    @Override
    public void onUserAddedSuccessfully() {

    }

    @Override
    public void onUserFailedToAdd() {

    }

    @Override
    public void onInputError(String message) {

    }

    @Override
    public void onUserDataUpdated(User user) {
//        getLocationButton.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userBranch" , 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("gotUserLocation?" , true);
        edit.commit();
        finish();
    }

    @Override
    public void onBranchDataChange(List<Branch> dataList) {
        this.dataList = dataList;
    }


    @Override
    public void onBranchDataCancel(DatabaseError error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserDataGotSuccessfully(List<User> userList) {
        for(User user : userList){
            this.currentUser = user;
        }
    }

    @Override
    public void onUserDataCanceled(DatabaseError error) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationUseCase.registerListener(this);
        branchesUseCase.registerListener(this);
        userUseCase.registerListener(this);
        userInfoFromFirebaseUseCase.registerListener(this);
        branchesUseCase.getAllBranches();
        userInfoFromFirebaseUseCase.getUserOfID(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationUseCase.unregisterListener(this);
        branchesUseCase.unregisterListener(this);
        userUseCase.unregisterListener(this);
        userInfoFromFirebaseUseCase.unregisterListener(this);
    }


}
