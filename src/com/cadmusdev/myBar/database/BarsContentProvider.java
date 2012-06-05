package com.cadmusdev.myBar.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BarsContentProvider extends ContentProvider {

	private BarsOpenHelper dbHelper;

	private static final String BASE_PATH = "bars";
	private static final String AUTHORITY = "com.cadmusdev.myBar.database";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final int BARS = 1;
	public static final int BAR_ID = 2;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/bar";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/bars";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, BARS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", BAR_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new BarsOpenHelper(getContext());
		return true;
	}

	public void clearBars(){
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		sqlDB.execSQL("DROP TABLE IF EXISTS "+BarsOpenHelper.TABLE_NAME);
		dbHelper.onCreate(sqlDB);
		sqlDB.close();
		getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		onCreate();
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case BARS:
			rowsAffected = sqlDB.delete(BarsOpenHelper.TABLE_NAME, selection,
					selectionArgs);
			break;
		case BAR_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(BarsOpenHelper.TABLE_NAME,
						BarsOpenHelper.ID + "=" + id, null);
			} else {
				rowsAffected = sqlDB.delete(BarsOpenHelper.TABLE_NAME,
						selection + " and " + BarsOpenHelper.ID + "=" + id,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		sqlDB.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case BAR_ID:
			return CONTENT_ITEM_TYPE;
		case BARS:
			return CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = sqlDB.insert(BarsOpenHelper.TABLE_NAME, null, values);
		sqlDB.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/#" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(BarsOpenHelper.TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case BAR_ID:
			queryBuilder.appendWhere(BarsOpenHelper.ID + "="
					+ uri.getLastPathSegment());
			break;
		case BARS:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case BARS:
			rowsAffected = sqlDB.update(BarsOpenHelper.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case BAR_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.update(BarsOpenHelper.TABLE_NAME, values,
						BarsOpenHelper.ID + "=" + id, null);
			} else {
				rowsAffected = sqlDB.update(BarsOpenHelper.TABLE_NAME, values,
						selection + " and " + BarsOpenHelper.ID + "=" + id,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		sqlDB.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}
}
