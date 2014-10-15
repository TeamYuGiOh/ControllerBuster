package com.example.controllerbuster;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class FragmentHome extends Fragment implements IAsyncResponse
{
	private String URL_POST_LOCATION;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		initializeComponents(v);
		return v;
	}
	
	private void initializeComponents(View v) {
		final ImageButton bustButton = (ImageButton)v.findViewById(R.id.bustButton);
		URL_POST_LOCATION = getResources().getString(R.string.url_post_locations);
		bustButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText((MainActivity) getActivity(), "bust from frag",
						Toast.LENGTH_SHORT).show();
				onBustButtonPressed(v);
			}
		});
	}
	
	private void onBustButtonPressed(View v) {
		// TODO get the location etc.

		 new HttpRequester(this).execute(
		 URL_POST_LOCATION, "POST",
		 "latitude=27.45&longitude=42.34");
	}

	@Override
	public void processFinish(String data) {
		// here should be the after posting a location logic
		//TODO in the server make it return success, latitude, longitude, stopName
		try {
			// this is how to get the json stuff from the server response
			JSONObject obj = new JSONObject(data);
			boolean success = obj.getBoolean("success");
			if(success) {
				onPostSuccess(data);
			}
			// String pageName = obj.getJSONObject().getString("latitude");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void onPostSuccess(String data) {
		// display to the user everything is ok
		//check if this stop is in his favourites
		//if not ask if he wants it to be
	}
}
	
