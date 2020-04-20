package com.example.myokhttp.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myokhttp.R;
import com.example.myokhttp.util.Api;
import com.example.myokhttp.util.RSAToolkit;
import com.example.myokhttp.util.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpActivity extends AppCompatActivity {

    private static final String TAG = "OkhttpMainActivity";
    String url = "http://www.kuaidi100.com/query?type=shentong&postid=12341431431";

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequest();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncGetRequest();
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klb1000();
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMuiltipartBody();
            }
        });

    }

    /**
     * 我们在有些情况下既要上传文件还要上传其他类型字段。比如在个人中心我们可以修改名字，年龄，修改图像，
     * 这其实就是一个表单。这里我们用到MuiltipartBody ,它 是RequestBody 的一个子类,我们提交表单就是利用这个类
     * 来构建一个 RequestBody
     */
    private void uploadMuiltipartBody() {
        String fileDir = "/sdcard/DCIM/Camera/image.jpg";
        String fileDir2 = "/sdcard/DCIM/Camera/lALPDgQ9rtdJXBfNAvrNBN8_1247_762.png_720x720g.jpg";
        //1.创建OkhttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //上传的图片
        File file = new File(fileDir);
        File file2 = new File(fileDir2);
        //2.通过new MultiPartBody.Bulder()创建RequestBody对象
        String signStr = Api.AID + Api.SID.POST_IMG_SID +Api.TOKEN;
        RequestBody requestBody = new MultipartBody.Builder()
                //设置类型是表单
                .setType(MultipartBody.FORM)
                //添加数据
                .addFormDataPart("sid", Api.SID.POST_IMG_SID)
                .addFormDataPart("aid", Api.AID)
                .addFormDataPart("token", Api.TOKEN)
                .addFormDataPart("token", Api.TOKEN)
                .addFormDataPart("sign", RSAToolkit.encryptToSHA(signStr))
                //相同字段 是有优先使用第一次设置的value
                .addFormDataPart("imageFile", "fileName2.jpg", RequestBody.create(file2, MediaType.parse("img/*")))
                .addFormDataPart("imageFile", "fileName.jpg", RequestBody.create(file, MediaType.parse("img/*")))
                .build();
        //3.创建Request对象，设置url地址，将requestBody作为post方法的参数传入
        Request request = new Request.Builder()
                .url(Api.BASE_IMG_URL)
                .post(requestBody)
                .build();
        //4.创建一个call对象，参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toast(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                Log.e(TAG, string);
                toast(string);
            }
        });




    }

    /**
     * 2-3 异步POST——键值对
     * 通过FormBody
     * 添加多个String键值对
     * 最后为Request添加post方法并传入formBody
     * 经过对比我们发现异步的POST请求和GET请求步骤很相似
     * 这里就不赘述了
     */
    private void asynPostHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("size", "10")
                .build();
        Request request = new Request.Builder().url(Api.BASE_URL)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toast("onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                toast("onResponse:" + string);
            }
        });
    }

    /**
     * 2-4  提交json数据
     */
    private void klb1000() {
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject object = new JSONObject();
        try {
            JSONObject ext = new JSONObject();
            //app版本
            ext.put("version", "3.0.0.1");
            //终端类型：0：PC，1：Android，2：iPhone
            ext.put("terminalType", "1");
            //app设备型号
            ext.put("userAgent", "xiaomi | MI 6X | 9");
            ext.put("n_state", "WIFI");
            object.put("ext", ext);
            object.put("aid", "QDD201809061356001");
            object.put("sid", "1000");
            JSONObject uj = new JSONObject();
            uj.put("m", "15755185581");
            object.put("data", uj.toString());
            String signStr = object.getString("aid") + object.getString("sid") + uj.toString();
            object.put("sign", RSAToolkit.encryptToSHA(signStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //两种构建requestBody方式都可以， FormBody和MultipartBody是RequestBody的子类
//        RequestBody formBody = FormBody.create(object.toString(), MediaType.parse("application/json; charset=utf-8"));
        RequestBody formBody = RequestBody.create(object.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(Api.BASE_URL).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toast("onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                toast("onResponse:" + string);
            }
        });
    }

    /**
     * 2-2 同步Get请求 （较少用）
     * 同步GET请求和异步GET请求基本一样，不同地方是同步请求调用Call的execute()方法，而异步请求调用call.enqueue()方法
     */
    private void syncGetRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).method("GET", null).build();
        final Call call = okHttpClient.newCall(request);
        //4.同步调用会阻塞主线程，这边在子线程进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = call.execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tv.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 2-1 异步Get请求
     * 异步GET请求的4个步骤：
     * 创建OkHttpClient对象
     * 通过Builder模式创建Request对象，参数必须有个url参数，可以通过Request.Builder设置更多的参数比如：header、method等
     * 通过request的对象去构造得到一个Call对象，Call对象有execute()和cancel()等方法。
     * 以异步的方式去执行请求，调用的是call.enqueue，将call加入调度队列，任务执行完成会在Callback中得到结果。
     * 注意事项：
     * 异步调用的回调函数是在子线程，我们不能在子线程更新UI
     * 需要借助于 runOnUiThread() 方法或者 Handler 来处理。
     * onResponse回调有一个参数是response
     * 如果想获得返回的是字符串，可以通过response.body().string()
     * 如果获得返回的二进制字节数组，则调用response.body().bytes()
     * 如果想拿到返回的inputStream，则调response.body().byteStream()
     * 有inputStream我们就可以通过IO的方式写文件
     */
    private void getRequest() {
        //1.创建okhttpClinet帝乡
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建REquest对象，设置一个url地址，设置请求方式
        final Request request = new Request.Builder().url(url).method("GET", null).build();
        //3.创建一个call对象，参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String string = response.body().string();
                final byte[] bytes = response.body().bytes();
                InputStream inputStream = response.body().byteStream();

                Log.e(TAG, "onResponse:"+ string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(string.toString());
                    }
                });
            }
        });
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.toastCenter(str);
            }
        });
    }
}
