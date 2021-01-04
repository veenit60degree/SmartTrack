package com.tracking.constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.TabActivity;
import com.tracking.smarttrack.fragment.ExportFragment;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class BackgroundLogoutService extends Service {


    private static final long TIME_UPDATES_INTERVAL = 60 * 1000;   // 60 sec
    String TAG = "Service", DRIVER_ID = "",  Emp_ID = "", LoginId = "", DeviceID = "";
    boolean isStopService = false;
    APIs API_URL;


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        super.onCreate();

        API_URL                 = new APIs(getApplicationContext());
        mTimer                  = new Timer();
        mTimer.schedule(timerTask, TIME_UPDATES_INTERVAL, TIME_UPDATES_INTERVAL);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("service", "---------onStartCommand Service");

        CheckEventTime();


        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }




    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.e(TAG, "-----Running timerTask");

            CheckEventTime();

        }
    };


    void CheckEventTime(){
        DRIVER_ID = Constants.getDriverId(Constants.KEY_DRIVER_ID, getApplicationContext());
        Emp_ID = Constants.getCompanyId(Constants.KEY_COMPANY, getApplicationContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN, getApplicationContext());
        DeviceID    = LoginActivity.deviceID.trim();

        if (DRIVER_ID.trim().length() > 0 && Emp_ID.trim().length() > 0) {

            String logoutTimeStr = Constants.getLogoutDate(getApplicationContext());
            String eventTimeStr = Constants.getEventDate(getApplicationContext());

            try {

                if (eventTimeStr.length() > 10 && Constants.getTruckNumber(getApplicationContext()).length() > 0) {

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
                       /* if (!TabActivity.isEventMsgShown) {
                            TabActivity.isEventMsgShown = true;
                            if (leftMin > 0 || leftHour > 0) {
                                String desc = "You have " + time + " left to complete the event";
                                Constants.ShowLocalNotification(getApplicationContext(), TabActivity.instance, "EVENT TIME !", desc, 1001);
                            }
                            // Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
                        }else{*/
                            if(leftHour == 0 ){
                                showNotificationWithInterval(leftMin);
                            }
                       // }
                    }

                    if (Constants.isNetworkAvailable(getApplicationContext())) {

                        if (currentDate.isAfter(logoutDate) &&
                                (ExportFragment.containerExportList.size() == 0 && ExportFragment.newJobsCount == 0)) {
                            LogoutRequest(LoginId, DRIVER_ID, DeviceID);
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            Log.e("Log", "--stop");
            StopService();
        }

    }


    void showNotificationWithInterval(int interval){
        switch (interval){
            case 1:
            case 3:
            case 5:
            case 10:
            case 20:
            case 40:
            case 60:

                String desc = "You have " + interval + " min left to complete the event";
                Constants.ShowLocalNotification(getApplicationContext(), TabActivity.instance, "EVENT TIME !", desc, 1001);

            break;



        }
    }


    void StopService(){
        isStopService = true;
        try {
            mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
        }
        stopForeground(true);
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"---onBind");
        return null;
    }


    public void onDestroy() {

        if(!isStopService) {

            Intent intent = new Intent("com.tracking.smarttrack");
            intent.putExtra("location", "torestore");
            sendBroadcast(intent);
        }else{
            Log.d(TAG, "Service stopped");

        }

    }



    void LogoutRequest(final String loginId, final String driverId, final String deviceId){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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

                                    Constants.setDriverId(Constants.KEY_DRIVER_ID, "", getApplicationContext());
                                    Constants.setCompanyId(Constants.KEY_COMPANY, "",getApplicationContext());
                                    String desc = "You are done for the day";
                                    Constants.ShowLocalNotification(getApplicationContext(), LoginActivity.instance, "EVENT COMPLETED !", desc, 1001 );

                                  //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();

                                    StopService();

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
                        error.printStackTrace();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();

                params.put("LoginID", loginId);
                params.put("DriverId", driverId);
                params.put("DeviceId", deviceId);
                params.put("Latitude", "");
                params.put("Longitude", "");

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }



}