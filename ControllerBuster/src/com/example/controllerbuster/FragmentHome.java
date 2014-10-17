package com.example.controllerbuster;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class FragmentHome extends Fragment implements IAsyncResponse,
		IAlertDialogYesAction, LocationListener {
	private Context mContext;
	private String URL_POST_LOCATION;
	private StopDataSource mDatasource;
	private AlertDialogMaker mAlertMaker;
	private LocationManager mLocationManager;
	private FindControllersAround mFindControllersAround;
	private Stop mStopForFaves;
	private double latitude = 0;
	private double longitude = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity().getApplicationContext();
		mDatasource = new StopDataSource(mContext);
		mAlertMaker = new AlertDialogMaker(this);
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		 getLocation();
		mFindControllersAround = new FindControllersAround(mContext, latitude, longitude);
		mFindControllersAround.findControllers();
		URL_POST_LOCATION = getResources().getString(
				R.string.url_post_locations);
		mStopForFaves = null;
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		initializeComponents(v);
		return v;
	}

	private void initializeComponents(View v) {

		final ImageButton bustButton = (ImageButton) v.findViewById(R.id.bustButton);
		bustButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBustButtonPressed(v);
			}
		});
	}

	private void onBustButtonPressed(View v) {
		 new HttpRequester(this).execute(
		 URL_POST_LOCATION, "POST",
		 "latitude=" + latitude + "&longitude=" + longitude);
	}
	
	private void getLocation() {
		LocationManager service = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = service
		  .isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
		  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		  startActivity(intent);
		} else {
		
		 Criteria criteria = new Criteria();
		    String provider = mLocationManager.getBestProvider(criteria, false);
		    Location location = mLocationManager.getLastKnownLocation(provider);

		    // Initialize the location fields
		    if (location != null) {
		      System.out.println("Provider " + provider + " has been selected.");
		      onLocationChanged(location);
		    } else {
		      mAlertMaker.makeConfirmAlert(null, "Your location is not available!", "I'll fix it");
		    }
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		 latitude  = location.getLatitude();
	    longitude = location.getLongitude();
	    Log.d("fuck this shit", "Lat " + String.valueOf(latitude));
	    Log.d("fuck this shit", "Lob " + String.valueOf(longitude));
	  }

	
	@Override
	public void processFinish(String data) {
		// here should be the after posting a location logic
		// TODO in the server make it return success, latitude, longitude,
		// stopName
		try {
			JSONObject obj = new JSONObject(data);
			boolean success = obj.getBoolean("success");
			if (success) {
				onPostSuccess(data);
			}
		} catch (JSONException e) {
			mAlertMaker.makeConfirmAlert(null, "Sorry, but there was a problem busting this controller.", "Lucky him");
		}
	}

	private void onPostSuccess(String data) {
		mStopForFaves = deserializeStopFromJson(data);
		boolean isInFaves = isInFavouriteStop(mStopForFaves);
		if (!isInFaves) {
			mAlertMaker
					.makeChoiceAlert(
							"Successful busting!",
							"This stop is not in your favourites.\nWould you like to add it?",
							"Yes", "No");
		} else {
			mAlertMaker.makeConfirmAlert(null,
					"You successfully busted this controller!", "OK");
		}
	}

	private void addStopToFavourites(Stop stop) {
		try {
			mDatasource.open();
			mDatasource.createStop(stop);
			mDatasource.close();
			mAlertMaker.makeConfirmAlert(null,
					"You successfully added stop '" + stop.getStopName() + "' to your favourites!",
					"OK");
		} catch (SQLException e) {
			mAlertMaker.makeConfirmAlert(null,
					"Sorry, but stop '" + stop.getStopName() + "' could not be added to your favourites.",
					"Not cool!");
		}
	}

	private boolean isInFavouriteStop(Stop stop) {
		boolean isInFaves = true;
		mDatasource.open();
		Stop stopFromDb = mDatasource.findStop(stop);
		mDatasource.close();
		if (stopFromDb == null) {
			isInFaves = false;
		}

		return isInFaves;
	}

	private Stop deserializeStopFromJson(String data) {
		Stop stop = null;
		try {
			JSONObject obj = new JSONObject(data);
			String stopName = obj.getString("busstop"); 
			double latitude = obj.getDouble("latitude");
			double longitude = obj.getDouble("longitude");
			stop = new Stop(stopName, latitude, longitude);
		} catch (JSONException e) {
			//Ignore
		}

		return stop;
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onAlertDialogYesPressed(DialogInterface dialog, int which) {
		addStopToFavourites(mStopForFaves);
	}

	 @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	    Toast.makeText(getActivity(), "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	    Toast.makeText(getActivity(), "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }
}
