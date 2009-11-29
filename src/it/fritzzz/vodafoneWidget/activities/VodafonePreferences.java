package it.fritzzz.vodafoneWidget.activities;

import it.fritzzz.utils.TTSUtils;
import it.fritzzz.vodafoneWidget.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/***
 * Class that manages preferences
 * @author alessandrofranzi
 *
 */
public class VodafonePreferences extends PreferenceActivity{
	private static final String TAG = "VodafonePreferences";
	
	public static final String TTS_PREF_NAME="TTSPref";
	public static final String SMSDEL_PREF_NAME= "smsPref";
	public static final String CALLDEL_PREF_NAME="callPref";

	/***
	 * OnCreate method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"Start VodafonePreferences");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		//Get the custom preference
		Preference customPref = (Preference) findPreference(TTS_PREF_NAME);
		customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				
			    SharedPreferences prefs = preference.getSharedPreferences();
			    boolean checkTTSPref = prefs.getBoolean(TTS_PREF_NAME, false);
			    if (checkTTSPref){
					// controllo che sia abilitato il TTS sul cellulare
					if (TTSUtils.isTTSAvailable(getBaseContext())){
						Log.i(TAG,"TTS pref enabled");
					}else{
						Log.e(TAG,"Preference TTS chosen but not available, restore default preference");
						Toast.makeText(getBaseContext(), getString(R.string.tts_error), Toast.LENGTH_LONG).show();
						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean(TTS_PREF_NAME,false);
						editor.commit();
						CheckBoxPreference chckPref = (CheckBoxPreference) findPreference(TTS_PREF_NAME);
						chckPref.setEnabled(false);
						chckPref.setChecked(false);
					}
			    }
				return true;
			}

		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	super.onMenuItemSelected(featureId, item);
    	Log.i(TAG,"onMenuItemSelected : "+item);
    	
         switch (item.getItemId()) {
	         
	         case R.id.quit : 
	        	 Log.i(TAG,"Close activity");
	        	 finish();
	        	 return true;
	         case R.id.about : 
	        	 
	        	 Log.i(TAG,"call about activity");
	        	 callAboutActivity(this.getBaseContext());
	        	 return true;
         }
      return super.onOptionsItemSelected(item);
    }
	
	private void callAboutActivity(Context context) {
		
   		Intent intent = new Intent("it.fritzzz.vodafoneWidget.action.ABOUT");
   		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   		context.startActivity(intent);
   		
    }
}
