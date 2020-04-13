package com.example.myokhttp.softkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myokhttp.R;
import com.example.myokhttp.util.LogPrintUtil;

public class KeyboardActivity extends AppCompatActivity {


    private EditText edit;
    private View activityRootView2;
    private ViewGroup.LayoutParams params;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        initViews();




        ViewGroup viewGroup = this.findViewById(android.R.id.content);

        View activityRootView = viewGroup.getChildAt(0);

        ViewGroup view2 = this.getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView2 = view2.getChildAt(0);
        params = activityRootView2.getLayoutParams();


        LogPrintUtil.zhangshi("viewGroup。getId:" + viewGroup.getId());
        LogPrintUtil.zhangshi("view2.getId:" + view2.getId());


        LogPrintUtil.zhangshi("activityRootView。getId:" + R.id.nmb);
        LogPrintUtil.zhangshi("activityRootView。getId:" + activityRootView.getId());
        LogPrintUtil.zhangshi("activityRootView2.getId:" + activityRootView2.getId());
        if (view2 == viewGroup) {
            LogPrintUtil.zhangshi("相等");
        } else {
            LogPrintUtil.zhangshi("不相等");
        }


    }

    private void initViews() {
        edit = findViewById(R.id.edit);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                params.height = i * 200;
                activityRootView2.requestLayout();
            }
        });
    }
}
