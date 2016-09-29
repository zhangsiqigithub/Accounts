package com.dragon.accounts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dragon.accounts.model.accountbook.info.AccountAddBookInfo;
import com.dragon.accounts.model.accountbook.info.AccountBookInfo;
import com.dragon.accounts.model.accountbook.info.IAccountBookInfo;
import com.dragon.accounts.provider.IProivderMetaData;
import com.dragon.accounts.util.sp.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class AccountBookManager {

    public static final int DEFAULT_ACCOUNT_BOOK_ID = 1;

    private static final String KEY_ACCOUNT_BOOK_SIZE = "key_account_book_id";// 当前已创建过的账本数量
    private static final String KEY_CURRENT_ACCOUNT_BOOK_ID = "key_current_account_book_id";// 当前已创建过的账本数量

    public static int getAccountBookSize(Context context) {
        return SharedPref.getInt(context, KEY_ACCOUNT_BOOK_SIZE, 0);
    }

    public static void setAccountBookSize(Context context, int size) {
        SharedPref.setInt(context, KEY_ACCOUNT_BOOK_SIZE, size);
    }

    public static int getCurrentAccountBookId(Context context) {
        return SharedPref.getInt(context, KEY_CURRENT_ACCOUNT_BOOK_ID, DEFAULT_ACCOUNT_BOOK_ID);
    }

    public static void setCurrentAccountBookId(Context context, int accountBookId) {
        SharedPref.setInt(context, KEY_CURRENT_ACCOUNT_BOOK_ID, accountBookId);
    }

    public static void insertAccountBook(Context context, String name, int size, int colorPosition) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        int accountBookId = getAccountBookSize(context) + 1;
        setAccountBookSize(context, accountBookId);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID, accountBookId);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_NAME, name);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_SIZE, size);
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION, colorPosition);
        context.getContentResolver().insert(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, contentValues);
//        log("insertAccountBook-->" + contentValues);
    }

    public static List<IAccountBookInfo> queryAccountBook(Context context, IAccountBookInfo.Callback callback) {
        List<IAccountBookInfo> tempList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, null, null, null, null);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns._ID));
            int accountBookId = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID));
            String name = query.getString(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_NAME));
            int colorPosition = query.getInt(query.getColumnIndex(IProivderMetaData.AccountBookColumns.COLUMNS_COLOR_POSITION));
            int size = AccountManager.getAccountsCount(context, accountBookId);

//            log("queryAccountBook-->id:" + id + " accountBookId:" + accountBookId + " name:" + name + " size:" + size + " colorPosition:" + colorPosition);

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

    public static int getAccountBookCount(Context context) {
        Cursor query = context.getContentResolver().query(IProivderMetaData.AccountBookColumns.URI_ACCOUNT_BOOK, null, null, null, null);
        if (query != null) {
            return query.getCount();
        }
        return 0;
    }

}
