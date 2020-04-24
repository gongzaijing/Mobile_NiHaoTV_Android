package com.china.cibn.bangtvmobile.bangtv.module.user.maillogin;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.dal.UserHelper;
import com.china.cibn.bangtvmobile.bangtv.entity.LoginReturnInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.common.BangTVMainActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.reg.RegMailActivity;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.utils.CommonUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleProgressView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/7 14:12
 *
 * <p/>
 * 登录界面
 */
public class LoginActivity extends RxBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;


  @BindView(R.id.delete_username)
  ImageView mDeleteUserName;

  @BindView(R.id.et_username)
  EditText et_username;

  @BindView(R.id.et_password)
  EditText et_password;
  @BindView(R.id.iv_wx_head)
  ImageView mWxIamge;

  @BindView(R.id.tv_reg)
  TextView tvReg;

    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;

  private String name;

  private String password;

 private LoginReturnInfo loginReturnInfo=new LoginReturnInfo();

    private ProgressDialog pd;

    private LocalData localData;

    private UserHelper userHelper;

  /**
   * 微信登录相关
   */
  private IWXAPI api;
  @Override
  public int getLayoutId() {

    return R.layout.activity_login_bangtv;
  }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }


    @Override
  public void initViews(Bundle savedInstanceState) {
      initDB();
      et_username.setOnFocusChangeListener((v, hasFocus) -> {

      if (hasFocus && et_username.getText().length() > 0) {
        mDeleteUserName.setVisibility(View.VISIBLE);
      } else {
        mDeleteUserName.setVisibility(View.GONE);
      }

    });

    et_password.setOnFocusChangeListener((v, hasFocus) -> {
    });

    et_username.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 如果用户名清空了 清空密码 清空记住密码选项
        et_password.setText("");
        if (s.length() > 0) {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteUserName.setVisibility(View.VISIBLE);
        } else {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteUserName.setVisibility(View.GONE);
        }

      }


      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


      @Override
      public void afterTextChanged(Editable s) {}
    });
    tvReg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegMailActivity.class));
        finish();
      }
    });
  }


  @Override
  public void initToolBar() {
    mToolbar.setNavigationIcon(R.drawable.action_button_back_pressed_light);
    mToolbar.setTitle("");
    mToolbar.setNavigationOnClickListener(v -> finish());
  }

  @OnClick(R.id.btn_login)
  void startLogin() {

    boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
    if (!isNetConnected) {
      ToastUtil.ShortToast(getString(R.string.LoginActivity_Net));
      return;
    }
    login();
  }
@OnClick(R.id.iv_wx_head)
void startwxlogin(){
  //通过WXAPIFactory工厂获取IWXApI的示例
  api = WXAPIFactory.createWXAPI(getApplicationContext(), Config.APP_ID_WX,true);
  //将应用的appid注册到微信
  api.registerApp(Config.APP_ID_WX);
  if(BangtvApp.isWeChatAppInstalled(getApplicationContext())){
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
    req.state = "wechat_sdk_微信登录";
    api.sendReq(req);
    LoginActivity.this.finish();
  }else{
    Toast.makeText(getApplicationContext(),getString(R.string.LoginActivity_WX),Toast.LENGTH_SHORT).show();
  }
}


  @OnClick(R.id.delete_username)
  void delete() {
    // 清空用户名以及密码
    et_username.setText("");
    et_password.setText("");
    mDeleteUserName.setVisibility(View.GONE);
    et_username.setFocusable(true);
    et_username.setFocusableInTouchMode(true);
    et_username.requestFocus();
  }

    private void initDB() {
        localData = new LocalData(this);
        userHelper = new UserHelper(this);
    }
  private void login() {

   name = et_username.getText().toString();
    password = et_password.getText().toString();

    if (TextUtils.isEmpty(name)) {
      ToastUtil.ShortToast(getString(R.string.LoginActivity_UserName));
      return;
    }

    if (TextUtils.isEmpty(password)) {
      ToastUtil.ShortToast(getString(R.string.LoginActivity_PassWord));
      return;
    }
    startVipEmLogin();

  }
  private void startVipEmLogin(){
      pd = ProgressDialog.show(LoginActivity.this, "", "正在登录，请稍后……");
      new Handler(new Handler.Callback() {
          @Override
          public boolean handleMessage(Message arg0) {
              if(pd.isShowing()){
                  ToastUtil.ShortToast(getString(R.string.LoginActivity_LoginFial));
                  pd.dismiss();
              }
              return false;
          }
      }).sendEmptyMessageDelayed(0, 20000);
      HashMap<String, String> postData = new HashMap<String, String>();
    postData.put("username",name );
    postData.put("password", password);
    RetrofitHelper.getNoCacheAppAPI().startVipEmLogin(BangtvApp.versionCodeUrl,postData)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
                pd.dismiss();
                if(resultBeans==null){return;}
                if(resultBeans.getCode().equals("0")){
                    ToastUtil.ShortToast(resultBeans.getMsg());
                    return;}

                loginReturnInfo=resultBeans;
                LoginEmSave();
            }, throwable -> {
                pd.dismiss();
                ToastUtil.ShortToast(getString(R.string.LoginActivity_Loadfail));
            });
  }
  private void LoginEmSave(){
    String userID = "", userName = "", wxID = "", userPicUrl = "", userToken = "";
    try {
      userID = loginReturnInfo.getData().getUser_id();
        userToken =loginReturnInfo.getData().getToken();
        userName = loginReturnInfo.getData().getUser_name();
        userPicUrl = loginReturnInfo.getData().getPic();

        ToastUtil.ShortToast(getString(R.string.LoginActivity_LoginSucc));
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

      startActivity(new Intent(LoginActivity.this, BangTVMainActivity.class));
      finish();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  @Override
  protected void onResume() {
    super.onResume();

  }



}
