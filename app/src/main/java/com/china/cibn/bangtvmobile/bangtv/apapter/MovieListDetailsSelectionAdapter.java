package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.helper.AbsRecyclerViewAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;

import java.util.List;

/**
 * Created by gzj on 2018/3/29 17:12
 *
 * <p>
 * 选集adapter
 */

public class MovieListDetailsSelectionAdapter extends AbsRecyclerViewAdapter {

  private int layoutPosition = 0;

  private List<MovieDetailsInfo.SourcesBean> episodes;


  private MovieDetailsInfo MovieDetailsBean;

  public MovieListDetailsSelectionAdapter(RecyclerView recyclerView, List<MovieDetailsInfo.SourcesBean> episodes,MovieDetailsInfo movieDetailsBean) {

    super(recyclerView);
    this.episodes = episodes;
    this.MovieDetailsBean=movieDetailsBean;
  }


  @Override
  public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    bindContext(parent.getContext());
    return new ItemViewHolder(
        LayoutInflater.from(getContext()).inflate(R.layout.item_bangumi_selection, parent, false));
  }


  @Override
  public void onBindViewHolder(ClickableViewHolder holder, int position) {

    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      MovieDetailsInfo.SourcesBean episodesBean = episodes.get(position);
      if(MovieDetailsBean.getType().equalsIgnoreCase( "ENTERTAINMENTCATG")) {
        if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)) {
          itemViewHolder.mIndex.setText( episodesBean.getVolumncount() + getContext().getString(R.string.MovieListDetailsSelectionAdapter_Season ));

        }else{
          itemViewHolder.mIndex.setText(getContext().getString(R.string.MovieListDetailsSelectionAdapter_At) + episodesBean.getVolumncount() + getContext().getString(R.string.MovieListDetailsSelectionAdapter_Season ));

        }
      }else{
        if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)) {
          itemViewHolder.mIndex.setText(episodesBean.getVolumncount() + getContext().getString(R.string.MovieListDetailsSelectionAdapter_Part ));

        }else{
          itemViewHolder.mIndex.setText(getContext().getString(R.string.MovieListDetailsSelectionAdapter_At) + episodesBean.getVolumncount() + getContext().getString(R.string.MovieListDetailsSelectionAdapter_Part ));

        }
      }
      if(MovieDetailsBean.getServerbuy()==0) {
        if (episodesBean.getIstry()== 1) {
          itemViewHolder.mIstry.setVisibility(View.GONE);
        } else {
          itemViewHolder.mIstry.setVisibility(View.VISIBLE);
        }
      }else{
        itemViewHolder.mIstry.setVisibility(View.GONE);
      }
      if (position == layoutPosition) {
        itemViewHolder.mCardView.setForeground(
            getContext().getResources().getDrawable(R.drawable.bg_selection));
        itemViewHolder.mIndex.setTextColor(
            getContext().getResources().getColor(R.color.colorPrimary));
      } else {
        itemViewHolder.mCardView.setForeground(
            getContext().getResources().getDrawable(R.drawable.bg_normal));
        itemViewHolder.mIndex.setTextColor(
            getContext().getResources().getColor(R.color.font_normal));
      }
    }
    super.onBindViewHolder(holder, position);
  }


  public void notifyItemForeground(int clickPosition) {

    layoutPosition = clickPosition;
    notifyDataSetChanged();
  }


  @Override
  public int getItemCount() {

    return episodes.size();
  }


  public class ItemViewHolder extends ClickableViewHolder {

    CardView mCardView;

    TextView mIndex;

    ImageView mIstry;



    public ItemViewHolder(View itemView) {

      super(itemView);
      mCardView = $(R.id.card_view);
      mIndex = $(R.id.tv_index);
      mIstry = $(R.id.iv_isvip);

    }
  }
}
