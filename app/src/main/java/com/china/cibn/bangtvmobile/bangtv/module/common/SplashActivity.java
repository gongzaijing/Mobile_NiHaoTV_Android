package com.china.cibn.bangtvmobile.bangtv.module.common;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.dal.UserHelper;
import com.china.cibn.bangtvmobile.bangtv.entity.AuthenticationInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.CheckUpdateInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.OpenViewInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.DownLoadApk;
import com.china.cibn.bangtvmobile.bangtv.utils.SystemUiVisibilityUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.china.cibn.bangtvmobile.bangtv.utils.DownLoadApk.isOk;

/**
 * Created by hcc on 16/8/7 14:12
 *
 * <p/>
 * 启动页界面
 */
public class SplashActivity extends RxBaseActivity {

      private Unbinder bind;


    private final String _dev_login_info = "_DEV_LOGIN_INFO_";

    private final String _dev_login_token = "_DEV_LOGIN_TOKEN_";

    private final String _dev_login_time = "_DEV_LOGIN_TIME_";

    public static SplashActivity instance;

    private AuthenticationInfo addressListBeanList = new AuthenticationInfo();

    private LocalData _ld = null;

    private boolean is_internet_access = false;

    private String UpdatePath = "";

    private String UpdateVersionName;

    private String svc = null;

    private String versionId;

    private String ForceUpdate = "";


    private String UpdateMd5 = null;

    private List<CheckUpdateInfo.DataBean> checkUpdateInfos = new ArrayList<>();

    private String desc = "";

    private AlertDialog.Builder mDialog;

    private AlertDialog.Builder mDialogCheck;


    private DownloadManager downloadManager;
    private long Id;

    private Thread myThread;

    private OpenViewInfo openViewInfo;

    //fix by gz begin 2018/05/25
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    //fix by gz end   2018/05/25

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_bangtv;
    }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //fix by gz begin 2018/05/25
        addNeedPermission();
        instance = SplashActivity.this;
        _ld=new LocalData(null);
        bind = ButterKnife.bind(this);
        SystemUiVisibilityUtil.hideStatusBar(getWindow(), true);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void initToolBar() {

    }


    /**
     * 用户认证
     * */
  private void initAuthentication(){
      BangtvApp.mInstance.initData();
    RetrofitHelper.getNoCacheAppAPI().getAuthen(BangtvApp.versionCodeUrl)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resurtBean-> {
              addressListBeanList=resurtBean;
              if(resurtBean.getState().equals("1111")){
                  AppGlobalVars.DEFAULT_TOKEN=addressListBeanList.getToken();
                  AppGlobalVars.DEFAULT_ID=addressListBeanList.getUserId();
                  _ld.setKV(_dev_login_info,
                          resurtBean.getUserId());
                  _ld.setKV(_dev_login_token,
                          resurtBean.getToken());
                  _ld.setKV(_dev_login_time, String
                          .valueOf(System
                                  .currentTimeMillis()));
                  getVipInfo();
                  _checkUpd();
              }else{
                if(resurtBean.getState().equals("2222")){
                    FeedBack();
                }else{
                  ToastUtil.ShortToast(resurtBean.getState());
                }
              }
            },throwable->{
            ToastUtil.ShortToast(throwable.toString());
    });
  }
  private void getVipInfo(){
      LocalData _ld = new LocalData(null);
      String loginInfo = _ld.getKV("_DEV_LOGIN_INFO_");
      String loginToken = _ld.getKV("_DEV_LOGIN_TOKEN_");
      if (TextUtils.isEmpty(loginInfo))
          return;

      String user = _ld.getKV(AppGlobalConsts.PERSIST_NAMES.CURRENT_USER
              .name());
      if (user == null || user.isEmpty()) {
          AppGlobalVars.USER_ID = loginInfo;
          AppGlobalVars.USER_TOKEN=loginToken;
          _ld.setKV(AppGlobalConsts.PERSIST_NAMES.DEFAULT_USER.name(),
                  AppGlobalVars.USER_ID);
      } else {
          UserHelper uh = new UserHelper(null);
          ContentValues cvs = uh.getUser(user);
          if (cvs.containsKey("token")) {
              AppGlobalVars.USER_PIC = cvs.getAsString("wxheadimgurl");
              AppGlobalVars.USER_NICK_NAME = cvs.getAsString("wxname");
              AppGlobalVars.USER_TOKEN = cvs.getAsString("token");
          }
          AppGlobalVars.USER_ID = user;
      }

  }
/**
 *  检测升级
 **/
