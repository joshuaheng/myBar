//Haven't added time
/*
package com.cadmusdev.myBar.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cadmusdev.myBar.containers.Bar;

public class BarsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private BarsOpenHelper dbHelper;
	private String[] allColumns = {"name","length"};

	public BarsDataSource(Context context) {
		dbHelper = new BarsOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createBar(Bar bar) {
		ContentValues values = new ContentValues();
		values.put("bar",bar.name);
		values.put("length",bar.length);
		long insertId = database.insert(BarsOpenHelper.TABLE_NAME, null,
				values);
		return insertId;
	}

	public void deleteBar(String name) {
		database.delete(BarsOpenHelper.TABLE_NAME, "name = '"+name+"'", null);
	}

	public ArrayList<Bar> getAllBars() {
		ArrayList<Bar> bars = new ArrayList<Bar>();
		Cursor cursor = database.query(BarsOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Bar b = cursorToBar(cursor);
			bars.add(b);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return bars;
	}

	private Bar cursorToBar(Cursor cursor) {
		Bar result = new Bar();
		result.name=cursor.getString(0);
		result.length=cursor.getInt(1);
		return result;

	}

}*/
