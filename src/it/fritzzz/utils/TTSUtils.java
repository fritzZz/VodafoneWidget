package it.fritzzz.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class TTSUtils {
	private static final String TAG = "TTSUtils";
	
	/***
	 * Checks if com.google.tts is avaiable
	 * @param context
	 * @return
	 */
	public static boolean isTTSAvailable(Context context){ 
		try { 
			Context myContext = context.createPackageContext("com.google.tts", 0); 
		} catch (NameNotFoundException e) { 
			Log.w(TAG,"com.google.tts not found");
			return false; 
		} 
		Log.i(TAG,"com.google.tts exists!");
		return true; 
    }
	
	
	

}
