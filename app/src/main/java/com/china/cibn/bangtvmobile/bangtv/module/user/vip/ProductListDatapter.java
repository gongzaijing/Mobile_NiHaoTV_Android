package com.china.cibn.bangtvmobile.bangtv.module.user.vip;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.ProductInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cibn on 2018/5/14.
 */

public class ProductListDatapter extends BaseAdapter {

   private Context context;

    private List<ProductInfo.DataBean> productList;

    private LayoutInflater mInflater;

    private  int itempostion=0;

    public ProductListDatapter(Context context,List<ProductInfo.DataBean> _productList) {
        this.context = context;
        this.productList = _productList;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void getItemRadio(int itempostion){
        this.itempostion=itempostion;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_product_bangtv, null);
        ImageView imageView = convertView.findViewById(R.id.iv_image);
       TextView mTvDollorPrice = convertView.findViewById(R.id.tv_dollar_price);
        TextView mTvDollar = (TextView) convertView.findViewById(R.id.tv_one_dollar);
        TextView mTvRmb = (TextView) convertView.findViewById(R.id.tv_one_rmb);
        mTvDollorPrice.setText(productList.get(position).getPrice());
        mTvDollar.setText(productList.get(position).getPriceY());
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        String rmbOnepPrice = decimalFormat.format((Float.parseFloat(productList.get(position).getRate()) * Float.parseFloat(productList.get(position).getPrice())));
        mTvRmb.setText("(RMB "+ rmbOnepPrice + " )");

if(itempostion==position){
    Resources resources =context.getResources();
    Drawable imageDrawable = resources.getDrawable(R.drawable.radio_btn_ivf);
    imageView.setBackgroundDrawable(imageDrawable);
}
        return convertView;
    }
}
