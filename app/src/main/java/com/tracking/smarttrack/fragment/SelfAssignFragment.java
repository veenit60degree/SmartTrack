package com.tracking.smarttrack.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.tracking.smarttrack.ExportActivity;
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

public class SelfAssignFragment  extends Fragment {


    View rootView;
    TextView actionBarTitleTV, noTripTextView;
    ListView unasignListView;
    public static Button hiddenBtn;
    FloatingActionButton selfAssignflotbtn;
    RelativeLayout notificationLay, searchActionBar, menuActionBar;
    List<ContainerModel> containerUnAssignList = new ArrayList<ContainerModel>();
    ContainerJobAdapter unasignAdapter;
    ProgressBar progressBarHome;
    ContainerModel containerModel;
    String DriverId = "", DeviceId = "", LoginId = "", CompanyId = "",jobType = "",container_No = "", PickUpLocationId = "";
    RequestQueue getUnAssignConReq;
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
        getUnAssignConReq   = Volley.newRequestQueue(getActivity());

        unasignListView     = (ListView)container.findViewById(R.id.unasignListView);

        noTripTextView      = (TextView) container.findViewById(R.id.noTripTextView);
        actionBarTitleTV    = (TextView) container.findViewById(R.id.actionBarTitleTV);

        progressBarHome     = (ProgressBar)container.findViewById(R.id.progressBarHome);
        hiddenBtn           = (Button)container.findViewById(R.id.hiddenBtn);

        notificationLay     = (RelativeLayout)container.findViewById(R.id.notificationLay);
        searchActionBar     = (RelativeLayout)container.findViewById(R.id.searchActionBar);
        menuActionBar       = (RelativeLayout)container.findViewById(R.id.menuActionBar);
        selfAssignflotbtn   = (FloatingActionButton) container.findViewById(R.id.selfAssignflotbtn);

