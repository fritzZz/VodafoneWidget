package it.fritzzz.vodafoneWidget.activities;

import it.fritzzz.vodafoneWidget.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/***
 * About activity
 * @author alessandrofranzi
 *
 */
public class About extends Activity{
	private static final String TAG = "About";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"Start About");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}
