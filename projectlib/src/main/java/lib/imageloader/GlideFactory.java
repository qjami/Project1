package lib.imageloader;

/**
 * Created by Q
 * on 2017/8/18.
 */

public class GlideFactory {
    private static GlideLoader loader;


    public static GlideLoader getLoader() {
        if (loader == null) {
            synchronized (GlideLoader.class) {
                if (loader == null) {
                    loader = new GlideLoader();
                }
            }
        }
        return loader;
    }
}
