package com.uber.challenge.flickr;


import com.uber.challenge.flickr.photoservice.IPhoto;
import com.uber.challenge.flickr.photoservice.flickr.FlickrService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.GridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;

public class MainActivity extends Activity {

    private static Context m_context;

    @BindView(R.id.in) GridView mDisplayingView;
    @BindView(R.id.searchBox) EditText m_searchEditText;
    @BindView(R.id.photosCounter) TextView m_photoCounter;

    //quick hack
    private int m_pageNumber = 0;
    private String m_topic = "kittens";

    //Array adapter for the Result thread
    private CustomArrayAdapter mCustArrAdap;

    public static final String IMG_URL_KEY = "img_url", IMG_TITLE_KEY = "img_title", IMG_ARRAY_KEY = "img_array" ;

    // more info for flicker photo search: https://www.flickr.com/services/api/explore/?method=flickr.photos.search
    private static final int FLICKR_ITEMS_PER_PAGE = 30;
    private static final String
            FLICKR_API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736",
            FLICKR_API_URL = "https://api.flickr.com/services/rest/",
            FLICKR_API_QUERY_FORMAT = "?method=flickr.photos.search&api_key="+FLICKR_API_KEY+"&format=json&nojsoncallback=1&text=%s&per_page="+FLICKR_ITEMS_PER_PAGE+"&page=%d";

    public static String Next_Input_Url = null;

    public static Context getAppContext(){
        return m_context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_context = this.getApplicationContext();
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);

        mCustArrAdap = new CustomArrayAdapter(this, R.layout.image_view);
        mDisplayingView.setAdapter(mCustArrAdap);
        mDisplayingView.setOnScrollListener(new EndlessScrollListener(){
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(MainActivity.class.getSimpleName(),"Loading new data!!!");
                loadPicturesAsync(null);
            }

        });

        if(savedInstanceState == null)
            loadPicturesAsync("Kittens");
    }

    @OnItemClick(R.id.in)
    void onGridItemClick(AdapterView<?> parent, View view,int position, long id){
        IPhoto photo = mCustArrAdap.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                .putExtra(IMG_URL_KEY, photo.getUrl())
                .putExtra(IMG_TITLE_KEY, photo.getTitle());
        startActivity(intent);
    }


    @OnEditorAction(R.id.searchBox)
    boolean onSearchEditorAcion(TextView v, int actionId, KeyEvent event){
        boolean handled = false;
        Log.v(MainActivity.class.getSimpleName(), String.format("SearchEditText: ActionId: %d, KeyEvent's keycode: %d, action: %d.",
                actionId, event==null?0:event.getKeyCode(), event==null?0:event.getAction()));
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            loadPicturesAsync(MainActivity.this.m_searchEditText.getText().toString());

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            handled = true;
        }
        return handled;
    }

    void loadPicturesAsync(String newTopic){
        if (newTopic != null &&!newTopic.equals(m_topic)){
            m_pageNumber = 0;
            m_topic = newTopic;
            mCustArrAdap.clear();
        }
        LoadPhotos task = new LoadPhotos();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_load) {
            //mCustArrAdap.add("http://farm2.static.flickr.com/1565/25842799704_0e62250277.jpg");
            Log.d(MainActivity.class.getSimpleName(),"Loading new data!!!");
            loadPicturesAsync(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //AsyncTask to query the API for pictures
    private class LoadPhotos extends AsyncTask<Void,Void,List<? extends IPhoto>>{
        @Override
        protected List<? extends IPhoto> doInBackground(Void... params) {
            return FlickrService.getPhotos(m_topic, ++m_pageNumber); // increment the page number!!s
        }

        @Override
        protected void onPostExecute(List<? extends IPhoto> photos) {
            mCustArrAdap.add(photos);
            m_photoCounter.setText("Photos: "+mCustArrAdap.getCount());
        }
    }

}
