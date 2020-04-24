package com.china.cibn.bangtvmobile.wxapi;

/**
 * Created by Administrator on 2018/4/3.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.VipActivity;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 *
 * @author gzj
 * @date 2018年5月16日16:20:44
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


        private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

        private IWXAPI api;

        private AlertDialog.Builder mDialog;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setContentView(R.layout.pay_result);

            api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX);
            api.handleIntent(getIntent(), this);
        }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            setIntent(intent);
            api.handleIntent(intent, this);
        }

        @Override
        public void onReq(BaseReq req) {
        }

        @SuppressLint("StringFormatInvalid")
        @Override
        public void onResp(BaseResp resp) {
//            Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                if(resp.errCode==-2){
                    ToastUtil.LongToast(getString(R.string.vipactivity_novip_text));
                    finish();
                }else {
                    getDialog();
                }
            }

    }
    private void getDialog(){
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(getString(R.string.vipactivity_okvip_text));
        mDialog.setNegativeButton(getString(R.string.vipactivity_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(VipActivity.instance!=null){
                    VipActivity.instance.finish();
                }
                finish();
            }
        }).setCancelable(false).create().show();
    }
}