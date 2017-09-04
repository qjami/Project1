package lib.mvp;

import android.os.Bundle;
import android.view.View;

/**
 *
 * @param <P>
 */
public interface IView<P> {
    void bindUI(View rootView);

    void initData(Bundle savedInstanceState);

    int getLayoutId();

    P newP();
}
