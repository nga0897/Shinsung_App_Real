package com.thlsoft.downloader;

public interface OnDownloadListener {

    void onDownloadComplete();

    void onError(Error error);

}
