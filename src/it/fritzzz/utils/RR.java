package it.fritzzz.utils;

import it.fritzzz.vodafoneWidget.R;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/***
 * Class that has access to Resources at runtime by reflection
 * 
 * @author alessandrofranzi
 *
 */
public class RR {
	private final static String TAG = "RR";
	
	/***
	 * Method for getting string resources
	 * @param context	: application context
	 * @param name		: name of resource
	 * @return			: string retrieved
	 */
	public static String getString(Context context, String name) {
        Class<R.string> resclass = R.string.class;
        String s = null;
        Field field;
        int id = 0;
 
        try {
        	field = resclass.getField(name);
        	id = field.getInt(field);
            s = context.getResources().getString(id);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return s;
    }
	
	/***
	 * Method for getting drawable resources
	 * @param context	: application context
	 * @param name		: name of resource
	 * @return			: drawable retrieved
	 */
	public static Drawable getDrawable(Context context, String name) {
        Class<R.drawable> resclass = R.drawable.class;
        Drawable d = null;
        Field field;
        int id = 0;
 
        try {
        	field = resclass.getField(name);
        	id = field.getInt(field);
            d = context.getResources().getDrawable(id);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return  d;
    }
}
