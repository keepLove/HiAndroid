package com.s.android.hiandroid.ui.android.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothReceiver(private val bluetoothListener: BluetoothListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            BluetoothDevice.ACTION_FOUND -> { // 发现设备的广播
                // 从intent中获取设备
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                // 判断是否配对过
                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    bluetoothListener.discovery(device)
                }
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                // 搜索完成的广播
                bluetoothListener.discoveryFinish()
            }
            BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                // 可检测到模式发生变化时收到通知
                val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE)
                bluetoothListener.scanModeChanged(scanMode)
            }
        }
    }

}
