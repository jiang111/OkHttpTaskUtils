package com.jiang.android.okhttptaskutils;

import com.google.gson.annotations.Expose;

/**
 * Created by jiang on 16/8/24.
 */

public class UploadModel {

    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
