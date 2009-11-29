package it.fritzzz.utils;

import android.util.Log;

/***
 * Strings utils
 * 
 * @author alessandrofranzi
 *
 */
public class StringUtils {
	public final static String TAG = "StringUtils";
	
	/***
	 * Check if the string is null or empty
	 * @param toCheck	 	: string to check
	 * @return				: true if the string is null or empty
	 */
	public static boolean isEmpty(String toCheck){
		if (toCheck==null || ("".equalsIgnoreCase(toCheck))){
			return true;
		}
		return false;
	}
	
	public static String getStringBetween(String originalString,String preString,String postString){
		String retString = "";
		if (!isEmpty(originalString)){
			int pointerToindex = originalString.indexOf(preString);
			int pointerToInit = -1;
			if (pointerToindex >= 0 ){
				pointerToInit = pointerToindex+preString.length();
				String trunchedString = originalString.substring(pointerToInit);
				if (!isEmpty(trunchedString)){
					int pointerToSuffix = trunchedString.indexOf(postString);
					if (pointerToSuffix >=0){
						retString = trunchedString.substring(0,pointerToSuffix);
						Log.d(TAG,"String founded : "+retString);
						return retString;
					}
				}
			}
		}
		return retString;
	}
	
	
	

}
