package com.uber.challenge.flickr.utils;

import com.uber.challenge.flickr.MainAct;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServerConn extends ServerCom{


	private static ServerConn instance;
	
	//let the user have many instances so each one has different headers/properties
	public ServerConn(){	}
	
	public static ServerConn shared(){
		if(instance == null){
			instance = new ServerConn();
		}
		return instance;
	}
	
	@Override
	public boolean isNetworkAvailable() {
		boolean connected = false;
		ConnectivityManager cm = (ConnectivityManager) MainAct.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if ( cm != null ){
			//this method iterates thru all the networks available to check which one is connected
	//		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	//		for (NetworkInfo ni : netInfo) 
	//			if ( ni.isConnected() /*&& ni.getTypeName().equalsIgnoreCase("WIFI")*/ )
	//				connected = true;
			
			//this method gets the active network and checks if it is connected
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			connected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
		}
		return connected;
	}
} 