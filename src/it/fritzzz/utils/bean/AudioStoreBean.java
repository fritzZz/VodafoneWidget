package it.fritzzz.utils.bean;

import java.io.Serializable;

import android.media.AudioManager;

/***
 * Bean class that store audio information
 * 
 * @author alessandrofranzi
 *
 */
public class AudioStoreBean implements Serializable{

	private static final long serialVersionUID = -3181524412089510013L;
	private int vibrateMode;
	private int volume;
	private int ringerMode;
	
	
	/***
	 * Constructor : it takes audioManager object and extract all needed information
	 * 
	 * @param audioManager	: audioManager object
	 */
	public AudioStoreBean(AudioManager audioManager) {
		vibrateMode = audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION);
		volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		ringerMode = audioManager.getRingerMode();
	}
	
	/***
	 * Constructor : do nothing
	 */
	public AudioStoreBean() {
		// do nothing
	}
	
	public int getVibrateMode() {
		return vibrateMode;
	}
	public void setVibrateMode(int vibrateMode) {
		this.vibrateMode = vibrateMode;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getRingerMode() {
		return ringerMode;
	}

	public void setRingerMode(int ringerMode) {
		this.ringerMode = ringerMode;
	}
}
