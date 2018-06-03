package com.vydia.RNUploader;

import android.content.Intent;

import net.gotev.uploadservice.HttpUploadTask;
import net.gotev.uploadservice.NameValue;
import net.gotev.uploadservice.UploadFile;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.http.BodyWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Implements an HTTP Multipart raw upload task.
 *
 * @author gotev (Aleksandar Gotev)
 * @author eliasnaur
 * @author cankov
 * @author textile
 */
public class RawMultipartUploadTask extends HttpUploadTask {

    protected static final String RAW_BOUNDARY = "rawMultipartBoundary";

    @Override
    protected void init(UploadService service, Intent intent) throws IOException {
        super.init(service, intent);

        String boundary = intent.getStringExtra(RAW_BOUNDARY);

        httpParams.addRequestHeader("Connection", "close");
        httpParams.addRequestHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    protected long getBodyLength() throws UnsupportedEncodingException {
        return getFilesLength();
    }

    @Override
    public void onBodyReady(BodyWriter bodyWriter) throws IOException {
        writeFiles(bodyWriter);
    }

    private long getFilesLength() throws UnsupportedEncodingException {
        long total = 0;

        for (UploadFile file : params.getFiles()) {
            total += getTotalMultipartBytes(file);
        }

        return total;
    }

    private long getTotalMultipartBytes(UploadFile file)
            throws UnsupportedEncodingException {
        return file.length(service);
    }

    private void writeFiles(BodyWriter bodyWriter) throws IOException {
        for (UploadFile file : params.getFiles()) {
            if (!shouldContinue)
                break;

            final InputStream stream = file.getStream(service);
            bodyWriter.writeStream(stream, this);
            stream.close();
        }
    }

    @Override
    protected void onSuccessfulUpload() {
        for (UploadFile file : params.getFiles()) {
            addSuccessfullyUploadedFile(file.getPath());
        }
        params.getFiles().clear();
    }

}
