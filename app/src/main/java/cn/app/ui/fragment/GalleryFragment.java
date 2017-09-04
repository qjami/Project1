package cn.app.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import lib.base.EFragment;
import cn.yhub.app.R;

/**
 * Created by devel
 * on 2017/8/28.
 */

public class GalleryFragment extends EFragment {

    @BindView(R.id.tv_gallery)
    TextView tvImport;
    private TextView tv_content;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }
}
