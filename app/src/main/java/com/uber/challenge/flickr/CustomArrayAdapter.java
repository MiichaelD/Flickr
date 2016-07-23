package com.uber.challenge.flickr;

import com.squareup.picasso.Picasso;
import com.uber.challenge.flickr.utils.BitmapLoader;
import com.uber.challenge.flickr.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.lang.annotation.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        Picasso.with(ctx).setIndicatorsEnabled(true);
	}
	
	public CustomArrayAdapter(Context context, int resource, String[] arr) {
		super(context, resource, arr);
		ctx = context;
		res_id = resource;
		data = arr;
        Picasso.with(ctx).setIndicatorsEnabled(true);
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
		@BindView(R.id.iv_1) ImageView img;
		int position = -1;

		ViewHolder (View view){
            ButterKnife.bind(this, view);
		}

		void loadImage(int position){
			if (position != this.position){
				this.position = position;
                int size = 150;
                Picasso.with(ctx).load(getItem(position))
//                        .resize(size,size).centerCrop()
                        .placeholder(R.drawable.loading).into(img);
			}
		}

	}

}
