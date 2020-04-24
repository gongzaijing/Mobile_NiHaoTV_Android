package com.china.cibn.bangtvmobile.bangtv.module.home.setting;

import butterknife.BindView;
import butterknife.OnClick;

import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.ShareUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.Utils;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.R;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hcc on 16/8/7 14:12
 *
 * <p/>
 * App介绍界面
 */
public class AppIntroduceActivity extends RxBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.tv_version)
  TextView mVersion;

  @BindView(R.id.tv_network_diagnosis)
  TextView mTvNetworkDiagnosis;

  @BindView(R.id.tv_share_app)
  TextView mTvshareapp;

  @BindView(R.id.tv_feedback)
  TextView mFeeback;

  @BindView(R.id.address01)
  TextView maddress;

  @BindView(R.id.softversion02)
  TextView softversion02;

  @BindView(R.id.mac01)
  TextView macaddress;




  @Override
  public int getLayoutId() {

    return R.layout.activity_app_introduce;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @SuppressLint("SetTextI18n")
  @Override
  public void initViews(Bundle savedInstanceState) {
    mVersion.setText("v" + getVersion());
    setAddress();
    setsoftversion();
    setmac();
    setviewstatus();
  }


  @Override
  public void initToolBar() {

    mToolbar.setTitle(R.string.about);
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }


  private String getVersion() {

    try {
      PackageInfo pi = getPackageManager()
          .getPackageInfo(getPackageName(), 0);
      return pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return getString(R.string.about_version);
    }
  }

  //分享
  @OnClick(R.id.tv_share_app)
  void shareApp() {

    ShareUtil.shareLink(getString(R.string.github_url),
        getString(R.string.share_title), AppIntroduceActivity.this);
  }

  //意见反馈
  @OnClick(R.id.tv_feedback)
  void showFeedbackDialog() {

    new AlertDialog.Builder(AppIntroduceActivity.this)
        .setTitle(R.string.feedback_titlle)
        .setMessage(R.string.feedback_message)
        .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
        .show();
  }

  public void setviewstatus() {
    mTvNetworkDiagnosis.setVisibility(View.GONE);
    mTvshareapp.setVisibility(View.GONE);
    mFeeback.setVisibility(View.GONE);
    mVersion.setVisibility(View.GONE
    );
  }

  public void setAddress() {
    maddress.setText(this.getString(R.string.setting_text1)+ AppGlobalVars.IP_ADRESS);
  }

  public void setsoftversion() {
    softversion02.setText(this.getString(R.string.setting_text2) + getVersionName());
    //softversion02.setText(this.getString(R.string.setting_text2) + android.os.Build.VERSION.RELEASE);
  }

  public void setmac() {
    AppGlobalVars.LAN_MAC= Utils.getMac();
    macaddress.setText(this.getString(R.string.setting_text3)+ AppGlobalVars.LAN_MAC);
  }

  private String getVersionName() {

    try {
      // 获取packagemanager的实例
      PackageManager packageManager = this.getPackageManager();
      // getPackageName()是你当前类的包名，0代表是获取版本信息
      PackageInfo packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
      String version = packInfo.versionName;
      return version;
    } catch (Exception e) {
      return "";
    }

  }

}
