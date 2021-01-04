package com.tracking.smarttrack.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.smarttrack.ContainerJobAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.models.ContainerModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFeedFragment extends Fragment {

    View rootView;
    TextView actionBarTitleTV, noTripTextView;
    ListView newFeedListView;
    RelativeLayout notificationLay, searchActionBar;
    FloatingActionButton selfAssignflotbtn;
    public static RelativeLayout menuActionBar;
    ImageView menuImgBtn;
    ContainerJobAdapter newJobAdapter;
    ProgressBar progressBarHome;
    String DriverId = "", DeviceId = "", LoginId = "", CompanyId = "";
    public static List<ContainerModel> newFeedList = new ArrayList<>();
    RequestQueue getNewJobReq;
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
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);


        return rootView;

    }

    @SuppressLint("RestrictedApi")
    void initView(View container) {

        API_URL             = new APIs(getActivity());
        getNewJobReq        = Volley.newRequestQueue(getActivity());

        newFeedListView     = (ListView)container.findViewById(R.id.unasignListView);

        actionBarTitleTV    = (TextView)container.findViewById(R.id.actionBarTitleTV);
        noTripTextView      = (TextView)container.findViewById(R.id.noTripTextView);

        menuImgBtn          = (ImageView)container.findViewById(R.id.menuImgBtn);

        notificationLay     = (RelativeLayout)container.findViewById(R.id.notificationLay);
        searchActionBar     = (RelativeLayout)container.findViewById(R.id.searchActionBar);
        menuActionBar       = (RelativeLayout)container.findViewById(R.id.menuActionBar);

        progressBarHome     = (ProgressBar)container.findViewById(R.id.progressBarHome);
        selfAssignflotbtn   = (FloatingActionButton) container.findViewById(R.id.selfAssignflotbtn);

        notificationLay.setVisibility(View.GONE);
        searchActionBar.setVisibility(View.GONE);
        selfAssignflotbtn.setVisibility(View.GONE);

        actionBarTitleTV.setText("New Jobs");
        menuImgBtn.setImageResource(R.drawable.ic_arrow_backbb);


        DeviceId    = LoginActivity.deviceID.trim();
        DriverId    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyId   = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());


        if(Constants.isNetworkAvailable(getActivity())){
            GET_NEW_JOBS_LIST(DriverId);
        }else{
            Constants.showToastMsg(actionBarTitleTV, "Please check your internet connection", 5000);
        }


        menuActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }



      /*---------- Get new Feed LIST ------------ */
    void GET_NEW_JOBS_LIST(final String DriverId){

        progressBarHome.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_ASSIGNED_JOBS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj, resultJson;
                        JSONArray resultJsonArray;

                        Log.d("Response ", ">>>Response: " + response);
                        progressBarHome.setVisibility(View.GONE);
                        newFeedList = new ArrayList<ContainerModel>();
                        ContainerModel containerModel = null;
                        
                        try {
                            obj = new JSONObject(response);

                            if(obj.getBoolean("Success") == true){

                                resultJsonArray = new JSONArray(obj.getString("lstResult"));

                                for (int i = 0 ; i < resultJsonArray.length() ; i++){
                                    resultJson = (JSONObject)resultJsonArray.get(i);
                                    int status = 0;
                                    if(resultJson.getString("Status") != null &&
                                            !resultJson.getString("Status").equalsIgnoreCase("null") ){
                                        status = resultJson.getInt("Status");
                                    }

                                    /*---------------- PARSE JSON of Container Details Array-----------------*/
                                    try {
                                        containerModel = Constants.containerModel(containerModel, resultJson, status, false, false);
                                        newFeedList.add(containerModel);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            }

                            notifyAssignedJobAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // response
                        Log.d("error", ">>error: " +error);
                        Constants.showToastMsg(actionBarTitleTV, "Please check your internet connection", 5000);
                        progressBarHome.setVisibility(View.GONE);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<String, String>();
                params.put("DriverId", DriverId);
                params.put("DeviceID", DeviceId);
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyId);

                return params;
            }
        };

        int socketTimeout = 20000;   //20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getNewJobReq.add(postRequest);

    }


       /* ---------- NOTIFY ASSIGNED ADAPTER ------------*/
    void notifyAssignedJobAdapter() {
        try {
            newJobAdapter = new ContainerJobAdapter(getActivity(), newFeedList, Constants.ASSIGNED);
            newFeedListView.setAdapter(newJobAdapter);
            noTripTextView.setVisibility(View.GONE);

        } catch (Exception execption) {
            execption.printStackTrace();
            noTripTextView.setVisibility(View.VISIBLE);
        }
    }
}