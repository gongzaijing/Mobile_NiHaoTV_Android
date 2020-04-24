package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class CheckUpdateInfo {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Forced : 1
         * versionId : 2802
         * versionName : bangtv v1.0.2802
         * packageLocation : http://198.255.14.42:8314/version/ott/bangtv/28/OTT_Bangtv_V2802_20180206_bangtv_V1.apk
         * md5 : 5c707db8210e01ca0d852cd3dde8c39e
         * upgradePattern : APK
         * desc : 版本信息：
         1.增加点播试看
         2.增加剧集试看
         3.增加开机画面倒计时
         4.增加春节风格的用户界面
         5.修复BUG，优化体验
         6.增加广告系统功能
         */

        private String Forced;
        private String versionId;
        private String versionName;
        private String packageLocation;
        private String md5;
        private String upgradePattern;
        private String desc;

        public String getForced() {
            return Forced;
        }

        public void setForced(String Forced) {
            this.Forced = Forced;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getPackageLocation() {
            return packageLocation;
        }

        public void setPackageLocation(String packageLocation) {
            this.packageLocation = packageLocation;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getUpgradePattern() {
            return upgradePattern;
        }

        public void setUpgradePattern(String upgradePattern) {
            this.upgradePattern = upgradePattern;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
