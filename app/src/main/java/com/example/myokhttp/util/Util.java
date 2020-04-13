package com.example.myokhttp.util;

import com.example.myokhttp.App;

public class Util {

    /**
     * 弹屏幕居中的toast
     *
     * @param text
     */
    public static void toastCenter(String text) {
        new MyToastView(App.getInstance(), text).show();
    }


}
