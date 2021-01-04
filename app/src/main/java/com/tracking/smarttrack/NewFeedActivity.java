package com.tracking.smarttrack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.tracking.constants.Constants;
import com.tracking.smarttrack.fragment.NewFeedFragment;

public class NewFeedActivity extends FragmentActivity {

    FragmentManager fragManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if(Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }

        setContentView(R.layout.activity_home);

        NewFeedFragment newFeedFragment = new NewFeedFragment();
        fragManager = getSupportFragmentManager();
        FragmentTransaction fragmentTran = fragManager.beginTransaction();
        fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTran.replace( R.id.job_fragment, newFeedFragment);
        fragmentTran.addToBackStack("newjob");
        fragmentTran.commit();

    }



    @Override
    protected void onResume() {
        super.onResume();
        Constants.ACTIVITY_STATUS = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExportActivity.SHOW_PROGRESS_BAR = true;
    }



}
