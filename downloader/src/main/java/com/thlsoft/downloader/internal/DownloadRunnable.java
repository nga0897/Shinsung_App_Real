package com.thlsoft.downloader.internal;


import com.thlsoft.downloader.Error;
import com.thlsoft.downloader.Priority;
import com.thlsoft.downloader.Response;
import com.thlsoft.downloader.Status;
import com.thlsoft.downloader.request.DownloadRequest;

public class DownloadRunnable implements Runnable {

    public final Priority priority;
    public final int sequence;
    public final DownloadRequest request;

    DownloadRunnable(DownloadRequest request) {
        this.request = request;
        this.priority = request.getPriority();
        this.sequence = request.getSequenceNumber();
    }

    @Override
    public void run() {
        request.setStatus(Status.RUNNING);
        DownloadTask downloadTask = DownloadTask.create(request);
        Response response = downloadTask.run();
        if (response.isSuccessful()) {
            request.deliverSuccess();
        } else if (response.isPaused()) {
            request.deliverPauseEvent();
        } else if (response.getError() != null) {
            request.deliverError(response.getError());
        } else if (!response.isCancelled()) {
            request.deliverError(new Error());
        }
    }

}
