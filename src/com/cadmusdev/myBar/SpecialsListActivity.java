package com.cadmusdev.myBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class SpecialsListActivity extends FragmentActivity implements SpecialsListFragment.OnBarsSelectedListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialslist_fragment);
	}
	
	@Override
	public void onBarsSelected(Uri barUrl) {
		  SpecialsViewerFragment viewer = (SpecialsViewerFragment) getSupportFragmentManager()
	                .findFragmentById(R.id.specialsview_fragment);

	        if (viewer == null || !viewer.isInLayout()) {
	            Intent showContent = new Intent(getApplicationContext(),
	                    SpecialsViewerActivity.class);
	            showContent.setData(barUrl);
	            startActivity(showContent);
	        } else {
	            viewer.updateUrl(barUrl.toString());
	        }
	    }
	
	
	
}
