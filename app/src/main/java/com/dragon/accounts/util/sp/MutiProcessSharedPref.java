package com.dragon.accounts.util.sp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MutiProcessSharedPref {

    public static void putLong(Context cxt, String key, String prefNameString, long value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        cxt.getContentResolver().update(
                Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                        + MutiProcessSharedPrefProvider.SPUT + "/long/" + prefNameString), cv, null, null);
    }

    public static long getLong(Context cxt, String key, String prefName, long defValue) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SGET + "/long/" + prefName);
        Cursor cursor = cxt.getContentResolver().query(uri, null, key, null, String.valueOf(defValue));
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return defValue;
    }

    public static void putInteger(Context cxt, String key, String prefNameString, Integer value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        cxt.getContentResolver().update(
                Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                        + MutiProcessSharedPrefProvider.SPUT + "/integer/" + prefNameString), cv, null, null);
    }

    public static int getInteger(Context cxt, String key, String prefName, int defValue) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SGET + "/integer/" + prefName);
        Cursor cursor = cxt.getContentResolver().query(uri, null, key, null, String.valueOf(defValue));
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return defValue;
    }

    public static void putString(Context cxt, String key, String prefNameString, String value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        cxt.getContentResolver().update(
                Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                        + MutiProcessSharedPrefProvider.SPUT + "/string/" + prefNameString), cv, null, null);
    }

    public static String getString(Context cxt, String key, String prefName, String defValue) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SGET + "/string/" + prefName);
        Cursor cursor = cxt.getContentResolver().query(uri, null, key, null, String.valueOf(defValue));
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return defValue;
    }

    public static void putBoolean(Context cxt, String key, String prefNameString, boolean value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        cxt.getContentResolver().update(
                Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                        + MutiProcessSharedPrefProvider.SPUT + "/boolean/" + prefNameString), cv, null, null);
    }

    public static boolean getBoolean(Context cxt, String key, String prefName, boolean defValue) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SGET + "/boolean/" + prefName);
        Cursor cursor = cxt.getContentResolver().query(uri, null, key, null, String.valueOf(defValue));
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Boolean.valueOf(cursor.getString(0));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return defValue;
    }

    public static void putFloat(Context cxt, String key, String prefNameString, float value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        cxt.getContentResolver().update(
                Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                        + MutiProcessSharedPrefProvider.SPUT + "/float/" + prefNameString), cv, null, null);
    }

    public static float getFloat(Context cxt, String key, String prefName, float defValue) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SGET + "/float/" + prefName);
        Cursor cursor = cxt.getContentResolver().query(uri, null, key, null, String.valueOf(defValue));
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getFloat(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return defValue;
    }

    public static void clear(Context cxt, String prefName, String key) {
        Uri uri = Uri.parse("content://" + MutiProcessSharedPrefProvider.sAuthority + "/"
                + MutiProcessSharedPrefProvider.SCLEAR + "/" + prefName);
        cxt.getContentResolver().delete(uri, key, null);
    }
}
