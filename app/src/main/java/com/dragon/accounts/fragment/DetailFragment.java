package com.dragon.accounts.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.accounts.R;
import com.dragon.accounts.model.AccountBookManager;
import com.dragon.accounts.model.AccountManager;
import com.dragon.accounts.model.account.info.AccountInfo;
import com.dragon.accounts.model.account.info.AccountTypeGroupInfo;
import com.dragon.accounts.model.accountbook.info.AccountBookInfo;
import com.dragon.accounts.provider.IProivderMetaData;
import com.dragon.accounts.util.LogUtil;
import com.dragon.accounts.util.TimeUtil;
import com.dragon.accounts.view.chart.animation.Easing;
import com.dragon.accounts.view.chart.charts.LineChart;
import com.dragon.accounts.view.chart.charts.PieChart;
import com.dragon.accounts.view.chart.components.AxisBase;
import com.dragon.accounts.view.chart.components.Legend;
import com.dragon.accounts.view.chart.components.XAxis;
import com.dragon.accounts.view.chart.components.YAxis;
import com.dragon.accounts.view.chart.data.Entry;
import com.dragon.accounts.view.chart.data.LineData;
import com.dragon.accounts.view.chart.data.LineDataSet;
import com.dragon.accounts.view.chart.data.PieData;
import com.dragon.accounts.view.chart.data.PieDataSet;
import com.dragon.accounts.view.chart.data.PieEntry;
import com.dragon.accounts.view.chart.formatter.IAxisValueFormatter;
import com.dragon.accounts.view.chart.formatter.PercentFormatter;
import com.dragon.accounts.view.chart.highlight.Highlight;
import com.dragon.accounts.view.chart.interfaces.datasets.ILineDataSet;
import com.dragon.accounts.view.chart.listener.OnChartValueSelectedListener;
import com.dragon.accounts.view.chart.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DetailFragment extends BaseFragment implements OnChartValueSelectedListener {

    private static final int DURATION = 1000;

    private Context mContext;
    private PieChart mPieChart;
    private LineChart mLineChart;

    protected Typeface mTf_percent;
    private PieData mData;
    private LineData mLineData;

    private int mCurrentAccountBookId;
    private int mCurrentAccountType = AccountManager.ACCOUNT_TYPE_EXPENSES;
    private String accountBookName;

    private static final int MSG_UPDATE_PIE_CHART = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_PIE_CHART:
                    // undo all highlights
                    mPieChart.highlightValues(null);
                    mPieChart.setCenterText(accountBookName);
                    mPieChart.invalidate();

                    mLineChart.invalidate();
                    break;
            }
        }
    };

    public DetailFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        mTf_percent = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Semibold.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        initPieChart(view);
        initLineChart(view);
        resetData();
        return view;
    }

    private void initPieChart(View view) {
        mPieChart = (PieChart) view.findViewById(R.id.detail_piechart);

        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterTextTypeface(mTf_percent);
        mPieChart.setCenterText("");
        mPieChart.setCenterTextSize(20);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);// 中间圆轮廓颜色
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(90);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        mPieChart.setOnChartValueSelectedListener(this);

        Legend l = mPieChart.getLegend();// 设置
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);// 每一个区域描述文字的颜色
        mPieChart.setEntryLabelTypeface(mTf_percent);// 每一个区域中描述文字的字体
        mPieChart.setEntryLabelTextSize(12f);

        mPieChart.setRotationEnabled(false);// 不可以滑动旋转
        mPieChart.setTouchEnabled(false);
    }

    private void initLineChart(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.detail_linechart);

        mLineChart.getDescription().setEnabled(false);
        mLineChart.setDrawGridBackground(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf_percent);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String result = null;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd");
                    Date d1 = new Date((long) value);
                    result = format.format(d1);
                } catch (Exception e) {
                }
                LogUtil.d("getFormattedValue-->value:" + value + " result:" + result);
                return result;
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTypeface(mTf_percent);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setTypeface(mTf_percent);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
    }

    private Map<Integer, AccountTypeGroupInfo> mAccountTypeGroupMap = new HashMap<>();
    private List<AccountInfo> mAccountList = new ArrayList<>();
    private boolean anim;

    @Override
    public void resetData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                anim = true;
                mCurrentAccountBookId = AccountBookManager.getCurrentAccountBookId(mContext);
                AccountBookInfo accountBookInfo = AccountBookManager.queryAccountBookByBookId(mContext, mCurrentAccountBookId);
                accountBookName = accountBookInfo.name;
                double totalMoney = 0;
                List<AccountInfo> accountList = AccountManager.queryAccounts(mContext, mCurrentAccountBookId, mCurrentAccountType,
                        IProivderMetaData.AccountColumns.COLUMNS_MONEY + " DESC");
                mAccountList.clear();
                mAccountList = accountList;

                if (!accountList.isEmpty()) {
                    mAccountTypeGroupMap.clear();

                    ArrayList<Entry> e1 = new ArrayList<>();

                    for (AccountInfo info : accountList) {
                        if (info == null)
                            continue;
                        AccountTypeGroupInfo accountTypeGroupInfo;
                        if (mAccountTypeGroupMap.containsKey(info.accountIconId)) {
                            accountTypeGroupInfo = mAccountTypeGroupMap.get(info.accountIconId);
                        } else {
                            accountTypeGroupInfo = new AccountTypeGroupInfo();
                            mAccountTypeGroupMap.put(info.accountIconId, accountTypeGroupInfo);
                        }
                        totalMoney += info.money;
                        accountTypeGroupInfo.accountTypeId = info.accountIconId;
                        accountTypeGroupInfo.accountTypeName = info.title;
                        accountTypeGroupInfo.accountSize++;
                        accountTypeGroupInfo.money += info.money;

                        e1.add(new Entry(info.date, (float) info.money));
                    }

                    ArrayList<PieEntry> entries = new ArrayList<>();
                    int index = 0;
                    for (Map.Entry<Integer, AccountTypeGroupInfo> entry : mAccountTypeGroupMap.entrySet()) {
                        AccountTypeGroupInfo tempInfo = entry.getValue();
                        entries.add(new PieEntry((float) ((tempInfo.money / totalMoney) * 100), tempInfo.accountTypeName));

                        if (index >= colors.size()) {
                            index++;
                            colors.add(Color.parseColor("#" + getRandColorCode()));
                        }
                    }
                    Collections.sort(entries, new Comparator<PieEntry>() {
                        @Override
                        public int compare(PieEntry lhs, PieEntry rhs) {
                            if (lhs.getValue() >= rhs.getValue()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });

                    String name;
                    if (mCurrentAccountType == AccountManager.ACCOUNT_TYPE_EXPENSES) {
                        name = mContext.getString(R.string.string_expenses);
                    } else {
                        name = mContext.getString(R.string.string_expenses);
                    }
                    PieDataSet dataSet = new PieDataSet(entries, name);
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);

                    dataSet.setColors(colors);
                    //dataSet.setSelectionShift(0f);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.WHITE);// 每一个区域中百分比文字的颜色
                    data.setValueTypeface(mTf_percent);// 每一个区域中百分比文字的字体
                    mData = data;


                    LineDataSet d1 = new LineDataSet(e1, "测试");
                    d1.setLineWidth(2.5f);
                    d1.setCircleRadius(4.5f);
                    d1.setHighLightColor(Color.rgb(244, 117, 117));
                    d1.setDrawValues(false);

                    ArrayList<ILineDataSet> sets = new ArrayList<>();
                    sets.add(d1);
                    mLineData = new LineData(sets);
                } else {
                    mData = null;
                    mLineData = null;
                }
                mPieChart.setData(mData);
                mLineChart.setData(mLineData);
                mHandler.sendEmptyMessage(MSG_UPDATE_PIE_CHART);
            }
        }.start();
    }

    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return r + g + b;
    }

    private ArrayList<Integer> colors = new ArrayList<Integer>();

    private void initColors() {
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        if (entry == null)
            return;
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onPageSelected() {
        if (anim) {
            anim = false;
            mPieChart.animateY(DURATION, Easing.EasingOption.Linear);
            mLineChart.animateX(DURATION);
        }
    }
}
