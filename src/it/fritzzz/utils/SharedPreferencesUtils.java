package it.fritzzz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/***
 * Help class to manage advanced SharedPreferences such as storing Java Bean
 * 
 * @author alessandrofranzi
 *
 */
public class SharedPreferencesUtils {
	final static String TAG = "SharedPreferencesUtils";
	
	/***
	 * Method that stores bean inside SharedPreferences of our application.
	 * The bean must implements Serializable, so it can be serialized as String and
	 * stored inside Preferences
	 * 
	 * @param prefs	: SharedPreferences instance
	 * @param bean	: bean that we want to store
	 * @param name	: name of property inside preferences
	 */
	public static void storeBean (SharedPreferences prefs,Object bean,String name){
		Editor editor = prefs.edit();
		try {
			String objectSerialized = toString(bean);
			Log.i(TAG,"Object stored id "+name+" serialized "+objectSerialized);
			editor.remove(objectSerialized);
			editor.putString(name,objectSerialized);
			boolean ok = editor.commit();
			Log.i(TAG,"ok : "+ok);
		} catch (IOException e) {
			Log.e(TAG,"Problem serializing object of class :"+bean.getClass().getName());
		}
	}
	
	/***
	 * Method that retrieve bean from SharedPreferences
	 * 
	 * @param prefs	: SharedPreferences instance
	 * @param name	: name of property inside preferences
	 * @return		: bean retrieved 
	 */
	public static Object retrieveBean (SharedPreferences prefs,String name){
		try {
			String serializedRetrievedString = prefs.getString(name, null);
			Log.i(TAG,"Serialized id "+name+" object : "+serializedRetrievedString);
			if (serializedRetrievedString!=null){
				return fromString(serializedRetrievedString);
			}
		} catch (IOException e) {
			Log.e(TAG,"Problem deserializing");
		} catch (ClassNotFoundException e) {
			Log.e(TAG,"Problem deserializing");
		}
		return null;
	}
	
	/***
	 * Method that retrieve bean from SharedPreferences
	 * 
	 * @param context		: context
	 * @param prefsName		: name of file for retrieve SharedPreferences
	 * @param mode			: mode to access to SharedPreferences
	 * @param name			: name of property inside preferences
	 * @return				: object retrieved
	 */
	public static Object retrieveBean (Context context,String prefsName, int mode,String name){
		return retrieveBean(context.getSharedPreferences(prefsName, mode),name);
	}
	
	/***
	 * Method that stores bean inside SharedPreferences of our application.
	 * The bean must implements Serializable, so it can be serialized as String and
	 * stored inside Preferences
	 * 
	 * @param context		: context
	 * @param prefsName		: name of file for retrieve SharedPreferences
	 * @param mode			: mode to access to SharedPreferences
	 * @param bean			: bean that we want to store
	 * @param name			: name of property for bean inside preferences
	 */
	public static void storeBean (Context context,String prefsName, int mode, Object bean, String name){
		storeBean(context.getSharedPreferences(prefsName, mode),bean,name);
	}
	
	/***
	 * Method that get Object instance from serialized string
	 * @param s		: string
	 * @return		: object instance
	 * @throws IOException				: Exception
	 * @throws ClassNotFoundException	: Exception
	 */
	private static Object fromString( String s ) throws IOException ,
    ClassNotFoundException {
		byte [] data = Base64Coder.decode( s );
		ObjectInputStream ois = new ObjectInputStream( 
		new ByteArrayInputStream(  data ) );
		Object o  = ois.readObject();
		ois.close();
		return o;
	}
	
	/***
	 * Method that serialize an object to String
	 * @param o		: object
	 * @return		: string serialized
	 * @throws IOException	: Exception
	 */
	private static String toString( Object o ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( o );
		oos.close();
		return new String( Base64Coder.encode( baos.toByteArray() ) );
	}
}
