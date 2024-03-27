package com.example.lab.lab4;

import android.os.AsyncTask;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnectionTask extends AsyncTask<String, Void, FileInfo> {
    private HttpConnectionListener listener;

    public HttpConnectionTask(HttpConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.beforeDownloadInfoStart();
    }

    @Override
    protected FileInfo doInBackground(String... strings) {
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(strings[0]);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int size = conn.getContentLength();
            String type = conn.getContentType();
            return new FileInfo(size, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(FileInfo info) {
        super.onPostExecute(info);
        listener.onDownloadInfoComplete(info);
    }
}
