package lib.net;


import android.content.Context;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lib.mvp.IModel;
import lib.subscriber.NetError;
import lib.utils.Empty;
import lib.utils.NetWork;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Q
 * on 2016/9/14.
 */

public class EHttp {

    private static OkHttpClient client;
    private  static volatile Retrofit retrofit;
    private static EHttp instance;
    private static Context mContext;
    public static final long connectTimeoutMills = 10 * 1000L;
    public static final long readTimeoutMills = 10 * 1000L;
    public static final long writeTimeoutMills = 10 * 1000L;

    private EHttp() {

    }

    public static EHttp getInstance() {
        if (instance == null) {
            synchronized (EHttp.class) {
                if (instance == null) {
                    instance = new EHttp();
                }
            }
        }
        return instance;
    }

    public static void setmContext(Context mContext) {
        EHttp.mContext = mContext;
    }

    /**
     * 设置公共参数
     */
    private static Interceptor addQueryParameterInterceptor() {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("phoneSystem", "")
                        .addQueryParameter("phoneModel", "")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        return addQueryParameterInterceptor;
    }

    /**
     * 设置头
     * @param token
     * @return
     */
    private static Interceptor addHeaderInterceptor(final String token) {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        // Provide your custom header here
                        .header("token", Empty.check(token) ? "" : token)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置缓存
     * @return
     */
    private static Interceptor addCacheInterceptor() {
        Interceptor cacheInterceptor = null;
        if (mContext != null){
            cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (NetWork.getNetworkTypeName(mContext).equals(NetWork.NETWORK_TYPE_DISCONNECT)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (!NetWork.getNetworkTypeName(mContext).equals(NetWork.NETWORK_TYPE_DISCONNECT)) {
                        int maxAge = 0;
                        // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        // 无网络时，设置超时为1天  只对get有用,post没有缓冲
                        int maxStale = 60 * 60 * 24;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" +
                                        maxStale)
                                .removeHeader("nyn")
                                .build();
                    }
                    return response;
                }
            };
        }
        return cacheInterceptor;
    }

    /**
     * log拦截器
     * @return
     */
    public static Interceptor addHttpLoggingInterceptor(){
        //添加一个log拦截器,打印所有的log
        /*HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    StringReader reader = new StringReader(message);
                    Properties properties = new Properties();
                    properties.load(reader);
                    properties.list(System.out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;*/
        return new LoggingInterceptor();//自定义Log打印
    }

    /**
     * 设置环境路径和大小
     * @return
     */
    public static Cache addHttpCache(){
        Cache cache = null;
        if (null != mContext){
            //设置 请求的缓存的大小跟位置
            File cacheFile = new File(mContext.getCacheDir(), "cache");
            cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小
        }
        return cache;
    }

    /**
     * 线程切换
     *
     * @return
     */
    public static <T extends IModel> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 异常处理变换
     *
     * @return
     */
    public static <T extends IModel> FlowableTransformer<T, T> getApiTransformer() {

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T model) throws Exception {

                        if (model == null) {
                            return Flowable.error(new NetError("modle为空", NetError.NoDataError));
                        }else {
                            return Flowable.just(model);
                        }
                    }
                });
            }
        };
    }

    public static Retrofit getRetrofit(String baseUrl, String token, boolean cache) {
        if (retrofit == null) {
            synchronized (EHttp.class) {
                if (retrofit == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(connectTimeoutMills, TimeUnit.MILLISECONDS);
                    builder.readTimeout(readTimeoutMills, TimeUnit.MILLISECONDS);
                    builder.writeTimeout(writeTimeoutMills, TimeUnit.MILLISECONDS);
                    if (!Empty.check(token)){
                        builder.addInterceptor(addHeaderInterceptor(token));
                    }
                    if (cache){
                        builder.addInterceptor(addCacheInterceptor());
                    }
                    builder.addInterceptor(addHttpLoggingInterceptor());
                    client = builder.build();
                    // 获取retrofit的实例
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(baseUrl)
                            .client(client)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
