package lib.cache;

import android.content.Context;
import android.content.SharedPreferences;

import lib.EConf;

/**
 * Created by Q
 * on 2016/11/27.
 */

public class SharedPrefHelper {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    static final String SP_NAME = EConf.CACHE_SP_NAME;

    private static SharedPrefHelper instance;

    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }


    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }


    public void clear() {
        editor.clear().apply();
    }


    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }


    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

}
