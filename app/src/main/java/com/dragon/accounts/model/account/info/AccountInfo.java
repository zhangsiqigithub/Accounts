package com.dragon.accounts.model.account.info;

public class AccountInfo implements IAccountInfo {

    public int id;
    public String title;// 账单名称
    public String content;// 账单描述
    public double money;// 钱
    public int accountType;// 账单类型：1、收入 -1、支出
    public long date;
    public int accountIconId;

    @Override
    public int getType() {
        return TYPE_ACCOUNT;
    }
}
