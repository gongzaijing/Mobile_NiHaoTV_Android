package com.china.cibn.bangtvmobile.bangtv.apapter.liveplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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


public class LiveFirstListAdapter extends BaseAdapter {
	private int iSelector = 0;
	private int iPreSelector = 0;
	private Context mcontext;
	private List<LiveListInfo> msourcelst;

	public LiveFirstListAdapter(Context context) {
		mcontext = context;
	}


	public void setSelector(int i) {
		iSelector = i;

	}
	
	public void setData(List<LiveListInfo> lst) {
		msourcelst = lst;
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
		if (mcontext == null)
			return null;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			view = View.inflate(mcontext, R.layout.live_series_item_bangtv, null);
			holder.rootlayout = (LinearLayout) view
					.findViewById(R.id.rootLayout);
			holder.textView = (TextView) view.findViewById(R.id.textView);
			holder.textView.setGravity(Gravity.CENTER);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		String str = "";
		String substr = "";
		str = msourcelst.get(arg0).getGroupname();

		if (iSelector != arg0) {
			holder.textView.setAlpha(0.7f);
			holder.rootlayout.setBackgroundColor(Color.TRANSPARENT);
			holder.textView.setTextColor(mcontext.getResources().getColor(R.color.white));
		} else {
			holder.textView.setAlpha(1f);
			holder.textView.setTextColor(mcontext.getResources().getColor(R.color.red));
			iPreSelector = iSelector;
		}
		holder.textView.setText(str);
		if (holder.textChannel != null) {

			holder.textChannel.setText(substr);
			/*holder.textChannel.setTextSize(DisplayUtil.getDisplayValue(12,
					(Activity) mcontext));*/
		}

		return view;
	}

	private class ViewHolder {
		TextView textView;
		TextView textChannel;
		LinearLayout rootlayout;
	}

}
