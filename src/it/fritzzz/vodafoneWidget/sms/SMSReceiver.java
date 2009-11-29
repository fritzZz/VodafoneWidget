package it.fritzzz.vodafoneWidget.sms;

import it.fritzzz.utils.CallLogUtils;
import it.fritzzz.utils.StringUtils;
import it.fritzzz.vodafoneWidget.R;
import it.fritzzz.vodafoneWidget.VodafoneWidgetProvider;
import it.fritzzz.vodafoneWidget.activities.VodafonePreferences;
import it.fritzzz.vodafoneWidget.bean.Credit;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/***
 * Sms Receiver class.
 * This class handle the SMS and manage them to update Widget Interface
 * 
 * @author alessandrofranzi
 *
 */
public class SMSReceiver extends BroadcastReceiver{
	final static String TAG = "SMSReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"Message Received");
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			Log.i(TAG,"Messaggio : "+smsMessage[n].getMessageBody());
			Log.i(TAG,"Messaggio : "+smsMessage[n].getOriginatingAddress());
			if (!StringUtils.isEmpty(smsMessage[n].getOriginatingAddress())){
				
				//SoundsUtils.enableNotificationVibrate(context);
				
				String creditNumber = context.getString(R.constant.prefix_credit_refresh_number)+context.getString(R.constant.credit_refresh_number);
				
				/***
				 * If creditNumber is empty originating address has send 
				 * another type of sms
				 */
				if (!StringUtils.isEmpty(creditNumber)){
					
				
					String valuta = context.getString(R.constant.valuta);
					// check if sender is credit refresh number
					if (creditNumber.equalsIgnoreCase(smsMessage[n].getOriginatingAddress())){
						
						String creditParsed = StringUtils.getStringBetween(smsMessage[n].getMessageBody(), context.getString(R.constant.stringCreditInsideSMSForCredit), context.getString(R.constant.stringSuffixCreditInsideSMSForCredit));
						String credit = creditParsed+valuta;
						
						String date = StringUtils.getStringBetween(smsMessage[n].getMessageBody(), context.getString(R.constant.stringCreditInsideSMSForDate), context.getString(R.constant.stringSuffixCreditInsideSMSForDate));
						
						Log.i(TAG,"Update Credit : "+credit+" date : "+date);
						
						AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
						
						// creo il bean contenente le informazioni sul credito
						Credit creditBean = new Credit();
						creditBean.setAmount(credit);
						creditBean.setDate(date);
						
						// metto uno spazio nella data per problema di visualizzazione italic
			            VodafoneWidgetProvider.refreshCredit(context, appWidgetManager,creditBean);
						
			            NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						mManager.cancelAll();
			            
						
						// cancello il messaggio se nelle preferenze  settata tale opzione
						if (isDeleteSMSActionEnabled(context)){
						
				            try {
								Thread.sleep(1000);
								// cancello il messaggio
					            Uri deleteUri = Uri.parse("content://sms");
					            context.getContentResolver().delete(deleteUri, "address=?", new String[] {smsMessage[n].getOriginatingAddress()});
					            Log.i(TAG,"SMS deleted");
					            
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							Log.i(TAG,"SMS delete preference disabled");
						}
						
						if (isDeleteCallActionEnabled(context)){
							CallLogUtils.deleteCallOfASpecifiedNumber(context, context.getString(R.constant.credit_refresh_number));
				            Log.i(TAG,"Call deleted");
						}else{
							Log.i(TAG,"Call delete preference disabled");
						}
			            
						
						
					}
				}
			}
		}
	}
	
	static boolean isDeleteSMSActionEnabled(Context context){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkboxPreference = prefs.getBoolean(VodafonePreferences.SMSDEL_PREF_NAME, false);
        Log.i(TAG,"SMS del enabled : "+checkboxPreference);
    	return checkboxPreference;
    }
	
	static boolean isDeleteCallActionEnabled(Context context){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkboxPreference = prefs.getBoolean(VodafonePreferences.CALLDEL_PREF_NAME, false);
        Log.i(TAG,"Call del enabled : "+checkboxPreference);
    	return checkboxPreference;
    }
	
}