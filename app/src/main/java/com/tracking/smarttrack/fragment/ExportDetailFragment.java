package com.tracking.smarttrack.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.customfont.ErrorPopupHelper;
import com.google.android.gms.maps.model.LatLng;
import com.models.ContainerModel;
import com.models.SubLocationModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.constants.LegDialog;
import com.tracking.constants.LocServices;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.MediaActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ExportDetailFragment extends Fragment implements View.OnClickListener{


    View rootView;
    Bundle savedInstanceStateBundle;
    TextView container_name_tv, originAddressTV, destAddressTV, actionBarTitleTV, container_type_tv;
    TextView loadTypeTxtVw, appointmentTV, contactTV, truckNoTV, chassiesNoTV, serial_no_detail_tv, uploadImgTV, hiddenUploadView,
            shippingLineAddTV, nameTitleTV, ReseveDateTV,noteTV,remarknoteV, closeMsgView;
    TextView billToTV, refNoTV, msgTV,ConfirmTitleTV,TareWtTitleTV, poNumberTV, inspReqTxtVw; // originAddressTimeTV, destAddressTimeTV;
    TextView originDropDateTV, originResNumTV, destDropDateTV, destResNumTV;    //destDropTimeTV
    EditText contnrNoEditText, tareWeightEditText,remarkEditText;
    TextInputLayout tareWeightInputType;
   // LinearLayout originAddLay1, originAddLay2, destAddLay1, destAddLay2;
    RelativeLayout menuActionBar, notificationLay, jobDetailLay, ActionbarDetail, editFieldView, layoutMain,editFieldTareView,
            updateChessisBtn, uploadMediaLay;
    public static RelativeLayout searchActionBar;
    LinearLayout contactLay, container_bar_layout,topliner, hideKeyboardLay;
    CardView framAnimMsg;
    ProgressBar progressBar;
    Button btnDriverJob, btnCancelJob;
    ImageView menuImgBtn, mapListBtn, contactBtn, uploadImgBtn, editChessisBtn,remarkBtn;
    String  StrContainerName = "", StrContainerType = "", StrContainerTypeDetail = "", CONTAINER_JOB_TYPE = "", URL = "",StrRemarks = "",StrGetRemarks = "",StrIsUpdateRequest = "false",
            StrContactNo = "", StrDateTime = "", StrContainerSize = "", StrTruckNo = "", StrChassiesNo = "",
            StrContainerId = "", StrNextStatus = "", StrMessage = "", StrBillTo = "", StrRefNo = "", ContainerLoadTypeID = "", StrMessageText = "",
            StrCompanyId = "";
    String TareWeight = "",  ContainerNo = "", container_Sr_No = "",
             imagePath = "", StrOriginAddress = "", StrDestAddress = "",
            StrResNo = "", StrDropOffResNo = "", StrShipingLineAdd = "";
    String StrDateFormat = "", StrSealNumber = "", StrGrossWeight = "", StrContainerLoadType = "0", StrPONumber = "", ConditionStatus = "";
    String pickResNo = "", pickResDate = "", pickResTime = "", dropResNo = "", dropResDate = "", dropResTime = "";

    int intDestStatus , intOriginStatus , intContainerStatus,intLegType , IntTotalLocations;
    double OriginLat = 0.0, OriginLong=0.0, DestLat=0.0, DestLong=0.0;
    boolean IsReadStatus = false , IsPermissionForDeadCall = false,IsLoadLocationLeft = false,isupdateChessisBtn=false,
            IsImageUploaded = false,IsStreetReturned, legDialogfromYard = false;
    private boolean isOpen = false;
    public boolean ImportPickUpImage = false, ImportDropOffImage = false, ImportUnloadingImage = false, ExportPickUpImage = false,
            ExportDropOffImage = false, isLoadingImageRequired = false, ExportLoadingImage = false, TareWeightRequiredImport = false, TareWeightRequiredExport = false,
            SealRequiredImport = false, SealRequiredExport = false, GrossWeightRequiredImport = false,GrossWeightRequiredExport = false,
             ShowLiveLoadingImport = false , showLiveLoadUnLoad = false, ShowLiveLoadingExport =false,
            CancelOrderImport = false, CancelOrderExport = false;
    public static boolean YardSelfAssignAllowedImport = false, YardSelfAssignAllowedExport = false;
    String LoginId ="";
    String DriverID ="";
    String DeviceID = "";
    String CompanyID = "";
    String selectedDocType = "";
    String TruckId = "";

    private int VIEW_DURATION = 8000;

    int Camera = 101, Gallery = 102, itemPosition = 0;
    int count = 0;
    ContainerModel containerModel;

    protected ErrorPopupHelper errorHelper = new ErrorPopupHelper();

    SpannableStringBuilder spannableStringBuilder;
    FloatingActionButton uploadImagesBtn;
    private List<String> valueList;
    private List<String> setingNameList;
    boolean isOnCreate = true, isTruckChangeDialogShown = false;
    LegDialog legDialog;
    Dialog truckChangeDialog;
    ArrayList<SubLocationModel> legList = new ArrayList<SubLocationModel>();
    boolean isSubLocCalled = false, isDirectCallPOD = false, isImageUploadBtnClick = false,
            isLeftChessisBtn = false, isButtonClicked = false, isUpdateStatusCalled = false, isContNumValid = false;;
    String selectedSubLocId = "";
    String alphabatOnlyPattern = "^[a-zA-Z]*$";
    String numberOnlyPattern = "^[0-9]*$";
    Dialog loadingFieldPicker, chessisPicker, updateRemarkPicker, contConditionPicker;
    Dialog mediaPickerD, mediaPickerYard, mediaPickerLoad;


    RequestQueue updateRemarkReq;
    RequestQueue getCompanySettingsReq;
    RequestQueue getSubLocReq;
    RequestQueue getContDetailReq;
    RequestQueue updateContStatusReq;
    RequestQueue updateContLoadTypeReq;
    RequestQueue getNextLocReq;
    RequestQueue updateChessisNumReq;

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
            rootView = inflater.inflate(R.layout.fragment_container_details, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            savedInstanceStateBundle = savedInstanceState;
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);
        //buttonblink(rootView);

        return rootView;
    }


    void initView(View container) {

        API_URL                 = new APIs(getActivity());
        updateRemarkReq         = Volley.newRequestQueue(getActivity());
        getCompanySettingsReq   = Volley.newRequestQueue(getActivity());
        getSubLocReq            = Volley.newRequestQueue(getActivity());
        getContDetailReq        = Volley.newRequestQueue(getActivity());
        updateContStatusReq     = Volley.newRequestQueue(getActivity());
        updateContLoadTypeReq   = Volley.newRequestQueue(getActivity());
        getNextLocReq           = Volley.newRequestQueue(getActivity());
        updateChessisNumReq     = Volley.newRequestQueue(getActivity());


        layoutMain          = (RelativeLayout) container.findViewById(R.id.layoutMain);
        editFieldView       = (RelativeLayout)container.findViewById(R.id.editFieldView);
        editFieldTareView   = (RelativeLayout)container.findViewById(R.id.editFieldTareView);
        ActionbarDetail     = (RelativeLayout)container.findViewById(R.id.ActionbarDetail);
        container_name_tv   = (TextView) container.findViewById(R.id.container_name_tv);
        originAddressTV     = (TextView) container.findViewById(R.id.originAddressTV);
        destAddressTV       = (TextView) container.findViewById(R.id.destAddressTV);
        actionBarTitleTV    = (TextView) ActionbarDetail.findViewById(R.id.actionBarTitleTV);

        shippingLineAddTV   = (TextView)container.findViewById(R.id.shippingLineAddTV);
        hiddenUploadView    = (TextView)container.findViewById(R.id.hiddenUploadView);
        container_type_tv   = (TextView) container.findViewById(R.id.container_type_tv);
        loadTypeTxtVw       = (TextView) container.findViewById(R.id.loadTypeTxtVw);
        appointmentTV       = (TextView) container.findViewById(R.id.appointmentTV );
        contactTV           = (TextView) container.findViewById(R.id.contactTV );
        truckNoTV           = (TextView) container.findViewById(R.id.truckNoTV);
        chassiesNoTV        = (TextView) container.findViewById(R.id.chassiesNoTV);
        serial_no_detail_tv = (TextView) container.findViewById(R.id.serial_no_detail_tv);
        uploadImgTV         = (TextView)container.findViewById(R.id.uploadImgTV);
        billToTV            = (TextView)container.findViewById(R.id.billToTV);
        refNoTV             = (TextView)container.findViewById(R.id.refNoTV);
        poNumberTV          = (TextView)container.findViewById(R.id.poNumberTV);
        inspReqTxtVw        = (TextView)container.findViewById(R.id.inspReqTxtVw);

        originDropDateTV = (TextView)container.findViewById(R.id.originDropDateTV);
        originResNumTV   = (TextView)container.findViewById(R.id.originResNumTV);
      //  originDropTimeTV = (TextView)container.findViewById(R.id.originDropTimeTV);
        destDropDateTV   = (TextView)container.findViewById(R.id.destDropDateTV);
        destResNumTV = (TextView)container.findViewById(R.id.destResNumTV);
      //  destDropTimeTV   = (TextView)container.findViewById(R.id.destDropTimeTV);

       /* originAddLay1 = (LinearLayout)container.findViewById(R.id.originAddLay1);
        originAddLay2   = (LinearLayout)container.findViewById(R.id.originAddLay2);
        destAddLay1 = (LinearLayout)container.findViewById(R.id.destAddLay1);
        destAddLay2   = (LinearLayout)container.findViewById(R.id.destAddLay2);
*/

        uploadImagesBtn     = (FloatingActionButton)container.findViewById(R.id.uploadImagesBtn);
        nameTitleTV         = (TextView)container.findViewById(R.id.nameTitleTV);

        progressBar         = (ProgressBar)container.findViewById(R.id.progressBar2);

        uploadMediaLay      = (RelativeLayout) container.findViewById(R.id.uploadMediaLay);
        contactLay          = (LinearLayout)container.findViewById(R.id.contactLay);
        container_bar_layout= (LinearLayout)container.findViewById(R.id.container_bar_layout);

        topliner            = (LinearLayout)container.findViewById(R.id.topliner);

        tareWeightInputType = (TextInputLayout)container.findViewById(R.id.tareWeightInputType);

        contnrNoEditText    = (EditText) container.findViewById(R.id.contnrNoEditText);
        tareWeightEditText  = (EditText) container.findViewById(R.id.tareWeightEditText);
        remarkEditText      = (EditText) container.findViewById(R.id.remarkEditText);
        menuImgBtn          = (ImageView)container.findViewById(R.id.menuImgBtn);
        mapListBtn          = (ImageView)container.findViewById(R.id.importExportImgVw);
        contactBtn          = (ImageView)container.findViewById(R.id.contactBtn);
        uploadImgBtn        = (ImageView)container.findViewById(R.id.uploadImgBtn);
        editChessisBtn      = (ImageView)container.findViewById(R.id.editChessisBtn);

        btnDriverJob        = (Button)container.findViewById(R.id.btnDriverJob);
        btnCancelJob        = (Button)container.findViewById(R.id.btnCancelJob);

        updateChessisBtn    = (RelativeLayout) container.findViewById(R.id.updateChessisBtn);
        hideKeyboardLay     = (LinearLayout) container.findViewById(R.id.hideKeyboardLay);
        searchActionBar     = (RelativeLayout) container.findViewById(R.id.searchActionBar);
        menuActionBar       = (RelativeLayout) container.findViewById(R.id.menuActionBar);
        notificationLay     = (RelativeLayout) container.findViewById(R.id.notificationLay);
        jobDetailLay        = (RelativeLayout) container.findViewById(R.id.jobDetailLay);

        msgTV               = (TextView)container.findViewById(R.id.msgTextView);
        ConfirmTitleTV      = (TextView)container.findViewById(R.id.ConfirmTitleTV);
        remarkBtn           = (ImageView)container.findViewById(R.id.remarkImgBtn);
        ReseveDateTV        = (TextView)container.findViewById(R.id.ReseveDateTV);
        noteTV              = (TextView)container.findViewById(R.id.noteTV);
        remarknoteV         = (TextView)container.findViewById(R.id.remarknoteV);
        TareWtTitleTV       = (TextView)container.findViewById(R.id.TareWtTitleTV);
        closeMsgView        = (TextView)container.findViewById(R.id.closeMsgView);



        valueList = new ArrayList<>();
        setingNameList = new ArrayList<>();

        framAnimMsg  = (CardView)container.findViewById(R.id.framAnimMsg);


        closeMsgView.setText(Html.fromHtml("<u>Close</u>"));

        String errorString = "Format like ABCD1234567                    .";  // Your custom error message.
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.white));
        spannableStringBuilder = new SpannableStringBuilder(errorString);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);

        DeviceID    = LoginActivity.deviceID.trim();
        DriverID    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
        CompanyID   = Constants.getCompanyId(Constants.KEY_COMPANY, getContext());
        TruckId     = Constants.getTruckId(getActivity());

        //Constants.gps           = new GPSTracker(getActivity());
        Constants.GET_BUNDLE    = this.getArguments();
        container_Sr_No         = Constants.GET_BUNDLE.getString("container_number");
        CONTAINER_JOB_TYPE      = Constants.GET_BUNDLE.getString("jobType");
        itemPosition            = Constants.GET_BUNDLE.getInt("position");

        /* ------------- GET CONTAINER DETAILS ------------ */
        SetExportContainerDetails(itemPosition);
        GetCompanySetting(StrCompanyId);
        GET_NEXT_LOCATION(StrContainerId, "", isOnCreate);
        GET_UPDATE_REMARK( Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),  StrContainerId , StrRemarks,  StrIsUpdateRequest);


        isOnCreate = false;


        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT)))
            nameTitleTV.setText("B/L No");

        menuImgBtn.setImageResource(R.drawable.ic_arrow_backbb);
        mapListBtn.setImageResource(R.drawable.refresh);
        searchActionBar.setVisibility(View.GONE);

        noteTV.setHorizontallyScrolling(true);
        noteTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        noteTV.setSingleLine(true);
        noteTV.setMarqueeRepeatLimit(-1);
        noteTV.setSelected(true);
        noteTV.setText(StrMessageText);

        remarknoteV.setHorizontallyScrolling(true);
        remarknoteV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        remarknoteV.setSingleLine(true);
        remarknoteV.setMarqueeRepeatLimit(-1);
        remarknoteV.setSelected(true);
        remarknoteV.setText(StrGetRemarks);

        /*remarknoteV.onWindowFocusChanged(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });*/
        TabActivity.isHome = false;


        contnrNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String input =  charSequence.toString();
                int inputLength = input.length();
                isContNumValid = false;

                if(inputLength == 10 || inputLength == 11){
                    if(input.substring(0, 4).matches(alphabatOnlyPattern) &&
                            input.substring(4, inputLength).matches(numberOnlyPattern) ){
                        isContNumValid = true;
                    }
                }else{
                    errorHelper.setError(contnrNoEditText, "error message showing");
                    //contnrNoEditText.setError(Html.fromHtml("<font color='white'>this is the error</font>"));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s= editable.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    contnrNoEditText.setText(s);
                    contnrNoEditText.setSelection(contnrNoEditText.length());
                }
            }
        });


        notificationLay.setOnClickListener(this);
        menuActionBar.setOnClickListener(this);
        btnDriverJob.setOnClickListener(this);
        btnCancelJob.setOnClickListener(this);
