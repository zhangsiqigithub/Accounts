package com.dragon.accounts.model.accountbook.info;

public class AccountBookInfo implements IAccountBookInfo {

    public int id;
    public int accountBookId;
    public String name;// 账本名字
    public int size;// 帐目数量
    public int colorPosition;// 颜色id

    public Callback callback;

    @Override
    public int getType() {
        return TYPE_ACCOUNT_BOOK;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public Callback getCallback() {
        return callback;
    }
}
