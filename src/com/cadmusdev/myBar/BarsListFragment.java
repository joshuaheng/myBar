package com.cadmusdev.myBar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cadmusdev.myBar.database.BarsContentProvider;
import com.cadmusdev.myBar.database.BarsOpenHelper;
import com.cadmusdev.myBar.services.BarsIntentService;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class BarsListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;
	private ResponseReceiver receiver;
	private boolean receiverRegistered;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		/*
		 * ParseObject murphys = new ParseObject("Bar");
		 * murphys.put("name","Murphys");
		 * 
		 * ParseObject legends = new ParseObject("Bar");
		 * legends.put("name","Legends");
		 * 
		 * ParseObject checkIn1 = new ParseObject("CheckIn");
		 * checkIn1.put("name", "Murphys"); checkIn1.put("length", 20);
		 * checkIn1.put("time", 20120528225230L);
		 * 
		 * ParseObject checkIn2 = new ParseObject("CheckIn");
		 * checkIn2.put("name", "Murphys"); checkIn2.put("length", 25);
		 * checkIn2.put("time", 20120528225231L);
		 * 
		 * ParseObject checkIn3 = new ParseObject("CheckIn");
		 * checkIn3.put("name", "Legends"); checkIn3.put("length", 30);
		 * checkIn3.put("time", 20120528225240L);
		 * 
		 * ParseObject checkIn4 = new ParseObject("CheckIn");
		 * checkIn4.put("name", "Legends"); checkIn4.put("length", 35);
		 * checkIn4.put("time", 20120528225241L);
		 * 
		 * try { murphys.save(); legends.save(); checkIn1.save();
		 * checkIn2.save(); checkIn3.save(); checkIn4.save(); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
		getActivity().registerReceiver(receiver, filter);
		receiverRegistered = true;
		Log.d("EDDY", "Registering receiver...");

		fillData();

		Intent msgIntent = new Intent(getActivity(), BarsIntentService.class);
		getActivity().startService(msgIntent);
		Log.d("EDDY", "Starting intent...");
	}

	@Override
	public void onStart() {
		super.onStart();
		if (receiverRegistered)
			setListShown(false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Context mContext = getActivity();

		// set custom_dialog
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(BarsFragmentActivity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog,
				(ViewGroup) getActivity().findViewById(R.id.layout_root));

		builder = new AlertDialog.Builder(mContext);
		builder.setView(layout);

		// create alert dialog
		final AlertDialog alert = builder.create();

		// set text
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("How many people do you see in the line?");

		// set edit text
		Cursor c = (Cursor) getListAdapter().getItem(position);
		final String name = c.getString(c.getColumnIndex("name"));
		final EditText edittext = (EditText) layout.findViewById(R.id.edittext);
		edittext.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					String s = edittext.getText().toString();
					int length = Integer.decode(s);
					checkIn(name, length);
					alert.dismiss();
					return true;
				}
				return false;
			}
		});

		// set "Check-in" button
		Button button1 = (Button) layout.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String s = edittext.getText().toString();
				int length = Integer.decode(s);
				checkIn(name, length);
				alert.dismiss();
			}
		});

		// show it
		alert.show();

	}

	private void checkIn(String bar, int length) {
		ParseObject checkin = new ParseObject("CheckIn");
		checkin.put("length", length);
		checkin.put("name", bar);
		long time = BarsIntentService.getCurrentTime();
		checkin.put("time", time);

		checkin.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					//TODO:Bug. if user leaves activity before toast is shown, getActivity() would no longer exist?
					Toast.makeText(getActivity(), "Check-in successful",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Could not check-in",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiverRegistered) {
			getActivity().unregisterReceiver(receiver);
			receiverRegistered = false;
		}
	}

	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { BarsOpenHelper.NAME,
				BarsOpenHelper.LENGTH };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label, R.id.number };
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_mobile,
				null, from, to, 0);

		setListAdapter(mAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = BarsContentProvider.CONTENT_URI;
		return new CursorLoader(getActivity(), baseUri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);

	}

	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.cadmusdev.myBar.BarsIntentService.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context context, Intent intent) {
			getLoaderManager().initLoader(0, null, BarsListFragment.this);
			BarsListFragment.this.getActivity().unregisterReceiver(this);
			BarsListFragment.this.receiverRegistered = false;
			Log.d("EDDY", "MESSAGE RECEIVED");
		}
	}

}
