package com.jiang.android.okhttptaskutils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.jiang.com.library.OkHttpRequest;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.ws_ret;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import okhttp3.Response;

/**
 * Created by jiang on 16/8/24.
 */

public class TestUploadFileActivity extends AppCompatActivity {


    private static final String TAG = "TestUploadFileActivity";
    private static final int IMG = 100;
    private static final int REQUEST_WRITE = 10;

    Button mUpload;
    Button mUpload1;

    private List<String> mLists = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_uploadfile);
        mUpload = (Button) findViewById(R.id.upload);
        mUpload1 = (Button) findViewById(R.id.upload_2);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE);
        }
        initView();
    }

    void initView() {

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(50)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(TestUploadFileActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });

        mUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new OkHttpRequest.Builder()
                        .url("http://120.27.244.198:8000/shard_upload")
                        .file(mLists.get(0))
//                        .file(Environment.getExternalStorageDirectory().getPath()+"/1.amr")
                        .upload(new BaseCallBack<Object>() {
                            @Override
                            public void onFail(ws_ret ret) {

                            }

                            @Override
                            public void onSuccess(Object o) {

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

                                Log.i(TAG, "onProgress: "+progress);
                            }
                        });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.i(TAG, "onActivityResult: " + photos.size());
                mLists.clear();
                mLists.addAll(photos);
            }
        }
    }


}