private void _checkUpd(){
    try {
        svc = getApplicationContext().getPackageManager().getPackageInfo(
                getApplicationContext().getPackageName(),
                PackageManager.GET_CONFIGURATIONS).versionCode
                + "";
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
        svc = null;
    }
        RetrofitHelper.getNoCacheAppAPI().getCheckUpdate(BangtvApp.versionCodeUrl)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    checkUpdateInfos.addAll(resultBeans.getData());
                    getBootImage();
                    String packageLocation = null;
                    for (int i = 0; i < checkUpdateInfos.size(); i++) {
                        versionId = checkUpdateInfos.get(i).getVersionId();
                        UpdateVersionName = checkUpdateInfos.get(i).getVersionName();
                        packageLocation = checkUpdateInfos.get(i).getPackageLocation();
                        UpdateMd5 = checkUpdateInfos.get(i).getMd5();
                        desc = checkUpdateInfos.get(i).getDesc();
                        ForceUpdate = checkUpdateInfos.get(i).getForced();
                    }
                    if(versionId != null
                            && !TextUtils.isEmpty(versionId)
                            && svc != null
                            && !TextUtils.isEmpty(svc)
                            && Integer.parseInt(versionId) > Integer
                            .parseInt(svc)){
                        forceUpdate(this,getString(R.string.app_name),packageLocation,desc);
                    }else{
                        setUpSplash();
                    }
                }, throwable -> {
                    Log.i("",throwable.toString());
                    setUpSplash();
});


}
private void getBootImage(){
    RetrofitHelper.getNoCacheAppAPI().getBootimg(BangtvApp.versionCodeUrl)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
                openViewInfo=resultBeans;
                AppGlobalVars.IP_ADRESS= openViewInfo.getIp();
            }, throwable -> {
                Log.i("", throwable.toString());
            });
}
    public void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName+getString(R.string.splashactivity_text6));
        mDialog.setMessage(updateinfo);
        mDialog.setNegativeButton(getString(R.string.splashactivity_text7), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(ForceUpdate.equals("1")){
                    System.exit(0);
                }else{
                    setUpSplash();
                }
            }
        });
        mDialog.setPositiveButton(getString(R.string.splashactivity_text8), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //检测网络状态 4G、Wifi
                if (context != null) {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
                    //1.判断是否有网络连接
                    boolean networkAvailable = networkInfo.isAvailable();
                    //2.获取当前网络连接的类型信息
                    int networkType = networkInfo.getType();
                    if (ConnectivityManager.TYPE_WIFI == networkType) {
                        // enqueue 开始启动下载...
                        DownLoadApk.download(SplashActivity.this,downUrl,"BangTV",appName);
//                        myThread=new  MyThread();
//                        myThread.start();
                        if(ForceUpdate.equals("1")){
                            System.exit(0);
                        }else{
                            setUpSplash();
                        }
                    } else if (ConnectivityManager.TYPE_MOBILE == networkType) {
                        dialog.dismiss();
                        CheckUpdate(appName,downUrl);
                    }
                }
            }
        }).setCancelable(false).create().show();
    }
//    private class MyThread extends Thread {
//        @Override
//        public void run() {
//            try {
//                while (!isOk){
//                    DownLoadApk.query();
//                    Thread.sleep(3000);
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 选择4G网络是否下载
     * */
    public void CheckUpdate(final String appName, final String downUrl) {
        mDialogCheck = new AlertDialog.Builder(this);
        mDialogCheck.setTitle(getString(R.string.splashactivity_text1));
        mDialogCheck.setNegativeButton(getString(R.string.splashactivity_text2), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDialogCheck.setCancelable(true);
                if(ForceUpdate.equals("1")){
                    System.exit(0);
                }else{
                    setUpSplash();
                }
            }
        });
        mDialogCheck.setPositiveButton(getString(R.string.splashactivity_text3), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadApk.download(SplashActivity.this,downUrl,getString(R.string.app_name),appName);
                mDialogCheck.setCancelable(true);
                if(ForceUpdate.equals("1")){
                    System.exit(0);
                }else{
                    setUpSplash();
                }
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 用户认证，大陆用户无法使用
     * */
    public void FeedBack() {
        mDialogCheck = new AlertDialog.Builder(this);
        mDialogCheck.setTitle(getString(R.string.splashactivity_text4));
        mDialogCheck.setPositiveButton(getString(R.string.splashactivity_text5), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogCheck.setCancelable(true);
                    System.exit(0);
            }
        }).setCancelable(false).create().show();
    }
    /**
     * 检测网络
     * @return
     */
    private boolean checkNet()
    {
        ConnectivityManager connManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connManager.getActiveNetworkInfo();
        checkInternetAccess(); // 检测外网
        int i = 0;
        while (!is_internet_access && i <= 10) {
            i++;
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ni != null && ni.isConnected() && is_internet_access) {

            return true;
        } else {
            return false;
        }
    }
    /**
     * 检测外网是否连通
     * @return
     */
    private boolean checkInternetAccess() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress address = null;
                try {
                    address = InetAddress.getByName("www.baidu.com");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (address != null) {
                    is_internet_access = true;
                } else {
                    is_internet_access = false;
                }
            }
        }).start();
        return is_internet_access;
    }


    private void setUpSplash() {

    Observable.timer(2000, TimeUnit.MILLISECONDS)
        .compose(bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aLong -> {
          finishTask();
        });
  }

    @Override
    public void finishTask() {
        super.finishTask();
        startActivity(new Intent(SplashActivity.this, BangTVMainActivity.class));
      SplashActivity.this.finish();
    }
//  private void finishTask() {
//      startActivity(new Intent(SplashActivity.this, BangTVMainActivity.class));
//      SplashActivity.this.finish();
//  }


  @Override
  protected void onDestroy() {

        super.onDestroy();
        bind.unbind();
    }

    //fix by gz begin 2018/05/25
    public void addNeedPermission() {

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= 23) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }else{
                if(checkNet()){
                    initAuthentication();
                }else{
                    ToastUtil.LongToast(getString(R.string.error_text));
                }
            }
        }else{
            if(checkNet()){
                initAuthentication();
            }else{
                ToastUtil.LongToast(getString(R.string.error_text));
            }
        }

    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.splashactivity_text9))
                .setMessage(getString(R.string.splashactivity_text10))
                .setPositiveButton(getString(R.string.splashactivity_text11), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton(getString(R.string.splashactivity_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},321);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    if(checkNet()){
                        initAuthentication();
                    }else{
                        ToastUtil.LongToast(getString(R.string.error_text));
                    }
                    Toast.makeText(this, getString(R.string.splashactivity_success), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.splashactivity_text12))
                .setMessage(getString(R.string.splashactivity_text13))
                .setPositiveButton(getString(R.string.splashactivity_text11), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton(getString(R.string.splashactivity_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //fix by gz end   2018/05/25
}
