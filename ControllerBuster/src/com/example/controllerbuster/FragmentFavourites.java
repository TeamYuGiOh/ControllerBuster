package com.example.controllerbuster;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class FragmentFavourites extends ListFragment implements
		IAlertDialogYesAction {
	private StopDataSource mDatasource;
	private Context mContext;
	private StopAdapter mAdapter;
	private AlertDialogMaker mAlertMaker;
	private Fragment mNewMap = null;
	private int[] imgResourses;
	private Stop stopToDelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		container.removeAllViews();
		mContext = this.getActivity().getApplicationContext();
		mAlertMaker = new AlertDialogMaker(this);
		mDatasource = new StopDataSource(mContext);
		mDatasource.open();

		imgResourses = new int[] { R.drawable.bus_stop };
		List<Stop> stops = mDatasource.getAllStops();
		mDatasource.close();
		if (stops == null || stops.size() == 0) {
			stops = new ArrayList<Stop>();
			stops.add(new Stop("You have no favourite stops yet.", 0, 0));
		}

		mAdapter = new StopAdapter(inflater.getContext(),
				R.layout.fragment_favourites, stops, imgResourses);
		setListAdapter(mAdapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mAlertMaker.makeChoiceAlert("Delete stop",
						"Do you want to delete this stop from your favourites?", "Yes",
						"No");
				mAdapter = (StopAdapter) getListAdapter();
				stopToDelete = (Stop) mAdapter.getItem(arg2);
				return true;
			}
		});
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onAlertDialogYesPressed(DialogInterface dialog, int which) {
		try {
		mDatasource.open();
		mDatasource.deleteStop(stopToDelete);
		mDatasource.close();
		mAdapter.remove(stopToDelete);
		mAdapter.notifyDataSetChanged();
		}
		catch(SQLException e) {
			mAlertMaker.makeConfirmAlert(null, "Sorry, but we could no delete the stop.", "Oh well");
		}
	}
}
