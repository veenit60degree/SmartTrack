package com.json.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tracking.constants.Constants;

import java.util.Map;


/*==================== Global class for API Handler =====================*/
public class MainAsyncTask  {

	public static ProgressDialog mDialog;

	MainAsynListener<String> listener;
	int receivedId;
	int errorCode;
	boolean isDialogDisplay, isSuccess = false;
	Context context;
	String url, dialog_text;
	Map<String,String> nameValuePairs;
	
	public MainAsyncTask(Context context, String url, int receivedId,
			MainAsynListener<String> listener,
			boolean isDialogDisplay, Map<String,String>  nameValuePairs,String dialog_text) {
		this.context = context;
		this.url = url;
		this.receivedId = receivedId;
		this.listener = listener;
		this.isDialogDisplay = isDialogDisplay;
		this.nameValuePairs=nameValuePairs;
		this.dialog_text=dialog_text;

	}



	void aaa(){

		RequestQueue queue = Volley.newRequestQueue(context);


		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						isSuccess = true;
						if(isDialogDisplay){
							showCommonDialog(context);
						}
						Log.d("Response ", ">>>Response: " + response);
						listener.onPostSuccess(response, receivedId, isSuccess);

					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						// response
						Log.d("error", ">>error: " +error);
						// Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
						//Toast.makeText(context,  error.toString(), Toast.LENGTH_LONG).show();
						if(isDialogDisplay){
							if (mDialog.isShowing()) {
								cancelDialog();
							}
						}

						isSuccess = false;
					listener.onPostError(receivedId, errorCode);

					}
				}
		) {


			@Override
			protected Map<String, String> getParams()
			{
				//Map<String,String> params = new HashMap<String, String>();

					//params = nameValuePairs;
				    Log.d("params", "params: " + nameValuePairs);
				return nameValuePairs;
			}
		};

		RetryPolicy policy = new DefaultRetryPolicy(Constants.SocketRequestTime20, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		postRequest.setRetryPolicy(policy);
		queue.add(postRequest);

	}




	
	// Common Progress bar
    public void showCommonDialog(Context mContext) {

    	mDialog = new ProgressDialog(mContext);
    	mDialog.setMessage(dialog_text);
    	mDialog.setIndeterminate(false);
    	
    	mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	mDialog.setCancelable(true);
    	
    	try {
    	mDialog.show();
		} catch (Exception e) { }

    }

    // cancel progress dialog

	public void cancelDialog() {
		try {
			if (mDialog != null) {
				mDialog.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}

