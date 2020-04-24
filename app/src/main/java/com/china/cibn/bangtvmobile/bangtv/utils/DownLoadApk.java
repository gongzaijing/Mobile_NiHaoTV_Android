package com.china.cibn.bangtvmobile.bangtv.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.facebook.stetho.common.LogUtil;


/**
 * Created by Administrator on 2018/4/11.
 */

public class DownLoadApk {
    public static final String TAG = DownLoadApk.class.getSimpleName();
   public static  long id;

    public static  boolean isOk=false;
    public static void download(Context context, String url, String title, final String appName) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downloadId =sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if (downloadId != -1L) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(context);
                ToastUtil.ShortToast( "更新下载中，请稍后······");
                //启动更新界面
                Uri uri = fdm.getDownloadUri(downloadId);
                if (uri != null) {
                    if (compare(getApkInfo(context, uri.getPath()), context)) {
                        startInstall(context, uri);
                        return;
                    } else {
                        fdm.getDownloadManager().remove(downloadId);
                    }
                }
                start(context, url, title,appName);
        } else {
            start(context, url, title,appName);
        }
    }

    private static void start(Context context, String url, String title,String appName) {
        FileDownloadManager.getInstance(context).startDownload(url,
                title, "下载完成后点击打开",appName);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID,id).commit();
    }

    public static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }
    // 查询下载进度，文件总大小多少，已经下载多少？
    public static void query() {
        DownloadManager.Query downloadQuery = new DownloadManager.Query();
        downloadQuery.setFilterById(id);
        Cursor cursor = FileDownloadManager.downloadManager.query(downloadQuery);
        if (cursor != null && cursor.moveToFirst()) {
            int fileName = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            int fileUri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            String fn = cursor.getString(fileName);
            String fu = cursor.getString(fileUri);

            int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

            // 下载的文件总大小
            int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);

            // 截止目前已经下载的文件总大小
            int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);
if(totalSizeBytes==bytesDownloadSoFar){
    isOk=true;
}
            LogUtil.d(
                    "from " + fu + " 下载到本地 " + fn + " 文件总大小:" + totalSizeBytes + " 已经下载:" + bytesDownloadSoFar);

            cursor.close();
        }
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}