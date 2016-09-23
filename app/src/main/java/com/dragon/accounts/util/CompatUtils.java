package com.dragon.accounts.util;

import android.content.Context;

/**
 * 描述 : 工具类
 */
public class CompatUtils {

	public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
