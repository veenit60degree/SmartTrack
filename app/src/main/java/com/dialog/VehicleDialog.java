package com.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.adapter.smarttrack.SpinnerCustomAdapter;
import com.models.VehicleModel;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.R;

import java.util.ArrayList;
import java.util.List;

public class VehicleDialog extends Dialog {


    public interface VehicleListener {
        public void ChangeVehicleReady(int position);
    }

    public interface LogoutListener {
        public void LogoutReady();
    }

    List<VehicleModel> truckList;
    private VehicleListener readyListener;
    private LogoutListener logoutListener;
    Button btnSaveTruck, btnLogout;
    Spinner truckSpinner;
    int SelectedPosition = 0;
    boolean isTruckUpdation;
    SpinnerCustomAdapter spinnerCustomAdapter;
    TextView TitleTV;
    ArrayList<String> EquipmentList = new ArrayList<String>();

    public VehicleDialog(Context context, List<VehicleModel> remarkList, boolean isUpdate, VehicleListener readyListener,
                         LogoutListener logoutListener) {
        super(context);
        this.truckList = remarkList;
        this.isTruckUpdation = isUpdate;
        this.readyListener = readyListener;
        this.logoutListener = logoutListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setBackgroundDrawable();//new ColorDrawable(android.graphics.Color.TRANSPARENT)
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.black_transparent_fifty)));
        setContentView(R.layout.dialog_truck_listing);
         setCancelable(false);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        btnSaveTruck = (Button) findViewById(R.id.btnSaveTruck);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        TitleTV = (TextView)findViewById(R.id.TitleTV);

        truckSpinner = (Spinner) findViewById(R.id.truckSpinner);

        if (truckList.size() > 0) {
            EquipmentList = new ArrayList<String>();
            for(int i = 0 ; i < truckList.size() ; i++ ) {
                EquipmentList.add(truckList.get(i).getEquipmentNumber());
            }

            // Creating adapter for spinner
            if(EquipmentList.size() > 0) {

                spinnerCustomAdapter = new SpinnerCustomAdapter(getContext(), EquipmentList);
                truckSpinner.setAdapter(spinnerCustomAdapter);

            }
        }


        // Spinner click listener
        truckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // String item = parent.getItemAtPosition(position).toString();
                SelectedPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(isTruckUpdation){
            btnLogout.setText(getContext().getResources().getString(R.string.cancel));
            btnSaveTruck.setText(getContext().getResources().getString(R.string.change));
            TitleTV.setText(getContext().getResources().getString(R.string.change_truck));

            String truck = Constants.getTruckNumber(getContext());

            for(int i = 0 ; i < EquipmentList.size() ; i++ ) {
                if(truck.equals(EquipmentList.get(i)) ){
                    truckSpinner.setSelection(i);
                    break;
                }
            }
        }


        btnSaveTruck.setOnClickListener(new VehicleFieldListener());
        btnLogout.setOnClickListener(new CancelBtnListener());

    }



    private class VehicleFieldListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(SelectedPosition > 0) {
                readyListener.ChangeVehicleReady(SelectedPosition);
            }else{
                Constants.showToastMsg(btnSaveTruck, "Please select truck first", Constants.VIEW_DURATION);

            }
        }
    }


    private class CancelBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            logoutListener.LogoutReady();

        }
    }

}
