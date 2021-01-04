package com.tracking.smarttrack.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.MediaActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.fresco.processors.BlurPostprocessor;

public class ProfileFragment  extends Fragment  {


    View rootView;
    TextView userNameTV,companyNameTV;
    TextView profileNameTV, profilePortNoTV, profileDlNoTV, profileDobTV, profileGenderTV, profileEmailTV,contInfoTitleTV;
    ImageView userProfileImgVw,uploadImgBtn,editImgVw;
    int MEDIA_FILE = 515;
    RelativeLayout editImageBtn, menuActionBar,notificationLay,searchActionBar;
    String DriverId = "", DriverName = "", DOB = "", DriverImageName = "", DriverImagePath = "", LoginId = "",
    Gender = "", PortPassNumber = "", DriverLicenceNumber = "", Email = "", CompanyName = "";

    private DisplayImageOptions options;
    ImageLoader imageLoader;

    private SimpleDraweeView backgroundBlurIV;
    //    1.Processor
    private Postprocessor postprocessor;
    //2.Image Request
    private ImageRequest imageRequest;
    //3.Controller
    private PipelineDraweeController controller;

    //static variables
    private int BLUR_PRECENTAGE = 50;

    String tempImgPath = ""; //https://image.flaticon.com/icons/png/128/149/149071.png

    RequestQueue profileDetailsReq;
    APIs API_URL;


/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        try {
            initView(rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (InflateException e) {
            e.printStackTrace();
        }


        initView(rootView);

        return rootView;

    }

    void initView(View container) {

        API_URL              = new APIs(getActivity());
        profileDetailsReq    = Volley.newRequestQueue(getActivity());
        userNameTV           = (TextView) container.findViewById(R.id.userNameProfileTV);
        companyNameTV        = (TextView) container.findViewById(R.id.companyNameTV);
        profileNameTV        = (TextView) container.findViewById(R.id.profileNameTV);
        profilePortNoTV      = (TextView) container.findViewById(R.id.profilePortNoTV);
        profileDlNoTV        = (TextView) container.findViewById(R.id.profileDlNoTV);
        profileDobTV         = (TextView) container.findViewById(R.id.profileDobTV);
        profileGenderTV      = (TextView) container.findViewById(R.id.profileGenderTV);
        profileEmailTV       = (TextView) container.findViewById(R.id.profileEmailTV);
        contInfoTitleTV      = (TextView) container.findViewById(R.id.actionBarTitleTV);
        userProfileImgVw     = (ImageView) container.findViewById(R.id.userProfileImgVw);
    //    backgroundBlurIV           = (ImageView) container.findViewById(R.id.backgroundBlurIV);
        backgroundBlurIV     = (SimpleDraweeView)container.findViewById(R.id.backgroundBlurIV);
        uploadImgBtn         = (ImageView) container.findViewById(R.id.uploadImgBtn);
        editImgVw            = (ImageView) container.findViewById(R.id.editImgVw);
        editImageBtn         = (RelativeLayout) container.findViewById(R.id.editImageBtn);
        menuActionBar        = (RelativeLayout) container.findViewById(R.id.menuActionBar);
        notificationLay      = (RelativeLayout)container.findViewById(R.id.notificationLay);
        searchActionBar      = (RelativeLayout)container.findViewById(R.id.searchActionBar);

        DriverId    = Constants.getDriverId(Constants.KEY_DRIVER_ID, getContext());
        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,getContext());
//INSTANTIATE BLUR POST PROCESSOR
        postprocessor = new BlurPostprocessor(getActivity(), BLUR_PRECENTAGE);

        //INSTATNTING IMAGE REQUEST USING POST PROCESSOR AS PARAMETER
        imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(tempImgPath))
                .setPostprocessor(postprocessor)
                .build();

        //INSTANTATE CONTROLLOR()
        controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(backgroundBlurIV.getController())
                .build();

