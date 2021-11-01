package com.thlsoft.downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.thlsoft.downloader.Constants;
import com.thlsoft.downloader.OnProgressListener;
import com.thlsoft.downloader.Progress;


public class ProgressHandler extends Handler {

    private final OnProgressListener listener;

    public ProgressHandler(OnProgressListener listener) {
        super(Looper.getMainLooper());
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.UPDATE:
                if (listener != null) {
                    final Progress progress = (Progress) msg.obj;
                    listener.onProgress(progress);
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
}
