package com.project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	/**
	 * 保存Preference的name
	 */
	public static final String PREFERENCE_NAME = "saveInfo";
	private static SharedPreferences mSharePreference;
	private static PreferenceUtils mPreferenceUntils;
	private static SharedPreferences.Editor editor;
	
	private static final String SHARED_KEY_SETTING_NOTIFICATION =  "shared_key_setting_notification";
	private static final String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
	private static final String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
	private static final String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";
	
	public PreferenceUtils(Context context){
		mSharePreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 单例模式
	 */
	
	public static PreferenceUtils getInstance(Context context){
		if(mPreferenceUntils == null){
			mPreferenceUntils = new PreferenceUtils(context);
		}
		
		editor = mSharePreference.edit();
		
		return mPreferenceUntils;
	}
	
	public void setSettingMsgNotification(boolean paramBoolean){
		editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
		editor.commit();
	}
	
	public boolean getSettingMsgNotification(){
		return mSharePreference.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
	}
	
	public void setSettingMsgSound(boolean paramBoolean){
		editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
		editor.commit();
	}
	
	public boolean getSettingMsgSound(){
		return mSharePreference.getBoolean(SHARED_KEY_SETTING_SOUND, true);
	}
	
	public void setSettingMsgVibrate(boolean paramBoolean){
		editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
		editor.commit();
	}
	
	public boolean getSettingMsgVibrate(){
		return mSharePreference.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
	}
	
	public void setSettingMsgSpeaker(boolean paramBoolean){
		editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
		editor.commit();
	}
	
	public boolean getSettingMsgSpeaker(){
		return mSharePreference.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
	}
	
}
