package com.dragon.accounts.util;

import java.text.NumberFormat;

public class AccountUtil {

    /**
     * 保留两位小数
     */
    public static String getAccountMoney(double money) {
        NumberFormat nbf = NumberFormat.getInstance();
        nbf.setMinimumFractionDigits(2);
        return nbf.format(money);
    }

}
