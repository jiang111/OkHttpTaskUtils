package android.jiang.com.library.request;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by jiang on 22/11/2017.
 */
public class UploadSliceRequestBody extends RequestBody {
    private File file = null;
    private long file_length = 0;

    public UploadSliceRequestBody(String filePath) {
        this.file = new File(filePath);
        file_length = this.file.length();
    }

    @Override
    public long contentLength() throws IOException {
        return -1;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.writeAll(Okio.buffer(Okio.source(file)));

    }
}
