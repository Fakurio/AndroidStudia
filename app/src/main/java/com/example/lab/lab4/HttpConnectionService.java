package com.example.lab.lab4;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnectionService extends IntentService {
    private final static String DOWNLOAD_FILE = "40";
    private final static String FILE_URL_ARG = "1";
    private DownloadNotificationService downloadNotificationService;


    public HttpConnectionService() {
        super("HttpConnectionService");
    }

    public static void startService(Context context, String arg) {
        Intent intent = new Intent(context, HttpConnectionService.class);
        intent.setAction(DOWNLOAD_FILE);
        intent.putExtra(FILE_URL_ARG, arg);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            String action = intent.getAction();
            if(action != null && action.equals(DOWNLOAD_FILE)) {
                String url = intent.getStringExtra(FILE_URL_ARG);
                downloadNotificationService = new DownloadNotificationService(getApplicationContext());
                downloadNotificationService.createNotificationChannel();
                try {
                    downloadFile(url);
                } catch (IOException e) {
                    Log.e("HttpConnectionService", "Błąd pobierania pliku");
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileName(String url) {
        int lastSlash = url.lastIndexOf("/");
        return url.substring(lastSlash);
    }

    private void downloadFile(String urlString) throws IOException {
        HttpsURLConnection conn = null;
        DataInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            Log.i("HttpConnectionService", "Rozpoczynam pobieranie");
            URL url = new URL(urlString);
            String fileName = getFileName(url.getFile());
            File outputFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Download" + fileName);
            if(outputFile.exists()) {
                outputFile.delete();
            }
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            inputStream = new DataInputStream(conn.getInputStream());
            outputStream = new FileOutputStream(outputFile.getPath());
            byte buffer[] = new byte[1024];
            int bytesRead;
            int fileSize = conn.getContentLength();
            int progress = 0;
            int step = 1;
            while((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
                progress += bytesRead;
                if(progress >= (fileSize/10)*step) {
                    downloadNotificationService.updateNotificationProgress(step*10);
                    step++;
                }
                outputStream.write(buffer, 0, bytesRead);
            }
            Log.i("HttpConnectionService", "Pobieranie zakończone");
            downloadNotificationService.setAsCompleted();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null) {
                outputStream.close();
            }
            if(conn != null) {
                conn.disconnect();
            }
        }
    }
}
