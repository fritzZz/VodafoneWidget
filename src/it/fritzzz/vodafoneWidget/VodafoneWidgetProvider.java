package it.fritzzz.vodafoneWidget;

import it.fritzzz.utils.SharedPreferencesUtils;
import it.fritzzz.utils.SoundsUtils;
import it.fritzzz.utils.TTSUtils;
import it.fritzzz.utils.bean.AudioStoreBean;
import it.fritzzz.vodafoneWidget.activities.VodafonePreferences;
import it.fritzzz.vodafoneWidget.bean.Credit;
import it.fritzzz.vodafoneWidget.voice.VoiceSpeakerForCredit;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/***
 * Class that manages the Widget
 * 
 * @author alessandrofranzi
 *
 */
public class VodafoneWidgetProvider extends AppWidgetProvider {
	
	
	private static final String TAG = "AppWidgetProvider";
	private static final String PREFS_NAME
    = "it.fritzzz.vodafoneWidget.VodafoneActivity";
	

	
	private static final String ACTION_NAME= "actionParam";
	private static final String ACTION_REFRESH= "actionRefresh";
	
	public static final String EXTRA_CREDIT = "creditExtra";
	

	
	private static final String PREFS_WIDGETID_NAME = "widget_id";
	private static final String PREFS_AUDIOSTORE_NAME = "audio_store";
	private static final String PREFS_CREDIT_NAME ="credit_store";
	
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		Log.i(TAG,"onUpdate");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
        	int appWidgetId = appWidgetIds[i];
        	setWidgetId(context,appWidgetId);
        	
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			//views.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
			attachConfigureIntent(context, views,appWidgetId);
			attachRefreshIntent(context, views,appWidgetId);
			
			// recupero eventuali informazioni sul credito registrate nelle preferenze
			Credit creditBean = (Credit)SharedPreferencesUtils.retrieveBean(context,PREFS_NAME,Context.MODE_APPEND, PREFS_CREDIT_NAME);
	        if (creditBean == null){
	        	Log.i(TAG,"No bean presente in SharedPreferences for credit.");
	        }else{
	        	Log.i(TAG,"Retrieved info for credit.");
	        	refreshWidgetForCredit(context,appWidgetManager, creditBean);
	        }
			
