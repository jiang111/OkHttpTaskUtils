/**
 * created by jiang, 16/5/14
 * Copyright (c) 2016, jyuesong@gmail.com All Rights Reserved.
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
import android.jiang.com.library.cookie.CookieJarImpl;
import android.jiang.com.library.cookie.store.CookieStore;
import android.jiang.com.library.cookie.store.HasCookieStore;
import android.jiang.com.library.cookie.store.MemoryCookieStore;
import android.jiang.com.library.exception.Exceptions;
import android.jiang.com.library.listener.NetTaskListener;
import android.jiang.com.library.log.HttpLoggingInterceptor;
import android.jiang.com.library.request.DeleteRequest;
import android.jiang.com.library.request.PostRequest;
import android.jiang.com.library.request.PutRequest;
import android.jiang.com.library.request.UploadRequest;
import android.jiang.com.library.request.UploadSliceRequestBody;
import android.jiang.com.library.request.getRequest;
import android.jiang.com.library.utils.HttpUtils;
import android.jiang.com.library.utils.HttpsUtils;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.jiang.com.library.OkHttpTask.ERROR_OPTIONS.ERROR_TOKEN;

/**
 * Created by jiang on 16/5/14.
 */
public class OkHttpTask {

    private static final String TAG = "OkHttpTask";

    public static final int TYPE_GET = 30;  //get请求
    public static final int TYPE_POST = 60; // post请求
    public static final int TYPE_PUT = 70; // post请求
    public static final int TYPE_DELETE = 90; // delete请求
    private static int[] exitLoginCode = null;
    private static OkHttpTask mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    private static boolean isDebug = false;


    final static class ERROR_OPTIONS {
        public static final String EROR_NONET = "无法连接网络，请检查网络连接状态";
        public static final String EROR_REQUEST_ERROR = "请求失败,请重试";
        public static final String EROR_REQUEST_UNKNOWN = "未知错误";
        public static final String EROR_REQUEST_CREATEDIRFAIL = "创建文件失败,请检查权限";
        public static final String EROR_REQUEST_IO = "IO异常，或者本次任务被取消";
        public static String ERROR_TOKEN = "登录失效，请重新登录";
    }


    public static OkHttpTask getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpTask.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpTask(null);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpTask getInstance(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpTask.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpTask(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    private OkHttpTask(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //cookie enabled
            okHttpClientBuilder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if (isDebug) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message, String json) {
                        LogUtils.i(message);
                        if (!TextUtils.isEmpty(json)) {
                            LogUtils.i("--------json---------\n");
                            LogUtils.json(json);
                            LogUtils.i("--------end----------\n");
                        }
                    }

                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(loggingInterceptor);
            }
            mOkHttpClient = okHttpClientBuilder.build();
        } else {
            mOkHttpClient = okHttpClient;
        }


