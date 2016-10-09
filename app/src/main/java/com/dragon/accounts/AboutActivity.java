package com.dragon.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity implements View.OnClickListener {

    public static void start(Context context) {
        if (context == null)
            return;
        Intent intent = new Intent(context, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.about_btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_btn_back:
                finish();
                break;
        }
    }
}
