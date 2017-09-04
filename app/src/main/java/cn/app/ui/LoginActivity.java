package cn.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.app.EApplication;
import cn.yhub.app.R;
import cn.app.config.Constant;
import cn.app.config.UrlConfig;
import cn.app.model.LoginBean;
import cn.app.model.LoginData;
import cn.app.present.LoginPresent;
import io.realm.Realm;
import lib.base.EActivity;
import lib.subscriber.NetError;
import lib.utils.Empty;

/**
 * Created by Q
 * on 2017/8/14.
 */

public class LoginActivity extends EActivity<LoginPresent> {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.sign_up_tv)
    TextView signUpTv;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_passwd)
    EditText etPasswd;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.check_remember)
    CheckBox checkRemember;
    @BindView(R.id.tv_remember_account)
    TextView tvRememberAccount;

    @BindView(R.id.btn_login)
    Button btnLogin;

    private Realm realm;

    @Override
    public void initData(Bundle savedInstanceState) {
        etUsername.setText("18566201554");
        etPasswd.setText("123456");
        String userID = getSharedPref().getString(Constant.STRING_USER_ID, "");
        String userKey = getSharedPref().getString(Constant.STRING_USER_KEY, "");
        //realm = Realm.getDefaultInstance();
        realm = EApplication.getRealm();
        /*if (!Empty.check(userID) && !Empty.check(userKey))
            goToPackageLIstActivity();*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresent newP() {
        return new LoginPresent();
    }


    @OnClick({R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String code = UrlConfig.mdcode;
                String userID = etUsername.getText().toString().trim();
                String password = etPasswd.getText().toString().trim();
                String deviceID = getPhoneID();
                String deviceToken = "cd3bd176cec31f89419090f37fec89b8df45ef4d";
                String format = "json";
                if (!Empty.check(getP())){
                    getP().requestLogin(code, userID, password, deviceID, deviceToken, format);
                }
                break;

        }
    }

    public void loginResult(LoginBean loginBean){
        if (null != loginBean){
            final LoginData loginData = loginBean.getResult();
            if (loginData != null && loginData.getCode() == 207) {
                // login successfully
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(loginData);//若该Model没有主键，使用copyToRealm方法
                    }
                });
                getToastor().showSingletonToast("登录成功！");
                String userID = loginData.getUserID();
                String userKey = loginData.getUserKey();
                getSharedPref().putString(Constant.STRING_USER_ID, userID);
                getSharedPref().putString(Constant.STRING_USER_KEY, userKey);
                //goToPackageLIstActivity(loginData);
                goToMainActivity();
            }else if (loginData != null
                    && loginData.getCode() == 301
                    || loginData != null
                    && loginData.getCode() == 210) {
                // 301=user not found
                // 210=incorrect pwd
                getToastor().showSingletonToast("账户密码不对");
            } else if (loginData != null) {
                getToastor().showSingletonToast(
                         "失败码:"
                        + loginData.getCode());
            }
        }
    }

    public void goToPackageLIstActivity(LoginData loginData){
        Intent intent = new Intent();
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(LoginActivity.this,
                PackageListActivity.class);
        if (null != loginData){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_NICK_NAME, loginData.getUserID());
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.right_enter,
                R.anim.zoom_exit);
        finish();
    }

    public void goToMainActivity(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_enter,
                R.anim.zoom_exit);
        finish();
    }

    public void loginError(NetError error){
        if (error != null) {
            Log.d("--------------error=", error.getType() + "|" + error.getMessage());
        }
    }

    @SuppressLint("HardwareIds")
    private String getPhoneID() {
        return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != realm)
            realm.close();
    }
}
