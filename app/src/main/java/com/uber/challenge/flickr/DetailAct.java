package com.uber.challenge.flickr;

import com.squareup.picasso.Picasso;
import com.uber.challenge.flickr.utils.BitmapLoader;
import com.uber.challenge.flickr.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAct extends Activity{
	
	String Url = null;
	
	@BindView(R.id.iv_1) ImageView imgv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.image_view);
		ButterKnife.bind(this);
		// load image into big image_view
		Url = getIntent().getExtras().getString(MainAct.IMG_VIEW_KEY);
		if(Url != null){
			imgv.setScaleType(ScaleType.FIT_CENTER);
//			ScaleType.
			Picasso.with(this).load(Url).into(imgv);
//			imgv.setImageBitmap(BitmapLoader.fetchImage(this, Url, true));
		}
		
	}

}
