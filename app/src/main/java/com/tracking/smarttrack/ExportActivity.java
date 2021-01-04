package com.tracking.smarttrack;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tracking.constants.Constants;
import com.tracking.constants.themeUtils;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.ExportDetailFragment;


public class ExportActivity extends FragmentActivity {


    public static FragmentManager fragManager;
    public static boolean SHOW_PROGRESS_BAR = true, IS_EXPORT_VIEW = true;
    public static Parcelable exportParcebaleState, importParcebaleState, unasignParcebaleState;
    public static TextView invisibleView;
    private long backpressTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   themeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

        if(Constants.isCapsTheme(this)){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme2);
        }

        setContentView(R.layout.activity_home);

        //invisibleView = (TextView)findViewById(R.id.invisibleView);


        ExportFragment detailFragment = new ExportFragment();
        fragManager = getSupportFragmentManager();
        FragmentTransaction fragmentTran = fragManager.beginTransaction();
        fragmentTran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTran.replace( R.id.job_fragment, detailFragment);
        fragmentTran.addToBackStack("job");
        fragmentTran.commit();




    }

    @Override
    public void onBackPressed() {
        Log.d("Export ACtivty", "onBackPressed"  );
        backPressed();

    }


    public void CLOSE_SLIDER(){
        if (TabActivity.smenu.isMenuShowing()) {
            TabActivity.smenu.toggle();
        } else {
            if(backpressTime + 2000 > System.currentTimeMillis()){
                finish();

            }else {
                Toast.makeText(getApplicationContext(), "Press back again to exit.",
                        Toast.LENGTH_LONG).show();
            }
            backpressTime = System.currentTimeMillis();

        }
    }


    private void backPressed(){

        try {
            if (!TabActivity.isHome) {
               /* if (ExportDetailFragment.searchActionBar != null) {
                    ExportDetailFragment.searchActionBar.performClick();
                } else {*/
                    if (TabActivity.smenu.isMenuShowing()) {
                        TabActivity.smenu.toggle();
                    } else {
                        if (ExportActivity.fragManager != null) {
                            if (ExportActivity.fragManager.getBackStackEntryCount() > 1) {
                                ExportActivity.fragManager.popBackStack();
                            } else {
                                finish();
                            }
                        } else {
                            TabActivity.host.setCurrentTab(0);
                        }
                    }
               // }
            } else {
                CLOSE_SLIDER();
            }
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        SHOW_PROGRESS_BAR = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.ACTIVITY_STATUS = 0;
    }
}
