package com.dragon.accounts.model.account.info;

public class AccountDateInfo implements IAccountInfo {

    public String date;
    public double money;

    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
