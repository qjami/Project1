package cn.app.config;

/**
 * Created by Q
 * l on 2017/8/14.
 */

public class UrlConfig {
    public static int conditionFlag = 2;// 0-开发环境，1-线上环境,2-测试环境
    public static String API = getAPIUrl();
    public static String MAPI = getMonthlyAPIUrl();
    public static final String mdcode = getMDCODE();

    private static String getAPIUrl() {
        String url = "";
        if (conditionFlag == 0) {
            url = "http://test.e-chong.com:8080/ECAPI_CHINA/";

        } else if (conditionFlag == 1) {
            url = "https://m.e-chong.com/ECAPI_CHINA/";

        } else if (conditionFlag == 2) {
            url = "http://tm.e-chong.com:8080/ECAPI_CHINA/";
        }
        return url;
    }

    private static String getMonthlyAPIUrl() {
        String url = "";
        if (conditionFlag == 0) {
            url = "http://114.55.219.4:9090/";

        } else if (conditionFlag == 1) {
            url = "https://php.e-chong.com/";

        } else if (conditionFlag == 2) {
            url = "http://114.55.219.4:9090/";
        }
        return url;
    }

    private static String getMDCODE() {
        String url = "";
        if (conditionFlag == 0) {
            //url = "hl4tePuNdw+niMpEESYuPjfj6/8ZTetZSRbm/xVpXxUhcIdsTqtyMtDTOzHfpaLe5agjjDbjVg0u+DWKk1vdjZD6Sw==";// sz
            url = "ZbEeujatqVfThYnr4yRP0OQaTgu8LzDKZDtapFtJQTJSL4LRx9c6hqVcd10X6rlkRpx8OxlvcpJSgNhtRk2ykrST1Q==";// sz
        } else if (conditionFlag == 1 || conditionFlag == 2) {
            url = "ZbEeujatqVfThYnr4yRP0OQaTgu8LzDKZDtapFtJQTJSL4LRx9c6hqVcd10X6rlkRpx8OxlvcpJSgNhtRk2ykrST1Q==";
        }
        return url;
    }
}
