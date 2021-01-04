package com.tracking.smarttrack;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.tracking.constants.Constants;
import com.tracking.smarttrack.fragment.ExportDetailFragment;
import com.tracking.smarttrack.fragment.ExportFragment;


public class ImportActivity extends FragmentActivity {


    FragmentManager fragManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ExportFragment importFragment = new ExportFragment();
        fragManager = getSupportFragmentManager();
        FragmentTransaction fragmentTran = fragManager.beginTransaction();
        fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTran.replace( R.id.job_fragment, importFragment);
        fragmentTran.addToBackStack("import");
        fragmentTran.commit();


    }



    @Override
    protected void onResume() {
        super.onResume();
        Constants.ACTIVITY_STATUS = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExportActivity.SHOW_PROGRESS_BAR = true;
    }



    @Override
    public void onBackPressed() {
       //
        if (!TabActivity.isImport) {
            ExportDetailFragment.searchActionBar.performClick();
        } else {
            CLOSE_SLIDER();
        }
    }

   void CLOSE_SLIDER(){
        if (TabActivity.smenu.isMenuShowing()) {
            TabActivity.smenu.toggle();
        } else {
            finish();
        }
    }
}
