package com.uber.challenge.flickr;

import com.uber.challenge.flickr.utils.BitmapLoader;
import com.uber.challenge.flickr.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/** Custom ArrayAdapter to display views more complicated than just
 * a TextView; In this case, 1 ImageView.
 * 
 * More info:
 * https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView */
public class CustomArrayAdapter extends ArrayAdapter<String>{

	int res_id;
	Context ctx;
	String[] data;
	
	public CustomArrayAdapter(Context context, int resource) {
		super(context, resource);
		ctx = context;
		res_id = resource;
	}
	
	public CustomArrayAdapter(Context context, int resource, String[] arr) {
		super(context, resource, arr);
		ctx = context;
		res_id = resource;
		data = arr;
	}
	
	public void add(String[] args){
		if(args == null) return;
		for(String val:args){
			if(val != null && !val.isEmpty())
				super.add(val);
		}
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		// In this example ViewHold pattern is not really needed since the view we 
		// are receiving is actually just an ImageView and not a viewgroup (no need to findViewById())
		
		ViewHolder viewHolder = null;
		
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
    	   Log.d(CustomArrayAdapter.class.getSimpleName(),"Creating view "+position);
          convertView = LayoutInflater.from(getContext()).inflate(res_id, parent, false);
          viewHolder = new ViewHolder(convertView);
          convertView.setTag(viewHolder);
       } else {
    	   //if it was already reused 
           viewHolder = (ViewHolder) convertView.getTag();
    	   Log.d(CustomArrayAdapter.class.getSimpleName(),"Reusing ViewHolder "+viewHolder.position+" for view: "+position);
       }
       // We change to a loading image bitmap, so it doesn't look like repeated images
       viewHolder.loadImage(position);
       
       // Return the completed view to render on screen
       return convertView;
   }
	
	/** This class is used to improve performance, we should modify the custom adapter
	 * by applying the ViewHolder pattern which speeds up the population of the
	 * ListView considerably by caching view lookups for smoother, faster loading:*/
	class ViewHolder{
		ImageView img;
		int position;
		ViewHolder (View v){
			img = (ImageView) v;
		}
		void loadImage(int position){
			if (position != this.position){
				this.position = position;
				// set an empty image while we load the new ones
				img.setImageBitmap(BitmapLoader.getImage(ctx, R.drawable.loading, true));
				// Populate the data into the template view using the data object       
		       ImageDownloaderTask idt = new ImageDownloaderTask(ctx, this, position);
		       idt.execute(getItem(position));
			}
		}
		
		
	}

}
