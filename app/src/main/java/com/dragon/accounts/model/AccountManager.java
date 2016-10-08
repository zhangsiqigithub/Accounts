package com.dragon.accounts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.provider.IProivderMetaData;
import com.dragon.accounts.util.sp.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    public static final int ACCOUNT_TYPE_REVENUE = 1;// 帐目类型：收入
    public static final int ACCOUNT_TYPE_EXPENSES = 2;// 帐目类型：支出

    private static final String KEY_TOTAL_REVENUE = "key_total_revenue";
    private static final String KEY_TOTAL_EXPENSES = "key_total_expenses";

    public static void setTotalRevenue(Context context, double money) {
        SharedPref.setString(context, KEY_TOTAL_REVENUE, String.valueOf(getTotalRevenue(context) + money));
    }

    public static double getTotalRevenue(Context context) {
        return Double.valueOf(SharedPref.getString(context, KEY_TOTAL_REVENUE, "0"));
    }

    public static void setTotalExpenses(Context context, double money) {
        SharedPref.setString(context, KEY_TOTAL_EXPENSES, String.valueOf(getTotalExpenses(context) + money));
    }

    public static double getTotalExpenses(Context context) {
        return Double.valueOf(SharedPref.getString(context, KEY_TOTAL_EXPENSES, "0"));
    }

    public static void insertAccount(Context context, String name, int accountIconId, String content, double money, int accountType, int accountBookId, long date) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID, accountBookId);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_NAME, name);
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID, accountIconId);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_CONTENT, content);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_MONEY, money);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_TYPE, accountType);
        contentValues.put(IProivderMetaData.AccountColumns.COLUMNS_DATE, date);
        context.getContentResolver().insert(IProivderMetaData.AccountColumns.URI_ACCOUNT, contentValues);
        switch (accountType) {
            case ACCOUNT_TYPE_REVENUE:
                setTotalRevenue(context, money);
                break;
            case ACCOUNT_TYPE_EXPENSES:
                setTotalExpenses(context, money);
                break;
        }
    }

    public static List<AccountInfo> queryAllAccounts(Context context, int currentAccountBookId) {
        List<AccountInfo> tempList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(
                IProivderMetaData.AccountColumns.URI_ACCOUNT,
                null,
                IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + "=?",
                new String[]{String.valueOf(currentAccountBookId)},
                IProivderMetaData.AccountColumns.COLUMNS_DATE + " DESC");
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountColumns._ID));
            String title = query.getString(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_NAME));
            String content = query.getString(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_CONTENT));
            double money = query.getDouble(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_MONEY));
            int accountType = query.getInt(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_ACCOUNT_TYPE));
            int accountIconId = query.getInt(query.getColumnIndex(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID));
            long date = query.getLong(query.getColumnIndex(IProivderMetaData.AccountColumns.COLUMNS_DATE));

            AccountInfo info = new AccountInfo();
            info.id = id;
            info.title = title;
            info.content = content;
            info.money = money;
            info.accountType = accountType;
            info.date = date;
            info.accountIconId = accountIconId;
            tempList.add(info);
        }
        return tempList;
    }

    public static int getAccountsCount(Context context, long accountBookId) {
        Cursor cursor = context.getContentResolver().query(
                IProivderMetaData.AccountColumns.URI_ACCOUNT,
                null,
                IProivderMetaData.AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + "=?",
                new String[]{String.valueOf(accountBookId)},
                null);
        int size = 0;
        if (cursor != null) {
            size = cursor.getCount();
        }
        return size;
    }
}
