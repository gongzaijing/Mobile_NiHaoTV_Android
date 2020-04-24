package com.china.cibn.bangtvmobile.bangtv.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;


/**
 * sqlite 本地记录数据库逻辑操作
 * 
 * @author sadshine
 * 
 */
public class PlayRecordHelpler {
	private LocalData openHelper;
	// 字段全局变量
	public static final String ID = "ID";// uuid
	public static final String EPGID = "EPGID";
	public static final String DETAILSID = "DETAILSID";// urlid
	public static final String PLAYERNAME = "PLAYERNAME";// channelName
	public static final String PLAYERPOS = "PLAYERPOS";
	public static final String VOLUMNCOUNT = "VOLUMNCOUNT";
	public static final String POINTIME = "POINTIME";
	public static final String TOTALTIME = "TOTALTIME";
	public static final String PLAYEREND = "PLAYEREND";
	public static final String PLAYERFAV = "PLAYERFAV";
	public static final String TYPE = "TYPE";
	public static final String PICURL = "PICURL";// logo
	public static final String DATETIME = "DATETIME";
	public static final String ISSINGLE = "ISSINGLE";
	public static final String ACTIONURL = "ACTIONURL";// onplay
	public static final String TABLENAME = "PLAYERECORD";
	public static final String LIVENO = "LIVENO";// no
	public static final String LIVETYPE = "LIVE";
	public static final String PRICEICON = "PRICEICON";
	public static final String TYPEICON = "TYPEICON";

	public PlayRecordHelpler(Context context) {
		openHelper = new LocalData(context);
	}

	/***************** 提供给对外接口操做 公有方法 ********************/

	// ---------------------LOGIC--------业务逻辑操作-----------//
	/**
	 * 保存当前记录
	 * 
	 * @param mplayrcinfo
	 */
	public void savePlayRecord(MPlayRecordInfo mplayrcinfo) {
		MPlayRecordInfo mqueryRecInfo = queryPlayRecord(mplayrcinfo.getEpgId());
		// 判断当前播放节目记录是否已存在
		if (mqueryRecInfo == null) {
			insertPlayRecord(mplayrcinfo);
		} else {

			if (mplayrcinfo.getIsSingle() != -1) {
				updatePlayRecord(mplayrcinfo.getEpgId(),
						mplayrcinfo.getDetailsId(), mplayrcinfo.getPonitime(),
						mplayrcinfo.getPlayerpos(), mplayrcinfo.getTotalTime(),
						mplayrcinfo.getDateTime(), mplayrcinfo.getIsSingle(),
						mplayrcinfo.getPlayerEnd(), mplayrcinfo.getActionUrl(),
						mplayrcinfo.getTypeicon(), mplayrcinfo.getPriceicon());
			} else { 
				updatePlayRecord(mplayrcinfo.getEpgId(),
						mplayrcinfo.getDetailsId(), mplayrcinfo.getPonitime(),
						mplayrcinfo.getPlayerpos(),
						mplayrcinfo.getVolumncount(), 
						mplayrcinfo.getTotalTime(),
						mplayrcinfo.getDateTime(), mplayrcinfo.getPlayerEnd(),
						mplayrcinfo.getActionUrl(),mplayrcinfo.getTypeicon()
						, mplayrcinfo.getPriceicon());
			}
			// KeelLg.v("-----update---"+mplayrcinfo.getPlayerEnd());

		}
	}

