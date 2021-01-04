package com.tracking.smarttrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.ExportActivity;
import com.tracking.smarttrack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewEventFragment extends Fragment implements View.OnClickListener {


    View rootView;
    TextView actionBarTitleTV, truckNumberTV, yardAddressTV;
    RelativeLayout menuActionBar, searchActionBar, eventRefreshLay;
    ImageView menuImgBtn, importExportImgVw;
    Button btnDepartEvent;
    String EventId = "", Status = "", Address = "";
    String DriverId, CompanyId, TruckNo;
    RequestQueue getBackYardEventReq, updateYardEventReq;
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
            rootView = inflater.inflate(R.layout.fragment_new_events, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);


        return rootView;

    }

    void initView(View container) {

        API_URL             = new APIs(getActivity());
        getBackYardEventReq = Volley.newRequestQueue(getActivity());
        updateYardEventReq  = Volley.newRequestQueue(getActivity());

        actionBarTitleTV    = (TextView)container.findViewById(R.id.actionBarTitleTV);
        truckNumberTV       = (TextView)container.findViewById(R.id.truckNumberTV);
        yardAddressTV       = (TextView)container.findViewById(R.id.yardAddressTV);

        searchActionBar     = (RelativeLayout)container.findViewById(R.id.searchActionBar);
        eventRefreshLay     = (RelativeLayout)container.findViewById(R.id.notificationLay);
        menuActionBar       = (RelativeLayout)container.findViewById(R.id.menuActionBar);

        importExportImgVw   = (ImageView)container.findViewById(R.id.importExportImgVw);
        menuImgBtn          = (ImageView)container.findViewById(R.id.menuImgBtn);
        btnDepartEvent      = (Button)container.findViewById(R.id.btnDepartEvent);

        menuImgBtn.setImageResource(R.drawable.ic_arrow_backbb);
        importExportImgVw.setImageResource(R.drawable.refresh);
        actionBarTitleTV.setText(getString(R.string.back_to_yard));
        searchActionBar.setVisibility(View.GONE);


        Constants.GET_BUNDLE    = this.getArguments();
        EventId                 = Constants.GET_BUNDLE.getString("EventId");
        Status                  = Constants.GET_BUNDLE.getString("Status");
        Address                 = Constants.GET_BUNDLE.getString("Address");

        TruckNo                 = Constants.getTruckNumber(getActivity());
        DriverId                = Constants.getDriverId( Constants.KEY_DRIVER_ID , getActivity());
        CompanyId               = Constants.getCompanyId(Constants.KEY_COMPANY, getActivity());

        truckNumberTV.setText(TruckNo);
        yardAddressTV.setText(Html.fromHtml(Address));

        if(Status.equals("1")){
            btnDepartEvent.setText(getString(R.string.Depart));
        }else {
            btnDepartEvent.setText(getString(R.string.arrived));
        }

        eventRefreshLay.setOnClickListener(this);
        menuActionBar.setOnClickListener(this);
        btnDepartEvent.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.menuActionBar:
                getFragmentManager().popBackStack();
                break;

            case R.id.notificationLay:

                if(Constants.isNetworkAvailable(getContext()) ) {
                    GetBackToYardEvent();
                }else{
                    Constants.showToastMsg(yardAddressTV, "Please check your internet connection", Constants.VIEW_DURATION);
                }

                break;

            case R.id.btnDepartEvent:

                if(Constants.isNetworkAvailable(getContext()) ) {
                    UpdteBackToYardEvent();
                }else{
                    Constants.showToastMsg(yardAddressTV, "Please check your internet connection", Constants.VIEW_DURATION);
                }
                break;

        }
    }


    void GetBackToYardEvent(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_BACK_TO_YARD_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response: " + response);

                        JSONObject obj;
                        int status ;
                        try {
                            obj = new JSONObject(response);
                            status = obj.getInt("Status");

                            if(status == 1) {
                                Constants.showToastMsg(yardAddressTV, "Event refreshed successfully", Constants.VIEW_DURATION);

                                JSONObject EventObj = new JSONObject(obj.getString("Events"));

                                EventId                 = EventObj.getString("DriverEventId");
                                Status                  = EventObj.getString("EventStatus");
                                Address                 = EventObj.getString("LocationName") + "<br>" +
                                                          EventObj.getString("LocationAddress");

                                yardAddressTV.setText(Html.fromHtml(Address));

                            }else{
                                Constants.showToastMsg(yardAddressTV, "Failed", Constants.VIEW_DURATION);

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
                        Log.d("error", ">>error: " +error);
                        Constants.showToastMsg(yardAddressTV, "Failed", Constants.VIEW_DURATION);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", DriverId);
                params.put("EventId", EventId);
                params.put("CompanyId", CompanyId);
              //  params.put("Latitude", Constants.CURRENT_LATITUDE);
              //  params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getBackYardEventReq.add(postRequest);

    }



    void UpdteBackToYardEvent(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPDATE_BACK_TO_YARD_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response: " + response);

                        JSONObject obj;
                        int status ;
                        try {
                            obj = new JSONObject(response);
                            status = obj.getInt("Status");

                            if(status == 1) {

                                if(Status.equals("1")) {
                                    Constants.showToastMsg(yardAddressTV, "Departed successfully", Constants.VIEW_DURATION);
                                }else{
                                    Constants.showToastMsg(yardAddressTV, "Arrived successfully", Constants.VIEW_DURATION);
                                }

                                JSONObject EventObj = new JSONObject(obj.getString("Events"));

                                EventId                 = EventObj.getString("DriverEventId");
                                Status                  = EventObj.getString("EventStatus");
                                Address                 = EventObj.getString("LocationName") + "<br>" +
                                                            EventObj.getString("LocationAddress");
                                yardAddressTV.setText(Html.fromHtml(Address));

                                getFragmentManager().popBackStack();


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
                        Log.d("error", ">>error: " +error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", DriverId);
                params.put("EventId", EventId);
                params.put("Status", Status);
                params.put("CompanyId", CompanyId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getBackYardEventReq.add(postRequest);

    }


}
