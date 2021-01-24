package com.devahmed.techx.onlineshop.Screens.LoginRegister;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivityMvcImp extends BaseObservableMvcView<LoginActivityMvc.Listener> implements LoginActivityMvc {
    TextView timerTextView , resendTextView , welcomeTo , storeName , signUpToContinue ,enterTheCodeText , weSentCodeText , didntReceiveCodeText;
    ConstraintLayout loginLayout , codeEnterLayout;
    TabLayout tableLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton submitBtn , enterCodeBtn;
    EditText codeEditText;
    ProgressBar progressBar;
    SignUpFragment signUpFragment;
    int timeToResendCode = 60;//IN SECONDS
    Button skipBtn;
    boolean canResend = false;

    public LoginActivityMvcImp(LayoutInflater inflater , ViewGroup parent , FragmentManager fragmentManager) {
        setRootView(inflater.inflate(R.layout.login_register_activity, parent , false));
        init(fragmentManager);

        enterTheCodeText.setText(getContext().getResources().getString(R.string.enter_the_code));
        weSentCodeText.setText(getContext().getResources().getString(R.string.we_sent_code));
        didntReceiveCodeText.setText(getContext().getResources().getString(R.string.didnt_recieve_code));
        codeEditText.setHint(getContext().getResources().getString(R.string.code));
        resendTextView.setText(getContext().getResources().getString(R.string.resend_code));

        enterCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onEnterCodeBtnClicked(codeEditText.getText().toString());
                }
            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSkipBtnClicked();
                }
            }
        });
        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canResend){
                    for(Listener listener : getmListeners()){
                        listener.onResendCodeBtnClicked();
                    }
                }else{
                    Toast.makeText(getContext(), "wait " + timerTextView.getText() + " seconds", Toast.LENGTH_SHORT).show();
                }

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSubmitBtnCLicked();
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(Listener listener : getmListeners()){
                    listener.onViewPagerPageChanged(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        welcomeTo.setText(getContext().getResources().getString(R.string.welcome_to));
        storeName.setText(getContext().getResources().getString(R.string.app_name));
        signUpToContinue.setText(getContext().getResources().getString(R.string.signup_to_continue));

    }

    private void init(FragmentManager fragmentManager){
        enterTheCodeText = findViewById(R.id.enterTheCodeTextView);
        weSentCodeText = findViewById(R.id.weSentCodeTextView);
        didntReceiveCodeText = findViewById(R.id.didntRecieveText);
        progressBar = findViewById(R.id.login_submit_progressbar);
        loginLayout = findViewById(R.id.loginLayout);
        codeEnterLayout = findViewById(R.id.codeEnterLayout);
        welcomeTo = findViewById(R.id.welcomeTo);
        storeName = findViewById(R.id.storeName);
        signUpToContinue = findViewById(R.id.signUpToContinue);
        skipBtn = findViewById(R.id.skipBtn);
        codeEditText = findViewById(R.id.CodeText);
        timerTextView = findViewById(R.id.timerTextView);
        enterCodeBtn = findViewById(R.id.EnterCodeBtn);
        resendTextView = findViewById(R.id.resendTextView);
        signUpFragment = new SignUpFragment();
        signUpFragment.setContext(getContext());
        tableLayout =  findViewById(R.id.tablLayout);
        viewPager = findViewById(R.id.viewPager);
        submitBtn = findViewById(R.id.login_submitBtn);
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPagerAdapter.AddFragment(signUpFragment , getContext().getResources().getString(R.string.enter_your_phone));
        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        //Add fragment here
        tableLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showProgressbar() {
        submitBtn.hide();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        submitBtn.show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public String getRegisterPhoneNumber() {
        String phone = SignUpFragment.getUserPhone();
        return phone;
    }

    @Override
    public void changeViewPagerPageTo(int page) {
        viewPager.setCurrentItem(page);
    }

    @Override
    public void hideLoginPage() {
        loginLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoginPage() {
        loginLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCodeEnterencePage() {
        codeEnterLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCodeEnterencePage() {
        codeEnterLayout.setVisibility(View.GONE);
    }

    public void startTimer(final Activity activity) {

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        timerTextView.setText(String.format(Locale.getDefault(), "%d", timeToResendCode));
                        if (timeToResendCode > 0)
                            timeToResendCode -= 1;
                        else {
                            canResend = true;
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
}
