package com.tracking.smarttrack;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
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
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tracking.constants.APIs;
import com.tracking.constants.BackgroundLogoutService;
import com.tracking.constants.CheckIsUpdateReady;
import com.tracking.constants.CommonUtils;
import com.tracking.constants.Constants;
import com.tracking.constants.LocServices;
import com.tracking.constants.Slidingmenufunctions;
import com.tracking.constants.UrlResponce;
import com.tracking.smarttrack.fragment.ExportDetailFragment;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kumar on 1/18/2017.
 * Worked by Hardeep on 3/18/2019
 */

public class TabActivity extends android.app.TabActivity implements View.OnClickListener {

    public static TabActivity instance;
    public static TabHost host;
    public static RelativeLayout sliderLay;
    public static Button selfAssignBtn, logoutBtn, logoutPopUpBtn, checkUpdateBtn;
    public static String jobType = "", contNo = "";
    public static boolean isActivityCalled = false, isEventMsgShown = false;
    public static boolean isHome = true, isImport = false;
    String UserName = "";
    String CONTAINER_JOB_TYPE = "";
    String LoginId ="";
    String DriverID ="";
    String DeviceID = "";
    String Latitude = "";
    String Longitude = "";
    Intent i;
    long backpressTime;
    private int VIEW_DURATION = 5000;

    public static SlidingMenu smenu;
    Slidingmenufunctions slideMenu;
    FrameLayout tabcontent;
    TextView usernameTV;
    boolean isCapsTheme = false;
    ProgressBar progressBarTab;
    Intent serviceIntent;
    Dialog truckChangeDialog;
    APIs API_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // Fabric.with(this, new Crashlytics());       //Fabric Crashlytics
       // Fresco.initialize(this);

        API_URL = new APIs(this);
        instance      = this;
        isCapsTheme = Constants.isCapsTheme(this);

        if(isCapsTheme){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }


        setContentView(R.layout.activity_tab);

        tabcontent = (FrameLayout)findViewById(android.R.id.tabcontent);
        Constants.CONTAINER_LIST_TYPE = "2";

        /*==================== Left Slide Menu  =====================*/
        setSlidingMenu();
        slideMenu = new Slidingmenufunctions(smenu, TabActivity.this);

        tabDeclaration();
        LocServices locationServices = new LocServices(TabActivity.this);
        locationServices.getLocations();

        try {
            Latitude  = String.valueOf(locationServices.getLatitude());
            Longitude = String.valueOf(locationServices.getLongitude());
        }catch (Exception e){
            e.printStackTrace();
        }

