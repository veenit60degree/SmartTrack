package com.tracking.constants;

import android.app.Activity;

import android.content.Intent;

import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;

public class themeUtils {

    private static int cTheme;



    public final static int ON = 0;

    public final static int OFF = 1;

    public static void changeToTheme(Activity activity, int theme)

    {

        cTheme = theme;

        activity.finish();

        activity.startActivity(new Intent(activity, TabActivity.class));

    }

    public static void onActivityCreateSetTheme(Activity activity)

    {

        switch (cTheme){

            default:
            case ON:
                activity.setTheme(R.style.AppTheme);
                break;

            case OFF:
               activity.setTheme(R.style.AppTheme2);
                break;

        }

    }

}
