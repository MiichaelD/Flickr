package com.uber.challenge.flickr;

import com.uber.challenge.flickr.utils.BitmapLoader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.AbsListView.LayoutParams;


/** 
 * Download the image and if we have a given ViewHolder, set its Imageview to 
 * contain the image, if not, just keep it for faster access.
 * 
 * Instead of resizing the bitmap, setting new layout parameters, I prefered
 * Scaling the imageview, I think it looks better and it is not as memory consumming
 * as resizing the bitmap (creating a new one). */
public class ImageDownloaderTask extends AsyncTask<String, Integer, Bitmap> {

	private static final int SIZE = 350;
	
	static final LayoutParams
			params = new LayoutParams(SIZE, SIZE);
	
	
	Context ctx = null;
	int position;
	CustomArrayAdapter.ViewHolder viewHolder = null;
	boolean big = false;
	
	
	ImageDownloaderTask(Context context, CustomArrayAdapter.ViewHolder vh, int position){
		ctx = context;
		viewHolder = vh;
		this.position = position;
	}
	
	ImageDownloaderTask(Context context){
		ctx = context;
	}
	
    protected Bitmap doInBackground(String... urls) {
    	Bitmap bp;
    	bp = BitmapLoader.fetchImage(ctx, urls[0], true);
        return bp;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onPostExecute(Bitmap result) {
    	if(viewHolder != null){
	    	if(position == viewHolder.position){
				Log.v(ImageDownloaderTask.class.getSimpleName(),"\tSetting recently downloaded image to view in position: "+position);
	     	   viewHolder.img.setImageBitmap(result);
	    	} else {
				Log.v(ImageDownloaderTask.class.getSimpleName(),"\tViewHolder has different position now, ignoring downlaoded image @"+position);
	    	}
    	}
    }
}