package com.dragon.accounts;

import android.app.Activity;

import com.dragon.accounts.statistics.Reporter;

public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        Reporter.onResume(this, getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Reporter.onPause(this, getClass().getSimpleName());
    }
}
