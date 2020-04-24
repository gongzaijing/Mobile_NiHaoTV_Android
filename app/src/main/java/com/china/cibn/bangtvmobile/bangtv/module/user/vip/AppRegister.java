package com.china.cibn.bangtvmobile.bangtv.module.user.vip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

		msgApi.registerApp(Config.APP_ID_WX);
	}
}
