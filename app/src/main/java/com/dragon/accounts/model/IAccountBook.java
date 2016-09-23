package com.dragon.accounts.model;

/**
 * Created by yeguolong on 16-9-22.
 */
public interface IAccountBook {

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
