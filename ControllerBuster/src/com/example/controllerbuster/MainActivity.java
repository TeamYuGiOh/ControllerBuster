package com.example.controllerbuster;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;

public class MainActivity extends Activity{
	private Context context = this;
	private GoogleMap googleMap;
	private UiSettings mUiSettings;
	private AlertDialogMaker alertMaker;
	private String URL_ALL_LOCATIONS;
	private String URL_POST_LOCATION;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertMaker = new AlertDialogMaker(context);
		URL_ALL_LOCATIONS = getResources()
				.getString(R.string.url_all_locations);
		URL_POST_LOCATION = getResources().getString(
				R.string.url_post_locations);

		// get imei
		// android.telephony.TelephonyManager.getDeviceId()

		// FragmentManager fragmentManager = getFragmentManager();
		// MapFragment mapFragment = (MapFragment)
		// fragmentManager.findFragmentById(R.id.map);
		// googleMap = mapFragment.getMap();
		//
		// LatLng sfLatLng = new LatLng(37.7750, -122.4183);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// googleMap.addMarker(new MarkerOptions()
		// .position(sfLatLng)
		// .title("San Francisco")
		// .snippet("Population: 776733")
		// .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		//
		// LatLng sLatLng = new LatLng(37.857236, -122.486916);
		// googleMap.addMarker(new MarkerOptions()
		// .position(sLatLng)
		// .title("Sausalito")
		// .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
		//
		//
		// googleMap.getUiSettings().setCompassEnabled(true);
		// googleMap.getUiSettings().setZoomControlsEnabled(true);
		// googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		//
		//
		// LatLng cameraLatLng = sfLatLng;
		// float cameraZoom = 10;
		//
		// if(savedInstanceState != null){
		// savedInstanceState.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
		//
		// double savedLat = savedInstanceState.getDouble("lat");
		// double savedLng = savedInstanceState.getDouble("lng");
		// cameraLatLng = new LatLng(savedLat, savedLng);
		//
		// cameraZoom = savedInstanceState.getFloat("zoom", 10);
		// }
		//
		// googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng,
		// cameraZoom));
		// tempRequest = locationResults;
		// Toast.makeText(context, URL_ALL_LOCATIONS, Toast.LENGTH_LONG).show();
		// new HttpRequester(this).execute(
		// URL_ALL_LOCATIONS,
		// "GET");
		//
		
		// new HttpRequester(this).execute(
		// URL_POST_LOCATION, "POST",
		// "latitude=27.45&longitude=42.34");
		showActionBar();
		checkConnectivity(this);

		NotificationMaker notificationMaker = new NotificationMaker(context);
		notificationMaker.MakeNotification("My notification test",
				"test test test");

		// Test test = new Test(this);
		// test.test();
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
			// make a class for the map and in there implement the IAsync..
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

	private void changeFragments(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fragment);			
		fragmentTransaction.commit();
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// initilizeMap();
	// setUpMap();
	// }
	//
	// /**
	// * function to load map If map is not created it will create it for you
	// * */
	// private void initilizeMap() {
	// if (googleMap == null) {
	// //googleMap = ((MapFragment)
	// getFragmentManager().findFragmentById(R.id.map)).getMap();
	// googleMap = ((MapFragment)
	// getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
	// // check if map is created successfully or not
	// if (googleMap == null) {
	// Toast.makeText(getApplicationContext(),
	// "Sorry! unable to create maps", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// // if (googleMap == null) {
	// // // Try to obtain the map from the SupportMapFragment.
	// // googleMap = ((SupportMapFragment)
	// getSupportFragmentManager().findFragmentById(R.id.googleMap))
	// // .getMap();
	// // // Check if we were successful in obtaining the map.
	// // if (googleMap != null) {
	// // setUpMap();
	// // }
	// // }
	// }
	//
	// private void setUpMap() {
	//
	// googleMap.setMyLocationEnabled(true);
	//
	// mUiSettings = googleMap.getUiSettings();
	//
	// mUiSettings.setZoomControlsEnabled(true);
	// mUiSettings.setMyLocationButtonEnabled(true);
	// mUiSettings.setCompassEnabled(true);
	// }

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
			alertMaker
					.MakeAlert(
							"No internet connection!",
							"The application will not work correctly with no internet.\nDo you want to exit?");
		}
	}
}
