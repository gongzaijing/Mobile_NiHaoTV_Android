package com.china.cibn.bangtvmobile.bangtv.module.user.wxlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @name Login
 * @class name：com..cloud
 * @class describe
 * @anthor
 * @time 2018/4/1 10:34
 * @change
 * @chang time
 * @class describe
 */

public class WXLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,WXLoginActivity.class);
        activity.startActivity(intent);
    }

    private ImageView ivHead;

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login_bangtv);
        ivHead = (ImageView)findViewById(R.id.iv_wx_head);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BangtvApp.isWeChatAppInstalled(getApplicationContext())){
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                    req.state = "wechat_sdk_微信登录";
                    api.sendReq(req);
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.WXLoginActivity_InstallWX),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void CancalWXLogin(){
        ivHead.setImageResource(R.drawable.ic_hotbitmapgg_avatar);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(AppGlobalVars.USER_PIC)){
            Glide.with(WXLoginActivity.this).load(AppGlobalVars.USER_PIC).into(ivHead);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
            String headUrl = data.getStringExtra("headUrl");
            Log.d("","url:"+headUrl);
            Glide.with(WXLoginActivity.this).load(headUrl).into(ivHead);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
