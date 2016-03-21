package com.example.octopuscabbage.vrmouse.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by octopuscabbage on 3/20/16.
 */
public class SettingsStorage {
    private static final String PREFS_NAME = "PrefsFile";
    private static final String PREFS_LEFT_STREAM_KEY = "LeftStream";
    private static final String PREFS_RIGHT_STREAM_KEY = "RightStream";

    private static SharedPreferences sharedPreferences = null;

    public SettingsStorage(Context context){
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        }
    }

    public void setStreamLocations(String leftStream, String rightStream){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_LEFT_STREAM_KEY,leftStream);
        editor.putString(PREFS_RIGHT_STREAM_KEY,rightStream);
        editor.apply();
    }

    public String readLeftStreamLocation(){
        return sharedPreferences.getString(PREFS_LEFT_STREAM_KEY,"");
    }
    public String readRightStreamLocation(){
        return sharedPreferences.getString(PREFS_RIGHT_STREAM_KEY,"");
    }

}
