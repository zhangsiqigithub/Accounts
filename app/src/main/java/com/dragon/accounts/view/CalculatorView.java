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
import java.util.List;

public class CalculatorView extends LinearLayout implements View.OnClickListener {

    public interface CalculatorViewCallback {
        void onUpdate(float result);

        void onFinish(float result);
    }

    private CalculatorViewCallback mCalculatorViewCallback;
    private boolean isAdd;
    private boolean isReduce;

    private LinkedList<String> mList = new LinkedList<>();
    private LinkedList<String> mTempList = new LinkedList<>();

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
        mList.clear();
        mTempList.clear();
        isAdd = false;
        isReduce = false;
        calculator_text_ok.setText("OK");
    }

    @Override
    public void onClick(View v) {
        List<String> list;
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
                if (isAdd || isReduce) {
                    list = mTempList;
                } else {
                    list = mList;
                }
                list.add(text);
                update();
                break;
            case R.id.calculator_text_point:
                if (isAdd || isReduce) {
                    list = mTempList;
                } else {
                    list = mList;
                }
                if (!list.isEmpty() && !list.contains(text)) {
                    list.add(text);
                }
                update();
                break;
            case R.id.calculator_text_clear:
                reset();
                update();
                break;
            case R.id.calculator_text_add:
                update();
                isAdd = true;
                calculator_text_ok.setText("=");
                break;
            case R.id.calculator_text_reduce:
                update();
                isReduce = true;
                calculator_text_ok.setText("=");
                break;
            case R.id.calculator_text_x:
                if (isAdd || isReduce) {
                    if (!mTempList.isEmpty()) {
                        mTempList.removeLast();
                    }
                } else {
                    if (!mList.isEmpty()) {
                        mList.removeLast();
                    }
                }
                update();
                break;
            case R.id.calculator_text_ok:
                if (isAdd || isReduce) {
                    String mListText = getMListText();
                    float fList = AccountUtil.getAccountFloatMoney(Float.valueOf(mListText));

                    String mTempListText = getTempListText();
                    float fTempList = AccountUtil.getAccountFloatMoney(Float.valueOf(mTempListText));

                    float result;
                    if (isAdd) {
                        result = fList + fTempList;
                    } else {
                        result = fList - fTempList;
                    }

                    reset();
                    char[] charArray = String.valueOf(result).toCharArray();
                    for (char c : charArray) {
                        mList.add(String.valueOf(c));
                    }

                    if (mCalculatorViewCallback != null) {
                        mCalculatorViewCallback.onUpdate(result);
                    }
                } else {
                    if (mCalculatorViewCallback != null) {
                        mCalculatorViewCallback.onFinish(getResultFloat());
                    }
                }
                break;
        }
    }

    private String getMListText() {
        StringBuilder sb = new StringBuilder();
        for (String str : mList) {
            sb.append(str);
        }
        return sb.toString();
    }

    private String getTempListText() {
        StringBuilder sb = new StringBuilder();
        for (String str : mTempList) {
            sb.append(str);
        }
        return sb.toString();
    }

    private void update() {
        if (mCalculatorViewCallback != null) {
            mCalculatorViewCallback.onUpdate(getResultFloat());
        }
    }

    private float getResultFloat() {
        List<String> list;
        if (isAdd || isReduce) {
            list = mTempList;
        } else {
            list = this.mList;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str);
        }
        String s = sb.toString();
        return TextUtils.isEmpty(s) ? 0 : AccountUtil.getAccountFloatMoney(Float.valueOf(s));
    }

}
