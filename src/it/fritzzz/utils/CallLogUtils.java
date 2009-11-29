package it.fritzzz.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

/***
 * Utility Class for manage Call Log
 * @author alessandrofranzi
 *
 */
public class CallLogUtils {
	private static final String TAG ="CallLogUtils";
	
	/***
	 * Deletes all entry in call log of a specified caller number
	 * @param context		: application context
	 * @param callerNumber	: caller number
	 * @return				: boolean (true = entries deleted; false = no entry has deleted)
	 */
	public static boolean deleteCallOfASpecifiedNumber(Context context,String callerNumber){
		try{
			context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, android.provider.CallLog.Calls.NUMBER+"=?", new String[] {callerNumber});
			Log.i(TAG,"deleted entries from Call log of callerNumber : "+callerNumber);
			return true;
		}catch(Exception e){
			Log.i(TAG,"Problem deleting Call log of callerNumber : "+callerNumber);
			return false;
		}
	}
	
	public static Cursor getAllCallLogAboutACaller(Context context, String callerNumber){
		// cancello la chiamata in uscita se nelle preferenze è settata tale opzione
		Uri delUri = Uri.withAppendedPath(CallLog.Calls.CONTENT_URI,"");
		Cursor cursor =      context.getContentResolver().query(delUri,null,android.provider.CallLog.Calls.NUMBER+"=?",new String[]{"404"},null);
		try{
			boolean moveToFirst=cursor.moveToFirst();
			Log.i("MOVETOFIRST", "moveToFirst="+moveToFirst);
			do{
				int numberColumn = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
				String callerPhoneNumber = cursor.getString(numberColumn);
				Log.i(TAG,"numero : "+callerPhoneNumber);
			}while(cursor.moveToNext());
			
		}catch(Exception e){
			Log.e(TAG,"Problem moving to first entry",e);
		}
		return cursor;
	}
}
