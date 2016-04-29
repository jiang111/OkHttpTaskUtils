package com.jiang.android.okhttptaskutils;

import android.jiang.com.library.OkHttpRequest;
import android.jiang.com.library.OkHttpTask;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.listener.NetTaskListener;
import android.jiang.com.library.ws_ret;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity implements NetTaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpTask.getInstance().initDebugModel(true);
        new OkHttpRequest.Builder()
                .addParams("phone", "15240393098")
                .with(this)
                .addParams("password", "123456")
                .url("http:///login")
                .post(new BaseCallBack<Object>() {

                    @Override
                    public void onFail(ws_ret ret) {
                        LogUtils.i(ret.getMsg());
                    }

                    @Override
                    public void onSuccess(Object o) {
                        LogUtils.json(o.toString());

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
