package com.dragon.accounts.util.sp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MutiProcessSharedPrefProvider extends ContentProvider {

    public static final String sAuthority = "com.dragon.util.sharepref";
    public static final String NAME = "default_shared_prefs";

    static final String SGET = "get";
    static final String SPUT = "put";
    static final String SCLEAR = "clear";
    static final Uri sContentUriPut = Uri.parse("content://" + sAuthority + "/" + SPUT);
    static final Uri sContentUriGet = Uri.parse("content://" + sAuthority + "/" + SGET);

    static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static final int PUT = 100;
    static final int GET = 101;

    static final HashMap<String, Integer> sType = new HashMap<String, Integer>();
    private static final int TYPE_LONG = 200;
    private static final int TYPE_INTEGER = 201;
    private static final int TYPE_STRING = 202;
    private static final int TYPE_BOOLEAN = 203;
    private static final int TYPE_STRINGS = 204;
    private static final int TYPE_FLOAT = 205;

    static {
        sUriMatcher.addURI(sAuthority, "put", PUT);
        sUriMatcher.addURI(sAuthority, "get", GET);
        sType.put("string", TYPE_STRING);
        sType.put("integer", TYPE_INTEGER);
        sType.put("long", TYPE_LONG);
        sType.put("boolean", TYPE_BOOLEAN);
        sType.put("strings", TYPE_STRINGS);
        sType.put("strings", TYPE_FLOAT);
    }

    @Override
    public int delete(Uri arg0, String key, String[] arg2) {
        List<String> path = arg0.getPathSegments();
        if (path.size() < 2) {
            return -1;
        }

        String prefName = path.get(1);
        if (SCLEAR.equals(path.get(0))) {
            SharedPreferences pref = getContext().getSharedPreferences(prefName, 0);
            SharedPreferences.Editor editor = pref.edit();
            if (key == null)
                editor.clear();
            else
                editor.remove(key);
            editor.commit();
        }
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String key, String[] arg3, String defVal) {
        List<String> path = arg0.getPathSegments();
        if (path.size() < 2) {
            return null;
        }
        String op = path.get(0);
        if (sType.get(path.get(1)) == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        String prefName = null;
        if (path.size() >= 3) {
            prefName = path.get(2);
        }

        if (TextUtils.isEmpty(prefName))
            prefName = NAME;
        SharedPreferences pref = getContext().getSharedPreferences(prefName, 0);
        if (pref == null) {
            return null;
        }

        MatrixCursor cursor = new MatrixCursor(new String[]{"value"});

        int type = sType.get(path.get(1));
        if (SGET.equals(op)) {
            switch (type) {
                case TYPE_LONG:
                    cursor.addRow(new Object[]{pref.getLong(key, Long.valueOf(defVal))});
                    break;
                case TYPE_INTEGER:
                    cursor.addRow(new Object[]{pref.getInt(key, Integer.valueOf(defVal))});
                    break;
                case TYPE_BOOLEAN:
                    cursor.addRow(new Object[]{String.valueOf(pref.getBoolean(key, Boolean.valueOf(defVal)))});
                    break;
                case TYPE_STRING:
                    cursor.addRow(new Object[]{pref.getString(key, defVal)});
                    break;
                case TYPE_FLOAT:
                    cursor.addRow(new Object[]{pref.getFloat(key, Float.valueOf(defVal))});
                    break;
                case TYPE_STRINGS:
                    Set<String> strings = pref.getStringSet(key, null);
                    if (strings != null) {
                        for (String s : strings) {
                            cursor.addRow(new Object[]{s});
                        }
                    }
                    break;
            }
        }
        return cursor;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        List<String> path = arg0.getPathSegments();
        if (path.size() < 2) {
            return -1;
        }
        String op = path.get(0);
        if (sType.get(path.get(1)) == null) {
            return -2;
        }
        Set<String> keys = arg1.keySet();
        Iterator<String> iterator = keys.iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
        }

        if (key == null) {
            return -3;
        }
        String prefName = null;
        SharedPreferences.Editor editor = null;
        if (path.size() >= 3) {
            prefName = path.get(2);
        }

        if (TextUtils.isEmpty(prefName))
            prefName = NAME;
        SharedPreferences pref = getContext().getSharedPreferences(prefName, 0);
        if (pref != null) {
            editor = pref.edit();
        } else {
            return -4;
        }

        int type = sType.get(path.get(1));
        if (SPUT.equals(op)) {
            switch (type) {
                case TYPE_LONG:
                    editor.putLong(key, arg1.getAsLong(key));
                    editor.commit();
                    break;
                case TYPE_INTEGER:
                    editor.putInt(key, arg1.getAsInteger(key));
                    editor.commit();
                    break;
                case TYPE_BOOLEAN:
                    editor.putBoolean(key, arg1.getAsBoolean(key));
                    editor.commit();
                    break;
                case TYPE_STRING:
                    editor.putString(key, arg1.getAsString(key));
                    editor.commit();
                    break;
                case TYPE_FLOAT:
                    editor.putFloat(key, arg1.getAsFloat(key));
                    editor.commit();
                    break;
                case TYPE_STRINGS:
                    String[] strings = arg1.getAsString(key).split(",");
                    if (strings != null && strings.length != 0) {
                        editor.putStringSet(key, new HashSet(Arrays.asList(strings)));
                        editor.commit();
                    } else {
                        editor.putStringSet(key, Collections.EMPTY_SET);
                        editor.commit();
                    }
                    break;
            }
        }
        return 0;
    }

}
