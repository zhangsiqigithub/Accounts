package com.dragon.accounts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dragon.accounts.R;
import com.dragon.accounts.model.accounting.info.AccountIconInfo;
import com.dragon.accounts.provider.IProivderMetaData;

import java.util.ArrayList;
import java.util.List;

public class AccountIconManager {

    public static final int ICON_ID_ADD = 0;// 添加

    public static final int[][] ICON_ARRAY_EXPENSES = {
            {R.mipmap.icon_star, R.string.string_icon_id_common},
            {R.mipmap.icon_food, R.string.string_icon_id_food},
            {R.mipmap.icon_bus, R.string.string_icon_id_bus},
            {R.mipmap.icon_snacks, R.string.string_icon_id_snacks},
            {R.mipmap.icon_traval, R.string.string_icon_id_traval},
            {R.mipmap.icon_redecorate, R.string.string_icon_id_redecorate}
    };

    public static final int[][] ICON_ARRAY_REVENUE = {
            {R.mipmap.icon_revenue, R.string.string_icon_id_common}
    };

    public static void init(Context context) {
        for (int[] id : ICON_ARRAY_EXPENSES) {
            insertAccountIcon(context, id[0], context.getString(id[1]), AccountManager.ACCOUNT_TYPE_EXPENSES);
        }
        for (int[] id : ICON_ARRAY_REVENUE) {
            insertAccountIcon(context, id[0], context.getString(id[1]), AccountManager.ACCOUNT_TYPE_REVENUE);
        }
    }

    public static void insertAccountIcon(Context context, int iconId, String name, int iconType) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID, iconId);
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_NAME, name);
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_TYPE, iconType);
        context.getContentResolver().insert(IProivderMetaData.AccountIconColumns.URI_ACCOUNT_ICON, contentValues);
    }

    public static List<AccountIconInfo> queryAllAccountIcons(Context context, int accountType, AccountIconInfo.AccountingCallback callback) {
        List<AccountIconInfo> list = new ArrayList<>();
        Cursor query = context.getContentResolver().query(
                IProivderMetaData.AccountIconColumns.URI_ACCOUNT_ICON, null,
                IProivderMetaData.AccountIconColumns.COLUMNS_ICON_TYPE + "=?",
                new String[]{String.valueOf(accountType)}, null);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountIconColumns._ID));
            int iconId = query.getInt(query.getColumnIndex(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID));
            String iconName = query.getString(query.getColumnIndex(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_NAME));
            list.add(new AccountIconInfo(id, iconId, iconName, accountType, callback));
        }
        return list;
    }

    public static int getAccountIconCount(Context context) {
        Cursor cursor = context.getContentResolver().query(
                IProivderMetaData.AccountIconColumns.URI_ACCOUNT_ICON, null, null, null, null);
        int size = 0;
        if (cursor != null) {
            size = cursor.getCount();
        }
        return size;
    }

}
