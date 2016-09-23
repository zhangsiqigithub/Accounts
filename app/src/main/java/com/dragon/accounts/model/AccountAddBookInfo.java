package com.dragon.accounts.model;

public class AccountAddBookInfo implements IAccountBook {

    public Callback callback;

    @Override
    public int getType() {
        return TYPE_ACCOUNT_ADD;
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
