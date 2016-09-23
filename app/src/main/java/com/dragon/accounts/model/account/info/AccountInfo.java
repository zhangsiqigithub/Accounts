package com.dragon.accounts.model.account.info;

public class AccountInfo implements IAccountInfo{

    public int id;
    public String name;
    public String content;
    public float money;
    public int type;
    public long date;

    @Override
    public int getType() {
        return TYPE_ACCOUNT;
    }
}
