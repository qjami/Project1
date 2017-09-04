package lib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.mvp.IPresenter;
import lib.mvp.IView;
import lib.utils.Toastor;

/**
 * Created by devel on 2017/8/11.
 */

public abstract class EFragment<P extends IPresenter> extends RxFragment implements IView<P>{

    private P p;
    protected Activity context;
    private View rootView;
    protected LayoutInflater layoutInflater;
    private Toastor toastor;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        layoutInflater = inflater;
        if (rootView == null && getLayoutId() > 0) {
            rootView = inflater.inflate(getLayoutId(), null);
            bindUI(rootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    protected Toastor getToastor(){
        if (toastor == null){
            toastor = new Toastor(getActivity());
        }
        return toastor;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getP() != null) {
            getP().detachView();
        }
        p = null;
    }
}
