package com.devahmed.techx.onlineshop.Screens.AdminDashboard.BranchesControl;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.Branch;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Home.categoryList.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BranchesMvcImp extends BaseObservableMvcView<BranchesMvc.Listener> implements BranchesMvc {

    private RecyclerView recyclerView;
    private List<Branch> branchesList;
    private BranchesAdapter adapter;
    private FloatingActionButton addBranchBtn;
    private Dialog addBranchDialog;
    private ProgressBar progressBar;
    private LinearLayout locationContainer;
    private TextView gpsLocationText , coordinatesText , rangeText;
    private Button createBranchBtn;
    private Button getLocationBtn;
    private SeekBar rangeSeekBar;
    public BranchesMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.branches_list_fragment , parent , false));

        initViews();

        addBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onAddBranchBtnClicked();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for(Listener listener : getmListeners()){
                    listener.onBranchItemClicked(branchesList.get(position));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerBranches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        branchesList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        adapter = new BranchesAdapter(getContext() , branchesList);
        recyclerView.setAdapter(adapter);
        addBranchBtn = findViewById(R.id.addBranchBtn);
    }

    @Override
    public void bindData(List<Branch> branches) {
        this.branchesList = branches;
        adapter.setList(branchesList);
    }

    @Override
    public void showAddBranchDialog() {
        addBranchDialog = new Dialog(getContext() );
        addBranchDialog.setContentView(R.layout.dialog_add_branch);
        addBranchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        addBranchDialog.setCancelable(true);

        final EditText editText = addBranchDialog.findViewById(R.id.branchName);
        getLocationBtn = addBranchDialog.findViewById(R.id.getBranchLocationBtn);
        createBranchBtn = addBranchDialog.findViewById(R.id.createBranchBtn);
        locationContainer = addBranchDialog.findViewById(R.id.container);
        gpsLocationText = addBranchDialog.findViewById(R.id.gpsLocationText);
        coordinatesText = addBranchDialog.findViewById(R.id.CoordinatesText);
        rangeSeekBar = addBranchDialog.findViewById(R.id.rangbarInKm);
        rangeText = addBranchDialog.findViewById(R.id.rangeText);
        rangeSeekBar.setProgress(5);
        rangeSeekBar.setMax(20);
        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for(Listener listener : getmListeners()){
                    rangeText.setText("" + progress + "\t-Km");
                    listener.onRangeProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onGetLocationBtnClicked(editText.getText().toString().trim());
                }
            }
        });
        createBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onCreateBranchBtnClicked();
                    addBranchDialog.dismiss();
                }
            }
        });
        addBranchDialog.show();
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
    public void showLocationContainer(Branch branch) {
        getLocationBtn.setVisibility(View.GONE);
        locationContainer.setVisibility(View.VISIBLE);
        gpsLocationText.setText(branch.getGpsLocation());
        coordinatesText.setText(branch.getmLat() + " : " + branch.getmLong());
    }
}
