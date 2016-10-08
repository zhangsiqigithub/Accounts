package com.dragon.accounts.model;

import android.content.Context;
import android.graphics.Color;

import com.dragon.accounts.model.accountbook.info.AccountBookInfo;

import java.util.ArrayList;
import java.util.List;

public class ColorManager {

    private static final int COLOR_0 = Color.parseColor("#FD9620");
    private static final int COLOR_1 = Color.parseColor("#BFD62B");
    private static final int COLOR_2 = Color.parseColor("#FC4AEA");
    private static final int COLOR_3 = Color.parseColor("#53443C");
    private static final int COLOR_4 = Color.parseColor("#3D4146");

    public static int getColor(int colorPosition) {
        switch (colorPosition) {
            case 0:
                return COLOR_0;
            case 1:
                return COLOR_1;
            case 2:
                return COLOR_2;
            case 3:
                return COLOR_3;
            case 4:
                return COLOR_4;
        }
        return COLOR_0;
    }

    public static int getCurrentBookColor(Context context) {
        int mAccountBookBgColor = 0;
        AccountBookInfo accountBookInfo = AccountBookManager.queryAccountBookByBookId(context, AccountBookManager.getCurrentAccountBookId(context));
        if (accountBookInfo != null) {
            mAccountBookBgColor = ColorManager.getColor(accountBookInfo.colorPosition);
        }
        return mAccountBookBgColor;
    }

    public static List<Integer> getColorList() {
        List<Integer> list = new ArrayList<>();
        list.add(COLOR_0);
        list.add(COLOR_1);
        list.add(COLOR_2);
        list.add(COLOR_3);
        list.add(COLOR_4);
        return list;
    }

}
