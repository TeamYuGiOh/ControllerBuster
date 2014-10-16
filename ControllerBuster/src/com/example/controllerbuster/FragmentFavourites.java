package com.example.controllerbuster;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentFavourites extends ListFragment {
	private StopDataSource datasource;
	private Context context;
	private int[] imgResourses;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = this.getActivity().getApplicationContext();
		datasource = new StopDataSource(context);
		datasource.open();

		imgResourses = new int[] { R.drawable.bus_stop };// R.drawable.bus_stop};
		List<Stop> stops = datasource.getAllStops();// new
													// ArrayList<Stop>();//datasource.getAllStops();
		datasource.close();
		// Stop testStop = new Stop();
		// testStop.setStopName("bla");
		// stops.add(testStop);
		// stops.add(testStop);
		// stops.add(testStop);
		if (stops == null || stops.size() == 0) {
			stops = new ArrayList<Stop>();
			stops.add(new Stop("You have no favourite stops yet.", 0, 0));
		}
		
		StopAdapter adapter = new StopAdapter(inflater.getContext(),
				R.layout.fragment_favourites, stops, imgResourses);
		/** Setting the list adapter for the ListFragment */
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