        //LOAD BLURRED IMAGE ON SimpleDraweeView(VIEW)
        backgroundBlurIV.setController(controller);






        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_profile_dummy)
                .showImageForEmptyUri(R.drawable.user_profile_dummy)
                .showImageOnFail(R.drawable.user_profile_dummy)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();



        imageLoader = ImageLoader.getInstance();

        editImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // mediaDialog();

            }

        });


        menuActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.sliderLay.performClick();
            }
        });

        contInfoTitleTV.setText("My Profile");
        searchActionBar.setVisibility(View.GONE);
        notificationLay.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();

        if(Constants.isNetworkAvailable(getActivity())) {
            if (DriverName.length() == 0) {
                GetDriverDetail();
            }
        }


    }


    void GetDriverDetail(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.GET_DRIVER_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response: " + response);

                        JSONObject obj, resultJson;
                        int status ;
                        try {
                            obj = new JSONObject(response);
                            status = obj.getInt("Status");

                            if(status == 1) {
                                resultJson = new JSONObject(obj.getString("Result"));


                                DriverName = resultJson.getString("DriverName");
                                DOB = resultJson.getString("DOB");
                                DriverImagePath = resultJson.getString("DriverImagePath");
                                DriverImageName = resultJson.getString("DriverImageName");
                                Gender = resultJson.getString("Gender");
                                PortPassNumber = resultJson.getString("PortPassNumber");
                                DriverLicenceNumber = resultJson.getString("DriverLicenceNumber");
                                Email = resultJson.getString("Email");


                                if(CompanyName.equals("null")){
                                    companyNameTV.setText("");
                                }else {
                                    companyNameTV.setText(CompanyName);
                                }
                                if(DriverName.equals("null")){
                                    userNameTV.setText("");
                                }else {
                                    userNameTV.setText(DriverName);
                                }
                                if(DriverName.equals("null")){
                                    profileNameTV.setText("");
                                }else {
                                    profileNameTV.setText(DriverName);
                                }
                                if(PortPassNumber.equals("null")){
                                    profilePortNoTV.setText("");
                                }else {
                                    profilePortNoTV.setText(PortPassNumber);
                                }
                                if(DriverLicenceNumber.equals("null")){
                                    profileDlNoTV.setText("");
                                }else {
                                    profileDlNoTV.setText(DriverLicenceNumber);
                                }
                                if(Gender.equals("null")){
                                    profileGenderTV.setText("");
                                }else {
                                    profileGenderTV.setText(Gender);
                                }
                                if(DOB.equals("null")){
                                    profileDobTV.setText("");
                                }else {
                                    profileDobTV.setText(DOB);
                                }
                                if(Email.equals("null")){
                                    profileEmailTV.setText("");
                                }else {
                                    profileEmailTV.setText(Email);
                                }

                                DriverImagePath = DriverImagePath.replaceAll(" ", "%20");
                                Log.d("DriverImagePath", "DriverImagePath: " +DriverImagePath);
                                imageLoader.displayImage(DriverImagePath, uploadImgBtn, options);

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
                params.put("LoginId", LoginId);
                params.put("Latitude", Constants.CURRENT_LATITUDE);
                params.put("Longitude", Constants.CURRENT_LONGITUDE);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        profileDetailsReq.add(postRequest);

    }


    /* ----------------- Media Dialog For Camera/Gallery---------------- */
    void mediaDialog(){

        final Dialog mediaPicker = new Dialog(getActivity());
        mediaPicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = mediaPicker.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mediaPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPicker.setContentView(R.layout.popup_loading_status);

        Button leftWithChassisBtn, leftWithOutChassisBtn, liveLoadingBtn,  cancelBtn;
        leftWithChassisBtn = (Button)mediaPicker.findViewById(R.id.leftWithChassisBtn);
        leftWithOutChassisBtn = (Button)mediaPicker.findViewById(R.id.leftWithOutChassisBtn);
        liveLoadingBtn = (Button)mediaPicker.findViewById(R.id.liveLoadingBtn);
        cancelBtn = (Button)mediaPicker.findViewById(R.id.closeSBtn);



        liveLoadingBtn.setVisibility(View.GONE);
        leftWithChassisBtn.setText("Camera");
        leftWithOutChassisBtn.setText("Gallery");

        leftWithChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPicker.dismiss();
                mediaIntent("camera");
            }
        });


        leftWithOutChassisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPicker.dismiss();
                mediaIntent("gallery");
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

    /* ----------------- Call Media Activity ---------------- */
    void mediaIntent(String from){
        Intent i = new Intent(getActivity(), MediaActivity.class);
        i.putExtra("type", from);
        startActivityForResult(i, MEDIA_FILE );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK && requestCode == MEDIA_FILE) {

            String imagePath = data.getStringExtra("result");

            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            uploadImgBtn.setImageBitmap(bitmap);
            UploadDriverImage( DriverId, imagePath);
        }
    }

    void UploadDriverImage(final String DriverId, final String ImageName){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.UPLOAD_DRIVER_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", ">>>Response: " + response);

                        JSONObject obj, resultJson;
                        int status ;
                        try {
                            obj = new JSONObject(response);
                            status = obj.getInt("Status");

                            if(status == 1) {
                                resultJson = new JSONObject(obj.getString("Result"));


//                                DriverName = resultJson.getString("DriverName");
//                                DOB = resultJson.getString("DOB");
//                                DriverImagePath = resultJson.getString("DriverImagePath");
//                                DriverImageName = resultJson.getString("DriverImageName");

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
                params.put("File", ImageName);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime50, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }



}
