package com.tracking.constants;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.models.CompanySetingModel;
import com.models.ContainerModel;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Constants {

    public Constants(){
        super();
    }

    static String PROJECT_NAME = "ALS Containers";
    public static String IMAGE_BASE_URL = "http://112.196.24.212:55549";

    //for office system
  //   public static String LOCAL_SERVER = "http://192.168.0.2:8266/api/integrate/";

    // for tab or phone
/*
   public static String LOCAL_SERVER = "http://182.73.78.171:8266/api/integrate/";
   public static String PRODUCTION_SERVER = "http://api.alscontainers.com/api/integrate/"; //USA server


    public static String API_BASE_URL = LOCAL_SERVER;


    public static String LOGIN                          = API_BASE_URL + "ValidateUser";
    public static String Validate_OTP                   = API_BASE_URL + "ValidateOTP";
    public static String GET_CONTAINER_LIST             = API_BASE_URL + "GetContainerList";
    public static String GET_CONTAINER_DETAIL           = API_BASE_URL + "GetContainerDetail";
    public static String UPDATE_READ_STATUS             = API_BASE_URL + "UpdateReadStausContainer";
    public static String UPDATE_PICKEDUP_CONTAINER      = API_BASE_URL + "UpdatePickedContainer";
    public static String UPDATE_CONTAINER_STATUS        = API_BASE_URL + "UpdateContainerStatus";
    public static String GET_NEXT_LOCATION              = API_BASE_URL + "GetNextLocation";
    public static String UPDATE_CONTAINER_LOADTYPE      = API_BASE_URL + "UpdateContainerLoadType";
    public static String UPLOAD_DELIVERY_DOCUMENT       = API_BASE_URL + "UploadDeliveryDocument";
    public static String UPDATE_CONTNR_STATUS_IMPORT    = API_BASE_URL + "UpdateContainerStatusForImport";
    public static String UPDATE_CONTNR_LOADTYPE_IMPORT  = API_BASE_URL + "UpdateContainerLoadTypeForImport";
    public static String UPDATE_PICK_CONTNR_IMPORT      = API_BASE_URL + "UpdatePickedContainerForImport";
    public static String UPLOAD_DELIVERY_DOC_IMPORT     = API_BASE_URL + "UploadDeliveryDocumentForImport";
    public static String UPDATE_CHASSIS_NO              = API_BASE_URL + "UpdateChassisNo";
    public static String UPDATE_CONTAINER_NO            = API_BASE_URL + "UpdateContainerNo";
    public static String GET_UNASSIGN_CONTAINER         = API_BASE_URL + "GetContainersByContainerNo";
    public static String GET_DRIVER_TRUCK_CHASSI        = API_BASE_URL + "GetDriverTruckChassiYardByCompany";
    public static String UPDATE_SELFASSIGN              = API_BASE_URL + "SelfAssign";
    public static String UPDATE_REMARKS                 = API_BASE_URL + "GetAndUpdateDriverRemarks";
    public static String LOGOUT                         = API_BASE_URL + "Logout";
    public static String JOB_Acceptance                 = API_BASE_URL + "AcceptJob";
    public static String Get_Company_Setting            = API_BASE_URL + "GetCompanySetting";
    public static String GET_ASSIGNED_JOBS              = API_BASE_URL + "GetAssignedJobs";
    public static String GET_SUB_LOCATIONS              = API_BASE_URL + "GetSubLocations";
    public static String GET_DRIVER_DETAIL              = API_BASE_URL + "GetDriverDetail";
    public static String UPLOAD_DRIVER_IMAGE            = API_BASE_URL + "UploadDriverImage";
    public static String SET_DEFAULT_TRUCK              = API_BASE_URL + "SetDefaulttruck";
    public static String SET_DRIVER_READY_STATUS        = API_BASE_URL + "SetDriverReadyStatus";

    public static String UPDATE_BACK_TO_YARD_EVENT      = API_BASE_URL + "UpdteBackToYardEvent";
    public static String GET_BACK_TO_YARD_EVENT         = API_BASE_URL + "GetBackToYardEvent";
    public static String SET_DRIVER_LOGOUT_TIME         = API_BASE_URL + "SetDriverLogoutTime";
*/


    public static int ACTIVITY_STATUS = 0;
    public static Bundle BUNDLE = new Bundle();
    public static Bundle GET_BUNDLE;
    public static boolean IS_LOG_SHOWN = true;
    public static int PICK_FROM_CAMERA = 2;

    String url = "";
    public static SharedPreferences preferences, prefs;

    public static String KEY_DRIVER_ID              = "driver_id";
    public static String KEY_GENDER                 = "gender";
    public static String KEY_STATE                  = "state";
    public static String KEY_CARRIERNAME            = "carrier_name";
    public static String KEY_COMPANYNAME            = "company_name";
    public static String KEY_DOB                    = "dob";
    public static String KEY_DRIVER_IMAGE           = "driver_image";
    public static String KEY_EMAIL                  = "email";
    public static String KEY_EMP_ID                 = "emp_id";
    public static String KEY_FAST_ID                = "fast_id";
    public static String KEY_FNAME                  = "fname";
    public static String KEY_LNAME                  = "lname";
    public static String KEY_MNAME                  = "mname";
    public static String KEY_MOBILE                 = "mobile";
    public static String KEY_COMPANY                = "CompanyId";
    public static String KEY_LOGIN                  = "LoginID";
    public static String EVENT_ITEM_TYPE            = "EventItemType";

    public static String CONTAINER_LIST_TYPE        = "2";// 1 For Import   &  2 For Export

    /* ---------- Container Load Type ---------- */
    public static String Empty                          = "1";
    public static String PartialLoad                    = "2";
    public static String Loaded                         = "3";

    /* ---------- Location Type Status ---------- */
    public static int PICKED_UP                         = 1;
    public static int LOADING                           = 2;
    public static int DROPOFF                           = 3;
    public static int YARD                              = 4;
    public static int NO_LOCATION                       = 5;
    public static int UNLOADING                         = 6;


    public static int IMPORT                            = 1;
    public static int EXPORT                            = 2;
    public static int ASSIGNED                          = 3;

    public static String IMPORT_                        = "1";
    public static String EXPORT_                        = "2";


        /*--------------- DocumentType ------------- */
        public static int PickupDocument = 1;
        public static int PODDocument = 2;
        public static int UnloadingDocument = 3;
        public static int ContainerCondition = 4;
        public static int LoadingDocument = 5;

    /* ---------- Container Locations Type ---------- */
    public static int New                               = 1;
    public static int Assign                            = 2;
    public static int PickUp                            = 3;
    public static int ArrivedAtLoadingLocation          = 4;
    public static int LiveLoading                       = 5;
    public static int LeftContainerWithChassie          = 6;
    public static int LeftContainerWithoutChassie       = 7;
 // public static int LoadingWithoutDriverTruckChessie  = 8;
    public static int ArrivedAtPickUpLoc                = 9;
    public static int ArrivedAtYardlocation             = 10;
    public static int ArrivedAtDropOffLocation          = 11;
    public static int LeftContainerAtYardWithChassis    = 12;
    public static int LeftContainerAtYardWithoutChasis  = 13;
    public static int ArrivedAtFirstGate                = 14;
    public static int POD                               = 15;
    public static int Delivered                         = 16;
    public static int LiveLoadingDone                   = 17;
    public static int AssignForLoadingLocation          = 18;
    public static int AssignForYardLocation             = 19;
    public static int PickFromYardLocation              = 20;
    public static int DeadCall                          = 21;
    public static int OrderCancel                       = 22;
    public static int LineHold                          = 23;
    public static int CustomHold                        = 24;
    public static int ReadyForPickup                    = 25;

    // -------------- Socket Request timeOut constants --------------
    public static int SocketRequestTime10               = 10000;   //  10 seconds ;
    public static int SocketRequestTime20               = 20000;   //  20 seconds ;
    public static int SocketRequestTime30               = 30000;   //  30 seconds ;
    public static int SocketRequestTime40               = 40000;   //  40 seconds ;
    public static int SocketRequestTime50               = 50000;   //  50 seconds ;

    public static String[] CONTAINER_STATUS_TYPES = {
            "Assign",                               //0
            "Arrived",                              //1
            "Pick Up Container",                    //2
            "Update Status",                        //3
            "Live Loading",                         //4
            "Loading Without Driver",               //5
            "Left Container Without Chassis",       //6
            "Left Container With Chassis",          //7
            "Partial Loaded",                       //8
            "Loaded",                               //9
            "Live loading is started",              //10
            "Loading",                              //11
            "Arrived At Picked Location",           //12
            "Arrived At Yard Location",             //13
            "Yard Location",                        //14
            "Upload Interchange In",                //15
            "Deliver Container",                    //16
            "Complete Your Loading",                //17
            "Get Next Location",                    //18
            "Arrived At Loading Location",          //19
            "Update Loading Status",                   //20
            "Arrived At Next Loading Location",     //21
            "Arrived At DropOff Location",          //22
            "Next",                                 //23
            "Change Status",                        //24
            "Live Loading is started",                 //25
            "Arrived At Loading Location",          //26
            "Arrived At Yard Location",             //27
            "Go to DropOff Location",               //28
            "Pickup Container",                    //29
            "Live Unloading",                       //30
            "Unloading Without Driver",             //31
            "Live unloading is started",            //32
            "Unloading",                            //33
            "Complete Your Unloading",              //34
            "Arrived At Unloading Location",        //35
           // "Set Unloading Status",                 //36
            "Update Unloading Status",                 //36
            "Arrived At Next unloading Location",   //37
            "Arrived At First Gate",                //38
            "Pick From Yard Location",                //39
            "Arrived At Pickup Location",           //40
    };

    public static String CURRENT_LATITUDE = "0.0", CURRENT_LONGITUDE = "0.0";
    public static int VIEW_DURATION = 5000;
    public static String DateFormat 				= "yyyy'-'MM'-'dd'T'HH':'mm':'ss";
    public static String DateFormatMMddyyyy			= "MM-dd-yyyy";

    public static boolean INTERNET_CONNECTIVITY_STATUS ;
    public static String INTERNET_MESSAGE = "Not connected to Internet" ;
    public static GPSTracker gps;
    public static LocServices locServices;

    static int[] NETWORK_TYPES = {
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_BLUETOOTH,
            ConnectivityManager.TYPE_ETHERNET };




        public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }


    // ------------------- Set User Name -------------------

    public static void saveUserName(String Driverkey, String value, Context context) {


        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Driverkey, value);
        editor.commit();


    }

    // ------------------- Set User Name -------------------

    public static void saveUserPassword(String Driverkey, String value, Context context) {


        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Driverkey, value);
        editor.commit();


    }

    // ------------------- Set User Id -------------------
    public static void setDriverId(String Driverkey, String value, Context context) {


        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Driverkey, value);
        editor.commit();


    }


    // ----------------------- Get  User Id -------------------
    public static String getDriverId(String key, Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "" );
    }



    // ------------------- Set app status to check build is local or production  -------------------
    public static void setApiUrlLocal(boolean value, Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("app_status", value);
        editor.commit();


    }


    // ----------------------- Get app detailsId -------------------
    public static boolean IsApiUrlLocal( Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("app_status", false );
    }



    // ------------------- Set Truck details -------------------
    public static void setTruckDetails(String TruckNo, String TruckId, Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TruckNumber", TruckNo);
        editor.putString("TruckId", TruckId);
        editor.commit();

    }


    // ----------------------- Get Truck number -------------------
    public static String getTruckNumber(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("TruckNumber", "" );
    }
    // ----------------------- Get Truck ID -------------------
    public static String getTruckId(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("TruckId", "" );
    }


    // ------------------- Set Truck Array  -------------------
    public static void setTruckArray(String TruckArray, Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TruckArray", TruckArray);
        editor.commit();

    }

    // ----------------------- Get Truck Array -------------------
    public static String getTruckArray(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("TruckArray", "[]" );
    }



    // ------------------- Set Event and Logout Date -------------------
    public static void setLogoutEventDate(String eventDate, String logoutDate, Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("event_date", eventDate);
        editor.putString("logout_date", logoutDate);
        editor.commit();

    }

    // ----------------------- Get Event Date -------------------
    public static String getEventDate( Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("event_date", "" );
    }


    // ----------------------- Get Logout Date -------------------
    public static String getLogoutDate( Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("logout_date", "" );
    }


    // ------------------- Set caps status -------------------
    public static void setCapsTheme(boolean value, boolean justSelected, Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("caps", value);
        editor.putBoolean("just_selected", justSelected);
        editor.commit();

    }


    // ----------------------- Get caps status -------------------
    public static boolean isCapsTheme(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("caps", false );
    }


    // ----------------------- Get caps status -------------------
    public static boolean isJustSelected(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("just_selected", false );
    }


    // ----------------------- save login status -------------------
    public static void setLoginStatus(boolean value, Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("loggedIn", value);
        editor.commit();

    }


    // ----------------------- Get fresh login status -------------------
    public static boolean isFreshLogin(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("loggedIn", false );
    }



    // ------------------- Set  User Details -------------------
    public static void setDriverDetails(String keyGender, String valGender,
                                      String keyState, String valState,
                                      String keyCarrierName, String valCarrierName,
                                      String keyCompanyName, String valCompanyName,
                                      String keyDOB, String valDOB,
                                      String keyDriverImage, String valDriverImage,
                                      String keyEmail, String valEmail,
                                      String keyEmpId, String valEmpId,
                                      String keyFastId, String valFastId,
                                      String keyFName, String valFName,
                                      String keyLName, String valLName,
                                      String keyMName, String valMName,
                                      String keyMobile, String valMobile,
                                        String keyCompanyID, String valCompID,
                                        String keyLoginID, String valLoginID,
                                      Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(keyGender, valGender);
        editor.putString(keyState, valState);
        editor.putString(keyCarrierName, valCarrierName);
        editor.putString(keyCompanyName, valCompanyName);
        editor.putString(keyDOB, valDOB);
        editor.putString(keyDriverImage, valDriverImage);
        editor.putString(keyEmail, valEmail);
        editor.putString(keyEmpId, valEmpId);
        editor.putString(keyFastId, valFastId);
        editor.putString(keyFName, valFName);
        editor.putString(keyLName, valLName);
        editor.putString(keyMName, valMName);
        editor.putString(keyMobile, valMobile);
        editor.putString(keyCompanyID, valCompID);
        editor.putString(keyLoginID, valLoginID);

        editor.commit();
    }

    // ------------------- Set Validate  User Details -------------------
    public static void setValidateUser(
                                        String keyEmail, String valEmail,


                                        Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(keyEmail, valEmail);


        editor.commit();
    }


    // ----------------------- Get  User Details -------------------
    public static String getDriverDetails(String key, Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    // ----------------------- Get  User Details -------------------
    public static String getCompanyId(String key, Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    // ------------------- Set Company Id -------------------
    public static void setCompanyId(String key,String value, Context context) {


        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();


    }


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connec = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : NETWORK_TYPES) {
                NetworkInfo netInfo = connec.getNetworkInfo(networkType);
               // Log.d("network", ">>>network State: " + netInfo.getState());
               // Log.d("networkType", ">>>networkType: " + networkType);
                if (netInfo != null && netInfo.isConnectedOrConnecting() ) {

                    // Check for network connections

                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }




    public static void hideSoftKeyboard(Activity activity) {
        try {

            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {    }
    }



    /*========================= Show Toast message =====================*/
    public static void showToastMsg(View view, String message, int viewDuration)
    {

        try {

            Snackbar snackbar = Snackbar
                    .make(view, message, viewDuration);

            snackbar.setActionTextColor(Color.WHITE); //parseColor("#494A59")
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#22ACE4"));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);  //parseColor("#496238")
            snackbar.setDuration(2500);
            snackbar.show();
        }catch (Exception e){
            e.printStackTrace();
        }

        //Toast.makeText(cxt, message, Toast.LENGTH_SHORT).show();
    }



    /*========================= SnackBar Violation View =====================*/
    public static void SnackBarAtTop(View v, final String message, final Context context) {

        try {
            final Snackbar mSnackBar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
            View view = mSnackBar.getView();
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            view.setBackgroundColor(Color.parseColor("#12A4ED"));  // Violation red color
            TextView mainTextView = (TextView) (view).findViewById(android.support.design.R.id.snackbar_text);
            mainTextView.setTextColor(Color.WHITE);

     /*       mSnackBar.setAction("READ", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadViolationDialog(message, context);
                    mSnackBar.dismiss();

                }
            });
            mSnackBar.setActionTextColor(Color.parseColor("#fff68f"));*/
            mSnackBar.show();


        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /* --- Get Image orientaion ---------*/
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }



    /*Check Image Orientation status */
    public static String SaveRotatedImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/"+PROJECT_NAME);
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        String fname = "IMG_" + timeStamp + ".jpeg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(file);
    }



    public static void DeleteFile(File file) {
        try {
            // delete the storage directory if it exists
            if (file.isFile()) {
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static long getFileSizeInKb(String selectedPath){
        File file = new File(selectedPath);
        long fileSize = file.length()/1024;
        return fileSize;
    }

    // Create URI file----
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }



    /*Create file directory with file */
    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),PROJECT_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create " + PROJECT_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == PICK_FROM_CAMERA) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpeg");
        }  else {
            return null;
        }
        return mediaFile;
    }



    public static String DownloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        //    progressBar.setVisibility(View.VISIBLE);
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downldg", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    /*---------------- PARSE JSON -----------------*/
    public static ContainerModel containerModel(ContainerModel container, JSONObject resultJson, int status, boolean isDriverJobSequence, boolean isFirst) throws Exception{

        int OriginType = 5;
        int DestType = 5;
        boolean IsRead = false;
        String ContainerLoadTypeID = "1", containerNo = "N/A", ContainerType = "N/A", OriginAddress = "N/A",
            DestAddress = "N/A", containerSize = "N/A", ContainerTypeDetail = "", ChessieName = "";
        String JobType = "",  PickUpResTime = "",  PickUpResDate = "";

        try {
            if (!resultJson.getString("OrignTypeID").equalsIgnoreCase("null"))
                OriginType = resultJson.getInt("OrignTypeID");

            if (!resultJson.getString("DestinationTypeID").equalsIgnoreCase("null"))
                DestType = resultJson.getInt("DestinationTypeID");


            if (!resultJson.getString("ContainerLoadTypeID").equalsIgnoreCase("null"))
                ContainerLoadTypeID = resultJson.getString("ContainerLoadTypeID");

            if (!resultJson.getString("ContainerNo").equalsIgnoreCase("null"))
                containerNo = resultJson.getString("ContainerNo");

            if (!resultJson.getString("ContainerType").equalsIgnoreCase("null"))
                ContainerType = resultJson.getString("ContainerType");

            if (!resultJson.getString("OrignAddress").equalsIgnoreCase("null") &&
                    !resultJson.getString("OrignAddress").equalsIgnoreCase("")) {
                OriginAddress = resultJson.getString("OrignAddress");
            }

            if (!resultJson.getString("DestinationAddress").equalsIgnoreCase("null") &&
                    !resultJson.getString("DestinationAddress").equalsIgnoreCase("")) {
                DestAddress = resultJson.getString("DestinationAddress");
            }

            if (!resultJson.getString("Size").equalsIgnoreCase("null")) {
                containerSize = resultJson.getString("Size");
            }

            if(!resultJson.isNull("ChessieName"))
                ChessieName =  resultJson.getString("ChessieName");


            if (!resultJson.isNull("IsRead")) {
                IsRead = resultJson.getBoolean("IsRead");
            }

            if (!resultJson.isNull("ContainerTypeDetail")) {
                ContainerTypeDetail = resultJson.getString("ContainerTypeDetail");
            }

            if (resultJson.has("JobType") && !resultJson.isNull("JobType")) {
                JobType = resultJson.getString("JobType");
            }

            if (!resultJson.isNull("PickUpResDate")) {
                PickUpResDate = resultJson.getString("PickUpResDate");
            }

            if (!resultJson.isNull("PickUpResTime")) {
                PickUpResTime = resultJson.getString("PickUpResTime");
            }


        }catch (Exception e){
            e.printStackTrace();
        }


        container = new ContainerModel(
                ChessieName,
                resultJson.getInt("ContainerId"),

                    resultJson.getString("TruckId"),
                    resultJson.getString("ChessieId"),
                    resultJson.getString("CompanyId"),
                containerNo,
                ContainerType,
                resultJson.getString("DriverName"),
                resultJson.getString("GrossWeight"),
                resultJson.getString("SealNo"),
                containerSize,
                status,
                resultJson.getString("TareWeight"),
                resultJson.getString("TrcukName"),
                    IsRead,
                resultJson.getString("NextLocation"),
                resultJson.getInt("NextLocationType"),
                resultJson.getString("PickDate"),
                resultJson.getString("LocationLongitude"),
                resultJson.getString("LocationLatitude"),
                resultJson.getBoolean("IsLoadingAppointmentRequired"),
                resultJson.getBoolean("IsPickUpAppointmentRequired"),
                resultJson.getBoolean("IsReturnAppointmentRequired"),
                resultJson.getString("RefNumber"),
                resultJson.getString("PONumber"),

                OriginType,
                OriginAddress,
                resultJson.getString("OrignLatitude"),
                resultJson.getString("OrignLogitude"),

                DestType,
                DestAddress,
                resultJson.getString("DestinationLatitude"),
                resultJson.getString("DestinationLogitude"),

                resultJson.getString("LocationTypeID"),
                ContainerLoadTypeID,
                resultJson.getString("BillTO"),
                resultJson.getString("BillToContact"),
                resultJson.getString("ReservationNo"),
                resultJson.getString("DropOffReservationNo"),
                resultJson.getString("ShipingLineAddress"),
                resultJson.getBoolean("IsStreetReturned"),
                    resultJson.getString("LoadingLocationId"),
                    resultJson.getString("LegType"),
                    resultJson.getString("MessageText"),
                    resultJson.getString("JobAcceptance"),
                    resultJson.getString("OrderType"),
                JobType,
                PickUpResDate,
                PickUpResTime,
                ContainerTypeDetail,
                isDriverJobSequence,
                isFirst
        );

        return container;
    }



    /*---------------- PARSE JSON -----------------*/
    public static ContainerModel eventContainerModel(JSONObject resultJson, boolean isFirst) throws Exception{


        String LocationName = "", LocationAddress = "";
        int DriverEventId = -1, LocationTypeID = 4, EventStatus = 0;
        try {


            if (!resultJson.isNull("LocationName")) {   // origin address is location name
                LocationName = resultJson.getString("LocationName");
            }

            if (!resultJson.isNull("LocationAddress")) {    // destination address is location address
                LocationAddress = resultJson.getString("LocationAddress");
            }

            if (!resultJson.isNull("LocationTypeID")) { // OrignTypeID is Loctaion type ID
                LocationTypeID = resultJson.getInt("LocationTypeID");
            }

            if (!resultJson.isNull("EventStatus")) {    // status is Event Status
                EventStatus = resultJson.getInt("EventStatus");
            }

            if (!resultJson.isNull("DriverEventId")) {    // status is Event Status
                DriverEventId = resultJson.getInt("DriverEventId");
            }


            // isTitleShown object is used to check event list first item to show yard title
            // containerType is used for EVENT_ITEM_TYPE

        }catch (Exception e){
            e.printStackTrace();
        }


        ContainerModel container = new ContainerModel(
                "", DriverEventId,"","","","",
                EVENT_ITEM_TYPE,"","","","", EventStatus,"",
                "",false,
                "", 0,
                "","","", false,
       false, false,
        "","", LocationTypeID,
                LocationName,"",
               "", 0,
        LocationAddress,"",
                "","","","","",
                "","","",
        false,  "","","","","",
                "","","","", false, isFirst
        );

        return container;
    }



    /*---------------- PARSE JSON FOR COMPANY SETINGS-----------------*/
    public static CompanySetingModel companySetingModel(CompanySetingModel setingModel, JSONObject resultJson, int status) throws Exception{



        setingModel = new CompanySetingModel(
                resultJson.getInt("Id"),
                resultJson.getInt("CompanyId"),

                resultJson.getString("SettingId"),
                resultJson.getString("Value"),
                resultJson.getString("Type"),
                resultJson.getString("SettingName"),
                resultJson.getString("OrderType")

        );

        return setingModel;
    }


    public static String NullCheckJson(JSONObject json, String key){
        String value = "";
        try {
            if(!json.isNull(key)) {
                value = json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String NullCheckString( String value){

            if(value.equals("null")) {
                value = "";
            }

        return value;
    }


    public String checkNullString(JSONObject resultJson, String key){
        String val = "";
        try {
            if (!resultJson.isNull(key))
                val = resultJson.getString(key);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  val;

    }



    // Convert default DateTime format to MM/dd/yyyy date format
    public static String ConvertDateFormatMMddyyyy(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DateFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DateFormatMMddyyyy);

        Date date = null;
        String str = "";

        if(!time.equals("null")) {
            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return str;
    }


    public static String getCurrentDate(){
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(DateFormat);
        Calendar c = Calendar.getInstance();
        String StringCurrentDate = "";

        try {
            StringCurrentDate = currentDateFormat.format(c.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return StringCurrentDate;
    }

    public static String GetCurrentUTCTimeFormat(){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DateFormat);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentTime = dateFormatGmt.format(new Date());
        return currentTime;
    }


    public static String SET_DATE_FORMAT(String StrDateTime){
        if(StrDateTime.length() >= 19) {
            StrDateTime = StrDateTime.substring(0, 19);
            String ChangedDateFormat = "MM'-'dd'-'yyyy' 'H':'mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");  //:SSSZ


            try {
                //Date containetDate = simpleDateFormat.parse(StrDateTime);
                Date d = simpleDateFormat.parse(StrDateTime);
                simpleDateFormat.applyPattern(ChangedDateFormat);
                ChangedDateFormat = simpleDateFormat.format(d);
                StrDateTime = ChangedDateFormat;
                //  loadDateTime.setText(StrDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return StrDateTime;
    }




    public static int HourFromMin(int min){
        int hours = min / 60; //since both are ints, you get an int
        return hours;
    }

    public static int MinFromHourOnly(int min){
        int minutes = min % 60;
        return minutes;
    }




    public static void ShowLocalNotification(Context context, Activity activity, String title, String message, int id ){

        Intent intent = new Intent(context, activity.getClass());   //
        //mNotificationManager.showLocalNotification(title, message, id , intent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        101,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "als_01";// The id of the channel.
            CharSequence name = context.getResources().getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Create a notification and set the notification channel.
            notification = mBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.truck_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setChannelId(CHANNEL_ID)
                    .build();

            notificationManager.createNotificationChannel(mChannel);
        }else {
            notification = mBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.truck_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .build();

        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }




    /* ---------- Set Origin Location View ------------- */
    public static void SET_ORIGIN_VIEW(TextView view, int OrignStatus, String currentLocation){

        switch (OrignStatus){

            case 1:
                view.setText("(Pickup Location)");

                break;

            case 2:
                view.setText("(Loading Location)");
                break;

            case 3:
                view.setText("(DropOff Location)");
                break;

            case 4:
                view.setText("(Yard Location)");
                break;

            case 5:
                view.setText("(No Location)");
                break;

            case 6:
                view.setText("(Unloading Location)");
                break;


            default:
                view.setText("(No Location)");

                break;
        }
    }



    /* ---------- Set Destination Location View ------------- */
    public static void SET_DESTINATION_VIEW(TextView destTV, int DestStatus){

        switch (DestStatus){

            case 1:
                destTV.setText("(Pickup Location)");

                break;

            case 2:
                destTV.setText("(Loading Location)");
                break;

            case 3:
                destTV.setText("(DropOff Location)");
                break;

            case 4:
                destTV.setText("(Yard Location)");
                break;

            case 5:
                destTV.setText("(No Location)");
                break;

            case 6:
                destTV.setText("(Unloading Location)");
                break;

            default:
                destTV.setText("(No Location)");

                break;
        }
    }




    public static String GetAppVersion(Context context, String type){
        String AppVersion = "";
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            if(type.equals("VersionName")) {
                AppVersion = info.versionName;
            }else{
                AppVersion = String.valueOf(info.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return AppVersion;
    }


    public static boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public static String GetCurrentDeviceDate(){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DateFormatMMddyyyy);
        String currentTime = dateFormatGmt.format(new Date());
        return currentTime;
    }


    // Set time when popup window is opened -------------------
    public static void SetUpdateAppDialogTime( String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("update_popup_time", value);
        editor.commit();
    }


    // Get popup window is opened time -------------------
    public static String GetUpdateAppDialogTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("update_popup_time", "");
    }



    public static void ShowLocalNotification(Context context, String title, String message, int id ){

        ClearNotificationById(context, id);

        Intent intent = new Intent(context, TabActivity.class);   //
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        101,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "als_02";// The id of the channel.
            CharSequence name = context.getResources().getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Create a notification and set the notification channel.
            notification = mBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.als_logo_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setChannelId(CHANNEL_ID)
                    .build();

            notificationManager.createNotificationChannel(mChannel);
        }else {
            notification = mBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.als_logo_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .build();

        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }


    public void ClearAllNotifications(Context context){
        if(context != null) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancelAll();
        }
    }

    public static void ClearNotificationById(Context context, int id){
        if(context != null) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(id);
        }
    }

    public static void hideKeyboardView(Context cxt, View lay){
        InputMethodManager imm = (InputMethodManager) cxt.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lay.getWindowToken(), 0);
    }

    public static void saveDriverLoginDetails(JSONObject resultJson, Context context){

        try{

            Constants.setDriverId( Constants.KEY_DRIVER_ID , resultJson.getString("DriverId") , context);
            Constants.setValidateUser(Constants.KEY_EMAIL, resultJson.getString("EmailAddress"),context);

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
                    context);



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static DateTime getDateTimeObj(String date) {
        DateTime oDate = null;
        DateTimeFormatter dtf = null;
        boolean requireOnlyTime = false;

        try {
            if (date != null && date != "") {
                if (requireOnlyTime) {
                    dtf = org.joda.time.format.DateTimeFormat.forPattern("HH:mm:ss");
                    oDate = dtf.parseDateTime(date);
                } else {
                    if (date.contains(".")) {
                        oDate = DateTime.parse(date).toDateTime(DateTimeZone.UTC);
                    } else {
                        oDate = new LocalDateTime(date).toDateTime(DateTimeZone.UTC);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return oDate;
    }


}


