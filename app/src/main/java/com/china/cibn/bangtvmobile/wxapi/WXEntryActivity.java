package com.china.cibn.bangtvmobile.wxapi;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSON;
import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.dal.UserHelper;
import com.china.cibn.bangtvmobile.bangtv.entity.user.WXAccessTokenEntity;
import com.china.cibn.bangtvmobile.bangtv.entity.user.WXBaseRespEntity;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 微信支付登录
 *
 * @author gzj
 * @date 2018年5月16日16:20:44
 */

public class WXEntryActivity extends RxBaseActivity implements IWXAPIEventHandler{

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    private UserHelper userHelper;

    private LocalData localData;

    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_empty;
    }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initDB();
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);
        Log.d("","------------------------------------");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result =  api.handleIntent(getIntent(), this);
            if(!result){
                Log.d("","参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initToolBar() {

    }

    private void initDB() {
        localData = new LocalData(this);
        userHelper = new UserHelper(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("","baseReq:"+ JSON.toJSONString(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d("","baseResp:--A"+JSON.toJSONString(baseResp));
        Log.d("","baseResp--B:"+baseResp.errStr+","+baseResp.openId+","+baseResp.transaction+","+baseResp.errCode);
        WXBaseRespEntity entity = JSON.parseObject(JSON.toJSONString(baseResp),WXBaseRespEntity.class);
        String result = "";
        switch(baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
                OkHttpClient  httpClient=new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("appid",Config.APP_ID_WX)
                        .add("secret",Config.APP_SECRET_WX)
                        .add("code",entity.getCode())
                        .add("grant_type","authorization_code")
                        .build();
                Request request=new Request.Builder()

                        .post(requestBody )

                        .url("https://api.weixin.qq.com/sns/oauth2/access_token")

                        .build();//(post请求)
                httpClient.newCall(request).enqueue(new Callback(){

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(WXEntryActivity.this,"微信登录失败！", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                               String string = body.string();// 把返回的结果转换为String类型
                                WXAccessTokenEntity accessTokenEntity = JSON.parseObject(string,WXAccessTokenEntity.class);
                                if(accessTokenEntity!=null){
                                    getUserInfo(accessTokenEntity);
                                }else {
                                    Log.d("","获取失败");
                                }
                        Log.d("",string);
                    }

                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Log.d("","发送取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Log.d("","发送被拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = "签名错误";
                Log.d("","签名错误");
                break;
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("支付结果");
                builder.setMessage(baseResp.errCode + "");
                builder.show();
               break;
            default:
                result = "发送返回";
//                showMsg(0,result);
                finish();
                break;
        }
//        Toast.makeText(WXEntryActivity.this,result, Toast.LENGTH_LONG).show();

    }

    /**
     * 获取个人信息
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                pd = ProgressDialog.show(WXEntryActivity.this, "", "正在登录···");
            }
        });

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("accessToken", accessTokenEntity.getAccess_token());
        postData.put("openid", accessTokenEntity.getOpenid());
        RetrofitHelper.getUserAPIBangtv().getLoginInfo(postData)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    String userID = "", userName = "", userPicUrl = "", userToken = "";
                    userID = resultBeans.getUid();
                    userToken =resultBeans.getToken();
                    userName = resultBeans.getUname();
                    userPicUrl = resultBeans.getPic();
                    AppGlobalVars.USER_ID=userID;
                    AppGlobalVars.USER_TOKEN=userToken;
                    AppGlobalVars.USER_NICK_NAME=userName;
                    AppGlobalVars.USER_PIC=userPicUrl;
                    localData.setKV(
                            AppGlobalConsts.PERSIST_NAMES.CURRENT_USER.name(),
                            userID);
                    ContentValues userData = new ContentValues();
                    userData.put("userid", userID);
                    userData.put("wxname", userName);
                    userData.put("wxheadimgurl", userPicUrl);
                    userData.put("token", userToken);
                    userHelper.addUser(userData);
                    Intent intent = getIntent();

                    intent.putExtra("mUserNickName",userName);
                    intent.putExtra("mUserPic",userPicUrl);
                    WXEntryActivity.this.setResult(0,intent);
                    finish();
                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                }, throwable -> {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    //放在UI线程弹Toast
                                    ToastUtil.ShortToast("微信登录失败");
                                    if(pd.isShowing()){
                                        pd.dismiss();
                                    }
                                }
                            });
                            //此处会发生异常
//				Toast.makeText(MainActivity.this, "toast in work thread", Toast.LENGTH_LONG).show();
                        }
                    }).start();
                    finish();

                    Log.i("","");
                });

    }
}
