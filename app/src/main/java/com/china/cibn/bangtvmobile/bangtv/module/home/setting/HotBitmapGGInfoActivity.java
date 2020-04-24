package com.china.cibn.bangtvmobile.bangtv.module.home.setting;

import butterknife.BindView;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by hcc on 16/8/7 14:12
 *
 * <p/>
 * 关于我
 */
public class HotBitmapGGInfoActivity extends RxBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;


  @Override
  public int getLayoutId() {

    return R.layout.activity_aboutus_bangtv;
  }

  @Override
  public String setPageName() {
    return null;
  }


  @Override
  public void initViews(Bundle savedInstanceState) {

  }


  @Override
  public void initToolBar() {

    mToolbar.setTitle("关于我");
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }
}
