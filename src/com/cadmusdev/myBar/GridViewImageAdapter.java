package com.cadmusdev.myBar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter {
    private Context mContext;

    public GridViewImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            
            
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        
        switch(position){

        case 0:
             imageView.setId(R.id.events);
             
             break;

        case 1:
            imageView.setId(R.id.specials);
           
            break;
            
        case 2:
            imageView.setId(R.id.bars);
            
            break;
            
        case 3:
            imageView.setId(R.id.friends);
            
            break;
            
        case 4:
            imageView.setId(R.id.about);
            
            break;
        }
        
        
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.events, R.drawable.specials,
            R.drawable.bars, R.drawable.friends,
            R.drawable.about
           
    };
    
}