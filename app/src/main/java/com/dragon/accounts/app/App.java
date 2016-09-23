package com.dragon.accounts.app;

import android.app.Application;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.provider.AccountContentProvider;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initTestData();
    }

    private void initTestData() {
        if (AccountContentProvider.getAccountBookCount(getApplicationContext()) == 0) {
            AccountContentProvider.insertAccountBooks(getApplicationContext(), getString(R.string.string_account_book_default_title), 0, 0);
            AccountManager.setCurrentAccountBookId(getApplicationContext(), AccountManager.DEFAULT_ACCOUNT_BOOK_ID);
        }
    }
}
