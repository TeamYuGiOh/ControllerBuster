package com.example.controllerbuster;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragmentHome extends Fragment implements IAsyncResponse,
		IAlertDialogYesAction {
	private Context mContext;
	private String URL_POST_LOCATION;
	private StopDataSource mDatasource;
	private AlertDialogMaker mAlertMaker;
	private Stop mStopForFaves;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity().getApplicationContext();
		mDatasource = new StopDataSource(mContext);
		mAlertMaker = new AlertDialogMaker(this);
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
		// TODO get the location etc.
		// new HttpRequester(this).execute(
		// URL_POST_LOCATION, "POST",
		// "latitude=27.45&longitude=42.34");

		onPostSuccess("{\"latitude\":\"34.45\",\"longitude\":\"45.56\", \"busstop\":\"TestStopAgain\"}"); // TODO
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
}
