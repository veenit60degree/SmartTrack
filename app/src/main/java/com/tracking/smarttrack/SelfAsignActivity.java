package com.tracking.smarttrack;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tracking.constants.Constants;
import com.tracking.smarttrack.fragment.ExportDetailFragment;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.SelfAssignFragment;

public class SelfAsignActivity extends FragmentActivity {


    public static FragmentManager selfFragManager;


    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }
        setContentView(R.layout.activity_home);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.ACTIVITY_STATUS = 2;
       // TabActivity.smenu.toggle();
        if(TabActivity.isActivityCalled) {
            selfFragManager = getSupportFragmentManager();
            try{
                if (selfFragManager.getBackStackEntryCount() > 1) {
                    selfFragManager.popBackStack();
                }
            }catch (Exception e){

            }


            moveToOtherFragment(TabActivity.jobType, TabActivity.contNo);
        }

        TabActivity.isActivityCalled = false;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExportActivity.SHOW_PROGRESS_BAR = true;
    }



    @Override
    public void onBackPressed() {

        if(selfFragManager != null) {
            if (selfFragManager.getBackStackEntryCount() > 1) {
                selfFragManager.popBackStack();
            } else {
                TabActivity.host.setCurrentTab(0);
            }
        }else{
            TabActivity.host.setCurrentTab(0);
        }
    }


    /* ---------- MOVE TO ExportFRAGMENT ------------ */
    void moveToOtherFragment(final String jobType, final String contNo ){

        try{
                     SelfAssignFragment selfAsignFragment = new SelfAssignFragment();

                    Constants.BUNDLE.putBoolean("IS_SELF_ASIGN", true );
                    Constants.BUNDLE.putString("jobType", jobType );
                    Constants.BUNDLE.putString("contNo", contNo );

                     selfAsignFragment.setArguments(Constants.BUNDLE);
                    selfFragManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTran = selfFragManager.beginTransaction();
                    fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out);
                    fragmentTran.replace( R.id.job_fragment, selfAsignFragment);
                    fragmentTran.addToBackStack("sign");
                    fragmentTran.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

