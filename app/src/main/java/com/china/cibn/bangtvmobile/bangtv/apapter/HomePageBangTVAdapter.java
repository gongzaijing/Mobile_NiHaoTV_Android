package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.RecommendBannerInfoBangTv;
import com.china.cibn.bangtvmobile.bangtv.module.home.moviedetails.MovieDetailsActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.movielist.MovieListActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.ranking.RankListActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.special.SpecialListActivity;
import com.china.cibn.bangtvmobile.bangtv.module.video.live.LivePlayerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gzj on 2018/4/2 17:06
 *
 * <p>
 * 首页分类推荐直播adapter
 */

public class HomePageBangTVAdapter extends RecyclerView.Adapter {

  private List<RecommendBannerInfoBangTv.ItemListBean> mBangumiDetailsRecommends;

  private final int BANNER_VIEW_TYPE = 0;//两列 3:2

  private final int CHANNEL_VIEW_TYPE = 1;//一列

  private final int GIRL_VIEW_TYPE = 2;//两列1:1
  private Context context;

  private int[] mList;

  private boolean _istrue;

  private String _isstyle;

  public HomePageBangTVAdapter(Context context) {
    this.context=context;
  }

  public void setLiveInfo(List<RecommendBannerInfoBangTv.ItemListBean> BangumiDetailsRecommends,int[] ints,boolean IsTrue,String isStyle) {

    this.mBangumiDetailsRecommends = BangumiDetailsRecommends;
    this.mList=ints;
    this._istrue = IsTrue;
    this._isstyle = isStyle;
  }

  public int getSpanSize(int pos) {

    int viewType = getItemViewType(pos);
    switch (viewType) {
      case BANNER_VIEW_TYPE:
        return 3;
      case CHANNEL_VIEW_TYPE:
        return 6;
      case  GIRL_VIEW_TYPE:
        return 3;
    }
    return 0;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    switch (viewType) {
      case BANNER_VIEW_TYPE: //大布局
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bangtv_recommend, null);
        return new BangTVRecommendViewHolder(view);

      case CHANNEL_VIEW_TYPE: //布局3:2
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bangtv_recommend, null);
        return new BangTVClassifyViewHolder(view);

      case GIRL_VIEW_TYPE: //布局2:2
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bangtv_live, null);
        return new BangTVLiveViewHolder(view);
    }
    return null;
  }


  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    RecommendBannerInfoBangTv.ItemListBean resultBean = mBangumiDetailsRecommends.get(position);

    if (holder instanceof BangTVRecommendViewHolder) {
      BangTVRecommendViewHolder itemViewHolder = (BangTVRecommendViewHolder) holder;
  if(TextUtils.isEmpty(resultBean.getM_img())){
    Glide.with(context)
            .load(resultBean.getImage())
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.bangtv_default_bj)
            .dontAnimate()
            .into(itemViewHolder.mImageRecommend);

}else {
    Glide.with(context)
            .load(resultBean.getM_img())
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.bangtv_default_bj)
            .dontAnimate()
            .into(itemViewHolder.mImageRecommend);
}
      if(_istrue==true){
           itemViewHolder.mTitleRecommend.setText(resultBean.getName());
        }else{
       itemViewHolder.mTitleRecommend.setVisibility(View.GONE);
        }
        itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if(resultBean.getAction().equals("OPEN_PROGRAM_LIST")){
              MovieListActivity.
                      launch((Activity) context, resultBean.getId(),resultBean.getName());
            }else if (resultBean.getAction().equals("OPEN_DETAIL")){
              MovieDetailsActivity.launch(
                      (Activity) context, resultBean.getId());
            }else if (resultBean.getAction().equals("OPEN_TOP_PROGRAM")){
              RankListActivity.launch((Activity) context);
            }else if(resultBean.getAction().equals("OPEN_SPECIAL_INDEX")){
              SpecialListActivity.launch((Activity) context,resultBean.getId());
            }else if(resultBean.getAction().equals("OPEN_LIVEPLAYER")){
              LivePlayerActivity.launch( (Activity) context,resultBean.getActionParam().getChannelId()+"",resultBean.getActionParam().getChannelName(),"1",resultBean.getActionParam().getChannelNo()+"");
            }
          }
        });

    }else if (holder instanceof BangTVClassifyViewHolder) {

      BangTVClassifyViewHolder itemViewHolder1 = (BangTVClassifyViewHolder) holder;
      if(TextUtils.isEmpty(resultBean.getM_img())){
        Glide.with(context)
                .load(resultBean.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bangtv_default_bj)
                .dontAnimate()
                .into(itemViewHolder1.mImageClassify);
      }else {
        Glide.with(context)
                .load(resultBean.getM_img())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bangtv_default_bj)
                .dontAnimate()
                .into(itemViewHolder1.mImageClassify);
      }
      if(_istrue==true){
        itemViewHolder1.mTitleClassify.setText(resultBean.getName());
      }else{
        itemViewHolder1.mTitleClassify.setVisibility(View.GONE);
      }
      itemViewHolder1.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if(resultBean.getAction().equals("OPEN_PROGRAM_LIST")){
            MovieListActivity.
                    launch((Activity) context, resultBean.getId(),resultBean.getName());
          }else if (resultBean.getAction().equals("OPEN_DETAIL")){
            MovieDetailsActivity.launch(
                    (Activity) context, resultBean.getId());
          }else if (resultBean.getAction().equals("OPEN_TOP_PROGRAM")){
            RankListActivity.launch((Activity) context);
          }else if(resultBean.getAction().equals("OPEN_LIVEPLAYER")){
            LivePlayerActivity.launch( (Activity) context,resultBean.getActionParam().getChannelId()+"",resultBean.getActionParam().getChannelName(),"1",resultBean.getActionParam().getChannelNo()+"");
          }
