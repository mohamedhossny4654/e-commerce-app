package com.devahmed.techx.onlineshop.Screens.AdminDashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.devahmed.techx.onlineshop.R;

public class AdminFragment extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment , container , false);

        CardView users = view.findViewById(R.id.users);
        CardView branches = view.findViewById(R.id.branches);
        CardView orders = view.findViewById(R.id.orders);
        CardView bills = view.findViewById(R.id.Bills);
        CardView notifications = view.findViewById(R.id.Notfications);
        CardView deliveryCost = view.findViewById(R.id.DeliveryCost);
        CardView minCharge = view.findViewById(R.id.minCharge);
        CardView howFragment = view.findViewById(R.id.howFindUs);
        CardView ONoFF = view.findViewById(R.id.onOff);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.usersControlFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.usersControlFragment);
            }
        });

        branches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.branchesControlFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.branchesControlFragment);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.ordersControlFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.ordersControlFragment);
            }
        });

        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.billsFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.billsFragment);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.pushNotificationsFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.pushNotificationsFragment);
            }
        });

        deliveryCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.deliverCostFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.deliverCostFragment);
            }
        });

        minCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.minChargeFragment );
//                Navigator.instance(requireActivity()).navigate(R.id.minChargeFragment);
            }
        });
        howFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.howFragment );

            }
        });
        ONoFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity() , R.id.nav_host_fragment).navigate(R.id.onOffFragment );

            }
        });
        return view;
    }
}
