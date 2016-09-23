package com.dragon.accounts.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dragon.accounts.model.AccountAddBookInfo;
import com.dragon.accounts.model.AccountBookInfo;
import com.dragon.accounts.model.IAccountBook;
import com.dragon.accounts.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountContentProvider extends ContentProvider {

    private static final int TYPE_ACCOUNT = 1;
    private static final int TYPE_ACCOUNT_ITEM = 2;

    private static UriMatcher uriMatcher;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountBookColumns.TABLE_NAME, TYPE_ACCOUNT);
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountColumns.TABLE_NAME + "/#", TYPE_ACCOUNT_ITEM);
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
            case TYPE_ACCOUNT:
                type = IProivderMetaData.AccountBookColumns.CONTENT_TYPE;
                break;
            case TYPE_ACCOUNT_ITEM:
                type = IProivderMetaData.AccountColumns.CONTENT_TYPE_ITEM;
                break;
        }
        return type;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case TYPE_ACCOUNT:
                tableName = IProivderMetaData.AccountBookColumns.TABLE_NAME;
                break;
            case TYPE_ACCOUNT_ITEM:
                tableName = IProivderMetaData.AccountColumns.TABLE_NAME;
                break;
        }
        return tableName;
    }

    public static void insertAccounts(Context context, String name, int size, int colorPosition) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_NAME, name);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_SIZE, size);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION, colorPosition);
        context.getContentResolver().insert(IProivderMetaData.AccountBookColumns.URI_ACCOUNT, contentValues);
        log("insertAccounts-->" + name);
    }

    public static List<IAccountBook> queryAccounts(Context context, IAccountBook.Callback callback) {
        List<IAccountBook> tempList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT, null, null, null, null);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_ID));
            String name = query.getString(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_NAME));
            int size = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_SIZE));
            int colorPosition = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION));

            AccountBookInfo info = new AccountBookInfo();
            info.id = id;
            info.title = name;
            info.size = size;
            info.colorPosition = colorPosition;
            info.callback = callback;
            tempList.add(info);
        }

        AccountAddBookInfo addBookInfo = new AccountAddBookInfo();
        addBookInfo.callback = callback;
        tempList.add(addBookInfo);
        log("queryAccounts-->" + tempList.size());
        return tempList;
    }

    private static void log(String log) {
        LogUtil.d("AccountContentProvider-->" + log);
    }
}
