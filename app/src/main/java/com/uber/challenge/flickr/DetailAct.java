package com.uber.challenge.flickr;

import com.squareup.picasso.Picasso;
import com.uber.challenge.flickr.utils.BitmapLoader;
import com.uber.challenge.flickr.R;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAct extends Activity{

//	private Matrix matrix = new Matrix();
	private float scale = 1f;
	private ScaleGestureDetector SGD;
    @BindView(R.id.iv_1) ImageView imgv;

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
            // ImageViewTouch already has this capability: https://github.com/sephiroth74/ImageViewZoom
			scale *= detector.getScaleFactor();
			scale = Math.max(0.1f, Math.min(scale, 5.0f));

//            matrix.setScale(scale, scale);
//            imgv.setImageMatrix(matrix);
            imgv.setScaleX(scale);
            imgv.setScaleY(scale);

			return true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		ButterKnife.bind(this);

		String url = getIntent().getExtras().getString(MainAct.IMG_URL_KEY);
        String title = getIntent().getExtras().getString(MainAct.IMG_TITLE_KEY);
		if(url != null){
			imgv.setScaleType(ScaleType.FIT_CENTER);
			Picasso.with(this).load(url).into(imgv);
		}

		SGD = new ScaleGestureDetector(this,new ScaleListener());
	}

	public boolean onTouchEvent(MotionEvent ev) {
		SGD.onTouchEvent(ev);
		return true;
	}

}
