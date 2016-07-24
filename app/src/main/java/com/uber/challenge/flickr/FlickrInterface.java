package com.uber.challenge.flickr;

import android.util.Log;

import com.uber.challenge.flickr.photoservice.flickr.FlickrApi;
import com.uber.challenge.flickr.utils.JSonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Michael Duarte on 7/23/2016.
 */

public interface FlickrInterface {
    @GET("?method=flickr.photos.search&api_key="+FlickrConstants.API_KEY+"&format=json"+
            "&nojsoncallback=1&per_page="+FlickrConstants.ITEMS_PER_PAGE)
    Call<FlickrApi> searchPhotos(@Query("text") String topic, @Query("page") int page);


}

class FlickrConstants{
    public static final int ITEMS_PER_PAGE = 30;
    public static final String
            API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736",
            API_URL = "https://api.flickr.com/services/rest/",
            API_QUERY_FORMAT = "?method=flickr.photos.search&api_key="+API_KEY+
                    "&format=json&nojsoncallback=1&text=%s&per_page="+ITEMS_PER_PAGE+"&page=%d";


}

