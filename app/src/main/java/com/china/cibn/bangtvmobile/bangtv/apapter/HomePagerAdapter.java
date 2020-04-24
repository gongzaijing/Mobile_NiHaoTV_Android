package com.china.cibn.bangtvmobile.bangtv.apapter;

import com.china.cibn.bangtvmobile.bangtv.entity.MenuListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.classify.HomePageFragmentBangTv;
import com.china.cibn.bangtvmobile.bangtv.module.home.live.HomeLiveFragmentBangTv;
import com.china.cibn.bangtvmobile.bangtv.module.home.recommend.HomeRecommendedFragmentBangTv;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by gzj on 18/3/20 14:12
 *
 * <p/>
 * 主界面Fragment模块Adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

//  private final String[] TITLES;

  private Fragment[] fragments;

  private List<MenuListInfo.DataBean> dataList;
  public HomePagerAdapter(FragmentManager fm, Context context,List<MenuListInfo.DataBean> dataBeanList) {

    super(fm);
    this.dataList  = dataBeanList;
    fragments = new Fragment[dataList.size()];
  }


  @Override
  public Fragment getItem(int position) {
    if(dataList.get(position).getName().equals("Type")|dataList.get(position).getName().equals("分类")){
      fragments[position] = HomePageFragmentBangTv.newInstance(dataList.get(position).getId());
    }else if(dataList.get(position).getName().equals("Hot")|dataList.get(position).getName().equals("推荐")) {
      fragments[position] = HomeRecommendedFragmentBangTv.newInstance(dataList.get(position).getId());
    }else if(dataList.get(position).getName().equals("Live")|dataList.get(position).getName().equals("直播")) {
      fragments[position] = HomeLiveFragmentBangTv.newInstance(dataList.get(position).getId());
    }
//    if (fragments[position] == null) {
//      switch (position) {
//        case 0:
//          if(dataList.get(0).getName().equals("Type")){
//            fragments[position] = HomePageFragmentBangTv.newInstance(dataList.get(0).getId());
//          }else if(dataList.get(0).getName().equals("Hot")) {
//            fragments[position] = HomeRecommendedFragmentBangTv.newInstance(dataList.get(0).getId());
//          }else if(dataList.get(0).getName().equals("Live")){
//            fragments[position] = HomeLiveFragmentBangTv.newInstance(dataList.get(0).getId());
//          }
//          break;
//        case 1:
//          if(dataList.get(1).equals("Type")){
//            fragments[position] = HomePageFragmentBangTv.newInstance(dataList.get(1).getId());
//          }else if(dataList.get(1).equals("Hot")) {
//            fragments[position] = HomeRecommendedFragmentBangTv.newInstance(dataList.get(1).getId());
//          }else if(dataList.get(1).equals("Live")){
//            fragments[position] = HomeLiveFragmentBangTv.newInstance(dataList.get(1).getId());
//          }
//          break;
//        case 2:
//          if(dataList.get(2).equals("Type")){
//            fragments[position] = HomePageFragmentBangTv.newInstance(dataList.get(2).getId());
//          }else if(dataList.get(2).equals("Hot")) {
//            fragments[position] = HomeRecommendedFragmentBangTv.newInstance(dataList.get(2).getId());
//          }else if(dataList.get(2).equals("Live")){
//            fragments[position] = HomeLiveFragmentBangTv.newInstance(dataList.get(2).getId());
//          }
//          break;
////        case 3:
////          fragments[position] = HomeRegionFragment.newInstance();
//
////          break;
////        case 4:
////          fragments[position] = HomeSetFragmentBangTv.newInstance();
////          break;
////        case 5:
////          fragments[position] = HomeDiscoverFragment.newInstance();
////          break;
//        default:
//          break;
//      }
//    }
    return fragments[position];
  }


  @Override
  public int getCount() {

    return dataList.size();
  }


  @Override
  public CharSequence getPageTitle(int position) {

    return dataList.get(position).getName();
  }
}