        DriverId     = Constants.getDriverId( Constants.KEY_DRIVER_ID , getActivity());
        DeviceId    = LoginActivity.deviceID.trim();
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyId      = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());


        notificationLay.setVisibility(View.GONE);
        searchActionBar.setVisibility(View.GONE);
        selfAssignflotbtn.setVisibility(View.GONE);

        actionBarTitleTV.setText("Self Assign");
        Constants.hideSoftKeyboard(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            container_No = bundle.getString("contNo");
            jobType = bundle.getString("jobType");

            GET_UNASSIGN_CONTAINER(CompanyId, jobType, container_No);


        }


        unasignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String OrderTypeU = String.valueOf(containerUnAssignList.get(position).getOrderType());
                String LegType = containerUnAssignList.get(position).getLegType();
                String LoadingLocationId = containerUnAssignList.get(position).getLoadingLocationId();
                String contId = String.valueOf(containerUnAssignList.get(position).getContainerId() );

                if(LegType.equals("1") || LegType.equals("3")){
                    if( PickUpLocationId.equals("null") ){
                        Constants.showToastMsg( noTripTextView,"Sorry ! this Container don't have Pickup Location.", Snackbar.LENGTH_LONG);
                    }else {
                        moveToSelfAsignFragment(OrderTypeU,container_No, contId, position);
                    }
                }else{
                    if(LoadingLocationId.equalsIgnoreCase("null")){
                        Constants.showToastMsg( noTripTextView,"Sorry ! this Container don't have Pickup Location.", Snackbar.LENGTH_LONG);
                    }else {
                        moveToSelfAsignFragment(OrderTypeU,container_No, contId, position);
                    }
                }

            }
        });


        hiddenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.host.setCurrentTab(0);
            }
        });

        menuActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.sliderLay.performClick();
            }
        });
    }




    /* ---------- MOVE TO Self Assign FRAGMENT ------------ */
    void moveToSelfAsignFragment(final String jobType, final String contNo , final String contId, final int itemPosition ){

        try{
            progressBarHome.setVisibility(View.VISIBLE);

            Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(getContext());
            if (Constants.INTERNET_CONNECTIVITY_STATUS) {

                FragmentManager fragManager;
                SelfAssignDetailFragment selfAssignDetailFragment = new SelfAssignDetailFragment();

                Constants.BUNDLE.putString("jobType", jobType);
                Constants.BUNDLE.putString("container_number", contNo);
                Constants.BUNDLE.putString("StrContainerId", contId);
                Constants.BUNDLE.putInt("position", itemPosition);
                Constants.BUNDLE.putBoolean("Is_Self_Location", true);

                selfAssignDetailFragment.setArguments(Constants.BUNDLE);

                fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTran = fragManager.beginTransaction();
                fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTran.replace(R.id.job_fragment, selfAssignDetailFragment);
                fragmentTran.addToBackStack("job_detail");
                fragmentTran.commit();

            } else {
                // Constants.showToastMsg(btnLogin, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                Constants.showToastMsg(noTripTextView, "Please check your internet connection", 5000);
                progressBarHome.setVisibility(View.GONE);
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /* ---------- NOTIFY UNASSIGN ADAPTER ------------ */
    void notifyUnasignAdapter() {
        try {
            unasignAdapter = new ContainerJobAdapter(getActivity(), containerUnAssignList, 0);
            unasignListView.setAdapter(unasignAdapter);
            unasignListView.onRestoreInstanceState(ExportActivity.unasignParcebaleState);
        } catch (Exception execption) {
            noTripTextView.setVisibility(View.VISIBLE);
        }
    }



    /* ---------- POST REQUEST FOR UNASSIGN CONTAINER LIST ------------ */
    void GET_UNASSIGN_CONTAINER(final String companyId,final String type, final String containerNo){

        progressBarHome.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_UNASSIGN_CONTAINER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj, resultJson;
                        JSONArray resultJsonArray;

                        Log.d("Response ", ">>>Response: " + response);
                        progressBarHome.setVisibility(View.GONE);
                        try {
                            obj = new JSONObject(response);
//
                            if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){
                                    containerUnAssignList = new ArrayList<ContainerModel>();
                                    //  notifyUnasignAdapter();

                                    PickUpLocationId = "";
                                    resultJsonArray = new JSONArray(obj.getString("lstResult"));

                                    for (int i = 0 ; i < resultJsonArray.length() ; i++){
                                        resultJson = (JSONObject)resultJsonArray.get(i);
                                        PickUpLocationId = resultJson.getString("PickUpLocationId");

                                        int status = 0;
                                        if(resultJson.getString("Status") != null &&
                                                !resultJson.getString("Status").equalsIgnoreCase("null") ){
                                            status = resultJson.getInt("Status");

                                            /*---------------- PARSE JSON of Container Details Array-----------------*/
                                            try {
                                                containerModel = Constants.containerModel(containerModel, resultJson, status, false, false);
                                                containerUnAssignList.add(containerModel);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }else{
                                     Constants.showToastMsg(noTripTextView, "No container found", 5000);
                                }
                                notifyUnasignAdapter();
                                NoRecordViewStatus(containerUnAssignList);
                            }else{
                                Constants.showToastMsg(noTripTextView, "No container found", 5000);
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
                        // response
                        Log.d("error", ">>error: " +error);
                        //  Constants.showToastMsg(homeParentLayout, "Please check your internet connection" + error.toString(), VIEW_DURATION);

                        Constants.showToastMsg(noTripTextView, "Please check your internet connection", 5000);

                        progressBarHome.setVisibility(View.GONE);


                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<String, String>();
                // params.put("DriverId", DriverId);
              //  params.put("OrderType", "0");
                params.put("CompanyId", companyId);
                params.put("ContainerNo", containerNo);

                params.put("DriverID", DriverId);
                params.put("DeviceID", DeviceId);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getUnAssignConReq.add(postRequest);

    }


    /* ---------- SHOW NO RECORD VIEW, IF LIST IS EMPTY------------ */
    void NoRecordViewStatus(List<ContainerModel> list){

        if(list.size() > 0){
            noTripTextView.setVisibility(View.GONE);
        }else{
            noTripTextView.setVisibility(View.VISIBLE);
        }


    }


}
