package com.dragon.accounts.util;

public class AccountUtil {

    public static float getAccountFloatMoney(float money) {
        return (float) (Math.round(money * 100)) / 100;
    }

}
