package com.dragon.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends Activity implements View.OnClickListener {

    public static void start(Context context) {
        if (context == null)
            return;
        Intent intent = new Intent(context, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        findViewById(R.id.setting_btn_back).setOnClickListener(this);
        findViewById(R.id.setting_layout_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_btn_back:
                finish();
                break;
            case R.id.setting_layout_about:
                AboutActivity.start(this);
                break;
        }
    }
}
