package it.fritzzz.vodafoneWidget.voice;

import it.fritzzz.utils.StringUtils;
import it.fritzzz.vodafoneWidget.R;

import java.util.Locale;

import com.google.tts.TTS;


import android.content.Context;

import android.util.Log;

public class VoiceSpeakerForCredit{
	private static final String TAG = "VoiceSpeakerForCredit";
	
	private TTS myTts;
	private String credit ="";
	Context ctx = null;
	
	/***
	 * Costruttore
	 * @param aCredit
	 */
	public VoiceSpeakerForCredit(Context aCtx,String aCredit){
        
		credit = aCredit;
        ctx = aCtx;
        
        Log.i(TAG,"Inizio");
        
    	if (!StringUtils.isEmpty(credit)){
    		credit = credit.substring(0,credit.length()-1);
    	}
    	
    	Log.i(TAG,"Credit to say : "+credit);
    
    }
	
	/***
	 * Costruttore
	 * @param aCredit
	 */
	public VoiceSpeakerForCredit(Context aCtx){
	    ctx = aCtx;
        
    }
    
	public void init(){
		try{
        	Context myContext = ctx.createPackageContext("com.google.tts", 0);
        	
        	myTts = new TTS(myContext, null, true);
    	}catch(Exception e){
    		Log.w(TAG,"Problem with TTS ",e);
    	}
	}
	
	public void sayCredit(){
		try{
        	Context myContext = ctx.createPackageContext("com.google.tts", 0);
        	
        	myTts = new TTS(myContext, ttsInitListener, true);
        	Log.i(TAG,"locale : "+Locale.ITALY.toString());
        	myTts.setLanguage(Locale.ITALY.toString());
    	}catch(Exception e){
    		Log.w(TAG,"Problem with TTS ",e);
    	}
	}

    
    private TTS.InitListener ttsInitListener = new TTS.InitListener() {
    	public void onInit(int version) {
    		try{
	    		Log.i(TAG,"Preparo il testo");
	    		String[] splittedCredit = credit.split("\\.");
	    		if (splittedCredit!=null){
	    			Log.i(TAG,"Splitted : "+splittedCredit.length);
	    			if (splittedCredit.length>1){
	    				String textToSay = "";
	    	    		textToSay += ctx.getString(R.string.credito_residuo);
	    				textToSay += " "+splittedCredit[0]+" ";
	    	    		textToSay += ctx.getString(R.string.moneta);
	    	    		int centesimi = Integer.valueOf(splittedCredit[1]);
	    	    		textToSay += " e "+centesimi+" ";
	    	    		textToSay += ctx.getString(R.string.centesimi);
	    	    		Log.i(TAG,"dico : "+textToSay);
	    	    		myTts.speak(textToSay, 0,null);
	    	    		boolean hasBeginTalk = false;
	    	    		while(true){
	    	    			//Log.i(TAG,"Vado!!");
	    	    			if (myTts.isSpeaking() && !hasBeginTalk){
	    	    				// se stˆ parlando e non ho settato tale avvenimento lo setto
	    	    				Log.i(TAG,"has begin to speak");
	    	    				hasBeginTalk = true;
	    	    			}
	    	    			if(hasBeginTalk && !myTts.isSpeaking()){
	    	    				Log.i(TAG,"End of speak");
	    	    				break;
	    	    			}
	    	    		}
	    			}
	    		}else{
	    			Log.w(TAG,"problemi nel recuperare il credito");
	    		}
    		}catch (Exception e) {
				Log.w(TAG,"Eccezione : ",e);
			}

			Log.i(TAG,"Fine");
    	}
    };
}
