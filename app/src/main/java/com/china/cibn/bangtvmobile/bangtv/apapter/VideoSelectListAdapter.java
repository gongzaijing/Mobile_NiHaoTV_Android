package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.helper.AbsRecyclerViewAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.module.video.play.VideoPlayerActivity;

/**
 * Created by gzj on 2018-5-24 09:51:41
 *
 * <p/>
 *
 */
public class VideoSelectListAdapter extends AbsRecyclerViewAdapter {

  private MovieDetailsInfo movieDetailsInfo;

private Context context;

private int layoutPosition;

  public VideoSelectListAdapter(Context context, RecyclerView recyclerView,MovieDetailsInfo _movieDetailsInfo) {

    super(recyclerView);
    this.context = context;
    this.movieDetailsInfo =_movieDetailsInfo;
  }


  @Override
  public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    bindContext(parent.getContext());
    return new ItemViewHolder(LayoutInflater.from(getContext())
        .inflate(R.layout.item_video_select_bangtv, parent, false));
  }


  @Override
  public void onBindViewHolder(ClickableViewHolder holder, int position) {

    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      itemViewHolder.mVideoTitle.setText(movieDetailsInfo.getSources().get(position).getVolumncount());
      itemViewHolder.cardView.setForeground(
              getContext().getResources().getDrawable(R.drawable.video_normal_focus));
      if (position+1 == layoutPosition) {
        itemViewHolder.cardView.setForeground(
                getContext().getResources().getDrawable(R.drawable.video_red_focus));
        itemViewHolder.mVideoTitle.setTextColor(
                getContext().getResources().getColor(R.color.pink_text_color));
      } else {
        itemViewHolder.cardView.setForeground(
                getContext().getResources().getDrawable(R.drawable.video_normal_focus));
        itemViewHolder.mVideoTitle.setTextColor(
                getContext().getResources().getColor(R.color.white));
      }
      if(movieDetailsInfo.getServerbuy()!=1){
        if(movieDetailsInfo.getSources().get(position).getIstry()==1){
          itemViewHolder.mIvVip.setVisibility(View.INVISIBLE);
        }else{
          itemViewHolder.mIvVip.setVisibility(View.VISIBLE);
        }
      }
      itemViewHolder.mRelative.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(movieDetailsInfo.getSources().get(position).getIstry()==1|movieDetailsInfo.getServerbuy()==1){
            VideoPlayerActivity.instance.refresh(movieDetailsInfo.getSources().get(position).getId(), movieDetailsInfo.getId(), position + 1 + "", movieDetailsInfo.getName());
          }else {
            VideoPlayerActivity.instance.getDialog();
          }
//     movieDetailsInfo.getSources().get(position).getId();
//          VideoPlayerActivity.launch((Activity) context,
//                  movieDetailsInfo.getSources().get(position).getId(),movieDetailsInfo.getName(),movieDetailsInfo.getId(),position+1+"");
        }
      });
    }
    super.onBindViewHolder(holder, position);
  }




  @Override
  public int getItemCount() {

    return movieDetailsInfo.getSources().size();
  }


  public void notifyItemForeground(int clickPosition) {

    layoutPosition = clickPosition;
  }

  public class ItemViewHolder extends ClickableViewHolder {


    TextView mVideoTitle;

    CardView cardView;

    RelativeLayout mRelative;

    ImageView mIvVip;


    public ItemViewHolder(View itemView) {

      super(itemView);
      mVideoTitle = $(R.id.item_title);
      cardView = $(R.id.card_view);
      mRelative = $(R.id.root_layout_select);
      mIvVip = $(R.id.iv_isvip);
    }
  }
}
