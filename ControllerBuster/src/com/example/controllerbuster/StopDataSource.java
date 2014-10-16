package com.example.controllerbuster;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StopDataSource {

	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_NAME, SQLiteHelper.COLUMN_LATITUDE,
			SQLiteHelper.COLUMN_LONGITUDE };

	public StopDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Stop createStop(Stop stop) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, stop.getStopName());
		values.put(SQLiteHelper.COLUMN_LATITUDE, stop.getLatitude());
		values.put(SQLiteHelper.COLUMN_LONGITUDE, stop.getLongitude());
		long insertId = database.insert(SQLiteHelper.TABLE_STOPS, null, values);
		Cursor cursor = database.query(SQLiteHelper.TABLE_STOPS, allColumns,
				SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Stop newStop = cursorToStop(cursor);
		cursor.close();
		return newStop;
	}

	public void deleteStop(Stop stop) {
		long id = stop.getId();
		System.out.println("Stop deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_STOPS, SQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Stop> getAllStops() {
		List<Stop> stops = new ArrayList<Stop>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_STOPS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Stop stop = cursorToStop(cursor);
			stops.add(stop);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return stops;
	}
	
	public Stop findStop(Stop stopToFind) {
		String stopName = stopToFind.getStopName();
		Cursor cursor = database.query(SQLiteHelper.TABLE_STOPS, allColumns,
				SQLiteHelper.COLUMN_NAME + "=?", new String[] {stopName}, null, null, null);
		Stop stop = null;
		if (cursor.moveToFirst()) {
		    stop = cursorToStop(cursor);
		}
		
		cursor.close();
		return stop;
	}

	private Stop cursorToStop(Cursor cursor) {
		Stop stop = new Stop();
		stop.setId(cursor.getLong(0));
		stop.setStopName(cursor.getString(1));
		stop.setLatitude(cursor.getDouble(2));
		stop.setLongitude(cursor.getDouble(3));
		return stop;
	}
}