//        contactBtn.setOnClickListener(this);
        uploadImgTV.setOnClickListener(this);
        uploadImgBtn.setOnClickListener(this);
        contactLay.setOnClickListener(this);
        container_bar_layout.setOnClickListener(this);
        hideKeyboardLay.setOnClickListener(this);
        updateChessisBtn.setOnClickListener(this);
        ActionbarDetail.setOnClickListener(this);
        remarkBtn.setOnClickListener(this);
        noteTV.setOnClickListener(this);
        closeMsgView.setOnClickListener(this);
        uploadImagesBtn.setOnClickListener(this);
        searchActionBar.setOnClickListener(this);

        SelfAssignDetailFragment selfAsignFragment = new SelfAssignDetailFragment();

        Constants.BUNDLE.putBoolean("YardSelfAssignAllowedImport", YardSelfAssignAllowedImport );
        Constants.BUNDLE.putBoolean("YardSelfAssignAllowedExport", YardSelfAssignAllowedExport );


        selfAsignFragment.setArguments(Constants.BUNDLE);
    }


    @SuppressLint("RestrictedApi")
    void SetExportContainerDetails(int position){

        if(ExportFragment.containerExportList.size() > position) {
            StrContainerName = ExportFragment.containerExportList.get(position).getChessieName();
            StrContainerType = ExportFragment.containerExportList.get(position).getContainerType() ;
            StrContainerTypeDetail = ExportFragment.containerExportList.get(position).getContainerTypeDetail();
            StrDateTime = ExportFragment.containerExportList.get(position).getPickDate();
            StrTruckNo = ExportFragment.containerExportList.get(position).getTrcukName();
            StrChassiesNo = ExportFragment.containerExportList.get(position).getChessieName();
            StrContainerId = String.valueOf(ExportFragment.containerExportList.get(position).getContainerId());
            IsReadStatus = ExportFragment.containerExportList.get(position).isRead();
            StrOriginAddress = ExportFragment.containerExportList.get(position).getOrignAddress();
            StrDestAddress = ExportFragment.containerExportList.get(position).getDestinationAddress();

            intContainerStatus = ExportFragment.containerExportList.get(position).getStatus();
            intOriginStatus = ExportFragment.containerExportList.get(position).getOrignTypeID();
            intDestStatus = ExportFragment.containerExportList.get(position).getDestinationTypeID();
            StrBillTo = ExportFragment.containerExportList.get(position).getBillTO();
            StrContactNo = ExportFragment.containerExportList.get(position).getBillToContact();
            StrRefNo = ExportFragment.containerExportList.get(position).getRefNumber();
            StrResNo = ExportFragment.containerExportList.get(position).getReservationNo();
            StrDropOffResNo = ExportFragment.containerExportList.get(position).getDropOffReservationNo();
            StrShipingLineAdd = ExportFragment.containerExportList.get(position).getShipingLineAddress();
            StrContainerSize = ExportFragment.containerExportList.get(position).getSize();
            StrContainerLoadType = ExportFragment.containerExportList.get(position).getContainerLoadTypeID();
            TareWeight = ExportFragment.containerExportList.get(position).getTareWeight();
            ContainerNo = ExportFragment.containerExportList.get(position).getContainerNo();
            IsStreetReturned = ExportFragment.containerExportList.get(position).isStreetReturned();
            StrMessageText = ExportFragment.containerExportList.get(position).getMessageText();
            StrCompanyId = ExportFragment.containerExportList.get(position).getCompanyId();

            if (ContainerNo.equalsIgnoreCase("N/A")) {
                ContainerNo = "";
            }
            SetContainerView();
            if (!ExportFragment.containerExportList.get(position).getOrignLatitude().equals("") &&
                    !ExportFragment.containerExportList.get(position).getOrignLatitude().equalsIgnoreCase("null"))
                OriginLat = Double.parseDouble(ExportFragment.containerExportList.get(position).getOrignLatitude());

            if (!ExportFragment.containerExportList.get(position).getOrignLogitude().equals("") &&
                    !ExportFragment.containerExportList.get(position).getOrignLogitude().equalsIgnoreCase("null"))
                OriginLong = Double.parseDouble(ExportFragment.containerExportList.get(position).getOrignLogitude());

            if (!ExportFragment.containerExportList.get(position).getDestinationLatitude().equals("") &&
                    !ExportFragment.containerExportList.get(position).getDestinationLatitude().equalsIgnoreCase("null"))
                DestLat = Double.parseDouble(ExportFragment.containerExportList.get(position).getDestinationLatitude());

            if (!ExportFragment.containerExportList.get(position).getDestinationLogitude().equals("") &&
                    !ExportFragment.containerExportList.get(position).getDestinationLogitude().equalsIgnoreCase("null"))
                DestLong = Double.parseDouble(ExportFragment.containerExportList.get(position).getDestinationLogitude());


            /*-------------- Set Date Format -------------------*/
            if (StrDateTime.equals("null")) {
                StrDateTime = "";
            } else{
                StrDateFormat = Constants.SET_DATE_FORMAT(StrDateTime);
            }
            String DestAddressHtml = "", OriginAddressHtml = "";
            DestAddressHtml = setHtmlTypeDest(StrDestAddress);
            OriginAddressHtml = setHtmlTypeOrigin(StrOriginAddress);

            serial_no_detail_tv.setText("#" + (itemPosition + 1));

            /*-------------- Set Text on TextView -------------------*/

            if (DestAddressHtml != "N/A") {
                destAddressTV.setText(Html.fromHtml(DestAddressHtml));
            }

            if (OriginAddressHtml != "N/A") {
                originAddressTV.setText(Html.fromHtml(OriginAddressHtml));
            }

            if (StrContainerType != "N/A") {
                String containerDetails = StrContainerType + "/" +StrContainerSize + " " + StrContainerTypeDetail;
                setTextOnView(containerDetails.trim(), container_type_tv);
            }
            if (ContainerNo != "N/A") {
                setTextOnView(ContainerNo, container_name_tv);
                setTextOnView(ContainerNo, contnrNoEditText);
            }
            if (StrRefNo != "N/A") {
                setTextOnView(StrRefNo, refNoTV);
            }
            if (StrTruckNo != "N/A") {
                setTextOnView(StrTruckNo, truckNoTV);
            }
            if (StrChassiesNo != "N/A") {
                setTextOnView(StrChassiesNo, chassiesNoTV);
            }
            String poNumber = ExportFragment.containerExportList.get(position).getPONumber();
            setTextOnView(poNumber, poNumberTV);
            setTextOnView(StrBillTo, billToTV);
            setTextOnView(StrShipingLineAdd, shippingLineAddTV);
            setTextOnView("Container Detail", actionBarTitleTV);
            setTextOnView(StrDateFormat, ReseveDateTV);
            setTextOnView(StrMessageText, noteTV);

            if (intOriginStatus == Constants.DROPOFF || intDestStatus == Constants.DROPOFF) {
                setTextOnView(StrDropOffResNo, appointmentTV);
            } else{
                setTextOnView(StrResNo, appointmentTV);
            }

            handleAddressView(intOriginStatus, intDestStatus);

            /*-------------- Handle Container Status -------------------*/
            handleExportContainerStatus(intContainerStatus, intOriginStatus, intDestStatus);


            /* --------- Check Chessis Update Status ---------- */
            checkChessisUpdateStatus(intContainerStatus);


        }

    }

    String setHtmlTypeOrigin(String address){
        String OriginAddress = "", OriginTitle = "", htmlType = "", addressText = "";
        if(!address.equals("") && !address.equalsIgnoreCase("null") && !address.equalsIgnoreCase("N/A"))    {
            String[] addArray = address.split(":");
            OriginTitle = addArray[0];
            if(addArray.length > 1) {
                OriginAddress = addArray[1];
                addressText = " <br>Address: </b>";
            }else
                addressText = "";


            htmlType = "<b>" + OriginTitle + addressText + OriginAddress;
        }else{
            htmlType = address;
        }
        return htmlType;
    }

    String setHtmlTypeDest(String address){
        String DestAddress = "", DestTitle = "", htmlType = "", addressText = "";
        if(!address.equals("") && !address.equalsIgnoreCase("null") && !address.equalsIgnoreCase("N/A"))    {

            String[] addArray = address.split(":");
            DestTitle = addArray[0];
            if(addArray.length > 1) {
                DestAddress = addArray[1];
                addressText = " <br>Address: </b>";
            }else
                addressText = "";

            //DestAddress = DestAddress + "<br><b>Date & Time:</b> "+ StrDateFormat;
            htmlType = "<b>" + DestTitle + addressText + DestAddress;
        }else{
            htmlType = address;
        }
        return htmlType;
    }


    void setTextOnView(String text, TextView view){
        if(!text.equalsIgnoreCase("null")) {
            view.setText(text);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        Constants.hideSoftKeyboard(getActivity());

        if (intContainerStatus != Constants.Assign && intContainerStatus != Constants.ArrivedAtPickUpLoc ) {

            uploadImagesBtn.setVisibility(View.VISIBLE);

        }else{
            uploadImagesBtn.setVisibility(View.GONE);
        }

        if(Constants.CURRENT_LATITUDE.length() < 4) {
            statusCheck();
        }
    }


    void setPickUpStatus(int dest){
        if(dest == Constants.LOADING || dest == Constants.UNLOADING){

            StrNextStatus = String.valueOf(Constants.ArrivedAtLoadingLocation);

            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                StrMessage = Constants.CONTAINER_STATUS_TYPES[19];
            }else{
                StrMessage = Constants.CONTAINER_STATUS_TYPES[35];
            }

            btnDriverJob.setText( StrMessage );

        }else if(dest == Constants.YARD){
            StrNextStatus = String.valueOf(Constants.ArrivedAtYardlocation);
            StrMessage = "Arrived at Yard Location";
            btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[13] );
        }else{
            StrMessage = Constants.CONTAINER_STATUS_TYPES[18];

            btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[18] );
        }
    }


    void setExportPickUpLocationCase(int dest, int containerStatus){

        if(containerStatus == Constants.PickUp){
            setPickUpStatus(dest);
        }else{

            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                hiddenUploadView.setVisibility(View.INVISIBLE);
                SetContainerView();
               // contnrNoEditText.setVisibility(View.VISIBLE);
                tareWeightInputType.setVisibility(View.VISIBLE);
                TareWtTitleTV.setVisibility(View.VISIBLE);
                editFieldTareView.setVisibility(View.VISIBLE);
                if(!ContainerNo.equalsIgnoreCase("null") && !ContainerNo.equals("")) {
                    contnrNoEditText.setText(ContainerNo);
                    contnrNoEditText.setEnabled(false);
                    isContNumValid = true;
                }
                if(!TareWeight.equalsIgnoreCase("null") && !TareWeight.equals("")) {
                    tareWeightEditText.setText(TareWeight);
                    tareWeightEditText.setEnabled(false);
                }
            }else{
               // contnrNoEditText.setVisibility(View.GONE);
                tareWeightInputType.setVisibility(View.GONE);
                TareWtTitleTV.setVisibility(View.GONE);

                SetContainerView();
            }

            uploadMediaLay.setVisibility(View.VISIBLE);
            editFieldTareView.setVisibility(View.VISIBLE);
            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[2]);
        }
    }


    /* ---------- Handle Container Location Status ------------- */
    void handleExportContainerStatus(int intContainerStatus, int OrignStatus, int DestStatus){

        StrNextStatus = String.valueOf(DestStatus);

        switch (OrignStatus){

             /* ------ PickUp Location -------*/
            case 1:

                if(intContainerStatus == Constants.ArrivedAtPickUpLoc || intContainerStatus == Constants.PickUp){
                        setExportPickUpLocationCase(DestStatus, intContainerStatus);
                }
                btnCancelJob.setVisibility(View.INVISIBLE);

                break;


    /* ------ Loading Location -------*/
            case 2:

                setLoadingStatus();

                break;


    /* ------ DropOff Location -------*/
            case 3:

                SetContainerView();
                StrNextStatus = String.valueOf(DestStatus);
                hiddenUploadView.setVisibility(View.GONE);
                editFieldTareView.setVisibility(View.GONE);
                btnCancelJob.setVisibility(View.INVISIBLE);

                if(intContainerStatus == Constants.ArrivedAtDropOffLocation ||
                        intContainerStatus == Constants.ArrivedAtFirstGate ){
                    SetContainerView();
                    uploadMediaLay.setVisibility(View.VISIBLE);
                    editFieldTareView.setVisibility(View.VISIBLE);
                    hiddenUploadView.setVisibility(View.INVISIBLE);
                    tareWeightInputType.setVisibility(View.GONE);
                    TareWtTitleTV.setVisibility(View.GONE);

                    StrNextStatus = String.valueOf(Constants.POD);
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[15] );

                    SetContainerView();
                }else if(intContainerStatus == Constants.POD ){
                    uploadMediaLay.setVisibility(View.VISIBLE);
                    StrNextStatus = String.valueOf(Constants.Delivered);
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[16] );
                }else{
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[23] );
                }

                break;


   /* ------ Yard Location -------*/
            case 4:

                SetContainerView();
                StrNextStatus = String.valueOf(DestStatus);   // Go to next Location
                editFieldTareView.setVisibility(View.GONE);
                btnCancelJob.setVisibility(View.INVISIBLE);
                hiddenUploadView.setVisibility(View.GONE);

                if(intContainerStatus == Constants.ArrivedAtYardlocation || intContainerStatus == Constants.PickFromYardLocation){

                    if(DestStatus == Constants.LOADING || DestStatus == Constants.UNLOADING){
                        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[26];
                        else
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[35];

                        btnDriverJob.setText(StrMessage);
                    }else if(DestStatus == Constants.DROPOFF){
                        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[22];
                        }
                        else{
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[38];
                        }
                        btnDriverJob.setText(StrMessage);
                    }else if(DestStatus == Constants.NO_LOCATION || DestStatus == 0){
                        if(intContainerStatus == Constants.ArrivedAtYardlocation) {
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[24];
                            btnDriverJob.setText(StrMessage);
                            loadingStatusDialog(Constants.ArrivedAtYardlocation);
                        }else{
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[18];
                            btnDriverJob.setText(StrMessage);
                        }
                    }
                }else if(intContainerStatus == Constants.AssignForYardLocation){
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );

                }

                break;


            case 5:

               if(intContainerStatus == Constants.AssignForLoadingLocation){
                   uploadMediaLay.setVisibility(View.VISIBLE);
                   btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[29]);
               }


                break;


            case 6:
                setLoadingStatus();
                break;

   /* ------ No Location -------*/
            default:
                btnCancelJob.setVisibility(View.INVISIBLE);
                if(intContainerStatus == Constants.Assign){

                    StrNextStatus = String.valueOf(Constants.ArrivedAtPickUpLoc );   // Arrived at pickup location
                    StrMessage = "Picked Up";
                }else if(intContainerStatus == Constants.ArrivedAtPickUpLoc || intContainerStatus == Constants.PickUp){
                    if(IsStreetReturned)
                        setPickUpStatus(DestStatus);
                     else
                        setExportPickUpLocationCase( DestStatus, Constants.ArrivedAtPickUpLoc);
                }else if(intContainerStatus == Constants.AssignForLoadingLocation){
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );

                    if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                        if(StrContainerLoadType.equals(String.valueOf(Constants.Empty)))
                            loadingStatusForEmptyDialog();
                        else
                            loadingFieldsDialog(StrContainerLoadType);
                    }else{
                        uploadMediaLay.setVisibility(View.GONE);
                        StrNextStatus = String.valueOf(Constants.LiveLoadingDone);

                    }

                }else if(intContainerStatus == Constants.AssignForYardLocation){
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );

                }else if(intContainerStatus == Constants.PickFromYardLocation){

                    if(DestStatus == Constants.LOADING || DestStatus == Constants.UNLOADING){
                        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[26];
                        else
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[35];

                        btnDriverJob.setText(StrMessage);
                    }else if(DestStatus == Constants.DROPOFF){
                        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[22];
                        }
                        else{
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[38];
                        }
                        btnDriverJob.setText(StrMessage);
                    }else if(DestStatus == Constants.NO_LOCATION || DestStatus == 0){
                            StrMessage = Constants.CONTAINER_STATUS_TYPES[18];
                            btnDriverJob.setText(StrMessage);
                    }

                }else{
                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[23] );
                }

                break;


        }


    }


    /* ---------- Handle res no, date, time kabel for address ------------- */
    void handleAddressView( int OrignStatus, int DestStatus){


      //  String pickupAddViewTxt = "<b>Res. No. :</b> " + pickResNo + "<br><b>Pickup Date :</b> " + pickResDate + "<br><b>Pickup Time :</b> " +pickResTime;
      //  String dropAddViewTxt = "<b>Res. No. :</b> " + dropResNo + "<br><b>Dropoff Date :</b> " + dropResDate + "<br><b>Dropoff Time :</b> " +dropResTime;

        if(OrignStatus == Constants.PICKED_UP || OrignStatus == Constants.DROPOFF){

            setAddress(OrignStatus);

            originDropDateTV.setVisibility(View.VISIBLE);
            originResNumTV.setVisibility(View.VISIBLE);
            destResNumTV.setVisibility(View.GONE);
            destDropDateTV.setVisibility(View.GONE);

        }else if(DestStatus == Constants.PICKED_UP || DestStatus == Constants.DROPOFF){
            setAddress(DestStatus);

            originDropDateTV.setVisibility(View.GONE);
            originResNumTV.setVisibility(View.GONE);
            destResNumTV.setVisibility(View.VISIBLE);
            destDropDateTV.setVisibility(View.VISIBLE);


        }else{
            originDropDateTV.setVisibility(View.GONE);
            originResNumTV.setVisibility(View.GONE);
            destResNumTV.setVisibility(View.GONE);
            destDropDateTV.setVisibility(View.GONE);

        }

    }



    void setAddress(int loc){
        if(loc == Constants.PICKED_UP){
            if(pickResNo.isEmpty() || pickResTime.isEmpty() || pickResDate.isEmpty() ){
                inspReqTxtVw.setVisibility(View.VISIBLE);
                inspReqTxtVw.setText(getString(R.string.res_detail_missing));
            }else{
                inspReqTxtVw.setVisibility(View.GONE);
            }


            originDropDateTV.setText(getString(R.string.pickup_date) + " " + pickResDate + " " + pickResTime);
            originResNumTV.setText(getString(R.string.res_no) + " " + pickResNo);


        }else {

            if (dropResNo.isEmpty() || dropResTime.isEmpty() || dropResDate.isEmpty()) {
                inspReqTxtVw.setVisibility(View.VISIBLE);
                inspReqTxtVw.setText(getString(R.string.res_detail_missing));
            } else {
                inspReqTxtVw.setVisibility(View.GONE);
            }

            destDropDateTV.setText(getString(R.string.dropoff_date) + dropResDate + " " + dropResTime);
           // destDropTimeTV.setText(getString(R.string.dropoff_time) + dropResTime );
            destResNumTV.setText(getString(R.string.res_no) + dropResNo );


        }
    }


    void ExportJobButtonClick(){

        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
            URL = API_URL.UPDATE_CONTAINER_STATUS;
        else
            URL = API_URL.UPDATE_CONTNR_STATUS_IMPORT;
//me
        if(intContainerStatus == Constants.Assign ){
            if(chassiesNoTV.getText().toString().equals("")) {
                UpdateChessisDialog(chassiesNoTV.getText().toString(), true, true, true, false);
            }
        }//me



        if(intOriginStatus == Constants.LOADING || intOriginStatus == Constants.UNLOADING  ){

            if(intContainerStatus == Constants.ArrivedAtLoadingLocation || intContainerStatus == Constants.LiveLoading ){

                loadingStatusDialog(intContainerStatus);

            }else if(intContainerStatus == Constants.LiveLoadingDone || intContainerStatus == Constants.DeadCall) {
                StrMessage = "";
                if (intDestStatus == 5 || intDestStatus == 0) {
                    if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))){
                        if((intLegType == 1) && (StrContainerLoadType.equals(String.valueOf(Constants.Loaded)))){
                            StrNextStatus = String.valueOf(Constants.Delivered);
                            UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );

                        }
                    }else{
                        if((intLegType == 1) && (StrContainerLoadType.equals(String.valueOf(Constants.Empty)))){
                            StrNextStatus = String.valueOf(Constants.Delivered);
                            UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );
                        }
                    }



                    if(intLegType == 1 && IsLoadLocationLeft == false) {
                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );
                    }else{
                        isButtonClicked = true;
                        GET_NEXT_LOCATION(StrContainerId, "", isOnCreate);
                    }

                } else if (intDestStatus == 2 || intDestStatus == 6) {
                    StrNextStatus = String.valueOf(Constants.ArrivedAtLoadingLocation);
                    if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                        StrMessage = "Arrived at next loading location";
                    else
                        StrMessage = "Arrived at next unloading location";
                    if(legList.size() == 0) {

                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo );
                    }else{
                        legDialog = new LegDialog(getActivity(), legList, new LegSelectionListener());
                        legDialog.show();
                    }


                } else if (intDestStatus == 4) {
                    StrNextStatus = String.valueOf(Constants.ArrivedAtYardlocation);
                    StrMessage = "Arrived at yard location";

                    UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE , ContainerNo );

                } else{
                    StrNextStatus = String.valueOf(Constants.ArrivedAtFirstGate);
                    StrMessage = "Arrived at drop off location";

                    UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );

                }
            }else if(intContainerStatus == Constants.LeftContainerWithChassie || intContainerStatus == Constants.LeftContainerWithoutChassie){


                    if(btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[26]) ||
                            btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[35])) {


                        StrMessage = btnDriverJob.getText().toString();
                        StrNextStatus = String.valueOf(Constants.ArrivedAtLoadingLocation);

                        if(legList.size() == 0) {
                            UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                        }else{
                            legDialog = new LegDialog(getActivity(), legList, new LegSelectionListener());
                            legDialog.show();
                        }

                    }else{
                        StrMessage = Constants.CONTAINER_STATUS_TYPES[27];
                        StrNextStatus = String.valueOf(Constants.ArrivedAtYardlocation);

                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );
                    }
                    btnDriverJob.setText(StrMessage );

            }else if(intContainerStatus == Constants.AssignForLoadingLocation){

                    UpdateContainer();

            }

            }else if(intOriginStatus == Constants.PICKED_UP ){
                if(intContainerStatus == Constants.ArrivedAtPickUpLoc ) {

                    ContainerNo = contnrNoEditText.getText().toString().trim().toUpperCase();

                    if(ContainerNo.length() > 0 && isContNumValid) {
                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                            if (ExportPickUpImage) {
                                if (!imagePath.equals("")) {
                                    StrNextStatus = String.valueOf(Constants.PickUp);
                                    PickContainer(Constants.PICKED_UP, StrContainerLoadType, "");
                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Please upload Interchange Out doc", VIEW_DURATION);
                                }
                            } else{
                                StrNextStatus = String.valueOf(Constants.PickUp);
                                PickContainer(Constants.PICKED_UP, StrContainerLoadType, "");
                            }
                        } else{
                            if (ImportPickUpImage) {
                                if (!imagePath.equals("")) {

                                    UpdateChessisDialog(chassiesNoTV.getText().toString(), false, true, false, true);
                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Please upload Interchange Out doc", VIEW_DURATION);
                                }
                            } else{
                                UpdateChessisDialog(chassiesNoTV.getText().toString(), false, true, false, true);

                            }
                        }
                    }else{
                        Constants.showToastMsg(btnDriverJob, "Enter valid container number", VIEW_DURATION);
                        contnrNoEditText.requestFocus();
                    }

                }else if(intContainerStatus == Constants.PickUp){
                    PickUpLocationClick();
                }else{
                    StrNextStatus = String.valueOf(Constants.ArrivedAtPickUpLoc);
                   if(!StrChassiesNo.equals("") || !StrChassiesNo.equals("null")) {
                       ContainerNo = contnrNoEditText.getText().toString().trim().toUpperCase();
                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                    }
                }

            }else if(intOriginStatus == Constants.YARD ){

            if(btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[26]) ||
                    btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[35])) {
                StrNextStatus = String.valueOf(Constants.ArrivedAtLoadingLocation);


                if (legList.size() == 0) {

                    UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                    btnDriverJob.setText(StrMessage);

                }else{
                    legDialog = new LegDialog(getActivity(), legList, new LegSelectionListener());
                    legDialog.show();
                    legDialogfromYard = true;

                }
            }else if(btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[22])|| btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[38])){
                StrNextStatus = String.valueOf(Constants.ArrivedAtFirstGate);
                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );
                btnDriverJob.setText(StrMessage );

            }else if(btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[18])){
                isButtonClicked = true;
                GET_NEXT_LOCATION(StrContainerId, String.valueOf(intContainerStatus), isOnCreate);

            }else if(btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[24])){
                loadingStatusDialog(Constants.ArrivedAtYardlocation);
            }else if(btnDriverJob.getText().toString().equalsIgnoreCase(Constants.CONTAINER_STATUS_TYPES[29])){

                UpdateChessisDialog(chassiesNoTV.getText().toString(), false, false, true, false);

            }

        }else if(intOriginStatus == Constants.DROPOFF ){

            if(btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[15])){
                StrNextStatus = String.valueOf(Constants.POD);
                if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))){
                    if(ExportDropOffImage) {
                        if(!imagePath.equals("")) {
                            new UploadPODDocImage().execute();
                        }else{
                            Constants.showToastMsg(btnDriverJob, "Please upload interchange In doc", VIEW_DURATION);
                        }
                    }else{

                        if(!imagePath.equals("")) {
                            new UploadPODDocImage().execute();
                        }else{
                            UPDATE_CONTAINER_STATUS(API_URL.UPDATE_CONTAINER_STATUS, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                        }

                    }
                }else{
                    if (ImportDropOffImage) {
                        if(!imagePath.equals("")) {
                            new UploadPODDocImage().execute();
                        }else{
                            Constants.showToastMsg(btnDriverJob, "Please upload interchange In doc", VIEW_DURATION);
                        }
                    }else{
                        if(!imagePath.equals("")) {
                            new UploadPODDocImage().execute();
                        }else{
                            UPDATE_CONTAINER_STATUS(API_URL.UPDATE_CONTNR_STATUS_IMPORT, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                        }
                    }
                }


            }else if(btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[16])){
                StrNextStatus = String.valueOf(Constants.Delivered);
                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );

            }else if(btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[23])){
                Constants.hideSoftKeyboard(getActivity());
                Constants.showToastMsg(btnDriverJob, "No job for this container.", VIEW_DURATION);
                getFragmentManager().popBackStack();
            }

        }else{

            if(intContainerStatus == Constants.ArrivedAtPickUpLoc ){
                StrNextStatus = String.valueOf(Constants.PickUp);

                PickContainer(Constants.ArrivedAtPickUpLoc, StrContainerLoadType, "");

            }else if(intContainerStatus == Constants.Delivered ){
                Constants.hideSoftKeyboard(getActivity());
                Constants.showToastMsg(btnDriverJob, "No job for this container.", VIEW_DURATION);
                getFragmentManager().popBackStack();
            }else if(intContainerStatus == Constants.AssignForLoadingLocation){

                StrNextStatus = String.valueOf(Constants.LiveLoadingDone);

                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );
                    if(StrContainerLoadType.equals(String.valueOf(Constants.Empty)))
                        loadingStatusForEmptyDialog();
                    else
                        loadingFieldsDialog(StrContainerLoadType);
                }else{

                    loadingFieldsDialog(StrContainerLoadType);

                }

            }else if(intContainerStatus == Constants.AssignForYardLocation){

                UpdateChessisDialog(chassiesNoTV.getText().toString(), false,  false, true, false);
            }else{

                if(IsStreetReturned) {
                    PickUpLocationClick();
                }else{
                    if(StrNextStatus.equals(String.valueOf(Constants.LiveLoadingDone))){
                        GET_NEXT_LOCATION(StrContainerId, String.valueOf(intContainerStatus), isOnCreate);
                    }else{
                    if(!chassiesNoTV.getText().toString().equals("")) {
                        StrNextStatus = String.valueOf(Constants.ArrivedAtPickUpLoc);
                        ContainerNo = contnrNoEditText.getText().toString().trim().toUpperCase();
                        Constants.hideSoftKeyboard(getActivity());
                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                    }
                   }

                }
            }
        }
    }


    void PickUpLocationClick(){
        if(intDestStatus == 5 || intDestStatus == 0) {
            isButtonClicked = true;
            GET_NEXT_LOCATION(StrContainerId, "", isOnCreate);
        }else{
            if (intDestStatus == Constants.YARD) {
                StrNextStatus = String.valueOf(Constants.ArrivedAtYardlocation);
                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE , ContainerNo );
            } else if (intDestStatus == Constants.LOADING || intDestStatus == Constants.UNLOADING) {
                StrNextStatus = String.valueOf(Constants.ArrivedAtLoadingLocation);

                if(legList.size() == 0) {
                    UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                }else{
                    legDialog = new LegDialog(getActivity(), legList, new LegSelectionListener());
                    legDialog.show();
                }

            } else{
                StrNextStatus = String.valueOf(Constants.ArrivedAtDropOffLocation);
                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE , ContainerNo );
            }



            }

        }



    void setLoadingStatus(){
        btnCancelJob.setVisibility(View.INVISIBLE);

        if(intContainerStatus == Constants.ArrivedAtLoadingLocation  ){
            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[20]);


            }else{
                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[36]);

            }

            SetContainerView();
            editFieldTareView.setVisibility(View.VISIBLE);
            hiddenUploadView.setVisibility(View.GONE);
            btnCancelJob.setVisibility(View.INVISIBLE);
            uploadMediaLay.setVisibility(View.VISIBLE);

        }else if(intContainerStatus == Constants.LiveLoading){


            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                StrMessage = "Select status at loading location";
                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[20]);
            }else{
                StrMessage = "Select status at unloading location";
                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[36]);
            }
            SetContainerView();
            tareWeightInputType.setVisibility(View.GONE);
            TareWtTitleTV.setVisibility(View.GONE);
            uploadMediaLay.setVisibility(View.VISIBLE);
            editFieldTareView.setVisibility(View.VISIBLE);
            btnCancelJob.setVisibility(View.INVISIBLE);


        }else if(intContainerStatus == Constants.LiveLoadingDone || intContainerStatus == Constants.DeadCall){

            if(intDestStatus == 5 || intDestStatus == 0) {


                if((intLegType == 1 || intLegType == 2)&& (IsLoadLocationLeft == true)){
                    if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                        if(StrContainerLoadType.equals(String.valueOf(Constants.Loaded))) {
                            StrNextStatus = String.valueOf(Constants.Delivered);
                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);
                        }else{
                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[18]);
                        }
                    }else{
                        if (StrContainerLoadType.equals(String.valueOf(Constants.Empty))) {
                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);
                            //GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                        }else{
                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[18]);
                        }
                    }

                }else if(intLegType == 3 ) {

                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[18]);

                }else{

                    if(count == 0) {
                        GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                    }
                    count++;
                        StrNextStatus = String.valueOf(Constants.Delivered);
                        btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);

                    }


            }else if(intDestStatus == 2 || intDestStatus == 6){
                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[21] );
                else
                     btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[35]);
            }else if(intDestStatus == 4){
                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[13]);
            }else{
                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[22]);
                }
                else{
                    btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[38]);
                }
