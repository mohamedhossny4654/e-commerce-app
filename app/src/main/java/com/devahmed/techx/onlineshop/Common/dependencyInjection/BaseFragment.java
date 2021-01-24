package com.devahmed.techx.onlineshop.Common.dependencyInjection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    private ControllerCompositionRoot controllerCompostionRoot;

    private CompositionRoot compositionRoot;

    protected ControllerCompositionRoot getCompositionRoot(){
        compositionRoot = new CompositionRoot();
        if(controllerCompostionRoot == null){
            controllerCompostionRoot =
                    new ControllerCompositionRoot(compositionRoot, getActivity());
        }
        return controllerCompostionRoot;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return null;
    }
}
