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
package com.jiang.android.okhttptask.request;


import com.jiang.android.okhttptask.Param;
import com.jiang.android.okhttptask.utils.HeaderUtils;
import com.jiang.android.okhttptask.utils.HttpUtils;
import com.squareup.okhttp.Request;

import java.util.List;
import java.util.Map;


/**
 * 生成get请求需要的request
 * Created by jiang on 15/10/16.
 */
public class getRequest {

    public static Request buildGetRequest(String url, Map<String, String> params, Object tag, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();

        List<Param> valdatedHeaders = HeaderUtils.validateHeaders(headers);
        if (valdatedHeaders != null && valdatedHeaders.size() != 0) {
            for (int i = 0; i < valdatedHeaders.size(); i++) {
                Param param = valdatedHeaders.get(i);
                String key = param.key;
                String value = param.value;
                builder.addHeader(key, value);
            }

        }

        if (params != null && params.size() != 0) {
            String par = HttpUtils.parseParams2String(params);
            String api = new StringBuffer().append(url).append("?").append(par).toString();
            builder.url(api);
        } else {
            builder.url(url);
        }

        if (tag != null) {
            builder.tag(tag);
        }

        return builder.build();
    }


}
