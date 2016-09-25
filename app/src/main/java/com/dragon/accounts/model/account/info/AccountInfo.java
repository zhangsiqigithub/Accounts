package com.dragon.accounts.model.account.info;

public class AccountInfo implements IAccountInfo {

    public static final int ACCOUNT_TYPE_REVENUE = 1;
    public static final int ACCOUNT_TYPE_EXPENSES = -1;

    public int id;
    public String name;// 账单名称
    public String content;// 账单描述
    public float money;// 钱
    public int type;// View Type 账单/时间
    public int accountType;// 账单类型：1、收入 -1、支出
    public long date;

    @Override
    public int getType() {
        return TYPE_ACCOUNT;
    }
}