//          BangTVMainActivity.instance.finish();
        }
      });

    }else if (holder instanceof BangTVLiveViewHolder) {

      BangTVLiveViewHolder itemViewHolder2 = (BangTVLiveViewHolder) holder;

      Glide.with(context)
              .load(resultBean.getImage())
              .centerCrop()
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .placeholder(R.drawable.bangtv_default_bj)
              .dontAnimate()
              .into(itemViewHolder2.mImageLive);
      itemViewHolder2.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(resultBean.getAction().equals("OPEN_PROGRAM_LIST")){
            MovieListActivity.
                    launch((Activity) context, resultBean.getId(),resultBean.getName());
          }else if (resultBean.getAction().equals("OPEN_DETAIL")){
            MovieDetailsActivity.launch(
                    (Activity) context, resultBean.getId());
          }else if (resultBean.getAction().equals("OPEN_TOP_PROGRAM")){
            RankListActivity.launch((Activity) context);
          }else if(resultBean.getAction().equals("OPEN_LIVEPLAYER")){
            LivePlayerActivity.launch( (Activity) context,resultBean.getActionParam().getChannelId()+"",resultBean.getActionParam().getChannelName(),"1",resultBean.getActionParam().getChannelNo()+"");
          }
        }
      });

    }

    }

  @Override
  public int getItemViewType(int position) {
 if (mList[position]==2) {
   if(_isstyle.equals("live")){
     return GIRL_VIEW_TYPE;
   }else {
     return BANNER_VIEW_TYPE;
   }
   } else {
      return CHANNEL_VIEW_TYPE;
    }
  }

  @Override
  public int getItemCount() {
  if(mBangumiDetailsRecommends!=null){
  return mBangumiDetailsRecommends.size();

  }else{
  return 0;
  }
  }

  static class BangTVRecommendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.item_img)
    ImageView mImageRecommend;

    @BindView(R.id.item_title)
    TextView mTitleRecommend;

    BangTVRecommendViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }


  static class BangTVClassifyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.item_img)
    ImageView mImageClassify;

    @BindView(R.id.item_title)
    TextView mTitleClassify;


    BangTVClassifyViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
  static class BangTVLiveViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.item_img)
    ImageView mImageLive;

    BangTVLiveViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
