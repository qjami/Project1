package lib.base;

import lib.mvp.IPresenter;
import lib.mvp.IView;

/**
 * Created by Q
 * on 2017/8/14.
 */

public class EPresent<V extends IView> implements IPresenter<V>{
    private V v;
    @Override
    public void attachView(V view) {
        v = view;
    }

    @Override
    public void detachView() {
        v = null;
    }

    @Override
    public V getIView() {
        if (v == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v;
    }
}
