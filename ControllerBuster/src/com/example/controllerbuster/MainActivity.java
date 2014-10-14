package com.example.controllerbuster;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;

public class MainActivity extends Activity {
	private GoogleMap googleMap;
	private UiSettings mUiSettings;
	private final String locationResults = "GET_LOCATIONS";
	private final String postLocation = "POST_LOCATION";
	private String tempRequest = "INITIAL";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		tempRequest = locationResults;
		new HttpRequester().execute(
				"http://netmonster.telerik-students.org/mobile/location/",
				"GET");
		
		tempRequest = postLocation;
		 new HttpRequester().execute("http://netmonster.telerik-students.org/mobile/bust/",
		 "POST",
		 "latitude=27.45&longitude=42.34");
		showActionBar();
		checkConnectivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.controllers_map) {
			Toast.makeText(this, "controllers_map", Toast.LENGTH_LONG).show();
			return true;
		} else if (id == R.id.home) {
			// show the fragment with the big red button bust
		} else if (id == R.id.facebook_share) {
			Intent intent = new Intent(MainActivity.this,
					FacebookActivity.class);
			// intent.putExtra("RESULT", result);
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(true);
			builder.setTitle("No internet connection!");
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			AlertDialog alert = builder.create();
			alert.setMessage("The application will not work correctly with no internet.\nDo you want to exit?");
			alert.show();
		}
	}

	class HttpRequester extends AsyncTask<String, Void, String> {
		private final String USER_AGENT = "Mozilla/5.0";

		@Override
		protected String doInBackground(String... params) {
			try {
				String tag = "REQUESTS";
				String url = params[0];
				String requestMethod = params[1];
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj
						.openConnection();
				// optional default is GET
				con.setRequestMethod(requestMethod);

				// add request header
				con.setRequestProperty("User-Agent", USER_AGENT);

				if (requestMethod == "POST") {
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
					String urlParameters = params[2];
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(
							con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();
				}

				int responseCode = con.getResponseCode();
				Log.d(tag, "Sending");
				Log.d(tag, "Response code " + responseCode);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				Log.d(tag, response.toString());
				return response.toString();
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String data) {
			// dismissDialog(progress_bar_type);
			// Toast.makeText(getApplicationContext(), result,
			// Toast.LENGTH_LONG).show();
			if (tempRequest == locationResults) {
				onGetLocationsResult(data);
			} else if(tempRequest == postLocation) {
				onPostLocationResult(data);
			}
		}
	}


	private void onPostLocationResult(String data) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
	}
	
	private void onGetLocationsResult(String data) {
		// TODO Auto-generated method stub
		JSONObject obj;
		try {
			obj = new JSONObject(data);
			String latitude = obj.getString("latitude");
			Toast.makeText(getApplicationContext(), latitude,
					Toast.LENGTH_SHORT).show();
			// String pageName = obj.getJSONObject().getString("latitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
	}
}
