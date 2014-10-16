package com.example.controllerbuster;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class FragmentHome extends Fragment implements IAsyncResponse,
		IAlertDialogYesAction {
	private Context context;
	private String URL_POST_LOCATION;
	private StopDataSource datasource;
	private AlertDialogMaker mAlertMaker;
	private Stop mStopForFaves;
	private ImageButton mBustButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if(getFragmentManager().findFragmentById(R.id.fragment_place) != null) {
//		container.removeAllViews();
//		}
		//((ViewGroup) FragmentHome.getParentFragment()).removeAllViews();
		context = this.getActivity().getApplicationContext();
		datasource = new StopDataSource(context);
		mAlertMaker = new AlertDialogMaker(this);
		URL_POST_LOCATION = getResources().getString(
				R.string.url_post_locations);
		mStopForFaves = null;

		View v = inflater.inflate(R.layout.fragment_home, container, false);
		initializeComponents(v);
		return v;
	}

	private void initializeComponents(View v) {

	//	RelativeLayout gameBoard = (RelativeLayout) v.findViewById(R.id.homeLayout);
		final ImageButton bustButton = (ImageButton) v
				.findViewById(R.id.bustButton);
		// mBustButton.setImageResource(R.drawable.button);
//		  ImageButton btnGreen = new ImageButton(context);
//	        btnGreen.setImageResource(R.drawable.button);
//	      // btnGreen.setLayoutParams(lp);
//	       // btnGreen.setOnClickListener(mGreenBallOnClickListener);
//	        btnGreen.setBackgroundColor(Color.TRANSPARENT); 
//	      //  btnGreen.setTag(i);
//	        btnGreen.setId(1);
//
//	                    gameBoard.addView(btnGreen);
		bustButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBustButtonPressed(v);
			}
		});
	}

	private void onBustButtonPressed(View v) {
		// TODO get the location etc.
		// new HttpRequester(this).execute(
		// URL_POST_LOCATION, "POST",
		// "latitude=27.45&longitude=42.34");

		onPostSuccess("{\"latitude\":\"34.45\",\"longitude\":\"45.56\", \"stopName\":\"TestStop\"}"); // TODO
																											// for
																											// testing
																											// only
																											// remove
																											// it
																											// from
																											// here
																											// later
	}

	@Override
	public void processFinish(String data) {
		// here should be the after posting a location logic
		// TODO in the server make it return success, latitude, longitude,
		// stopName
		try {
			// this is how to get the json stuff from the server response
			JSONObject obj = new JSONObject(data);
			boolean success = obj.getBoolean("success");
			if (success) {
				onPostSuccess(data);
			}
			// String pageName = obj.getJSONObject().getString("latitude");
		} catch (JSONException e) {
			// display friendly error message to the user
			e.printStackTrace();
		}
	}

	private void onPostSuccess(String data) {
		// display to the user everything is ok
		// check if this stop is in his favourites
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
			datasource.open();
			datasource.createStop(stop);
			datasource.close();
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
		datasource.open();
		Stop stopFromDb = datasource.findStop(stop);
		datasource.close();
		if (stopFromDb == null) {
			isInFaves = false;
		}

		return isInFaves;
	}

	private Stop deserializeStopFromJson(String data) {
		Stop stop = null;
		try {
			JSONObject obj = new JSONObject(data);
			String stopName = obj.getString("stopName"); // TODO sync with
															// backend
			double latitude = obj.getDouble("latitude");
			double longitude = obj.getDouble("longitude");
			stop = new Stop(stopName, latitude, longitude);
		} catch (JSONException e) {
			// TODO add a friendly user message
			e.printStackTrace();
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
}
