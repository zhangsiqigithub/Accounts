package com.dragon.accounts.model;

import android.content.Context;

import com.dragon.accounts.util.sp.SharedPref;

public class AccountManager {

    public static final int ACCOUNT_TYPE_REVENUE = 1;// 帐目类型：收入
    public static final int ACCOUNT_TYPE_EXPENSES = 2;// 帐目类型：支出

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
}