        init();
    }

    public Gson getmGson() {
        return mGson;
    }

    public static void debug(boolean isdebug) {
        isDebug = isdebug;
    }

    public static void exitLoginCode(int... code) {
        exitLoginCode = code;
    }

    private void init() {
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();

    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


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
    public void filterData(NetTaskListener obj, String url, Object tag, Map<String, String> params, final BaseCallBack callBack, Map<String, String> headers, boolean notConvert, int type, boolean focusCallBack) {
        if (obj == null) {
            doJobNormal(url, params, callBack, tag, type, notConvert, headers, focusCallBack);
        } else if (obj instanceof Activity) {
            doJobByActivity(new WeakReference<>((Activity) obj), url, params, callBack, tag, type, notConvert, headers, focusCallBack);
        } else if (obj instanceof Fragment) {
            doJobByFragment(new WeakReference<>((Fragment) obj), url, params, callBack, tag, type, notConvert, headers, focusCallBack);
        } else {
            doJobNormal(url, params, callBack, tag, type, notConvert, headers, focusCallBack);
        }
    }


    /**
     * 上传文件
     *
     * @param url      url
     * @param headers  验证
     * @param callBack 回调
     * @param o
     */
    void uploadFile(final String url, Map<String, String> headers, List<String> files, final BaseCallBack callBack, Object o, final Boolean focusCallBack) {


        if (isDebug) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append("------upload file-------").append("\n")
                    .append("url: ").append(url).append("\n");
            if (headers != null) {
                logBuilder.append("headers: ").append(headers.toString()).append("\n");
            }
            if (files != null) {
                logBuilder.append("files: ").append(files.toString()).append("\n");
            }
            LogUtils.i(logBuilder.toString());
        }


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null && files.size() > 0) {
            final int fileSize = files.size();
            for (int i = 0; i < fileSize; i++) {
                String resultName = files.get(i).substring(files.get(i).lastIndexOf("/") + 1);
                builder.addFormDataPart("upload", resultName, new UploadSliceRequestBody(files.get(i)));
                if (isDebug) {
                    LogUtils.i("开始上传文件 file: " + files.get(i));
                }
            }
        } else {
            failCallBack(WS_State.OTHERS, "没有文件可上传", callBack);
            return;
        }

        OkHttpClient.Builder uploadBuilder = mOkHttpClient.newBuilder().
                connectTimeout(5, TimeUnit.HOURS)
                .writeTimeout(5, TimeUnit.HOURS)
                .readTimeout(5, TimeUnit.HOURS);

        final Call call = uploadBuilder.build().newCall(UploadRequest.buildPostRequest(url, headers, o, builder.build()));
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("error start  url:" + url);
                e.printStackTrace();
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dealSuccessResponse(response, TYPE_POST, true, callBack, focusCallBack);

            }
        });


    }

    public void downLoadFile(final String url, final String destFileDir, final String fileName, final BaseCallBack callback, Object tag, Map<String, String> headers, final boolean focusCallBack) {
        int type = TYPE_GET;
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(destFileDir) || callback == null || TextUtils.isEmpty(fileName))
            return;
        callback.onBefore();
        doJob(url, null, tag, type, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    FileOutputStream fos = null;
                    InputStream is = null;
                    try {
                        byte[] buf = new byte[2048];

                        is = response.body().byteStream();
                        long fileLongth = (int) response.body().contentLength();
                        int len;
                        long totalLength = 0;
                        long lastProgress = -1;

                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            boolean createDirSuccess = dir.mkdirs();
                            if (!createDirSuccess) {  //创建文件夹失败
                                failCallBack(WS_State.EXCEPTION, ERROR_OPTIONS.EROR_REQUEST_CREATEDIRFAIL, callback);
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
                        successCallBack("下载成功", callback);
                    } catch (IOException e) {
                        failCallBack(WS_State.EXCEPTION, ERROR_OPTIONS.EROR_REQUEST_IO, callback);
                    } catch (Exception e) {
                        failCallBack(WS_State.EXCEPTION, ERROR_OPTIONS.EROR_REQUEST_UNKNOWN, callback);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (containExitLoginCode(code)) {
                    if (focusCallBack) {
                        failCallBack(code, ERROR_TOKEN, callback);
                    }
                    EventBus.getDefault().post(new Integer(exitLoginCode[0]));

                } else {
                    failCallBack(code, ERROR_OPTIONS.EROR_REQUEST_ERROR, callback);
                }
            }
        }, headers);

    }

    private void doJobNormal(final String url, Map<String, String> params, final BaseCallBack callBack, Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers, final boolean focusCallBack) {

        callBack.onBefore();

        doJob(url, params, tag, TYPE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack, focusCallBack);
            }
        }, headers);
    }

    private void doJobByFragment(final WeakReference<Fragment> act, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers, final boolean focusCallBack) {
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
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (!canPassFragment(act))
                    return;
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!canPassFragment(act))
                    return;
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack, focusCallBack);
            }
        }, headers);
    }


    private void doJobByActivity(final WeakReference<Activity> act, final String url, final Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, final boolean notConvert, Map<String, String> headers, final boolean focusCallBack) {
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
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (!canPassActivity(act)) {
                    return;
                }
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!canPassActivity(act)) {
                    return;
                }
                callBack.onFinishResponse(response);
                dealSuccessResponse(response, TYPE, notConvert, callBack, focusCallBack);

            }
        }, headers);


    }

    private void doJob(String url, Map<String, String> params, Object tag, int type, Callback callback, Map<String, String> headers) {

        Request request = null;
        if (type == TYPE_POST) {
            request = PostRequest.buildPostRequest(url, params, tag, headers);  //拿到一个post的request
        } else if (TYPE_GET == type) {
            request = getRequest.buildGetRequest(url, params, tag, headers);//拿到一个get的request
        } else if (TYPE_PUT == type) {
            request = PutRequest.buildOtherRequest(url, params, tag, headers, type);
        } else if (type == TYPE_DELETE) {
            request = DeleteRequest.buildDeleteRequest(url, params, tag, headers);
        } else {
            Exceptions.illegalArgument("只支持 get post put delete");
        }

        final Call call = mOkHttpClient.newCall(request);  //获得一个 call， 这是okhttp核心的类

        call.enqueue(callback);  //加入队列
    }


    //*********************************处理返回的结果********************************************************
    private void dealSuccessResponse(Response response, int type, boolean notConvert, BaseCallBack callBack, boolean focusCallBack) {
        try {
            int status = response.code();
            String msg = response.message();
            if (containExitLoginCode(status)) {
                if (focusCallBack) {
                    failCallBack(status, ERROR_TOKEN, callBack);
                }
                EventBus.getDefault().post(new Integer(exitLoginCode[0]));

            } else {
                final String string = HttpUtils.getContent(notConvert, response.body().string());
                if (status == 200) {
                    Object o;
                    if (callBack.mType == String.class) {
                        o = string;
                    } else {
                        o = mGson.fromJson(string, callBack.mType);
                    }
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
            failCallBack(WS_State.EXCEPTION, ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
        } finally {
            try {
                response.body().close();
            } catch (Exception e) {
                if (isDebug) {
                    LogUtils.d("解析Body失败: " + e.toString());
                }
            }
        }

    }

    private boolean containExitLoginCode(int status) {
        if (exitLoginCode == null)
            return false;
        for (int i = 0; i < exitLoginCode.length; i++) {
            if (status == exitLoginCode[i]) {
                return true;
            }
        }
        return false;
    }

    private void dealNoNetResponse(String erorNonet, BaseCallBack callBack) {
        failCallBack(WS_State.OTHERS, erorNonet, callBack);
    }

    private void dealFailResponse(String msg, BaseCallBack callBack) {
        failCallBack(WS_State.SERVER_ERROR, msg, callBack);
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


    private void successCallBack(final Object o, final BaseCallBack ret) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ret.onSuccess(o);
                ret.onAfter();
            }
        });
    }

    private void progressCallBack(final long l, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(l);
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

    //*********************************end 处理返回的结果********************************************************


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

    public void cancelTask(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    public CookieStore getCookieStore() {
        final CookieJar cookieJar = mOkHttpClient.cookieJar();
        if (cookieJar == null) {
            Exceptions.illegalArgument("you should invoked okHttpClientBuilder.cookieJar() to set a cookieJar.");
        }
        if (cookieJar instanceof HasCookieStore) {
            return ((HasCookieStore) cookieJar).getCookieStore();
        } else {
            return null;
        }
    }


    /**
     * for https mutual authentication
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(certificates, bksFile, password);
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
    }


}
