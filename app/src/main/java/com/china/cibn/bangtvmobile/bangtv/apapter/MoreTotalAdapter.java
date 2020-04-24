package com.china.cibn.bangtvmobile.bangtv.apapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.helper.AbsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzj on 2018/3/30 20:34
 *
 * <p>
 * 多集选择  上方选集adapter
 */

public class MoreTotalAdapter extends AbsRecyclerViewAdapter {

  private int layoutPosition = 0;

  private ArrayList<String> array;


  public MoreTotalAdapter(RecyclerView recyclerView, ArrayList<String> array) {

    super(recyclerView);
    this.array = array;
  }


  @Override
  public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    bindContext(parent.getContext());
    return new ItemViewHolder(LayoutInflater.from(getContext())
        .inflate(R.layout.item_bangumi_details_seasons, parent, false));
  }


  @Override
  public void onBindViewHolder(ClickableViewHolder holder, int position) {

    if (holder instanceof ItemViewHolder) {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

      itemViewHolder.mSeasons.setText(array.get(position));

      if (position == layoutPosition) {
        itemViewHolder.mCardView.setForeground(
            getContext().getResources().getDrawable(R.drawable.bg_selection));
        itemViewHolder.mSeasons.setTextColor(
            getContext().getResources().getColor(R.color.colorPrimary));
      } else {
        itemViewHolder.mCardView.setForeground(
            getContext().getResources().getDrawable(R.drawable.bg_normal));
        itemViewHolder.mSeasons.setTextColor(
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

    return array.size();
  }


  private class ItemViewHolder extends ClickableViewHolder {

    CardView mCardView;

    TextView mSeasons;


    public ItemViewHolder(View itemView) {

      super(itemView);
      mCardView = $(R.id.card_view);
      mSeasons = $(R.id.tv_seasons);
    }
  }
}
