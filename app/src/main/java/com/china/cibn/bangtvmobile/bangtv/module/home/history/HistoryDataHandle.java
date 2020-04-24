package com.china.cibn.bangtvmobile.bangtv.module.home.history;

import com.china.cibn.bangtvmobile.bangtv.dal.MPlayRecordInfo;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.HistoryListInfo;
import com.china.cibn.bangtvmobile.bangtv.utils.DateTools;

import java.util.ArrayList;
import java.util.List;

import static com.china.cibn.bangtvmobile.bangtv.utils.DateTools.getStrTime_hms;

/**
 * fix by gz 2018/4/20.
 * 数据的获取
 */


public class HistoryDataHandle {


    private List<HistoryListInfo.HistoryListBean> historyListBeanListtemp = new ArrayList<>();

    private ArrayList<MPlayRecordInfo> allFilmList =  new ArrayList<>(); //fix by gz 2018/04/19 database

//    private HistorylistActivity HistorylistActivity = new HistorylistActivity();



    public List<HistoryListInfo.HistoryListBean> getHistorydata(PlayRecordHelpler mPlayRecordOpt)
    {

        allFilmList = mPlayRecordOpt.getAllPlayRecord();

        for (int i = 0; i < allFilmList.size(); i++ ) {
            HistoryListInfo.HistoryListBean tempBean = new HistoryListInfo.HistoryListBean();
            tempBean.setId(allFilmList.get(i).getEpgId());
            tempBean.setName(allFilmList.get(i).getPlayerName());
            tempBean.setImage(allFilmList.get(i).getPicUrl());
            tempBean.settype(allFilmList.get(i).getType());

            if(allFilmList.get(i).getType().equals("MOVIECATG")) {
                tempBean.setTimePosition(DateTools.generateTime(allFilmList.get(i).getPonitime()));
            } else {
                tempBean.setplayerpos(allFilmList.get(i).getVolumncount()+"");
            }

            historyListBeanListtemp.add(tempBean);
        }
        return historyListBeanListtemp;
    }

}
