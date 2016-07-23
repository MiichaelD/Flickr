package com.uber.challenge.flickr;

import com.uber.challenge.flickr.utils.BitmapLoader;
import com.uber.challenge.flickr.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class DetailAct extends Activity{
	
	String Url = null;
	
	ImageView imgv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.image_view);
		// load image into big image_view
		Url = getIntent().getExtras().getString(MainAct.IMG_VIEW_KEY);
		if(Url != null){
			imgv = (ImageView)findViewById(R.id.iv_1);
			imgv.setScaleType(ScaleType.FIT_CENTER);
//			ScaleType.
			imgv.setImageBitmap(BitmapLoader.fetchImage(this, Url, true));
		}
		
	}

}
