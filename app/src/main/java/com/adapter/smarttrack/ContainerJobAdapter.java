package com.adapter.smarttrack;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.models.ContainerModel;
import com.tracking.constants.APIs;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;
import com.tracking.smarttrack.fragment.ExportDetailFragment;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.NewFeedFragment;
import com.tracking.smarttrack.fragment.OTPFragment;
import com.tracking.smarttrack.fragment.SelfAssignFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContainerJobAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;
    List<ContainerModel> transferList;
    String jobAcceptance;
    String driver_Id = "", StrContainerId = "";
    private int VIEW_DURATION = 5000;
    Button isAcceptbtn;
    int OrderType = 1;
    Button hideAllbtns;
    String LoginId ="";
    String DriverID ="";
    String DeviceID = "";
    String CompanyID = "";
    APIs API_URL;


    public ContainerJobAdapter(Context context, List<ContainerModel> transferList, int orderType) {
        this.context = context;
        this.transferList = transferList;
        this.OrderType = orderType;
        mInflater = LayoutInflater.from(context);
        API_URL = new APIs(context);
    }

    @Override
    public int getCount() {
        return transferList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return transferList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;



        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_container_job, null);

            holder.container_name_tv    = (TextView) convertView.findViewById(R.id.container_name_tv);
            holder.container_distnc_tv  = (TextView) convertView.findViewById(R.id.container_distnc_tv);
            holder.pickup_loc_tv        = (TextView) convertView.findViewById(R.id.pickup_loc_tv);
            holder.pickup_address_tv    = (TextView) convertView.findViewById(R.id.pickup_address_tv);
            holder.locTypeTV            = (TextView) convertView.findViewById(R.id.locTypeTV);
         //   holder.originItemDropTimeTV = (TextView) convertView.findViewById(R.id.originItemDropTimeTV);
            holder.originItemResNumTV   = (TextView) convertView.findViewById(R.id.originItemResNumTV);
            holder.originItemDropDateTV = (TextView) convertView.findViewById(R.id.originItemDropDateTV);

            holder.pickup_date_tv       = (TextView) convertView.findViewById(R.id.pickup_date_tv);
            holder.container_type_tv    = (TextView) convertView.findViewById(R.id.container_type_tv);
            holder.serial_no_tv         = (TextView) convertView.findViewById(R.id.serial_no_tv);
            holder.dest_detail_tv       = (TextView) convertView.findViewById(R.id.dest_detail_tv);
            holder.pickup_loc_Trans_tv  = (TextView) convertView.findViewById(R.id.pickup_loc_Trans_tv);
            holder.jobTypeTxtView       = (TextView)convertView.findViewById(R.id.jobTypeTxtView);

            holder.isAcceptbtn          = (Button) convertView.findViewById(R.id.isAcceptbtn);
            holder.isRejectbtn          = (Button) convertView.findViewById(R.id.isRejectbtn);
            holder.layoutbtns           = (LinearLayout) convertView.findViewById(R.id.layoutbtns);
            holder.container_bar_lay    = (LinearLayout)convertView.findViewById(R.id.container_bar_lay);
            holder.originAddresslay     = (RelativeLayout)convertView.findViewById(R.id.originAddresslay);

            holder.location_dash        = (ImageView)convertView.findViewById(R.id.location_dash);
            holder.location_dash2       = (ImageView)convertView.findViewById(R.id.location_dash2);
            holder.location_dash3       = (ImageView)convertView.findViewById(R.id.location_dash3);
            holder.location_pointer_iv  = (ImageView)convertView.findViewById(R.id.location_pointer_iv);

            holder.hideAllbtn           = (Button) convertView.findViewById(R.id.hideAllbtn);
            holder.listCard             = (CardView)convertView.findViewById(R.id.listCard);

            isAcceptbtn                 = (Button) convertView.findViewById(R.id.isAcceptbtn);
            hideAllbtns                 = (Button) convertView.findViewById(R.id.hideAllbtn);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        if(ExportFragment.containerExportList.size() == transferList.size()) {
            if (transferList.get(position).isDriverJobSequence() && position != 0) {
                holder.listCard.setCardBackgroundColor(context.getResources().getColor(R.color.black_disabled));
              //  holder.serial_no_tv.setBackgroundResource(R.drawable.circle_number_dull);

                holder.container_name_tv.setTextColor(context.getResources().getColor(R.color.disabled_text));
                holder.container_type_tv.setTextColor(context.getResources().getColor(R.color.disabled_text));
                holder.pickup_address_tv.setTextColor(context.getResources().getColor(R.color.disabled_text));
                holder.dest_detail_tv.setTextColor(context.getResources().getColor(R.color.disabled_text));
                holder.container_distnc_tv.setTextColor(context.getResources().getColor(R.color.disabled_text));
              //  holder.serial_no_tv.setTextColor(context.getResources().getColor(R.color.white_trans));
                holder.pickup_loc_tv.setTextColor(context.getResources().getColor(R.color.white_trans));
                holder.pickup_date_tv.setTextColor(context.getResources().getColor(R.color.black_disabled));
                holder.pickup_loc_Trans_tv.setVisibility(View.VISIBLE);
                holder.originItemDropDateTV.setTextColor(context.getResources().getColor(R.color.white_trans));
             //   holder.originItemDropTimeTV.setTextColor(context.getResources().getColor(R.color.white_trans));
                holder.originItemResNumTV.setTextColor(context.getResources().getColor(R.color.white_trans));

            }
        }

        String OriginAddress  = HtmlText(transferList.get(position).getOrignAddress());
        String DestAddress    = HtmlText(transferList.get(position).getDestinationAddress());
        String JobAcceptance  = (transferList.get(position).getJobAcceptance());
        Integer Status        = (transferList.get(position).getStatus());
        String DriverName  = (transferList.get(position).getDriverName());

        holder.container_name_tv.setText(transferList.get(position).getContainerNo());
        holder.container_type_tv.setText(transferList.get(position).getContainerType());
        holder.pickup_address_tv.setText(Html.fromHtml(OriginAddress));
        holder.dest_detail_tv.setText(Html.fromHtml(DestAddress));
        holder.container_distnc_tv.setText(transferList.get(position).getSize());
      //  holder.serial_no_tv.setText( String.valueOf(position + 1));
        holder.locTypeTV.setText(transferList.get(position).getJobType());


        setLocationStatusView( holder.pickup_loc_tv, transferList.get(position).getOrignTypeID(),
                transferList.get(position).getStatus(), transferList.get(position).isRead());

        setLocationStatusView( holder.pickup_loc_Trans_tv, transferList.get(position).getOrignTypeID(),
                transferList.get(position).getStatus(), transferList.get(position).isRead());

        driver_Id = Constants.getDriverId(Constants.KEY_DRIVER_ID, context);
        DeviceID    = LoginActivity.deviceID.trim();

        LoginId     = Constants.getDriverDetails(Constants.KEY_LOGIN,context);
        CompanyID   = Constants.getCompanyId(Constants.KEY_COMPANY, context);


       try{
            String pickResNo = Constants.NullCheckString(transferList.get(position).getReservationNo());
            String pickResDate = Constants.ConvertDateFormatMMddyyyy(Constants.NullCheckString(transferList.get(position).getPickUpResDate()));
            String pickResTime = Constants.NullCheckString(transferList.get(position).getPickUpResTime());

            if (pickResTime.length() > 5) {
                pickResTime = pickResTime.substring(0, 5);
            }

            //  holder.originItemDropTimeTV.setText(context.getResources().getString(R.string.pickup_time) + " " +  );
            holder.originItemResNumTV.setText(context.getResources().getString(R.string.res_no) + " " + pickResNo);
            holder.originItemDropDateTV.setText(context.getResources().getString(R.string.pickup_date) + " " + pickResDate + " " + pickResTime);

       }catch (Exception e){}




       if(transferList.get(position).isTitleShown()){
            if (transferList.get(position).getContainerType().equals(Constants.EVENT_ITEM_TYPE)) {
                holder.jobTypeTxtView.setText(context.getResources().getString(R.string.back_to_yard));
                holder.jobTypeTxtView.setVisibility(View.VISIBLE);
            }else{
                holder.jobTypeTxtView.setText(context.getResources().getString(R.string.yours_jobs));
                holder.jobTypeTxtView.setVisibility(View.VISIBLE);
            }
        }


        if(transferList.get(position).getContainerType().equals(Constants.EVENT_ITEM_TYPE)) {
            holder.locTypeTV.setVisibility(View.GONE);
            holder.container_bar_lay.setVisibility(View.GONE);
            holder.originItemDropDateTV.setVisibility(View.GONE);
            holder.originItemDropDateTV.setVisibility(View.GONE);
            holder.originAddresslay.setVisibility(View.INVISIBLE);
            holder.pickup_date_tv.setVisibility(View.GONE);
            holder.location_dash.setVisibility(View.GONE);
            holder.location_dash2.setVisibility(View.GONE);
            holder.location_dash3.setVisibility(View.GONE);
            holder.location_pointer_iv.setVisibility(View.GONE);
            holder.originItemResNumTV.setVisibility(View.GONE);

            holder.originAddresslay.setLayoutParams(new LinearLayout.LayoutParams(10, 30));

            String locationName     = transferList.get(position).getOrignAddress() ;
            String LocationAddress  = transferList.get(position).getDestinationAddress() ;
            String nextLine = "\n\n";

            SpannableString spanableAddress=  new SpannableString(locationName + nextLine +LocationAddress);
            spanableAddress.setSpan(new RelativeSizeSpan(1.6f), 0,locationName.length(), 0); // set size
            spanableAddress.setSpan(new ForegroundColorSpan(Color.parseColor("#334856")), 0, locationName.length(), 0);// set color
            spanableAddress.setSpan(new ForegroundColorSpan(Color.parseColor("#4C5E6A")), locationName.length(),
                    locationName.length() + LocationAddress.length() + nextLine.length(), 0);// set color


            holder.dest_detail_tv.setText(spanableAddress);
        }


        holder.isAcceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(context);

                if(Constants.INTERNET_CONNECTIVITY_STATUS){

                    jobAcceptance = "1";
                    StrContainerId = String.valueOf(transferList.get(position).getContainerId());
                    jobConfirmDialog(position);
                }else{
                    // Constants.showToastMsg(btnLogin, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                    Constants.showToastMsg(isAcceptbtn, "Please check your internet connection", VIEW_DURATION);
                }

            }

        });

        holder.isRejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.INTERNET_CONNECTIVITY_STATUS = Constants.isNetworkAvailable(context);

                if(Constants.INTERNET_CONNECTIVITY_STATUS){

                    jobAcceptance = "2";
                    StrContainerId = String.valueOf(transferList.get(position).getContainerId());

                jobConfirmDialog(position);
                }else{
                    // Constants.showToastMsg(btnLogin, Constants.INTERNET_MESSAGE, VIEW_DURATION);
                    Constants.showToastMsg(isAcceptbtn, "Please check your internet connection", VIEW_DURATION);
                }
            }
        });


        if(JobAcceptance =="2" && DriverName =="") {
            holder.hideAllbtn.setVisibility(View.VISIBLE);
        } else {
            holder.hideAllbtn.setVisibility(View.GONE);
        }


        if (OrderType == Constants.ASSIGNED ) {
            if(JobAcceptance.equals("0")){
                holder.layoutbtns.setVisibility(View.VISIBLE);
            }else{
                holder.layoutbtns.setVisibility(View.GONE);
            }
        }else{
            if ((Status.equals(2) || Status.equals(18) || Status.equals(19)) && JobAcceptance.equals("0")) {
                holder.layoutbtns.setVisibility(View.VISIBLE);
            } else {
                holder.layoutbtns.setVisibility(View.GONE);
            }
        }

        //String ChangedDateFormat = "dd'-'MMM'-'yyyy' 'h':'mm' 'a";
        String ChangedDateFormat = "MM'-'dd'-'yyyy' 'H':'mm";
        String StrDateTime = transferList.get(position).getPickDate();
        setDateFormat(StrDateTime, ChangedDateFormat, holder.pickup_date_tv);

        return convertView;

    }


    String HtmlText(String address) {
        String title = "", locAdd = "", htmlAddress = "";
        if (!address.equals("") && !address.equalsIgnoreCase("null") && !address.equalsIgnoreCase("N/A")) {
            String[] addArray = address.split(":");
            title = addArray[0];
            if (addArray.length > 1)
                locAdd = addArray[1];
            htmlAddress = "<b>" + title + ".</b> " + locAdd;
        } else {
            htmlAddress = address;
        }
        return htmlAddress;
    }

    void setDateFormat(String StrDateTime, String ChangedDateFormat, TextView view) {
        if (StrDateTime.length() >= 19) {
            StrDateTime = StrDateTime.substring(0, 19);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");  //:SSSZ

            try {
                if(StrDateTime == "null")
                {
                    StrDateTime = "";
                }else {
                    Date d = simpleDateFormat.parse(StrDateTime);
                    simpleDateFormat.applyPattern(ChangedDateFormat);
                    ChangedDateFormat = simpleDateFormat.format(d);
                    view.setText(ChangedDateFormat);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    void setLocationStatusView( TextView locationTypeView, int Orignstatus, int containerStatus, boolean isRead) {

        switch (Orignstatus) {


            case 1:
                locationTypeView.setText("Pickup Loc");

                break;

            case 2:
                if (Constants.CONTAINER_LIST_TYPE == "1") {
                    locationTypeView.setText("Unloading Loc");
                } else {
                    locationTypeView.setText("Loading Loc");
                }
                break;

            case 3:
                locationTypeView.setText("DropOff Loc");
                break;

            case 4:
                locationTypeView.setText("Yard Loc");
                break;

            case 6:
                locationTypeView.setText("Unloading Loc");
                break;

            default:
                locationTypeView.setText("No Loc");
              if (containerStatus == Constants.AssignForLoadingLocation) {

                    if (Constants.CONTAINER_LIST_TYPE.equals("1")) {
                        locationTypeView.setText("Unloading Loc");
                    } else {
                        locationTypeView.setText("Loading Loc");
                    }
                } else if (containerStatus == Constants.AssignForYardLocation) {
                    locationTypeView.setText("Yard Loc");
                  //  view.setText("Y");
                }
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    public class ViewHolder {
        TextView jobTypeTxtView;
        TextView container_name_tv, container_distnc_tv, pickup_loc_tv, dest_detail_tv, pickup_address_tv, locTypeTV,
                pickup_date_tv, container_type_tv, serial_no_tv, pickup_loc_Trans_tv, originItemResNumTV, originItemDropDateTV; //originItemDropTimeTV
        Button isAcceptbtn, isRejectbtn, hideAllbtn;
        ImageView location_dash, location_dash2, location_dash3, location_pointer_iv;
        CardView listCard;
        RelativeLayout originAddresslay;
        LinearLayout layoutbtns, container_bar_lay;


    }

    public void JobAcceptence(final int pos, final String driver_Id, final String strContainerId, final String jobAcceptance) {

        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest postRequest = new StringRequest(Request.Method.POST, API_URL.JOB_Acceptance,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj, resultJson;
                        Log.d("Response ", ">>>Response: " + response);

                        try {
                            obj = new JSONObject(response);

                            if (obj.getInt("Status") == 0) {
                               Constants.showToastMsg(isAcceptbtn, "Not Successfull", VIEW_DURATION);


                            } else if (obj.getInt("Status") == 1) {
                                if (obj.getBoolean("Success") == true) {


                                    Log.d("resultJson", "resultJson: ");


                                    if(jobAcceptance=="2") {
                                        Constants.showToastMsg(isAcceptbtn, "Your request Accepted Successfully", VIEW_DURATION);
                                        NewFeedFragment.newFeedList.remove(pos);


                                      /*  if (OrderType == Constants.EXPORT) {
                                            ContainerModel exprtModel = ExportFragment.containerExportList.get(pos);
                                            exprtModel.setDriverName("");
                                            exprtModel.setJobAcceptance("2");
                                            ExportFragment.containerExportList.set(pos, exprtModel);

                                        } else {
                                            ContainerModel importModel = ExportFragment.containerAssignedList.get(pos);
                                            importModel.setDriverName("");
                                            importModel.setJobAcceptance("2");
                                          //  ExportFragment.containerAssignedList.set(pos, importModel);

                                            ExportFragment.containerAssignedList.remove(pos);

                                            Constants.CONTAINER_LIST_TYPE = String.valueOf(Constants.EXPORT);
                                            ExportFragment.IS_SELF_ASIGN = false;
                                            ExportFragment.IS_SELF_PICKEDUP = true;

                                        }*/

                                        notifyDataSetChanged();

                                    }else if(jobAcceptance=="1") {

                                       /* if (OrderType == Constants.EXPORT) {
                                            ContainerModel exprtModel = ExportFragment.containerExportList.get(pos);
                                            exprtModel.setJobAcceptance("1");
                                            ExportFragment.containerExportList.set(pos, exprtModel);

                                        } else {
                                            ContainerModel importModel = ExportFragment.containerAssignedList.get(pos);
                                            importModel.setJobAcceptance("1");
                                            ExportFragment.containerAssignedList.remove(pos);

                                        }*/

                                       try {
                                           if (ExportFragment.containerExportList.size() > pos) {
                                               ContainerModel exprtModel = ExportFragment.containerExportList.get(pos);
                                               exprtModel.setJobAcceptance("1");
                                               NewFeedFragment.newFeedList.set(pos, exprtModel);
                                           }
                                           notifyDataSetChanged();

                                           if (NewFeedFragment.menuActionBar != null) {
                                               NewFeedFragment.menuActionBar.performClick();
                                           }
                                       }catch (ArrayIndexOutOfBoundsException e){
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("error", ">>error: " + error);
                        Constants.showToastMsg(isAcceptbtn, "Please check your internet connection", VIEW_DURATION);

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ContainerId", strContainerId);
                params.put("DriverId", driver_Id);
                params.put("JobAcceptance", jobAcceptance);


                params.put("DeviceID", DeviceID);
                params.put("LoginId", LoginId);
                params.put("CompanyID", CompanyID);

                Log.d("params", "params: " + params);
                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    void jobConfirmDialog(final int position) {

        final Dialog picker = new Dialog(context);
        picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        picker.setContentView(R.layout.popup_logout);

        RelativeLayout cancelBtn, confirmBtn;
        cancelBtn = (RelativeLayout) picker.findViewById(R.id.cancelPopupBtn);
        confirmBtn = (RelativeLayout) picker.findViewById(R.id.confirmPopupBtn);
        Button confirmPopupButton = (Button) picker.findViewById(R.id.confirmPopupButton);
        Button cancelPopupButton = (Button) picker.findViewById(R.id.cancelPopupButton);
        TextView titleDescView = (TextView) picker.findViewById(R.id.titleDescView);

        if(jobAcceptance == "1") {
            titleDescView.setText("Do you really want to accept this job?");
        }else{
            titleDescView.setText("Do you really want to reject this job?");
        }

        confirmPopupButton.setText("Confirm");
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

                JobAcceptence(position, driver_Id, StrContainerId, jobAcceptance);
                picker.dismiss();
            }
        });

        picker.show();

    }

}
