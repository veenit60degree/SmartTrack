package com.tracking.smarttrack.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.models.ContainerModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.constants.GPSTracker;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfAssignDetailFragment extends android.support.v4.app.Fragment {

    View rootView;
    Bundle savedInstanceStateBundle;
    Button btnDriverJob, btnCancelJob;
    EditText ChasisNoEditTxt;
    private int VIEW_DURATION = 5000;

    private List<String> truckList;
    private List<String> chasisList;
    private List<String> truckIdList;
    private List<String> chasisIdList;
    private List<String> yardIdList;
    private List<String> yardList;
    private List<String> locationList;
    private List<String> locationIdList;
    List<ContainerModel> containerDetailList = new ArrayList<ContainerModel>();
    List<ContainerModel> containerUnAssignList = new ArrayList<ContainerModel>();
    ArrayList<String> yardAddressArray = new ArrayList<>();

    Spinner spinTruck, spinChasis, spinYard, spinUnloading, spinLoadType, spinDropoff;
    String[] loadType = {"Select Load", "Loaded", "Partial Load", "Empty"};
    String dropOffLocationName = "", dropOffAddress = "";
    private RadioGroup radioSelctLocType;
    private RadioButton radioYardbtn, radioDropoffbtn, radioLoadUnloadbtn;

    TextView jobtypeTV, containerIdTV, SizeTV, grosswtTV, DropoffTV, chassies_eNo, Truck_eNo, contInfoTitleTV, radioGHader;
    TextView resNumberTV , resDateTV, resTimeTV;
    TextView jobtypelblTV, contnrTitleTV, sizetitleTV, GrosswtitleTV, containerTypeIdTV, chassiesNoTV, ContainerLoadTypeTV, truckNoTV;
    RelativeLayout editFieldView, asignParentLayout, notificationLay, menuActionBar, LoadTypeLayout;
    RelativeLayout searchActionBar, dropOffDetailsLay;
    LinearLayout radioLayout;
    ImageView menuImgBtn;
    ProgressBar progressBarHome;
    String driver_Id = "", StrContainerId = "", StrLoadtype= "",StrContainerType="", StrContainerSize= "", StrGrosswtitleTV= "", StrCompanyId= "",
            StrTruckId= "", StrContainerTypeDetail = "", StrChessieId= "", StrAddressId="", StrLegType = "", StrTrucksId= "", StrChessiesId= "", StrYardId= "", StrLocationId= "", StrDropoffId= "",
            StrTruckNo= "", StrChassiesNo= "", StrContainerNo= "";

    String ContainerNo = "", JobType = "", StrContainerLoadType = "0", strLoadUnload = "";
    int intContainerStatus, IntNextStatus, position = 0;
    ContainerModel containerModel;
    Constants constants;

    String LoginId ="";
    String DriverID ="";
    String DeviceID = "";
    String CompanyID = "";
    boolean isSelfAsign = false, isChessisValidate = false;
    Bundle bundle;


    RequestQueue getUnAssignContReq;
    RequestQueue getContDetailsReq;
    RequestQueue getCompSettingReq;
    RequestQueue pickUpReq;
    RequestQueue selfAssignReq;
    RequestQueue getTruckChessisReq;

    APIs API_URL;

    boolean isLoad = false,YardSelfAssignImport = false, YardSelfAssignExport = false ;
    boolean YardAssignImport = false, YardAssignExport = false, ValidateChassisExport = false, ValidateChassisImport = false ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.self_assignjob_details, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            savedInstanceStateBundle = savedInstanceState;
        } catch (InflateException e) {
            e.printStackTrace();
        }
        initView(rootView);

        return rootView;

    }

    void initView(View container) {

        API_URL              = new APIs(getActivity());
        getUnAssignContReq   = Volley.newRequestQueue(getActivity());
        getContDetailsReq    = Volley.newRequestQueue(getActivity());
        getCompSettingReq    = Volley.newRequestQueue(getActivity());
        pickUpReq            = Volley.newRequestQueue(getActivity());
        selfAssignReq        = Volley.newRequestQueue(getActivity());
        getTruckChessisReq   = Volley.newRequestQueue(getActivity());

        constants         = new Constants();
        editFieldView     = (RelativeLayout) container.findViewById(R.id.editFieldView);
        asignParentLayout = (RelativeLayout) container.findViewById(R.id.asignParentLayout);
        menuActionBar     = (RelativeLayout) container.findViewById(R.id.menuActionBar);
        notificationLay   = (RelativeLayout) container.findViewById(R.id.notificationLay);
        LoadTypeLayout    = (RelativeLayout) container.findViewById(R.id.ContainerLoadTypelbl);
        searchActionBar   = (RelativeLayout) container.findViewById(R.id.searchActionBar);
        dropOffDetailsLay = (RelativeLayout) container.findViewById(R.id.dropOffDetailsLay);
        radioLayout       = (LinearLayout) container.findViewById(R.id.radioLayout);

        contInfoTitleTV   = (TextView) container.findViewById(R.id.actionBarTitleTV);
        jobtypeTV         = (TextView) container.findViewById(R.id.jobtypeTV);
        jobtypelblTV      = (TextView) container.findViewById(R.id.jobtypelblTV);
        contnrTitleTV     = (TextView) container.findViewById(R.id.contnrTitleTV);
        containerIdTV     = (TextView) container.findViewById(R.id.containerIdTV);
        sizetitleTV       = (TextView) container.findViewById(R.id.sizetitleTV);
        SizeTV            = (TextView) container.findViewById(R.id.SizeTV);
        GrosswtitleTV     = (TextView) container.findViewById(R.id.GrosswtitleTV);
        grosswtTV         = (TextView) container.findViewById(R.id.grosswtTV);
        chassies_eNo      = (TextView) container.findViewById(R.id.chassies_eNo);
        Truck_eNo         = (TextView) container.findViewById(R.id.Truck_eNo);
        truckNoTV         = (TextView) container.findViewById(R.id.TruckNoTV);
        chassiesNoTV      = (TextView) container.findViewById(R.id.ChasisNoTV);
        radioGHader       = (TextView) container.findViewById(R.id.header);
        DropoffTV         = (TextView) container.findViewById(R.id.DropoffTV);
        containerTypeIdTV = (TextView) container.findViewById(R.id.containerTypeIdTV);
        ContainerLoadTypeTV = (TextView) container.findViewById(R.id.ContainerLoadTypeTV);

        resNumberTV         = (TextView) container.findViewById(R.id.resNumberTV);
        resDateTV         = (TextView) container.findViewById(R.id.resDateTV);
        resTimeTV         = (TextView) container.findViewById(R.id.resTimeTV);

        spinChasis        = (Spinner) container.findViewById(R.id.spinnerChasis);
        spinTruck         = (Spinner) container.findViewById(R.id.spinnerTruck);
        spinYard          = (Spinner) container.findViewById(R.id.spinnerYard);
        spinUnloading     = (Spinner) container.findViewById(R.id.spinnerUnloading);
        spinDropoff       = (Spinner) container.findViewById(R.id.spinnerDropoff);
        spinLoadType      = (Spinner) container.findViewById(R.id.spinnLoadType);
        progressBarHome   = (ProgressBar) container.findViewById(R.id.progressBarSelf);
        menuImgBtn        = (ImageView)container.findViewById(R.id.menuImgBtn);

        radioSelctLocType = (RadioGroup) container.findViewById(R.id.selctLocTypegrp);
        radioYardbtn      = (RadioButton) container.findViewById(R.id.radio_yard);
        radioDropoffbtn   = (RadioButton) container.findViewById(R.id.radio_Dropoff);
        radioLoadUnloadbtn = (RadioButton) container.findViewById(R.id.radio_Loading);
        ChasisNoEditTxt     = (EditText)container.findViewById(R.id.ChasisNoEditTxt);

        btnDriverJob = (Button) container.findViewById(R.id.btnDriverJob);
        btnCancelJob = (Button) container.findViewById(R.id.btnCancelJob);

        driver_Id = Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity());

        radioYardbtn.setChecked(false);
        radioDropoffbtn.setChecked(false);
        radioLoadUnloadbtn.setChecked(false);

        spinYard.setVisibility(View.GONE);
        spinUnloading.setVisibility(View.GONE);
        LoadTypeLayout.setVisibility(View.GONE);
        spinDropoff.setVisibility(View.GONE);
        DropoffTV.setVisibility(View.GONE);

        truckList = new ArrayList<>();
        chasisList = new ArrayList<>();
        yardList = new ArrayList<>();
        locationList = new ArrayList<>();

        searchActionBar.setVisibility(View.GONE);
        notificationLay.setVisibility(View.GONE);

        DeviceID    = LoginActivity.deviceID.trim();
        DriverID    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyID   = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());

        menuImgBtn.setImageResource(R.drawable.ic_arrow_backbb);
        TabActivity.isHome = false;

        radioYardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioYardbtn.isChecked()) {
                    spinYard.setVisibility(View.VISIBLE);
                    dropOffDetailsLay.setVisibility(View.GONE);
                    if(yardList.size() > 0) {
                        spinYard.setSelection(0);
                    }
                    spinUnloading.setVisibility(View.GONE);
                    DropoffTV.setVisibility(View.VISIBLE);
                    StrAddressId = "";
                    intContainerStatus = Constants.YARD;
                    strLoadUnload = Integer.toString(intContainerStatus);
                    DropoffTV.setText("");

                }
            }
        });

        radioLoadUnloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioLoadUnloadbtn.isChecked()) {
                    StrAddressId = "";
                    spinUnloading.setVisibility(View.VISIBLE);
                    dropOffDetailsLay.setVisibility(View.GONE);
                    if(locationList.size() > 0) {
                        spinUnloading.setSelection(0);
                    }
                    spinYard.setVisibility(View.GONE);
                    DropoffTV.setVisibility(View.GONE);
                    if (radioLoadUnloadbtn.getText().toString().equals("Unloading")) {
                        intContainerStatus = Constants.UNLOADING;
                        strLoadUnload = Integer.toString(intContainerStatus);
                    } else {
                        intContainerStatus = Constants.LOADING;
                        strLoadUnload = Integer.toString(intContainerStatus);
                    }
                }
            }
        });

        radioDropoffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioDropoffbtn.isChecked()) {
                    dropOffDetailsLay.setVisibility(View.VISIBLE);
                    DropoffTV.setVisibility(View.VISIBLE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);

                    StrAddressId = StrDropoffId;
                    DropoffTV.setText(Html.fromHtml(dropOffLocationName +  "<br>" + dropOffAddress));
                    intContainerStatus = Constants.DROPOFF;
                    strLoadUnload = Integer.toString(intContainerStatus);
                }
            }
        });


        menuActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFragmentManager().popBackStack();
            }
        });



        ArrayAdapter TruckAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, loadType);
        TruckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        spinLoadType.setAdapter(TruckAdapter);
        spinLoadType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ((spinLoadType.getSelectedItem().equals("Loaded")) && (IntNextStatus == 6 || IntNextStatus == 7)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }else {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }
                } else if ((spinLoadType.getSelectedItem().equals("Partial Load")) && (IntNextStatus == 6 || IntNextStatus == 7)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        StrLoadtype = "2";
                    }else {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.GONE);
                        if (YardSelfAssignExport || YardAssignExport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        StrLoadtype = "2";
                    }

                } else if ((spinLoadType.getSelectedItem().equals("Empty")) && (IntNextStatus == 6 || IntNextStatus == 7)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);

                    if(JobType.equals("1")) {//Import
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "1";
                    }else if(JobType.equals("2")){
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        }
                        StrLoadtype = "1";
                    }
                }
                if ((spinLoadType.getSelectedItem().equals("Loaded")) && (IntNextStatus == 6 || IntNextStatus == 7)) {

                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }else {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }
                } else if ((spinLoadType.getSelectedItem().equals("Partial Load")) && (IntNextStatus == 6 || IntNextStatus == 7)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        StrLoadtype = "2";
                    }else {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.GONE);
                        if (YardSelfAssignExport || YardAssignExport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        StrLoadtype = "2";
                    }

                } else if ((spinLoadType.getSelectedItem().equals("Empty")) && (IntNextStatus == 6 || IntNextStatus == 7)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);

                    if(JobType.equals("1")) {//Import
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "1";
                    }else if(JobType.equals("2")){
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        }
                        StrLoadtype = "1";
                    }
                }


                //for 25 STATUS

                if ((spinLoadType.getSelectedItem().equals("Loaded")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }else {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }
                } else if ((spinLoadType.getSelectedItem().equals("Partial Load")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        StrLoadtype = "2";
                    }else {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.GONE);
                        if (YardSelfAssignExport || YardAssignExport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        StrLoadtype = "2";
                    }

                } else if ((spinLoadType.getSelectedItem().equals("Empty")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);

                    if(JobType.equals("1")) {//Import
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "1";
                    }else if(JobType.equals("2")){
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        }
                        StrLoadtype = "1";
                    }
                }
                if ((spinLoadType.getSelectedItem().equals("Loaded")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }else {
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "3";
                    }
                } else if ((spinLoadType.getSelectedItem().equals("Partial Load")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);
                    if(JobType.equals("1")) {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        StrLoadtype = "2";
                    }else {
                        radioSelctLocType.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.GONE);
                        if (YardSelfAssignExport || YardAssignExport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        StrLoadtype = "2";
                    }

                } else if ((spinLoadType.getSelectedItem().equals("Empty")) && (IntNextStatus == 25)) {
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                    spinUnloading.setVisibility(View.GONE);
                    spinYard.setVisibility(View.GONE);

                    if(JobType.equals("1")) {//Import
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else if(StrLegType.equals("1")){
                            radioSelctLocType.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            spinYard.setVisibility(View.GONE);
                            DropoffTV.setVisibility(View.GONE);
                            spinUnloading.setVisibility(View.GONE);
                        }else{
                            if (YardSelfAssignImport || YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }
                        StrLoadtype = "1";
                    }else if(JobType.equals("2")){
                        if(StrLegType.equals("3")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.GONE);
                        }else  if(StrLegType.equals("1")) {
                            radioSelctLocType.setVisibility(View.VISIBLE);
                            radioDropoffbtn.setVisibility(View.GONE);
                            if(YardSelfAssignExport || YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        }
                        StrLoadtype = "1";
                    }
                }

            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinYard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Arrays.asList(yardIdList).indexOf(position);
                if(position == 0){
                    StrAddressId = "";
                    DropoffTV.setVisibility(View.GONE);
                }else {
                    DropoffTV.setVisibility(View.VISIBLE);
                    StrAddressId = yardIdList.get(position);

                }
                DropoffTV.setText(yardAddressArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinUnloading.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Arrays.asList(locationIdList).indexOf(position);

                if(position == 0){
                    StrAddressId = "";
                }else {
                    StrAddressId = locationIdList.get(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinTruck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Arrays.asList(truckIdList).indexOf(position);
                StrTruckId = truckIdList.get(position);
                if(position == 0){
                    StrTruckId = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });
        spinChasis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Arrays.asList(chasisIdList).indexOf(position);
                StrChessieId = chasisIdList.get(position);

                if(position == 0){
                    StrChessieId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

        btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnDriverJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (JobType.equals("1")) {
                    isChessisValidate = ValidateChassisImport;

                }else{
                    isChessisValidate = ValidateChassisExport;

                }

                if(isChessisValidate){
                    if (StrChessieId.trim().equals("0")) {
                        StrChessieId = "";
                        spinChasis.setSelection(0);
                    }
                }else{
                    StrChessieId = ChasisNoEditTxt.getText().toString().trim();
                }



                if (StrTruckId.trim().equals("0")){
                    StrTruckId = "";
                    spinTruck.setSelection(0);

                }


                    if (StrAddressId.trim().equals("0")) {
                        StrAddressId = "";
                    }


                if (IntNextStatus == 1) {
                    if(StrChessieId.length() > 0) {
                        if(StrTruckId.length() > 0) {
                            AssignReqByUnAsin(driver_Id, StrContainerId, StrTruckId, StrChessieId);
                        }else{
                            Constants.showToastMsg(menuActionBar, "Select Truck first", Snackbar.LENGTH_LONG);
                        }
                    }else{
                        if(isChessisValidate) {
                            Constants.showToastMsg(menuActionBar, "Select Chessis first", Snackbar.LENGTH_LONG);
                        }else{
                            Constants.showToastMsg(menuActionBar, "Enter Chessis number", Snackbar.LENGTH_LONG);
                        }
                    }
                } else if (IntNextStatus == 2 || IntNextStatus == 3 || IntNextStatus == 9 || IntNextStatus == 12 ||
                        IntNextStatus == 13 || IntNextStatus == 17 || IntNextStatus == 20) {

                    String chessis = "";
                    if(IntNextStatus == 13 && !isChessisValidate){
                        StrChassiesNo = ChasisNoEditTxt.getText().toString().trim();
                        chessis = StrChassiesNo;
                    }else{
                        chessis = StrChessieId;
                    }

                    if (radioDropoffbtn.isChecked() || radioLoadUnloadbtn.isChecked() || radioYardbtn.isChecked()) {
                        if(chessis.length() > 0) {
                            if(StrTruckId.length() > 0) {
                                if (StrAddressId.length() > 0) {
                                    PickupReqByUnAsin(driver_Id, StrContainerId, StrTruckId, chessis, strLoadUnload, StrAddressId, StrLoadtype);
                                } else {
                                    Constants.showToastMsg(menuActionBar, "Select Location first", Snackbar.LENGTH_LONG);
                                }
                            }else{
                                Constants.showToastMsg(menuActionBar, "Select Truck first", Snackbar.LENGTH_LONG);
                            }
                        }else{
                            if((IntNextStatus == 7 || IntNextStatus == 13) && !isChessisValidate){
                                Constants.showToastMsg(menuActionBar, "Enter Chessis number", Snackbar.LENGTH_LONG);
                            }else {
                                Constants.showToastMsg(menuActionBar, "Select Chessis first", Snackbar.LENGTH_LONG);
                            }
                        }
                    } else {
                        Constants.showToastMsg(menuActionBar, "Select Location Type", Snackbar.LENGTH_LONG);
                    }

                } else if (IntNextStatus == 6 || IntNextStatus == 7 || IntNextStatus == 25) {

                    String chessis = "";
                    if(IntNextStatus == 7 && !isChessisValidate){
                        StrChassiesNo = ChasisNoEditTxt.getText().toString().trim();
                        chessis = StrChassiesNo;
                    }else{
                        chessis = StrChessieId;
                    }

                    if ((spinLoadType.getSelectedItem().equals("Select Load"))) {
                        Constants.showToastMsg(menuActionBar, "Select Load Type", Snackbar.LENGTH_LONG);
                    } else {
                        if (chessis.length() > 0) {
                            if (StrTruckId.length() > 0) {
                                if (StrAddressId.length() > 0) {
                                    if (radioDropoffbtn.isChecked() || radioLoadUnloadbtn.isChecked() || radioYardbtn.isChecked()) {
                                        PickupReqByUnAsin(driver_Id, StrContainerId, StrTruckId, chessis, strLoadUnload, StrAddressId, StrLoadtype);
                                    } else {
                                        Constants.showToastMsg(menuActionBar, "Select Location Type", Snackbar.LENGTH_LONG);
                                    }
                                    } else {
                                    Constants.showToastMsg(menuActionBar, "Select Location first", Snackbar.LENGTH_LONG);
                                }
                            } else {
                                Constants.showToastMsg(menuActionBar, "Select Truck first", Snackbar.LENGTH_LONG);
                            }
                        } else {
                            if(IntNextStatus == 7 && !isChessisValidate){
                                Constants.showToastMsg(menuActionBar, "Enter Chessis number", Snackbar.LENGTH_LONG);
                            }else {
                                Constants.showToastMsg(menuActionBar, "Select Chessis first", Snackbar.LENGTH_LONG);
                            }
                        }
                    }
                }
            }
        });

       // Constants.gps = new GPSTracker(getActivity());
        bundle = this.getArguments();
        if (bundle != null) {

            ContainerNo = bundle.getString("contNo");
            StrContainerId = bundle.getString("StrContainerId");
            JobType = bundle.getString("jobType");
            isSelfAsign = bundle.getBoolean("Is_Self_Location");
            position    = bundle.getInt("position");

            if(!StrContainerId.equalsIgnoreCase("null") && StrContainerId.length() > 0) {
                GET_CONTAINER_DETAIL(StrContainerId, JobType);
            }

            if(isSelfAsign)
                GET_UNASSIGN_CONTAINER(CompanyID,JobType,ContainerNo);
        }


        }

    void setTextOnView(String text, TextView view) {
        if (!text.equalsIgnoreCase("null")) {
            view.setText(text);
        }
    }

    void setTextOnEditView(String text, EditText view) {
        if (!text.equalsIgnoreCase("null")) {
            view.setText(text);
        }
    }


    void SetUnAssignDetails() {

        if(containerUnAssignList.size() > position) {
            StrContainerId   = String.valueOf(containerUnAssignList.get(position).getContainerId());
            StrContainerSize = containerUnAssignList.get(position).getSize();
            StrContainerType = containerUnAssignList.get(position).getContainerType();
            StrGrosswtitleTV = containerUnAssignList.get(position).getGrossWeight();
            StrContainerNo   = containerUnAssignList.get(position).getContainerNo();
            StrCompanyId     = containerUnAssignList.get(position).getCompanyId();
            IntNextStatus    = containerUnAssignList.get(position).getStatus();
            StrTruckId       = containerUnAssignList.get(position).getTruckId();
            StrChessieId     = containerUnAssignList.get(position).getChessieId();
            StrTruckNo       = containerUnAssignList.get(position).getTrcukName();
            StrChassiesNo    = containerUnAssignList.get(position).getChessieName();
            Log.d("Chessie", "ChessieId" + StrChessieId + "  ChassiesNo: " + StrChassiesNo);
            StrContainerLoadType = containerUnAssignList.get(position).getContainerLoadTypeID();
            StrLegType       = containerUnAssignList.get(position).getLegType();

        }

        SetTextOnViews();
        GET_TRUCK_CHESIS(isLoad, StrCompanyId, StrContainerId, driver_Id);
        GetCompanySetting(StrCompanyId);

        handleSelfAsignContainerStatus();
    }



    void SetExportselfAssignLocation() {

        if(containerDetailList.size() > position) {
            StrContainerId      = String.valueOf(containerDetailList.get(position).getContainerId());
            StrContainerSize    = containerDetailList.get(position).getSize();
            StrContainerType    = containerDetailList.get(position).getContainerType();
            StrGrosswtitleTV    = containerDetailList.get(position).getGrossWeight();
            StrContainerNo      = containerDetailList.get(position).getContainerNo();
            StrCompanyId        = containerDetailList.get(position).getCompanyId();
            IntNextStatus       = containerDetailList.get(position).getStatus();
            StrTruckId          = containerDetailList.get(position).getTruckId();
            StrChessieId        = containerDetailList.get(position).getChessieId();
            StrTruckNo          = containerDetailList.get(position).getTrcukName();
            StrChassiesNo       = containerDetailList.get(position).getChessieName();
            Log.d("Chessie", "ChessieId" + StrChessieId + "  ChassiesNo: " + StrChassiesNo);
            StrContainerLoadType = containerDetailList.get(position).getContainerLoadTypeID();
            StrLegType          = containerDetailList.get(position).getLegType();
        }
        /*-------------- Set Text on TextView -------------------*/
        SetTextOnViews();
        GET_TRUCK_CHESIS(isLoad, StrCompanyId, StrContainerId, driver_Id);
        GetCompanySetting(StrCompanyId);
        handleSelfAsignContainerStatus();

    }


    void SetTextOnViews() {
        setTextOnView(StrContainerNo, containerIdTV);
        if(!StrContainerSize.equalsIgnoreCase("null") && !StrContainerSize.equalsIgnoreCase("N/A")){
            setTextOnView(StrContainerSize, SizeTV);
        }
        if(!StrContainerType.equalsIgnoreCase("null") && !StrContainerType.equalsIgnoreCase("N/A")){
            if(StrContainerTypeDetail.length() > 0){
                setTextOnView(StrContainerType + "/" + StrContainerTypeDetail  , containerTypeIdTV);
            }else {
                setTextOnView(StrContainerType, containerTypeIdTV);
            }
        }
        setTextOnView(StrGrosswtitleTV, grosswtTV);
        setTextOnView(StrChassiesNo, chassiesNoTV);
        setTextOnEditView(StrChassiesNo, ChasisNoEditTxt);
        setTextOnView(StrTruckNo, truckNoTV);



        if(StrContainerLoadType.equals("1")){
            ContainerLoadTypeTV.setText("Empty");


        }else if(StrContainerLoadType.equals("2")){

            ContainerLoadTypeTV.setText("Partial Load");
        }
        else {
            ContainerLoadTypeTV.setText("Loaded");
        }

        if (JobType.equals("1")) {

            jobtypeTV.setText("Import");
            contInfoTitleTV.setText("Self Assign");
            radioLoadUnloadbtn.setText("Unloading");


        } else {
            jobtypeTV.setText("Export");
            contInfoTitleTV.setText("Self Assign");
            radioLoadUnloadbtn.setText("Loading");
        }
    }


    void handleSelfAsignContainerStatus(){
        radioLayout.setVisibility(View.VISIBLE);
        ChasisNoEditTxt.setVisibility(View.GONE);

        if(IntNextStatus== 18){
            IntNextStatus = 17;
        }

        switch (IntNextStatus){

            /* ------ New Location -------*/
            case 1:

                radioLayout.setVisibility(View.GONE);
              //  locationTypeCard.setVisibility(View.GONE);


                break;

            /* ------ Assign Location -------*/
            case 2:
                radioLayout.setVisibility(View.GONE);
                radioDropoffbtn.setVisibility(View.GONE);
               // locationTypeCard.setVisibility(View.GONE);
                break;

            /* ------ PickUp Location -------*/
            case 3:
                if(JobType.equals("1")) {
                    if (YardSelfAssignImport || YardAssignImport) {
                        radioYardbtn.setVisibility(View.VISIBLE);
                    } else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                }else {

                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                }
                radioDropoffbtn.setVisibility(View.GONE);

                break;

            case 9:
                if(JobType.equals("1")) {
                    if (YardSelfAssignImport || YardAssignImport) {
                        radioYardbtn.setVisibility(View.VISIBLE);
                    } else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                }else {

                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                }
                radioDropoffbtn.setVisibility(View.GONE);

                break;

            case 6:

                if(JobType.equals("1")) {
                    if (StrContainerLoadType.equals("1")) {
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);

                    } else if (StrContainerLoadType.equals("2")) {
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }

                        radioDropoffbtn.setVisibility(View.GONE);

                    } else if (StrContainerLoadType.equals("3")){
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                    }
                }else if (StrContainerLoadType.equals("1")) {
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioDropoffbtn.setVisibility(View.GONE);

                } else if (StrContainerLoadType.equals("2")) {
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.GONE);
                } else if (StrContainerLoadType.equals("3")) {
                    radioLoadUnloadbtn.setVisibility(View.GONE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                }
                break;

            case 7:
                if(JobType.equals("1")) {
                    if (StrContainerLoadType.equals("1")) {
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioLoadUnloadbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);

                    } else if (StrContainerLoadType.equals("2")) {

                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                    } else if (StrContainerLoadType.equals("3")) {
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                    }
                } else if (StrContainerLoadType.equals("1")) {
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioDropoffbtn.setVisibility(View.GONE);

                } else if (StrContainerLoadType.equals("2")) {
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.GONE);
                }  else if (StrContainerLoadType.equals("3")) {
                    radioLoadUnloadbtn.setVisibility(View.GONE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                }

                if(isChessisValidate){
                    spinChasis.setVisibility(View.VISIBLE);
                    chassiesNoTV.setVisibility(View.GONE);
                }else{
                    ChasisNoEditTxt.setVisibility(View.VISIBLE);
                    chassiesNoTV.setVisibility(View.GONE);
                }

                break;

            /* ------ Yard Location -------*/
            case 12:

                if(JobType.equals("1")){
                    if (StrContainerLoadType.equals("1")) {

                        radioDropoffbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioLoadUnloadbtn.setVisibility(View.GONE);

                    } else if (StrContainerLoadType.equals("2")) {

                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.GONE);
                    } else if (StrContainerLoadType.equals("3")){
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.GONE);
                    }
                }else  if (StrContainerLoadType.equals("1")) {

                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioDropoffbtn.setVisibility(View.GONE);

                } else if (StrContainerLoadType.equals("2")) {

                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioDropoffbtn.setVisibility(View.GONE);
                } else if (StrContainerLoadType.equals("3")){
                    radioDropoffbtn.setVisibility(View.VISIBLE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.GONE);
                }

                break;

            case 13:
                if(JobType.equals("1")) {
                    if (StrContainerLoadType.equals("3")) {

                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.GONE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);

                    } else if (StrContainerLoadType.equals("1")) {

                        radioDropoffbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioLoadUnloadbtn.setVisibility(View.GONE);

                    } else if (StrContainerLoadType.equals("2")) {

                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.GONE);
                    }
                } else if(StrContainerLoadType.equals("3")) {

                    radioDropoffbtn.setVisibility(View.VISIBLE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.GONE);


                } else if (StrContainerLoadType.equals("1")) {

                    radioYardbtn.setVisibility(View.GONE);
                    radioDropoffbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);

                } else if (StrContainerLoadType.equals("2")) {

                    radioYardbtn.setVisibility(View.GONE);
                    radioDropoffbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                }

                if(isChessisValidate){
                    spinChasis.setVisibility(View.VISIBLE);
                    chassiesNoTV.setVisibility(View.GONE);
                }else{
                    ChasisNoEditTxt.setVisibility(View.VISIBLE);
                    chassiesNoTV.setVisibility(View.GONE);
                }

                break;

            /* ------ Live Loading Done-------*/
            case 17:
                if(JobType.equals("1")) {   // Import

                        if(StrContainerLoadType.equals("3")) { // Loaded
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            radioDropoffbtn.setVisibility(View.GONE);

                            if (YardAssignExport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }


                    }else if(StrContainerLoadType.equals("2")){ // partial Loaded
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.GONE);

                            if (YardSelfAssignImport || YardAssignImport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }

                    }else  {    // Empty
                        if (StrLegType.equals("3") || StrLegType.equals("2")) {
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardAssignImport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else{
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                    }


                }else { // Export

                    if(StrContainerLoadType.equals("1")) {
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if(YardAssignExport){
                            radioYardbtn.setVisibility(View.VISIBLE);
                        }else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);

                    }else if(StrContainerLoadType.equals("2")){
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if(YardAssignExport){
                            radioYardbtn.setVisibility(View.VISIBLE);
                        }else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);

                    }else if(StrContainerLoadType.equals("3")) {
                        if (StrLegType.equals("3") || StrLegType.equals("2")) {
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if(YardAssignExport){
                                radioYardbtn.setVisibility(View.VISIBLE);
                            }else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else{
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                    }
                }
                break;


            /* ------ Pick From Yard Location -------*/
            case 20:

                if(JobType.equals("1")) {
                    if (StrContainerLoadType.equals("1")) {
                        if (StrLegType.equals("3") || StrLegType.equals("2")) {
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            if (YardAssignExport) {
                                radioYardbtn.setVisibility(View.VISIBLE);
                            } else {
                                radioYardbtn.setVisibility(View.GONE);
                            }
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                        }else{
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                            radioYardbtn.setVisibility(View.GONE);
                            radioDropoffbtn.setVisibility(View.GONE);
                        }
                    } else if (StrContainerLoadType.equals("2")) {
                        radioDropoffbtn.setVisibility(View.GONE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        radioYardbtn.setVisibility(View.GONE);

                    } else if (StrContainerLoadType.equals("3")) {
                        radioDropoffbtn.setVisibility(View.GONE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    }
                }else if(StrContainerLoadType.equals("1")) {
                    radioDropoffbtn.setVisibility(View.GONE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);

                }else if(StrContainerLoadType.equals("2")){
                    radioDropoffbtn.setVisibility(View.GONE);
                    radioYardbtn.setVisibility(View.GONE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);

                }else if(StrContainerLoadType.equals("3")) {
                    if (StrLegType.equals("3") || StrLegType.equals("2")) {
                        radioLoadUnloadbtn.setVisibility(View.GONE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.VISIBLE);
                    }else{
                        radioLoadUnloadbtn.setVisibility(View.GONE);
                        radioYardbtn.setVisibility(View.GONE);
                        radioDropoffbtn.setVisibility(View.GONE);
                    }
                }
                break;

            case 25:

                if(JobType.equals("1")) {
                    if (StrContainerLoadType.equals("1")) {
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }

                        radioLoadUnloadbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                        radioDropoffbtn.setVisibility(View.VISIBLE);

                    } else if (StrContainerLoadType.equals("2")) {
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }

                        radioDropoffbtn.setVisibility(View.GONE);

                    } else if (StrContainerLoadType.equals("3")){
                        radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                        if (YardSelfAssignImport || YardAssignImport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        } else {
                            radioYardbtn.setVisibility(View.GONE);
                        }
                        radioDropoffbtn.setVisibility(View.GONE);
                        LoadTypeLayout.setVisibility(View.VISIBLE);
                    }
                }else if (StrContainerLoadType.equals("1")) {
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioDropoffbtn.setVisibility(View.GONE);

                } else if (StrContainerLoadType.equals("2")) {
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                    radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.GONE);
                } else if (StrContainerLoadType.equals("3")) {
                    radioLoadUnloadbtn.setVisibility(View.GONE);
                    if(YardSelfAssignExport || YardAssignExport){
                        radioYardbtn.setVisibility(View.VISIBLE);
                    }else {
                        radioYardbtn.setVisibility(View.GONE);
                    }
                    radioDropoffbtn.setVisibility(View.VISIBLE);
                    LoadTypeLayout.setVisibility(View.VISIBLE);
                }
                break;


                default:
                    radioYardbtn.setVisibility(View.GONE);

                    if(JobType.equals(Constants.IMPORT_)) {
                        if(StrLoadtype.equals(Constants.Loaded) || StrLoadtype.equals(Constants.PartialLoad)){
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            radioYardbtn.setVisibility(View.VISIBLE);
                        }else{
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }

                        if(YardAssignImport)
                            radioYardbtn.setVisibility(View.VISIBLE);

                    }else{
                        if(StrLoadtype.equals(Constants.Empty) || StrLoadtype.equals(Constants.PartialLoad)){
                            radioDropoffbtn.setVisibility(View.GONE);
                            radioLoadUnloadbtn.setVisibility(View.VISIBLE);
                            radioYardbtn.setVisibility(View.VISIBLE);
                        }else{
                            radioDropoffbtn.setVisibility(View.VISIBLE);
                            radioLoadUnloadbtn.setVisibility(View.GONE);
                        }

                        if(YardAssignExport) {
                            radioYardbtn.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

        }



        if(!isChessisValidate){
            spinChasis.setVisibility(View.GONE);

            if(StrChassiesNo.length() > 0){
                chassiesNoTV.setVisibility(View.VISIBLE);
            }else {
                chassiesNoTV.setVisibility(View.GONE);
                ChasisNoEditTxt.setVisibility(View.VISIBLE);
            }
        }else{
            spinChasis.setVisibility(View.VISIBLE);
            ChasisNoEditTxt.setVisibility(View.GONE);
        }



    }



    void GET_TRUCK_CHESIS(boolean IsLoaderStatus, final String StrCompanyId, final String StrContainerId, final String driver_Id) {

        try{
            if(getActivity() != null) {


                if (IsLoaderStatus) {
                    progressBarHome.setVisibility(View.VISIBLE);
                }
                StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_DRIVER_TRUCK_CHASSI,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject obj, resultJson;

                                JSONObject ThelstResultObj;
                                JSONArray TruckJsonArray;
                                JSONArray ChasisJsonArray;
                                JSONArray YardJsonArray;
                                JSONArray LocationJsonArray;
                                JSONObject dropoffJsonArray;

                                Log.d("Response ", ">>>Response: " + response);
                                progressBarHome.setVisibility(View.GONE);
                                try {
                                    obj = new JSONObject(response);

                                    if (obj.getInt("Status") == 1) {
                                        if (obj.getBoolean("Success") == true) {

                                            ThelstResultObj = new JSONObject(obj.getString("lstResult"));

                                            truckList = new ArrayList<>();
                                            chasisList = new ArrayList<>();
                                            truckIdList = new ArrayList<>();
                                            chasisIdList = new ArrayList<>();
                                            yardList = new ArrayList<>();
                                            yardIdList = new ArrayList<>();
                                            locationList = new ArrayList<>();
                                            locationIdList = new ArrayList<>();


                                            TruckJsonArray = (JSONArray) ThelstResultObj.get("Trucks");
                                            ChasisJsonArray = (JSONArray) ThelstResultObj.get("Chassis");
                                            YardJsonArray = (JSONArray) ThelstResultObj.get("Yards");
                                            LocationJsonArray = (JSONArray) ThelstResultObj.get("Locations");
                                            dropoffJsonArray = (JSONObject) ThelstResultObj.get("DropOffLocations");
                                            yardAddressArray = new ArrayList<>();

                                            yardIdList.add("");
                                            yardList.add("Select Yard");
                                            yardAddressArray.add("");

                                            for (int i = 0; i < YardJsonArray.length(); i++) {
                                                JSONObject objs = (JSONObject) YardJsonArray.get(i);

                                                StrYardId = objs.getString("YardId");
                                                yardAddressArray.add(objs.getString("Address"));
                                                String YardName = objs.getString("YardLocationName");
                                                yardList.add(YardName);
                                                yardIdList.add(StrYardId);
                                            }

                                            try {
                                                ArrayAdapter YardAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, yardList);
                                                YardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinYard.setAdapter(YardAdapter);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            locationIdList.add("");
                                            locationList.add("Select Location");
                                            for (int i = 0; i < LocationJsonArray.length(); i++) {
                                                JSONObject objs = (JSONObject) LocationJsonArray.get(i);

                                                StrLocationId = objs.getString("LocationId");
                                                String LocationName = objs.getString("LocationName");
                                                locationList.add(LocationName);
                                                locationIdList.add(StrLocationId);
                                            }

                                            try {
                                                ArrayAdapter LocationAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, locationList);
                                                LocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinUnloading.setAdapter(LocationAdapter);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (StrTruckNo.length() > 0 && !StrTruckNo.equals("null")) {
                                                spinTruck.setVisibility(View.GONE);
                                                truckNoTV.setVisibility(View.VISIBLE);
                                            } else {

                                                spinTruck.setVisibility(View.VISIBLE);
                                                truckNoTV.setVisibility(View.GONE);
                                                truckIdList.add("");
                                                truckList.add("Select");

                                                for (int i = 0; i < TruckJsonArray.length(); i++) {
                                                    JSONObject objs = (JSONObject) TruckJsonArray.get(i);

                                                    StrTrucksId = objs.getString("TruckId");
                                                    String TEquipmentNumber = objs.getString("EquipmentNumber");

                                                    truckList.add(TEquipmentNumber);
                                                    truckIdList.add(StrTrucksId);
                                                }

                                                try {
                                                    ArrayAdapter TruckAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, truckList);
                                                    TruckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    //Setting the ArrayAdapter data on the Spinner
                                                    spinTruck.setAdapter(TruckAdapter);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (StrChassiesNo.length() > 0 ) {  //&& !StrChassiesNo.equals("null")

                                                spinChasis.setVisibility(View.GONE);

                                                if((IntNextStatus == 7 || IntNextStatus == 13) && !isChessisValidate) {
                                                    chassiesNoTV.setVisibility(View.GONE);
                                                }else{
                                                    if(isChessisValidate) {
                                                        chassiesNoTV.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            } else {



                                                if((IntNextStatus == 7 || IntNextStatus == 13) && !isChessisValidate) {
                                                    chassiesNoTV.setVisibility(View.GONE);
                                                    spinChasis.setVisibility(View.GONE);
                                                }else{
                                                    if(isChessisValidate) {
                                                        chassiesNoTV.setVisibility(View.VISIBLE);
                                                        spinChasis.setVisibility(View.VISIBLE);
                                                    }
                                                }


                                                chasisList.add("Select");
                                                chasisIdList.add("");

                                                for (int i = 0; i < ChasisJsonArray.length(); i++) {
                                                    JSONObject objs = (JSONObject) ChasisJsonArray.get(i);

                                                    StrChessiesId = objs.getString("ChessieId");
                                                    String EEquipmentNumber = objs.getString("EquipmentNumber");
                                                    chasisList.add(EEquipmentNumber);
                                                    chasisIdList.add(StrChessiesId);
                                                }


                                                ArrayAdapter ChasisAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, chasisList);
                                                ChasisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinChasis.setAdapter(ChasisAdapter);
                                            }

                                            StrDropoffId = dropoffJsonArray.getString("LocationId");

                                            dropOffLocationName = dropoffJsonArray.getString("LocationName");
                                            dropOffAddress      = dropoffJsonArray.getString("Address");
                                            DropoffTV.setText(Html.fromHtml(dropOffLocationName +  "<br>" + dropOffAddress));

                                            String StrPowerDefaultTruck = ThelstResultObj.getString("PowerDefaultTruck");

                                            if (truckIdList.contains(StrPowerDefaultTruck)) {
                                                int truckposi = (truckIdList.indexOf(StrPowerDefaultTruck));
                                                spinTruck.setSelection(truckposi);
                                                StrTruckId = StrPowerDefaultTruck;
                                            }

                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Constants.showToastMsg(menuActionBar, "Please check your internet connection", VIEW_DURATION);
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("CompanyId", StrCompanyId);
                        params.put("ContainerId", StrContainerId);
                        params.put("DriverId", driver_Id);

                        params.put("DeviceID", DeviceID);
                        params.put("LoginId", LoginId);
                        params.put("Latitude", Constants.CURRENT_LATITUDE);
                        params.put("Longitude", Constants.CURRENT_LONGITUDE);

                        return params;
                    }
                };

                RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                postRequest.setRetryPolicy(policy);
                getTruckChessisReq.add(postRequest);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void AssignReqByUnAsin(final String driverId,final String containerId,final String TruckId,final String ChessieId){


        progressBarHome.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPDATE_SELFASSIGN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarHome.setVisibility(View.GONE);
                        JSONObject obj;

                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){

                                    Constants.showToastMsg(menuActionBar, "Assign successfully.", Snackbar.LENGTH_LONG);
                                    if(getFragmentManager().getBackStackEntryCount() > 1){
                                        getFragmentManager().popBackStack();
                                        TabActivity.host.setCurrentTab(0);
                                    }

                                }else{
                                    String msg = obj.getString("Result");
                                    Constants.showToastMsg(menuActionBar, msg, Snackbar.LENGTH_LONG);
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
                        Constants.showToastMsg(menuActionBar, "Please check your internet connection", VIEW_DURATION);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", driverId);
                params.put("ContainerId", containerId);
                params.put("TruckId", TruckId);

                if(isChessisValidate){
                    params.put("ChessieId", ChessieId);
                }else{
                    params.put("ChessieName", ChessieId);
                }
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyID);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        selfAssignReq.add(postRequest);

    }

    void PickupReqByUnAsin(final String driverId,final String containerId,final String TruckId,final String ChessieId,
                           final String nextLocationType,final String nextLocationid,final String loadType){

        btnDriverJob.setEnabled(false);
        progressBarHome.setVisibility(View.VISIBLE);


        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPDATE_SELFASSIGN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarHome.setVisibility(View.GONE);
                        btnDriverJob.setEnabled(true);
                        JSONObject obj;

                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){

                                    Constants.showToastMsg(menuActionBar, "Assign successfully.", Snackbar.LENGTH_LONG);
                                    if(getFragmentManager().getBackStackEntryCount() > 1){
                                        Constants.CONTAINER_LIST_TYPE = String.valueOf(Constants.EXPORT);
                                        ExportFragment.IS_SELF_ASIGN = false;
                                        ExportFragment.IS_SELF_PICKEDUP = true;

                                        getFragmentManager().popBackStack();
                                        TabActivity.host.setCurrentTab(0);
                                    }
                                //    TabActivity.host.setCurrentTab(0);
                                }else{
                                    String msg = obj.getString("Result");
                                    Constants.showToastMsg(menuActionBar, msg, Snackbar.LENGTH_LONG);
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
                        Log.d("error", "error: " + error.toString());
                        btnDriverJob.setEnabled(true);
                        Constants.showToastMsg(menuActionBar, "Please check your internet connection", VIEW_DURATION);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("DriverId", driverId);
                params.put("ContainerId", containerId);
                params.put("TruckId", TruckId);


                if(isChessisValidate) {
                    params.put("ChessieId", StrChessieId);
                }else {
                    if(IntNextStatus == 7 || IntNextStatus == 13){
                        params.put("ChessieName", ChessieId);
                    }else {
                        params.put("ChessieName", StrChassiesNo);
                    }
                }

                params.put("NextLocationType", nextLocationType);
                params.put("NextLocationId", nextLocationid);
                params.put("LoadType", loadType);

                params.put("DriverID", DriverID);
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyID);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        pickUpReq.add(postRequest);

    }



    void GetCompanySetting(final String StrCompanyId) {

        // progressBar.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.Get_Company_Setting,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response ", ">>>Response: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getInt("Status") == 1) {
                                if (obj.getBoolean("Success") == true) {

                                    JSONArray valueJsonArray = new JSONArray(obj.getString("lstResult"));

                                    for (int i = 0; i < valueJsonArray.length(); i++) {
                                        JSONObject objs = (JSONObject) valueJsonArray.get(i);
                                        String Value = objs.getString("Value");
                                        String setingName = objs.getString("SettingName");

                                        if (setingName.equals("IsYardSelfAssignAllowedImport") ) {
                                            if (Value.equals("true")) {   // && OrderType.equals("1")
                                                YardAssignImport = true;
                                            } else {
                                                YardAssignImport = false;
                                            }
                                        }
                                        if (setingName.equals("IsYardSelfAssignAllowedExport") ) {
                                            if (Value.equals("true")) {   // && OrderType.equals("1")
                                                YardAssignExport = true;
                                            } else {
                                                YardAssignExport = false;
                                            }
                                        }
                                        if (setingName.equals("ValidateChassisImport")) {
                                            if (Value.equals("true")) {   // && OrderType.equals("1")
                                                ValidateChassisImport = true;
                                            } else {
                                                ValidateChassisImport = false;
                                            }
                                        }
                                        if (setingName.equals("ValidateChassisExport")) {
                                            if (Value.equals("true")) {   // && OrderType.equals("1")
                                                ValidateChassisExport = true;
                                            } else {
                                                ValidateChassisExport = false;
                                            }
                                        }

                                    }


                                    // 15 july changes code
                                    if (JobType.equals("1")) {
                                        isChessisValidate = ValidateChassisImport;

                                    }else{
                                        isChessisValidate = ValidateChassisExport;

                                    }

                                    handleSelfAsignContainerStatus();

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.showToastMsg(menuActionBar, "Please check your internet connection", VIEW_DURATION);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("CompanyId", StrCompanyId);
                params.put("DriverID", DriverID);
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getCompSettingReq.add(postRequest);

    }

    void GET_CONTAINER_DETAIL(final String ContainerId, final String OrderType){

        progressBarHome.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_CONTAINER_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response: " + response);

                        progressBarHome.setVisibility(View.GONE);
                        JSONObject obj, resultJson;
                        int status ;
                        try {
                            obj = new JSONObject(response);
                            status = obj.getInt("Status");

                            if(status == 1) {
                                StrChessieId = ""; StrTruckId = "";

                                resultJson = new JSONObject(obj.getString("Result"));
                                containerDetailList = new ArrayList<ContainerModel>();

                                StrContainerId =  resultJson.getString("ContainerId");
                                StrLoadtype   =  resultJson.getString("ContainerLoadTypeID");
                                StrCompanyId  = resultJson.getString("CompanyId");
                                IntNextStatus = resultJson.getInt("Status");


                                StrContainerSize = constants.checkNullString(resultJson, "Size");
                                StrContainerType = constants.checkNullString(resultJson, "ContainerType");
                                StrGrosswtitleTV = constants.checkNullString(resultJson, "GrossWeight") ;
                                StrContainerNo   = constants.checkNullString(resultJson, "ContainerNo");
                                StrCompanyId     = constants.checkNullString(resultJson, "CompanyId");
                                StrTruckId       = constants.checkNullString(resultJson, "TruckId");
                                StrChessieId     = constants.checkNullString(resultJson, "ChessieId");
                                StrTruckNo       = constants.checkNullString(resultJson, "TrcukName");
                                StrChassiesNo    = constants.checkNullString(resultJson, "ChessieName");
                                Log.d("Chessie", "ChessieId" + StrChessieId + "  ChassiesNo: " + StrChassiesNo);

                                StrContainerLoadType = constants.checkNullString(resultJson, "ContainerLoadTypeID");
                                StrLegType       = constants.checkNullString(resultJson, "LegType");
                                StrContainerTypeDetail = constants.checkNullString(resultJson, "ContainerTypeDetail");

                                String reservationDate = constants.checkNullString(resultJson,"DropOffReservationDate");
                                String[] reservationDateArray = reservationDate.split("T");

                                if(reservationDateArray.length > 0){
                                    resDateTV.setText(reservationDateArray[0]);
                                }
                                resNumberTV.setText(constants.checkNullString(resultJson, "DropOffReservationNo") );

                                resTimeTV.setText(constants.checkNullString(resultJson,"DropOffReservationTime"));

                                /*---------------- PARSE JSON of Container Details-----------------*/
                                try {
                                    int containerStatus = resultJson.getInt("Status");
                                    containerModel = Constants.containerModel(containerModel, resultJson, containerStatus, false, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                containerDetailList.add(containerModel);

                                if (containerDetailList.size() > 0) {
                                    SetExportselfAssignLocation();
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
                        progressBarHome.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String, String>();
                params.put("ContainerId", ContainerId);
                params.put("OrderType", OrderType);
                params.put("DriverID", DriverID);
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyID);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getContDetailsReq.add(postRequest);

    }

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

                            if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){
                                    containerUnAssignList = new ArrayList<ContainerModel>();
                                    resultJsonArray = new JSONArray(obj.getString("lstResult"));

                                    for (int i = 0 ; i < resultJsonArray.length() ; i++){
                                        resultJson = (JSONObject)resultJsonArray.get(i);
                                        int status = 0;
                                        if(resultJson.getString("Status") != null &&
                                                !resultJson.getString("Status").equalsIgnoreCase("null") ){
                                            status = resultJson.getInt("Status");

                                            /**/
                                            /*---------------- PARSE JSON of Container Details Array-----------------*/
                                            try {
                                                containerModel = Constants.containerModel(containerModel, resultJson, status, false, false);
                                                containerUnAssignList.add(containerModel);
                                                Log.d("containerModel", "containerModel: " + containerModel);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    if (containerUnAssignList.size() > 0) {
                                        SetUnAssignDetails();
                                    }

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
                        progressBarHome.setVisibility(View.GONE);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<String, String>();
                // params.put("DriverId", DriverId);
                params.put("OrderType", type);
                params.put("CompanyId", companyId);
                params.put("ContainerNo", containerNo);

                params.put("DriverID", DriverID);
                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        getUnAssignContReq.add(postRequest);

    }



}



