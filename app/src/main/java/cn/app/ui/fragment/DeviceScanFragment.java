package cn.app.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import lib.base.EFragment;
import cn.yhub.app.R;
import cn.app.model.BluetoothDeviceBean;
import cn.app.viewholder.BleDevicesViewHolder;

/**
 * Created by li.qing
 * on 2017/8/28.
 */
@SuppressLint("NewApi")
public class DeviceScanFragment extends EFragment{


    /*@BindView(R.id.titleBar)
    TitleBar titleBar;*/
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.pro_loading)
    ProgressBar proLoading;
    @BindView(R.id.tv_stop)
    TextView tvStop;
    //TextView tvRight;
    MenuItem item;

    private BluetoothAdapter mBluetoothAdapter;
    private RecyclerArrayAdapter<BluetoothDeviceBean> adapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_ENABLE_BT2 = 2;
    private static final long SCAN_PERIOD = 10000;
    private static boolean ble_flag = false;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    public void initData(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (!getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported,
                    Toast.LENGTH_SHORT).show();
            // getActivity().finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity()
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(),
                    R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT)
                    .show();
            // finish();
            return;
        }

        //tvRight = titleBar.getRightTx();
        Spannable span = new SpannableString(getString(R.string.h6_scan));
        span.setSpan(new AbsoluteSizeSpan(20, true), 0, 4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(10, true), 4, 11,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvStop.setText(span);
        proLoading.setVisibility(View.VISIBLE);
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                ble_flag = true;
            }
        }
        initToolbar("遥控设备");
        initAdapter();
        ble_flag = false;

    }

    public Toolbar initToolbar(CharSequence title) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) mAppCompatActivity.findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) mAppCompatActivity.findViewById(R.id.drawer_layout);
        mAppCompatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(itemClickListener);
        /*ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }*/

        return toolbar;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_device_scan_menu, menu);
        item = menu.findItem(R.id.menu_scan);
        super.onCreateOptionsMenu(menu, inflater);
        scanLeDevice(true);
    }

    private void initAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        /*DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY,
                DensityUtils.dp2px(getActivity(),2), 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);*/

        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<BluetoothDeviceBean>(context) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BleDevicesViewHolder(parent);
            }
        });
        //adapter.setMore(R.layout.view_more, this);
        //adapter.setNoMore(R.layout.view_nomore);
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
        //recyclerView.setRefreshListener(this);
        //onRefresh();
    }

    private void scanLeDevice(final boolean enable) {
        if (!mBluetoothAdapter.isEnabled() && enable && !ble_flag) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT2);
        }
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //scan_tv.setVisibility(View.VISIBLE);
                    //stop_tv.setVisibility(View.INVISIBLE);
                    item.setTitle("重新扫描");
                    proLoading.setVisibility(View.GONE);
                    tvStop.setVisibility(View.GONE);
                    //setBeanForAdapter(bleList);
                }
            }, SCAN_PERIOD);
            item.setTitle("停止扫描");
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             final byte[] scanRecord) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null && device.getName() != null) {
                        if (device.getName().length() == 8
                                || (device.getName().length() == 12 && device
                                .getName().contains("CN"))) {
                            BluetoothDeviceBean bean = new BluetoothDeviceBean();
                            bean.setBluetoothDevice(device);
                            bean.setScanRecord(scanRecord);
                            bean.setDeviceInfo("");
                            /*List<BluetoothDeviceBean> list = adapter.getAllData();
                            if (list != null && list.size() > 0){
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getBluetoothDevice().getName()
                                            .equals(device.getName())){

                                    }
                                }
                            }*/
                            if (!deviceList.contains(device) && null != adapter
                                    && !adapter.getAllData().contains(bean)){
                                deviceList.add(device);
                                adapter.add(bean);
                                adapter.notifyDataSetChanged();
                            }
                            //mLeDeviceListAdapter.addDevice(bean);
                            //mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device_scan;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static DeviceScanFragment newInstance() {
        return new DeviceScanFragment();
    }

    @OnClick({ R.id.tv_stop, R.id.custom_title_rightTx})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_stop:
                tvStop.setVisibility(View.GONE);
                proLoading.setVisibility(View.GONE);
                item.setTitle("重新扫描");
                scanLeDevice(false);
                break;
            case R.id.custom_title_rightTx:
                /*if (tvRight.getText().toString().trim().equals("重新扫描")){
                    tvRight.setText("停止扫描");
                    proLoading.setVisibility(View.INVISIBLE);
                    tvStop.setVisibility(View.INVISIBLE);
                    if (deviceList != null)
                        deviceList.clear();
                    scanLeDevice(true);
                }else {
                    tvRight.setText("重新扫描");
                    proLoading.setVisibility(View.VISIBLE);
                    tvStop.setVisibility(View.VISIBLE);
                    adapter.clear();
                    scanLeDevice(false);
                }*/
                break;
        }
    }

    Toolbar.OnMenuItemClickListener itemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_scan:
                    if (item.getTitle().toString().trim().equals("重新扫描")){
                        item.setTitle("停止扫描");
                        proLoading.setVisibility(View.VISIBLE);
                        tvStop.setVisibility(View.VISIBLE);
                        if (deviceList != null)
                        deviceList.clear();
                        adapter.clear();
                        scanLeDevice(true);
                    }else {
                        item.setTitle("重新扫描");
                        proLoading.setVisibility(View.GONE);
                        tvStop.setVisibility(View.GONE);
                        scanLeDevice(false);
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_CANCELED) {
            // finish();
            return;
        }
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_OK) {
            scanLeDevice(true);
            return;
        }
        if (requestCode == REQUEST_ENABLE_BT2
                && resultCode == Activity.RESULT_OK) {
            scanLeDevice(true);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
