package com.uber.challenge.flickr;


import com.uber.challenge.flickr.utils.JSonParser;
import com.uber.challenge.flickr.utils.ServerConn;
import com.uber.challenge.flickr.R;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import android.widget.GridView;

public class MainAct extends Activity {
	
	private static Context m_context;

	private GridView mDisplayingView;
	
	private EditText m_searchEditText;
	
	private TextView m_photoCounter;
	
    //quick hack
    private int m_pageNumber = 0;
    private String m_topic = "kittens";
    
	//Array adapter for the Result thread
    private CustomArrayAdapter mCustArrAdap;
	
    public static final String IMG_VIEW_KEY = "img_on_view";
    
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
		
		mCustArrAdap = new CustomArrayAdapter(this, R.layout.image_view);
		
		mDisplayingView = (GridView) findViewById(R.id.in);
		mDisplayingView.setAdapter(mCustArrAdap);
		mDisplayingView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent(MainAct.this, DetailAct.class)
                .putExtra(IMG_VIEW_KEY, mCustArrAdap.getItem(position));
				startActivity(intent);
			}		
		});
		
		mDisplayingView.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.d(MainAct.class.getSimpleName(),"Loading new data!!!");
				loadPicturesAsync(null);		
			}
			
		});
		
		m_searchEditText = (EditText) findViewById(R.id.searchBox);
		m_searchEditText.setOnEditorActionListener(new OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				Log.v(MainAct.class.getSimpleName(), String.format("SearchEditText: ActionId: %d, KeyEvent's keycode: %d, action: %d.",
						actionId, event==null?0:event.getKeyCode(), event==null?0:event.getAction()));
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	loadPicturesAsync(MainAct.this.m_searchEditText.getText().toString());

		        	InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		        	
		            handled = true;
		        }
		        return handled;
			}
			
		});
		
		m_photoCounter = (TextView) findViewById(R.id.photosCounter);
		
		if(savedInstanceState == null)
			loadPicturesAsync("Kittens");
	}
	
	void loadPicturesAsync(String newTopic){
		if (newTopic != null && !newTopic.equals(m_topic)){
			m_pageNumber = 0;
			m_topic = newTopic;
			mCustArrAdap.clear();
		}

		new StartFetching().execute();
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
			Log.d(MainAct.class.getSimpleName(),"Loading new data!!!");
			loadPicturesAsync(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/** Save data from ListView when rotating the device 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle) */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		Log.i(this.getClass().getSimpleName(),"onRestoreInstanceState restoring  values");
		// Initialize the array adapter for the conversation thread
        if (savedInstanceState != null) {
        	String[] values = savedInstanceState.getStringArray(IMG_VIEW_KEY);
            for (String result : values) {
            	mCustArrAdap.add(result);
            }
        }
	}
	
	
	/** Save data from ListView when rotating the device */
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        int items = mCustArrAdap.getCount();
        
        Log.i(this.getClass().getSimpleName(),"onSaveInstanceState saving "+items+" values");
        
        String[] values =  new String[items];
        for(int i =0 ; i < items;i++)
        	values[i] = mCustArrAdap.getItem(i);
        
        savedState.putStringArray(IMG_VIEW_KEY, values);
    }
    
    
    //AsyncTask to query the API for pictures
    private class StartFetching extends AsyncTask<Void,Void,String[]>{
    	@Override
	    protected String[] doInBackground(Void... params) {
    		String[] urls = null;
    		++m_pageNumber; // increment the page number!!
    		String inputJson = null;
	    	try {
	    		inputJson = ServerConn.shared().getResponse(FLICKR_API_URL+ String.format(FLICKR_API_QUERY_FORMAT, m_topic, m_pageNumber) );
				Log.v(MainAct.class.getSimpleName(),"Response; chars: "+inputJson.length()+"\nText: "+inputJson);
				if (inputJson == null || inputJson.isEmpty()){
					throw new Exception("Empty Response from query: "+FLICKR_API_URL+ String.format(FLICKR_API_QUERY_FORMAT, m_topic, m_pageNumber));
				}
		    	urls = JSonParser.getURLs(MainAct.this, inputJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return urls;
	    }

		@Override
	    protected void onPostExecute(String[] urls) {
			mCustArrAdap.add(urls);
			m_photoCounter.setText("Photos: "+m_pageNumber*FLICKR_ITEMS_PER_PAGE);
	    }
    }
	
}
