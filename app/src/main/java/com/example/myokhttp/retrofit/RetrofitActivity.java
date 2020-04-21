package com.example.myokhttp.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;

import com.example.myokhttp.R;
import com.example.myokhttp.databinding.ActivityRetrofitBinding;
import com.example.myokhttp.util.LogPrintUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    ActivityRetrofitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_retrofit);
        binding.btn1.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://fanyi.youdao.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            // 创建 网络请求接口 的实例
            IGetRequest request = retrofit.create(IGetRequest.class);
            //对 发送请求 进行封装
            Call<String> call = request.getCall();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    LogPrintUtil.zhangshi("onResponse:" + response.body().toString());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    LogPrintUtil.zhangshi("onFailure:");
                }

            });
        });

    }
}
