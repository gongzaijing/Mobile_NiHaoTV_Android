package com.china.cibn.bangtvmobile.bangtv.apapter.stction;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieListInfo;
import com.china.cibn.bangtvmobile.bangtv.widget.sectioned.StatelessSection;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 2016/10/21 23:28
 *
 * <p>
 * 分区推荐最新视频section
 */

public class MovieListFilterSection extends StatelessSection {

  private Context mContext;

  private int rid;

  private List<MovieListInfo.ProgramListBean> news;


  public MovieListFilterSection(Context context, int rid, List<MovieListInfo.ProgramListBean> news) {

    super( R.layout.item_bangtv_movielist_filter);
    this.news = news;
    this.rid = rid;
    this.mContext = context;
  }


  @Override
  public int getContentItemsTotal() {

    return news.size();
  }


  @Override
  public RecyclerView.ViewHolder getItemViewHolder(View view) {

    return new ItemViewHolder(view);
  }


  @Override
  public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//    itemViewHolder.mCardView.setOnClickListener(
//        v -> VideoDetailsActivity.launch((Activity) mContext,
//            Integer.valueOf(newBean.()), newBean.getCover()));
  }


  @Override
  public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
    return null;

//    return new HeadViewHolder(view);
  }


  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

//    HeadViewHolder headViewHolder = (HeadViewHolder) holder;
//    if (rid == ConstantUtil.ADVERTISING_RID) {
//      headViewHolder.mMore.setVisibility(View.GONE);
//      headViewHolder.mTypeIcon.setImageResource(R.drawable.ic_header_movie_relate);
//      headViewHolder.mTypeTv.setText("最新投稿");
//    } else {
//      headViewHolder.mMore.setVisibility(View.VISIBLE);
//      headViewHolder.mTypeIcon.setImageResource(R.drawable.ic_header_new);
//      headViewHolder.mTypeTv.setText("最新视频");
//      headViewHolder.mMore.setOnClickListener(v -> RxBus.getInstance().post(0));
//    }
  }


  static class ItemViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.sliding_tabs)
//    SlidingTabLayout slidingTabLayout;
//    @BindView(R.id.sliding_tabs1)
//    SlidingTabLayout slidingTabLayout1;
//
//    @BindView(R.id.sliding_tabs2)
//    SlidingTabLayout slidingTabLayout2;




    public ItemViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
