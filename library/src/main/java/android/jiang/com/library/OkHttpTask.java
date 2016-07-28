/**
 * created by jiang, 15/10/16
 * Copyright (c) 2015, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */
package android.jiang.com.library;

import android.app.Activity;
import android.app.Fragment;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.listener.NetTaskListener;
import android.jiang.com.library.request.CountingRequestBody;
import android.jiang.com.library.request.DeleteRequest;
import android.jiang.com.library.request.PostRequest;
import android.jiang.com.library.request.PutRequest;
import android.jiang.com.library.request.UploadRequest;
import android.jiang.com.library.request.getRequest;
import android.jiang.com.library.utils.HttpUtils;
import android.jiang.com.library.utils.HttpsUtils;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.UnknownFormatFlagsException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okio.Buffer;


/**
 * 网络请求的主体
 * Created by jiang on 15/10/16.
 */
public class OkHttpTask {

    private static boolean isDebug;
    private static int exitLoginCode = -1;

    public static final int TYPE_GET = 30;  //get请求
    public static final int TYPE_POST = 60; // post请求
    public static final int TYPE_PUT = 70; // post请求
    public static final int TYPE_DELETE = 90; // delete请求
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static OkHttpTask mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;   //发送到主线程需要的handler
    private Gson mGson;         //解析json的工具类
    private static final int timeoutMs = 60;

    public OkHttpTask initDebugModel(boolean isdebug) {
        isDebug = isdebug;
        return this;
    }

