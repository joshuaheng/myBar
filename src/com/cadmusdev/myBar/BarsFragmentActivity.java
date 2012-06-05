package com.cadmusdev.myBar;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cadmusdev.myBar.database.BarsContentProvider;

public class BarsFragmentActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bars_activity);
		ContentProviderClient temp = getContentResolver()
				.acquireContentProviderClient(BarsContentProvider.CONTENT_URI);
		ContentProvider c=temp.getLocalContentProvider();
		((BarsContentProvider) c).clearBars();
		temp.release();
		

	}

}
