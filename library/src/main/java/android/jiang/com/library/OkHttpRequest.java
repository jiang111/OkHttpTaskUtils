/**
 * created by jiang, 11/9/15
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

import android.jiang.com.library.callback.BaseCallBack;
import android.jiang.com.library.exception.NotPermissionException;
import android.jiang.com.library.listener.NetTaskListener;
import android.text.TextUtils;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by jiang on 11/9/15.
 */
public class OkHttpRequest {

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
    public static void doJob(NetTaskListener obj, String url, Object tag, Map<String, String> params, final BaseCallBack callBack, Map<String, String> headers, boolean notConvert, int type) {
        OkHttpTask.getInstance().filterData(obj, url, tag, params, callBack, headers, notConvert, type);
    }

    public static void downLoadFile(String url, String path, String fileName, BaseCallBack callBack, Object tag) {
        OkHttpTask.getInstance().doJobDownLoadFile(url, path, fileName, callBack, tag, null);
    }

    private static void cancel(Object tag) {
        OkHttpTask.getInstance().cancelTask(tag);
    }


    public static class Builder {
        NetTaskListener ct;
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private String path;
        private String fileName;
        private int type;
        private boolean notConvert;
        private BaseCallBack callBack;

        public Builder build() {
            return this;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Builder with(NetTaskListener ct) {
            this.ct = ct;
            return this;
        }

        /**
         * true 则不需要进行string转换
         *
         * @param notConvert
         * @return
         */
        public Builder notConvert(boolean notConvert) {
            this.notConvert = notConvert;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }


        public void get(BaseCallBack callBack) {
            if (validateParams()) {
                type = OkHttpTask.TYPE_GET;
                doJob(ct, url, tag, params, callBack, headers, notConvert, OkHttpTask.TYPE_GET);
            }
        }

        private boolean validateParams() {
            if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
                throw new NotPermissionException("url不合法");
            }
            if (callBack == null) {
                throw new NotPermissionException("没有CallBack");
            }
            return true;
        }

        public void post(BaseCallBack c) {
            this.callBack = c;
            if (validateParams()) {
                type = OkHttpTask.TYPE_POST;
                doJob(ct, url, tag, params, callBack, headers, notConvert, OkHttpTask.TYPE_POST);
            }
        }

        public void put(BaseCallBack c) {
            this.callBack = c;
            if (validateParams()) {
                type = OkHttpTask.TYPE_PUT;
                doJob(ct, url, tag, params, callBack, headers, notConvert, OkHttpTask.TYPE_PUT);
            }
        }

        public void delete(BaseCallBack c) {
            this.callBack = c;
            if (validateParams()) {
                type = OkHttpTask.TYPE_DELETE;
                doJob(ct, url, tag, params, callBack, headers, notConvert, OkHttpTask.TYPE_DELETE);
            }
        }

        public void downLoad(BaseCallBack c) {
            this.callBack = c;
            if (validateParams()) {
                downLoadFile(url, path, fileName, callBack, tag);
            }
        }

        public void execute(BaseCallBack c) {
            this.callBack = c;
            if (validateParams()) {
                doJob(ct, url, tag, params, callBack, headers, notConvert, type);
            }
        }


    }


}
