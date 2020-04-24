package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.china.cibn.bangtvmobile.bangtv.entity.recommend.ProgramMenusInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.movielist.MovieListDetailsFragment;
import com.china.cibn.bangtvmobile.bangtv.module.home.movielist.MovieListFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by hcc on 16/8/4 14:12
 *
 * <p/>
 * 分区界面PagerAdapter
 */
public class MovieListPagerAdapter extends FragmentStatePagerAdapter {

  private String rid;

  private List<String> titles;

  private List<ProgramMenusInfo.MenuListBean> childrens;

  private List<Fragment> fragments = new ArrayList<>();

  private boolean Istrue;

  private String mParentCatgId;

  private int mPageNumber;

  private String mPageSize;

  private String mCateName;

  private String mYear;

  private  String mArea;

  private String mType;

  private  String mClassType;
  public MovieListPagerAdapter(FragmentManager fm, String rid, List<String> titles,
                               List<ProgramMenusInfo.MenuListBean> childrens,boolean isTrue,String mParentCatgId, int mPageNumber,
                               String mPageSize, String mCateName,
                               String _mYear, String _mArea,
                               String _mType, String _mClassType) {

    super(fm);
    this.rid = rid;
    this.titles = titles;
    this.childrens = childrens;
    this.Istrue = isTrue;
    this.mParentCatgId=mParentCatgId;
    this.mPageNumber = mPageNumber;
    this.mPageSize=mPageSize;
    this.mCateName=mCateName;
    this.mYear =_mYear;
    this.mArea = _mArea;
    this.mType = _mType;
    this.mClassType=_mClassType;
    initFragments();
  }


  private void initFragments() {

    fragments.add(MovieListFragment.newInstance(rid,Istrue,mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType));

    Observable.from(childrens)
        .subscribe(childrenBean -> {
          fragments.add(MovieListDetailsFragment.
              newInstance(childrenBean.getId()));
        });
  }


  @Override
  public Fragment getItem(int position) {

    return fragments.get(position);
  }


  @Override
  public int getCount() {

    return fragments.size();
  }


  @Override
  public CharSequence getPageTitle(int position) {

    return titles.get(position);
  }
}
