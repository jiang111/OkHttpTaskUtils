package android.jiang.com.library.request;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by jiang on 22/11/2017.
 */
public class UploadSliceRequestBody extends RequestBody {
    private File file = null;
    private long file_length = 0;
    private int EACH_COUNT = 1024 * 100;


    public UploadSliceRequestBody(String filePath) {
        this.file = new File(filePath);
        file_length = this.file.length();
    }

    @Override
    public long contentLength() throws IOException {
        return file_length;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
        try {
            byte[] tmp = new byte[EACH_COUNT];
            randomAccessFile.seek(0);
            int n;
            //可在此设置进度
            while ((n = randomAccessFile.read(tmp, 0, EACH_COUNT)) != -1) {
                sink.write(tmp, 0, n);
            }
            sink.flush();
        } finally {
            randomAccessFile.close();
        }

    }
}
