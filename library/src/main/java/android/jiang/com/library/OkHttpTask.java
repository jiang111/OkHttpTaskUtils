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
import android.content.Context;
import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.callback.SimpleCallBack;
import android.jiang.com.library.request.DeleteRequest;
import android.jiang.com.library.request.PostRequest;
import android.jiang.com.library.request.PutRequest;
import android.jiang.com.library.request.getRequest;
import android.jiang.com.library.utils.HttpUtils;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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


/**
 * 网络请求的主体
 * Created by jiang on 15/10/16.
 */
public class OkHttpTask {

    private static final String TAG = "OkHttpTask";
    private static boolean isDebug;

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

    public void initDebugModel(boolean isdebug) {
        isDebug = isdebug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    final static class ERROR_OPTIONS {
        public static final String EROR_NONET = "无法连接网络，请检查网络连接状态";
        public static final String EROR_REQUEST_ERROR = "请求失败,请重试";
        public static final String EROR_REQUEST_404 = "暂无资源可用";
        public static final String EROR_REQUEST_500 = "服务器内部出错";
        public static final String EROR_REQUEST_401 = "你的账号已被别人登录";
        public static final String EROR_REQUEST_EMPTY = "暂无数据";
        public static final String EROR_REQUEST_JSONERROR = "Json解析出错";
        public static final String EROR_REQUEST_UNKNOWN = "未知错误";
        public static final String EROR_REQUEST_CREATEDIRFAIL = "创建文件失败,请检查权限";
        public static final String EROR_REQUEST_IO = "IO异常，或者本次任务被取消";
        public static final String EROR_REQUEST_NO_STATECODE = "服务器未返回状态码";


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


    public void doJobNormal(final String url, Map<String, String> params, final BaseCallBack callBack, Object tag, final int TYPE, Map<String, String> headers) {

        if (!validateMethodParams(url, callBack, TYPE))
            return;
        callBack.onBefore();
        doJob(url, params, tag, TYPE, new SimpleCallBack() {
            @Override
            public void onFailure(Request request, IOException e) {
                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                dealSuccessResponse(response, TYPE, callBack);

            }
        }, headers);
    }

    public void doJobDownLoadFile(final String url, final String destFileDir, final String fileName, final BaseCallBack callback, Object tag, Map<String, String> headers) {

        int type = TYPE_GET;
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(destFileDir) || callback == null || TextUtils.isEmpty(fileName))
            return;
        callback.onBefore();
        doJob(url, null, tag, type, new SimpleCallBack() {
            @Override
            public void onFailure(Request request, IOException e) {
                dealDownLoadFileFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {

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
                            dealDownLoadFileFailResponse(ERROR_OPTIONS.EROR_REQUEST_CREATEDIRFAIL, callback);
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
                            sendProgressDownLoadCallback(progress, callback);
                        }
                        lastProgress = progress;

                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径

                    sendSuccessDownLoadCallback("下载成功", callback);
                } catch (IOException e) {
                    dealDownLoadFileFailResponse(ERROR_OPTIONS.EROR_REQUEST_IO, callback);
                } catch (Exception e) {
                    dealDownLoadFileFailResponse(ERROR_OPTIONS.EROR_REQUEST_UNKNOWN, callback);
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
            }
        }, headers);


    }


    public void doJobByContext(final Context context, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, Map<String, String> headers) {

        if (context == null)
            return;
        if (!validateMethodParams(url, callBack, TYPE))
            return;
        Context ct = context.getApplicationContext();
        callBack.onBefore();
        if (!HttpUtils.isNetworkConnected(ct)) {
            dealNoNetResponse(ERROR_OPTIONS.EROR_NONET, callBack);
            return;
        }
        doJob(url, params, tag, TYPE, new SimpleCallBack() {
            @Override
            public void onFailure(Request request, IOException e) {

                dealFailResponse(ERROR_OPTIONS.EROR_REQUEST_ERROR, callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                dealSuccessResponse(response, TYPE, callBack);

            }
        }, headers);
    }


    public void doJobByFragment(final WeakReference<Fragment> act, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, Map<String, String> headers) {
        if (!canPassFragment(act)) {
            return;
        }
        if (!validateMethodParams(url, callBack, TYPE))
            return;
        callBack.onBefore();
        if (!HttpUtils.isNetworkConnected((Context) act.get().getActivity())) {
            dealNoNetResponse(ERROR_OPTIONS.EROR_NONET, callBack);
            return;
        }
        doJob(url, params, tag, TYPE, new SimpleCallBack() {
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
                dealSuccessResponse(response, TYPE, callBack);

            }
        }, headers);
    }


    public void doJobByActivity(final WeakReference<Activity> act, final String url, Map<String, String> params, final BaseCallBack callBack, final Object tag, final int TYPE, Map<String, String> headers) {
        if (!canPassActivity(act)) {
            return;
        }
        if (!validateMethodParams(url, callBack, TYPE))
            return;
        callBack.onBefore();
        if (!HttpUtils.isNetworkConnected((Context) act.get())) {
            dealNoNetResponse(ERROR_OPTIONS.EROR_NONET, callBack);
            return;
        }

        doJob(url, params, tag, TYPE, new SimpleCallBack() {
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
                dealSuccessResponse(response, TYPE, callBack);

            }
        }, headers);
    }

