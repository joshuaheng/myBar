package com.cadmusdev.myBar;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this,"rjCNOsrZ4QqOqLLgJEPmaduiHPg1xKBKc0eUXpsR", "TzV5S4dQTYZydQdyn3g60KlGr9KF5ys5iTLPZ3gO");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access by default.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
