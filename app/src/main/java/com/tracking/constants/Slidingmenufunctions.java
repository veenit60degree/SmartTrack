package com.tracking.constants;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tracking.smarttrack.ExportActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.TabActivity;
import com.tracking.smarttrack.fragment.ExportFragment;
import com.tracking.smarttrack.fragment.OTPFragment;
import com.tracking.smarttrack.fragment.ProfileFragment;


public class Slidingmenufunctions implements OnClickListener {

	SlidingMenu menu;
	Context c;
	public static TextView usernameTV, homeTxtView;
	TextView appVersionTV;
	RelativeLayout myProfileLay;
	RelativeLayout  logoutLay,  homeLay, selfAssignLay, settingsLay;
	ImageView slideOffBtn;
	ProgressDialog dialog;


	public Slidingmenufunctions() {
		super();
	}

	public Slidingmenufunctions(SlidingMenu menu, Context c) {

		if(Constants.isCapsTheme(c)){
			c.setTheme(R.style.AppTheme);
		}else{
			c.setTheme(R.style.AppTheme2);
		}

		this.menu = menu;
		this.c = c;

		dialog			 = new ProgressDialog(c);
		homeTxtView		 = (TextView)       menu.findViewById(R.id.homeTxtView);
		usernameTV 		 = (TextView)       menu.findViewById(R.id.usernameTV);

		myProfileLay	 = (RelativeLayout) menu.findViewById(R.id.myProfileLay);
		homeLay 		 = (RelativeLayout) menu.findViewById(R.id.homeLay);
		selfAssignLay	 = (RelativeLayout) menu.findViewById(R.id.selfAssignLay);
		settingsLay		 = (RelativeLayout) menu.findViewById(R.id.settingsLay);
		logoutLay 		 = (RelativeLayout) menu.findViewById(R.id.logoutLay);

		slideOffBtn		 = (ImageView) menu.findViewById(R.id.slideOffBtn);
		appVersionTV     = (TextView)menu.findViewById(R.id.appVersionTV);

		dialog.setMessage("Loading..");
		appVersionTV.setText( "Version: " + Constants.GetAppVersion( c, "VersionName"));

		homeLay.setOnClickListener(this);
		selfAssignLay.setOnClickListener(this);
		settingsLay.setOnClickListener(this);
		logoutLay.setOnClickListener(this);
		slideOffBtn.setOnClickListener(this);
		myProfileLay.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		
		
		menu.showContent();

		switch (v.getId()) {


			case R.id.homeLay:

				if(TabActivity.host.getCurrentTab() == 0 ){
					TabActivity.host.setCurrentTab(2);
				}
				Constants.CONTAINER_LIST_TYPE = String.valueOf(Constants.EXPORT);
				ExportFragment.IS_SELF_ASIGN = false;
				ExportActivity.IS_EXPORT_VIEW = false;
				TabActivity.host.setCurrentTab(0);
				break;

			case R.id.myProfileLay:
				TabActivity.host.setCurrentTab(3);
				break;


			case R.id.selfAssignLay:
				TabActivity.selfAssignBtn.performClick();
				break;


			case R.id.settingsLay:
				TabActivity.host.setCurrentTab(4);
				break;



			case R.id.slideOffBtn:
				menu.showMenu();
				break;

		case R.id.logoutLay:

			TabActivity.logoutBtn.performClick();
			break;


		default:
			break;
		}

	}
}
