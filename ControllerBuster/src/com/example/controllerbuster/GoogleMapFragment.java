package com.example.controllerbuster;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapFragment extends Fragment{
	private GoogleMap googleMap;
	private UiSettings mUiSettings;
	private Context context;
	private View newMap;
	private double latitude;
	private double longitude;
	private final float mZoom = 10;

	public GoogleMapFragment(Context context, double latitude, double longitude) {
		this.context = context;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			newMap = inflater.inflate(R.layout.fragment_map, container,
					false);
		} catch (Exception e) {
			Log.d("D1", e.toString());
		}

		initilizeMap();
		setUpMap();

		return newMap;
	}

	private void setUpMap() {

		googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		googleMap.setMyLocationEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(latitude, longitude)).zoom(mZoom).build();

		googleMap.animateCamera(CameraUpdateFactory
		.newCameraPosition(cameraPosition));
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(latitude, longitude));
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		googleMap.addMarker(marker);
		
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(latitude + 0.5, longitude + 0.5)));
		
		mUiSettings = googleMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(true);
		mUiSettings.setCompassEnabled(true);
	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.googleMap)).getMap();
			if (googleMap == null) {
				Toast.makeText(this.context, "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
