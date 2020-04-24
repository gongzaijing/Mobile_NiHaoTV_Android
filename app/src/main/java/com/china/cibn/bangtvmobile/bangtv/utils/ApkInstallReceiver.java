package com.china.cibn.bangtvmobile.bangtv.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.home.update.AndroidOPermissionActivity;
import com.facebook.stetho.common.LogUtil;

import java.io.File;

import static android.content.ContentValues.TAG;
import static com.china.cibn.bangtvmobile.bangtv.utils.FileDownloadManager.downloadManager;
import static com.china.cibn.bangtvmobile.bangtv.utils.FileDownloadManager.mReqId;

/**
 * Created by Administrator on 2018/4/11.
 */

public class ApkInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean haveInstallPermission;
        // 兼容Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                // 弹窗，并去设置页面授权
                final FileDownloadManager.AndroidOInstallPermissionListener listener = new FileDownloadManager.AndroidOInstallPermissionListener() {
                    @Override
                    public void permissionSuccess() {
                        installApk(context, intent);
                    }

                    @Override
                    public void permissionFail() {
                        ToastUtil.ShortToast(context.getString(R.string.apkInstallReceiver_tvfail));
                    }
                };

                AndroidOPermissionActivity.sListener = listener;
                Intent intent1 = new Intent(context, AndroidOPermissionActivity.class);
                context.startActivity(intent1);

            } else {
                installApk(context, intent);
            }
        } else {
            installApk(context, intent);
        }

    }

    private void installApk(Context context, Intent intent) {
        long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        Log.e(TAG, "收到广播");
        Uri uri;
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);

        if (completeDownLoadId == mReqId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                uri = downloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(context, completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                uri = FileProvider.getUriForFile(context,
                        "com.china.cibn.bangtvmobile.fileProvider",
                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"BangTV.apk"));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            // 安装应用
            LogUtil.e("BangTV", "下载完成了");

            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intentInstall);
        }
    }

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

}