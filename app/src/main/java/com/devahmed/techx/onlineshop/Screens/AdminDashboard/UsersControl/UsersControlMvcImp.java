package com.devahmed.techx.onlineshop.Screens.AdminDashboard.UsersControl;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class UsersControlMvcImp extends BaseObservableMvcView<UserControlsMvc.Listener> implements UserControlsMvc{

    RecyclerView usersRecyclerView;
    List<User> userList;
    UsersAdapter adapter;
    ProgressBar progressBar;
    Dialog dialog;

    public UsersControlMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.users_control_fragment , parent , false));
        initViews();
    }

    private void initViews() {
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        userList = new ArrayList<>();
        adapter = new UsersAdapter(userList);
        usersRecyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRecyclerView.setAdapter(adapter);
        usersRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), usersRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for(Listener listener : getmListeners()){
                    listener.onUserItemClicked(userList.get(position));

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public void bindData(List<User> userList) {
        this.userList = userList;
        adapter.setList(userList);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUserDialog(final User user) {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_user_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name_id);
        TextView dialog_phone = (TextView) dialog.findViewById(R.id.dialog_phone_id);
        ImageView dialog_photo = (ImageView) dialog.findViewById(R.id.dialog_photo_id);

        ImageButton userInfoBtn = dialog.findViewById(R.id.userInfoBtn);
        ImageButton userBlockBtn = dialog.findViewById(R.id.userBlockBtn);
        ImageButton userOrdersBtn = dialog.findViewById(R.id.userOrdersBtn);
        Button callBtn = dialog.findViewById(R.id.dialog_call_btn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    if(user != null)
                        listener.onCallBtnClicked(user);
                    dismissDialogIfShown();
                }
            }
        });
        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    if(user != null)
                        listener.onUserInfoBtnClicked(user);
                    dismissDialogIfShown();
                }
            }
        });
        userBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    if(user != null)
                        listener.onBlockBtnClicked(user);
                    dismissDialogIfShown();
                }
            }
        });
        userOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    if(user != null)
                        listener.onUserOrdersBtnClicked(user);
                    dismissDialogIfShown();
                }
            }
        });
        if(user.getName() == null){
            dialog_name.setText("Unknown");
        }else{
            dialog_name.setText(user.getName());
        }

        dialog_phone.setText(user.getPhone());
        dialog_photo.setImageResource(R.drawable.user_image_placeholder);

        dialog.show();
    }

    private void dismissDialogIfShown(){
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