    public OkHttpTask initExitLoginCode(int code) {
        exitLoginCode = code;
        return this;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    final static class ERROR_OPTIONS {
        public static final String EROR_NONET = "无法连接网络，请检查网络连接状态";
        public static final String EROR_REQUEST_ERROR = "请求失败,请重试";
        public static final String EROR_REQUEST_500 = "服务器内部出错";
        public static final String EROR_REQUEST_EXITLOGIN = "请重新登录";
        public static final String EROR_REQUEST_JSONERROR = "Json解析出错";
        public static final String EROR_REQUEST_UNKNOWN = "未知错误";
        public static final String EROR_REQUEST_CREATEDIRFAIL = "创建文件失败,请检查权限";
        public static final String EROR_REQUEST_IO = "IO异常，或者本次任务被取消";


    }

    public Handler getmDelivery() {
        return mDelivery;
    }

    private OkHttpTask() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(timeoutMs, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(timeoutMs, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(timeoutMs, TimeUnit.SECONDS);
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    }


    public static OkHttpTask getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpTask.class) {
                mInstance = new OkHttpTask();
            }
        }
        return mInstance;
    }

    public Gson getmGson() {
        return mGson;
    }


    //************ 添加证书功能 ***********

    /**
     * for https-way authentication
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(certificates, null, null);
        mOkHttpClient.setSslSocketFactory(sslSocketFactory);
    }

    /**
     * for https mutual authentication
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        mOkHttpClient.setSslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, bksFile, password));
    }

    //***********end*********

    /**
     * @param obj        一个用来引用的对象
     * @param url        url
     * @param params     需要传递的参数  get请求? 后面的参数也可以通过param传递
     * @param callBack   返回的回调
     * @param tag        唯一的key， 可以通过这个唯一的key来取消网络请求
     * @param type       请求的类型
     * @param notConvert
     * @param headers    需要特殊处理的请求头
     */
    public void filterData(NetTaskListener obj, String url, Object tag, Map<String, String> params, final BaseCallBack callBack, Map<String, String> headers, boolean notConvert, int type) {
        if (obj == null) {
            doJobNormal(url, params, callBack, tag, type, notConvert, headers);
        } else if (obj instanceof Activity) {
            doJobByActivity(new WeakReference<Activity>((Activity) obj), url, params, callBack, tag, type, notConvert, headers);
        } else if (obj instanceof Fragment) {
            doJobByFragment(new WeakReference<Fragment>((Fragment) obj), url, params, callBack, tag, type, notConvert, headers);
        } else {
            doJobNormal(url, params, callBack, tag, type, notConvert, headers);
        }
    }


    public void doJobNormal(final String url, Map<String, String> params, final BaseCallBack callBack, Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers) {

        callBack.onBefore();
        doJob(url, params, tag, TYPE, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack);

            }
        }, headers);
    }

    /**
     * 上传文件
     *
     * @param url      url
     * @param headers  验证
     * @param path     文件全路径
     * @param callBack 回调
     * @param tag      tag
     */
    public void uploadFile(String url, Map<String, String> headers, String path, final BaseCallBack callBack, Object tag) {
        File file = new File(path);
        if (isDebug()) {
            LogUtils.i("开始上传文件 file: " + file.toString());
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                long progress = bytesWritten * 100 / contentLength;
                if (isDebug()) {
                    LogUtils.i("上传文件中... progress: " + progress + "%");
                }
                progressCallBack(progress, callBack);
            }
        });

        final Call call = mOkHttpClient.newCall(UploadRequest.buildPutRequest(url, headers, tag, countingRequestBody));
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                failCallBack(303, "上传失败,请重试", callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                int code = response.code();
                if (response.code() == 200) {
                    successCallBack("上传成功", callBack);
                } else {
                    String msg = response.message();
                    if (TextUtils.isEmpty(msg)) {
                        msg = "上传失败,请重试";
                    }
                    failCallBack(code, msg, callBack);
                }

            }
        });


    }


    public void doJobDownLoadFile(final String url, final String destFileDir, final String fileName, final BaseCallBack callback, Object tag, Map<String, String> headers) {

        int type = TYPE_GET;
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(destFileDir) || callback == null || TextUtils.isEmpty(fileName))
            return;
        callback.onBefore();
        doJob(url, null, tag, type, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_ERROR, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.code() == 200) {
                    FileOutputStream fos = null;
                    InputStream is = null;
                    try {
                        byte[] buf = new byte[2048];

                        is = response.body().byteStream();
                        long fileLongth = (int) response.body().contentLength();
                        int len = 0;
                        long totalLength = 0;
                        long lastProgress = -1;

                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            boolean createDirSuccess = dir.mkdirs();
                            if (!createDirSuccess) {  //创建文件夹失败
                                failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_CREATEDIRFAIL, callback);
                                return;
                            }
                        }
                        File file = new File(dir, fileName);
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            totalLength += len;
                            long progress = totalLength * 100 / fileLongth;

                            if (lastProgress != progress) {
                                progressCallBack(progress, callback);
                            }
                            lastProgress = progress;

                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径

                        successCallBack("下载成功", callback);
                    } catch (IOException e) {
                        failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_IO, callback);
                    } catch (Exception e) {
                        failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_UNKNOWN, callback);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }
                } else {
                    failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_ERROR, callback);
                }
            }
        }, headers);


    }

    public void doJobByFragment(final WeakReference<Fragment> act, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers) {
        if (!canPassFragment(act)) {
            return;
        }

        callBack.onBefore();
        if (!HttpUtils.isNetworkConnected(act.get().getActivity())) {
            dealNoNetResponse(ERROR_OPTIONS.EROR_NONET, callBack);
            return;
        }
        doJob(url, params, tag, TYPE, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (!canPassFragment(act))
                    return;
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!canPassFragment(act))
                    return;
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack);

            }
        }, headers);
    }


    public void doJobByActivity(final WeakReference<Activity> act, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers) {
        if (!canPassActivity(act)) {
            return;
        }

        callBack.onBefore();
        if (!HttpUtils.isNetworkConnected(act.get())) {
            dealNoNetResponse(ERROR_OPTIONS.EROR_NONET, callBack);
            return;
        }

        doJob(url, params, tag, TYPE, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                if (!canPassActivity(act)) {
                    return;
                }
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (!canPassActivity(act)) {
                    return;
                }
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack);

            }
        }, headers);
    }

    private void dealNoNetResponse(String erorNonet, BaseCallBack callBack) {
        failCallBack(WS_State.OTHERS, erorNonet, callBack);
    }

    private static boolean canPassFragment(WeakReference<Fragment> act) {
        if (act == null || act.get() == null || !act.get().isAdded() || act.get().isDetached()) {
            return false;
        }
        return true;

    }

    private static boolean canPassActivity(WeakReference<Activity> act) {
        if (act == null || act.get() == null || act.get().isFinishing()) {
            return false;
        }
        return true;

    }

    private void doJob(final String url, Map<String, String> params, final Object tag, final int TYPE, Callback back, Map<String, String> headers) {
        Request request;
        if (TYPE == TYPE_POST) {
            request = PostRequest.buildPostRequest(url, params, tag, headers);  //拿到一个post的request
        } else if (TYPE_GET == TYPE) {
            request = getRequest.buildGetRequest(url, params, tag, headers);//拿到一个get的request
        } else if (TYPE == TYPE_PUT) {
            request = PutRequest.buildPutRequest(url, params, tag, headers);
        } else if (TYPE == TYPE_DELETE) {
            request = DeleteRequest.buildDeleteRequest(url, params, tag, headers);
        } else {
            throw new UnknownFormatFlagsException("暂时只支持Get put和 Post Delete");
        }

        final Call call = mOkHttpClient.newCall(request);  //获得一个 call， 这是okhttp核心的类

        call.enqueue(back);  //加入队列
    }


    private void dealSuccessResponse(Response response, int TYPE, boolean notConvert, BaseCallBack callBack) {
        try {
            int status = response.code();
            if (isDebug()) {
                StringBuffer buffer = new StringBuffer();
                try {
                    buffer.append(" \n url:").append(response.request().url());
                    buffer.append(" \n header: \n")
                            .append(response.request().headers().toString())
                            .append("status:")
                            .append(status);

                    if (TYPE == TYPE_POST) {
                        try {
                            buffer.append(" \n body: \n ")
                                    .append(bodyToString(response.request()));
                        } catch (Exception e) {

                        }
                    }

                } catch (Exception e) {
                } finally {
                    LogUtils.i(buffer.toString());
                }
            }
            if (status == exitLoginCode) {
                EventBus.getDefault().post(exitLoginCode);
                failCallBack(status, ERROR_OPTIONS.EROR_REQUEST_EXITLOGIN, callBack);
            } else {
                final String string = HttpUtils.getContent(notConvert, response.body().string());
                if (isDebug()) {
                    LogUtils.json(string);

                }
                if (status == 200) {
                    Object o = mGson.fromJson(string, callBack.mType);
                    successCallBack(o, callBack);
                } else if (status == 204) {
                    emptyCallBack(WS_State.NODATA, "暂无数据", callBack);
                } else {
                    ws_ret o = mGson.fromJson(string, ws_ret.class);
                    if (TextUtils.isEmpty(o.getMsg())) {
                        failCallBack(status, ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
                    } else {
                        failCallBack(status, o.getMsg(), callBack);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isDebug) {
                LogUtils.d("exception info: " + e.toString());
            }
            failCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
        } finally {
            try {
                response.body().close();
            } catch (IOException e) {
            }
        }


    }


    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "parse failed";
        }
    }

    private void dealFailResponse(String msg, BaseCallBack callBack) {
        failCallBack(WS_State.OTHERS, msg, callBack);
    }


    private void progressCallBack(final long l, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(l);
            }
        });

    }


    private void successCallBack(final Object o, final BaseCallBack ret) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ret.onSuccess(o);
                ret.onAfter();
            }
        });
    }

    private void emptyCallBack(final int state, final String msg, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ws_ret ret = new ws_ret(state, msg);
                callback.onNoData(ret);
                callback.onAfter();
            }
        });
    }

    private void failCallBack(final int state, final String msg, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ws_ret ret = new ws_ret(state, msg);
                callback.onFail(ret);
                callback.onAfter();
            }
        });
    }


    public void cancelTask(Object key) {
        try {
            mOkHttpClient.cancel(key);
        } catch (Exception e) {

        }
    }


}
