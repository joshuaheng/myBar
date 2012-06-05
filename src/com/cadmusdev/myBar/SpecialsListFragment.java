package com.cadmusdev.myBar;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SpecialsListFragment extends ListFragment {
	OnBarsSelectedListener barsSelectedListener;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String[] links = getResources().getStringArray(R.array.bars_links);
		 
	    String content = links[position];
	    barsSelectedListener.onBarsSelected(Uri.parse(content));
	}
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setListAdapter(ArrayAdapter.createFromResource(getActivity()
	            .getApplicationContext(), R.array.bars_titles,
	            R.layout.list_items));
	}
	
	public interface OnBarsSelectedListener {
	    public void onBarsSelected(Uri barsUri);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			barsSelectedListener = (OnBarsSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnBarsSelectedListener");
		}
	}
}
