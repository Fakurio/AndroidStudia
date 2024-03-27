package com.example.lab.lab4;

public interface HttpConnectionListener {
    void onDownloadInfoComplete(FileInfo result);
    void beforeDownloadInfoStart();
}
