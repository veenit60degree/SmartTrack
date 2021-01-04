package com.json.parser;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;


public class BaseRequest <T> extends Request<T> {
    private final Class<T> resultClass;
    private final Response.Listener<T> listener;
    Gson gson = new Gson();
    private final int REQUEST_TIMEOUT_LIMIT_SECONDS = 30; // Value in seconds

    public BaseRequest(int method, String url, Class<T> resultClass,
                       Response.Listener<T> listenerSuccess, Response.ErrorListener listenerError) {
        super(method, url, listenerError);
        this.resultClass = resultClass;
        this.listener = listenerSuccess;
        this.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_LIMIT_SECONDS * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String( response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, resultClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}