			// Tell the AppWidgetManager to perform an update on the current App Widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
        }
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i(TAG,"OnDeleted");
	}
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.i(TAG,"onEnables");
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		Log.i(TAG,"OnReceived");
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
			AppWidgetManager.EXTRA_APPWIDGET_ID,
			AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			super.onReceive(context, intent);
			Bundle extras = intent.getExtras();
			if (extras!=null){
				String azione = extras.getString(ACTION_NAME);
				Log.i(TAG, "Azione ricevuta : "+azione);
				
				if (ACTION_REFRESH.equalsIgnoreCase(azione)){
					int mAppWidgetId = 0;
			        if (extras != null) {
			            mAppWidgetId = extras.getInt(
			                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			        }
			        Log.i(TAG,"Widget id : "+mAppWidgetId);
					Log.i(TAG, "Visualizzo la progressBar");
					
					// aggiorno la GUI per il loading
					RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			        views.setViewVisibility(R.id.frameProgressBar, View.VISIBLE);
			        views.setTextViewText(R.id.widget_credit, "loading...");
			        views.setTextViewText(R.id.widget_date, "");
			        
			        // recupero le informazioni audio attuali e le salvo
			        AudioStoreBean astoreBean = SoundsUtils.getNotificationSettings(context);
			        SharedPreferencesUtils.storeBean(context,PREFS_NAME,0, astoreBean, PREFS_AUDIOSTORE_NAME);
			        
			        // setto le informazioni sul credito..se esiste uno store precedente
			        
			        
			        // aggiorno il widget
			        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			        appWidgetManager.updateAppWidget(getWidgetId(context), views);
			        
			        // disabilito i suoni
			        SoundsUtils.disableNotification(context);
			        
			        // inizializzo il textToSpeech
			     // check se il TTS e' configurato a livello di sistema
					if (isTTSAvaiableForThisInstance(context)){
						initVoice(context);
						Log.i(TAG,"TTS initialized");
					}else{
						Log.i(TAG,"TTS not initialized, it is disabled");
					}
			        
			        //Context
			        call(context);
				}else{
					Log.w(TAG,"Action not managed : "+azione);
				}
			}
		}
	}
	
	
	private static void refreshWidgetView(Context context,AppWidgetManager appWidgetManager){
		Log.i(TAG,"Widget ID : "+getWidgetId(context));
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
        appWidgetManager.updateAppWidget(getWidgetId(context), views);
	}
	
	public static void refreshWidgetForCredit(Context context,AppWidgetManager appWidgetManager,Credit creditBean){
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		views.setTextViewText(R.id.widget_credit, creditBean.getAmount());
		views.setTextViewText(R.id.widget_date, creditBean.getDate());
        views.setViewVisibility(R.id.frameProgressBar, View.INVISIBLE);
        appWidgetManager.updateAppWidget(getWidgetId(context), views);
        //refreshWidgetView(context,appWidgetManager);
	}
    
    public static void refreshCredit(Context context, AppWidgetManager appWidgetManager, Credit creditBean){
    	Log.i(TAG,"Refresh credit");
    	
    	// aggiorno il widget con il nuovo credito
    	refreshWidgetForCredit(context,appWidgetManager,creditBean);        
        
        // registro nelle preferenze l'ultimo aggiornamento del credito
        SharedPreferencesUtils.storeBean(context,PREFS_NAME,0, creditBean, PREFS_CREDIT_NAME);
        
        // eseguo il restore delle configurazioni audio precedenti
        AudioStoreBean beanRetrieved = (AudioStoreBean)SharedPreferencesUtils.retrieveBean(context,PREFS_NAME,Context.MODE_APPEND, PREFS_AUDIOSTORE_NAME);
        SoundsUtils.restoreNotificationSettings(context, beanRetrieved);
        
       
		// check se il TTS e' configurato a livello di sistema
		if (isTTSAvaiableForThisInstance(context)){
        	Log.i(TAG,"preparing for speech");
        	try{
        		callVoiceIntent(context,creditBean);
        	}catch(Exception e){
        		Log.w(TAG,"Problem with TTS ",e);
        	}
        }else{
        	Log.w(TAG,"TTS not active or not installed on machine. Configuration problem!");
        }
        
    }
	
    static boolean isTTSAvaiableForThisInstance(Context context){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkboxPreference = prefs.getBoolean(VodafonePreferences.TTS_PREF_NAME, false);
    	if (checkboxPreference && TTSUtils.isTTSAvailable(context)){
    		return true;
    	}
    	return false;
    }
    
    static void callVoiceIntent(Context context, Credit creditBean){
    	VoiceSpeakerForCredit speak4Cred = new VoiceSpeakerForCredit(context,creditBean.getAmount());
    	speak4Cred.sayCredit();
    }
    
    static void initVoice(Context context){
    	VoiceSpeakerForCredit speak4Cred = new VoiceSpeakerForCredit(context);
    	speak4Cred.init();
    }
    
	static void attachConfigureIntent(Context context,RemoteViews views,int WidgetId){
		Intent configureIntent = new Intent();
		configureIntent.setClassName("it.fritzzz.vodafoneWidget","it.fritzzz.vodafoneWidget.activities.VodafonePreferences");
		configureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Context.MODE_APPEND, configureIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.hiddenRightButton, pendingIntent);
	}
	
	static void attachRefreshIntent(Context context,RemoteViews views,int WidgetId){
		Intent configureIntent = new Intent(context,VodafoneWidgetProvider.class);
		configureIntent.putExtra(ACTION_NAME, ACTION_REFRESH);
		configureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, configureIntent,PendingIntent.FLAG_UPDATE_CURRENT); 
        views.setOnClickPendingIntent(R.id.hiddenLeftButton, pendingIntent);
	}
	
	final void call(Context context) {
		String creditNumber = context.getString(R.constant.credit_refresh_number);
		Log.i(TAG,"Starting call to : "+creditNumber);
   		Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + creditNumber));
   		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   		context.startActivity(intent);
   		
    }
	
	
	
	private static int getWidgetId(Context context){
    	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
    	int widgetId = prefs.getInt(PREFS_WIDGETID_NAME,0);
    	return widgetId;
    }
    
    private void setWidgetId(Context context,int widgetId){
    	SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND).edit();
        prefs.putInt(PREFS_WIDGETID_NAME, widgetId);
        
        Log.i(TAG, "Settato widgetID nelle prefs :"+widgetId);
        prefs.commit();
    }
    
}
