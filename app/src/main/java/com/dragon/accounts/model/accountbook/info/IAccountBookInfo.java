package com.dragon.accounts.model.accountbook.info;

public interface IAccountBookInfo {

    interface Callback {
        void onListItemClick(int type, int position);
    }

    /**
     * 普通账本
     */
    int TYPE_ACCOUNT_BOOK = 1;

    /**
     * 添加帐目的条目
     */
    int TYPE_ACCOUNT_ADD = 2;

    int getType();

    void setCallback(Callback callback);

    Callback getCallback();

}
