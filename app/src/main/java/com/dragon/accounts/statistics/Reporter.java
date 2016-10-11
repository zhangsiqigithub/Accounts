package com.dragon.accounts.statistics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.umeng.analytics.MobclickAgent;

/**
 * 数据上报相关
 */
public class Reporter {

    /**
     * 初始化友盟数据打点相关
     */
    public static void init(Context context) {
        if (context == null) {
            return;
        }
//        MobclickAgent.onProfileSignIn("test");
        MobclickAgent.setDebugMode(true);

        String appkey_umeng = null;
        String chennalid_umeng = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            appkey_umeng = appInfo.metaData.getString("UMENG_APPKEY");
            chennalid_umeng = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context,
                appkey_umeng,
                chennalid_umeng,
                MobclickAgent.EScenarioType.E_UM_NORMAL));
        MobclickAgent.enableEncrypt(false);// 日志加密设置
    }

    public static void onResume(Context context, String pageName) {
        if (context == null) {
            return;
        }
        onPageStart(pageName);
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context, String pageName) {
        if (context == null) {
            return;
        }
        onPageEnd(pageName);
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    public static void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    public static void onEvent(Context context, String eventKey) {
        if (context == null) {
            return;
        }
        MobclickAgent.onEvent(context, eventKey);
    }

}
