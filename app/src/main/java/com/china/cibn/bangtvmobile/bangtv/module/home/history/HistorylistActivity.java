package com.china.cibn.bangtvmobile.bangtv.module.home.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.HistoryListAdapter;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.HistoryListInfo;

import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.widget.CustomEmptyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * fix by gz on 2018/4/18.
 *
 * 观看历史汇总页面
 */
public class HistorylistActivity extends RxBaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;


    private HistoryListAdapter mAdapter;
    private List<HistoryListInfo.HistoryListBean> historyListBeanList;

    private String mId;


    private HistoryDataHandle historyDataHandle;    //fix by gz 2018/04/19 database
    private PlayRecordHelpler mPlayRecordOpt;       //fix by gz 2018/04/19 database


    @Override
    public int getLayoutId() {

        return R.layout.activity_history_bangtv;
    }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }


    @Override
    public void initViews(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getStringExtra("historyID");
        }
        initCustomEmptyView();
        initRefreshLayout();
        initRecyclerView();
    }


    public void initCustomEmptyView() {
        historyListBeanList= new ArrayList<>();
        mCustomEmptyView.setEmptyImage(R.drawable.ic_movie_pay_order_error03);
        mCustomEmptyView.setEmptyText(getString(R.string.historyactivity_nohstory));
    }



    @Override
    public void initRecyclerView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryListAdapter(this, mRecyclerView, historyListBeanList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            loadData();
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }




    @Override
    public void loadData() {

        mPlayRecordOpt = new PlayRecordHelpler(this);
        historyDataHandle = new HistoryDataHandle();
        if(historyListBeanList!=null){
            historyListBeanList.clear();
        }
        historyListBeanList.addAll( historyDataHandle.getHistorydata(mPlayRecordOpt));

        if (0 == historyListBeanList.size()) {
            historyempty();
        }
        else {
            historyfull();
        }

        finishTask();

    }

    public void historyempty()
    {
        mToolbar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);//View.VISIBLE
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setVisibility(View.VISIBLE);
    }

    public void historyfull() {
        mToolbar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);//View.VISIBLE
        mRecyclerView.setVisibility(View.VISIBLE);
        mCustomEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void finishTask() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initToolBar() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity activity,String mId) {

        Intent intent = new Intent(activity, HistorylistActivity.class);
        intent.putExtra("historyID", mId);

        activity.startActivity(intent);
    }
}
