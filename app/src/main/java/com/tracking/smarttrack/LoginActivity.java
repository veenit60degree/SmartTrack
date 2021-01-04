package com.tracking.smarttrack;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.models.VehicleModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.constants.LocServices;
import com.tracking.smarttrack.fragment.NewFeedFragment;
import com.tracking.smarttrack.fragment.OTPFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kumar on 1/20/2017.
 */

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    protected static final String TAG = "LoginActivity";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final String TAG1 = LoginActivity.class.getSimpleName();
    public static String token;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 0x1;

    public static LoginActivity instance;
    LinearLayout loginParentLayout;
    RelativeLayout loginFieldView;
    EditText emailEditText, passwrdEditText;
    TextView appVersionTV, appTypeTxtVw;
    Button btnLogin, localUrlBtn, productionUrlBtn;
    ProgressBar progressBarlogin;
    private String email = "", password = "", Latitude = "", Longitude = "";
    String AppVersion = "", DRIVER_ID = "",Emp_ID = "";
    private int VIEW_DURATION = 5000;
    Animation fadeOutAnim;
    public static FrameLayout otp_fragment;
    ImageView logoImg;
    Handler handler, imgHandler;
    boolean isLoginBtnClicked = false;
    public static String deviceID = "";
    static String appVersion = "";
    String DriverId = "", CompanyId = "", LoginID = "";
    JSONObject resultJson;
    APIs API_URL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }

        setContentView(R.layout.activity_loginnn);

        instance = this;
        initView();
        requestAppPermissions();
    }

    void initView(){
        API_URL = new APIs(this);
        progressBarlogin = (ProgressBar)findViewById(R.id.progressBarlogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        localUrlBtn = (Button)findViewById(R.id.localUrlBtn);
        productionUrlBtn = (Button)findViewById(R.id.productionUrlBtn);


        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwrdEditText = (EditText)findViewById(R.id.passwrdEditText);
        appVersionTV = (TextView)findViewById(R.id.appVersionTV);
        appTypeTxtVw = (TextView)findViewById(R.id.appTypeTxtVw);

        loginParentLayout = (LinearLayout) findViewById(R.id.loginParentLayout);
        loginFieldView = (RelativeLayout) findViewById(R.id.loginFieldView);
        logoImg = (ImageView)findViewById(R.id.logoImg);
        otp_fragment = (FrameLayout)findViewById(R.id.otp_fragment);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ArethosIndia", "Arethos India", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("arethos")

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {

                        }

                    }
                });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG1, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        deviceID = token;
                    }
                });


        requestLocationPermission();


        emailEditText.addTextChangedListener(new CustomTextWatcher(emailEditText));
        passwrdEditText.addTextChangedListener(new CustomTextWatcher(passwrdEditText));

        appVersion = BuildConfig.VERSION_NAME;

        DRIVER_ID = Constants.getDriverId(Constants.KEY_DRIVER_ID, LoginActivity.this);
        Emp_ID = Constants.getCompanyId(Constants.KEY_COMPANY, LoginActivity.this);

        fadeOutAnim = AnimationUtils.loadAnimation( this, R.anim.login_fade_out);
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1100);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1100);
        fadeOut.setDuration(1100);

        handler = new Handler();
        imgHandler = new Handler();

        AppVersion = Constants.GetAppVersion(this, "VersionName");
        appVersionTV.setText("Version: " + AppVersion);

      //  localUrlBtn.setVisibility(View.GONE);
     //   productionUrlBtn.setVisibility(View.GONE);

        localUrlBtn.setOnClickListener(this);
        productionUrlBtn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        loginParentLayout.setOnClickListener(this);


        passwrdEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i(TAG,"Enter pressed");
                    moveToHomeScreen();
                }
                return false;
            }
        });


        getUserLoginStatus();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Constants.IsApiUrlLocal(LoginActivity.this)) {
            appTypeTxtVw.setVisibility(View.VISIBLE);
        }else{
            appTypeTxtVw.setVisibility(View.GONE);

        }
    }



    private void getUserLoginStatus(){

        if(DRIVER_ID.trim().length() > 0 && Emp_ID.trim().length() > 0 ) {
            Intent i = new Intent(LoginActivity.this, TabActivity.class);
            startActivity(i);
            finish();
        }else{
            getGPSLocation();
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean requestLocationPermission(){



        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getGPSLocation();

                return true;
            } else {
                if(DRIVER_ID.trim().length() > 0 && Emp_ID.trim().length() > 0 ) {
                    Intent i = new Intent(LoginActivity.this, TabActivity.class);
                    startActivity(i);
                    finish();
                }
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return false;
            }
        }else {
            Log.v("TAG","Permission is granted");
           // statusCheck();
            return true;
        }

    }


    void getGPSLocation() {

        LocServices locationServices = new LocServices(LoginActivity.this);
        locationServices.getLocations();

       // if (requestLocationPermission()) {

            try {
                Latitude = String.valueOf(locationServices.getLatitude());
                Longitude = String.valueOf(locationServices.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //   }

            if (locationServices.getLatitude() == 0.0) {
                locationServices = new LocServices(LoginActivity.this);
                locationServices.getLocations();
                // if(locationServices.getIsGPSTrackingEnabled()){
               // if (requestLocationPermission()) {
                    try {
                        Latitude = String.valueOf(locationServices.getLatitude());
                        Longitude = String.valueOf(locationServices.getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //  }
                }
                Log.d("Location", "Location: " + Latitude + " " + Latitude);


            }


    void moveToHomeScreen(){
        email =  emailEditText.getText().toString();
        password = passwrdEditText.getText().toString();

        if(!validateEmail()){
            return;
        }

        if(!validatePassword()){
            return;
        }

        LoginRequest( email, password, appVersion, deviceID);
    }




    void LoginRequest(final String email,final String password,final String AppVersion,final String DeviceID){


        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        progressBarlogin.setVisibility(View.VISIBLE);

        Log.d("API", "API: " + API_URL.LOGIN);

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.LOGIN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressBarlogin.setVisibility(View.GONE);
                        JSONObject obj;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            Constants.setTruckArray("[]", LoginActivity.this);
                            Constants.setTruckDetails("", "", LoginActivity.this);
                            Constants.SetUpdateAppDialogTime("", LoginActivity.this);

                            obj = new JSONObject(response);

                            if(obj.getInt("Status") == 0){

                                Constants.showToastMsg(btnLogin, "Please check your username and password", VIEW_DURATION);
                                loginFieldView.setVisibility(View.VISIBLE);
                                passwrdEditText.setText("");

                            }else if(obj.getInt("Status") == 1){
                                if(obj.getBoolean("Success") == true){
                                    resultJson = new JSONObject(obj.getString("Result"));
                                    boolean IsAuthenticationRequired = resultJson.getBoolean("IsAuthenticationRequired");

                                    if(IsAuthenticationRequired) {

                                        Constants.setDriverId( Constants.KEY_DRIVER_ID , resultJson.getString("DriverId") , getApplicationContext());
                                        Constants.setLogoutEventDate("", "", getApplicationContext());
                                        Constants.setValidateUser(Constants.KEY_EMAIL, resultJson.getString("EmailAddress"),
                                                getApplicationContext()  );


                                        Constants.showToastMsg(btnLogin, "Enter OTP for Authentication.", VIEW_DURATION);

                                        otp_fragment.setVisibility(View.VISIBLE);
                                        OTPFragment otpFragment = new OTPFragment();
                                        FragmentManager fragManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTran = fragManager.beginTransaction();
                                        fragmentTran.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                                android.R.anim.fade_in, android.R.anim.fade_out);
                                        fragmentTran.replace(R.id.otp_fragment, otpFragment);

                                        fragmentTran.addToBackStack(null);
                                        fragmentTran.commit();


                                    }else {
                                       // moveToHomeScreen(resultJson);

                                        try{

                                            DriverId    = resultJson.getString("DriverId");
                                            CompanyId   = resultJson.getString("CompanyId");
                                            LoginID     = resultJson.getString("LoginID");

                                            String truckList   = obj.getString("lstResult");
                                            Constants.setTruckArray(truckList, LoginActivity.this);

                                            launchHomeScreen(resultJson);


                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }



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
                        progressBarlogin.setVisibility(View.GONE);
                        Constants.showToastMsg(btnLogin, "Please check your internet connection", VIEW_DURATION);
                        // response
                        Log.d("error", ">>error: " +error);
                        loginFieldView.setVisibility(View.VISIBLE);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("UserName", email);
                params.put("Password", password);
                params.put("Latitude", Latitude);
                params.put("Longitude", Longitude);
                params.put("AppVersion", AppVersion);
                params.put("DeviceNumber", DeviceID);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime50, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }




    void launchHomeScreen(JSONObject resultJson){

        try{

            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
            Constants.saveDriverLoginDetails(resultJson, getApplicationContext());
            Constants.setLoginStatus(true, getApplicationContext());

            Intent i = new Intent(getApplicationContext(), TabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.localUrlBtn:
                Constants.setApiUrlLocal(true, LoginActivity.this);
                appTypeTxtVw.setVisibility(View.VISIBLE);
                appTypeTxtVw.setText(getString(R.string.local));
               // API_URL.API_BASE_URL = API_URL.LOCAL_SERVER;
                API_URL = new APIs(LoginActivity.this);
                break;


            case R.id.productionUrlBtn:
                Constants.setApiUrlLocal(false, LoginActivity.this);
                appTypeTxtVw.setVisibility(View.VISIBLE);
                appTypeTxtVw.setText(getString(R.string.production));
               // API_URL.API_BASE_URL = API_URL.PRODUCTION_SERVER;
                API_URL = new APIs(LoginActivity.this);
                break;

            case R.id.btnLogin:
                Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(getApplicationContext());
                Constants.hideSoftKeyboard(LoginActivity.this);
                if(Constants.INTERNET_CONNECTIVITY_STATUS){
                    getGPSLocation();
                    if(emailEditText.getText().toString().equals("") || passwrdEditText.getText().toString().equals("")){
                        Constants.showToastMsg(btnLogin, "Enter User Name and Password", VIEW_DURATION);
                    }else {
                        moveToHomeScreen();
                    }
                }else{
                    Constants.showToastMsg(btnLogin, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                }
                isLoginBtnClicked = true;

                break;

            case R.id.loginParentLayout:
                Constants.hideSoftKeyboard(LoginActivity.this);
                break;

        }
    }


    private class CustomTextWatcher implements TextWatcher {

        private View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.emailEditText:
                    validateEmail();
                    break;
                case R.id.passwrdEditText:
                    validatePassword();
                    break;
            }

        }
    }

    private boolean validatePassword() {
        if (passwrdEditText.getText().toString().trim().isEmpty()) {
            requestFocus(passwrdEditText);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty() ) {
            requestFocus(emailEditText);
            return false;
        }

        return true;
    }





}
