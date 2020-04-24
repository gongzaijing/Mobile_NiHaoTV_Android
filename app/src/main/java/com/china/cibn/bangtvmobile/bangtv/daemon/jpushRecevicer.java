package com.china.cibn.bangtvmobile.bangtv.daemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.facebook.stetho.common.LogUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * fix by gz 2018/5/10.
 *
 */


public class jpushRecevicer extends BroadcastReceiver {
    public static String TAG = "MyJPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyJPushReceiver] 接收Registration Id : " + regId);
            if (null != regId) {
                BangtvApp.jid = regId;
            }
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
            Toast.makeText(context,bundle.getString(JPushInterface.EXTRA_EXTRA),Toast.LENGTH_SHORT).show();

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyJPushReceiver] 用户点击打开了通知");

//        	//打开自定义的Activity
//        	Intent i = new Intent(context, TestActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyJPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyJPushReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyJPushReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (null != bundle.getString(JPushInterface.EXTRA_TITLE)
                && "login".equalsIgnoreCase(bundle.getString(JPushInterface.EXTRA_TITLE))) {
            _sendLocalMsg(context, AppGlobalConsts.LOCAL_MSG_FILTER.USER_BIND, bundle.getString(JPushInterface.EXTRA_EXTRA), false);
        }

        if (null != bundle.getString(JPushInterface.EXTRA_TITLE)
                && "pay".equalsIgnoreCase(bundle.getString(JPushInterface.EXTRA_TITLE))) {
            _sendLocalMsg(context, AppGlobalConsts.LOCAL_MSG_FILTER.REPLAYPAY_SUCCESS, bundle.getString(JPushInterface.EXTRA_EXTRA), false);
        }

        if (null !=  bundle.getString(JPushInterface.EXTRA_MESSAGE)) {
            _sendLocalMsg(context, AppGlobalConsts.LOCAL_MSG_FILTER.USER_BIND, bundle.getString(JPushInterface.EXTRA_EXTRA), false);
        }

        if (null !=  bundle.getString(JPushInterface.EXTRA_MESSAGE)) {
            _sendLocalMsg(context, AppGlobalConsts.LOCAL_MSG_FILTER.NOTICE_DISPLAY, bundle.getString(JPushInterface.EXTRA_EXTRA), true);
        }




//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }

    /**
     * 发送到
     * @param context
     * @param msgFilter
     * @param text
     * @param isSticky
     */
    private void _sendLocalMsg(Context context, AppGlobalConsts.LOCAL_MSG_FILTER msgFilter, String text, boolean isSticky) {
        LogUtil.i(TAG, "_sendLocalMsg::msgFilter=" + msgFilter.name() + " | text::" + text);
        Intent intent = new Intent();
        intent.setAction(msgFilter.toString());
        intent.putExtra(AppGlobalConsts.INTENT_MSG_PARAM, text);
        if (isSticky)
            context.sendStickyBroadcast(intent);
        else
            context.sendBroadcast(intent);
    }
}