    private void dealNoNetResponse(String erorNonet, BaseCallBack callBack) {
        sendFSCallBack(WS_State.OTHERS, erorNonet, callBack);
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

    private static boolean validateMethodParams(String url, BaseCallBack callBack, int type) {
        if (TextUtils.isEmpty(url))
            return false;
        if (callBack == null)
            return false;
        if (type != TYPE_GET && type != TYPE_POST && type != TYPE_PUT && type != TYPE_DELETE)
            return false;
        return true;
    }


    private void doJob(final String url, Map<String, String> params, final Object tag, final int TYPE, SimpleCallBack back, Map<String, String> headers) {
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


    private void dealSuccessResponse(Response response, int TYPE, BaseCallBack callBack) {
        try {
            int status = response.code();
            if (isDebug()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(" \n url:").append(response.request().url())
                        .append(" \n header: \n")
                        .append(response.request().headers().toString())
                        .append("status:")
                        .append(status);
                Log.i(TAG, buffer.toString());
            }
            final String string = HttpUtils.getContent(response.body().string());
            if (status == 200) {
                if (isDebug())
                    Log.i(TAG, "response: " + string);
                Object o = mGson.fromJson(string, callBack.mType);
                sendPostSuccessCallBack(o, callBack);
            } else if (status == 204) {
                sendESCallBack(WS_State.NODATA, "暂无数据", callBack);
            } else {
                ws_ret o = mGson.fromJson(string, ws_ret.class);
                if (TextUtils.isEmpty(o.getMsg())) {
                    sendFSCallBack(status, ERROR_OPTIONS.EROR_REQUEST_500, callBack);
                } else {
                    sendFSCallBack(status, o.getMsg(), callBack);
                }
            }
        } catch (IOException e) {
            sendFSCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_IO, callBack);
        } catch (com.google.gson.JsonParseException e) {
            sendFSCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_JSONERROR, callBack);
        } catch (Exception e) {
            sendFSCallBack(WS_State.OTHERS, ERROR_OPTIONS.EROR_REQUEST_UNKNOWN, callBack);
        } finally {
            try {
                response.body().close();
            } catch (IOException e) {
            }
        }


    }

    private void dealFailResponse(String msg, BaseCallBack callBack) {
        sendFSCallBack(WS_State.OTHERS, msg, callBack);
    }

    private void dealDownLoadFileFailResponse(String erorRequestError, BaseCallBack callback) {
        sendDownLoadFailedStringCallback(WS_State.OTHERS, erorRequestError, callback);
    }

    private void sendDownLoadFailedStringCallback(final int state, final String erorRequestError, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ws_ret ret = new ws_ret(state, erorRequestError);
                callback.onFail(ret);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessDownLoadCallback(String successModel, BaseCallBack callback) {
        sendSuccessDownLoadString(WS_State.SUCCESS, successModel, callback);
    }

    private void sendSuccessDownLoadString(final int state, final String successModel, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(successModel);
                callback.onAfter();
            }
        });
    }


    private void sendProgressDownLoadCallback(long l, BaseCallBack callback) {
        sendProgressDownLoadString(l, callback);
    }

    private void sendProgressDownLoadString(final long l, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(l);
            }
        });

    }


    private void sendFSCallBack(final int state, final String msg, final BaseCallBack callback) {
        sendErrorString(state, msg, callback);
    }

    private void sendErrorString(final int state, final String msg, final BaseCallBack callback) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ws_ret ret = new ws_ret(state, msg);
                callback.onFail(ret);
                callback.onAfter();
            }
        });
    }

    private void sendESCallBack(final int state, final String msg, final BaseCallBack callback) {

        sendEmptyString(state, msg, callback);
    }

    private void sendEmptyString(final int state, final String msg, final BaseCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ws_ret ret = new ws_ret(state, msg);
                callback.onNoData(ret);
                callback.onAfter();
            }
        });
    }

    private void sendPostSuccessCallBack(final Object object, final BaseCallBack ret) {

        sendSuccessString(object, ret);

    }

    private void sendSuccessString(final Object o, final BaseCallBack ret) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                ret.onSuccess(o);
                ret.onAfter();
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
