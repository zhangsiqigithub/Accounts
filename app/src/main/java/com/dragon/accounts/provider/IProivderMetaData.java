package com.dragon.accounts.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public interface IProivderMetaData {

    // 定义外部访问的Authority
    String AUTHORITY_ACCOUNT = "com.dragon.accounts.provider";

    /**
     * 数据库名称
     */
    String DB_NAME = "accounts.db";// 账本

    /**
     * 数据库版本
     */
    int VERSION = 2;

    /**
     * 账本
     */
    interface AccountBookColumns extends BaseColumns {

        // 表名
        String TABLE_NAME = "accountBooks";

        // 外部程序访问本表的uri地址：登录
        Uri URI_ACCOUNT = Uri.parse("content://" + AUTHORITY_ACCOUNT + "/" + TABLE_NAME);

        // 表列名
        String COLUMNS_ACCOUNT_BOOK_ID = "account_book_id";// 这条记录所在的账本id
        String COLUMNS_NAME = "name";// 账本名称
        String COLUMNS_SIZE = "size";// 账目数量
        String COLUMNS_COLOR_POSITION = "color_position";// 账本颜色id

        //该ContentProvider所返回的数据类型的定义
        String CONTENT_TYPE = "vnd.android.cursor.dir/account_books";
    }

    /**
     * 每一条帐目记录
     */
    interface AccountColumns extends BaseColumns {

        // 表名
        String TABLE_NAME = "accounts";

        // 外部程序访问本表的uri地址：登录
        Uri URI_ACCOUNT = Uri.parse("content://" + AUTHORITY_ACCOUNT + "/" + TABLE_NAME);

        // 表列名
        String COLUMNS_ACCOUNT_BOOK_ID = "account_book_id";// 这条记录所在的账本id
        String COLUMNS_NAME = "name";// 标题
        String COLUMNS_CONTENT = "content";// 内容
        String COLUMNS_MONEY = "money";// 钱
        String COLUMNS_TYPE = "type";// 流动类型：收入/支出
        String COLUMNS_DATE = "date";// 日期

        //该ContentProvider所返回的数据类型的定义
        String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/accounts";
    }

}
