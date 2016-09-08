package android.jiang.com.library.log;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


public class LInterceptor implements Interceptor {


    public LInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return res(request, response);
    }

    private Response res(Request request, Response response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("========request'log=======\n");
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            stringBuilder.append("url:" + clone.request().url() + "\n");
            stringBuilder.append("method:" + request.method() + "\n");
            Headers headers = request.headers();
            if (headers != null && headers.size() > 0) {
                stringBuilder.append("headers:\n" + headers.toString());
            }
            stringBuilder.append("response:\n");
            stringBuilder.append("code:" + clone.code() + "; ");
            if (!TextUtils.isEmpty(clone.message()))
                stringBuilder.append("message:" + clone.message() + "\n");
            LogUtils.d(stringBuilder.toString());
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    stringBuilder.append("body: " + bodyToString(request) + "\n");
                    String resp = body.string();
                    LogUtils.json(resp);
                    body = ResponseBody.create(mediaType, resp);
                    return response.newBuilder().body(body).build();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "parse failed";
        }
    }
}