        DeviceID    = LoginActivity.deviceID.trim();
        DriverID    = Constants.getDriverId(Constants.KEY_DRIVER_ID, TabActivity.this);
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,TabActivity.this);


        /*========= Start Service =============*/

        serviceIntent = new Intent(this, BackgroundLogoutService.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }

                startService(serviceIntent);
            }
        }, Constants.SocketRequestTime10);



        if(!Constants.GetUpdateAppDialogTime(this).equals(Constants.GetCurrentDeviceDate()) &&
                !Constants.isFreshLogin(this) ){
            getAppVersion();
        }


    }





    private void setSlidingMenu() {
        smenu = new SlidingMenu(getApplicationContext());
        smenu.setMode(SlidingMenu.LEFT);
        smenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        smenu.setFadeDegree(0.9f);

        smenu.setBehindWidth(CommonUtils.setWidth(TabActivity.this));
        smenu.attachToActivity(TabActivity.this, SlidingMenu.SLIDING_CONTENT);

        smenu.setMenu(R.layout.slide_menu);


    }



    // This method is used to create tabs to show in LIST in side menu
    private void tabDeclaration() {

        /* Friend_list */
        host = getTabHost();

        TabHost.TabSpec export_spec = host.newTabSpec("export");
        export_spec.setIndicator("", getResources().getDrawable(R.drawable.new_logo_sm));
        i = new Intent(TabActivity.this, ExportActivity.class);
        export_spec.setContent(i);


        TabHost.TabSpec import_spec = host.newTabSpec("import");
        import_spec.setIndicator("", getResources().getDrawable(R.drawable.new_logo_sm));
        i = new Intent(TabActivity.this, ImportActivity.class);
        import_spec.setContent(i);


        TabHost.TabSpec selfAssignSpec = host.newTabSpec("2");
        selfAssignSpec.setIndicator("", getResources().getDrawable(R.drawable.new_logo_sm));
        i = new Intent(TabActivity.this, SelfAsignActivity.class);
        selfAssignSpec.setContent(i);


        TabHost.TabSpec profileTab = host.newTabSpec("3");
        profileTab.setIndicator("", getResources().getDrawable(R.drawable.new_logo_sm));
        i = new Intent(TabActivity.this, ProfileActivity.class);
        profileTab.setContent(i);

        TabHost.TabSpec settingTab = host.newTabSpec("4");
        settingTab.setIndicator("", getResources().getDrawable(R.drawable.new_logo_sm));
        i = new Intent(TabActivity.this, AppSettings.class);
        settingTab.setContent(i);



        host.addTab(export_spec);
        host.addTab(import_spec);
        host.addTab(selfAssignSpec);
        host.addTab(profileTab);
        host.addTab(settingTab);

        host.getTabWidget().setVisibility(View.GONE);

        host.getTabWidget().getChildAt(0).getLayoutParams().width = 0;
        host.getTabWidget().getChildAt(1).getLayoutParams().width = 0;
        host.getTabWidget().getChildAt(2).getLayoutParams().width = 0;
         host.getTabWidget().getChildAt(3).getLayoutParams().width = 0 ;
        host.getTabWidget().getChildAt(4).getLayoutParams().width = 0 ;

        sliderLay      = (RelativeLayout) findViewById(R.id.sliderLay);
        selfAssignBtn  = (Button)findViewById(R.id.selfAssignBtn);
        logoutBtn      = (Button)findViewById(R.id.logoutBtn);
        logoutPopUpBtn = (Button)findViewById(R.id.logoutPopUpBtn);
        checkUpdateBtn = (Button)findViewById(R.id.checkUpdateBtn);
        usernameTV     = (TextView) findViewById(R.id.usernameTV);
        progressBarTab = (ProgressBar)findViewById(R.id.progressBarTab);

        if(Constants.isJustSelected(this)){
            host.setCurrentTab(4);
        }else{
          //  host.setCurrentTab(1);
            host.setCurrentTab(0);
        }

        Constants.setCapsTheme(isCapsTheme, false, TabActivity.this );


        if (!Constants.getDriverDetails(Constants.KEY_FNAME, TabActivity.this).equalsIgnoreCase("null")) {
            UserName = Constants.getDriverDetails(Constants.KEY_FNAME, TabActivity.this);
        }

        if (!Constants.getDriverDetails(Constants.KEY_LNAME, TabActivity.this).equalsIgnoreCase("null")) {
            UserName = UserName + " " + Constants.getDriverDetails(Constants.KEY_LNAME, TabActivity.this);
        }



        usernameTV.setText(UserName);
        smenu.addIgnoredView(tabcontent);

        sliderLay.setOnClickListener(this);
        selfAssignBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        logoutPopUpBtn.setOnClickListener(this);
        checkUpdateBtn.setOnClickListener(this);

    }




    void logoutDialog() {
        final Dialog picker = new Dialog(TabActivity.this);
        picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        picker.setContentView(R.layout.popup_logout);

        RelativeLayout cancelBtn, confirmBtn;
        cancelBtn = (RelativeLayout) picker.findViewById(R.id.cancelPopupBtn);
        confirmBtn = (RelativeLayout) picker.findViewById(R.id.confirmPopupBtn);
        Button confirmPopupButton = (Button) picker.findViewById(R.id.confirmPopupButton);
        Button cancelPopupButton = (Button) picker.findViewById(R.id.cancelPopupButton);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picker.dismiss();
            }
        });

        cancelPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picker.dismiss();
            }
        });

        confirmPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutPopUpBtn.performClick();

                picker.dismiss();

            }
        });


        picker.show();

    }


    void selfAssignDialog() {

        final Dialog mediaPicker = new Dialog(TabActivity.this);
        mediaPicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = mediaPicker.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mediaPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPicker.setContentView(R.layout.popup_loading_status);

        final Button exportBtn, importBtn, cancelBtn, liveLoadingBtn;
        final TextView selectlbl, jobsView;

        importBtn = (Button) mediaPicker.findViewById(R.id.leftWithChassisBtn);
        exportBtn = (Button) mediaPicker.findViewById(R.id.leftWithOutChassisBtn);
        selectlbl = (TextView) mediaPicker.findViewById(R.id.jobtitleview);
        jobsView = (TextView) mediaPicker.findViewById(R.id.jobView);

        cancelBtn = (Button) mediaPicker.findViewById(R.id.closeSBtn);

        exportBtn.setText("Export");
        importBtn.setText("Import");
        selectlbl.setVisibility(View.VISIBLE);
        jobsView.setVisibility(View.VISIBLE);

        liveLoadingBtn = (Button) mediaPicker.findViewById(R.id.liveLoadingBtn);
        liveLoadingBtn.setVisibility(View.GONE);


        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPicker.dismiss();
                CONTAINER_JOB_TYPE = "2";
                selfAsignDialog();
            }
        });


        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPicker.dismiss();
                CONTAINER_JOB_TYPE = "1";
                selfAsignDialog();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPicker.dismiss();
            }
        });

        mediaPicker.show();

    }

    void selfAsignDialog() {
        final Dialog mediaPicker = new Dialog(TabActivity.this);
        mediaPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mediaPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPicker.setContentView(R.layout.popup_search_container);
        //picker.setTitle("Select Date and Time");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mediaPicker.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        mediaPicker.getWindow().setAttributes(lp);


        //  StrPONumber = "";
        Button btnAsignJob, btnCancelAsignJob;
        final EditText ContrEditText;

        TextInputLayout ContrInputType = (TextInputLayout) mediaPicker.findViewById(R.id.ContrInputType);

        btnAsignJob = (Button) mediaPicker.findViewById(R.id.btnAsignJob);
        btnCancelAsignJob = (Button) mediaPicker.findViewById(R.id.btnCancelAsignJob);
        ContrEditText = (EditText) mediaPicker.findViewById(R.id.ContrEditText);

        btnAsignJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                contNo = ContrEditText.getText().toString();
                contNo = contNo.toLowerCase().trim();
                jobType = "1";
                isActivityCalled = true;
                Constants.hideKeyboardView(TabActivity.this, ContrEditText);

                mediaPicker.dismiss();
                host.setCurrentTab(0);
                host.setCurrentTab(2);

            }
        });

        btnCancelAsignJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.hideKeyboardView(TabActivity.this, ContrEditText);
                mediaPicker.dismiss();
            }
        });

        mediaPicker.show();

    }


    @Override
    public void onBackPressed() {
        Log.d("Export ACtivty", "onBackPressed");
        backPressed();

    }


    public void CLOSE_SLIDER() {
        if (smenu.isMenuShowing()) {
            smenu.toggle();
        } else {
            if (backpressTime + 2000 > System.currentTimeMillis()) {
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Press back again to exit.",
                        Toast.LENGTH_LONG).show();
            }
            backpressTime = System.currentTimeMillis();
        }
    }


    private void backPressed() {

        try {
            if (!TabActivity.isHome) {
                if (ExportDetailFragment.searchActionBar != null) {
                    ExportDetailFragment.searchActionBar.performClick();
                } else {
                    if (smenu.isMenuShowing()) {
                        smenu.toggle();
                    } else {
                        if (ExportActivity.fragManager != null) {
                            if (ExportActivity.fragManager.getBackStackEntryCount() > 2) {
                                ExportActivity.fragManager.popBackStack();
                            } else {
                                if (SelfAsignActivity.selfFragManager != null) {
                                    if (SelfAsignActivity.selfFragManager.getBackStackEntryCount() > 1) {
                                        SelfAsignActivity.selfFragManager.popBackStack();
                                    } else {
                                        host.setCurrentTab(0);
                                    }
                                } else {
                                    CLOSE_SLIDER();
                                }
                            }

                        } else {
                            host.setCurrentTab(0);
                        }
                    }
                }
            } else {
                CLOSE_SLIDER();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }


    void LogoutRequest(final String loginId,final String Latitude,final String Longitude,final String deviceId,final String driverId){


        RequestQueue queue = Volley.newRequestQueue(TabActivity.this);
       // progressBarlogin.setVisibility(View.VISIBLE);

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
                                    String logOutResult = obj.getString("Result");

                                    Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_LONG).show();
                                   // Constants.showToastMsg(selfAssignBtn, ""+ logOutResult.toString(), VIEW_DURATION);

                                    Constants.setDriverId(Constants.KEY_DRIVER_ID, "", TabActivity.this);
                                    Constants.setCompanyId(Constants.KEY_COMPANY, "",TabActivity.this);

                                    Intent i = new Intent(TabActivity.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                }

                            }else{
                                Constants.showToastMsg(selfAssignBtn, "Failed", VIEW_DURATION);

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
                        //progressBarlogin.setVisibility(View.GONE);

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("LoginID", loginId);
                params.put("DriverId", driverId);
                params.put("Latitude", Latitude);
                params.put("Longitude", Longitude);
                params.put("DeviceId", deviceId);
                //params.put("Type", Type);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sliderLay:
                smenu.showMenu();
                smenu.addIgnoredView(tabcontent);
                break;

            case R.id.selfAssignBtn:
                selfAsignDialog();
                break;

            case R.id.checkUpdateBtn:
                if(!Constants.GetUpdateAppDialogTime(this).equals(Constants.GetCurrentDeviceDate()) &&
                        !Constants.isFreshLogin(this) ){
                    getAppVersion();
                }
                break;

            case R.id.logoutBtn:
                logoutDialog();
                break;

            case R.id.logoutPopUpBtn:
                LogoutRequest(LoginId, Latitude, Longitude,DeviceID, DriverID);
                break;


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ExportFragment.IS_SELF_ASIGN = false;
        ExportActivity.IS_EXPORT_VIEW = false;

    }



    private boolean isUpdateAvailable(String responseVersion) {

        boolean isUpdateAvailable = false;
        String currentVersion = Constants.GetAppVersion(getApplicationContext(), "VersionName");

        if (!currentVersion.equals(responseVersion)) {
            String[] updatedVersionArray = responseVersion.split("\\.");
            String[] installedVersionArray = currentVersion.split("\\.");

            for (int i = 0; i < updatedVersionArray.length; i++) {
                int storedVersionName = Integer.valueOf(updatedVersionArray[i]);
                int installedVersionName = -1;
                if (i < installedVersionArray.length) {
                    installedVersionName = Integer.valueOf(installedVersionArray[i]);
                }

                if (storedVersionName > installedVersionName) {
                    isUpdateAvailable = true;
                    break;
                }else{
                    if (installedVersionName > storedVersionName) {
                        break;
                    }
                }
            }

        }

        return isUpdateAvailable;

    }



    private void getAppVersion(){
        try {
            new CheckIsUpdateReady("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en", new UrlResponce() {
                @Override
                public void onReceived(String responseVersion) {
                    Log.d("responseStr", "responseStr: " + responseVersion);

                    if (isUpdateAvailable(responseVersion)) {
                      //  String playStorePackage = "com.android.vending";
                        //boolean isPlayStoreDownload = Constants.appInstalledOrNot(playStorePackage, TabActivity.this);

                        updateAppDialog(responseVersion);

                    }

                }
            }).execute();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    void updateAppDialog(String versionName){

        try {
            if(truckChangeDialog != null && truckChangeDialog.isShowing()){
                truckChangeDialog.dismiss();
            }

            truckChangeDialog = new Dialog(TabActivity.this);
            truckChangeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            truckChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            truckChangeDialog.setContentView(R.layout.popup_app_update);
          //  truckChangeDialog.setCancelable(false);

            Button appUpdateBtn = (Button) truckChangeDialog.findViewById(R.id.appUpdateBtn);
            TextView updateDescTV = (TextView) truckChangeDialog.findViewById(R.id.updateDescTV);

            String desc = "A new version ("+ versionName +") of ALS Container app is available.";
            updateDescTV.setText(desc);
            Constants.SetUpdateAppDialogTime(Constants.GetCurrentDeviceDate(), TabActivity.this);


            appUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    truckChangeDialog.dismiss();
                }
            });

            truckChangeDialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}