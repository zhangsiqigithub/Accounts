package com.dragon.accounts.app;

import android.app.Application;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountBookManager;
import com.dragon.accounts.model.AccountIconManager;
import com.dragon.accounts.util.sp.SharedPref;

public class App extends Application {

    private static final String KEY_IS_INIT_ACCOUNT_BOOK = "key_is_init_account_book";
    private static final String KEY_IS_INIT_ACCOUNT_ICON = "key_is_init_account_icon";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        if (!SharedPref.getBoolean(getApplicationContext(), KEY_IS_INIT_ACCOUNT_BOOK, false)) {
            AccountBookManager.insertAccountBook(getApplicationContext(), getString(R.string.string_account_book_default_title), 0, 0);
            AccountBookManager.setCurrentAccountBookId(getApplicationContext(), AccountBookManager.DEFAULT_ACCOUNT_BOOK_ID);
            SharedPref.setBoolean(getApplicationContext(), KEY_IS_INIT_ACCOUNT_BOOK, true);
        }
        if (!SharedPref.getBoolean(getApplicationContext(), KEY_IS_INIT_ACCOUNT_ICON, false)) {
            AccountIconManager.init(getApplicationContext());
            SharedPref.setBoolean(getApplicationContext(), KEY_IS_INIT_ACCOUNT_ICON, true);
        }
    }

}
