package com.china.cibn.bangtvmobile.bangtv.apapter.stction;

import android.app.Activity;
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
import com.china.cibn.bangtvmobile.bangtv.module.home.moviedetails.MovieDetailsActivity;
import com.china.cibn.bangtvmobile.bangtv.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 2016/10/21 23:28
 *
 * <p>
 * 分区推荐最新视频section
 */

public class MovieListSection extends StatelessSection {

  private Context mContext;

  private int rid;

  private List<MovieListInfo.ProgramListBean> news;


  public MovieListSection(Context context, List<MovieListInfo.ProgramListBean> news) {

    super(R.layout.item_bangtv_movielist);
    this.news = news;
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
    MovieListInfo.ProgramListBean newBean = news.get(position);

    Glide.with(mContext)
        .load(newBean.getImage())
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.bangtv_default_bj)
        .dontAnimate()
        .into(itemViewHolder.mImage);
    itemViewHolder.mTitle.setText(newBean.getName());
     itemViewHolder.mCardView.setOnClickListener(v -> MovieDetailsActivity.launch(
    (Activity) mContext, newBean.getId()));
  }


  @Override
  public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
    return null;

//    return new HeadViewHolder(view);
  }


  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

  }



  static class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    CardView mCardView;

    @BindView(R.id.item_img)
    ImageView mImage;

    @BindView(R.id.item_title)
    TextView mTitle;



    public ItemViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
