package com.cadmusdev.myBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class MyBarActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new GridViewImageAdapter(this));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	final ImageView button1 = (ImageView) findViewById(R.id.events);
                button1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on clicks
                        //Intent i = new Intent(MyBarActivity.this,Events.class);
                        //startActivity(i);
                    }
                });
                
            	final ImageView button2 = (ImageView) findViewById(R.id.specials);
                button2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on clicks
                        Intent i = new Intent(MyBarActivity.this,SpecialsListActivity.class);
                        startActivity(i);
                    }
                });
                
                final ImageView button3 = (ImageView) findViewById(R.id.bars);
                button3.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on clicks
                       Intent i = new Intent(MyBarActivity.this,BarsFragmentActivity.class);
                       startActivity(i);
                    }
                });
                
                final ImageView button4 = (ImageView) findViewById(R.id.friends);
                button4.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on clicks
                       //Intent i = new Intent(MyBarActivity.this,Friends.class);
                       //startActivity(i);
                    }
                });
                
                final ImageView button5 = (ImageView) findViewById(R.id.about);
                button5.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on clicks
                       Intent i = new Intent(MyBarActivity.this,About.class);
                       startActivity(i);
                    }
                });
            }
        });
        
    }
   
}