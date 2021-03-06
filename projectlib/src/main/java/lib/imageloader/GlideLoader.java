package lib.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by Q
 * on 2017/8/18.
 * 图片加载工具类
 */

public class GlideLoader {
    //默认加载
    public void loadImageView(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).into(mImageView);
    }

    //加载指定大小
    public void loadImageViewSize(String path, int width, int height, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public void loadImageViewLoding(String path, ImageView mImageView, int
            lodingImage, int errorImageView) {
        Glide.with(mImageView.getContext()).load(path).placeholder(lodingImage).error
                (errorImageView).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public void loadImageViewLodingSize(String path, int width, int height,
                                        ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mImageView.getContext()).load(path).override(width, height).placeholder
                (lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public void loadImageViewCache(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public void loadImageViewPriority(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public void loadImageViewDiskCache(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public void loadImageViewAnim(String path, int anim, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public void loadImageViewThumbnail(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public void loadImageViewCrop(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).centerCrop().into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(String path, ImageView
            mImageView) {
        Glide.with(mImageView.getContext()).load(path).asGif().into(mImageView);
    }

    //设置静态GIF加载方式
    public void loadImageViewStaticGif(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).asBitmap().into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public static void loadImageViewListener(String path, ImageView mImageView,
                                             RequestListener<String, GlideDrawable>
                                                     requstlistener) {
        Glide.with(mImageView.getContext()).load(path).listener(requstlistener).into(mImageView);
    }


    //清理磁盘缓存
    public void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

}
