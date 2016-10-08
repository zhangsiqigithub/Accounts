package com.dragon.accounts.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dragon.accounts.R;
import com.dragon.accounts.view.calendar.CalendarDay;
import com.dragon.accounts.view.calendar.MaterialCalendarView;
import com.dragon.accounts.view.calendar.OnDateSelectedListener;
import com.dragon.accounts.view.calendar.OnMonthChangedListener;

import java.util.Date;

public class CalendarDialog extends Dialog implements OnDateSelectedListener, OnMonthChangedListener {

    public interface CalendarDialogCallback {
        void onDateSelected(long time);
    }

    private MaterialCalendarView mMaterialCalendarView;
    private CalendarDialogCallback mCalendarDialogCallback;
    private long mTimeLong;

    public CalendarDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calander);
        mMaterialCalendarView = (MaterialCalendarView) findViewById(R.id.dialot_calendar_calendarView);
        mMaterialCalendarView.setOnDateChangedListener(this);
        mMaterialCalendarView.setOnMonthChangedListener(this);
        setSelectedDate(mTimeLong);
    }

    public void setSelectedDate(long mTimeLong) {
        this.mTimeLong = mTimeLong;
        if (mMaterialCalendarView != null) {
            mMaterialCalendarView.setSelectedDate(new Date((mTimeLong == 0 ? System.currentTimeMillis() : mTimeLong)));
        }
    }

    public void setCalendarDialogCallback(CalendarDialogCallback calendarDialogCallback) {
        this.mCalendarDialogCallback = calendarDialogCallback;
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Date tempDate = date.getDate();
        if (mCalendarDialogCallback != null) {
            mCalendarDialogCallback.onDateSelected(tempDate.getTime());
        }
    }
}
