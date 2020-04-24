package com.china.cibn.bangtvmobile.bangtv.entity.recommend;

import java.util.List;

/**
 * Created by gzj on 2018/3/23  19:25
 *
 * <p>
 * 首页Banner推荐
 */

public class RecommendBannerInfoBangTv {


  /**
   * id : 50
   * layoutFile : http://ag05.resource.bangtv.tv/res/layout/28/tuijian.json
   * itemList : [{"name":"最近观看","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171106/5a010f4ac1da6.jpg","m_img":"","id":"17","action":"OPEN_LIVEPLAYER","actionParam":{"groupid":0,"channelId":0,"channelName":"","channelNo":0,"channel":"all"}},{"name":"三三布局最下","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171106/5a0115c2cf4ad.jpg","m_img":"","id":"","action":"OPEN_HISTORY","actionParam":{}},{"name":"搜索","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171106/5a0115d62f72f.jpg","m_img":"","id":"44","action":"OPEN_SEARCH","actionParam":{}},{"name":"猎场","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180330/5abe0ed4abfc9.jpg","m_img":"","id":"t-105","action":"OPEN_DETAIL","actionParam":{}},{"name":"急诊科医生","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171114/5a0b969073087.jpg","m_img":"","id":"t-98","action":"OPEN_DETAIL","actionParam":{}},{"name":"奇门遁甲","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe38c3e9875.jpg","m_img":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe3ae55bf2c.jpg","id":"t-248","action":"OPEN_DETAIL","actionParam":{}},{"name":"大话红娘","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180426/5ae2c037c39ef.jpg","m_img":"","id":"t-132","action":"OPEN_DETAIL","actionParam":{}},{"name":"超级飞侠3","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171113/5a095ba189e04.jpg","m_img":"","id":"c-11","action":"OPEN_DETAIL","actionParam":{}},{"name":"智趣羊学堂之羊羊游世界","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180327/5abb0fcf4576c.jpg","m_img":"","id":"c-77","action":"OPEN_DETAIL","actionParam":{}},{"name":"战狼2","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe38e1ccac0.jpg","m_img":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe38f3933db.jpg","id":"m-216","action":"OPEN_DETAIL","actionParam":{}},{"name":"大军师司马懿之虎啸龙吟","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe387823704.jpg","m_img":"","id":"t-122","action":"OPEN_DETAIL","actionParam":{}},{"name":"凡人的品格","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171114/5a0b961b9e24a.jpg","m_img":"","id":"t-93","action":"OPEN_DETAIL","actionParam":{}},{"name":"琅琊榜之风起长林","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180517/5afe382e6fe1d.jpg","m_img":"","id":"t-125","action":"OPEN_DETAIL","actionParam":{}},{"name":"大片起来嗨","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171113/5a0953183fecf.jpg","m_img":"","id":"v-82","action":"OPEN_DETAIL","actionParam":{}},{"name":"那年花开月正圆","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171031/59f9619cc1d59.jpg","m_img":"","id":"t-94","action":"OPEN_DETAIL","actionParam":{}},{"name":"射雕英雄传","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20171113/5a09532c1760e.jpg","m_img":"","id":"t-102","action":"OPEN_DETAIL","actionParam":{}},{"name":"芳华纪录片","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180327/5abb111c6567d.jpg","m_img":"","id":"d-83","action":"OPEN_DETAIL","actionParam":{}},{"name":"五代十国","subName":"","image":"http://ag05.resource.bangtv.tv/recommend/0/20180327/5abb114d7bab2.jpg","m_img":"","id":"d-79","action":"OPEN_DETAIL","actionParam":{}}]
   */

  private String id;
  private String layoutFile;
  private List<ItemListBean> itemList;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLayoutFile() {
    return layoutFile;
  }

  public void setLayoutFile(String layoutFile) {
    this.layoutFile = layoutFile;
  }

  public List<ItemListBean> getItemList() {
    return itemList;
  }

  public void setItemList(List<ItemListBean> itemList) {
    this.itemList = itemList;
  }

  public static class ItemListBean {
    /**
     * name : 最近观看
     * subName :
     * image : http://ag05.resource.bangtv.tv/recommend/0/20171106/5a010f4ac1da6.jpg
     * m_img :
     * id : 17
     * action : OPEN_LIVEPLAYER
     * actionParam : {"groupid":0,"channelId":0,"channelName":"","channelNo":0,"channel":"all"}
     */

    private String name;
    private String subName;
    private String image;
    private String m_img;
    private String id;
    private String action;
    private ActionParamBean actionParam;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getSubName() {
      return subName;
    }

    public void setSubName(String subName) {
      this.subName = subName;
    }

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }

    public String getM_img() {
      return m_img;
    }

    public void setM_img(String m_img) {
      this.m_img = m_img;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getAction() {
      return action;
    }

    public void setAction(String action) {
      this.action = action;
    }

    public ActionParamBean getActionParam() {
      return actionParam;
    }

    public void setActionParam(ActionParamBean actionParam) {
      this.actionParam = actionParam;
    }

    public static class ActionParamBean {
      /**
       * groupid : 0
       * channelId : 0
       * channelName :
       * channelNo : 0
       * channel : all
       */

      private int groupid;
      private int channelId;
      private String channelName;
      private int channelNo;
      private String channel;

      public int getGroupid() {
        return groupid;
      }

      public void setGroupid(int groupid) {
        this.groupid = groupid;
      }

      public int getChannelId() {
        return channelId;
      }

      public void setChannelId(int channelId) {
        this.channelId = channelId;
      }

      public String getChannelName() {
        return channelName;
      }

      public void setChannelName(String channelName) {
        this.channelName = channelName;
      }

      public int getChannelNo() {
        return channelNo;
      }

      public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
      }

      public String getChannel() {
        return channel;
      }

      public void setChannel(String channel) {
        this.channel = channel;
      }
    }
  }
}
