package com.dragon.accounts.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dragon.accounts.util.LogUtil;

public class DbHelper extends SQLiteOpenHelper implements IProivderMetaData {

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLESQL_ACCOUNT_BOOK = "create table if not exists "
                + AccountBookColumns.TABLE_NAME + " ("
                + AccountBookColumns._ID + " integer primary key,"
                + AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + " integer,"
                + AccountBookColumns.COLUMNS_NAME + " varchar,"
                + AccountBookColumns.COLUMNS_SIZE + " integer,"
                + AccountBookColumns.COLUMNS_COLOR_POSITION + " integer"
                + ")";
        db.execSQL(TABLESQL_ACCOUNT_BOOK);

        String TABLESQL_ACCOUNT = "create table if not exists "
                + AccountColumns.TABLE_NAME + " ("
                + AccountColumns._ID + " integer primary key,"
                + AccountBookColumns.COLUMNS_ACCOUNT_BOOK_ID + " integer,"
                + AccountColumns.COLUMNS_NAME + " varchar,"
                + AccountIconColumns.COLUMNS_ICON_ID + " integer,"
                + AccountColumns.COLUMNS_CONTENT + " varchar,"
                + AccountColumns.COLUMNS_MONEY + " double,"
                + AccountColumns.COLUMNS_ACCOUNT_TYPE + " integer,"
                + AccountColumns.COLUMNS_DATE + " long"
                + ")";
        db.execSQL(TABLESQL_ACCOUNT);

        String TABLESQL_ACCOUNT_ICON = "create table if not exists "
                + AccountIconColumns.TABLE_NAME + " ("
                + AccountIconColumns._ID + " integer primary key,"
                + AccountIconColumns.COLUMNS_ICON_ID + " integer,"
                + AccountIconColumns.COLUMNS_ICON_TYPE + " integer,"
                + AccountIconColumns.COLUMNS_ICON_NAME + " varchar"
                + ")";
        db.execSQL(TABLESQL_ACCOUNT_ICON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d("DbHelper-->onUpgrade-->database from version " + oldVersion
                + " to " + newVersion + ", destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AccountBookColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AccountColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AccountIconColumns.TABLE_NAME);
        onCreate(db);
    }

}