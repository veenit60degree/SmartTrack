package com.tracking.smarttrack.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class OTPFragment extends Fragment implements View.OnClickListener{


    View rootView;
    EditText OtpEditText;
    TextView emailIdTV;
    Button btnSendOtp,btnBackLogin;
    private int VIEW_DURATION = 8000;
    ProgressBar progressBarOtp;
    String driver_Id = "",otp_No = "",StrEmailId="",lastEmailChar = "",firstFourChars="";
    String DriverId = "", CompanyId = "", LoginID = "";
    RequestQueue otpReq;
    JSONObject resultJson;
    VehicleDialog vehicleDialog;
    List<VehicleModel> vehicleList = new ArrayList<>();
    APIs API_URL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_otp, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);


        return rootView;

    }

    void initView(View container) {

        API_URL             = new APIs(getActivity());
        otpReq              = Volley.newRequestQueue(getActivity());
        emailIdTV           = (TextView) container.findViewById(R.id.emailId);
        OtpEditText         = (EditText) container.findViewById(R.id.editOTp);
        btnSendOtp          = (Button) container.findViewById(R.id.btnSendOtp);
        btnBackLogin        = (Button) container.findViewById(R.id.btnBackLogin);
        progressBarOtp      = (ProgressBar) container.findViewById(R.id.progressBarOTP);

        btnSendOtp.setOnClickListener(this);
        btnBackLogin.setOnClickListener(this);

        StrEmailId          = Constants.getDriverDetails(Constants.KEY_EMAIL, getActivity());

        if (StrEmailId.length() > 10)
        {
            lastEmailChar = StrEmailId.substring(StrEmailId.length() - 15);
            firstFourChars = StrEmailId.substring(0, 2);
        }
        else
        {
            lastEmailChar = StrEmailId ;
        }

        emailIdTV.setText(firstFourChars +"***" + lastEmailChar);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.hideKeyboardLay:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.btnSendOtp:
                Constants.hideSoftKeyboard(getActivity());
                Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(getActivity());
                if (Constants.INTERNET_CONNECTIVITY_STATUS) {
                    driver_Id = Constants.getDriverId( Constants.KEY_DRIVER_ID , getActivity());
                    otp_No = OtpEditText.getText().toString().trim();

                    if(otp_No.trim().length() > 0) {
                        OTPRequest(driver_Id, otp_No);
                    }else {
                        Constants.showToastMsg(btnSendOtp, "Enter OTP first", VIEW_DURATION);
                    }
                } else {
                    Constants.showToastMsg(btnSendOtp, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }

                break;

            case R.id.btnBackLogin:

               LoginActivity.otp_fragment.setVisibility(View.GONE);
               getFragmentManager().popBackStack();
                /* Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().getSupportFragmentManager().popBackStackImmediate();*/
        }
    }


    void OTPRequest(final String driver_Id,final String otp_No){

        progressBarOtp.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.Validate_OTP,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarOtp.setVisibility(View.GONE);
                        JSONObject obj;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);
                            Constants.setTruckArray("[]", getActivity());

                            if(obj.getInt("Status") == 0){
                                Constants.showToastMsg(btnSendOtp, "Please enter your OTP correctly !", VIEW_DURATION);
                            }else if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){

                                    resultJson = new JSONObject(obj.getString("Result"));

                                    try{

                                        DriverId    = resultJson.getString("DriverId");
                                        CompanyId   = resultJson.getString("CompanyId");
                                        LoginID     = resultJson.getString("LoginID");

                                        String truckList   = obj.getString("lstResult");
                                        Constants.setTruckArray(truckList, getActivity());

                                        moveToHomeScreen(resultJson);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                               /*     Constants.setDriverId( Constants.KEY_DRIVER_ID , resultJson.getString("DriverId") , getActivity());

                                    Constants.setDriverDetails(
                                            Constants.KEY_GENDER, resultJson.getString("strGender"),
                                            Constants.KEY_STATE, resultJson.getString("strState"),
                                            Constants.KEY_CARRIERNAME, resultJson.getString("CarrierName"),
                                            Constants.KEY_COMPANYNAME, resultJson.getString("CompanyName"),
                                            Constants.KEY_DOB, resultJson.getString("DOB"),
                                            Constants.KEY_DRIVER_IMAGE, Constants.IMAGE_BASE_URL + resultJson.getString("DriverImagePath"),
                                            Constants.KEY_EMAIL, resultJson.getString("EmailAddress"),
                                            Constants.KEY_EMP_ID, resultJson.getString("EmpId"),
                                            Constants.KEY_FAST_ID, resultJson.getString("FastId"),
                                            Constants.KEY_FNAME, resultJson.getString("FName"),
                                            Constants.KEY_LNAME, resultJson.getString("LName"),
                                            Constants.KEY_MNAME, resultJson.getString("MName"),
                                            Constants.KEY_MOBILE, resultJson.getString("Mobile"),
                                            Constants.KEY_COMPANY, resultJson.getString("CompanyId"),
                                            Constants.KEY_LOGIN, resultJson.getString("LoginID"),
                                            getActivity() );
                                    Log.d("setDriverDetails", "setDriverDetails: ");
                                    Constants.showToastMsg(btnSendOtp, "OTP Granted Successfully.", VIEW_DURATION);

                                    Intent i = new Intent(getActivity(), TabActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                   // finish();
                                    getActivity().getSupportFragmentManager().popBackStackImmediate();
*/
                                }

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
                        progressBarOtp.setVisibility(View.GONE);
                        Constants.showToastMsg(btnSendOtp, "Please check your internet connection", VIEW_DURATION);
                        // response
                        Log.d("error", ">>error: " +error);

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", driver_Id);
                params.put("OTP", otp_No);

                Log.d("params", "params: " + params);
                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        otpReq.add(postRequest);

    }



    void moveToHomeScreen(JSONObject resultJson){

        try{

            Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_LONG).show();
            Constants.saveDriverLoginDetails(resultJson, getActivity());

            Intent i = new Intent(getActivity(), TabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().finish();

        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
