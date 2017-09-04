package cn.app.model;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Created by li.qing
 * on 2017/7/6.
 */

public class BluetoothDeviceBean implements Serializable {
    private BluetoothDevice bluetoothDevice;
    private byte[] scanRecord;
    private String deviceInfo;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }
}
