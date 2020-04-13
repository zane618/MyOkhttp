package com.example.myokhttp.wm;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myokhttp.R;


public class WmActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm);
        mContext = this;
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyEffectView(mContext);

            }
        });

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {



    }
}
