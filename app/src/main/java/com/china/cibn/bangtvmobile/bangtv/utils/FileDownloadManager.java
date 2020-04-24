package com.china.cibn.bangtvmobile.bangtv.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.china.cibn.bangtvmobile.bangtv.module.common.SplashActivity;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/4/11.
 */

public class FileDownloadManager {
    public static DownloadManager downloadManager;
    private Context context;
    private static FileDownloadManager instance;
    public static long mReqId;
    private DownloadChangeObserver mDownLoadChangeObserver;
    private OnUpdateListener mUpdateListener;
    private FileDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mDownLoadChangeObserver = new DownloadChangeObserver(new Handler());
        this.context = context.getApplicationContext();
    }

    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileDownloadManager(context);
        }
        return instance;
    }
    public void setUpdateListener(OnUpdateListener mUpdateListener) {
        this.mUpdateListener = mUpdateListener;
    }
    /**
     * @param uri
     * @param title
     * @param description
     * @return download id
     */
    public void startDownload(String uri, String title, String description,String appName) {
        File apkFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), appName+".apk");
        if (apkFile != null && apkFile.exists()) {
            apkFile.delete();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setTitle(title);
        request.setDescription(description);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, appName+".apk");
        request.setMimeType("application/vnd.android.package-archive");
        mReqId = downloadManager.enqueue(request);
    }

            /**
             * 获取文件保存的路径
             *
             * @param downloadId an ID for the download, unique across the system.
             *                   This ID is used to make future calls related to this download.
             * @return file path
             * @see FileDownloadManager#getDownloadUri(long)
             */
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see FileDownloadManager#getDownloadPath(long)
     */
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
    private void updateView() {
        int[] bytesAndStatus = new int[]{0, 0, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReqId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (mUpdateListener != null) {
            mUpdateListener.update(bytesAndStatus[0], bytesAndStatus[1]);
        }

        Log.i(TAG, "下载进度：" + bytesAndStatus[0] + "/" + bytesAndStatus[1] + "");
    }

    class DownloadChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }

    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
    public interface AndroidOInstallPermissionListener {
        void permissionSuccess();

        void permissionFail();
    }
    public interface OnUpdateListener {
        void update(int currentByte, int totalByte);
    }
}