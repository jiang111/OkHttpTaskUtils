package com.jiang.android.okhttptaskutils;

import com.google.gson.annotations.Expose;

/**
 * Created by jiang on 2017/6/12.
 */

public class BaseModel<T> {

    @Expose
    private int status;
    @Expose
    private T data;
    @Expose
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
