package com.dragon.accounts;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AccountingWriteCoutentActivity extends Activity implements View.OnClickListener {

    public static void start(Activity activity, Intent intent, int requestCode) {
        if (activity == null)
            return;
        if (intent == null) {
            intent = new Intent();
        }
        intent.setClass(activity, AccountingWriteCoutentActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String EXTRA_CONTENT = "extra_content";

    private EditText accounting_write_content_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_write_content);

        initView();

    }

    private void initView() {
        findViewById(R.id.accounting_write_content_back_btn).setOnClickListener(this);
        findViewById(R.id.accounting_write_content_finish).setOnClickListener(this);
        accounting_write_content_edittext = (EditText) findViewById(R.id.accounting_write_content_edittext);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accounting_write_content_back_btn:
                finish();
                break;
            case R.id.accounting_write_content_finish:
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CONTENT, accounting_write_content_edittext.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
