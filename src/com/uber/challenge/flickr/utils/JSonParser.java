package com.uber.challenge.flickr.utils;

import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.content.Context;


public class JSonParser {

	private static final String PHOTOS_OBJ = "photos", PHOTOS_ARR = "photo", PHOTO_ID = "id",// PHOTO_OWNER = "owner", PHOTO_PAGE = "page"
						PHOTO_SERVER = "server", PHOTO_SECRET = "secret", PHOTO_FARM = "farm";
	
	
	@SuppressLint("DefaultLocale") public static String[] getURLs(Context context, String JSon){
		JSONObject res = null;
		String[] dataOutput = null;
		try {
			res = new JSONObject(JSon);
			JSONObject photosObj = res.getJSONObject(PHOTOS_OBJ);
			JSONArray photosArr = photosObj.getJSONArray(PHOTOS_ARR);
			
			int data_length = photosArr.length();
			dataOutput = new String[data_length];
			
			for(int i=0 ;i < data_length; i++){
				JSONObject photo = photosArr.getJSONObject(i);
				String id = photo.getString(PHOTO_ID);
//				String owner = photo.getString(PHOTO_OWNER);
				String server = photo.getString(PHOTO_SERVER);
				String secret = photo.getString(PHOTO_SECRET);
				int farm = photo.getInt(PHOTO_FARM);
				String imageUrl = String.format("http://farm%d.static.flickr.com/%s/%s_%s.jpg", farm, server, id, secret);
				dataOutput[i] = imageUrl;
			}
			
		}catch (Exception e){
			android.util.Log.e(JSonParser.class.getSimpleName(),e.getLocalizedMessage());
		}
		
		return dataOutput;
	}
	
	
}
