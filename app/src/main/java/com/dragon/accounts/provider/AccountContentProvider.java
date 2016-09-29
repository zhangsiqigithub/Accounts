package com.dragon.accounts.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class AccountContentProvider extends ContentProvider {

    private static final int TYPE_ACCOUNT_BOOK = 1;
    private static final int TYPE_ACCOUNT = 2;
    private static final int TYPE_ACCOUNT_ICON = 3;

    private static UriMatcher uriMatcher;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountBookColumns.TABLE_NAME, TYPE_ACCOUNT_BOOK);
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountColumns.TABLE_NAME, TYPE_ACCOUNT);
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountIconColumns.TABLE_NAME, TYPE_ACCOUNT_ICON);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        String tableName = getTableName(uri);
        if (db != null && tableName != null) {
            return db
                    .query(tableName, projection, selection, selectionArgs, null, null,
                            sortOrder);
        } else {
            // 不能识别uri
            throw new IllegalArgumentException("This is a unKnow Uri"
                    + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getReadableDatabase();
        String tableName = getTableName(uri);
        if (db != null && tableName != null) {
            long rowId = db.insert(tableName, null, values);
            if (rowId > 0) {//判断插入是否执行成功
                Uri insertUri = Uri.withAppendedPath(uri, "/" + rowId);
                // 发出数据变化通知(表的数据发生变化)
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            }
            return uri;
        } else {
            // 不能识别uri
            throw new IllegalArgumentException("This is a unKnow Uri"
                    + uri.toString());
        }

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getReadableDatabase();
        String tableName = getTableName(uri);
        if (db != null && tableName != null) {
            return db.delete(tableName, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getReadableDatabase();
        String tableName = getTableName(uri);
        if (db != null && tableName != null) {
            return db.update(tableName, values, selection, selectionArgs);
        }
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = null;
        switch (uriMatcher.match(uri)) {
            case TYPE_ACCOUNT_BOOK:
                type = IProivderMetaData.AccountBookColumns.CONTENT_BOOK;
                break;
            case TYPE_ACCOUNT:
                type = IProivderMetaData.AccountColumns.CONTENT;
                break;
            case TYPE_ACCOUNT_ICON:
                type = IProivderMetaData.AccountIconColumns.CONTENT_ICON;
                break;
        }
        return type;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case TYPE_ACCOUNT_BOOK:
                tableName = IProivderMetaData.AccountBookColumns.TABLE_NAME;
                break;
            case TYPE_ACCOUNT:
                tableName = IProivderMetaData.AccountColumns.TABLE_NAME;
                break;
            case TYPE_ACCOUNT_ICON:
                tableName = IProivderMetaData.AccountIconColumns.TABLE_NAME;
                break;
        }
        return tableName;
    }
}