	/**
	 * 保存live当前记录
	 * 
	 * @param mplayrcinfo
	 *            type存channelId actionurl 存 urlid
	 */
	public void saveLivePlayRecord(MPlayRecordInfo mplayrcinfo) {
		try {
			MPlayRecordInfo mqueryRecInfo = queryLivePlayRecord(mplayrcinfo
					.getEpgId());
			// 判断当前播放节目记录是否已存在
			if (mqueryRecInfo == null) {
				insertPlayRecord(mplayrcinfo);
			} else {
				updateLivePlayRecord(mplayrcinfo.getEpgId(),
						mplayrcinfo.getPlayerFav());
				// KeelLg.v("-----update---"+mplayrcinfo.getPlayerEnd());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<MPlayRecordInfo> getLiveAllPlayRecord() {
		ArrayList<MPlayRecordInfo> list = queryAllLivePlayRecord(LIVETYPE);
		return list;
	}

	/**
	 * 删除当前已播放完的记录
	 * 
	 * @param mplayrcinfo
	 */
	public void delPlayRecord(MPlayRecordInfo mplayrcinfo) {
		MPlayRecordInfo mqueryRecInfo = queryPlayRecord(mplayrcinfo.getEpgId());
		// 判断当前播放节目记录是否已存在
		if (mqueryRecInfo != null
				&& mplayrcinfo.getPonitime() == mqueryRecInfo.getPonitime()
				&& mplayrcinfo.getPonitime() != 0) {
			deletePlayRecord(mplayrcinfo);
		}
	}

	/**
	 * 更新当前记录
	 * 
	 * @param iend
	 */
	public void updPlayComplete(int iend, String epgid, long datetime) {
		MPlayRecordInfo mqueryRecInfo = queryPlayRecord(epgid);
		if (mqueryRecInfo != null) {
			updatePlayComplete(iend, epgid, datetime);
		}
	}

	/**
	 * 获取当前记录详细
	 * 
	 * @param epgid
	 * @return
	 */
	public MPlayRecordInfo getPlayRcInfo(String epgid) {
		MPlayRecordInfo mqueryRecInfo = queryPlayRecord(epgid);
		return mqueryRecInfo;
	}

	/**
	 * 获取当前Live记录详细
	 *
	 * @return
	 */
	public MPlayRecordInfo getLivePlayRcInfo(String channelid) {
		MPlayRecordInfo mqueryRecInfo = queryLivePlayRecord(channelid);
		return mqueryRecInfo;
	}

	/**
	 * 获取所有播放记录
	 * 
	 * @return
	 */
	public ArrayList<MPlayRecordInfo> getAllPlayRecord() {
		ArrayList<MPlayRecordInfo> arrplayrec = queryAllPlayRecord();
		return arrplayrec;
	}

	/***************** 数据库增删改查操作 私有方法 ********************/

	// ---------------------ADD----------------------------------//
	/**
	 * 添加当前影片数据
	 *
	 */
	private void insertPlayRecord(MPlayRecordInfo mplayrcinfo) {
		try {
			ContentValues userData = new ContentValues();
			userData.put(PlayRecordHelpler.EPGID, mplayrcinfo.getEpgId());
			userData.put(PlayRecordHelpler.DETAILSID,
					mplayrcinfo.getDetailsId());
			userData.put(PlayRecordHelpler.PLAYERNAME,
					mplayrcinfo.getPlayerName());
			userData.put(PlayRecordHelpler.PLAYERPOS,
					mplayrcinfo.getPlayerpos());
			userData.put(PlayRecordHelpler.VOLUMNCOUNT,
					mplayrcinfo.getVolumncount());
			userData.put(PlayRecordHelpler.POINTIME, mplayrcinfo.getPonitime());
			userData.put(PlayRecordHelpler.TOTALTIME,
					mplayrcinfo.getTotalTime());
			userData.put(PlayRecordHelpler.TYPE, mplayrcinfo.getType());
			userData.put(PlayRecordHelpler.ISSINGLE, mplayrcinfo.getIsSingle());
			userData.put(PlayRecordHelpler.PICURL, mplayrcinfo.getPicUrl());
			userData.put(PlayRecordHelpler.ACTIONURL,
					mplayrcinfo.getActionUrl());
			userData.put(PlayRecordHelpler.DATETIME, System.currentTimeMillis());
			userData.put(PlayRecordHelpler.LIVENO, mplayrcinfo.getLiveno());
			userData.put(PlayRecordHelpler.PLAYERFAV,
					mplayrcinfo.getPlayerFav());
			userData.put(PlayRecordHelpler.PRICEICON,
					mplayrcinfo.getCornerPrice());
			openHelper.execInsert(TABLENAME, null, userData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// -----------------------DEL------------------------------//
	/**
	 * 播放结束后删除记录
	 *
	 */
	private void deletePlayRecord(MPlayRecordInfo mplayrcinfo) {
		try {
			openHelper.execSQL("DELETE FROM " + PlayRecordHelpler.TABLENAME
					+ " WHERE " + PlayRecordHelpler.EPGID + "='"
					+ mplayrcinfo.getEpgId() + "' AND"
					+ PlayRecordHelpler.DETAILSID + "='"
					+ mplayrcinfo.getDetailsId() + "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 播放结束后删除记录
	 *
	 */
	public void deletePlayRecordBy(String epgId) {
		try {
			openHelper.execSQL("DELETE FROM " + PlayRecordHelpler.TABLENAME
					+ " WHERE " + PlayRecordHelpler.EPGID + "='"
					+ epgId + "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// --------------------------UPDATE-------------------------//

	/**
	 * 实时更新播放完成记录
	 *
	 */
	private void updatePlayComplete(int iend, String iepg, long datetime) {
		try {
			openHelper.execSQL("UPDATE " + PlayRecordHelpler.TABLENAME
					+ " SET " + PlayRecordHelpler.PLAYEREND + "='" + iend
					+ "' , " + PlayRecordHelpler.DATETIME + "='" + datetime
					+ "' WHERE " + PlayRecordHelpler.EPGID + "='" + iepg + "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 实时更新播放记录
	 * 
	 * @param epgid
	 * @param detailsid
	 */
	private void updatePlayRecord(String epgid, String detailsid, int pointime,
                                  int pos, int volumncount, int totaltime, long datetime, int iplayend,
                                  String actionurl, String typeicon, String priceicon) {

		try {
			openHelper
					.execSQL("UPDATE " + PlayRecordHelpler.TABLENAME + " SET "
							+ PlayRecordHelpler.POINTIME + "='" + pointime + "' , "
							+ PlayRecordHelpler.PLAYERPOS + "='" + pos+ "' , " 
							+ PlayRecordHelpler.VOLUMNCOUNT + "=" + volumncount + " , "
							+ PlayRecordHelpler.TOTALTIME + "='" + totaltime + "' , "
							+ PlayRecordHelpler.DATETIME + "='" + datetime + "' , "
							+ PlayRecordHelpler.DETAILSID + "='" + detailsid + "' , "
							+ PlayRecordHelpler.PLAYEREND + "='" + iplayend + "' , "
							+ PlayRecordHelpler.ACTIONURL + "='" + actionurl  + "' , "
							+ PlayRecordHelpler.TYPEICON + "='" + typeicon + "' , "
							+ PlayRecordHelpler.PRICEICON + "='" + priceicon
							+ "' WHERE "
							+ PlayRecordHelpler.EPGID + "='" + epgid + "'");
		} finally {
		}
	}

	/**
	 * 实时更新播放记录
	 * 
	 * @param epgid
	 * @param detailsid
	 * @param pointime
	 * @param pos
	 * @param totaltime
	 * @param datetime
	 * @param issingle
	 */
	private void updatePlayRecord(String epgid, String detailsid, int pointime,
                                  int pos, int totaltime, long datetime, int issingle, int iplayend,
                                  String actionurl, String typeicon, String priceicon) {
		try {
			openHelper.execSQL("UPDATE " + PlayRecordHelpler.TABLENAME + " SET "
					+ PlayRecordHelpler.POINTIME + "=" + pointime + " , "
					+ PlayRecordHelpler.PLAYERPOS + "=" + pos + " , "
					+ PlayRecordHelpler.TOTALTIME + "=" + totaltime + " , "
					+ PlayRecordHelpler.DATETIME + "=" + datetime + " , "
					+ PlayRecordHelpler.ISSINGLE + "=" + issingle + " , "
					+ PlayRecordHelpler.DETAILSID + "=" + detailsid + " , "
					+ PlayRecordHelpler.PLAYEREND + "=" + iplayend + " , "
					+ PlayRecordHelpler.TYPEICON + "=" + typeicon + " , "
					+ PlayRecordHelpler.PRICEICON + "=" + priceicon + " , "
					+ PlayRecordHelpler.ACTIONURL + "=" + actionurl
					+ " WHERE "
					+ PlayRecordHelpler.EPGID + "=" + epgid);
		} finally {
		}
	}

	// --------------------------QUERY-------------------------//
	/**
	 * 获取播放记录信息
	 * 
	 * @param epgid
	 * @return
	 */
	private MPlayRecordInfo queryPlayRecord(String epgid) {
		MPlayRecordInfo mPlayercInfo = null;
		Cursor cursor = null;
		try {
			String sql = "SELECT " + PlayRecordHelpler.EPGID + ", "
					+ PlayRecordHelpler.DETAILSID + ", "
					+ PlayRecordHelpler.POINTIME + ", "
					+ PlayRecordHelpler.TOTALTIME + ", "
					+ PlayRecordHelpler.PLAYERNAME + ", "
					+ PlayRecordHelpler.TYPE + ", " + PlayRecordHelpler.PICURL
					+ ", " + PlayRecordHelpler.ISSINGLE + ", "
					+ PlayRecordHelpler.PLAYERPOS + ", "
					+ PlayRecordHelpler.VOLUMNCOUNT + ", "
					+ PlayRecordHelpler.ACTIONURL + ", "					
					+ PlayRecordHelpler.DATETIME + ", "
					+ PlayRecordHelpler.TYPEICON + ", "
					+ PlayRecordHelpler.PRICEICON + 
					" FROM "
					+ PlayRecordHelpler.TABLENAME + " WHERE "
					+ PlayRecordHelpler.EPGID + "='" + epgid + "' ORDER BY "
					+ PlayRecordHelpler.DATETIME + " DESC, "
					+ PlayRecordHelpler.PLAYEREND + " ASC";

			cursor = openHelper.runQuery(sql, null);
			if (cursor.moveToFirst()) {
				mPlayercInfo = new MPlayRecordInfo();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					mPlayercInfo.setEpgId(cursor.getString(0));
					mPlayercInfo.setDetailsId(cursor.getString(1));
					mPlayercInfo.setPonitime(cursor.getInt(2));
					mPlayercInfo.setTotalTime(cursor.getInt(3));
					mPlayercInfo.setPlayerName(cursor.getString(4));
					mPlayercInfo.setType(cursor.getString(5));
					mPlayercInfo.setPicUrl(cursor.getString(6));
					mPlayercInfo.setIsSingle(cursor.getInt(7));
					mPlayercInfo.setPlayerpos(cursor.getInt(8));
					mPlayercInfo.setVolumncount(cursor.getInt(9));
					mPlayercInfo.setActionUrl(cursor.getString(10));
					mPlayercInfo.setDateTime(cursor.getInt(11));
					mPlayercInfo.setDateTime(cursor.getInt(12));
					mPlayercInfo.setDateTime(cursor.getInt(13));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}

		}

		return mPlayercInfo;
	}

	/**
	 * 实时live更新播放记录
	 *
	 *            type:LIVETYPE
	 */
	private void updateLivePlayRecord(String chanelid, int fav) {
		try {
			openHelper.execSQL("UPDATE " + PlayRecordHelpler.TABLENAME
					+ " SET " + PlayRecordHelpler.PLAYERFAV + "='" + fav
					+ "' WHERE " + PlayRecordHelpler.EPGID + "='" + chanelid
					+ "'");
		} finally {
		}
	}

	/**
	 * 获取live播放记录信息
	 *
	 * @return
	 */
	private MPlayRecordInfo queryLivePlayRecord(String channelId) {
		MPlayRecordInfo mPlayercInfo = null;
		Cursor cursor = null;
		try {
			cursor = openHelper.runQuery("SELECT " + PlayRecordHelpler.EPGID
					+ ", " + PlayRecordHelpler.DETAILSID + ", "
					+ PlayRecordHelpler.POINTIME + ", "
					+ PlayRecordHelpler.TOTALTIME + ", "
					+ PlayRecordHelpler.PLAYERNAME + ", "
					+ PlayRecordHelpler.TYPE + ", "
					+ PlayRecordHelpler.PICURL
					+ ", " + PlayRecordHelpler.ISSINGLE + ", "
					+ PlayRecordHelpler.PLAYERPOS + ", "
					+ PlayRecordHelpler.ACTIONURL + ", "
					+ PlayRecordHelpler.DATETIME + ", "
					+ PlayRecordHelpler.LIVENO + ", "
					+ PlayRecordHelpler.PLAYERFAV + " FROM "
					+ PlayRecordHelpler.TABLENAME + " WHERE "
					+ PlayRecordHelpler.EPGID + "='" + channelId + "'", null);

			while (cursor.moveToNext()) {
				mPlayercInfo = new MPlayRecordInfo();
				mPlayercInfo.setEpgId(cursor.getString(0));
				mPlayercInfo.setDetailsId(cursor.getString(1));
				mPlayercInfo.setPonitime(cursor.getInt(2));
				mPlayercInfo.setTotalTime(cursor.getInt(3));
				mPlayercInfo.setPlayerName(cursor.getString(4));
				mPlayercInfo.setType(cursor.getString(5));
				mPlayercInfo.setPicUrl(cursor.getString(6));
				mPlayercInfo.setIsSingle(cursor.getInt(7));
				mPlayercInfo.setPlayerpos(cursor.getInt(8));
				mPlayercInfo.setActionUrl(cursor.getString(9));
				mPlayercInfo.setDateTime(cursor.getInt(10));
				mPlayercInfo.setTypeicon(cursor.getString(11));
				mPlayercInfo.setPlayerFav(cursor.getInt(12));
			}
		} catch (Exception ex) {

		} finally {
			cursor.close();
		}

		return mPlayercInfo;
	}

	/**
	 * 获取live播放记录信息
	 *
	 * @return
	 */
	private ArrayList<MPlayRecordInfo> queryAllLivePlayRecord(String strType) {
		ArrayList<MPlayRecordInfo> arrPlayRec = new ArrayList<MPlayRecordInfo>();
		Cursor cursor = null;
		try {
			cursor = openHelper.runQuery("SELECT " + PlayRecordHelpler.EPGID
					+ ", " + PlayRecordHelpler.DETAILSID + ", "
					+ PlayRecordHelpler.POINTIME + ", "
					+ PlayRecordHelpler.TOTALTIME + ", "
					+ PlayRecordHelpler.PLAYERNAME + ", "
					+ PlayRecordHelpler.TYPE + ", "
					+ PlayRecordHelpler.PICURL + ", "
					+ PlayRecordHelpler.ISSINGLE + ", "
					+ PlayRecordHelpler.PLAYERPOS + ", "
					+ PlayRecordHelpler.ACTIONURL + ", "
					+ PlayRecordHelpler.DATETIME + ", "
					+ PlayRecordHelpler.LIVENO + ","
					+ PlayRecordHelpler.PRICEICON +" FROM "
					+ PlayRecordHelpler.TABLENAME + " WHERE "
					+ PlayRecordHelpler.TYPE + "='" + strType + "' AND "
					+ PlayRecordHelpler.PLAYERFAV + "='1'" + " ORDER BY "
					+ PlayRecordHelpler.DATETIME + " DESC, "
					+ PlayRecordHelpler.PLAYEREND + " ASC", null);

			while (cursor.moveToNext()) {
				MPlayRecordInfo mPlayercInfo = new MPlayRecordInfo();
				mPlayercInfo = new MPlayRecordInfo();
				mPlayercInfo.setEpgId(cursor.getString(0));
				mPlayercInfo.setDetailsId(cursor.getString(1));
				mPlayercInfo.setPonitime(cursor.getInt(2));
				mPlayercInfo.setTotalTime(cursor.getInt(3));
				mPlayercInfo.setPlayerName(cursor.getString(4));
				mPlayercInfo.setType(cursor.getString(5));
				mPlayercInfo.setPicUrl(cursor.getString(6));
				mPlayercInfo.setIsSingle(cursor.getInt(7));
				mPlayercInfo.setPlayerpos(cursor.getInt(8));
				mPlayercInfo.setActionUrl(cursor.getString(9));
				mPlayercInfo.setDateTime(cursor.getInt(10));
				mPlayercInfo.setLiveno(cursor.getString(11));
				mPlayercInfo.setCornerPrice(cursor.getString(12));
				arrPlayRec.add(mPlayercInfo);
			}
		} catch (Exception ex) {

		} finally {
			cursor.close();
		}

		return arrPlayRec;
	}

	/**
	 * 获取所有播放记录信息
	 *
	 * @return
	 */
	private ArrayList<MPlayRecordInfo> queryAllPlayRecord() {
		ArrayList<MPlayRecordInfo> arrPlayRec = new ArrayList<MPlayRecordInfo>();
		Cursor cursor = null;
		try {
			cursor = openHelper.runQuery("SELECT "
					+ PlayRecordHelpler.EPGID + ", "
					+ PlayRecordHelpler.DETAILSID + ", "
					+ PlayRecordHelpler.POINTIME + ", "
					+ PlayRecordHelpler.TOTALTIME + ", "
					+ PlayRecordHelpler.PLAYERNAME + ", "
					+ PlayRecordHelpler.TYPE + ", "
					+ PlayRecordHelpler.PICURL + ", "
					+ PlayRecordHelpler.ISSINGLE + ", "
					+ PlayRecordHelpler.PLAYERPOS + ", "
					+ PlayRecordHelpler.VOLUMNCOUNT + ", "
					+ PlayRecordHelpler.ACTIONURL + ", "
					+ PlayRecordHelpler.DATETIME +", "
					+ PlayRecordHelpler.TYPEICON + ", "
					+ PlayRecordHelpler.PRICEICON 
					+ " FROM "
					+ PlayRecordHelpler.TABLENAME + " ORDER BY "
					+ PlayRecordHelpler.DATETIME + " DESC", null);

			while (cursor.moveToNext()) {
				MPlayRecordInfo mPlayercInfo = new MPlayRecordInfo();
				mPlayercInfo.setEpgId(cursor.getString(0));
				mPlayercInfo.setDetailsId(cursor.getString(1));
				mPlayercInfo.setPonitime(cursor.getInt(2));
				mPlayercInfo.setTotalTime(cursor.getInt(3));
				mPlayercInfo.setPlayerName(cursor.getString(4));
				mPlayercInfo.setType(cursor.getString(5));
				mPlayercInfo.setPicUrl(cursor.getString(6));
				mPlayercInfo.setIsSingle(cursor.getInt(7));
				mPlayercInfo.setPlayerpos(cursor.getInt(8));
				mPlayercInfo.setVolumncount(cursor.getInt(9));
				mPlayercInfo.setActionUrl(cursor.getString(10));
				mPlayercInfo.setDateTime(cursor.getInt(11));
				mPlayercInfo.setTypeicon(cursor.getString(12));
				mPlayercInfo.setPriceicon(cursor.getString(13));
				arrPlayRec.add(mPlayercInfo);
			}
		} catch (Exception ex) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return arrPlayRec;
	}

}
