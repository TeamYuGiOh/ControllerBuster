package com.example.controllerbuster;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentGoogleMap extends Fragment implements IAsyncResponse,
		IAlertDialogYesAction {
	private GoogleMap mGoogleMap;
	private UiSettings mUiSettings;
	private Context mContext;
	private AlertDialogMaker mAlertMaker;
	private View mNewMap;
	private double latitude;
	private double longitude;
	private final float mZoom = 10;
	private String URL_ALL_LOCATIONS;
	private List<Stop> stops = null;

	public FragmentGoogleMap(Context context, double latitude, double longitude) {
		this.mContext = context;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		URL_ALL_LOCATIONS = getResources()
				.getString(R.string.url_all_locations);
		getStopsFromServer();
		mAlertMaker = new AlertDialogMaker(this);
		mNewMap = null;
		try {
			mNewMap = inflater.inflate(R.layout.fragment_map, container, false);
		} catch (Exception e) {
			e.printStackTrace(); 
			Log.d("fuck this shit", e.getMessage() + " | " + e.getLocalizedMessage());
			mAlertMaker.makeConfirmAlert(null, "Sorry, but we could not load the map!", "I'll live");
		}

		initilizeMap();
		setUpMap();
		
		return mNewMap;
	}

	private void setUpMap() {

		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		mGoogleMap.setMyLocationEnabled(false);
		mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
		mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
		mGoogleMap.getUiSettings().setCompassEnabled(true);
		mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
		mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(mZoom).build();
		mGoogleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(latitude, longitude)).title("Me");
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		mGoogleMap.addMarker(marker);
		mUiSettings = mGoogleMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(true);
		mUiSettings.setCompassEnabled(true);
	}

	private void initilizeMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.googleMap)).getMap();
			if (mGoogleMap == null) {
				mAlertMaker.makeConfirmAlert(null, "Sorry, but we could not load the map!", "I'll live");
			}
		}
	}

	private void getStopsFromServer() {
		new HttpRequester(this).execute(URL_ALL_LOCATIONS, "GET");
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onAlertDialogYesPressed(DialogInterface dialog, int which) {
		dialog.dismiss();
	}

	@Override
	public void processFinish(String data) {
		stops = deserializeStopsFromJson(data);
		if (stops == null || stops.size() == 0) {
			mAlertMaker.makeConfirmAlert(null,
					"There are no controllers to display.", "Yey");
		} else {
			setMarkers(stops);
		}
	}

	private void setMarkers(List<Stop> stops) {
		for (int i = 0; i < stops.size(); i++) {
			double lat = stops.get(i).getLatitude();
			double lon = stops.get(i).getLongitude();
			String title = stops.get(i).getStopName();
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(lat, lon)).title(title);
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			mGoogleMap.addMarker(marker);
		}
	}

	private List<Stop> deserializeStopsFromJson(String data) {
		List<Stop> stops = new ArrayList<Stop>();
		if(data == null) {
			return stops;
		}
		try {
			JSONArray objArray = new JSONArray(//data);
					"[{\"latitude\":\"43.8000\",\"longitude\":\"22.000\",\"busttime\":\"2014-10-16 21:12:36\",\"busstop\":\"Zapaden park\"},{\"latitude\":\"43.8111\",\"longitude\":\"22.1126\",\"busttime\":\"2014-10-16 21:12:14\",\"busstop\":\"Lulin\"},{\"latitude\":\"43.8321\",\"longitude\":\"22.8326\",\"busttime\":\"2014-10-16 21:11:22\",\"busstop\":\"Lulin\"}]");
			for (int i = 0; i < objArray.length(); i++) {
				String stopName = objArray.getJSONObject(i)
						.getString("busstop");
				double lat = objArray.getJSONObject(i).getDouble("latitude");
				double lon = objArray.getJSONObject(i).getDouble("longitude");
				stops.add(new Stop(stopName, lat, lon));
			}

		} catch (JSONException e) {
			mAlertMaker
					.makeConfirmAlert(
							null,
							"Sorry, we can't show you where the controllers are right now :(",
							"Crap...");
		}

		return stops;
	}
}
