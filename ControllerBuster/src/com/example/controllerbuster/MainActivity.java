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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements IAlertDialogYesAction{
	private Context context = this;
	private GoogleMap googleMap;
	private UiSettings mUiSettings;
	private AlertDialogMaker alertMaker;
	private Fragment cachedMap = null;
	private String URL_ALL_LOCATIONS;
	private String URL_POST_LOCATION;

	protected void onCreate(Bundle savedInstanceState) {
		//TODO make private variables with m
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertMaker = new AlertDialogMaker(this);
		URL_ALL_LOCATIONS = getResources()
				.getString(R.string.url_all_locations);
		URL_POST_LOCATION = getResources().getString(
				R.string.url_post_locations);

		showActionBar();
		checkConnectivity(this);

		NotificationMaker notificationMaker = new NotificationMaker(context);
		notificationMaker.MakeNotification("My notification test",
				"test test test");
	}
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		getFragmentManager().putFragment(outState, "cashed_map", cachedMap);
//	}
//
//	@Override
//	protected void onRestoreInstanceState(Bundle inState) {
//		cachedMap = getFragmentManager().getFragment(inState, "cashed_map");
//	}
////	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		initilizeMap();
//		setUpMap();
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//TODO add fragments to backstack
		Fragment fragment;
		int id = item.getItemId();
		switch (id) {
		case R.id.home:
			fragment = new FragmentHome();
			changeFragments(fragment);
			return true;
		case R.id.controllers_map:
			// make a class for the map and in there implement the IAsync..
			//fragment = new GoogleMapFragment(context,42.69, 23.32); 
			//changeFragments(fragment);
			if (cachedMap == null) {
				cachedMap = new GoogleMapFragment(MainActivity.this, 42.69, 23.32);
			}
			fragment = cachedMap;
			changeFragments(fragment);
			return true;
		case R.id.favourite_stops:
			// go to the sqlite databse etc.
			fragment = new FragmentFavourites();
			changeFragments(fragment);
			return true;
		case R.id.facebook_share:
			FacebookPosting facebookPosting = new FacebookPosting(context);
			facebookPosting.postToWall();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
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
	
	private void initilizeMap() {
		if (googleMap == null) {
			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void setUpMap() {

        googleMap.setMyLocationEnabled(true);

        mUiSettings = googleMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setCompassEnabled(true);
    }
	
	public void checkConnectivity(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		if (!isConnected) {
			alertMaker
					.makeChoiceAlert(
							"No internet connection!",
							"The application will not work correctly with no internet.\nDo you want to exit?",
							"Yes",
							"No");
		}
	}

	@Override
	public Context getContext() {
		return this.context;
	}

	@Override
	public void onAlertDialogYesPressed(DialogInterface dialog, int which) {
		dialog.dismiss();
		this.finish();
	}
}
