package android.jiang.com.library.callback;


import okhttp3.Response;

/**
 * B可以随便写
 * Created by jiang on 16/8/24.
 */
public abstract class UploadCallBack<B> extends BaseCallBack {


    @Override
    public void onSuccess(Object o) {
        success();
    }

    @Override
    public void onNoData(android.jiang.com.library.ws_ret ret) {

    }

    public abstract void success();


    @Override
    public void onBefore() {

    }

    @Override
    public void onAfter() {

    }

    @Override
    public void onFinishResponse(Response response) {

    }


}
