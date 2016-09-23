package com.dragon.accounts.util.sp;

import android.content.Context;

public class SharedPref {
    public static final String NAME = "default_shared_prefs";

    public static void setString(Context context, String key, String value) {
        /* SharedPreferences sharedPreferences = getSharedPref(context);
         Editor editor = sharedPreferences.edit();
         editor.putString(key, value);
         editor.commit();*/
        MutiProcessSharedPref.putString(context, key, NAME, value);
    }

    public static String getString(Context context, String key, String defValue) {
        /*SharedPreferences sharedPreferences = getSharedPref(context);
        return sharedPreferences.getString(key, defValue);*/
        return MutiProcessSharedPref.getString(context, key, NAME, defValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        /*   SharedPreferences sharedPreferences = getSharedPref(context);
           Editor editor = sharedPreferences.edit();
           editor.putBoolean(key, value);
           editor.commit();*/
        MutiProcessSharedPref.putBoolean(context, key, NAME, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        /*  SharedPreferences sharedPreferences = getSharedPref(context);
          return sharedPreferences.getBoolean(key, defValue);*/
        return MutiProcessSharedPref.getBoolean(context, key, NAME, defValue);
    }

    public static void setInt(Context context, String key, int value) {
        /* SharedPreferences sharedPreferences = getSharedPref(context);
         Editor editor = sharedPreferences.edit();
         editor.putInt(key, value);
         editor.commit();*/
        MutiProcessSharedPref.putInteger(context, key, NAME, value);
    }

    public static int getInt(Context context, String key, int defValue) {
        /* SharedPreferences sharedPreferences = getSharedPref(context);
         return sharedPreferences.getInt(key, defValue);*/
        return MutiProcessSharedPref.getInteger(context, key, NAME, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        /*   SharedPreferences sharedPreferences = getSharedPref(context);
           return sharedPreferences.getLong(key, defValue);*/
        return MutiProcessSharedPref.getLong(context, key, NAME, defValue);
    }

    public static void setLong(Context context, String key, long value) {
        /*  SharedPreferences sharedPreferences = getSharedPref(context);
          Editor editor = sharedPreferences.edit();
          editor.putLong(key, value);
          editor.commit();*/
        MutiProcessSharedPref.putLong(context, key, NAME, value);
    }

    public static void remove(Context context, String key) {
        /*   SharedPreferences sharedPreferences = getSharedPref(context);
           Editor editor = sharedPreferences.edit();
           editor.remove(key);
           editor.commit();*/
        MutiProcessSharedPref.clear(context, NAME, key);
    }
}
