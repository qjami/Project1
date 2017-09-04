package lib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.cache.SharedPrefHelper;
import lib.mvp.IPresenter;
import lib.mvp.IView;
import lib.utils.Toastor;

/**
 * Created by devel
 * on 2017/8/11.
 */

public abstract class EActivity<P extends IPresenter> extends RxAppCompatActivity implements IView<P>{
    private P p;
    private Unbinder unbinder;
    protected Activity context;
    private Toastor toastor;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
        }
        initData(savedInstanceState);
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachView(this);
            }
        }
        return p;
    }

    protected SharedPrefHelper getSharedPref(){
        if (sharedPrefHelper == null){
            sharedPrefHelper = new SharedPrefHelper(context);
        }
        return sharedPrefHelper;
    }

    protected Toastor getToastor(){
        if (toastor == null){
            toastor = new Toastor(context);
        }
        return toastor;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getP() != null) {
            getP().detachView();
        }
        p = null;
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = ButterKnife.bind(this);
    }

}
