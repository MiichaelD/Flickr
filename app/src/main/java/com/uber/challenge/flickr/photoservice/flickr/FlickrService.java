package com.uber.challenge.flickr.photoservice.flickr;

import com.uber.challenge.flickr.photoservice.IPhoto;
import com.uber.challenge.flickr.photoservice.flickr.model.Root;
import com.uber.challenge.flickr.photoservice.flickr.model.Photo;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Michael Duarte on 7/23/2016.
 */

public class FlickrService {

    public static final int ITEMS_PER_PAGE = 30;
    public static final String
            API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736",
            API_URL = "https://api.flickr.com/services/rest/",
            API_QUERY_FORMAT = "?method=flickr.photos.search&api_key="+API_KEY+
                    "&format=json&nojsoncallback=1&text=%s&per_page="+ITEMS_PER_PAGE+"&page=%d";

    public static List<? extends IPhoto> getPhotos(String topic, int pageNumber){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FlickrApi photoService = retrofit.create(FlickrApi.class);
        Call<Root> call = photoService.searchPhotos(topic, pageNumber);
        try {
            Response<Root> response = call.execute();
            List<Photo> photos = response.body().getPhotos().getPhoto();
            return photos;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}

//https://www.flickr.com/services/api/
interface FlickrApi{

//https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736
// &format=json&nojsoncallback=1&text=kittens&per_page=30&page=1
    @GET("?method=flickr.photos.search&api_key="+FlickrService.API_KEY+"&format=json"+
            "&nojsoncallback=1&per_page="+FlickrService.ITEMS_PER_PAGE)
    Call<Root> searchPhotos(@Query("text") String topic, @Query("page") int page);


//https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=3e7cc266ae2b0e0d78e279ce8e361736
// &format=json&nojsoncallback=1&photo_id=28506354275&secret=f3560e2565
    @GET("?method=flickr.photos.getInfo&api_key="+FlickrService.API_KEY+"&format=json"+
            "&nojsoncallback=1")
        Call<Root> getPhotoInfo(@Query("photo_id") String photoId, @Query("secret") String secret);
}



