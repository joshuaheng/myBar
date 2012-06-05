package com.cadmusdev.myBar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BarsOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "bars.db";
	public static final String TABLE_NAME = "bars";
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String LENGTH = "length";
	public static final String TIME = "time";

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT, " + LENGTH + " INTEGER, " + TIME + " INTEGER, UNIQUE("
			+ NAME + ") ON CONFLICT REPLACE);";

	public BarsOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BarsOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
		onCreate(db);
	}
}