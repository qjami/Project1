package cn.app.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import lib.base.EFragment;
import lib.subscriber.NetError;
import lib.utils.AppUtils;
import cn.yhub.app.R;
import cn.app.config.UrlConfig;
import cn.app.model.AppBean;
import cn.app.present.MinePresent;
import cn.app.utils.UpdateManager;
import cn.app.widget.TitleBar;

/**
 * Created by devel
 * on 2017/8/28.
 */

public class MineFragment extends EFragment<MinePresent> {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.ry_search)
    RelativeLayout rySearch;
    @BindView(R.id.record_loy)
    RelativeLayout recordLoy;
    @BindView(R.id.project_online)
    RelativeLayout projectOnline;
    @BindView(R.id.loginLoy)
    LinearLayout loginLoy;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.logout_btn)
    Button logoutBtn;

    @Override
    public void initData(Bundle savedInstanceState) {
        String code = UrlConfig.mdcode;
        String versionNo = AppUtils.getAppVersionCode() + "";
        String format = "json";
        getP().requestCheckVersion(code, versionNo, format);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public MinePresent newP() {
        return new MinePresent();
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @OnClick({R.id.ry_search, R.id.record_loy, R.id.project_online, R.id.login_btn, R.id
            .logout_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ry_search:
                break;
            case R.id.record_loy:
                break;
            case R.id.project_online:
                break;
            case R.id.login_btn:
                break;
            case R.id.logout_btn:
                break;
        }
    }


    public void getCheckVersion(AppBean appBean){
        if (null != appBean){
            if (appBean.getIsLatest() == 1) {// 是最新版本;无需更新
                getToastor().showSingletonToast("当前已是最新版本~");
                return;
            } else {
                String[] info =	appBean.getUptInfo().split("\n");
                ArrayList<String> content = new ArrayList<String>();
                for(int i = 0;i<info.length;i++)
                    content.add(info[i]);

                if (appBean.getForceUpdate() == 0) {// 不需要强制更新;用户可以选择
                    UpdateManager.isUpdate = true;
                    UpdateManager manager = new UpdateManager(getActivity(), content, appBean.getDownloadAddress(),false);
                    manager.checkUpdate("");
                } else {
                    UpdateManager.isUpdate = true;
                    UpdateManager manager = new UpdateManager(getActivity(), content, appBean.getDownloadAddress(),true);
                    manager.checkUpdate("");

                }
            }
        }
    }

    public void loginError(NetError error){
        if (error != null) {
            Log.d("--------------error=", error.getType() + "|" + error.getMessage());
        }
    }
}