//                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[22]);

            }
        }else if(intContainerStatus == Constants.AssignForLoadingLocation){
            btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );
            loadingFieldsDialog(StrContainerLoadType);
        }else if(intContainerStatus == Constants.DeadCall){
            btnDriverJob.setText( Constants.CONTAINER_STATUS_TYPES[29] );
        }
    }

    void UpdateContainer(){
        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
            loadingFieldsDialog(StrContainerLoadType);
        } else{
            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                URL = API_URL.UPDATE_CONTAINER_LOADTYPE;
            else
                URL = API_URL.UPDATE_CONTNR_LOADTYPE_IMPORT;

            UPDATE_CONTAINER_LOAD_TYPE(URL, StrContainerId, String.valueOf(intContainerStatus),
                    StrContainerLoadType, StrMessage,
                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, StrGrossWeight, StrSealNumber, StrPONumber, ContainerNo);
        }
    }


    private void SetContainerView(){
        if(ContainerNo.length() > 0){
            isContNumValid = true;
            contnrNoEditText.setEnabled(false);
            contnrNoEditText.setVisibility(View.GONE);
        }else{
            contnrNoEditText.setEnabled(true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.searchActionBar:

                if(getActivity() != null){
                    getFragmentManager().popBackStack();
                }
                break;


            case R.id.closeMsgView:
                viewMenu();
                break;

            case R.id.updateChessisBtn:
              //  isupdateChessisBtn=true;
             //   if(intContainerStatus == Constants.ArrivedAtPickUpLoc ) {
                    UpdateChessisDialog(chassiesNoTV.getText().toString(), true, true, false, false);
                /*}else{
                    UpdateChessisDialog(chassiesNoTV.getText().toString(), false, true, false, false);
                }*/
                break;

            case R.id.ActionbarDetail:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.container_bar_layout:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.contactLay:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.hideKeyboardLay:
                Constants.hideSoftKeyboard(getActivity());
                break;

            case R.id.btnDriverJob:
                Constants.hideSoftKeyboard(getActivity());
                if (Constants.isNetworkAvailable(getActivity())) {
                    ExportJobButtonClick();
                } else{
                    Constants.showToastMsg(btnDriverJob, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }

            break;


            case R.id.btnCancelJob:
                Constants.hideSoftKeyboard(getActivity());
                    getFragmentManager().popBackStack();
                break;


            case R.id.notificationLay:
                isOpen = true;
                viewMenu();
                GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                GetCompanySetting(StrCompanyId);
                progressBar.setVisibility(View.VISIBLE);


              //  zoomInImgDialog();

                break;



            case R.id.menuActionBar:

                Constants.hideSoftKeyboard(getActivity());
                getFragmentManager().popBackStack();

                break;


            case R.id.contactBtn:
                contactDialer(StrContactNo);
                break;


            case R.id.uploadImgTV:
                mediaDialog();
                break;

            case R.id.uploadImgBtn:
                mediaDialog();
                break;

            case R.id.remarkImgBtn:

                UpdateRemarkDialog();

                break;



            case R.id.uploadImagesBtn:
                if (Constants.isNetworkAvailable(getActivity())) {
                    showDocTypeDialog();
                } else{
                    isDirectCallPOD = false;
                    Constants.showToastMsg(btnDriverJob, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }

                break;



            case R.id.noteTV:
                viewMenu();
                msgTV.setText(StrMessageText);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void viewMenu() {

        if (!isOpen) {

            int x = topliner.getRight();
            int y = topliner.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

//            btnBlink.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),android.R.color.holo_blue_light,null)));
//            btnBlink.setImageResource(R.drawable.ic_close_grey);

            Animator anim = ViewAnimationUtils.createCircularReveal(framAnimMsg, x, y, startRadius, endRadius);

            framAnimMsg.setVisibility(View.VISIBLE);
            anim.start();

            isOpen = true;

        } else{

            int x = framAnimMsg.getRight();
            int y = framAnimMsg.getBottom();

            int startRadius = Math.max(topliner.getWidth(), topliner.getHeight());
            int endRadius = 0;

//            btnBlink.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.black_semi_dark,null)));
//            btnBlink.setImageResource(R.drawable.ic_textsms_black_24dp);

            Animator anim = ViewAnimationUtils.createCircularReveal(framAnimMsg, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    framAnimMsg.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();

            isOpen = false;
        }
    }


    void contactDialer(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }



    void checkChessisUpdateStatus(int status){
        updateChessisBtn.setEnabled(true);
        editChessisBtn.setVisibility(View.VISIBLE);

       if(status == Constants.Assign ){
           btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[40]);
       }else if (status == Constants.ArrivedAtPickUpLoc){
           btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[2]);
       }else if(status == Constants.PickUp){

           setPickUpStatus(intDestStatus);
        }else if(status == Constants.AssignForLoadingLocation ){
           btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[29]);
       }else if(status == Constants.AssignForYardLocation ){
           btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[39]);
    }else{
           updateChessisBtn.setEnabled(false);
           editChessisBtn.setVisibility(View.GONE);
       }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    statusCheck();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK && (requestCode == Camera || requestCode == Gallery ) ) {

            imagePath = data.getStringExtra("result");

            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            uploadImgBtn.setImageBitmap(bitmap);


            if (isDirectCallPOD){
                if (StrNextStatus.equals(String.valueOf(Constants.LiveLoadingDone)) || intContainerStatus == Constants.AssignForLoadingLocation
                        || intContainerStatus == Constants.LiveLoading) {
                    if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {

                        if(isLeftChessisBtn) {
                            StrNextStatus = String.valueOf(Constants.LeftContainerWithChassie);

                           // updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[6]);
                        }else{
                            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                            StrContainerLoadType = Constants.Empty;
                               // PickContainer(Constants.LiveLoading, StrContainerLoadType, "");


                       }

                        if(isImageUploadBtnClick){
                            if(selectedDocType.equals(getString(R.string.picup_doc))){
                                new PickContainerWithFile().execute();
                            }else{
                                new UploadPODDocImage().execute();
                            }
                        }else{
                            new UploadPODDocImage().execute();
                        }
                    }else{
                        if(isImageUploadBtnClick){
                            if(selectedDocType.equals(getString(R.string.picup_doc))){
                                new PickContainerWithFile().execute();
                            }else{
                                new UploadPODDocImage().execute();
                            }
                        }
                    }

                    isDirectCallPOD = false;
                }else{
                    if(isImageUploadBtnClick){
                        if(selectedDocType.equals(getString(R.string.picup_doc))){
                            new PickContainerWithFile().execute();
                        }else{
                            new UploadPODDocImage().execute();
                        }
                    }

                }
            }
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Constants.showToastMsg(btnDriverJob, "Enable GPS first", VIEW_DURATION);
        }else{
            getGPSLocation();
        }
    }

    void getGPSLocation(){

        LocServices locationServices = new LocServices(getActivity());
        locationServices.getLocations();
      //  if(Constants.locServices.getIsGPSTrackingEnabled()){

            try {
                Constants.CURRENT_LATITUDE = String.valueOf(locationServices.getLatitude());
                Constants.CURRENT_LONGITUDE = String.valueOf(locationServices.getLongitude());
            }catch (Exception e){
                e.printStackTrace();
            }
      //  }

        if(locationServices.getLatitude() == 0.0){
            locationServices = new LocServices(getActivity());
         //   if(locationServices.getIsGPSTrackingEnabled()){

                try {
                    Constants.CURRENT_LATITUDE = String.valueOf(locationServices.getLatitude());
                    Constants.CURRENT_LONGITUDE = String.valueOf(locationServices.getLongitude());
                }catch (Exception e){
                    e.printStackTrace();
                }

         //   }
        }

    }




    private void ShowMsgWithBackStack(String msg){
            Constants.showToastMsg(btnDriverJob, msg, VIEW_DURATION);
            getFragmentManager().popBackStack();
        }


    private void ShowMsgWithContainerDetail(String msg){
        Constants.showToastMsg(btnDriverJob, msg, VIEW_DURATION);
        GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
    }


    /*-------------- UPDATE CONTAINER STATUS API -------------------*/
    void UPDATE_CONTAINER_STATUS(final String URL, final String ContainerId, final String Status,
                                    final String Message, final String DriverId,
                                    final String Latitude, final String Longitude, final String ContainerNo){

        btnDriverJob.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response UPDATE_CONTAINER_STATUS: " + response);
                        isSubLocCalled = false;


                        JSONObject obj;

                        if(getActivity() != null) {
                            try {
                                progressBar.setVisibility(View.GONE);
                                btnDriverJob.setEnabled(true);

                                obj = new JSONObject(response);

                                if (obj.getInt("Status") == 0) {
                                    Constants.showToastMsg(btnDriverJob, getString(R.string.error_msg), VIEW_DURATION);
                                } else if (obj.getInt("Status") == 1) {
                                    if (obj.getBoolean("Success")) {

                                        String loadingStatus;
                                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                                            loadingStatus = "loading";
                                        else
                                            loadingStatus = "unloading";

                                        if (StrNextStatus.equals(String.valueOf(Constants.ArrivedAtPickUpLoc))) {
                                            Constants.showToastMsg(btnDriverJob, "Arrived at pickup location successfully", VIEW_DURATION);
                                            isUpdateStatusCalled = false;

                                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                                SetContainerView();

                                                tareWeightInputType.setVisibility(View.VISIBLE);
                                                TareWtTitleTV.setVisibility(View.VISIBLE);
                                                TareWtTitleTV.setVisibility(View.VISIBLE);
                                                uploadMediaLay.setVisibility(View.VISIBLE);
                                                hiddenUploadView.setVisibility(View.INVISIBLE);

                                                if (!ContainerNo.equalsIgnoreCase("null") && !ContainerNo.equals("")) {
                                                    contnrNoEditText.setText(ContainerNo);
                                                    contnrNoEditText.setEnabled(false);
                                                    isContNumValid = true;
                                                }
                                                if (!TareWeight.equalsIgnoreCase("null") && !TareWeight.equals("")) {
                                                    tareWeightEditText.setText(TareWeight);
                                                    tareWeightEditText.setEnabled(false);
                                                }
                                            } else {
                                                tareWeightInputType.setVisibility(View.GONE);
                                                TareWtTitleTV.setVisibility(View.GONE);
                                                TareWtTitleTV.setVisibility(View.GONE);
                                                uploadMediaLay.setVisibility(View.VISIBLE);
                                                hiddenUploadView.setVisibility(View.GONE);
                                                SetContainerView();
                                            }

                                            intContainerStatus = Constants.ArrivedAtPickUpLoc;
                                            StrNextStatus = String.valueOf(Constants.PickUp);
                                            btnCancelJob.setVisibility(View.GONE);
                                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[2]);

                                            String OriginAddressHtml = setHtmlTypeOrigin(StrDestAddress);
                                            originAddressTV.setText(Html.fromHtml(OriginAddressHtml));
                                            destAddressTV.setText("");
                                            GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.ArrivedAtLoadingLocation))) {

                                            String OriginAddressHtml = setHtmlTypeOrigin(StrDestAddress);
                                            originAddressTV.setText(Html.fromHtml(OriginAddressHtml));
                                            destAddressTV.setText("");

                                            ShowMsgWithContainerDetail("Arrived at " + loadingStatus + " location successfully.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LiveLoading))) {
                                            isUpdateStatusCalled = false;
                                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[25]);
                                            } else {
                                                btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[32]);
                                            }
                                            Constants.showToastMsg(btnDriverJob, "Live " + loadingStatus + " started.", VIEW_DURATION);
                                            btnCancelJob.setVisibility(View.INVISIBLE);
                                            intContainerStatus = Constants.LiveLoading;
                                            hiddenUploadView.setVisibility(View.GONE);
                                            editFieldTareView.setVisibility(View.VISIBLE);
                                            uploadMediaLay.setVisibility(View.VISIBLE);
                                            SetContainerView();

                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LiveLoadingDone))) {
                                            isUpdateStatusCalled = false;
                                            if (CONTAINER_JOB_TYPE.equals("1")) {
                                                Constants.showToastMsg(btnDriverJob, "Unloading done.", VIEW_DURATION);
                                                showUploadDocDialog();
                                            } else {
                                                Constants.showToastMsg(btnDriverJob, "Live " + loadingStatus + " done.", VIEW_DURATION);
                                                GET_NEXT_LOCATION(StrContainerId, StrNextStatus, isOnCreate);
                                            }

                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithChassie))) {


                                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
                                                if (IsImageUploaded) {
                                                    Constants.showToastMsg(btnDriverJob, "Container left With Chassis at " + loadingStatus + " location.", VIEW_DURATION);
                                                    getFragmentManager().popBackStack();
                                                } else {
                                                    if (!imagePath.equals("")) {
                                                        new UploadPODDocImage().execute();
                                                        Constants.showToastMsg(btnDriverJob, "Container left With Chassis at " + loadingStatus + " location.", VIEW_DURATION);

                                                    } else {
                                                        Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                                                    }
                                                }
                                            } else {
                                                Constants.showToastMsg(btnDriverJob, "Container left With Chassis at " + loadingStatus + " location.", VIEW_DURATION);
                                                getFragmentManager().popBackStack();

                                            }

                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithoutChassie))) {

                                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
                                                if (IsImageUploaded) {
                                                    Constants.showToastMsg(btnDriverJob, "Container left Without Chassis at " + loadingStatus + " location.", VIEW_DURATION);
                                                    getFragmentManager().popBackStack();
                                                } else {
                                                    if (!imagePath.equals("")) {
                                                        new UploadPODDocImage().execute();
                                                        Constants.showToastMsg(btnDriverJob, "Container left Without Chassis at " + loadingStatus + " location.", VIEW_DURATION);
                                                    } else {
                                                        Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                                                    }
                                                }
                                            } else {
                                                Constants.showToastMsg(btnDriverJob, "Container left Without Chassis at " + loadingStatus + " location.", VIEW_DURATION);
                                                getFragmentManager().popBackStack();

                                            }


                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LeftContainerAtYardWithChassis))) {
                                            ShowMsgWithBackStack("Container left With Chassis at yard location.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.LeftContainerAtYardWithoutChasis))) {
                                            ShowMsgWithBackStack("Container left Without Chassis at yard location.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.ArrivedAtYardlocation))) {

                                            //me
                                            String OriginAddressHtml = setHtmlTypeOrigin(StrDestAddress);
                                            originAddressTV.setText(Html.fromHtml(OriginAddressHtml));
                                            destAddressTV.setText("");//

                                            ShowMsgWithContainerDetail("Arrived at yard location successfully.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.PickFromYardLocation))) {
                                            Constants.showToastMsg(btnDriverJob, "Container picked up at yard location successfully.", VIEW_DURATION);
                                            //  GET_NEXT_LOCATION(StrContainerId, "20", isOnCreate);
                                            GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.ArrivedAtFirstGate))) {
                                            //me
                                            String OriginAddressHtml = setHtmlTypeOrigin(StrDestAddress);
                                            originAddressTV.setText(Html.fromHtml(OriginAddressHtml));
                                            destAddressTV.setText("");//

                                            ShowMsgWithContainerDetail("Arrived at first gate of drop off location.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.POD))) {


                                            Constants.showToastMsg(btnDriverJob, "Proof of delivery document has been successfully uploaded.", VIEW_DURATION);
                                            GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.Delivered))) {
                                            //  ShowMsgWithBackStack("Container delivered successfully.");

                                            zoomInImgDialog();

                                        } else if (StrNextStatus.equals(String.valueOf(Constants.DeadCall))) {
                                            ShowMsgWithBackStack("Dead Call request has been completed.");
                                        } else if (StrNextStatus.equals(String.valueOf(Constants.OrderCancel))) {
                                            ShowMsgWithBackStack("Your order has been cancelled.");
                                        } else {
                                            getFragmentManager().popBackStack();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", ">>error: " +error);

                        if(getActivity() != null) {
                            isSubLocCalled = false;
                            btnDriverJob.setEnabled(true);
                            progressBar.setVisibility(View.GONE);

                            String errorStr = error.toString();
                            if (errorStr.contains("NoConnectionError")) {
                                errorStr = "Internet connection not working properly";
                            }
                            Constants.showToastMsg(btnDriverJob, errorStr, VIEW_DURATION);
                        }
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<String, String>();
                params.put("ContainerId", ContainerId);
                params.put("Status", Status);
                params.put("Message", Message);
                params.put("DriverId", DriverId);
                params.put("DeviceNumber", DeviceID);
                params.put("Latitude", Latitude);
                params.put("Longitude", Longitude);
               // params.put("ContainerNo", ContainerNo);

                params.put("LoginID", LoginId);
                params.put("CompanyId", StrCompanyId);
                params.put("Type", "Android");



                if(isSubLocCalled) {
                    params.put("SubLocationId", selectedSubLocId);
                }


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
        postRequest.setRetryPolicy(policy);
        updateContStatusReq.add(postRequest);

    }



    /*-------------- UPDATE CONTAINER STATUS API -------------------*/
    void UPDATE_CONTAINER_LOAD_TYPE (final String URL, final String ContainerId,
                                     final String ContainerStatus, final String Status,
                                  final String Message, final String DriverId,
                                  final String Latitude, final String Longitude,
                                     final String GrossWeight, final String SealNo, final String PONumber,
                                     final String ContainerNo){

        progressBar.setVisibility(View.VISIBLE);
        btnDriverJob.setEnabled(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response UPDATE_CONTAINER_STATUS: " + response);

                        btnDriverJob.setEnabled(true);
                        JSONObject obj;

                        if(getActivity() != null) {
                            try {
                                progressBar.setVisibility(View.GONE);

                                obj = new JSONObject(response);
                                if (obj.getInt("Status") == 0) {
                                    Constants.showToastMsg(btnDriverJob, getString(R.string.error_msg), VIEW_DURATION);
                                } else if (obj.getInt("Status") == 1) {
                                    if (obj.getBoolean("Success")) {

                                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                            Constants.showToastMsg(btnDriverJob, "Loading completed", VIEW_DURATION);
                                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[18]);
                                            GET_NEXT_LOCATION(StrContainerId, String.valueOf(Constants.LiveLoadingDone), isOnCreate);
                                        } else {
                                            intContainerStatus = Integer.valueOf(ContainerStatus);

                                            if (intContainerStatus == Constants.LiveLoading) {

                                                showUploadDocDialog();
                                            } else {
                                                if (intContainerStatus == Constants.AssignForLoadingLocation) {
                                                    Constants.showToastMsg(btnDriverJob, "Container picked up.", VIEW_DURATION);
                                                    btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[18]);
                                                    GET_NEXT_LOCATION(StrContainerId, String.valueOf(Constants.LiveLoadingDone), isOnCreate);
                                                } else {
                                                    Constants.showToastMsg(btnDriverJob, "Unloading completed", VIEW_DURATION);
                                                    getFragmentManager().popBackStack();
                                                }

                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", ">>error: " +error);

                        if(getActivity() != null) {
                            progressBar.setVisibility(View.GONE);
                            btnDriverJob.setEnabled(true);
                            String errorStr = error.toString();
                            if (errorStr.contains("NoConnectionError")) {
                                errorStr = "Internet connection not working properly";
                            }
                            Constants.showToastMsg(btnDriverJob, errorStr, VIEW_DURATION);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String, String>();
                params.put("ContainerId", ContainerId);
                params.put("ContainerLoadTypeID", Status);
                params.put("Status", ContainerStatus);
                params.put("Message", Message);
                params.put("DriverId", DriverId);
                params.put("Latitude", Latitude);
                params.put("Longitude", Longitude);
                params.put("LoginId", LoginId);
                params.put("GrossWeight", GrossWeight);
                params.put("SealNo", SealNo);
                params.put("PONumber", PONumber);
                params.put("ContainerNo", ContainerNo);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime50, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
        postRequest.setRetryPolicy(policy);
        updateContLoadTypeReq.add(postRequest);

    }


     /*-------------- Get Next Location API -------------------*/
    void GET_NEXT_LOCATION(final String ContainerId, final String Status, final boolean isOnCreate){

        if(getActivity() != null) {
            progressBar.setVisibility(View.VISIBLE);

            StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_NEXT_LOCATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject obj, resultJson;
                            int status, DestinationType = 0;
                            String destAddress = "";
                            Log.d("Response ", ">>>Response NextLoc: " + response);

                            if(getActivity() != null) {
                                try {

                                    progressBar.setVisibility(View.GONE);
                                    obj = new JSONObject(response);
                                    status = obj.getInt("Status");

                                    if (status == 1) {
                                        resultJson = new JSONObject(obj.getString("Result"));
                                        DestinationType = resultJson.getInt("DestinationTypeId");
                                        destAddress = HtmlText(resultJson.getString("DestinationAddress"));
                                        //me
                                        GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);//me

                                        if (!isOnCreate) {
                                            if (DestinationType == 5 || DestinationType == 0) {

                                                if (IsLoadLocationLeft == true) {
                                                    if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                                        if ((intLegType == 1) && (intContainerStatus == Constants.LiveLoadingDone)) {
                                                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);

                                                        } else {
                                                            showNoLocationDialog();
                                                        }
                                                    } else {
                                                        if ((intLegType == 1) && (StrContainerLoadType.equals(String.valueOf(Constants.Empty)))) {
                                                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);

                                                        } else {
                                                            showNoLocationDialog();
                                                        }
                                                    }


                                                } else {
                                                    if (intLegType == 3) {
                                                        showNoLocationDialog();
                                                    } else {
                                                        btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);
                                                    }
                                                }
                                            } else {


                                                // if(isNextLoacDialog) {
                                                if (DestinationType == Constants.LOADING)
                                                    NextLocationDescDialog("Loading", destAddress);
                                                else if (DestinationType == Constants.DROPOFF)
                                                    NextLocationDescDialog("DropOff", destAddress);
                                                else if (DestinationType == Constants.YARD)
                                                    NextLocationDescDialog("Yard", destAddress);
                                                else if (DestinationType == Constants.UNLOADING)
                                                    NextLocationDescDialog("Unloading", destAddress);
                                                // }
                                            }
                                        }
                                    } else {
                                        if (!isOnCreate) {
                                            showNoLocationDialog();

//                                    if(!isButtonClicked) {
//                                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)) &&
//                                                (intContainerStatus == Constants.LiveLoadingDone)) {
//                                            btnDriverJob.setText(Constants.CONTAINER_STATUS_TYPES[16]);
//                                        }
//                                    }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(getActivity() != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.d("error", ">>error: " + error);
                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ContainerId", ContainerId);
                    params.put("DriverID", DriverID);
                    params.put("DeviceNumber", DeviceID);
                    params.put("CompanyID", CompanyID);
                    params.put("Status", Status);    // NOT USING ON API SIDE. SO PLEASE IGNORE "STATUS" FILED
                    params.put("LoginId", LoginId);
                    params.put("Latitude", Constants.CURRENT_LATITUDE);
                    params.put("Longitude", Constants.CURRENT_LONGITUDE);

                    return params;
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES); //DEFAULT_BACKOFF_MULT
            postRequest.setRetryPolicy(policy);
            getNextLocReq.add(postRequest);
        }
    }


    /*-------------- UPDATE CHESSIS NUMBER -------------------*/
    void UPDATE_CHESSIS_NUMBER(final String ContainerId, final String chessisNo,  final String containerNo,
                               final boolean isMsg, final String method, final String type){

        progressBar.setVisibility(View.VISIBLE);
        btnDriverJob.setEnabled(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPDATE_CHASSIS_NO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        int status ;
                        boolean isSuccess = false;

                        Log.d("Response ", ">>>Response: " + response);
                        if(getActivity() != null) {
                            try {
                                btnDriverJob.setEnabled(true);
                                progressBar.setVisibility(View.GONE);

                                obj = new JSONObject(response);
                                status = obj.getInt("Status");
                                if (status == 1)
                                    isSuccess = obj.getBoolean("Success");

                                if (isSuccess) {
                                    chassiesNoTV.setText(StrChassiesNo);
                                    contnrNoEditText.setText(ContainerNo);
                                    setTextOnView(ContainerNo, container_name_tv);


                                    if (method.equals("UC")) {
                                        StrNextStatus = String.valueOf(Constants.PickFromYardLocation);
                                        UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                                Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                                    } else if (method.equals("PC")) {
                                        StrNextStatus = String.valueOf(Constants.PickUp);
                                        PickContainer(Constants.PICKED_UP, StrContainerLoadType, "");
                                    } else if (method.equals("PF")) {
                                        new PickContainerWithFile().execute();
                                    } else if (method.equals("PC_PO_CONT")) {
                                        PickContainer(Constants.LiveLoading, type, StrPONumber);
                                    } else {
                                        if (isMsg)
                                            Constants.showToastMsg(btnDriverJob, "Chassis number updated.", VIEW_DURATION);
                                    }


                                } else {
                                    Constants.showToastMsg(btnDriverJob, "Chassis equipment number does not exist", VIEW_DURATION);
                                    StrChassiesNo = "";
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", ">>error: " +error);

                        if(getActivity() != null) {
                            btnDriverJob.setEnabled(true);
                            progressBar.setVisibility(View.GONE);

                            String errorStr = error.toString();
                            if (errorStr.contains("NoConnectionError")) {
                                errorStr = "Internet connection not working properly";
                            }
                            Constants.showToastMsg(btnDriverJob, errorStr, VIEW_DURATION);
                        }
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("ContainerId", ContainerId);
                params.put("ChessieName", chessisNo);
                params.put("ContainerNo", containerNo);
                params.put("DriverID", DriverID);
                params.put("DeviceNumber", DeviceID);
                params.put("CompanyID", CompanyID);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
        postRequest.setRetryPolicy(policy);
        updateChessisNumReq.add(postRequest);

    }



    /*-------------- Get Next Location API -------------------*/
    void GET_CONTAINER_DETAIL(final String ContainerId, final String OrderType){

        try {
            if (getActivity() != null) {

                StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_CONTAINER_DETAIL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response ", ">>>Response: " + response);
                                pickResNo = ""; pickResDate = ""; pickResTime = ""; dropResNo = ""; dropResDate = ""; dropResTime = "";
                                isUpdateStatusCalled = false;

                                if(getActivity() != null) {
                                    uploadImgBtn.setImageResource(R.drawable.upload_image);
                                    JSONObject obj, resultJson;
                                    int status;
                                    try {
                                        obj = new JSONObject(response);
                                        status = obj.getInt("Status");

                                        //me. get company sub locations
                                        GetSubLoadingLocations(StrContainerId);

                                        if (status == 1) {
                                            resultJson = new JSONObject(obj.getString("Result"));
                                            ExportFragment.containerExportList = new ArrayList<ContainerModel>();

                                            int containerStatus = 0;
                                            if (resultJson.getString("Status") != null &&
                                                    !resultJson.getString("Status").equalsIgnoreCase("null")) {
                                                containerStatus = resultJson.getInt("Status");

                                                IsPermissionForDeadCall = resultJson.getBoolean("IsPermissionForDeadCall");
                                                IsLoadLocationLeft = resultJson.getBoolean("IsLoadLocationLeft");
                                                intLegType = resultJson.getInt("LegType");
                                                StrMessageText = resultJson.getString("MessageText");
                                                IntTotalLocations = resultJson.getInt("TotalLocations");
                                                ContainerLoadTypeID = resultJson.getString("ContainerLoadTypeID");

                                                if (resultJson.getString("ConditionStatus").equals("1") ||
                                                        resultJson.getString("ConditionStatus").equals("2")) {
                                                    ConditionStatus = resultJson.getString("ConditionStatus");
                                                }

                                                if(!resultJson.isNull("isInspectionRequired") && resultJson.getBoolean("isInspectionRequired")){
                                                    inspReqTxtVw.setVisibility(View.VISIBLE);
                                                    inspReqTxtVw.setText(getString(R.string.insp_req));
                                                }else{
                                                    inspReqTxtVw.setVisibility(View.GONE);
                                                }
                                            }

                                            String truck = resultJson.getString("TrcukName");
                                            String savedTruck = Constants.getTruckNumber(getActivity());
                                            if(!truck.equals(savedTruck)){
                                                if(!isTruckChangeDialogShown) {
                                                    changeTruckDialog(savedTruck, truck);
                                                }
                                            }

                                            pickResNo = Constants.NullCheckJson(resultJson, "ReservationNo");
                                            pickResDate = Constants.ConvertDateFormatMMddyyyy(Constants.NullCheckJson(resultJson, "PickUpResDate"));
                                            pickResTime = Constants.NullCheckJson(resultJson, "PickUpResTime");

                                            dropResNo = Constants.NullCheckJson(resultJson, "DropOffReservationNo");
                                            dropResDate = Constants.ConvertDateFormatMMddyyyy( Constants.NullCheckJson(resultJson, "DropOffReservationDate"));
                                            dropResTime =  Constants.NullCheckJson(resultJson, "DropOffReservationTime");

                                            if(dropResTime.length() > 5){
                                                dropResTime = dropResTime.substring(0, 5);
                                            }

                                            if(pickResTime.length() > 5){
                                                pickResTime = pickResTime.substring(0, 5);
                                            }

                                            if (ContainerLoadTypeID.equals(Constants.Empty)) {
                                                loadTypeTxtVw.setText("Empty");
                                            } else if (ContainerLoadTypeID.equals(Constants.PartialLoad)) {
                                                loadTypeTxtVw.setText("Partial Load");
                                            } else {
                                                loadTypeTxtVw.setText("Loaded");
                                            }


                                            /*---------------- PARSE JSON of Container Details-----------------*/
                                            try {
                                                containerModel = Constants.containerModel(containerModel, resultJson, containerStatus,false, false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            ExportFragment.containerExportList.add(containerModel);

                                            /* ------------- GET CONTAINER DETAILS ------------ */
                                            SetExportContainerDetails(0);
                                            GetCompanySetting(StrCompanyId);

                                        } else {
                                            Constants.showToastMsg(btnDriverJob, "No data found" , VIEW_DURATION);
                                            getFragmentManager().popBackStack();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("error", ">>error: " +error);
                                isUpdateStatusCalled = false;

                                if(getActivity() != null) {
                                    String errorMsg = error.toString();
                                    if (errorMsg.contains("NoConnectionError")) {
                                        // Constants.showToastMsg(btnDriverJob, "NoConnectionError" , VIEW_DURATION);
                                    } else {
                                        Constants.showToastMsg(btnDriverJob, "" + error.toString(), VIEW_DURATION);
                                    }

                                    if (!StrNextStatus.equals(String.valueOf(Constants.LeftContainerAtYardWithChassis)) &&
                                            !StrNextStatus.equals(String.valueOf(Constants.LeftContainerAtYardWithoutChasis)) &&
                                            !StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithChassie)) &&
                                            !StrNextStatus.equals(String.valueOf(Constants.LeftContainerAtYardWithoutChasis))) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                 getFragmentManager().popBackStack();
                                            }
                                        }, 2000);

                                    }
                                }

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()  {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("ContainerId", ContainerId);
                        params.put("OrderType", OrderType);
                        params.put("DriverID", DriverID);
                        params.put("DeviceNumber", DeviceID);
                        params.put("LoginId", LoginId);
                        params.put("Latitude", Constants.CURRENT_LATITUDE);
                        params.put("Longitude", Constants.CURRENT_LONGITUDE);

                        params.put("CompanyID", CompanyID);

                        return params;
                    }
                };

                RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
                postRequest.setRetryPolicy(policy);
                getContDetailReq.add(postRequest);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void GET_UPDATE_REMARK(final String driverId,  final String containerId,
                                final String remarks, final String isUpdateRequest){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPDATE_REMARKS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;

                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);
                            if(obj.getInt("Status") == 1){
                                if (obj.getBoolean("Success") == true) {

                                    if(!obj.getString("Result").equals("null") ) {
                                        StrGetRemarks = obj.getString("Result");
                                        remarknoteV.setText(StrGetRemarks);

                                    }

                                    if(isUpdateRequest.equals("true")){
                                        Constants.showToastMsg(btnDriverJob, "Remarks Updated Successfully", VIEW_DURATION);
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
                        Log.d("error", ">>error: " +error);
                        String errorStr = error.toString();
                        if(errorStr.contains("NoConnectionError")){
                            errorStr = "Internet connection not working properly";
                        }
                        Constants.showToastMsg(btnDriverJob, errorStr, VIEW_DURATION);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("DriverId", driverId);
                params.put("ContainerId", containerId);
                params.put("Remarks", remarks);
                params.put("IsUpdateRequest", isUpdateRequest);
                params.put("DeviceNumber", DeviceID);
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                params.put("CompanyID", CompanyID);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
        postRequest.setRetryPolicy(policy);
        updateRemarkReq.add(postRequest);

    }







    /* ----------------- Media Dialog For Camera/Gallery---------------- */
    void mediaDialog(){

        try{
            if(mediaPickerD != null){
                if(mediaPickerD.isShowing())
                    mediaPickerD.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaPickerD = new Dialog(getActivity());
        mediaPickerD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = mediaPickerD.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mediaPickerD.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPickerD.setContentView(R.layout.popup_loading_status);

        Button leftWithChassisBtn, leftWithOutChassisBtn, liveLoadingBtn,  cancelBtn;
        leftWithChassisBtn = (Button)mediaPickerD.findViewById(R.id.leftWithChassisBtn);
        leftWithOutChassisBtn = (Button)mediaPickerD.findViewById(R.id.leftWithOutChassisBtn);
        liveLoadingBtn = (Button)mediaPickerD.findViewById(R.id.liveLoadingBtn);
        cancelBtn = (Button)mediaPickerD.findViewById(R.id.closeSBtn);



        liveLoadingBtn.setVisibility(View.GONE);
        leftWithChassisBtn.setText("Camera");
        leftWithOutChassisBtn.setText("Gallery");

        leftWithChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerD.dismiss();
                mediaIntent("camera", Camera);
            }
        });


        leftWithOutChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerD.dismiss();
                mediaIntent("gallery", Gallery);
            }
        });




        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerD.dismiss();
            }
        });
        mediaPickerD.show();

    }


    /* ----------------- Media Dialog For Camera/Gallery---------------- */
    void zoomInImgDialog(){

        final Dialog zoomInPicker = new Dialog(getActivity());
        zoomInPicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        zoomInPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zoomInPicker.setContentView(R.layout.popup_zoom_in_img);

        ImageView zoominImgVw = (ImageView) zoomInPicker.findViewById(R.id.zoominImgVw);
        Button jobDoneBtn = (Button)zoomInPicker.findViewById(R.id.jobDoneBtn);

        Animation animZoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        zoominImgVw.startAnimation(animZoomIn);

        jobDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomInPicker.dismiss();
                getFragmentManager().popBackStack();
            }
        });

        zoomInPicker.show();
    }



    /* ----------------- Media Dialog For Camera/Gallery---------------- */
    void changeTruckDialog(String oldTruck, String newTruck){

        try {
            if(truckChangeDialog != null && truckChangeDialog.isShowing()){
                truckChangeDialog.dismiss();
            }

            truckChangeDialog = new Dialog(getActivity());
            truckChangeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            truckChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            truckChangeDialog.setContentView(R.layout.popup_truck_change);
            truckChangeDialog.setCancelable(false);

            TextView truckDescTV = (TextView) truckChangeDialog.findViewById(R.id.truckDescTV);
            truckDescTV.setText(oldTruck + " to " + newTruck);

            Button truckChangeBtn = (Button) truckChangeDialog.findViewById(R.id.truckChangeBtn);
            truckChangeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isTruckChangeDialogShown = true;
                    truckChangeDialog.dismiss();
                }
            });

            truckChangeDialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /* ----------------- Call Media Activity ---------------- */
    void mediaIntent(String from, int Source){
        Intent i = new Intent(getActivity(), MediaActivity.class);
        i.putExtra("type", from);
        startActivityForResult(i, Source );
    }


    void PickContainer(int loadType, String loadStatus, String PONumber){

        ContainerNo = contnrNoEditText.getText().toString().trim().toUpperCase();

        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
            URL = API_URL.UPDATE_CONTAINER_LOADTYPE;
        else
            URL = API_URL.UPDATE_CONTNR_LOADTYPE_IMPORT;

        if(loadType == Constants.LiveLoading || loadType == Constants.AssignForLoadingLocation ) {

            if(isLoadingImageRequired){
                if (!imagePath.equals("")) {
                    intContainerStatus = Constants.LiveLoadingDone;
                    new UploadPODDocImage().execute();
                } else{
                    Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                }

            }else{
                intContainerStatus = Constants.LiveLoadingDone;
                UPDATE_CONTAINER_LOAD_TYPE(URL, StrContainerId, String.valueOf(intContainerStatus), loadStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, StrGrossWeight, StrSealNumber, PONumber, ContainerNo);
            }
        }else{

            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {

                if(intContainerStatus == Constants.ArrivedAtPickUpLoc ) {
                    StrNextStatus = String.valueOf(Constants.PickUp);

                    if(ImportPickUpImage) {
                        if (!imagePath.equals("")) {
                            if (ConditionStatus.equals("")) {
                                ContainerConditionDialog();
                            } else{
                                new PickContainerWithFile().execute();
                            }
                        } else{
                            Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                        }
                    }else{
                        if (ConditionStatus.equals("")) {
                            ContainerConditionDialog();
                        } else{
                            new PickContainerWithFile().execute();
                        }

                    }

                }else{

                    TareWeight = "null";      ContainerNo = "null";
                    UPDATE_CONTAINER_LOAD_TYPE(URL, StrContainerId, String.valueOf(intContainerStatus), loadStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, StrGrossWeight, StrSealNumber, PONumber, ContainerNo);
                }
            } else{
                TareWeight = tareWeightEditText.getText().toString().trim();
                ContainerNo = contnrNoEditText.getText().toString().trim().toUpperCase();
                StrChassiesNo   = chassiesNoTV.getText().toString().trim();

                if (ContainerNo.length() > 0 && isContNumValid) {
                    if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
                        if (TareWeightRequiredImport) {

                            if (TareWeight.length() > 0) {
                                if (StrChassiesNo.length() > 0) {
                                    UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PF", "");
                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Enter Chassis number first", VIEW_DURATION);
                                }
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Enter tare weight first", VIEW_DURATION);
                            }
                        } else{
                            if (StrChassiesNo.length() > 0) {
                                UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PF", "");
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Enter Chassis number first", VIEW_DURATION);
                            }
                        }
                    } else{
                        if (TareWeightRequiredExport) {

                            if (TareWeight.length() > 0) {
                                if (StrChassiesNo.length() > 0) {
                                    UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PF", "");
                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Enter Chassis number first", VIEW_DURATION);
                                }
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Enter tare weight first", VIEW_DURATION);
                            }
                        } else{
                            if (StrChassiesNo.length() > 0) {
                                UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PF", "");
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Enter Chassis number first", VIEW_DURATION);
                            }
                        }
                    }
                }else{
                    Constants.showToastMsg(btnDriverJob, "Enter valid container number first", VIEW_DURATION);
                    contnrNoEditText.requestFocus();
                }
            }
        }
    }


    void updateStatus(String status, String msg){

        isUpdateStatusCalled = true;
        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
            URL = API_URL.UPDATE_CONTAINER_STATUS;
        else
            URL = API_URL.UPDATE_CONTNR_STATUS_IMPORT;

        UPDATE_CONTAINER_STATUS(URL, StrContainerId, status, msg,
                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );
    }





    /* ----------------- Container Status Dialog ---------------- */
    void loadingStatusDialog(final int status) {

        try{
            if(mediaPickerYard != null){
                if(mediaPickerYard.isShowing())
                    mediaPickerYard.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        mediaPickerYard = new Dialog(getActivity());
        mediaPickerYard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = mediaPickerYard.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mediaPickerYard.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPickerYard.setContentView(R.layout.popup_loading_status);

        Button leftWithChassisBtn, leftWithOutChassisBtn, liveLoadingBtn, deadCallBtn,
                orderCancelBtn, cancelBtn;
        View deadCallView, orderCancelView, orderCancelView2;

        deadCallView = (View) mediaPickerYard.findViewById(R.id.deadCallView);
        orderCancelView = (View) mediaPickerYard.findViewById(R.id.orderCancelView);
        orderCancelView2 = (View) mediaPickerYard.findViewById(R.id.orderCancelView2);

        leftWithChassisBtn = (Button) mediaPickerYard.findViewById(R.id.leftWithChassisBtn);
        leftWithOutChassisBtn = (Button) mediaPickerYard.findViewById(R.id.leftWithOutChassisBtn);
        liveLoadingBtn = (Button) mediaPickerYard.findViewById(R.id.liveLoadingBtn);
        deadCallBtn = (Button) mediaPickerYard.findViewById(R.id.deadCallBtn);
        orderCancelBtn = (Button) mediaPickerYard.findViewById(R.id.orderCancelBtn);
        cancelBtn = (Button) mediaPickerYard.findViewById(R.id.closeSBtn);
        TextView liveLoadingView = (TextView)mediaPickerYard.findViewById(R.id.liveLoadingView);

        if (status == Constants.LiveLoading) {

            leftWithChassisBtn.setText("Partial Load");

            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                leftWithOutChassisBtn.setText("Loaded");
                deadCallBtn.setText("Left For Live Loading");
            } else{
                leftWithOutChassisBtn.setText("Empty");
                deadCallBtn.setText("Left For Live Unloading");
            }

            liveLoadingBtn.setVisibility(View.GONE);
            deadCallBtn.setVisibility(View.VISIBLE);
            deadCallView.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);

        } else if (status == Constants.NO_LOCATION || status == 0) {
            liveLoadingBtn.setVisibility(View.GONE);
        } else if (status == Constants.ArrivedAtYardlocation) {
            liveLoadingBtn.setVisibility(View.GONE);

        } else if (status == Constants.ArrivedAtLoadingLocation) {

            if (CONTAINER_JOB_TYPE.equals("1")) {
                liveLoadingBtn.setText("Live Unloading");

                if(IntTotalLocations == 1) {
                    if (ShowLiveLoadingImport) {
                        liveLoadingBtn.setVisibility(View.VISIBLE);
                    } else{
                        liveLoadingBtn.setVisibility(View.GONE);
                    }
                }else{
                    liveLoadingBtn.setVisibility(View.VISIBLE);
                }


            } else if (CONTAINER_JOB_TYPE.equals("2")) {
                if (IntTotalLocations == 1) {
                    if (ShowLiveLoadingExport) {
                        liveLoadingBtn.setVisibility(View.VISIBLE);
                    } else{
                        liveLoadingBtn.setVisibility(View.GONE);
                    }
                } else{
                    liveLoadingBtn.setVisibility(View.VISIBLE);
                }
            }



                    if (IsPermissionForDeadCall == true) {
                deadCallView.setVisibility(View.VISIBLE);
                deadCallBtn.setVisibility(View.VISIBLE);
            }

            if (CONTAINER_JOB_TYPE.equals("1")) {
                if (CancelOrderImport) {
                    orderCancelView.setVisibility(View.VISIBLE);
                    orderCancelBtn.setVisibility(View.VISIBLE);
                    orderCancelView2.setVisibility(View.VISIBLE);
                } else{
                    orderCancelView.setVisibility(View.GONE);
                    orderCancelBtn.setVisibility(View.GONE);
                }
            } else if (CONTAINER_JOB_TYPE.equals("2")) {

                if (CancelOrderExport) {
                    orderCancelView.setVisibility(View.VISIBLE);
                    orderCancelBtn.setVisibility(View.VISIBLE);
                    orderCancelView2.setVisibility(View.VISIBLE);
                } else{
                    orderCancelView.setVisibility(View.GONE);
                    orderCancelBtn.setVisibility(View.GONE);
                }
            }

            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setText("Close");
        }



        if(!showLiveLoadUnLoad){
            liveLoadingBtn.setVisibility(View.GONE);
            liveLoadingView.setVisibility(View.GONE);
        }




        liveLoadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                    msg = Constants.CONTAINER_STATUS_TYPES[10];
                else
                    msg = Constants.CONTAINER_STATUS_TYPES[32];

                    StrNextStatus = String.valueOf(Constants.LiveLoading);
                    updateStatus(StrNextStatus, msg);

                mediaPickerYard.dismiss();
            }
        });


        leftWithChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    //  new MyLog(Constants.IS_LOG_SHOWN,Constants.CONTAINER_STATUS_TYPES[7]);
                    StrContainerLoadType = Constants.PartialLoad;
                    if (status == Constants.LiveLoading) {

                        if (btnDriverJob.getText().toString().equals(Constants.CONTAINER_STATUS_TYPES[16])) {
                            StrNextStatus = String.valueOf(Constants.Delivered);

                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                                URL = API_URL.UPDATE_CONTAINER_STATUS;
                            else
                                URL = API_URL.UPDATE_CONTNR_STATUS_IMPORT;

                            UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);

                        } else{
                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                if (!imagePath.equals("")) {
                                    loadingFieldsDialog(StrContainerLoadType);
                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                                }

                            } else{
                                PickContainer(Constants.LiveLoading, StrContainerLoadType, "");
                            }

                        }


                    } else if (status == Constants.NO_LOCATION || status == Constants.ArrivedAtYardlocation) {
                        StrNextStatus = String.valueOf(Constants.LeftContainerAtYardWithChassis);
                        updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[7]);
                    } else{
                        if (CONTAINER_JOB_TYPE.equals("1")) {
                            if (!imagePath.equals("")) {
                                StrNextStatus = String.valueOf(Constants.LeftContainerWithChassie);
                                updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[7]);
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                            }
                        }else{
                            StrNextStatus = String.valueOf(Constants.LeftContainerWithChassie);
                            updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[7]);
                        }
                    }


                    mediaPickerYard.dismiss();

            }
        });


        leftWithOutChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    StrContainerLoadType = Constants.Loaded;
                    if (status == Constants.LiveLoading) {
                        mediaPickerYard.dismiss();


                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {

                            if (!imagePath.equals("")) {
                                loadingFieldsDialog(StrContainerLoadType);
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                            }



                        } else{
                            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                            StrContainerLoadType = Constants.Empty;

                            if(imagePath.length() > 0) {
                                PickContainer(Constants.LiveLoading, StrContainerLoadType, "");
                              //  new UploadPODDocImage().execute();
                            }else{
                                if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
                                    isDirectCallPOD = true;
                                    isLeftChessisBtn = false;
                                }

                                Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                            }
                        }

                    } else if (status == Constants.NO_LOCATION || status == Constants.ArrivedAtYardlocation) {
                        mediaPickerYard.dismiss();
                        StrNextStatus = String.valueOf(Constants.LeftContainerAtYardWithoutChasis);

                            updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[6]);

                    } else{
                        mediaPickerYard.dismiss();
                        if (CONTAINER_JOB_TYPE.equals("1")) {
                            if (!imagePath.equals("")) {
                                StrNextStatus = String.valueOf(Constants.LeftContainerWithoutChassie);
                                updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[6]);
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                            }

                        }else{
                            StrNextStatus = String.valueOf(Constants.LeftContainerWithoutChassie);
                            updateStatus(StrNextStatus, Constants.CONTAINER_STATUS_TYPES[6]);
                        }
                    }

                  }

        });


        deadCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerYard.dismiss();
                if(status == Constants.LiveLoading){

                    StrNextStatus = String.valueOf(Constants.LeftContainerWithChassie);
                    StrMessage = "Left container with chassis";

                    if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {

                        UPDATE_CONTAINER_STATUS(API_URL.UPDATE_CONTAINER_STATUS, StrContainerId, StrNextStatus, StrMessage,
                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                    }else{
                        if(!imagePath.equals("")) {
                            new UploadPODDocImage().execute();

                           /* UPDATE_CONTAINER_STATUS(Constants.UPDATE_CONTAINER_STATUS, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                    Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);*/
                        }else{
                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
                                isDirectCallPOD = true;
                                isLeftChessisBtn = true;
                            }
                            Constants.showToastMsg(btnDriverJob, "Upload image first.", VIEW_DURATION);
                        }
                    }

                }else{
                    StrNextStatus = String.valueOf(Constants.DeadCall);

                    UPDATE_CONTAINER_STATUS(API_URL.UPDATE_CONTAINER_STATUS, StrContainerId, StrNextStatus, StrMessage,
                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                            Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                }

            }
        });


        orderCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerYard.dismiss();

                StrNextStatus = String.valueOf(Constants.OrderCancel);

            UPDATE_CONTAINER_STATUS(API_URL.UPDATE_CONTAINER_STATUS, StrContainerId, StrNextStatus, StrMessage,
                                    Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
            Constants.CURRENT_LATITUDE , Constants.CURRENT_LONGITUDE, ContainerNo  );

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPickerYard.dismiss();
            }
        });

        mediaPickerYard.show();

    }

    /* ----------------- Container Status For Empty Dialog ---------------- */
    void loadingStatusForEmptyDialog(){

        try{
            if(mediaPickerLoad != null){
                if(mediaPickerLoad.isShowing())
                    mediaPickerLoad.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        mediaPickerLoad = new Dialog(getActivity());
        mediaPickerLoad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = mediaPickerLoad.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mediaPickerLoad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPickerLoad.setContentView(R.layout.popup_loading_status);

        Button partialLoadBtn, loadedBtn, liveLoadingBtn, cancelBtn;


        partialLoadBtn = (Button)mediaPickerLoad.findViewById(R.id.leftWithChassisBtn);
        loadedBtn = (Button)mediaPickerLoad.findViewById(R.id.leftWithOutChassisBtn);
        liveLoadingBtn = (Button)mediaPickerLoad.findViewById(R.id.liveLoadingBtn);
        cancelBtn = (Button)mediaPickerLoad.findViewById(R.id.closeSBtn);

        liveLoadingBtn.setVisibility(View.GONE);
        partialLoadBtn.setText("Partial Load");
        loadedBtn.setText("Loaded");



        partialLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPickerLoad.dismiss();

                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    loadingFieldsDialog(Constants.PartialLoad);
                }else{
                    PickContainer(Constants.LiveLoading, StrContainerLoadType, "");
                }


            }
        });


        loadedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPickerLoad.dismiss();
                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    StrContainerLoadType = Constants.Loaded;
                    loadingFieldsDialog(Constants.Loaded);


                }else{
                    StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                    PickContainer(Constants.LiveLoading, Constants.Empty, "");
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPickerLoad.dismiss();
            }
        });

        mediaPickerLoad.show();

    }

    /* ----------------- Loading Fields Dialog ---------------- */
    void loadingFieldsDialog(final String type){

        try{
            if(loadingFieldPicker != null){
                if(loadingFieldPicker.isShowing())
                    loadingFieldPicker.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        loadingFieldPicker = new Dialog(getActivity());
        loadingFieldPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadingFieldPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingFieldPicker.setContentView(R.layout.popup_loading_fields);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(loadingFieldPicker.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        loadingFieldPicker.getWindow().setAttributes(lp);

        StrPONumber = "";
        final Button btnLoadingJob,  btnCancelLoadingJob;
       final EditText sealNoEditText, grossWeightEditText, POEditText, ChessesEditText, ContrEditText;
        TextInputLayout ChessesInputType = (TextInputLayout)loadingFieldPicker.findViewById(R.id.ChessesInputType);
        TextInputLayout ContrInputType = (TextInputLayout)loadingFieldPicker.findViewById(R.id.ContrInputType);

        btnLoadingJob = (Button)loadingFieldPicker.findViewById(R.id.btnLoadingJob);
        btnCancelLoadingJob = (Button)loadingFieldPicker.findViewById(R.id.btnCancelLoadingJob);

        sealNoEditText = (EditText)loadingFieldPicker.findViewById(R.id.sealNoEditText);
        grossWeightEditText = (EditText)loadingFieldPicker.findViewById(R.id.grossWeightEditText);
        POEditText = (EditText)loadingFieldPicker.findViewById(R.id.POEditText);
        ChessesEditText = (EditText)loadingFieldPicker.findViewById(R.id.ChessesEditText);
        ContrEditText = (EditText)loadingFieldPicker.findViewById(R.id.ContrEditText);

        if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT)) && intContainerStatus == Constants.AssignForLoadingLocation ) {
            TextInputLayout POInputType = (TextInputLayout)loadingFieldPicker.findViewById(R.id.POInputType);
            POInputType.setVisibility(View.GONE);
        }
            if ( intContainerStatus == Constants.ArrivedAtPickUpLoc || intContainerStatus == Constants.AssignForLoadingLocation || intContainerStatus == Constants.AssignForYardLocation ||
                    intContainerStatus == Constants.PickFromYardLocation) {

                    ChessesInputType.setVisibility(View.VISIBLE);
                    ContrInputType.setVisibility(View.VISIBLE);

            }

        ChessesEditText.setText(StrChassiesNo);
        ContrEditText.setText(ContainerNo);

        if(type.equals(Constants.PartialLoad ) || CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.IMPORT))) {
            TextInputLayout sealNoInputType = (TextInputLayout)loadingFieldPicker.findViewById(R.id.sealNoInputType);
            TextInputLayout grossWeightInputType = (TextInputLayout)loadingFieldPicker.findViewById(R.id.grossWeightInputType);
            sealNoInputType.setVisibility(View.GONE);
            grossWeightInputType.setVisibility(View.GONE);
        }

        btnLoadingJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrSealNumber = sealNoEditText.getText().toString().trim();
                StrGrossWeight = grossWeightEditText.getText().toString().trim();
                StrPONumber = POEditText.getText().toString();
                Constants.hideKeyboardView(getActivity(), POEditText);

                if(type.equals(Constants.Loaded) && CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    if(SealRequiredExport) {
                        if (StrSealNumber.length() > 0) {
                            if (GrossWeightRequiredExport) {
                                if (StrGrossWeight.length() > 0) {
                                    if (intContainerStatus == Constants.AssignForLoadingLocation || intContainerStatus == Constants.AssignForYardLocation ||
                                            intContainerStatus == Constants.PickFromYardLocation) {
                                        if (ChessesEditText.getText().toString().length() > 0) {
                                            StrChassiesNo = ChessesEditText.getText().toString();
                                            if (ContrEditText.getText().toString().length() > 0) {
                                                ContainerNo = ContrEditText.getText().toString();
                                                loadingFieldPicker.dismiss();
                                                UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PC_PO_CONT", type);
                                            } else{
                                                Constants.showToastMsg(btnLoadingJob, "Enter container number first", VIEW_DURATION);
                                            }
                                        } else{
                                            Constants.showToastMsg(btnLoadingJob, "Enter Chassis number first", VIEW_DURATION);
                                        }
                                    } else{
                                        loadingFieldPicker.dismiss();
                                        PickContainer(Constants.LiveLoading, type, StrPONumber);
                                    }

                                } else{
                                    Constants.showToastMsg(btnDriverJob, "Enter gross weight first", VIEW_DURATION);
                                    grossWeightEditText.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            grossWeightEditText.setSelection(grossWeightEditText.getText().toString().length());
                                            grossWeightEditText.requestFocus();
                                        }
                                    });

                                }
                            }


                        } else{
                            Constants.showToastMsg(btnDriverJob, "Enter seal number first", VIEW_DURATION);
                            sealNoEditText.setSelection(sealNoEditText.getText().toString().length());
                            sealNoEditText.requestFocus();
                        }

                    }else{
                        if (intContainerStatus == Constants.AssignForLoadingLocation || intContainerStatus == Constants.AssignForYardLocation ||
                                intContainerStatus == Constants.PickFromYardLocation) {
                            if (ChessesEditText.getText().toString().length() > 0) {
                                StrChassiesNo = ChessesEditText.getText().toString();
                                if (ContrEditText.getText().toString().length() > 0) {
                                    ContainerNo = ContrEditText.getText().toString();
                                    loadingFieldPicker.dismiss();
                                    UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PC_PO_CONT", type);
                                } else{
                                    Constants.showToastMsg(btnLoadingJob, "Enter container number first", VIEW_DURATION);
                                }
                            } else{
                                Constants.showToastMsg(btnLoadingJob, "Enter Chassis number first", VIEW_DURATION);
                            }
                        } else{
                            loadingFieldPicker.dismiss();
                            PickContainer(Constants.LiveLoading, type, StrPONumber);
                        }
                    }
     //new me
                }else{
                    if(intContainerStatus == Constants.AssignForLoadingLocation || intContainerStatus == Constants.AssignForYardLocation ||
                            intContainerStatus == Constants.PickFromYardLocation ) {
                        if (ChessesEditText.getText().toString().length() > 0) {
                            StrChassiesNo = ChessesEditText.getText().toString();
                            if (ContrEditText.getText().toString().length() > 0) {
                                ContainerNo = ContrEditText.getText().toString();
                                loadingFieldPicker.dismiss();
                                UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PC_PO_CONT", type);
                            } else{
                                Constants.showToastMsg(btnDriverJob, "Enter container number first", VIEW_DURATION);
                            }
                        } else{
                            Constants.showToastMsg(btnDriverJob, "Enter Chassis number first", VIEW_DURATION);
                        }
                    }else{
                        loadingFieldPicker.dismiss();
                        PickContainer(Constants.LiveLoading, type, StrPONumber);
                    }
                }


            }

        });



        btnCancelLoadingJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrSealNumber = "";
                StrGrossWeight = "";
                StrPONumber = "";
                Constants.hideKeyboardView(getActivity(), ContrEditText);
                loadingFieldPicker.dismiss();
            }
        });


        loadingFieldPicker.show();


    }

    /* ----------------- Update Chesses Number Dialog ---------------- */
    void UpdateChessisDialog(final String chessisNumber, final boolean isAssign, final boolean isMsg,
                             final boolean UPDATE_CONTAINER_STATUS, final boolean isPickedUp){

        try{
            if(chessisPicker != null){
                if(chessisPicker.isShowing())
                    chessisPicker.dismiss();
            }
        }catch (Exception e){  e.printStackTrace(); }

        chessisPicker = new Dialog(getActivity());
        chessisPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        chessisPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chessisPicker.setContentView(R.layout.popup_loading_fields);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(chessisPicker.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        chessisPicker.getWindow().setAttributes(lp);

        final Button btnLoadingJob,  btnCancelLoadingJob;
        final EditText chessisNoEditText, containerNoEditText;
        TextInputLayout grossWeightInputType, POInputType, chessisNoInputType;

        btnLoadingJob = (Button)chessisPicker.findViewById(R.id.btnLoadingJob);
        btnCancelLoadingJob = (Button)chessisPicker.findViewById(R.id.btnCancelLoadingJob);

        chessisNoEditText = (EditText)chessisPicker.findViewById(R.id.sealNoEditText);
        containerNoEditText = (EditText)chessisPicker.findViewById(R.id.grossWeightEditText);

        grossWeightInputType = (TextInputLayout)chessisPicker.findViewById(R.id.grossWeightInputType);
        POInputType = (TextInputLayout)chessisPicker.findViewById(R.id.POInputType);
        chessisNoInputType = (TextInputLayout)chessisPicker.findViewById(R.id.sealNoInputType);

        chessisNoEditText.setHint("Chassis Number");
        chessisNoInputType.setHint("Chassis Number");
        containerNoEditText.setHint("Container Number");
        grossWeightInputType.setHint("Container Number");
        containerNoEditText.setText(ContainerNo);
        containerNoEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_NORMAL);


        if(isAssign) {
            grossWeightInputType.setVisibility(View.GONE);
            btnLoadingJob.setText("Update");
        }else{
            TextView ConfirmTitleTV = (TextView)chessisPicker.findViewById(R.id.ConfirmTitleTV);
            ConfirmTitleTV.setVisibility(View.VISIBLE);
            btnLoadingJob.setText("Confirm");
        }

        POInputType.setVisibility(View.GONE);
        chessisNoEditText.setText(chessisNumber);

            btnLoadingJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        StrChassiesNo   = chessisNoEditText.getText().toString().trim();
                        ContainerNo     = containerNoEditText.getText().toString().trim();

                    Constants.hideKeyboardView(getActivity(), containerNoEditText);

                    if (Constants.isNetworkAvailable(getActivity())) {
                        if(StrChassiesNo.length() > 0){
                            if(isAssign){
                                chessisPicker.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, isMsg, "", "");
                                    } }, 300);
                            }else{

                                if(isupdateChessisBtn){
                                    isupdateChessisBtn = false;
                                    chessisPicker.dismiss();
                                    CallUpdateChessesMethod(isPickedUp, UPDATE_CONTAINER_STATUS, isMsg);
                                }else{

                                    if (ContainerNo.length() > 0) {
                                        chessisPicker.dismiss();
                                        CallUpdateChessesMethod(isPickedUp, UPDATE_CONTAINER_STATUS, isMsg);
                                    } else{
                                        Constants.showToastMsg(btnLoadingJob, "Enter container number first.", VIEW_DURATION);
                                    }
                                }
                            }

                        }else{
                            Constants.showToastMsg(btnLoadingJob, "Enter chassis number first.", VIEW_DURATION);
                        }
                    } else{
                        Constants.showToastMsg(btnLoadingJob, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                    }

                }
            });


            btnCancelLoadingJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.hideKeyboardView(getActivity(), containerNoEditText);

                    chessisPicker.dismiss();
                }
            });




        chessisPicker.show();

    }


    void CallUpdateChessesMethod(boolean isPickedUp, boolean UPDATE_CONTAINER_STATUS, final boolean isMsg){
        if(isPickedUp){
            UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "PC", "");
        }else{
            if (UPDATE_CONTAINER_STATUS) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, false, "UC", "");
                    }
                }, 300);

            } else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UPDATE_CHESSIS_NUMBER(StrContainerId, StrChassiesNo, ContainerNo, isMsg, "", "");
                    }
                }, 300);
            }
        }
    }

    /* ----------------- Container Condition Dialog ---------------- */
    void ContainerConditionDialog(){

        try{
            if(contConditionPicker != null){
                if(contConditionPicker.isShowing())
                    contConditionPicker.dismiss();
            }
        }catch (Exception e){  e.printStackTrace(); }


        contConditionPicker = new Dialog(getActivity());
        contConditionPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = contConditionPicker.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        contConditionPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contConditionPicker.setContentView(R.layout.popup_loading_status);

        Button goodConditionBtn, badConditionBtn, liveLoadingBtn, cancelBtn;

        goodConditionBtn = (Button)contConditionPicker.findViewById(R.id.leftWithChassisBtn);
        badConditionBtn = (Button)contConditionPicker.findViewById(R.id.leftWithOutChassisBtn);
        liveLoadingBtn = (Button)contConditionPicker.findViewById(R.id.liveLoadingBtn);
        cancelBtn = (Button)contConditionPicker.findViewById(R.id.closeSBtn);
        TextView jobtitleview = (TextView)contConditionPicker.findViewById(R.id.jobtitleview);

        goodConditionBtn.setText("Good");
        badConditionBtn.setText("Bad");
        liveLoadingBtn.setTextColor(getResources().getColor(R.color.gray_text1) );
        liveLoadingBtn.setText("Set Container Condition");
        liveLoadingBtn.setEnabled(false);

        liveLoadingBtn.setVisibility(View.VISIBLE);
        jobtitleview.setVisibility(View.GONE);

        goodConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConditionStatus = "1";
                if (Constants.isNetworkAvailable(getActivity())) {
                    contConditionPicker.dismiss();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new PickContainerWithFile().execute();
                        }
                    },300);
                } else{
                    Constants.showToastMsg(btnDriverJob, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }
                contConditionPicker.dismiss();
            }
        });


        badConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConditionStatus = "2";

                if (Constants.isNetworkAvailable(getActivity())) {
                        contConditionPicker.dismiss();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new PickContainerWithFile().execute();
                            }
                        },300);
                } else{
                    Constants.showToastMsg(btnDriverJob, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }
                contConditionPicker.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConditionStatus = "";
                contConditionPicker.dismiss();
            }
        });

        contConditionPicker.show();

    }


    /* ----------------- No Next Location Dialog ---------------- */
    private void showNoLocationDialog() {

        if(isButtonClicked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            builder.setIcon(R.drawable.no_location);
            builder.setTitle(getString(R.string.no_loc));
            builder.setMessage(getString(R.string.self_assign_location));
            builder.setCancelable(true);

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                FragmentManager fragManager;
                                SelfAssignDetailFragment selfAssignDetailFragment = new SelfAssignDetailFragment();

                                Constants.BUNDLE.putBoolean("Is_Self_Location", false);
                                Constants.BUNDLE.putString("jobType", CONTAINER_JOB_TYPE);
                                Constants.BUNDLE.putString("contNo", ContainerNo);
                                Constants.BUNDLE.putString("StrContainerId", StrContainerId);
                                Constants.BUNDLE.putInt("position", itemPosition);

                                selfAssignDetailFragment.setArguments(Constants.BUNDLE);

                                fragManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTran = fragManager.beginTransaction();
                                fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                        android.R.anim.fade_in, android.R.anim.fade_out);
                                fragmentTran.replace(R.id.job_fragment, selfAssignDetailFragment);
                                fragmentTran.addToBackStack("job_detail");
                                fragmentTran.commit();
                            } catch (Exception e) {
                            }
                        }
                    });


            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
        }

        isButtonClicked = false;

    }


    /* ----------------- Next Location Description Dialog ---------------- */
    private void NextLocationDescDialog(final String loc, final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.next_loc) + " (" + loc + ")");
        builder.setMessage(Html.fromHtml(address));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    /* ----------------- document type Dialog ---------------- */
    private void showDocTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.selct_doc_type));
        builder.setCancelable(false);

        final String pickUpDocBtn = getString(R.string.picup_doc);
        final String podDocBtn = getString(R.string.pod_doc);
        selectedDocType = "";

        builder.setPositiveButton(pickUpDocBtn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedDocType = pickUpDocBtn;
                        isDirectCallPOD = true;
                        isImageUploadBtnClick = true;
                        mediaDialog();

                        dialog.dismiss();
                    }
                });


        builder.setNegativeButton(podDocBtn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedDocType = podDocBtn;
                        isDirectCallPOD = true;
                        isImageUploadBtnClick = true;
                        mediaDialog();

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }



    /* ----------------- Container Condition Dialog ---------------- */
    private void showUploadDocDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setMessage(getString(R.string.want_to_Upload_Del_Doc));
        builder.setCancelable(false);

        String positiveText = getString(R.string.yes);
        String negativeText = getString(R.string.no);

        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        try{
                            mediaDialog();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });


        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        GET_CONTAINER_DETAIL(StrContainerId, CONTAINER_JOB_TYPE);
                       /* try {
                            getFragmentManager().popBackStack();
                        }catch (Exception e){}*/
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    /* ----------------- Upload POD Doucument to Server ---------------- */
    private class UploadPODDocImage extends AsyncTask<String, String, String>
    {


        ProgressDialog p;
        String strResponse = "", URL = "";
        com.squareup.okhttp.Response response;
        com.squareup.okhttp.Request request = null;
        com.squareup.okhttp.MultipartBuilder builderNew;
        File file;
        String driverId = Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity());


        @Override
        protected void onPreExecute() {
            btnDriverJob.setEnabled(false);
            p = new ProgressDialog(getActivity());
            p.setMessage("Loading ...");
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                    URL = API_URL.UPLOAD_DELIVERY_DOCUMENT;

                    String exportStatus = "";
                    if(StrNextStatus.equals(String.valueOf(Constants.LiveLoading)) ||
                            StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithChassie)) ||
                            StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithoutChassie))  ||
                            StrNextStatus.equals(String.valueOf(Constants.LiveLoadingDone))){
                        exportStatus = String.valueOf(Constants.LoadingDocument);
                    }else{
                        exportStatus = String.valueOf(Constants.PODDocument);
                    }


                    builderNew = new com.squareup.okhttp.MultipartBuilder().type(com.squareup.okhttp.MultipartBuilder.FORM)
                            .addFormDataPart("ContainerId", StrContainerId)
                            .addFormDataPart("DriverId",  driverId)
                            .addFormDataPart("Status",  exportStatus);
                }else{
                    URL = API_URL.UPLOAD_DELIVERY_DOC_IMPORT;
                    String importStatus ;

                    if(StrNextStatus.equals(String.valueOf(Constants.LiveLoadingDone)) ||
                            StrNextStatus.equals(String.valueOf(Constants.AssignForLoadingLocation))  ||
                            StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithChassie))  ||
                            StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithoutChassie)) ||
                            StrContainerLoadType.equals(Constants.PartialLoad)
                            ){
                        importStatus = String.valueOf(Constants.UnloadingDocument);
                    }else{
                        importStatus = String.valueOf(Constants.PODDocument);
                    }

                    builderNew = new com.squareup.okhttp.MultipartBuilder().type(com.squareup.okhttp.MultipartBuilder.FORM)
                            .addFormDataPart("ContainerId", StrContainerId)
                            .addFormDataPart("DriverId", driverId )
                            .addFormDataPart("DeviceNumber", DeviceID)
                            .addFormDataPart("Status",  importStatus   )
                            .addFormDataPart("Latitude",  Constants.CURRENT_LATITUDE   )
                            .addFormDataPart("Longitude",  Constants.CURRENT_LONGITUDE   );

                }



                file = new File(imagePath);
                file.getName();
                if (file.exists()) {
                    builderNew.addFormDataPart("myFile", file.getName(),
                            com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("image/jpeg"), new File(file.toString())));
                }

                com.squareup.okhttp.RequestBody requestBody = builderNew.build();
                request = new com.squareup.okhttp.Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build();

                com.squareup.okhttp.OkHttpClient client = new com.squareup.okhttp.OkHttpClient();
                client.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(30, TimeUnit.SECONDS);
                response = client.newCall(request).execute();
                strResponse = response.body().string();

            } catch (Exception e) {
                e.printStackTrace();

            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            if(p != null){
                p.dismiss();
            }

            Log.e("String Response", ">>>>   " + result);

            try {
                btnDriverJob.setEnabled(true);
                JSONObject obj = new JSONObject(result);
                String response = "";
                response = obj.getString("Status");
                if (response.equalsIgnoreCase("true")) {
                    IsImageUploaded = true;
                    if(isImageUploadBtnClick) {
                        Constants.showToastMsg(btnDriverJob, "Image uploaded successfully", VIEW_DURATION);
                    }else{

                        if(!isUpdateStatusCalled) {
                            if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                                URL = API_URL.UPDATE_CONTAINER_STATUS;
                            else
                                URL = API_URL.UPDATE_CONTNR_STATUS_IMPORT;

                            if (StrNextStatus.equals(String.valueOf(Constants.POD))) {
                                imagePath = "";
                                uploadImgBtn.setImageResource(R.drawable.upload_image);
                                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                        Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                            } else {

                                if (StrNextStatus.equals(String.valueOf(Constants.LeftContainerWithChassie))) {
                                    UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                                            Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                            Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                                } else {
                                    if (StrContainerLoadType.equals(Constants.PartialLoad) ||
                                            StrContainerLoadType.equals(Constants.Empty) ||
                                            StrContainerLoadType.equals(Constants.Loaded) ||
                                            StrNextStatus.equals(String.valueOf(Constants.LiveLoading))) {

                                        if (CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)))
                                            URL = API_URL.UPDATE_CONTAINER_LOADTYPE;
                                        else
                                            URL = API_URL.UPDATE_CONTNR_LOADTYPE_IMPORT;

                                        String loadStatus = "";
                                        imagePath = "";
                                        uploadImgBtn.setImageResource(R.drawable.upload_image);
                                        if (StrContainerLoadType.equals(Constants.Empty) ) { //&& CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT)
                                            StrMessage = "Empty";
                                            loadStatus = Constants.Empty;
                                            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                                        } else if (StrContainerLoadType.equals(Constants.Loaded)) {
                                            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                                            StrMessage = Constants.CONTAINER_STATUS_TYPES[9];
                                            loadStatus = Constants.Loaded;

                                        } else if (StrContainerLoadType.equals(Constants.PartialLoad)) {


                                            StrMessage = "Partial Unloaded";
                                            loadStatus = Constants.PartialLoad;
                                            StrNextStatus = String.valueOf(Constants.LiveLoadingDone);
                                        } else {
                                            StrMessage = "Container Left for Live Unloading";
                                            loadStatus = ContainerLoadTypeID;
                                            StrNextStatus = String.valueOf(Constants.LeftContainerWithChassie);

                                        }

                                        UPDATE_CONTAINER_LOAD_TYPE(URL, StrContainerId, StrNextStatus, loadStatus, StrMessage,
                                                Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                                                Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, StrGrossWeight, StrSealNumber, StrPONumber, ContainerNo);


                                    } else {
                                        Constants.showToastMsg(btnDriverJob
                                                , "Proof of delivery document has been successfully uploaded.", VIEW_DURATION);
                                        Constants.hideSoftKeyboard(getActivity());
                                        if (getFragmentManager() != null)
                                            getFragmentManager().popBackStack();
                                    }
                                }

                            }
                        }else{
                            isUpdateStatusCalled = false;
                           getFragmentManager().popBackStack();
                        }



                    }

                }else{
                    Constants.showToastMsg(btnDriverJob, obj.getString("Message"), VIEW_DURATION);
                }

                isImageUploadBtnClick = false;
            } catch (Exception e) {
                e.printStackTrace();
                isImageUploadBtnClick = false;
                if(getActivity() != null) {
                    Constants.showToastMsg(btnDriverJob,  getString(R.string.error_msg), VIEW_DURATION);
                }
            }
        }

    }


  /*-------------- UPDATE PICKEDUP CONTAINER STATUS API -------------------*/
    private class PickContainerWithFile extends AsyncTask<String, String, String>
    {

        ProgressDialog p;
        String strResponse = "";
        com.squareup.okhttp.Response response;
        File file;
        com.squareup.okhttp.Request request = null;
        com.squareup.okhttp.MultipartBuilder builderNew;

        @Override
        protected void onPreExecute() {
            btnDriverJob.setEnabled(false);
            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT) ))
                URL = API_URL.UPDATE_PICKEDUP_CONTAINER;
            else
                URL = API_URL.UPDATE_PICK_CONTNR_IMPORT;

            p = new ProgressDialog(getActivity());
            p.setMessage("Loading ...");
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                 builderNew = new com.squareup.okhttp.MultipartBuilder().type(com.squareup.okhttp.MultipartBuilder.FORM)
                        .addFormDataPart("ContainerId", StrContainerId)
                        .addFormDataPart("Status", StrNextStatus)
                        .addFormDataPart("Message", StrMessage)
                        .addFormDataPart("DriverId", Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()) )
                        .addFormDataPart("Latitude", Constants.CURRENT_LATITUDE)
                        .addFormDataPart("Longitude", Constants.CURRENT_LONGITUDE)
                        .addFormDataPart("TareWeight", TareWeight)
                        .addFormDataPart("ContainerNo", ContainerNo)
                        .addFormDataPart("ConditionStatus", ConditionStatus)
                        .addFormDataPart("TruckId", TruckId);

                file = new File(imagePath);
                file.getName();
                if (file.exists()) {
                    Log.d("File", "---Add File: " + file.toString());
                 //   Log.d("File", "---Name: " + file.getName());

                    builderNew.addFormDataPart("myFile", file.getName(),
                            com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("image/jpeg"), new File(file.toString())));
                }

                com.squareup.okhttp.RequestBody requestBody = builderNew.build();
                request = new com.squareup.okhttp.Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build();

                com.squareup.okhttp.OkHttpClient client = new com.squareup.okhttp.OkHttpClient();
                client.setConnectTimeout(50, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(50, TimeUnit.SECONDS);
                response = client.newCall(request).execute();

                strResponse = response.body().string();


            } catch (Exception e) {
                e.printStackTrace();
                strResponse = e.getMessage();
            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            if(p != null){
                p.dismiss();
            }

            Log.e("String Response", ">>>>   " + result);
            JSONObject obj;
            String StrMsg =  getString(R.string.error_msg);

            try {
                btnDriverJob.setEnabled(true);
                obj = new JSONObject(result);

                if(obj.has("Message")){
                    StrMsg = obj.getString("Message");
                }
                if(obj.getString("Status").equals("true")){
                    SetContainerView();
                    imagePath = "";
                        editFieldTareView.setVisibility(View.GONE);
                        Constants.showToastMsg(btnDriverJob, "Picked Up successfully", VIEW_DURATION);
                        Constants.hideSoftKeyboard(getActivity());
                        GET_NEXT_LOCATION(StrContainerId, "3", isOnCreate);

                }else{
                    Constants.showToastMsg(btnDriverJob, StrMsg, VIEW_DURATION);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Constants.showToastMsg(btnDriverJob, StrMsg, VIEW_DURATION);

            }


        }

    }

    String HtmlText(String address){
        String title = "", locAdd = "", htmlAddress = "";
        if(!address.equals("") && !address.equalsIgnoreCase("null") && !address.equalsIgnoreCase("N/A"))    {
            String[] addArray = address.split(":");
            title = addArray[0];
            if(addArray.length > 1)
                locAdd = addArray[1] ;

            htmlAddress = "<b>" + title + "</b><br> " + locAdd;
        }else{
            htmlAddress = address;
        }
        return htmlAddress;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Constants.hideSoftKeyboard(getActivity());
    }

    void UpdateRemarkDialog(){

        try{
            if(updateRemarkPicker != null){
                if(updateRemarkPicker.isShowing())
                    updateRemarkPicker.dismiss();
            }
        }catch (Exception e){  e.printStackTrace(); }


        updateRemarkPicker = new Dialog(getActivity());
        updateRemarkPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        updateRemarkPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateRemarkPicker.setContentView(R.layout.popup_loading_fields);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(updateRemarkPicker.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        updateRemarkPicker.getWindow().setAttributes(lp);

        Button btnLoadingJob,  btnCancelLoadingJob;

        TextInputLayout grossWeightInputType, POInputType, chessisNoInputType,remarkInputType;
        final EditText remark;
        final TextView ConfirmTitleTV;


        remark = (EditText)updateRemarkPicker.findViewById(R.id.remarkEditText);
        btnLoadingJob = (Button)updateRemarkPicker.findViewById(R.id.btnLoadingJob);
        btnCancelLoadingJob = (Button)updateRemarkPicker.findViewById(R.id.btnCancelLoadingJob);
        ConfirmTitleTV = (TextView)updateRemarkPicker.findViewById(R.id.ConfirmTitleTV);

        grossWeightInputType = (TextInputLayout)updateRemarkPicker.findViewById(R.id.grossWeightInputType);
        POInputType = (TextInputLayout)updateRemarkPicker.findViewById(R.id.POInputType);
        chessisNoInputType = (TextInputLayout)updateRemarkPicker.findViewById(R.id.sealNoInputType);
        remarkInputType = (TextInputLayout)updateRemarkPicker.findViewById(R.id.remarkInputType);

        grossWeightInputType.setVisibility(View.GONE);
                POInputType.setVisibility(View.GONE);
        chessisNoInputType.setVisibility(View.GONE);
        remarkInputType.setVisibility(View.VISIBLE);


            if(StrGetRemarks!="null") {
            remark.setVisibility(View.VISIBLE);
            ConfirmTitleTV.setVisibility(View.GONE);
            remark.setText(StrGetRemarks);

        }

        btnLoadingJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    StrRemarks = remark.getText().toString().trim();
                    StrIsUpdateRequest = "true";
                Constants.hideKeyboardView(getActivity(), remark);

                GET_UPDATE_REMARK(Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()), StrContainerId, StrRemarks, StrIsUpdateRequest);

                    updateRemarkPicker.dismiss();

            }

        });


        btnCancelLoadingJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.hideKeyboardView(getActivity(), remark);
                updateRemarkPicker.dismiss();
            }
        });

        updateRemarkPicker.show();

    }




    void GetCompanySetting(final String StrCompanyId) {

        try{
            if(getActivity() != null) {

                    StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.Get_Company_Setting,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject obj;

                                JSONArray valueJsonArray;

                                Log.d("Response ", ">>>Response: " + response);
                                progressBar.setVisibility(View.GONE);
                                try {
                                    obj = new JSONObject(response);

                                    if (obj.getInt("Status") == 1) {
                                        if (obj.getBoolean("Success") == true) {

                                            valueJsonArray = new JSONArray(obj.getString("lstResult"));

                                            valueList = new ArrayList<>();
                                            setingNameList = new ArrayList<>();

                                            for (int i = 0; i < valueJsonArray.length(); i++) {
                                                JSONObject objs = (JSONObject) valueJsonArray.get(i);

                                                String Value = objs.getString("Value");
                                                valueList.add(Value);

                                                String setingName = objs.getString("SettingName");
                                                setingNameList.add(setingName);

                                                String OrderType = objs.getString("OrderType");
                                                // setingNameList.add(setingName);


                                                if (setingName.equals("IsImportPickUpImageRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ImportPickUpImage = true;
                                                    } else{
                                                        ImportPickUpImage = false;
                                                    }
                                                }

                                                if (setingName.equals("IsImportDropOffImageRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ImportDropOffImage = true;
                                                    } else{
                                                        ImportDropOffImage = false;
                                                    }
                                                }
                                                if (setingName.equals("IsImportUnloadingImageIsRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ImportUnloadingImage = true;
                                                    } else{
                                                        ImportUnloadingImage = false;
                                                    }
                                                }
                                                if (setingName.equals("IsExportPickUpImageRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ExportPickUpImage = true;
                                                    } else{
                                                        ExportPickUpImage = false;
                                                    }
                                                }
                                                if (setingName.equals("IsExportDropOffImageRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ExportDropOffImage = true;
                                                    } else{
                                                        ExportDropOffImage = false;
                                                    }
                                                }
                                                if (setingName.equals("IsExportLoadingImageIsRequired")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ExportLoadingImage = true;
                                                    } else{
                                                        ExportLoadingImage = false;
                                                    }
                                                }
                                                if (setingName.equals("IsTareWeightRequiredImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        TareWeightRequiredImport = true;
                                                    } else{
                                                        TareWeightRequiredImport = false;
                                                    }
                                                }
                                                if (setingName.equals("IsTareWeightRequiredExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        TareWeightRequiredExport = true;
                                                    } else{
                                                        TareWeightRequiredExport = false;
                                                    }
                                                }

                                                if (setingName.equals("IsSealRequiredImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        SealRequiredImport = true;
                                                    } else{
                                                        SealRequiredImport = false;
                                                    }
                                                }
                                                if (setingName.equals("IsSealRequiredExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        SealRequiredExport = true;
                                                    } else{
                                                        SealRequiredExport = false;
                                                    }
                                                }
                                                if (setingName.equals("IsGrossWeightRequiredImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        GrossWeightRequiredImport = true;
                                                    } else{
                                                        GrossWeightRequiredImport = false;
                                                    }
                                                }
                                                if (setingName.equals("IsGrossWeightRequiredExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        GrossWeightRequiredExport = true;
                                                    } else{
                                                        GrossWeightRequiredExport = false;
                                                    }
                                                }

                                                if (setingName.equals("IsYardSelfAssignAllowedImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        YardSelfAssignAllowedImport = true;
                                                    } else{
                                                        YardSelfAssignAllowedImport = false;
                                                    }
                                                }
                                                if (setingName.equals("IsYardSelfAssignAllowedExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        YardSelfAssignAllowedExport = true;
                                                    } else{
                                                        YardSelfAssignAllowedExport = false;
                                                    }
                                                }

                                                if (setingName.equals("ShowLiveLoadingExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        ShowLiveLoadingExport = true;
                                                    } else{
                                                        ShowLiveLoadingExport = false;
                                                    }
                                                }

                                                if (setingName.equals("ShowLiveLoadingImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")

                                                        ShowLiveLoadingImport = true;
                                                    } else{
                                                        ShowLiveLoadingImport = false;
                                                    }
                                                }


                                                if (setingName.equals("CancelOrderImport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        CancelOrderImport = true;
                                                    } else{
                                                        CancelOrderImport = false;
                                                    }
                                                }
                                                if (setingName.equals("CancelOrderExport")) {
                                                    if (Value.equals("true")) {   // && OrderType.equals("1")
                                                        CancelOrderExport = true;
                                                    } else{
                                                        CancelOrderExport = false;
                                                    }
                                                }

                                            }

                                            if(CONTAINER_JOB_TYPE.equals(String.valueOf(Constants.EXPORT))) {
                                                showLiveLoadUnLoad = ShowLiveLoadingExport;
                                                isLoadingImageRequired = ExportLoadingImage;
                                            }else{
                                                showLiveLoadUnLoad = ShowLiveLoadingImport;
                                                isLoadingImageRequired = ImportUnloadingImage;
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
                        params.put("DriverID", DriverID);
                        params.put("DeviceNumber", DeviceID);
                        params.put("LoginId", LoginId);
                        params.put("Latitude", Constants.CURRENT_LATITUDE);
                        params.put("Longitude", Constants.CURRENT_LONGITUDE);

                        return params;
                    }
                };

                RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
                postRequest.setRetryPolicy(policy);
                getCompanySettingsReq.add(postRequest);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void GetSubLoadingLocations(final String StrContainerId) {

        if(getActivity() != null) {

            try{

                StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_SUB_LOCATIONS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject obj;

                                JSONArray valueJsonArray;

                                Log.d("Response ", ">>>Response: " + response);
                                progressBar.setVisibility(View.GONE);
                                try {
                                    obj = new JSONObject(response);

                                    if (obj.getInt("Status") == 1) {
                                        if (obj.getBoolean("Success") == true) {
                                            legList = new ArrayList<SubLocationModel>();

                                            valueJsonArray = new JSONArray(obj.getString("lstResult"));
                                            for (int i = 0; i < valueJsonArray.length(); i++) {
                                                JSONObject objs = (JSONObject) valueJsonArray.get(i);

                                                SubLocationModel locModel = new SubLocationModel(
                                                        objs.getString("SubLocationID"),
                                                        objs.getString("MasterLoadingLocationId"),
                                                        objs.getString("LocationName"),
                                                        objs.getString("LocationAddress"),
                                                        objs.getBoolean("IsActive")

                                                );

                                                legList.add(locModel);


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
                        params.put("DriverId", DriverID);
                        params.put("DeviceNumber", DeviceID);
                        params.put("ContainerId", StrContainerId);
                        params.put("LoginId", LoginId);
                        params.put("Latitude", Constants.CURRENT_LATITUDE);
                        params.put("Longitude", Constants.CURRENT_LONGITUDE);

                        return params;
                    }
                };

                RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES);   //DEFAULT_BACKOFF_MULT
                postRequest.setRetryPolicy(policy);
                getSubLocReq.add(postRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    /*================== Leg location Listener ====================*/
    private class LegSelectionListener implements LegDialog.LegTypeListener {

        @Override
        public void LegTypeReady(int position) {
            legDialog.dismiss();

            // Perform action
            selectedSubLocId = legList.get(position).getSubLocationID();
            isSubLocCalled = true;
            if(legDialogfromYard){

                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);
                btnDriverJob.setText(StrMessage);
            }else{
                UPDATE_CONTAINER_STATUS(URL, StrContainerId, StrNextStatus, StrMessage,
                        Constants.getDriverId(Constants.KEY_DRIVER_ID, getActivity()),
                        Constants.CURRENT_LATITUDE, Constants.CURRENT_LONGITUDE, ContainerNo);

            }

        }
    }
}
