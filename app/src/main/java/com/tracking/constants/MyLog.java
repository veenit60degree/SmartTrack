package com.tracking.constants;

import android.util.Log;

/**
 * Created by kumar on 2/20/2017.
 */

public class MyLog {

    public MyLog (boolean isShown, String msg){

        if(isShown)
            Log.d("DEBUG", "value: " +msg);


    }
}
