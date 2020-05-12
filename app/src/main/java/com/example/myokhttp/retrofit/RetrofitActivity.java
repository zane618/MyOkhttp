package com.example.myokhttp.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.myokhttp.R;
import com.example.myokhttp.databinding.ActivityRetrofitBinding;
import com.example.myokhttp.retrofit.bean.GetBean;
import com.example.myokhttp.retrofit.bean.GetBean01;
import com.example.myokhttp.util.LogPrintUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
            first();
        });
        binding.btn2.setOnClickListener(v -> {
            get01();
        });

    }

    private void get01() {
        //原始http://www.kuaidi100.com/query?type=shentong&postid=12341431431/
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.kuaidi100.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IGetRequest getRequest = retrofit.create(IGetRequest.class);
        Call<GetBean01> call = getRequest.getCall01("shentong", "12341431431");
        call.enqueue(new Callback<GetBean01>() {
            @Override
            public void onResponse(Call<GetBean01> call, Response<GetBean01> response) {
                GetBean01 bean01 = response.body();
                LogPrintUtil.zhangshi("bean01:" + bean01.getMessage()
                +",nu:" + bean01.getNu());
            }

            @Override
            public void onFailure(Call<GetBean01> call, Throwable t) {
                LogPrintUtil.zhangshi("onFailure");
            }
        });
    }

    private void first() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        // 创建 网络请求接口 的实例
        IGetRequest request = retrofit.create(IGetRequest.class);
        //对 发送请求 进行封装
        Call<GetBean> call = request.getCall("");
        //异步
        call.enqueue(new Callback<GetBean>() {
            @Override
            public void onResponse(Call<GetBean> call, Response<GetBean> response) {
                LogPrintUtil.zhangshi("onResponse:" + response.body().toString());
                GetBean bean01 = response.body();
            }

            @Override
            public void onFailure(Call<GetBean> call, Throwable t) {
                LogPrintUtil.zhangshi("onFailure:");
            }
        });

        //同步
        /*try {
            Response<GetBean01> response = call.execute();
            GetBean01 bean01 = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}
