package com.jiang.android.okhttptaskutils;

import android.jiang.com.library.OkHttpRequest;
import android.jiang.com.library.OkHttpTask;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.listener.NetTaskListener;
import android.jiang.com.library.ws_ret;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NetTaskListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        OkHttpUtils
//                .get()
//                .url("https://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk")//
//                .build()
//                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "asas.apk")
//                {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Log.i(TAG, "onError: ");
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        Log.i(TAG, "onResponse: ");
//
//                    }
//                });

        OkHttpTask.debug(true);

//        new OkHttpRequest.Builder()
//                .addParams("phone", "15240393098")
//                .with(this)
//                .addParams("password", "123456")
//                .url("http://139.196.36.70:3001/login")
//                .post(new BaseCallBack<Object>() {
//
//                    @Override
//                    public void onFail(ws_ret ret) {
//                        LogUtils.i(ret.getMsg());
//                    }
//
//                    @Override
//                    public void onSuccess(Object o) {
//                        LogUtils.json(o.toString());
//
//                    }
//
//                    @Override
//                    public void onNoData(ws_ret ret) {
//
//                    }
//
//                    @Override
//                    public void onBefore() {
//
//                    }
//
//                    @Override
//                    public void onAfter() {
//
//                    }
//
//                    @Override
//                    public void onFinishResponse(Response response) {
//
//                    }
//
//                    @Override
//                    public void onProgress(long progress) {
//
//                    }
//                });


        new OkHttpRequest.Builder()
                .url("https://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk")
                .fileName("asasas.apk")
                .path(Environment.getExternalStorageDirectory().getPath())
                .downLoad(new BaseCallBack<Object>() {
                    @Override
                    public void onFail(ws_ret ret) {
                        Log.i(TAG, "onFail: "+ret.toString());
                    }

                    @Override
                    public void onSuccess(Object o) {
                        Log.i(TAG, "onSuccess: ");

                    }

                    @Override
                    public void onNoData(ws_ret ret) {

                    }

                    @Override
                    public void onBefore() {

                    }

                    @Override
                    public void onAfter() {

                    }

                    @Override
                    public void onFinishResponse(Response response) {

                    }

                    @Override
                    public void onProgress(long progress) {

                    }
                });


    }
}
