package com.china.cibn.bangtvmobile.bangtv.module.user.reg;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.dal.UserHelper;
import com.china.cibn.bangtvmobile.bangtv.entity.RegisterReturnInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.user.maillogin.LoginActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.ProtocolDialog;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.CommonUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.MD5FileUtil;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/7 14:12
 *
 * <p/>
 * 注册界面
 */
public class RegMailActivity extends RxBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.delete_useremail)
  ImageView mDeleteUserMail;

//  @BindView(R.id.delete_username)
//  ImageView mDeleteUserName;

  @BindView(R.id.delete_password)
  ImageView mDeletePassword;

  @BindView(R.id.delete_ispassword)
  ImageView mDeleteIspassword;

  @BindView(R.id.et_useremail)
  EditText et_useremail;

//  @BindView(R.id.et_username)
//  EditText et_username;

  @BindView(R.id.et_password)
  EditText et_password;

  @BindView(R.id.et_ispassword)
  EditText et_ispassword;

  @BindView(R.id.tv_login)
  TextView tvLogin;

  @BindView(R.id.tv_xieyi)
  TextView mTvXieyi;

  @BindView(R.id.checkxieyi)
  CheckBox mCheckBox;



  private String email;

  private String name;

  private String password;

  private String ispassword;

  private RegisterReturnInfo dataBeanList = new RegisterReturnInfo();

  private UserHelper userHelper;

  private LocalData localData;

  private ProgressDialog pd;

  private boolean isCheck=true;

  @Override
  public int getLayoutId() {

    return R.layout.activity_reg_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @Override
  public void initViews(Bundle savedInstanceState) {
    et_useremail.setOnFocusChangeListener((v, hasFocus) -> {

      if (hasFocus && et_useremail.getText().length() > 0) {
        mDeleteUserMail.setVisibility(View.VISIBLE);
      } else {
        mDeleteUserMail.setVisibility(View.GONE);
      }

    });

//    et_username.setOnFocusChangeListener((v, hasFocus) -> {
//
//      if (hasFocus && et_username.getText().length() > 0) {
//        mDeleteUserName.setVisibility(View.VISIBLE);
//      } else {
//        mDeleteUserName.setVisibility(View.GONE);
//      }
//
//    });

    et_password.setOnFocusChangeListener((v, hasFocus) -> {
      if (hasFocus && et_password.getText().length() > 0) {
        mDeletePassword.setVisibility(View.VISIBLE);
      } else {
        mDeletePassword.setVisibility(View.GONE);
      }

    });
    et_ispassword.setOnFocusChangeListener((v, hasFocus) -> {
      if (hasFocus && et_ispassword.getText().length() > 0) {
        mDeleteIspassword.setVisibility(View.VISIBLE);
      } else {
        mDeleteIspassword.setVisibility(View.GONE);
      }

    });
    et_useremail.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 如果用户名清空了 清空密码 清空记住密码选项
        if (s.length() > 0) {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteUserMail.setVisibility(View.VISIBLE);
        } else {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteUserMail.setVisibility(View.GONE);
        }

      }


      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


      @Override
      public void afterTextChanged(Editable s) {}
    });
//    et_username.addTextChangedListener(new TextWatcher() {
//
//      @Override
//      public void onTextChanged(CharSequence s, int start, int before, int count) {
//        // 如果用户名清空了 清空密码 清空记住密码选项
//        if (s.length() > 0) {
//          // 如果用户名有内容时候 显示删除按钮
//          mDeleteUserName.setVisibility(View.VISIBLE);
//        } else {
//          // 如果用户名有内容时候 显示删除按钮
//          mDeleteUserName.setVisibility(View.GONE);
//        }
//
//      }
//
//
//      @Override
//      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//
//      @Override
//      public void afterTextChanged(Editable s) {}
//    });
    et_password.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 如果用户名清空了 清空密码 清空记住密码选项
        if (s.length() > 0) {
          // 如果用户名有内容时候 显示删除按钮
          mDeletePassword.setVisibility(View.VISIBLE);
        } else {
          // 如果用户名有内容时候 显示删除按钮
          mDeletePassword.setVisibility(View.GONE);
        }

      }


      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


      @Override
      public void afterTextChanged(Editable s) {}
    });
    et_ispassword.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 如果用户名清空了 清空密码 清空记住密码选项
        if (s.length() > 0) {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteIspassword.setVisibility(View.VISIBLE);
        } else {
          // 如果用户名有内容时候 显示删除按钮
          mDeleteIspassword.setVisibility(View.GONE);
        }

      }


      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


      @Override
      public void afterTextChanged(Editable s) {}
    });

    tvLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(RegMailActivity.this,LoginActivity.class));
        finish();
      }
    });


    mCheckBox.setOnCheckedChangeListener(new BootBallOnCheckedChangeListener());

    mTvXieyi.setOnClickListener(v-> ProtocolDialog.launch(RegMailActivity.this));
    initDB();
  }

  @Override
  public void initToolBar() {

    mToolbar.setNavigationIcon(R.drawable.action_button_back_pressed_light);
    mToolbar.setTitle("");
    mToolbar.setNavigationOnClickListener(v -> finish());
  }


  @OnClick(R.id.btn_login)
  void startLogin() {

    if(isCheck){
      boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
      if (!isNetConnected) {
        ToastUtil.ShortToast(getString(R.string.RegMailActivity_Net));
        return;
      }
      login();
    }else{
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_UserProtocol));
    }
  }

  @OnClick(R.id.delete_useremail)
  void setmDeleteUserMail() {
    // 清空用户名以及密码
    et_useremail.setText("");
    mDeleteUserMail.setVisibility(View.GONE);
    et_useremail.setFocusable(true);
    et_useremail.setFocusableInTouchMode(true);
    et_useremail.requestFocus();
  }
