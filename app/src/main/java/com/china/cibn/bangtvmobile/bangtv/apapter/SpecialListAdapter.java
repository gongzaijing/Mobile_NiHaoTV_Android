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
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.special.SpecialTemplateActivity;

import java.util.List;

/**
 * Created by hcc on 16/8/4 14:12
 *
 * <p/>
 * 全区排行榜adapter
 */
public class SpecialListAdapter extends AbsRecyclerViewAdapter {

  private List<SpecialListInfo.SpecialListBean> lists;

private Context context;
  public SpecialListAdapter(Context context, RecyclerView recyclerView, List<SpecialListInfo.SpecialListBean> specialListBeanList) {

    super(recyclerView);
    this.context = context;
    this.lists = specialListBeanList;
  }


  @Override
  public AbsRecyclerViewAdapter.ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    bindContext(parent.getContext());
    return new ItemViewHolder(LayoutInflater.from(getContext())
        .inflate(R.layout.item_special_list_bangtv, parent, false));
  }


  @Override
  public void onBindViewHolder(ClickableViewHolder holder, int position) {

    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      SpecialListInfo.SpecialListBean listBean = lists.get(position);
      itemViewHolder.mVideoTitle.setText(listBean.getName());

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
          SpecialTemplateActivity.launch(
                  (Activity) context, listBean.getId(),listBean.getName());
        }
      });
    }
    super.onBindViewHolder(holder, position);
  }




  @Override
  public int getItemCount() {

    return lists.size();
  }


  public class ItemViewHolder extends ClickableViewHolder {

    ImageView mVideoImg;

    TextView mVideoTitle;

    CardView cardView;


    public ItemViewHolder(View itemView) {

      super(itemView);
      mVideoImg = $(R.id.item_img);
      mVideoTitle = $(R.id.item_title);
      cardView = $(R.id.card_view);
    }
  }
}
