package com.china.cibn.bangtvmobile.bangtv.module.common;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.VipPayInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.home.HomePageFragment;
import com.china.cibn.bangtvmobile.bangtv.module.home.history.HistoryFragment;
import com.china.cibn.bangtvmobile.bangtv.module.home.history.HistorylistActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.setting.SettingActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.maillogin.LoginActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.VipActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleImageView;

import java.util.Locale;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/30 14:12
 *
 * <p/>
 * MainActivity
 */
public class BangTVMainActivity extends RxBaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;

  @BindView(R.id.navigation_view)
  NavigationView mNavigationView;

  private Fragment[] fragments;

  private int currentTabIndex;

  private int index;

  private long exitTime;

  private HomePageFragment mHomePageFragment;

  private ImageView mUserAvatarView;

 private TextView mUserName;

 private   TextView mUserSign;

 public static BangTVMainActivity instance;
 private VipPayInfo.DataBean payInfoList = new VipPayInfo.DataBean();
  @Override
  public int getLayoutId() {

    return R.layout.activity_main_bangtv;
  }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }


    @Override
  public void initViews(Bundle savedInstanceState) {
      set("en");
      instance=this;
    //初始化Fragment
    initFragments();
    //初始化侧滑菜单
    initNavigationView();
  }
    //设置语言
    private void set(String lauType) {
        // 本地语言设置
        Locale myLocale = new Locale(lauType);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

  /**
   * 初始化Fragments
   */
  private void initFragments() {

    mHomePageFragment = HomePageFragment.newInstance();
    HistoryFragment mHistoryFragment = HistoryFragment.newInstance();
    fragments = new Fragment[] {
        mHomePageFragment,
        mHistoryFragment
    };

    // 添加显示第一个fragment
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.container, mHomePageFragment)
        .show(mHomePageFragment).commit();
  }


  /**
   * 初始化NavigationView
   */
  private void initNavigationView() {
    mNavigationView.setNavigationItemSelectedListener(this);
    /**设置MenuItem的字体颜色**/
    Resources resource=(Resources)getBaseContext().getResources();
    ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.navigation_menu_item_color);
    mNavigationView.setItemTextColor(csl);
    mNavigationView.setItemIconTintList(csl);
/**设置MenuItem默认选中项**/
    mNavigationView.getMenu().getItem(0).setChecked(true);
    View headerView = mNavigationView.getHeaderView(0);
     mUserAvatarView = (ImageView) headerView.findViewById(
        R.id.user_avatar_view);
     mUserName = (TextView) headerView.findViewById(R.id.user_name);
    mUserSign = (TextView) headerView.findViewById(R.id.user_other_info);
    ImageView mSwitchMode = (ImageView) headerView.findViewById(R.id.iv_head_switch_mode);
    //设置头像
    mUserAvatarView.setImageResource(R.drawable.ico_user_default);
    mUserAvatarView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)) {
              Toast.makeText(BangTVMainActivity.this,R.string.bangtvmainactivity_text2,Toast.LENGTH_LONG).show();
          }else{
              if(!AppGlobalVars.USER_ID.equals(AppGlobalVars.DEFAULT_ID)){
                  return;
              }else {
                  startActivity(new Intent(BangTVMainActivity.this, LoginActivity.class));
              }}
          }

    });
    //设置用户名 签名
      if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
          mUserName.setText(getString(R.string.bangtvmainactivity_text2));
      }else {
          mUserName.setText(getString(R.string.bangtvmainactivity_text1));
      }
    //设置日夜间模式切换
//    mSwitchMode.setOnClickListener(v -> switchNightMode());

//    boolean flag = PreferenceUtil.getBoolean(ConstantUtil.SWITCH_MODE_KEY, false);
//    if (flag) {
//      mSwitchMode.setImageResource(R.drawable.ic_switch_daily);
//    } else {
//      mSwitchMode.setImageResource(R.drawable.ic_switch_night);
//    }
  }


  /**
   * 日夜间模式切换
   */
