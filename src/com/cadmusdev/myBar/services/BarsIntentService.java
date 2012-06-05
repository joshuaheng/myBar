package com.cadmusdev.myBar.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.cadmusdev.myBar.BarsListFragment;
import com.cadmusdev.myBar.containers.Bar;
import com.cadmusdev.myBar.database.BarsContentProvider;
import com.cadmusdev.myBar.database.BarsOpenHelper;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class BarsIntentService extends IntentService {
	public BarsIntentService() {
		super("BarsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ParseQuery query = new ParseQuery("Bar");
		List<ParseObject> data = null;
		try {
			data = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ParseQuery> queries = convertToParseQueries(data);
		ParseQuery query2 = ParseQuery.or(queries);
		query2.orderByDescending("time");
		List<ParseObject> temp = null;
		try {
			temp = query2.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bar[] bars = convertToBars(temp, data);

		commitToDatabase(bars);
		// tell BarsListFragment that we're done.
		Intent broadcastIntent = new Intent();
		broadcastIntent
				.setAction(BarsListFragment.ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);
	}

	private List<ParseQuery> convertToParseQueries(List<ParseObject> list) {
		ArrayList<ParseQuery> result = new ArrayList<ParseQuery>();
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getString("name");
			ParseQuery query = new ParseQuery("CheckIn");
			query.whereEqualTo("name", name);
			long timeLimit = getCurrentTime() - 2000;
			Log.d("EDDY", "EDDY: " + timeLimit);
			query.whereGreaterThanOrEqualTo("time", timeLimit);
			result.add(query);
		}

		return result;
	}

	private Bar[] convertToBars(List<ParseObject> list,
			List<ParseObject> barList) {

		// Sort entries by bar into hashtable
		Hashtable<String, Long[]> bars = new Hashtable<String, Long[]>();
		for (int i = 0; i < barList.size(); i++) {
			ParseObject element = barList.get(i);
			String name = element.getString("name");
			Long[] emptyValue = { 0L, 0L};
			bars.put(name, emptyValue);
		}
		for (int i = 0; i < list.size(); i++) {
			ParseObject element = list.get(i);
			String name = element.getString("name");
			Long[] value = (Long[]) bars.get(name);
			if (value != null) {
				if (value[1]==0) {
					// assuming that the entries are organized by time. ie
					// latest entry first.
					Long[] newValue = { (long) element.getInt("length"), 1L,
							element.getLong("time") };
					bars.put(name, newValue);
				} else {
					value[0] += element.getInt("length");
					value[1]++;
				}
			}
		}

		// convert from hashtable to Bar[]
		Bar[] result = new Bar[bars.size()];
		Enumeration<String> keys = bars.keys();
		int i = 0;
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			Long[] value = bars.get(key);
			result[i] = new Bar();
			result[i].name = key;
			if(value.length==3){
			int average = (int) (value[0] /value[1]);
			result[i].length = average;
			result[i].time = value[2];
			} else{
				result[i].length=-1;
				result[i].time=0;
			}
			i++;
		}

		return result;
	}

	private void commitToDatabase(Bar[] bars) {
		for (int i = 0; i < bars.length; i++) {
			ContentValues mNewValues = new ContentValues();
			mNewValues.put(BarsOpenHelper.NAME, bars[i].name);
			mNewValues.put(BarsOpenHelper.LENGTH, bars[i].length);
			mNewValues.put(BarsOpenHelper.TIME, bars[i].time);
			Uri mNewUri = BarsContentProvider.CONTENT_URI;
			mNewUri = getContentResolver().insert(mNewUri, mNewValues);
		}
	}

	public static long getCurrentTime() {
		String result = "";
		Calendar c = Calendar.getInstance();
		result += c.get(Calendar.YEAR);
		result += String.format("%02d", c.get(Calendar.MONTH) + 1);
		result += String.format("%02d", c.get(Calendar.DATE));
		result += String.format("%02d", c.get(Calendar.HOUR_OF_DAY));
		result += String.format("%02d", c.get(Calendar.MINUTE));
		result += String.format("%02d", c.get(Calendar.SECOND));
		long l = Long.decode(result);
		return l;
	}
}