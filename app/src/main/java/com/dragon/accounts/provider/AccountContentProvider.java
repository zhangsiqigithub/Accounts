package com.dragon.accounts.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.model.accountbook.info.AccountAddBookInfo;
import com.dragon.accounts.model.accountbook.info.AccountBookInfo;
import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;
import com.dragon.accounts.util.LogUtil;
import com.dragon.accounts.util.sp.SharedPref;

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
        uriMatcher.addURI(IProivderMetaData.AUTHORITY_ACCOUNT, IProivderMetaData.AccountColumns.TABLE_NAME, TYPE_ACCOUNT_ITEM);
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

    public static void insertAccountBooks(Context context, String name, int size, int colorPosition) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        int accountBookId = AccountManager.getAccountBookSize(context) + 1;
        AccountManager.setAccountBookSize(context, accountBookId);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID, accountBookId);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_NAME, name);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_SIZE, size);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION, colorPosition);
        context.getContentResolver().insert(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, contentValues);
        log("insertAccountBooks-->" + contentValues);
    }

    public static void insertAccount(Context context, String name, String content, float money, int accountType, int accountBookId) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_BOOK_ID, accountBookId);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_NAME, name);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_CONTENT, content);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_MONEY, money);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_TYPE, accountType);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_DATE, System.currentTimeMillis());
        context.getContentResolver().insert(IProivderMetaData.AccountColumns.URI_ACCOUNT, contentValues);
        log("insertAccount-->" + contentValues);
        switch (accountType) {
            case AccountManager.ACCOUNT_TYPE_REVENUE:
                AccountManager.setTotalRevenue(context, money);
                break;
            case AccountManager.ACCOUNT_TYPE_EXPENSES:
                AccountManager.setTotalExpenses(context, money);
                break;
        }
    }

    public static List<IAccountBookInfo> queryAccountBooks(Context context, IAccountBookInfo.Callback callback) {
        List<IAccountBookInfo> tempList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, null, null, null, null);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns._ID));
            int accountBookId = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID));
            String name = query.getString(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_NAME));
            int colorPosition = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION));
            int size = getAccountsCount(context, accountBookId);

//            log("queryAccountBooks-->id:" + id + " accountBookId:" + accountBookId + " name:" + name + " size:" + size + " colorPosition:" + colorPosition);

            AccountBookInfo info = new AccountBookInfo();
            info.id = id;
            info.accountBookId = accountBookId;
            info.name = name;
            info.size = size;
            info.colorPosition = colorPosition;
            info.callback = callback;
            tempList.add(info);
        }

        AccountAddBookInfo addBookInfo = new AccountAddBookInfo();
        addBookInfo.callback = callback;
        tempList.add(addBookInfo);
        return tempList;
    }

    public static List<AccountInfo> queryAccounts(Context context, int currentAccountBookId) {
        List<AccountInfo> tempList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(
                IProivderMetaData.AccountColumns.URI_ACCOUNT,
                null,
                IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_BOOK_ID + "=?",
                new String[]{String.valueOf(currentAccountBookId)},
                IProivderMetaData.AccountColumns.COLUMNS_DATE);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountColumns._ID));
            long accountBookId = query.getLong(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_BOOK_ID));
            String title = query.getString(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_NAME));
            String content = query.getString(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_CONTENT));
            float money = query.getFloat(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_MONEY));
            int accountType = query.getInt(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_TYPE));
            long date = query.getLong(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_DATE));

//            log("queryAccounts-->" +
//                    "id:" + id +
//                    " accountBookId:" + accountBookId +
//                    " title:" + title +
//                    " content:" + content +
//                    " money:" + money +
//                    " accountType:" + accountType +
//                    " date:" + date
//            );
            AccountInfo info = new AccountInfo();
            info.id = id;
            info.title = title;
            info.content = content;
            info.money = money;
            info.accountType = accountType;
            info.date = date;
            tempList.add(info);
        }
        return tempList;
    }

    public static int getAccountsCount(Context context, long accountBookId) {
        Cursor accounts = context.getContentResolver().query(
                IProivderMetaData.AccountColumns.URI_ACCOUNT,
                null,
                IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + "=?",
                new String[]{String.valueOf(accountBookId)},
                null);
        int size = 0;
        if (accounts != null) {
            size = accounts.getCount();
        }
        return size;
    }

    public static int getAccountBookCount(Context context) {
        Cursor query = context.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, null, null, null, null);
        if (query != null) {
            return query.getCount();
        }
        return 0;
    }

    private static void log(String log) {
        LogUtil.d("AccountContentProvider-->" + log);
    }
}
