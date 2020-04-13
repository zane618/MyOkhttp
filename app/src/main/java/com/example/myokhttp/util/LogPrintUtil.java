package com.example.myokhttp.util;

import android.util.Log;


/**
 * 日志操作类,打印日志所用
 * Created by duantianhui on 2018/6/14.
 */
public class LogPrintUtil {

    // 日志开关 true为开启 false关闭
    private static final boolean isPrintLog = true;

    public static final String TAG = "wodedemo";


    public static void zhangshi(String msg) {
        if (isPrintLog) {
            Log.e("zhangshi", msg);
        }
    }

}