//  private void switchNightMode() {
//
//    boolean isNight = PreferenceUtil.getBoolean(ConstantUtil.SWITCH_MODE_KEY, false);
//    if (isNight) {
//      // 日间模式
//      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//      PreferenceUtil.putBoolean(ConstantUtil.SWITCH_MODE_KEY, false);
//    } else {
//      // 夜间模式
//      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//      PreferenceUtil.putBoolean(ConstantUtil.SWITCH_MODE_KEY, true);
//    }
//
//    recreate();
//  }


  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    mDrawerLayout.closeDrawer(GravityCompat.START);
    switch (item.getItemId()) {
      case R.id.item_home:
        // 主页
        changeFragmentIndex(item, 0);
        return true;


      case R.id.item_vip:
          //如果是netrange不登录
          if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)) {
              Toast.makeText(this,R.string.bangtvmainactivity_text2,Toast.LENGTH_LONG).show();
          }else{
              //判断是否登录
              if(AppGlobalVars.USER_ID.equals(AppGlobalVars.DEFAULT_ID)){
                  startActivity(new Intent(BangTVMainActivity.this, LoginActivity.class));
              }else {
                  startActivity(new Intent(BangTVMainActivity.this, VipActivity.class));
              }
          }

        return true;

      case R.id.item_history:
        // 最近观看
          // 判断一下有没有记录在展示fix by gz 2018/04/18
          startActivity(new Intent(BangTVMainActivity.this, HistorylistActivity.class));
          return true;
              //changeFragmentIndex(item, 1);
              //return true;

      case R.id.item_settings:
        // 设置中心
          startActivity(new Intent(BangTVMainActivity.this, SettingActivity.class));
        return true;
    }

    return false;
  }


  /**
   * Fragment切换
   */
  private void switchFragment() {

    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
    trx.hide(fragments[currentTabIndex]);
    if (!fragments[index].isAdded()) {
      trx.add(R.id.container, fragments[index]);
    }
    trx.show(fragments[index]).commit();
    currentTabIndex = index;
  }


  /**
   * 切换Fragment的下标
   */
  private void changeFragmentIndex(MenuItem item, int currentIndex) {

    index = currentIndex;
    switchFragment();
    item.setChecked(true);
  }


  /**
   * DrawerLayout侧滑菜单开关
   */
  public void toggleDrawer() {

    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      mDrawerLayout.openDrawer(GravityCompat.START);
    }
  }


  /**
   * 监听back键处理DrawerLayout和SearchView
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {

    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (mDrawerLayout.isDrawerOpen(mDrawerLayout.getChildAt(1))) {
        mDrawerLayout.closeDrawers();
      } else {
        if (mHomePageFragment != null) {
          if (mHomePageFragment.isOpenSearchView()) {
            mHomePageFragment.closeSearchView();
          } else {
            exitApp();
          }
        } else {
          exitApp();
        }
      }
    }

    return true;
  }


  /**
   * 双击退出App
   */
  private void exitApp() {

    if (System.currentTimeMillis() - exitTime > 2000) {
      ToastUtil.ShortToast(getString(R.string.bangtvmainactivity_exit));
      exitTime = System.currentTimeMillis();
    } else {
//      PreferenceUtil.remove(ConstantUtil.SWITCH_MODE_KEY);
      finish();
    }
  }


  /**
   * 解决App重启后导致Fragment重叠的问题
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
//    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onResume() {
      set("en");
    super.onResume();
    if(!TextUtils.isEmpty(AppGlobalVars.USER_PIC)){
                Glide.with(getApplicationContext()).load(AppGlobalVars.USER_PIC).into(mUserAvatarView);
    }else {
      mUserAvatarView.setImageResource(R.drawable.ico_user_default);

    }
    if(!AppGlobalVars.USER_ID.equals(AppGlobalVars.DEFAULT_ID)) {
        mUserName.setText(AppGlobalVars.USER_NICK_NAME);
        loadData();
    }else{
        if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
            mUserName.setText(getString(R.string.bangtvmainactivity_text2));
        }else {
            mUserName.setText(getString(R.string.bangtvmainactivity_text1));
        }
        mUserSign.setText("");
        mUserSign.setVisibility(View.GONE);
    }
  }

    @Override
    public void loadData() {
        RetrofitHelper.getNoCacheAppAPI().getPayInfo(BangtvApp.versionCodeUrl)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    payInfoList=resultBeans.getData();
                    if(!TextUtils.isEmpty(payInfoList.getTime())){
                        mUserSign.setText(payInfoList.getTime()+payInfoList.getDay());
                    }
                    mUserSign.setVisibility(View.VISIBLE);

                }, throwable -> {
                    Log.i("","");
                });
    }

    @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == 0){
      mUserName.setText(BangtvApp.getShared().getString("mUserNickName",""));

      String headUrl = data.getStringExtra("mUserPic");
      Log.d("","url:"+headUrl);
      Glide.with(BangTVMainActivity.this).load(headUrl).into(mUserAvatarView);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  @Override
  public void initToolBar() {}
}
