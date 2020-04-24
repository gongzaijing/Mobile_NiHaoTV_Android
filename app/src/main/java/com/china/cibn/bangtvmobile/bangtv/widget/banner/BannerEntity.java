package com.china.cibn.bangtvmobile.bangtv.widget.banner;

/**
 * Created by gzj on 18/3/24 21:37
 *
 * <p>
 * Banner模型类
 */
public class BannerEntity {

  public BannerEntity(String link, String title, String img) {

    this.link = link;
    this.title = title;
    this.img = img;
  }


  public String title;

  public String img;

  public String link;
}
