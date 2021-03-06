/**
 * created by jiang, 15/10/19
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
package android.jiang.com.library.utils;

import android.content.Context;
import android.jiang.com.library.Param;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;


/**
 * 相关网络请求的工具类
 * Created by jiang on 15/10/16.
 */
public class HttpUtils {


    public static String parseParams2String(Map<String, String> param) {
        if (param == null || param.size() == 0)
            return "";

        StringBuffer buffer = new StringBuffer();

        Set<Map.Entry<String, String>> entrySet = param.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            Param par = new Param(entry.getKey(), entry.getValue());
            buffer.append(par.toString()).append("&");

        }
        return buffer.substring(0, buffer.length() - 1);

    }


    public static boolean isNetworkConnected(Context ct) {
        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int getNetworkType(Context ct) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static String getContentByString(boolean notConvert, String content) {

        if (TextUtils.isEmpty(content))
            return "";
        if (notConvert)
            return content;
        return content.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#39;", "\'")
                .replace("&#34;", "\"")
                .replace("&amp;", "&")
                .replace("&quot;", "\"");

    }

    public static String getContent(boolean notConvert, String content) {

        if (TextUtils.isEmpty(content))
            return "";
        if (notConvert)
            return content;
        return content.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#39;", "\'")
                .replace("&#34;", "\\\"")
                .replace("&amp;", "&")
                .replace("&quot;", "\\\"");

    }

    public static String getXSSContent(String content) {
        if (TextUtils.isEmpty(content))
            return "";
        return content.replace("\\u003c", "<")
                .replace("\\u003e", ">")
                .replace("\\u0026", "&");
    }

    /**
     * 这个方法专门给消息中心的列表页用的
     *
     * @param content
     * @return
     */
    public static String getAllContent(String content) {
        String result = getXSSContent(content);
        return getContentByString(false, result);
    }

    /**
     * 通用
     *
     * @param content
     * @return
     */
    public static String getAllContentByString(String content) {
        String result = getXSSContent(content);
        return getContent(false, result);

    }

}