//  @OnClick(R.id.delete_username)
//  void delete() {
//    // 清空用户名以及密码
//    et_username.setText("");
//    mDeleteUserName.setVisibility(View.GONE);
//    et_username.setFocusable(true);
//    et_username.setFocusableInTouchMode(true);
//    et_username.requestFocus();
//  }

  @OnClick(R.id.delete_password)
  void setmDeletePassword() {
    // 清空用户名以及密码
    et_password.setText("");
    mDeletePassword.setVisibility(View.GONE);
    et_password.setFocusable(true);
    et_password.setFocusableInTouchMode(true);
    et_password.requestFocus();
  }  @OnClick(R.id.delete_ispassword)
  void setmDeleteIspassword() {
    // 清空用户名以及密码
    et_ispassword.setText("");
    mDeleteIspassword.setVisibility(View.GONE);
    et_ispassword.setFocusable(true);
    et_ispassword.setFocusableInTouchMode(true);
    et_ispassword.requestFocus();
  }
  private void login() {
    email = et_useremail.getText().toString();
    name = et_useremail.getText().toString();
    //name = et_username.getText().toString();
    password = et_password.getText().toString();
    ispassword = et_ispassword.getText().toString();


    if (TextUtils.isEmpty(email)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_Mail));
      return;
    }

//    if (TextUtils.isEmpty(name)) {
//      ToastUtil.ShortToast("用户名不能为空");
//      return;
//    }

    if (TextUtils.isEmpty(password)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_PassWord));
      return;
    }
    if (TextUtils.isEmpty(ispassword)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_Corfim));
      return;
    }

    if (!isEmail(email)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_FormatWrong));
      return;
    }
    if (!isPass(password)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_PassWordWrong));
      return;
    }
    if (password.length() < 8) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_PassWordAtLeastEigth));
      return;

    }
    if (!password.equals(ispassword)) {
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_PassWordWrongAgain));
      return;
    }

    getVipRegLoad();
//    finish();
  }
  private void initDB() {
    localData = new LocalData(this);
    userHelper = new UserHelper(this);
  }
  private void getVipRegLoad(){
    pd = ProgressDialog.show(RegMailActivity.this, "", "注册中，请稍后……");
    new Handler(new Handler.Callback() {
      @Override
      public boolean handleMessage(Message arg0) {
        if(pd.isShowing()){
          ToastUtil.ShortToast(getString(R.string.RegMailActivity_LoginFial));
          pd.dismiss();
        }
        return false;
      }
    }).sendEmptyMessageDelayed(0, 20000);
    String code = email + BangtvApp.secretkey;
    String key = MD5FileUtil.getMD5String(code);
    HashMap<String, String> postData = new HashMap<String, String>();
    postData.put("email", email);
    postData.put("username", name);
    postData.put("password1", password);
    postData.put("key", key);
    postData.put("sex", "1");
    RetrofitHelper.getNoCacheAppAPI().startVipEMRegister(BangtvApp.versionCodeUrl,postData)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
              .subscribe(resultBeans -> {
                pd.dismiss();
                if(resultBeans.getCode().equals("0")){
                  ToastUtil.ShortToast(resultBeans.getMsg());
                  return;
                }
                dataBeanList=resultBeans;
                regEmsave();
              }, throwable -> {
                pd.dismiss();
                ToastUtil.ShortToast(getString(R.string.RegMailActivity_Loadfail));
              });
  }

  public void regEmsave() {
    String userID = "", userName = "", wxID = "", userPicUrl = "", userToken = "";
    try {
      userID = dataBeanList.getData().getUser_id()+"";
      userName = dataBeanList.getData().getUser_name();
      userPicUrl = dataBeanList.getData().getPic();
      userToken =dataBeanList.getData().getToken();
      BangtvApp.getShared().putString("mUserNickName",userName);
      BangtvApp.getShared().putString("mToken",userToken);
      BangtvApp.getShared().putString("mUserPic",userPicUrl);
      BangtvApp.getShared().putString("mUserID",userID);
      ToastUtil.ShortToast(getString(R.string.RegMailActivity_RegSucc));
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
      finish();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
    //邮箱判断正则表达式
  private boolean isEmail(String email) {
    Pattern pattern = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    Matcher mc = pattern.matcher(email);
    return mc.matches();
  }

  //判断密码格式是否正确
  public boolean isPass(String pass) {
    String str = "^([A-Za-z]|[0-9])+$";
    Pattern p = Pattern.compile(str);
    Matcher m = p.matcher(pass);

    return m.matches();
  }

  @Override
  protected void onResume() {
    super.onResume();

  }

  private class BootBallOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked){

      if(isChecked){
        isCheck=isChecked;
//check true do nothing

      }
      else{
        isCheck=isChecked;
      }
    }
  }


}
