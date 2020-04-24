package com.china.cibn.bangtvmobile.bangtv.module.home.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.entity.CheckUpdateInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.DownLoadApk;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/30 14:12
 *
 * <p/>
 * 设置与帮助
 */
public class SettingActivity extends RxBaseActivity {

  @BindView(R.id.app_version_code)
  TextView mVersionCode;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.btn_logout)
  Button btnLogout;

  private List<CheckUpdateInfo.DataBean> checkUpdateInfos = new ArrayList<>();

  private String UpdatePath = "";

  private String UpdateVersionName;

  private String svc = null;

  private String versionId;

  private String ForceUpdate = "";

  private String desc = "";

  private String UpdateMd5 = null;

  private AlertDialog.Builder mDialog;

  private LocalData localData;

  private ProgressDialog pd;

  public static SettingActivity newInstance() {

    return new SettingActivity();
  }
  @Override
  public int getLayoutId() {
    return R.layout.fragment_setting_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }

  @Override
  public void initViews(Bundle savedInstanceState) {
    localData=new LocalData(this);
    if(AppGlobalVars.USER_ID.equals(AppGlobalVars.DEFAULT_ID)){
      btnLogout.setVisibility(View.GONE);
    }else{
      btnLogout.setVisibility(View.VISIBLE);
    }
    mToolbar.setTitle("");
    mToolbar.setNavigationIcon(R.drawable.action_button_back_pressed_light);
    mToolbar.setNavigationOnClickListener(v -> {
           this.finish();
    });
    mVersionCode.setText("v" + getVersionCode());
  }

  @Override
  public void initToolBar() {

  }

  @OnClick(R.id.layout_about_me)
  void startAboutMeActivity() {
    //关于我
    startActivity(new Intent(this, AboutUsActivity.class));
  }


  @OnClick(R.id.layout_about_app)
  void startAboutBiliBiliActivity() {
    //本地信息
    startActivity(new Intent(this, AppIntroduceActivity.class));
  }
  @OnClick(R.id.layout_update)
  void startCheckUpdate() {
//检查更新
   _checkUpd();
  }


  @OnClick(R.id.btn_logout)
  void logout() {
    //退出登录
    getOut();
    localData
            .removeKV(AppGlobalConsts.PERSIST_NAMES.CURRENT_USER
                    .name());
    AppGlobalVars.USER_ID =  AppGlobalVars.DEFAULT_ID;
    AppGlobalVars.USER_TOKEN =  AppGlobalVars.DEFAULT_TOKEN;
    AppGlobalVars.USER_PIC = "";
    AppGlobalVars.USER_NICK_NAME ="";
    ToastUtil.ShortToast(getString(R.string.settingactivity_exitsucess));
    finish();


  }

  private void getOut(){
    RetrofitHelper.getUserAPIBangtv().getGoOut()
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              ToastUtil.ShortToast(getString(R.string.settingactivity_exitsucess));

            }, throwable -> {
              ToastUtil.ShortToast(getString(R.string.error_text2));
            });
  }

  public String getVersionCode() {

    PackageInfo packageInfo = null;
    try {
      packageInfo = this.getPackageManager()
          .getPackageInfo(this.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    assert packageInfo != null;
    return packageInfo.versionName;
  }

  private void _checkUpd(){
    pd = ProgressDialog.show(SettingActivity.this, "", getString(R.string.load_more));

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
              if(pd.isShowing()){
                pd.dismiss();
              }
              checkUpdateInfos.addAll(resultBeans.getData());
              String packageLocation = null;
                    for (int i = 0; i < checkUpdateInfos.size(); i++) {
                        versionId = checkUpdateInfos.get(i).getVersionId();
                        UpdateVersionName = checkUpdateInfos.get(i).getVersionName();
                        packageLocation = checkUpdateInfos.get(i).getPackageLocation();
                        UpdateMd5 = checkUpdateInfos.get(i).getMd5();
                        // upgradePattern = object.getString("upgradePattern");
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
                CheckUpdate(this,getString(R.string.app_name));
              }
            }, throwable -> {
              Log.i("",throwable.toString());
              CheckUpdate(this,getString(R.string.app_name));
              if(pd.isShowing()){
                pd.dismiss();
              }
            });
  }
  public void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
    mDialog = new AlertDialog.Builder(context);
    mDialog.setTitle(appName+getString(R.string.splashactivity_text6));
    mDialog.setMessage(updateinfo);
    mDialog.setNegativeButton(getString(R.string.splashactivity_text7), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        mDialog.setCancelable(true);
        if(ForceUpdate.equals("1")){
          System.exit(0);
        }
      }
    });
    mDialog.setPositiveButton(getString(R.string.splashactivity_text8), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        DownLoadApk.download(SettingActivity.this,downUrl,getString(R.string.app_name),appName);
        if(ForceUpdate.equals("1")){
          System.exit(0);
        }
      }
    }).setCancelable(true).create().show();
  }
  /*
  * 无更新版本
  * */
  public void CheckUpdate(final Context context, final String appName) {
    mDialog = new AlertDialog.Builder(context);
    mDialog.setTitle(appName+getString(R.string.settingactivity_text1));
    mDialog.setMessage(R.string.settingactivity_text2);
    mDialog.setPositiveButton(R.string.splashactivity_text5, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mDialog.setCancelable(true);
      }
    }).setCancelable(true).create().show();
  }
}
