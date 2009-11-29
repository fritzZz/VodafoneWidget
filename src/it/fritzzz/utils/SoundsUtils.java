package it.fritzzz.utils;

import it.fritzzz.utils.bean.AudioStoreBean;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class SoundsUtils {
	private static final String TAG = "SoundsUtils";
	
	public static void disableNotification (Context context){
		AudioManager audioManager = getAudioManager(context);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
		audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		
	}
	
	public static AudioStoreBean getNotificationSettings(Context context){
		AudioManager audioManager = getAudioManager(context);
		AudioStoreBean abean = new AudioStoreBean(audioManager);
		return abean;
	}
	
	public static void restoreNotificationSettings (Context context, AudioStoreBean audioBean){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,audioBean.getVibrateMode());
		audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,audioBean.getVolume(),AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		Log.i(TAG,"Restore notification settings");
		Log.i(TAG,"Vibrate mode : "+audioBean.getVibrateMode());
		Log.i(TAG,"Stream volume : "+audioBean.getVolume());
	}
	
	
	public static AudioManager getAudioManager(Context context){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return audioManager;
	}
}
