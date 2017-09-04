package cn.app.present;

import lib.base.EPresent;
import lib.net.EHttp;
import lib.subscriber.ApiSubscriber;
import lib.subscriber.NetError;
import lib.utils.Empty;
import cn.app.config.UrlConfig;
import cn.app.model.ZHPreBean;
import cn.app.net.Api;
import cn.app.ui.PackageListActivity;

/**
 * Created by liqing
 * on 2017/8/17.
 */

public class PackageListPresent extends EPresent<PackageListActivity>{
    public void requestUserPackageList(String userKey, String userId, String format,
                                       String code){
        String url = UrlConfig.MAPI + "api/user/package/overview?{userKey}&{userId}&{format}&{code}";
        Api.getEChargeHttpApi().userPackageOverview(url, userKey, userId, format, code)
                .compose(EHttp.<ZHPreBean>getApiTransformer())
                .compose(EHttp.<ZHPreBean>getScheduler())
                .compose(getIView().<ZHPreBean>bindToLifecycle())
                .subscribe(new ApiSubscriber<ZHPreBean>(){

                    @Override
                    public void onNext(ZHPreBean zhPreBean) {
                        if (!Empty.check(getIView()))
                            getIView().showUserPackageList(zhPreBean);
                    }

                    @Override
                    protected void onFail(NetError error) {
                        if (!Empty.check(getIView()))
                            getIView().loginError(error);
                    }
                });
    }
}
