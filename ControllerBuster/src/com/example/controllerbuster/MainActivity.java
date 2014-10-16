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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;

public class MainActivity extends Activity implements IAlertDialogYesAction{
	private Context context = this;
	private GoogleMap googleMap;
	private UiSettings mUiSettings;
	private AlertDialogMaker alertMaker;
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
	
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
		setUpMap();
	}

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
			//fragment = new MapFragment(); 
			//changeFragments(fragment);
			try {
				// Loading map
				initilizeMap();

				// Changing map type
//				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//				 googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				 googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//				 googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

				// Showing / hiding your current location
				googleMap.setMyLocationEnabled(true);

				// Enable / Disable zooming controls
				googleMap.getUiSettings().setZoomControlsEnabled(false);

				// Enable / Disable my location button
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				// Enable / Disable Compass icon
				googleMap.getUiSettings().setCompassEnabled(true);

				// Enable / Disable Rotate gesture
				googleMap.getUiSettings().setRotateGesturesEnabled(true);

				// Enable / Disable zooming functionality
				googleMap.getUiSettings().setZoomGesturesEnabled(true);

				double latitude = 42.4953;
				double longitude = 27.4717;

				// lets place some 10 random markers
				for (int i = 0; i < 10; i++) {
					// random latitude and logitude
					double[] randomLocation = createRandLocation(latitude,
							longitude);

					// Adding a marker
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(randomLocation[0], randomLocation[1]))
							.title("Hello Maps " + i);

					Log.e("Random", "> " + randomLocation[0] + ", "
							+ randomLocation[1]);

					// changing marker color
					if (i == 0)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
					if (i == 1)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					if (i == 2)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
					if (i == 3)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					if (i == 4)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
					if (i == 5)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
					if (i == 6)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					if (i == 7)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
					if (i == 8)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
					if (i == 9)
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

					googleMap.addMarker(marker);

					// Move the camera to last position with a zoom level
					if (i == 9) {
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(randomLocation[0],
										randomLocation[1])).zoom(15).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
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
