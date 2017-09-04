package cn.app.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lib.base.EActivity;
import lib.subscriber.NetError;
import lib.utils.Empty;
import cn.app.EApplication;
import cn.yhub.app.R;
import cn.app.config.Constant;
import cn.app.config.UrlConfig;
import cn.app.model.LoginData;
import cn.app.model.ZHBean;
import cn.app.model.ZHPackageBean;
import cn.app.model.ZHPreBean;
import cn.app.present.PackageListPresent;
import cn.app.utils.DensityUtils;
import cn.app.viewholder.PackagesViewHolder;
import cn.app.widget.TitleBar;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by li.qing
 * on 2017/8/17.
 * 用户套餐列表页面
 */

public class PackageListActivity extends EActivity<PackageListPresent>
        implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<ZHPackageBean> adapter;

    private Handler handler = new Handler();
    private TextView zhyeTv, yfyeTv, nicknameTV, zhbzTv;
    RelativeLayout rlPackageMy;//我的套餐
    RelativeLayout rlPackageRenew;//我的套餐

    private boolean hasMonthlyPackage;
    private Realm realm;

    @Override
    public void initData(Bundle savedInstanceState) {
        titleBar.getCenterTx().setText("订单列表");
        titleBar.getRightTx().setText("充电记录");
        realm = EApplication.getRealm();
        //realm = Realm.getDefaultInstance();
        RealmResults<LoginData> userList = realm.where(LoginData.class).findAll();
        initAdapter();

    }

    private void initExtras(){
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String nickname = bundle.getString("nickname");
                nicknameTV.setText(nickname);
            }
        }

    }

    private void initAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY,
                DensityUtils.dp2px(this,2), 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<ZHPackageBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PackagesViewHolder(parent);
            }
        });
        //adapter.setMore(R.layout.view_more, this);
        //adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.remove(position);
                return true;
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        recyclerView.setRefreshListener(this);
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return getHeadView();
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        onRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_history;
    }

    @Override
    public PackageListPresent newP() {
        return new PackageListPresent();
    }

    public void showUserPackageList(ZHPreBean zhPreBean){
        if (null != zhPreBean){
            if (zhPreBean.getCode() == 203) {
                showData(zhPreBean.getData());
            }
        }
    }

    public void loginError(NetError error){
        if (error != null) {
            Log.d("--------------error=", error.getType() + "|" + error.getMessage());
        }
    }

    private void showData(ZHBean bean) {
        if (bean != null && adapter != null) {
            hasMonthlyPackage = bean.getHasMonthlyPackage();
            if (hasMonthlyPackage) {
                //如果有月费套餐
                rlPackageMy.setVisibility(View.VISIBLE);
                rlPackageRenew.setVisibility(View.VISIBLE);
            } else {
                rlPackageMy.setVisibility(View.GONE);
                rlPackageRenew.setVisibility(View.GONE);
            }
            if (zhyeTv != null && yfyeTv != null) {
                String point = bean.getUnUsePoint() + "";
                String str = getString(R.string.zh_my_logo) + ": " + point + getString(R.string.zh_point);
                int fstart = str.indexOf(point);
                int fend= fstart + point.length();
                SpannableStringBuilder style=new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_1))
                        ,fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                zhyeTv.setText(style);
                String power = bean.getUnUsePower() + "";
                str = getString(R.string.zh_my_yf) + ": " + power + getString(R.string.zh_power);
                fstart = str.indexOf(power);
                fend= fstart + power.length();
                style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_1))
                        ,fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                yfyeTv.setText(style);
            }

            List<ZHPackageBean> mPackList = new ArrayList<>();
            if (bean.getCompany() != null && bean.getCompany().size() > 0) {
                //添加适配器
                for (ZHPackageBean mPackBean: bean.getCompany()) {
                    if (null != mPackBean && !TextUtils.isEmpty(mPackBean.getPackageID())
                            && mPackBean.getPackageID().contains(Constant.MONTHLY_TYPE))//不展示月费套餐
                        continue;
                    mPackList.add(mPackBean);
                }
            }
            adapter.clear();
            adapter.addAll(mPackList);
        }
    }

    /**
     * 获取头部View
     * @return
     */
    private View getHeadView(){
        LayoutInflater lif = (LayoutInflater) this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.item_package_monthly, null);
        yfyeTv = (TextView) headerView.findViewById(R.id.tv_yfye);
        zhyeTv = (TextView) headerView.findViewById(R.id.tv_zhye);
        nicknameTV = (TextView) headerView.findViewById(R.id.tv_nickname);
        rlPackageMy = (RelativeLayout) headerView.findViewById(R.id.ipm_my);
        rlPackageMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击月费套餐
                        /*MobclickAgent.onEvent(PackageListActivity.this, "yuefeitaocan");
                        if (hasMonthlyPackage) {
                            Intent i = new Intent(PackageListActivity.this, MyPackageActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.zoom_exit);
                        } else {
                            showShortToast(getString(R.string.m_no_package));
                        }*/
            }});

        rlPackageRenew = (RelativeLayout) headerView.findViewById(R.id.ipm_renew);
        rlPackageRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击月费续费
                        /*MobclickAgent.onEvent(PackageListActivity.this, "xufei");
                        Intent i = new Intent(PackageListActivity.this, HybridPaymentActivity.class);
                        if (hasMonthlyPackage) {
                            //续费
                            i.putExtra("type", "renew");
                        } else {
                            //充值
                            i.putExtra("type", "order");
                        }
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.zoom_exit);*/
            }
        });

        ImageView helpImv = (ImageView) headerView.findViewById(R.id.question);
        helpImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击月费续费帮助
                        /*MobclickAgent.onEvent(PackageListActivity.this, "xufeibangzhu");
                        showDialogWithResource(R.layout.dialog_yf_xf);*/
            }
        });
        return headerView;
    }

    @Override
    public void onRefresh() {
        String userID = getSharedPref().getString(Constant.STRING_USER_ID, "");
        String userKey = getSharedPref().getString(Constant.STRING_USER_KEY, "");
        String format = "json";
        String code = UrlConfig.mdcode;
        if (!Empty.check(userID) && !Empty.check(userKey))
            getP().requestUserPackageList(userKey, userID, format, code);
    }


    @Override
    public void onMoreShow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getToastor().showSingletonToast("加载完成~");
            }
        }, 2000);
    }

    @Override
    public void onMoreClick() {
        getToastor().showSingletonToast("onMoreClick");
    }
}
