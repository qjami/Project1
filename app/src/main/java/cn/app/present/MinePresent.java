package cn.app.present;

import lib.base.EPresent;
import lib.net.EHttp;
import lib.subscriber.ApiSubscriber;
import lib.subscriber.NetError;
import lib.utils.Empty;
import cn.app.model.AppBean;
import cn.app.net.Api;
import cn.app.ui.fragment.MineFragment;

/**
 * Created by Q
 * on 2017/8/14.
 */

public class MinePresent extends EPresent<MineFragment>{

    public void requestCheckVersion(String code, String versionNo, String format){
        Api.getEChargeHttpApi().checkVersion(code, versionNo, format)
                .compose(EHttp.<AppBean>getApiTransformer())
                .compose(EHttp.<AppBean>getScheduler())
                .compose(getIView().<AppBean>bindToLifecycle())
                .subscribe(new ApiSubscriber<AppBean>(){

                    @Override
                    public void onNext(AppBean loginBean) {
                        if (!Empty.check(getIView()))
                            getIView().getCheckVersion(loginBean);
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (!Empty.check(getIView()))
                            getIView().loginError(error);
                    }
                });
    }
}
