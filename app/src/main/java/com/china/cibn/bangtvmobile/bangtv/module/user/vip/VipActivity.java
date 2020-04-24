package com.china.cibn.bangtvmobile.bangtv.module.user.vip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.ProductInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleProgressView;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 2018/4/3 13:49
 *
 * <p>
 * 开通会员界面
 */

public class VipActivity extends RxBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_xieyi)
    TextView mTvXieyi;

    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;

    @BindView(R.id.layout_pro)
    LinearLayout linearLayout;

    @BindView(R.id.btn_wxpay)
    Button btnWxPay;

    @BindView(R.id.btn_paypal)
    Button btnPaypal;


    @BindView(R.id.list_item)
    ListView mListproduct;

    @BindView(R.id.checkxieyi)
    CheckBox mCheckXieyi;

    private List<ProductInfo.DataBean> productList = new ArrayList<>();

    private IWXAPI iwxapi;

    private WXInfo wxInfo = new WXInfo();

    private String mPid;

    private ProgressDialog pd;

    private  ProductListDatapter mProductListAdapter;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private int _postion=0;

    List<PayPalItem > productsInCart= new ArrayList<>();

    private AlertDialog.Builder mDialog;

    public static VipActivity instance;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(Config.CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
  @Override
  public int getLayoutId() {

    return R.layout.activity_vip_bangtv;
  }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }


    @Override
  public void initViews(Bundle savedInstanceState) {
      instance =VipActivity.this;
      initWechat();
      Intent intent = new Intent(this, PayPalService.class);
      intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
      startService(intent);
      mListproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Log.d("","");
              _postion=position;
              mPid=productList.get(position).getPid();
              mProductListAdapter.getItemRadio(position);
              mProductListAdapter.notifyDataSetChanged();
          }
      });

    mTvXieyi.setOnClickListener(v->ProtocolDialog.launch(VipActivity.this));
      btnWxPay.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              wxPay();
          }
      });
      btnPaypal.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onBuyPressed();
          }
      });
        mCheckXieyi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    btnPaypal.setEnabled(true);
                    btnWxPay.setEnabled(true);
                }else{
                    btnPaypal.setEnabled(false);
                    btnWxPay.setEnabled(false);
                }
            }
        });
        loadData();
  }

    /**
     * PayPal支付
     * */
    public void onBuyPressed() {
//         PayPalItem的四个参数. 1.商品名称 2.商品数量 3.商品单价 4.货币 5.商品描述
        PayPalItem item = new PayPalItem(productList.get(_postion).getPname(), 1, new BigDecimal(productList.get(_postion).getPrice()), "USD", "1");

        productsInCart.add(item);

        PayPalPayment thingsToBuy =getPayment();

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        productsInCart.clear();
    }

    /**
     * 获取PayMent对象
     *
     * @return
     */
    private PayPalPayment getPayment() {
        // 以商品集合的长度作为数组长度创建数组.
        PayPalItem[] items = new PayPalItem[productsInCart.size()];

        // 把集合的数据转为数组储存.
        items = productsInCart.toArray(items);

        // 订单的商品总金额,四舍五入保留2位小数
        BigDecimal subtotal = PayPalItem.getItemTotal(items).setScale(2, BigDecimal.ROUND_HALF_UP);

        // 如果订单有运费跟税,在这里添加
        BigDecimal shipping = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);

        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal  mAmount = subtotal.add(shipping).setScale(2, BigDecimal.ROUND_HALF_UP);
        String description = "总额";
        PayPalPayment payment = new PayPalPayment(mAmount, "USD", description, PayPalPayment.PAYMENT_INTENT_SALE);

        payment.items(items).paymentDetails(paymentDetails);

        payment.custom(AppGlobalVars.USER_ID+"_phone"+"_"+productList.get(0).getPid());

        return payment;
    }

    @Override
    public void loadData() {
        linearLayout.setVisibility(View.GONE);
        RetrofitHelper.getBangTVAppAPI().getProductList(BangtvApp.versionCodeUrl)
                .compose(bindToLifecycle())
                .doOnSubscribe(this::showProgressBar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    productList.addAll(resultBeans.getData());
                    finishTask();
                }, throwable -> {
                    Log.i("","");
                    hideProgressBar();
                    linearLayout.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void finishTask() {
        mProductListAdapter =new ProductListDatapter(this,productList);
        mListproduct.setAdapter(mProductListAdapter);
        setListViewHeightBasedOnChildren(mListproduct);
        hideProgressBar();
        linearLayout.setVisibility(View.VISIBLE);
        btnWxPay.setEnabled(true);
        btnPaypal.setEnabled(true);
    }

    @Override
  public void initToolBar() {

    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

  }

  /**
   * 初始化微信支付api
   */
  private void initWechat() {
    iwxapi = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX);
  }

    /**
     * 微信支付
     * */
  private void wxPay(){
    final IWXAPI mWxApi  = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX);
    // 判断是否安装客户端
    if(!mWxApi.isWXAppInstalled()&& !mWxApi.isWXAppSupportAPI()){
      ToastUtil.ShortToast(getString(R.string.VipActivity_InstallWX));
      return;
    }
      if(TextUtils.isEmpty(mPid)){
          mPid=productList.get(0).getPid();
      }
      pd = ProgressDialog.show(this, "", "加载中，请稍后···");
    RetrofitHelper.getUserAPIBangtv().startWX(mPid)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              wxInfo=resultBeans;
                Runnable payRunnable = new Runnable() {  //放在子线程
                    @Override
                    public void run() {
              PayReq req = new PayReq();
              req.appId = wxInfo.getData().getAppid();// 微信开放平台审核通过的应用APPID
              req.partnerId = wxInfo.getData().getPartnerid();// 微信支付分配的商户号
              req.prepayId =  wxInfo.getData().getPrepayid();// 预支付订单号，app服务器调用“统一下单”接口获取
              req.nonceStr = wxInfo.getData().getNoncestr();// 随机字符串，不长于32位
              req.timeStamp = wxInfo.getData().getTimestamp();// 时间戳
              req.packageValue = wxInfo.getData().getPackageX();// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
              req.sign = wxInfo.getData().getSign();// 签名，
              // 调用微信SDK，发起支付，回调WxPayEntryActivity
                        mWxApi.registerApp(Config.APP_ID_WX);
                        mWxApi.sendReq(req);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
                if(pd.isShowing()){
                    pd.dismiss();
                }
            }, throwable -> {
              ToastUtil.ShortToast(getString(R.string.VipActivity_LoadFial));
                if(pd.isShowing()){
                    pd.dismiss();
                }
            });

  }
    @Override
    public void showProgressBar() {

        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }
    @Override
    public void hideProgressBar() {

        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();


    }

    @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                         ToastUtil.LongToast(getString(R.string.VipActivity_PayPalSucc));
                        Log.i("", confirm.toJSONObject().toString(4));
                        initDialog();

                    } catch (JSONException e) {
                        Log.i("","");                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("","");
                ToastUtil.LongToast(getString(R.string.VipActivity_PayPalCancel));

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
              Log.i("","");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);
                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);
                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void initDialog(){
        mDialog = new AlertDialog.Builder(this);
        mDialog.setMessage(getString(R.string.vipactivity_okvip_text));
        mDialog.setNegativeButton(getString(R.string.vipactivity_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               VipActivity.this.finish();
            }
        }).setCancelable(false).create().show();
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
