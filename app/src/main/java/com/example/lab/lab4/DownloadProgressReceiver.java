package com.example.lab.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DownloadProgressReceiver extends BroadcastReceiver {
    private FileDownloadListener listener;

    public DownloadProgressReceiver(FileDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction() != null
                && intent.getAction().equals(HttpConnectionService.DOWNLOAD_NOTIFICATION)) {
            ProgressInfo progressInfo = intent.getParcelableExtra(HttpConnectionService.DOWNLOAD_PROGRESS_INFO);
            if(progressInfo != null && listener != null) {
                listener.onDownloadProgressUpdate(progressInfo);
            }
        }
    }
}
