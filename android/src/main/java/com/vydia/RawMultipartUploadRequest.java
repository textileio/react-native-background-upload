package com.vydia.RNUploader;

import android.content.Context;
import android.content.Intent;

import net.gotev.uploadservice.ContentType;
import net.gotev.uploadservice.HttpUploadRequest;
import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadFile;
import net.gotev.uploadservice.UploadRequest;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadTask;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 * HTTP/Multipart upload request with a pre-constructed payload.
 *
 * @author gotev (Aleksandar Gotev)
 * @author eliasnaur
 * @author textile
 *
 */
public class RawMultipartUploadRequest extends HttpUploadRequest<RawMultipartUploadRequest> {

    private static final String LOG_TAG = RawMultipartUploadRequest.class.getSimpleName();
    private String boundary = "";

    /**
     * Creates a new raw multipart upload request.
     *
     * @param context application context
     * @param uploadId unique ID to assign to this upload request.<br>
     *                 It can be whatever string you want, as long as it's unique.
     *                 If you set it to null or an empty string, an UUID will be automatically
     *                 generated.<br> It's advised to keep a reference to it in your code,
     *                 so when you receive status updates in {@link UploadServiceBroadcastReceiver},
     *                 you know to which upload they refer to.
     * @param serverUrl URL of the server side script that will handle the multipart form upload.
     *                  E.g.: http://www.yourcompany.com/your/script
     * @throws IllegalArgumentException if one or more arguments are not valid
     * @throws MalformedURLException if the server URL is not valid
     */
    public RawMultipartUploadRequest(final Context context, final String uploadId, final String serverUrl)
            throws IllegalArgumentException, MalformedURLException {
        super(context, uploadId, serverUrl);
    }

    /**
     * Creates a new raw multipart upload request and automatically generates an upload id, that will
     * be returned when you call {@link UploadRequest#startUpload()}.
     *
     * @param context application context
     * @param serverUrl URL of the server side script that will handle the multipart form upload.
     *                  E.g.: http://www.yourcompany.com/your/script
     * @throws IllegalArgumentException if one or more arguments are not valid
     * @throws MalformedURLException if the server URL is not valid
     */
    public RawMultipartUploadRequest(final Context context, final String serverUrl)
            throws MalformedURLException, IllegalArgumentException {
        this(context, null, serverUrl);
    }

    @Override
    protected void initializeIntent(Intent intent) {
        super.initializeIntent(intent);
        intent.putExtra(RawMultipartUploadTask.RAW_BOUNDARY, boundary);
    }

    @Override
    protected Class<? extends UploadTask> getTaskClass() {
        return RawMultipartUploadTask.class;
    }

    /**
     * Sets the multipart payload for this upload request.
     *
     * @param filePath path to the payload file that you want to upload
     * @throws FileNotFoundException if the file does not exist at the specified path
     * @throws IllegalArgumentException if one or more parameters are not valid
     * @return {@link RawMultipartUploadRequest}
     */
    public RawMultipartUploadRequest setPayload(final String filePath)
            throws FileNotFoundException, IllegalArgumentException {
        UploadFile file = new UploadFile(filePath);
        params.addFile(file);
        return this;
    }

    /**
     * Sets the multipart boundary used in the payload.
     * @return request instance
     */
    public RawMultipartUploadRequest setBoundary(final String boundary) {
        this.boundary = boundary;
        return this;
    }
}
