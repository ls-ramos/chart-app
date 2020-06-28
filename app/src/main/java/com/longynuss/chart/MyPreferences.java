package com.longynuss.chart;

import android.content.Context;
import android.content.SharedPreferences;

class MyPreferences {

    private final String PREFS_GAMETIME = "gameTimeText";
    private final String PREFS_VELOCITYBAR = "velocityBarProgress";
    private final String PREFS_NUMLETTERSBAR = "numLettersProgress";
    private final String PREFS_FIRSTACESS = "firstAcessBool";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    MyPreferences(Context context){
        String FILE_NAME = "chartApp.file";
        preferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }

    boolean optionStatesExist(){
        return !(preferences.getString(PREFS_GAMETIME,"null").equals("null"));
    }

    String getGameTime(){
        return preferences.getString(PREFS_GAMETIME,null);
    }
    int getVelocityBarProgress(){
        return preferences.getInt(PREFS_VELOCITYBAR,-1);
    }
    int getNumLettersBarProgress(){
        return preferences.getInt(PREFS_NUMLETTERSBAR,-1);
    }

    void saveOptionStates(String gameTime, int velBarProgresss, int numLetterBarProgress){
        editor.putString(PREFS_GAMETIME,gameTime);
        editor.putInt(PREFS_VELOCITYBAR,velBarProgresss);
        editor.putInt(PREFS_NUMLETTERSBAR,numLetterBarProgress);
        editor.apply();
    }

    void doFirstAcess(){
        editor.putBoolean(PREFS_FIRSTACESS,false);
        editor.apply();
    }

    boolean isFirstAcess(){
        return preferences.getBoolean(PREFS_FIRSTACESS,true);
    }
}
