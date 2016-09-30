package com.dragon.accounts.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.accounts.R;
import com.dragon.accounts.util.AccountUtil;

import java.util.LinkedList;

public class CalculatorView extends LinearLayout implements View.OnClickListener {

    public interface CalculatorViewCallback {
        void onUpdate(String result);

        void onFinish(double result);
    }

    private CalculatorViewCallback mCalculatorViewCallback;

    private LinkedList<String> mIntegerList = new LinkedList<>();
    private LinkedList<String> mDecimalList = new LinkedList<>();
    private boolean isAdd;
    private boolean isReduce;
    private boolean isPointClick;
    private String mStoreText;

    private TextView calculator_text_ok;

    public CalculatorView(Context context) {
        super(context);
        init(context);
    }

    public CalculatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalculatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_calculator, this);
        initView();
    }

    private void initView() {
        findViewById(R.id.calculator_text_1).setOnClickListener(this);
        findViewById(R.id.calculator_text_2).setOnClickListener(this);
        findViewById(R.id.calculator_text_3).setOnClickListener(this);
        findViewById(R.id.calculator_text_4).setOnClickListener(this);
        findViewById(R.id.calculator_text_5).setOnClickListener(this);
        findViewById(R.id.calculator_text_6).setOnClickListener(this);
        findViewById(R.id.calculator_text_7).setOnClickListener(this);
        findViewById(R.id.calculator_text_8).setOnClickListener(this);
        findViewById(R.id.calculator_text_9).setOnClickListener(this);
        findViewById(R.id.calculator_text_0).setOnClickListener(this);
        findViewById(R.id.calculator_text_clear).setOnClickListener(this);
        findViewById(R.id.calculator_text_point).setOnClickListener(this);
        findViewById(R.id.calculator_text_add).setOnClickListener(this);
        findViewById(R.id.calculator_text_reduce).setOnClickListener(this);
        findViewById(R.id.calculator_text_x).setOnClickListener(this);
        calculator_text_ok = (TextView) findViewById(R.id.calculator_text_ok);
        calculator_text_ok.setOnClickListener(this);
    }

    public void setCalculatorViewCallback(CalculatorViewCallback calculatorViewCallback) {
        this.mCalculatorViewCallback = calculatorViewCallback;
    }

    public void reset() {
        mIntegerList.clear();
        mDecimalList.clear();
        isAdd = false;
        isReduce = false;
        isPointClick = false;
        calculator_text_ok.setText("OK");
        mStoreText = null;
        update();
    }

    @Override
    public void onClick(View v) {
        LinkedList<String> list;
        TextView textView = (TextView) v;
        String text = textView.getText().toString();
        switch (v.getId()) {
            case R.id.calculator_text_1:
            case R.id.calculator_text_2:
            case R.id.calculator_text_3:
            case R.id.calculator_text_4:
            case R.id.calculator_text_5:
            case R.id.calculator_text_6:
            case R.id.calculator_text_7:
            case R.id.calculator_text_8:
            case R.id.calculator_text_9:
            case R.id.calculator_text_0:
                if (isPointClick) {
                    list = mDecimalList;
                } else {
                    list = mIntegerList;
                }
                if (isPointClick) {
                    if (list.size() < 2) {
                        list.add(text);
                    }
                } else {
                    list.add(text);
                }
                update();
                break;
            case R.id.calculator_text_point:
                isPointClick = true;
                break;
            case R.id.calculator_text_clear:
                reset();
                break;
            case R.id.calculator_text_add:
                if (TextUtils.isEmpty(mStoreText)) {
                    mStoreText = getListResult();
                }
                mIntegerList.clear();
                mDecimalList.clear();
                isPointClick = false;
                isAdd = true;
                isReduce = false;
                calculator_text_ok.setText("=");
                break;
            case R.id.calculator_text_reduce:
                if (TextUtils.isEmpty(mStoreText)) {
                    mStoreText = getListResult();
                }
                mIntegerList.clear();
                mDecimalList.clear();
                isPointClick = false;
                isReduce = true;
                isAdd = false;
                calculator_text_ok.setText("=");
                break;
            case R.id.calculator_text_x:
                if (isAdd || isReduce) {
                    isAdd = false;
                    isReduce = false;
                    resetList(mStoreText);
                }

                if (isPointClick) {
                    list = mDecimalList;
                } else {
                    list = mIntegerList;
                }
                if (!list.isEmpty()) {
                    list.removeLast();
                }

                update();
                break;
            case R.id.calculator_text_ok:
                if (isAdd || isReduce) {
                    double d1 = Double.valueOf(mStoreText);
                    String string2 = getListResult();
                    double d2 = TextUtils.isEmpty(string2) ? 0 : Double.valueOf(string2);
                    String result = AccountUtil.getAccountMoney(isAdd ? (d1 + d2) : ((d1 - d2) <= 0 ? 0 : (d1 - d2)));

                    if (mCalculatorViewCallback != null) {
                        mCalculatorViewCallback.onUpdate(result);
                    }
                    isAdd = false;
                    isReduce = false;
                    mStoreText = null;

                    resetList(result);
                    calculator_text_ok.setText("OK");
                } else {
                    finish();
                }
                break;
        }
    }

    private void resetList(String text) {
        if (text == null)
            return;
        mIntegerList.clear();
        mDecimalList.clear();
        boolean isAfter = false;
        char[] charArray = text.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '.') {
                isAfter = true;
                continue;
            }
            if (isAfter) {
                mDecimalList.add(String.valueOf(c));
            } else {
                mIntegerList.add(String.valueOf(c));
            }
        }
    }

    private String getListText(String string) {
        return TextUtils.isEmpty(string) ? "0" : string.replaceAll(",", "");
    }

    private String getIntegerListText() {
        StringBuilder sb = new StringBuilder();
        for (String str : mIntegerList) {
            sb.append(str);
        }
        return getListText(sb.toString());
    }

    private String getdecimalListText() {
        StringBuilder sb = new StringBuilder();
        for (String str : mDecimalList) {
            sb.append(str);
        }
        return getListText(sb.toString());
    }

    private void update() {
        if (mCalculatorViewCallback != null) {
            mCalculatorViewCallback.onUpdate(getCurrentResult());
        }
    }

    private String getListResult() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIntegerListText());
        if (isPointClick) {
            sb.append(".");
            sb.append(getdecimalListText());
        }
        return sb.toString();
    }

    private String getCurrentResult() {
        String result = getListResult();
        double d = Double.valueOf(result);
        result = AccountUtil.getAccountMoney(TextUtils.isEmpty(result) ? 0 : d);
        return result;
    }

    private void finish() {
        String result = getListResult();
        double d = Double.valueOf(result);
        if (mCalculatorViewCallback != null) {
            mCalculatorViewCallback.onFinish(d);
        }
    }

}
