package cn.app.net;

import lib.net.EHttp;
import cn.app.config.UrlConfig;

/**
 * Created by Q
 * on 2017/8/14.
 */

public class Api {
    private static HttpApi httpApi;
    public static HttpApi getEChargeHttpApi() {
        if (httpApi == null) {
            synchronized (Api.class) {
                if (httpApi == null) {
                    httpApi = EHttp.getInstance().getRetrofit(UrlConfig.API, null, false).create(HttpApi.class);
                }
            }
        }
        return httpApi;
    }
}
