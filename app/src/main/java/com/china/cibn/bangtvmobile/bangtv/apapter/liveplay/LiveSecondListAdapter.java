package com.china.cibn.bangtvmobile.bangtv.apapter.liveplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.LiveListInfo;

import java.util.List;


public class LiveSecondListAdapter extends BaseAdapter {
	private int iSelector = 0;
	private int iPreSelector = 0;
	private Context mcontext;
	private List<LiveListInfo> msourcelst;

	public LiveSecondListAdapter(Context context) {
		mcontext = context;
	}

	public void setData(List<LiveListInfo> lst) {
		msourcelst = lst;
	}

	public void setSelector(int i) {
		iSelector = i;

	}

	public int getSelector() {
		return iSelector;
	}

	public int getPreSelector() {
		return iPreSelector;
	}

	@Override
	public int getCount() {
		return msourcelst.size();
	}

	@Override
	public Object getItem(int arg0) {
		return msourcelst.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		View view = null;
		ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			view = View.inflate(mcontext, R.layout.live_seriesline_item_bangtv, null);
			holder.rootlayout = (LinearLayout) view
					.findViewById(R.id.rootLayout);
			holder.textView = (TextView) view.findViewById(R.id.textView);
			// 初始化布局
			holder.textNum = (TextView) view.findViewById(R.id.textnum);
			holder.textChannel = (TextView) view
					.findViewById(R.id.textcurchanel);
			holder.textChannel.setGravity(Gravity.CENTER
					| Gravity.CENTER_VERTICAL);
			holder.rootlayout
					.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		String str = "";
		String substr = "";
		str = msourcelst.get(arg0).getChannelName()+"";
		substr = msourcelst.get(arg0).getIsVip()+"";
		if (!TextUtils.isEmpty(msourcelst.get(arg0).getNo())) {
			if (Integer.parseInt(msourcelst.get(arg0).getNo()) > 0) {

				holder.textNum.setText(String.valueOf(msourcelst.get(arg0)
						.getNo()));
			} else {
				if (arg0 < 9) {
					holder.textNum.setText("0" + String.valueOf(arg0 + 1));
				} else {
					holder.textNum.setText(String.valueOf(arg0 + 1));
				}
			}
		}else{
			holder.textNum.setText("");
		}

		if (iSelector != arg0) {
			((TextView)holder.textView.findViewById(R.id.textView)).setSelected(false);
			holder.textView.setAlpha(0.7f);
			holder.rootlayout.setBackgroundColor(Color.TRANSPARENT);
			holder.textView.setTextColor(mcontext.getResources().getColor(R.color.white));
			holder.textNum.setTextColor(mcontext.getResources().getColor(R.color.white));
			holder.textChannel.setTextColor(mcontext.getResources().getColor(R.color.white));
		} else {
			((TextView)holder.textView.findViewById(R.id.textView)).setSelected(true);
			holder.textView.setAlpha(1f);
			holder.textView.setTextColor(mcontext.getResources().getColor(R.color.red));
			holder.textNum.setTextColor(mcontext.getResources().getColor(R.color.red));
			holder.textChannel.setTextColor(mcontext.getResources().getColor(R.color.red));
			iPreSelector = iSelector;
		}
		holder.textView.setText(str);
		if (holder.textChannel != null) {
			if (substr.equals("1")) {
				holder.textChannel.setText(mcontext.getString(R.string.LiveSecondListAdapter_VipChannal));
				holder.textChannel.setVisibility(View.VISIBLE);
			} else {
				holder.textChannel.setText("");
				holder.textChannel.setVisibility(View.GONE);
			}
		}

		return view;
	}

	private class ViewHolder {
		TextView textNum;
		TextView textView;
		TextView textChannel;
		LinearLayout rootlayout;
	}

}
