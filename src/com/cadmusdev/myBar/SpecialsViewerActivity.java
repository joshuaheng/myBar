package com.cadmusdev.myBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class SpecialsViewerActivity extends FragmentActivity implements SpecialsListFragment.OnBarsSelectedListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.specialsview_fragment);
	    //if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //    finish();
        //    return;
        //}

	    Intent launchingIntent = getIntent();
	    String content = launchingIntent.getData().toString();
	 
	    SpecialsViewerFragment viewer = (SpecialsViewerFragment) getSupportFragmentManager()
	            .findFragmentById(R.id.specialsview_fragment);
	 
	    viewer.updateUrl(content);
    }
	

	public void onBarsSelected(Uri tutUri) {
	    Intent showContent = new Intent(getApplicationContext(),
	            SpecialsViewerActivity.class);
	    showContent.setData(tutUri);
	    startActivity(showContent);
	}
}

