package cn.app.viewholder;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.yhub.app.R;
import cn.app.model.BluetoothDeviceBean;

/**
 *
 */
public class BleDevicesViewHolder extends BaseViewHolder<BluetoothDeviceBean> {
    //@BindView(R.id.device_name)
    TextView deviceName;
    //@BindView(R.id.device_address)
    TextView deviceAddress;
    //@BindView(R.id.device_info)
    TextView deviceInfo;
    /*private TextView tVpname;
    private TextView tVsy;
    private TextView tVyy;
    private ImageView testImg;*/

    public BleDevicesViewHolder(ViewGroup parent) {
        super(parent, R.layout.listitem_device);
        //ButterKnife.bind(this, itemView);
        deviceName = $(R.id.device_name);
        deviceAddress = $(R.id.device_address);
        deviceInfo = $(R.id.device_info);
    }

    @Override
    public void setData(final BluetoothDeviceBean bluetoothDeviceBean) {
        Log.i("ViewHolder", "position" + getDataPosition());
        BluetoothDevice device = bluetoothDeviceBean.getBluetoothDevice();
        final String name = device.getName();
        if (name != null && name.length() > 0)
            deviceName.setText(name);
        deviceAddress.setText(device.getAddress());
        deviceInfo.setText(bluetoothDeviceBean.getDeviceInfo());
    }
}
