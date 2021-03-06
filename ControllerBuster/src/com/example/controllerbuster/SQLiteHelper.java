package com.example.controllerbuster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_STOPS = "stops";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_LATITUDE = "latitude";
  public static final String COLUMN_LONGITUDE = "longitude";

  private static final String DATABASE_NAME = "todos.db";
  private static final int DATABASE_VERSION = 1;

  private static final String DATABASE_CREATE = "create table "
      + TABLE_STOPS + "(" + COLUMN_ID + " integer primary key autoincrement, " +
		  COLUMN_NAME + " text not null, " +
		  COLUMN_LATITUDE + " real, " +
		  COLUMN_LONGITUDE + " real" +
      ");";

  public SQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOPS);
    onCreate(db);
  }

} 
