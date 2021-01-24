package com.devahmed.techx.onlineshop.Common.dependencyInjection;

import android.app.Activity;
import android.view.LayoutInflater;

import com.google.firebase.database.FirebaseDatabase;

public class ControllerCompositionRoot {

    private CompositionRoot compositionRoot;
    private Activity activity;

    public ControllerCompositionRoot(CompositionRoot compositionRoot, Activity activity) {
        this.compositionRoot = compositionRoot;
        this.activity = activity;
    }


    private LayoutInflater getLayoutInfalter() {
        return LayoutInflater.from(activity);
    }

    public MvcViewFactory getMvcFactory() {
        return new MvcViewFactory(getLayoutInfalter());
    }

    public FirebaseDatabase ConnectToFirebase(){
        return compositionRoot.ConnectToFirebase();
    }

}
