package com.tracking.smarttrack.fragment;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.smarttrack.ContainerJobAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dialog.VehicleDialog;
import com.models.CompanySetingModel;
import com.models.ContainerModel;
import com.models.VehicleModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.constants.LocServices;
import com.tracking.smarttrack.ExportActivity;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExportFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{


    View rootView;

    RequestQueue SetDriverLogoutTimeReq;
    TextView newJobText, pickedupJobText, doneJobText, newJobView, doneJobTextView, noTripTextView;
    RelativeLayout jobAssignBtn, menuActionBar, notificationLay, searchActionBar, searchBarLayout,
                     backActionBar, homeParentLayout ;

    ImageView clearTextBtn, importExportImgVw;
    EditText searchEditText;
    TextView actionBarTitleTV,counterTextView;
    ListView exportListView, assignJobsListView, unasignListView;
    ContainerJobAdapter exportAdapter, assignJobsAdapter , unasignAdapter;
    Handler handler;
    Animation inAnim, outAnim;
    private int VIEW_DURATION = 5000;
    String driver_Id = "", container_No= "",PickUpLocationId="";
    String jobType = " ", StrCompanyId= " ";
    ProgressBar progressBarHome;
    ContainerModel containerModel;
    public static boolean IS_SELF_ASIGN,IS_Notification ;
    public static boolean IS_SELF_PICKEDUP = false ;
    ImageView searchImgBtn, menuImgBtn;
    String StrJobAcceptance;
    FloatingActionButton selfAssignflotbtn;
    String LoginId ="";
    String DriverID ="";
    String DeviceID = "";
    String CompanyID = "";
    String TruckId = "";
    String TruckNumber = "";
    String eventTime = "", logoutTime = "";

    public static Integer newJobsCount = -1;
    Integer JobsCount = 0;

    public static List<ContainerModel> containerUnAssignList = new ArrayList<ContainerModel>();
    public static List<ContainerModel> containerSelectedList  = new ArrayList<ContainerModel>();
    public static List<ContainerModel> containerExportList    = new ArrayList<ContainerModel>();
    public static List<ContainerModel> containerAssignedList  = new ArrayList<ContainerModel>();
    List <ContainerModel> listClone = new ArrayList<ContainerModel>();
    List<VehicleModel> vehicleList = new ArrayList<>();
    VehicleDialog vehicleDialog;

    private SwipeRefreshLayout exportSwipeContainer;
    RequestQueue getContainersListReq;
    boolean isDriverJobSequence = false, onErrorCalled = false;
    Dialog zoomInPicker;
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

    void initView(View container) {

        API_URL                     = new APIs(getActivity());
        SetDriverLogoutTimeReq      = Volley.newRequestQueue(getActivity());
        handler                     = new Handler();
        getContainersListReq        = Volley.newRequestQueue(getActivity());
        containerUnAssignList       = new ArrayList<ContainerModel>();

        actionBarTitleTV            = (TextView) container.findViewById(R.id.actionBarTitleTV);
        newJobText                  = (TextView) container.findViewById(R.id.newJobText);
        newJobView                  = (TextView) container.findViewById(R.id.newJobTextView);
        pickedupJobText             = (TextView) container.findViewById(R.id.pickedupJobText);
        doneJobTextView             = (TextView) container.findViewById(R.id.doneJobTextView);
        doneJobText                 = (TextView) container.findViewById(R.id.doneJobText);
        noTripTextView              = (TextView) container.findViewById(R.id.noTripTextView);
        counterTextView             = (TextView) container.findViewById(R.id.counterTextView);
        searchEditText              = (EditText)container.findViewById(R.id.searchEditText);
        searchImgBtn                = (ImageView)container.findViewById(R.id.searchImgBtn);
        menuImgBtn                  = (ImageView)container.findViewById(R.id.menuImgBtn);
        clearTextBtn                = (ImageView)container.findViewById(R.id.clearTextBtn);
        importExportImgVw           = (ImageView)container.findViewById(R.id.importExportImgVw);
        exportListView              = (ListView) container.findViewById(R.id.exportListView);
        assignJobsListView          = (ListView) container.findViewById(R.id.importListView);
        unasignListView             = (ListView) container.findViewById(R.id.unasignListView);

        jobAssignBtn                = (RelativeLayout) container.findViewById(R.id.jobAssignBtn);
        menuActionBar               = (RelativeLayout) container.findViewById(R.id.menuActionBar);
        notificationLay             = (RelativeLayout) container.findViewById(R.id.notificationLay);
        searchActionBar             = (RelativeLayout) container.findViewById(R.id.searchActionBar);
        searchBarLayout             = (RelativeLayout) container.findViewById(R.id.searchBarLayout);
        backActionBar               = (RelativeLayout) container.findViewById(R.id.backActionBar);
        homeParentLayout            = (RelativeLayout) container.findViewById(R.id.homeParentLayout);
        progressBarHome             = (ProgressBar)container.findViewById(R.id.progressBarHome);
        selfAssignflotbtn           = (FloatingActionButton) container.findViewById(R.id.selfAssignflotbtn);
        inAnim                      = AnimationUtils.loadAnimation( getActivity(), R.anim.in_animation);
        outAnim                     = AnimationUtils.loadAnimation( getActivity(), R.anim.out_animation);

        containerSelectedList  = new ArrayList<ContainerModel>();
        containerExportList    = new ArrayList<ContainerModel>();
        containerAssignedList  = new ArrayList<ContainerModel>();
        containerUnAssignList  = new ArrayList<ContainerModel>();

        if(!IS_SELF_PICKEDUP) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                container_No      = bundle.getString("contNo");
                jobType           = bundle.getString("jobType");
                IS_SELF_ASIGN     = bundle.getBoolean("IS_SELF_ASIGN");
                IS_Notification   = bundle.getBoolean("IsNotification");
            }
        }

        IS_SELF_PICKEDUP = false;
        DeviceID    = LoginActivity.deviceID.trim();
        DriverID    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyID   = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());

        if(IS_Notification){
            notificationLay.performClick();
        }
        exportSwipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.exportSwipeContainer);
        // Setup refresh listener which triggers new data loading
        exportSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GET_CONTAINER_LIST(driver_Id, ExportActivity.SHOW_PROGRESS_BAR, Constants.CONTAINER_LIST_TYPE);

            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    listClone = new ArrayList<ContainerModel>();
                    String containerNumber = "";

                    if(s.length()>0){
                        clearTextBtn.setVisibility(View.VISIBLE);
                        containerNumber = searchEditText.getText().toString();
                        containerNumber = containerNumber.toLowerCase().trim();
                        for (ContainerModel string : containerExportList) {
                            if(string.getContainerNo().toLowerCase().trim().contains(containerNumber)){
                                listClone.add(string);
                            }
                        }
                        exportListView.setAdapter( new ContainerJobAdapter(getActivity(), listClone, Constants.EXPORT));
                    }else{
                        clearTextBtn.setVisibility(View.GONE);
                        exportListView.setAdapter(new ContainerJobAdapter(getActivity(), containerExportList, Constants.EXPORT));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        driver_Id = Constants.getDriverId( Constants.KEY_DRIVER_ID , getActivity());
        StrCompanyId = Constants.getCompanyId(Constants.KEY_COMPANY, getActivity());
        statusCheck();

        showTruckList();


        jobAssignBtn.setOnClickListener(this);
        menuActionBar.setOnClickListener(this);
        notificationLay.setOnClickListener(this);
        searchActionBar.setOnClickListener(this);
        backActionBar.setOnClickListener(this);
        homeParentLayout.setOnClickListener(this);
        clearTextBtn.setOnClickListener(this);

        exportListView.setOnItemClickListener(this);
        assignJobsListView.setOnItemClickListener(this);
        unasignListView.setOnItemClickListener(this);
        selfAssignflotbtn.setOnClickListener(this);

    }




    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getGPSLocation();
        }
    }


    void getGPSLocation(){

        LocServices locationServices = new LocServices(getActivity());
        locationServices.getLocations();
      //  if(locationServices.getIsGPSTrackingEnabled()){
            try {
                Constants.CURRENT_LATITUDE = "" +locationServices.getLatitude();
                Constants.CURRENT_LONGITUDE = "" +locationServices.getLongitude();
            }catch (Exception e){
                e.printStackTrace();
            }
      //  }

    }

    @Override
    public void onResume() {
        super.onResume();

        Constants.hideSoftKeyboard(getActivity());

            menuImgBtn.setImageResource(R.drawable.icn_menu_dark);
            searchActionBar.setVisibility(View.VISIBLE);
            notificationLay.setVisibility(View.VISIBLE);
            actionBarTitleTV.setText("Containers");
            JobExportView();
            notifyExportAdapter();

            if(Constants.isNetworkAvailable(getContext()) ) {
                GET_CONTAINER_LIST(driver_Id, ExportActivity.SHOW_PROGRESS_BAR, Constants.CONTAINER_LIST_TYPE);
            }else{
                Constants.showToastMsg(clearTextBtn, "Please check your internet connection", VIEW_DURATION);
            }

            ExportActivity.SHOW_PROGRESS_BAR = false;
            TabActivity.isImport = true;

        NoRecordViewStatus(containerExportList);

        TabActivity.isHome = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.notificationLay:

                moveToNewFeedFragment();

                break;


            case R.id.jobAssignBtn:

                if(Constants.CONTAINER_LIST_TYPE.equals("2")){
                    JobExportView();
                }else{
                    JobImportView();
                }

            break;


            case R.id.menuActionBar:
                Constants.hideSoftKeyboard(getActivity());

                if (IS_SELF_ASIGN == false) {
                    TabActivity.sliderLay.performClick();
                }else{
                    IS_SELF_ASIGN = false;
                    TabActivity.host.setCurrentTab(0);
                }

                break;

            case R.id.searchActionBar:
                searchBarLayout.setVisibility(View.VISIBLE);
                searchBarLayout.startAnimation( inAnim );
                searchEditText.setText("");
                searchEditText.setFocusable(true);
                searchEditText.requestFocus();
                searchEditText.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_FORCED);

                break;

            case R.id.backActionBar:
                Constants.hideSoftKeyboard(getActivity());
                searchBarLayout.setVisibility(View.GONE);
                searchBarLayout.startAnimation( outAnim );
                searchEditText.setText("");
                clearTextBtn.setVisibility(View.GONE);

                break;

            case R.id.homeParentLayout:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.clearTextBtn:
                searchEditText.setText("");
                clearTextBtn.setVisibility(View.GONE);
                break;

            case R.id.selfAssignflotbtn:
                TabActivity.selfAssignBtn.performClick();


                break;

        }
    }


    /* ---------- EXPORT CONAINER VIEW ------------ */
    void JobExportView(){
        exportListView.setVisibility(View.VISIBLE);
        assignJobsListView.setVisibility(View.GONE);
        unasignListView.setVisibility(View.GONE);
        Constants.hideSoftKeyboard(getActivity());

        NoRecordViewStatus(containerExportList);
        exportListView.setItemsCanFocus(true);

    }

    /* ---------- ASSIGNED CONAINER VIEW ------------ */
    void JobImportView(){
        exportListView.setVisibility(View.GONE);
        assignJobsListView.setVisibility(View.VISIBLE);
        unasignListView.setVisibility(View.GONE);

        Constants.hideSoftKeyboard(getActivity());

        NoRecordViewStatus(containerAssignedList);
        assignJobsListView.setItemsCanFocus(true);
    }

    /* ---------- UNASSIGN CONAINER VIEW ------------ */
    void UnAssignContrView(){
        exportListView.setVisibility(View.GONE);
        assignJobsListView.setVisibility(View.GONE);
        importExportImgVw.setVisibility(View.GONE);
        unasignListView.setVisibility(View.VISIBLE);
        Constants.hideSoftKeyboard(getActivity());

        NoRecordViewStatus(containerUnAssignList);
        unasignListView.setItemsCanFocus(true);
    }

    /* ---------- SHOW NO RECORD VIEW, IF LIST IS EMPTY------------ */
    void NoRecordViewStatus(List<ContainerModel> list){

        if(list.size() > 0){
            noTripTextView.setVisibility(View.GONE);
        }else{
            noTripTextView.setVisibility(View.VISIBLE);
        }


    }


    /* ---------- MOVE TO New Feed FRAGMENT ------------ */
    void moveToNewFeedFragment( ){

        try{
            FragmentManager fragManager;
            NewFeedFragment newFeedFragment = new NewFeedFragment();
            fragManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTran = fragManager.beginTransaction();
            fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTran.replace(R.id.job_fragment, newFeedFragment);
            fragmentTran.addToBackStack("new_feed");
            fragmentTran.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /* ---------- MOVE TO Event FRAGMENT ------------ */
    void moveToEventFragment( final String EventId,  final String Status, final String Address){

        try{
                NewEventFragment detailFragment = new NewEventFragment();
                Constants.BUNDLE.putString("EventId", EventId);
                Constants.BUNDLE.putString("Status", Status);
                Constants.BUNDLE.putString("Address", Address);
                detailFragment.setArguments(Constants.BUNDLE);

                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTran = fragManager.beginTransaction();
                fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTran.replace(R.id.job_fragment, detailFragment);
                fragmentTran.addToBackStack("job_detail");
                fragmentTran.commit();




        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /* ---------- MOVE TO CONTAINER DETAIL FRAGMENT ------------ */
    void moveToOtherFragment(final String jobType, final String contNo,  final int position ){

        try{
//            progressBarHome.setVisibility(View.VISIBLE);

                    Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(getContext());
                    if(Constants.INTERNET_CONNECTIVITY_STATUS) {
                        progressBarHome.setVisibility(View.GONE);

                        ExportDetailFragment detailFragment = new ExportDetailFragment();
                        Constants.BUNDLE.putString("jobType", jobType);
                        Constants.BUNDLE.putString("container_number", contNo);
                        Constants.BUNDLE.putInt("position", position);
                        detailFragment.setArguments(Constants.BUNDLE);

                        FragmentManager fragManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTran = fragManager.beginTransaction();
                        fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTran.replace(R.id.job_fragment, detailFragment);
                        fragmentTran.addToBackStack("job_detail");
                        fragmentTran.commit();
                    }else {
                        Constants.showToastMsg(clearTextBtn, "Please check your internet connection", VIEW_DURATION);
                    }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /* ---------- MOVE TO Self Assign FRAGMENT ------------ */
    void moveToSelfAsignFragment(final String jobType, final String contNo ,final int itemPosition ){

        try{

                    Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(getContext());
                    if (Constants.INTERNET_CONNECTIVITY_STATUS) {
                        progressBarHome.setVisibility(View.GONE);

                        SelfAssignDetailFragment selfAssignDetailFragment = new SelfAssignDetailFragment();

                        Constants.BUNDLE.putString("jobType", jobType);
                        Constants.BUNDLE.putString("container_number", contNo);
                        Constants.BUNDLE.putInt("position", itemPosition);
                        Constants.BUNDLE.putBoolean("Is_Self_Location", true);

                        selfAssignDetailFragment.setArguments(Constants.BUNDLE);

                        FragmentManager fragManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTran = fragManager.beginTransaction();
                        fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTran.replace(R.id.job_fragment, selfAssignDetailFragment);
                        fragmentTran.addToBackStack("job_detail");
                        fragmentTran.commit();

                    } else {
                        // Constants.showToastMsg(btnLogin, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                        Constants.showToastMsg(clearTextBtn, "Please check your internet connection", VIEW_DURATION);
                        progressBarHome.setVisibility(View.GONE);
                    }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /* ---------- CONAINER LISTVIEW ITEM CLICK------------ */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        containerSelectedList = new ArrayList<ContainerModel>();
        int pos = position + 1;

        switch (parent.getId()){


            case R.id.exportListView:

                    String conrNo = "";
                    int positionItem = position;
                    if (listClone.size() > 0) {
                        String containerNo = listClone.get(position).getContainerNo();
                        for (int i = 0; i < containerExportList.size(); i++) {
                            conrNo = containerExportList.get(i).getContainerNo();
                            if (containerNo.equals(conrNo)) {
                                positionItem = i;
                                break;
                            }
                        }
                    }

                    String OrderType = String.valueOf(containerExportList.get(positionItem).getOrderType());
                    Constants.hideSoftKeyboard(getActivity());
                    containerSelectedList.add(containerExportList.get(positionItem));
                    ExportActivity.exportParcebaleState = exportListView.onSaveInstanceState();

                if(!isDriverJobSequence) {

                    if(containerExportList.get(positionItem).getContainerType().equals(Constants.EVENT_ITEM_TYPE)) {
                        moveToEventFragment( ""+containerExportList.get(positionItem).getContainerId(),
                                ""+containerExportList.get(positionItem).getStatus(),
                                containerExportList.get(positionItem).getOrignAddress() +  "<br>" + containerExportList.get(positionItem).getDestinationAddress());
                    }else{
                        moveToOtherFragment(OrderType, "" + positionItem, positionItem);
                    }

                }else{
                    if(position == 0) {
                        if (conrNo.equals("") || conrNo.equals(containerExportList.get(0).getContainerNo())) {

                            if(containerExportList.get(positionItem).getContainerType().equals(Constants.EVENT_ITEM_TYPE)) {
                                moveToEventFragment( ""+containerExportList.get(positionItem).getContainerId(),
                                        ""+containerExportList.get(positionItem).getStatus(),
                                        containerExportList.get(positionItem).getOrignAddress() +  "<br>" + containerExportList.get(positionItem).getDestinationAddress());
                            }else{
                                moveToOtherFragment(OrderType, "" + positionItem, positionItem);
                            }

                        } else {
                            Constants.showToastMsg(homeParentLayout, getString(R.string.complete_job_desc), Snackbar.LENGTH_LONG);
                        }
                    }else{
                        Constants.showToastMsg(homeParentLayout, getString(R.string.complete_job_desc), Snackbar.LENGTH_LONG);
                    }
                }

                break;

            case R.id.importListView:
                String OrderTypei = String.valueOf(containerAssignedList.get(position).getOrderType());
                containerSelectedList.add(containerAssignedList.get(position));
                ExportActivity.importParcebaleState = assignJobsListView.onSaveInstanceState();
                moveToOtherFragment(OrderTypei, "" +pos, position);
                break;

            case R.id.unasignListView:
                String OrderTypeU = String.valueOf(containerUnAssignList.get(position).getOrderType());
                String LegType = containerUnAssignList.get(position).getLegType();
                String LoadingLocationId = containerUnAssignList.get(position).getLoadingLocationId();
                containerSelectedList.add(containerUnAssignList.get(position));
                ExportActivity.unasignParcebaleState = unasignListView.onSaveInstanceState();

                if(LegType.equals("1") || LegType.equals("3")){
                    if( PickUpLocationId == "null"){
                        Constants.showToastMsg( homeParentLayout,"Sorry ! this Container don't have Pickup Location.", Snackbar.LENGTH_LONG);
                    }else {
                        moveToSelfAsignFragment(OrderTypeU,container_No, position);
                    }
                }else{
                    if(LoadingLocationId.equalsIgnoreCase("null")){
                        Constants.showToastMsg( homeParentLayout,"Sorry ! this Container don't have Pickup Location.", Snackbar.LENGTH_LONG);
                    }else {
                        moveToSelfAsignFragment(OrderTypeU,container_No, position);
                    }
                }

                break;
        }

    }


    /* ---------- POST REQUEST FOR IMORT/EXPORT CONTAINERS LIST ------------ */
    void GET_CONTAINER_LIST(final String DriverId, boolean loaderStatus, final String type){


        progressBarHome.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_CONTAINER_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj, resultJson;
                        JSONArray resultJsonArray;

                        Log.d("Response ", ">>>Response: " + response);
                        progressBarHome.setVisibility(View.GONE);
                        exportSwipeContainer.setRefreshing(false);
                        isDriverJobSequence = false;

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 0){

                                if(obj.getBoolean("Success") == true){

                                    if(type.equals("2")) {
                                        containerExportList = new ArrayList<ContainerModel>();
                                        notifyExportAdapter();
                                    }else{
                                        containerAssignedList = new ArrayList<ContainerModel>();
                                        notifyAssignedJobAdapter();
                                    }
                                }

                            }else if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){
                                    containerExportList = new ArrayList<ContainerModel>();
                                    containerAssignedList = new ArrayList<ContainerModel>();

                                    if(obj.get("Result") != null)
                                    {
                                        newJobsCount = obj.getInt("Result");
                                    }

                                    if(newJobsCount > JobsCount) {
                                        counterTextView.setVisibility(View.VISIBLE);
                                    }


                                    try{

                                        JSONArray settingArray = new JSONArray(obj.getString("Extra"));
                                        if(settingArray.length() > 0) {
                                            JSONObject settingJson = (JSONObject) settingArray.get(0);
                                            if(!settingJson.isNull("Value")) {
                                                isDriverJobSequence = settingJson.getBoolean("Value");
                                            }
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    try{
                                        eventTime = obj.getString("EventTime").replaceAll("Z","");
                                        logoutTime = obj.getString("LogoutTime").replaceAll("Z","");

                                        if(!eventTime.equals("null") && !eventTime.equals("null")) {
                                            Constants.setLogoutEventDate(eventTime, logoutTime, getActivity());
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    resultJsonArray = new JSONArray(obj.getString("lstResult"));

                                    boolean isFirstItem = true;
                                    for (int i = 0 ; i < resultJsonArray.length() ; i++){
                                        resultJson = (JSONObject)resultJsonArray.get(i);
                                        int status = 0;
                                        if(resultJson.getString("Status") != null &&
                                                !resultJson.getString("Status").equalsIgnoreCase("null") ){
                                            status = resultJson.getInt("Status");
                                        }

                                   /*---------------- PARSE JSON of Container Details Array-----------------*/
                                        try {


                                            if(type.equals("2")) {
                                                containerModel = Constants.containerModel(containerModel, resultJson, status,
                                                        isDriverJobSequence, isFirstItem);
                                                isFirstItem = false;

                                                containerExportList.add(containerModel);
                                                StrJobAcceptance = containerExportList.get(0).getJobAcceptance();
                                            }else{
                                                isFirstItem = false;
                                                containerModel = Constants.containerModel(containerModel, resultJson, status,
                                                        isDriverJobSequence, isFirstItem);

                                                containerAssignedList.add(containerModel);
                                                StrJobAcceptance = containerAssignedList.get(0).getJobAcceptance();
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }

                                    if(type.equals("2")) {
                                        if(obj.has("Events") && !obj.isNull("Events")){

                                            try {
                                                boolean isFirst = true;
                                                JSONArray eventArray = new JSONArray(obj.getString("Events"));
                                                for (int ev = 0; ev < eventArray.length(); ev++) {
                                                    JSONObject eventJson = (JSONObject) eventArray.get(ev);
                                                    if(eventJson.getInt("EventType") == 3) {
                                                        containerModel = Constants.eventContainerModel(eventJson, isFirst);
                                                        containerExportList.add(containerModel);
                                                        isFirst = false;
                                                    }
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    if(containerExportList.size() == 0 && newJobsCount == 0){
                                        SetDriverLogoutTime();
                                    }else{
                                        showAlertToast(eventTime, logoutTime, true);
                                    }

                                }

                                if(type.equals("2")) {
                                    notifyExportAdapter();
                                    NoRecordViewStatus(containerExportList);
                                }else{
                                    notifyAssignedJobAdapter();
                                    NoRecordViewStatus(containerAssignedList);
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
                        // response
                        Log.d("error", ">>error: " +error);
                       // Constants.showToastMsg(homeParentLayout, "Please check your internet connection", VIEW_DURATION);


                        progressBarHome.setVisibility(View.GONE);
                        exportSwipeContainer.setRefreshing(false);
                        jobAssignBtn.performClick();

                        if(!onErrorCalled) {
                            TabActivity.host.setCurrentTab(4);
                            TabActivity.host.setCurrentTab(0);
                        }
                        onErrorCalled = true;
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<String, String>();
                params.put("DriverId", DriverId);
                params.put("OrderType", type);

                params.put("DriverID", DriverID);
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                params.put("CompanyID", CompanyID);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getContainersListReq.add(postRequest);

    }




    void LogoutRequest(final String driverId, final String deviceId){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.LOGOUT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;

                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){
                                    TabActivity activity = TabActivity.instance;

                                    Constants.setDriverId(Constants.KEY_DRIVER_ID, "", getActivity());
                                    Constants.setCompanyId(Constants.KEY_COMPANY, "",getActivity());
                                    String desc = "You are done for the day";
                                    Toast.makeText(getActivity(), desc , Toast.LENGTH_LONG).show();
                                    Constants.ShowLocalNotification(getActivity(), LoginActivity.instance, "EVENT COMPLETED !", desc, 1001 );


                                    Intent i = new Intent(activity, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(i);
                                    activity.finish();

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

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", driverId);
                params.put("DeviceId", deviceId);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }



    void SetDriverLogoutTime(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.SET_DRIVER_LOGOUT_TIME,
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

                                JSONObject ResultObj = new JSONObject(obj.getString("Result"));
                                logoutTime = ResultObj.getString("LogoutDateTime");
                                eventTime = ResultObj.getString("EventDateTime");

                                if(!logoutTime.equals("null")) {
                                    eventTime = eventTime.replaceAll("Z","");
                                    logoutTime = logoutTime.replaceAll("Z","");
                                    Constants.setLogoutEventDate(eventTime, logoutTime, getActivity());

                                    if(logoutTime.length() >= 19) {
                                        logoutTime = logoutTime.substring(0, 19);
                                    }
                                    calculateLogoutTime(logoutTime);


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
                        Log.d("error", ">>error: " +error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", DriverID);
                params.put("CompanyId", CompanyID);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        SetDriverLogoutTimeReq.add(postRequest);

    }




    void showAlertToast(String eventTimeStr, String logoutTimeStr, boolean isLogoutApiCalled ){

        try {

            if (eventTimeStr.length() > 10 && Constants.getTruckNumber(getActivity()).length() > 0) {

                DateTime currentDate = Constants.getDateTimeObj(Constants.GetCurrentUTCTimeFormat());
                DateTime logoutDate = Constants.getDateTimeObj(logoutTimeStr);
                DateTime eventTime = Constants.getDateTimeObj(eventTimeStr);

                if (currentDate.isAfter(eventTime)) {
                    eventTime = currentDate;
                }
                int totalLeft = logoutDate.getMinuteOfDay() - eventTime.getMinuteOfDay();
                int leftHour = Constants.HourFromMin(totalLeft);
                int leftMin = Constants.MinFromHourOnly(totalLeft);

                String time = "";
                if (leftHour > 0) {
                    if (leftHour == 1) {
                        time = "" + leftHour + " hour";
                    } else {
                        time = "" + leftHour + " hours";
                    }
                } else {
                    time = "" + leftMin + " mins";
                }

                if (currentDate.equals(eventTime) || currentDate.isAfter(eventTime)) {

                        TabActivity.isEventMsgShown = true;
                        if (leftMin > 0 || leftHour > 0) {
                            String desc = "You have " + time + " left to complete the event";
                            //Constants.showToastMsg(searchEditText, desc, VIEW_DURATION);
                            Constants.SnackBarAtTop(searchEditText, desc, getActivity());
                        }

                }


                if (Constants.isNetworkAvailable(getActivity()) && isLogoutApiCalled) {
                    if (currentDate.isAfter(logoutDate) && (containerExportList.size() == 0 && newJobsCount == 0) ) {
                         LogoutRequest(DriverID, DeviceID);

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /* ---------- NOTIFY ASSIGNED ADAPTER ------------ */
    void notifyAssignedJobAdapter() {
        try {
            assignJobsAdapter = new ContainerJobAdapter(getActivity(), containerAssignedList, Constants.ASSIGNED);
            assignJobsListView.setAdapter(assignJobsAdapter);
            assignJobsListView.onRestoreInstanceState(ExportActivity.importParcebaleState);
        } catch (Exception execption) {
            noTripTextView.setVisibility(View.VISIBLE);
        }
    }

    /* ---------- NOTIFY EXPORT ADAPTER ------------ */
    void notifyExportAdapter(){
        try {
            listClone = new ArrayList<>();
            exportAdapter = new ContainerJobAdapter(getActivity(), containerExportList, Constants.EXPORT);
            exportListView.setAdapter(exportAdapter);
          //  exportListView.onRestoreInstanceState(ExportActivity.exportParcebaleState);
        }catch (Exception execption){
            noTripTextView.setVisibility(View.VISIBLE);
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


    void showTruckList(){

        if(Constants.getTruckNumber(getActivity()).length() == 0) {
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

                    vehicleDialog = new VehicleDialog(getActivity(), vehicleList, false, new VehicleListener(), new LogoutListener() );
                    vehicleDialog.show();

                } else {
                    Constants.showToastMsg(searchEditText, "No vehicles are available", VIEW_DURATION);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            getDriverReadyStatusDialog();
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

                SaveDefaultTruck( driver_Id, TruckId, CompanyID, LoginId );

            } else {
                Constants.showToastMsg(searchEditText, "Please check your internet connection", VIEW_DURATION);
            }

        }

    }



    private class LogoutListener implements  VehicleDialog.LogoutListener {

        @Override
        public void LogoutReady() {
            if (Constants.isNetworkAvailable(getActivity())) {

                try {
                    if (vehicleDialog != null && vehicleDialog.isShowing())
                        vehicleDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                TabActivity.logoutPopUpBtn.performClick();

            } else {
                Constants.showToastMsg(searchEditText, "Please check your internet connection", VIEW_DURATION);
            }
        }
    }




    void SaveDefaultTruck(final String Driverid, final String truckid, final String companyid, final String LoginID){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressBarHome.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.SET_DEFAULT_TRUCK,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarHome.setVisibility(View.GONE);
                        JSONObject obj;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1 && obj.getBoolean("Success")){
                                Constants.setTruckDetails(TruckNumber, TruckId, getActivity());
                                getDriverReadyStatusDialog();
                            }else{
                                Constants.showToastMsg(searchEditText, obj.getString("Success"), VIEW_DURATION);
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
                        Constants.showToastMsg(searchEditText, "Please check your internet connection", VIEW_DURATION);
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



    void getDriverReadyStatusDialog(){

        if(Constants.isFreshLogin(getActivity())){

            try{

                if(zoomInPicker != null && zoomInPicker.isShowing()){
                    zoomInPicker.dismiss();
                }

                zoomInPicker = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                zoomInPicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                zoomInPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zoomInPicker.setContentView(R.layout.popup_ready_status);
                zoomInPicker.setCancelable(false);

                ImageView zoominImgVw = (ImageView) zoomInPicker.findViewById(R.id.zoominReadyImgVw);
                final LinearLayout readyDoneLay = (LinearLayout) zoomInPicker.findViewById(R.id.readyDoneLay);
                LinearLayout readyDialogParentLay = (LinearLayout)zoomInPicker.findViewById(R.id.readyDialogParentLay);

                Animation animZoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
                zoominImgVw.startAnimation(animZoomIn);

                readyDoneLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Constants.isNetworkAvailable(getActivity())) {
                            SaveDriverReadyStatus(DriverID, LoginId );
                        } else {
                            Constants.showToastMsg(searchEditText, "Please check your internet connection", VIEW_DURATION);
                        }
                    }
                });

                readyDialogParentLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        readyDoneLay.performClick();
                    }
                });
                zoomInPicker.show();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }



    void SaveDriverReadyStatus(final String Driverid, final String LoginID){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressBarHome.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.SET_DRIVER_READY_STATUS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarHome.setVisibility(View.GONE);
                        JSONObject obj;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1 && obj.getBoolean("Success")){

                                Constants.showToastMsg(searchEditText, "Now you can start your job.", VIEW_DURATION);
                                zoomInPicker.dismiss();
                                Constants.setLoginStatus(false, getActivity());
                                showAlertToast(eventTime, logoutTime, false);
                                TabActivity.checkUpdateBtn.performClick();

                            }else{
                                Constants.showToastMsg(searchEditText, obj.getString("Success"), VIEW_DURATION);
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
                        Constants.showToastMsg(searchEditText, "Please check your internet connection", VIEW_DURATION);
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
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyID);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }




    void calculateLogoutTime(String logoutTimeStr){


        try {

            if (logoutTimeStr.length() > 10) {

                DateTime currentDate = Constants.getDateTimeObj(Constants.GetCurrentUTCTimeFormat());
                DateTime logoutDate = Constants.getDateTimeObj(logoutTimeStr);

        if(getActivity() != null){
                if (currentDate.isAfter(logoutDate) ) {
                    LogoutRequest(DriverID, DeviceID);
                }else {

                    int totalLeft = logoutDate.getMinuteOfDay() - currentDate.getMinuteOfDay();
                    int leftHour = Constants.HourFromMin(totalLeft);
                    int leftMin = Constants.MinFromHourOnly(totalLeft);

                    String time = "";
                    if (leftHour > 0) {
                        if (leftHour == 1) {
                            time = "" + leftHour + " hour";
                        } else {
                            time = "" + leftHour + " hours";
                        }
                    } else {
                        time = "" + leftMin + " mins";
                    }


                    if (leftMin > 0 || leftHour > 0) {
                        String desc = "You have " + time + " left to complete the event";
                        Constants.SnackBarAtTop(searchEditText, desc, getActivity());
                    }
                }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void onPause() {
        super.onPause();
        TabActivity.isHome = false;
        TabActivity.isImport = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TabActivity.isHome = false;
        TabActivity.isImport = false;
    }



}
