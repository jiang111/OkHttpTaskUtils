package android.jiang.com.library.request;

import android.jiang.com.library.Param;
import android.jiang.com.library.utils.HeaderUtils;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.List;
import java.util.Map;


/**
 * Created by jiang on 16/7/28.
 */
public class UploadRequest {

    public static Request buildPutRequest(String url, Map<String, String> headers, Object tab, RequestBody requestBody) {

        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.post(requestBody).url(url);
        reqBuilder.tag(tab);

        List<Param> valdatedHeaders = HeaderUtils.validateHeaders(headers);
        if (valdatedHeaders != null && valdatedHeaders.size() > 0) {
            for (int i = 0; i < valdatedHeaders.size(); i++) {
                Param param = valdatedHeaders.get(i);
                String key = param.key;
                String value = param.value;
                reqBuilder.addHeader(key, value);
            }

        }
        return reqBuilder.build();


    }
}
