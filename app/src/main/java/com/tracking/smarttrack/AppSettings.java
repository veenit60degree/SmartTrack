package com.tracking.smarttrack;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.tracking.constants.Constants;
import com.tracking.constants.themeUtils;
import com.tracking.smarttrack.fragment.AppSettingFragment;
import com.tracking.smarttrack.fragment.ProfileFragment;

public class AppSettings extends FragmentActivity {

    FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  themeUtils.onActivityCreateSetTheme(this);

        if(Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }

        setContentView(R.layout.activity_home);

        moveToOtherFragment();

    }

    /* ---------- MOVE TO AppSettingFragment ------------ */
    void moveToOtherFragment( ){

        try{
            AppSettingFragment appSettingFragment = new AppSettingFragment();
            appSettingFragment.setArguments(Constants.BUNDLE);
            fragManager = getSupportFragmentManager();
            FragmentTransaction fragmentTran = fragManager.beginTransaction();
            fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                    android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTran.replace( R.id.job_fragment, appSettingFragment);
            fragmentTran.addToBackStack("appSetting");
            fragmentTran.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        TabActivity.host.setCurrentTab(0);
    }
}



