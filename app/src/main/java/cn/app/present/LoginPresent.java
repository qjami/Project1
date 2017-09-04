package cn.app.present;

import lib.base.EPresent;
import lib.net.EHttp;
import lib.subscriber.ApiSubscriber;
import lib.subscriber.NetError;
import lib.utils.Empty;
import cn.app.model.LoginBean;
import cn.app.net.Api;
import cn.app.ui.LoginActivity;

/**
 * Created by Q
 * on 2017/8/14.
 */

public class LoginPresent extends EPresent<LoginActivity>{
    public void requestLogin(String code, String userID, String password,
                             String deviceID, String deviceToken, String format){
        Api.getEChargeHttpApi().login(code, userID, password, deviceID, deviceToken, format)
                .compose(EHttp.<LoginBean>getApiTransformer())
                .compose(EHttp.<LoginBean>getScheduler())
                .compose(EHttp.<LoginBean>getScheduler())
                .compose(getIView().<LoginBean>bindToLifecycle())
                .subscribe(new ApiSubscriber<LoginBean>(){

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (!Empty.check(getIView()))
                            getIView().loginResult(loginBean);
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (!Empty.check(getIView()))
                            getIView().loginError(error);
                    }
                });
    }
}
