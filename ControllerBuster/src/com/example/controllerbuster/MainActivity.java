package com.example.controllerbuster;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class MainActivity extends Activity implements IAlertDialogYesAction{
	private Context mContext = this;
	private AlertDialogMaker mAlertMaker;
	private Fragment mNewMap = null;

	protected void onCreate(Bundle savedInstanceState) {
		//TODO make private variables with m
		super.onCreate(savedInstanceState);
		mAlertMaker = new AlertDialogMaker(this);
		showActionBar();
		checkConnectivity(this);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment;
		int id = item.getItemId();
		switch (id) {
		case R.id.home:
			fragment = new FragmentHome();
			changeFragments(fragment);
			return true;
		case R.id.controllers_map:
			if (mNewMap == null) {
				mNewMap = new FragmentGoogleMap(MainActivity.this, 42.71, 23.25);
			}
			fragment = mNewMap;
			changeFragments(fragment);
			return true;
		case R.id.favourite_stops:
			fragment = new FragmentFavourites();
			changeFragments(fragment);
			return true;
		case R.id.facebook_share:
			FacebookPosting facebookPosting = new FacebookPosting(mContext);
			facebookPosting.postToWall();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void changeFragments(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fragment);	
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	private void showActionBar() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

		ActionBar bar = this.getActionBar();
		bar.show();
		bar.setDisplayShowHomeEnabled(true);
	}
	
	public void checkConnectivity(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		if (!isConnected) {
			mAlertMaker
					.makeChoiceAlert(
							"No internet connection!",
							"The application will not work correctly with no internet.\nDo you want to exit?",
							"Yes",
							"No");
		}
	}

	@Override
	public Context getContext() {
		return this.mContext;
	}

	@Override
	public void onAlertDialogYesPressed(DialogInterface dialog, int which) {
		dialog.dismiss();
		this.finish();
	}
}
