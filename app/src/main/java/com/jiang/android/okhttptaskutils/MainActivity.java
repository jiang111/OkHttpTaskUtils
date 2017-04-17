package com.jiang.android.okhttptaskutils;

import android.jiang.com.library.OkHttpRequest;
import android.jiang.com.library.OkHttpTask;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.listener.NetTaskListener;
import android.jiang.com.library.ws_ret;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NetTaskListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpTask.debug(BuildConfig.DEBUG);

        OkHttpTask.debug(true);


        new OkHttpRequest.Builder()
                .addHeader("k12code", "cloud")
                .addParams("type", "1")
                .url("http://approute.kexinedu.net/api/route")
                .addParams("code", "2")
                .addHeader("k12url", "cloud/update_version")
                .post(new BaseCallBack<Object>() {

                    @Override
                    public void onSuccess(Object body) {
                        LogUtils.i("success: " + body.toString());
                    }

                    @Override
                    public void onFail(ws_ret ret) {
                        LogUtils.i(ret.getMsg());
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


//        new OkHttpRequest.Builder()
//                .url("https://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk")
//                .fileName("asasas.apk")
//                .path(Environment.getExternalStorageDirectory().getPath())
//                .downLoad(new BaseCallBack<Object>() {
//                    @Override
//                    public void onFail(ws_ret ret) {
//                        Log.i(TAG, "onFail: "+ret.toString());
//                    }
//
//                    @Override
//                    public void onSuccess(Object o) {
//                        Log.i(TAG, "onSuccess: ");
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


    }
}
