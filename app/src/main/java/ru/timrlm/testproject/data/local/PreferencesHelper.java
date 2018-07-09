package ru.timrlm.testproject.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.timrlm.testproject.di.ApplicationContext;


@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";
    public static final String PHONE = "PHONE";
    public static final String STAR = "STAR";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void setPhone(String value){ mPref.edit().putString(PHONE,value).apply();}

    public String[] getStars(){
        String s = mPref.getString(STAR,"");
        if ( s.length() == 0 ) return new String[]{};
        return s.split(";;;");
    }

    public void addIdStar(String id){
        String s = mPref.getString(STAR,"");
        mPref.edit().putString(STAR, s + id + ";;;").apply();
    }

    public void removeIdStar(String id){
        String[] starts = getStars();
        String s = "";
        for (String start : starts){
            if (!start.equals(id)) s += start + ";;;";
        }
        mPref.edit().putString(STAR, s).apply();
    }

    public String getPhone(){ return  mPref.getString(PHONE,"");}
}
