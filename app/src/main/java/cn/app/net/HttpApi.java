package cn.app.net;

import cn.app.model.AppBean;
import cn.app.model.LoginBean;
import cn.app.model.ZHPreBean;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Q
 * on 2016/9/13.
 * 网络请求的接口都在这里
 */

public interface HttpApi {
    /**登录接口*/
    @FormUrlEncoded
    @POST("ECAPI_CHECK_DRIVER_PW_ANDROID")
    Flowable<LoginBean> login(@Field("code") String code,
                              @Field("userID") String userID,
                              @Field("password") String password,
                              @Field("deviceID") String deviceID,
                              @Field("deviceToken") String deviceToken,
                              @Field("format") String format);

    /**登录接口*/
    @FormUrlEncoded
    @POST("ECAPI_CHECK_APP_VERSION_QR_ANDORID")
    Flowable<AppBean> checkVersion(@Field("code") String code,
                                   @Field("versionNo") String versionNo,
                                   @Field("format") String format);

    /**获取用户套餐信息 非baseUrl，所以url需要传全路径*/
    @GET
    Flowable<ZHPreBean> userPackageOverview(@Url String url,
                                            @Query("userKey") String userKey,
                                            @Query("userId") String userId,
                                            @Query("format") String format,
                                            @Query("code") String code);

    /**获取用户套餐信息 如果是baseUrl的用法*/
    //@GET("api/user/package/overview?{userKey}&{userId}&{format}&{code}")
    //Flowable<ZHPreBean> userPackageOverview(@Query("userKey") String userKey,
                                            //@Query("userId") String userId,
                                            //@Query("format") String format,
                                            //@Query("code") String code);
}
