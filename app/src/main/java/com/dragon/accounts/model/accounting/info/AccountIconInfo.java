package com.dragon.accounts.model.accounting.info;

public class AccountIconInfo {

    public interface AccountingCallback {
        void onClick(int position);
    }

    public int id;
    public int iconId;
    public String name;
    public int iconType;
    public AccountingCallback mAccountingCallback;

    public AccountIconInfo(int id, int iconId, String name, int iconType, AccountingCallback callback) {
        this.id = id;
        this.iconId = iconId;
        this.name = name;
        this.iconType = iconType;
        this.mAccountingCallback = callback;
    }

}
