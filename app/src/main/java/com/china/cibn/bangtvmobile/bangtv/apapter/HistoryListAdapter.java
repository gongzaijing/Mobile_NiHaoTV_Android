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
import com.china.cibn.bangtvmobile.bangtv.entity.HistoryListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.moviedetails.MovieDetailsActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.special.SpecialTemplateActivity;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;

import java.util.List;

/**
 * fix by gz  2018/4/18.
 * 历史页面的adapt
 */

public class HistoryListAdapter extends AbsRecyclerViewAdapter {


    private List<HistoryListInfo.HistoryListBean> lists;

    private Context context;
    public HistoryListAdapter(Context context, RecyclerView recyclerView, List<HistoryListInfo.HistoryListBean> hsitoryListBeanList) {

        super(recyclerView);
        this.context = context;
        this.lists = hsitoryListBeanList;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.item_history_list_bangtv, parent, false));
    }


    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof HistoryListAdapter.ItemViewHolder) {
            HistoryListAdapter.ItemViewHolder itemViewHolder = (HistoryListAdapter.ItemViewHolder) holder;
            HistoryListInfo.HistoryListBean listBean = lists.get(position);
            itemViewHolder.mVideoTitle.setText(listBean.getName());

            if(listBean.gettype().equals("MOVIECATG")) {
                itemViewHolder.mTimePosition.setText(listBean.getTimePosition());
            } else {
                if(listBean.getplayerpos().length()>5){
                    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
                        itemViewHolder.mTimePosition.setText(listBean.getplayerpos() + getContext().getString(R.string.HistoryListAdapter_Season));

                    }else{
                        itemViewHolder.mTimePosition.setText(getContext().getString(R.string.HistoryListAdapter_At) + listBean.getplayerpos() + getContext().getString(R.string.HistoryListAdapter_Season));


                    }

                }else{
                    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
                        itemViewHolder.mTimePosition.setText(listBean.getplayerpos() + getContext().getString(R.string.HistoryListAdapter_Part));

                    }else{
                        itemViewHolder.mTimePosition.setText(getContext().getString(R.string.HistoryListAdapter_At) + listBean.getplayerpos() + getContext().getString(R.string.HistoryListAdapter_Part));


                    }

                }

            }

            Glide.with(getContext())
                    .load(listBean.getImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bangtv_default_bj)
                    .dontAnimate()
                    .into(itemViewHolder.mVideoImg);
            itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {//fix by gz 2018/04/18click sub itme
                @Override
                public void onClick(View view) {
                    MovieDetailsActivity.launch(
                            (Activity) context, listBean.getId());
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

        TextView mTimePosition;//fix by gz 2018/04/18 add time position

        CardView cardView;


        public ItemViewHolder(View itemView) {

            super(itemView);
            mVideoImg = $(R.id.item_img);
            mVideoTitle = $(R.id.item_title);
            mTimePosition = $(R.id.item_timeposition);//fix by gz 2018/04/18 add time position
            cardView = $(R.id.card_view);
        }
    }


}
