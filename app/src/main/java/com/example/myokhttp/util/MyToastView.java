package com.example.myokhttp.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myokhttp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的toast 根据我们传入的文本内容和显示时间来弹toast
 */
public class MyToastView {

    private Toast toast;
    private int time;
    private Timer timer;

    public MyToastView(Context context, String text) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_view, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_text);
        tv.setText(text);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);

    }

    /**
     * 设置toast显示位置
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    /**
     * 设置显示时间
     *
     * @param duration
     */
    public MyToastView setDuration(int duration) {
        toast.setDuration(duration);
        return this;
    }

    /**
     * 设置持续时间(自定义时间)
     */
    public void setLongTime(int duration) {
        time = duration;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time - 1000 >= 0) {
                    show();
                    time = time - 1000;
                } else {
                    cancel();
                    timer.cancel();
                }
            }
        }, 0, 1000);

    }

    public void show() {
        toast.show();
    }

}
