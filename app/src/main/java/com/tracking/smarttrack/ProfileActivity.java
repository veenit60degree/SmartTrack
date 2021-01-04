package com.tracking.smarttrack;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.tracking.constants.Constants;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.NewFeedFragment;
import com.tracking.smarttrack.fragment.ProfileFragment;

public class ProfileActivity extends FragmentActivity {


    FragmentManager fragManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }

        setContentView(R.layout.activity_home);

        moveToOtherFragment();

    }



    @Override
    public void onBackPressed() {

        if(fragManager != null) {
            if (fragManager.getBackStackEntryCount() > 2) {
                fragManager.popBackStack();
            } else {
                TabActivity.host.setCurrentTab(0);
            }
        }else{
            TabActivity.host.setCurrentTab(0);
        }
    }



    /* ---------- MOVE TO ProfileFragment ------------ */
    void moveToOtherFragment( ){

        try{
            ProfileFragment profileFragment = new ProfileFragment();
            fragManager = getSupportFragmentManager();
            FragmentTransaction fragmentTran = fragManager.beginTransaction();
            fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                    android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTran.replace( R.id.job_fragment, profileFragment);
            fragmentTran.addToBackStack("profile");
            fragmentTran.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
