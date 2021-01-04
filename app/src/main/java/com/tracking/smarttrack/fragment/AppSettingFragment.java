package com.tracking.smarttrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dialog.VehicleDialog;
import com.models.VehicleModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.constants.themeUtils;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppSettingFragment extends Fragment {

    View rootView;
    private Switch CapsOnOff;

    APIs API_URL;
    TextView contInfoTitleTV, truckNoTxtView;
    RelativeLayout menuActionBar,notificationLay,searchActionBar, truckChangeLay;
    VehicleDialog vehicleDialog;
    List<VehicleModel> vehicleList = new ArrayList<>();
    String TruckNumber = "", TruckId = "";
    String DeviceID = "", DriverID = "", LoginId = "", CompanyID = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.activity_app_settings, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);


        return rootView;

    }


    void initView(View container) {

        API_URL              = new APIs(getActivity());
        CapsOnOff            = (Switch) container.findViewById(R.id.CapsOnswitch);
        menuActionBar        = (RelativeLayout) container.findViewById(R.id.menuActionBar);
        notificationLay      = (RelativeLayout)container.findViewById(R.id.notificationLay);
        truckChangeLay       = (RelativeLayout)container.findViewById(R.id.truckChangeLay);
        searchActionBar      = (RelativeLayout)container.findViewById(R.id.searchActionBar);
        contInfoTitleTV      = (TextView) container.findViewById(R.id.actionBarTitleTV);
        truckNoTxtView       = (TextView) container.findViewById(R.id.truckNoTxtView);

        if(Constants.isCapsTheme(getActivity())){
            CapsOnOff.setChecked(true);
        }


        CapsOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isPressed()) {
                    Constants.setCapsTheme(isChecked, true, getActivity());
                    if (isChecked) {
                        themeUtils.changeToTheme(getActivity(), themeUtils.ON);
                    } else {
                        themeUtils.changeToTheme(getActivity(), themeUtils.OFF);
                    }
                }
            }
        });

        DeviceID    = LoginActivity.deviceID.trim();
        DriverID    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyID   = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());
        TruckNumber = Constants.getTruckNumber(getActivity());

        truckNoTxtView.setText(TruckNumber);
        contInfoTitleTV.setText("Settings");
        searchActionBar.setVisibility(View.GONE);
        notificationLay.setVisibility(View.GONE);

        menuActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.sliderLay.performClick();
            }
        });

        truckChangeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruckList();
            }
        });
    }



    void showTruckList(){

            vehicleList = new ArrayList<>();

            try {
                String truckArray = Constants.getTruckArray(getActivity());
                JSONArray vehicleJsonArray = new JSONArray(truckArray);

                // Add Spinner title
                VehicleModel vehicleModel = new VehicleModel("", "Select", "", "", "", "", "");
                vehicleList.add(vehicleModel);

                for (int i = 0; i < vehicleJsonArray.length(); i++) {
                    JSONObject resultJson = (JSONObject) vehicleJsonArray.get(i);
                    vehicleModel = new VehicleModel(
                            resultJson.getString("TruckId"),
                            resultJson.getString("EquipmentNumber"),
                            resultJson.getString("PlateNumber"),
                            resultJson.getString("VIN"),
                            resultJson.getString("YearModel"),
                            resultJson.getString("CarrierId"),
                            resultJson.getString("CompanyId")
                    );
                    vehicleList.add(vehicleModel);
                }


                if (vehicleList.size() > 0) {

                    if (vehicleDialog != null && vehicleDialog.isShowing()) {
                        vehicleDialog.dismiss();
                    }

                    vehicleDialog = new VehicleDialog(getActivity(), vehicleList, true, new VehicleListener(), new LogoutListener() );
                    vehicleDialog.show();

                } else {
                    Constants.showToastMsg(truckChangeLay, "No vehicles are available", Constants.VIEW_DURATION);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }



    private class VehicleListener implements  VehicleDialog.VehicleListener {

        @Override
        public void ChangeVehicleReady(int position) {

            if (Constants.isNetworkAvailable(getActivity())) {

                try {
                    if (vehicleDialog != null && vehicleDialog.isShowing())
                        vehicleDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                TruckNumber = vehicleList.get(position).getEquipmentNumber();
                TruckId = vehicleList.get(position).getVehicleId();

                SaveDefaultTruck( DriverID, TruckId, CompanyID, LoginId );

            } else {
                Constants.showToastMsg(truckChangeLay, "Please check your internet connection", Constants.VIEW_DURATION);
            }

        }

    }


    void SaveDefaultTruck(final String Driverid, final String truckid, final String companyid, final String LoginID){


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.SET_DEFAULT_TRUCK,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1 && obj.getBoolean("Success")){
                                truckNoTxtView.setText(TruckNumber);
                                Constants.setTruckDetails(TruckNumber, TruckId, getActivity());
                                Constants.showToastMsg(truckChangeLay, "Changed successfully", Constants.VIEW_DURATION);
                            }else{
                                Constants.showToastMsg(truckChangeLay, obj.getString("Success"), Constants.VIEW_DURATION);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.showToastMsg(truckChangeLay, "Please check your internet connection", Constants.VIEW_DURATION);
                        // response
                        Log.d("error", ">>error: " +error);

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("Driverid", Driverid);
                params.put("truckid", truckid);
                params.put("companyid", companyid);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime50, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private class LogoutListener implements  VehicleDialog.LogoutListener {

        @Override
        public void LogoutReady() {

                try {
                    if (vehicleDialog != null && vehicleDialog.isShowing())
                        vehicleDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final Exception e) {
                    e.printStackTrace();
                }



        }
    }

}
