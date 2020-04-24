package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.helper.AbsRecyclerViewAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.RankingListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.moviedetails.MovieDetailsActivity;

import java.util.List;

/**
 * Created by hcc on 16/8/4 14:12
 *
 * <p/>
 * 全区排行榜adapter
 */
public class AllRankAdapter extends AbsRecyclerViewAdapter {

  private List<RankingListInfo.DataBean.ProgramListBean> allRanks;

private Context context;
  public AllRankAdapter(Context context,RecyclerView recyclerView, List<RankingListInfo.DataBean.ProgramListBean> allRanks) {

    super(recyclerView);
    this.context = context;
    this.allRanks = allRanks;
  }


  @Override
  public AbsRecyclerViewAdapter.ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    bindContext(parent.getContext());
    return new ItemViewHolder(LayoutInflater.from(getContext())
        .inflate(R.layout.item_rank_list_bangtv, parent, false));
  }


  @Override
  public void onBindViewHolder(ClickableViewHolder holder, int position) {

    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      RankingListInfo.DataBean.ProgramListBean listBean = allRanks.get(position);
      itemViewHolder.mVideoTitle.setText(listBean.getName());
      itemViewHolder.mSortNum.setText(String.valueOf(position + 1));
      setSortNumTextSize(itemViewHolder, position);

      Glide.with(getContext())
          .load(listBean.getImage())
          .centerCrop()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .placeholder(R.drawable.bangtv_default_bj)
          .dontAnimate()
          .into(itemViewHolder.mVideoImg);
      itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          MovieDetailsActivity.launch(
                  (Activity) context, listBean.getId());
        }
      });
    }
    super.onBindViewHolder(holder, position);
  }


  private void setSortNumTextSize(ItemViewHolder itemViewHolder, int position) {
//
    if (position <= 2) {
      itemViewHolder.mSortNum.setTextSize(20);
      itemViewHolder.mSortNum.setBackgroundResource(R.drawable.ranking_red);

    } else if (position==3){
      itemViewHolder.mSortNum.setTextSize(20);
      itemViewHolder.mSortNum.setBackgroundResource(R.drawable.ranking_blue);

    }else if (position==4) {
      itemViewHolder.mSortNum.setTextSize(20);
      itemViewHolder.mSortNum.setBackgroundResource(R.drawable.ranking_blue);
    }
  }


  @Override
  public int getItemCount() {

    return allRanks.size();
  }


  public class ItemViewHolder extends ClickableViewHolder {

    ImageView mVideoImg;

    TextView mVideoTitle;

    TextView mSortNum;

    CardView cardView;


    public ItemViewHolder(View itemView) {

      super(itemView);
      mVideoImg = $(R.id.item_img);
      mVideoTitle = $(R.id.item_title);
      mSortNum = $(R.id.item_sort_num);
      cardView = $(R.id.card_view);
    }
  }
}
