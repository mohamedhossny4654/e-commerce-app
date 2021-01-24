package com.devahmed.techx.onlineshop.Screens.LoginRegister.LoginRegisterUseCase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.firebase.auth.PhoneAuthProvider.*;

public class LoginRegisterUseCase extends BaseObservableMvcView<LoginRegisterUseCase.Listener> {
    private Activity activity;
    private String mVerificationId;
    private ForceResendingToken mResendToken;

    public interface Listener{
        void onVerificationCompleted();
        void onVerificationFailed(String message);
        void onCodeSent();
        void onCodeSentAndUserSuccessfullyRegister(FirebaseUser user);
        void onCodeSentAndUserFailedToRegister(String message);
    }

    public LoginRegisterUseCase(Activity activity) {
        this.activity = activity;
    }

    OnVerificationStateChangedCallbacks mCallbacks = new OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);
            Toast.makeText(activity, "Here3", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
            notifyOnVerificationCompleted();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                notifyOnVerificationFailed(e.getMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                notifyOnVerificationFailed(e.getMessage());
            }
            notifyOnVerificationFailed(e.getMessage());
            // Show a message and update the UI
            // ...

            Log.w(TAG, "onVerificationFailed " + e.getMessage(), e);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            notifyOnCodeSent();
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
            Toast.makeText(activity, "Here5", Toast.LENGTH_SHORT).show();
            // ...
        }
    };


    public void registerNewUser(String phoneNumber){

        if(phoneNumber == null
                || phoneNumber.trim().isEmpty()
                || phoneNumber.trim().equals("")
                || phoneNumber.length() == 0){
            notifyOnVerificationFailed("Please enter a valid phone number");
        }else{
            if( !phoneNumber.contains("+2")){
                //add +2 key to entered phone number if it doesn't exist
                phoneNumber = "+2" + phoneNumber;
            }
            Toast.makeText(activity, "Here1", Toast.LENGTH_SHORT).show();
            getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    activity,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            Toast.makeText(activity, "Here2", Toast.LENGTH_SHORT).show();
        }

    }


    public void signInWithCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            notifyOnCodeSentAndUserSuccessfullyRegister(user);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                notifyOnCodeSentAndUserFailedToRegister(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    public void notifyOnVerificationCompleted(){
        for(Listener listener : getmListeners()){
            listener.onVerificationCompleted();
        }
    }

    public void notifyOnVerificationFailed(String message){
        for(Listener listener : getmListeners()){
            listener.onVerificationFailed(message);
        }
    }

    public void notifyOnCodeSent(){
        for(Listener listener : getmListeners()){
            listener.onCodeSent();
        }
    }

    public void notifyOnCodeSentAndUserSuccessfullyRegister(FirebaseUser user){
        for(Listener listener : getmListeners()){
            listener.onCodeSentAndUserSuccessfullyRegister(user);
        }
    }

    public void notifyOnCodeSentAndUserFailedToRegister(String message){
        for(Listener listener : getmListeners()){
            listener.onCodeSentAndUserFailedToRegister(message);
        }
    }

}
