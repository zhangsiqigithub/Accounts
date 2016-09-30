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
    public static final int ICON_ID_COMMON = R.mipmap.icon_star_a8b615;// 一般

    public static void init(Context context) {
        AccountIconManager.insertAccountIcon(context, ICON_ID_COMMON, getIconNameIdByIconId(context, ICON_ID_COMMON));
    }

    /**
     * 根据IconId获取名字，仅限本类内部使用，外部获取必须通过数据库获取
     */
    private static String getIconNameIdByIconId(Context context, int iconId) {
        String name = context.getString(R.string.string_icon_id_common);
        if (context != null) {
            switch (iconId) {
                case ICON_ID_COMMON:
                    name = context.getString(R.string.string_icon_id_common);
                    break;
            }
        }
        return name;
    }

    public static int getTitleIconIdByIconId(int iconId) {
        switch (iconId) {
            case ICON_ID_COMMON:
                return R.mipmap.icon_star_white;
        }
        return 0;
    }

    public static void insertAccountIcon(Context context, int iconId, String name) {
        if (context == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID, iconId);
        contentValues.put(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_NAME, name);
        context.getContentResolver().insert(IProivderMetaData.AccountIconColumns.URI_ACCOUNT_ICON, contentValues);
    }

    public static List<AccountIconInfo> queryAllAccountIcons(Context context, AccountIconInfo.AccountingCallback callback) {
        List<AccountIconInfo> list = new ArrayList<>();
        Cursor query = context.getContentResolver().query(
                IProivderMetaData.AccountIconColumns.URI_ACCOUNT_ICON, null, null, null, null);
        while (query != null && query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(IProivderMetaData.AccountIconColumns._ID));
            int iconId = query.getInt(query.getColumnIndex(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_ID));
            String iconName = query.getString(query.getColumnIndex(IProivderMetaData.AccountIconColumns.COLUMNS_ICON_NAME));
            list.add(new AccountIconInfo(id, iconId, iconName, callback));
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
