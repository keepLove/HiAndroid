package com.s.android.hiandroid.ui.android.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity

abstract class BluetoothImpl(protected val activity: FragmentActivity, protected val bluetoothListener: BluetoothListener) {

    /**
     * 本地设备蓝牙适配器
     */
    protected var bluetoothAdapter: BluetoothAdapter? = null
    /**
     * 当前连接的device
     */
    var connectBluetoothDevice: BluetoothDevice? = null
    /**
     * 连接状态
     */
    var state: Int = STATE_NONE

    /**
     * 是否支持蓝牙
     */
    open fun isBluetooth(): Boolean = bluetoothAdapter != null

    /**
     * 扫描蓝牙设备
     */
    abstract fun startDiscovery()

    /**
     * 启动聊天服务
     */
    abstract fun start()

    /**
     * 停止蓝牙扫描
     */
    abstract fun cancelDiscovery()

    /**
     * 写入数据
     */
    abstract fun write(byteArray: ByteArray)

    /**
     * 连接蓝牙设备
     */
    abstract fun connect(bluetoothDevice: BluetoothDevice)

    /**
     * 销毁时取消连接
     */
    abstract fun onDestroy()

    /**
     * 将本地设备设为可被其他设备检测到.
     * 应用可以设置的最大持续时间为 3600 秒，值为 0 则表示设备始终可检测到。
     * 任何小于 0 或大于 3600 的值都会自动设为 120 秒。
     */
    open fun settingDiscoverable(requestCode: Int, time: Int? = 120) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time ?: 120)
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
    }

    /**
     * 可检测模式
     */
    fun getScanModel(): String {
        return getScanModel(bluetoothAdapter?.scanMode)
    }

    /**
     * 可检测模式
     */
    fun getScanModel(scanMode: Int?): String {
        return when (scanMode) {
            BluetoothAdapter.SCAN_MODE_NONE -> {
                "未处于可检测到模式并且无法接收连接"
            }
            BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                "未处于可检测到模式但仍能接收连接"
            }
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                "设备处于可检测到模式"
            }
            else -> {
                "未处于可检测到模式并且无法接收连接"
            }
        }
    }

    /**
     * 连接状态
     */
    fun getBondState(bondState: Int): String {
        return when (bondState) {
            BluetoothDevice.BOND_BONDED -> {
                "已连接"
            }
            BluetoothDevice.BOND_BONDING -> {
                "正在连接"
            }
            BluetoothDevice.BOND_NONE -> {
                "未连接"
            }
            else -> {
                ""
            }
        }
    }

    /**
     * 获取本地蓝牙名称
     */
    fun getLocalName(): String {
        return bluetoothAdapter?.name ?: ""
    }

    /**
     * 获取本地蓝牙地址
     */
    @SuppressLint("HardwareIds")
    fun getLocalAddress(): String {
        return bluetoothAdapter?.address ?: ""
    }

    /**
     * 判断是否打开蓝牙
     */
    fun isEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    /**
     * 打开蓝牙
     */
    fun enable(requestCode: Int) {
        // 弹窗申请权限打开蓝牙
        activity.startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestCode)
        // 不做提示，强行打开
        // bluetoothAdapter?.enable()
    }

    /**
     * 获取已经配对的设备
     */
    fun getBondedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }

    companion object {

        /*
         * 表示当前连接状态的常量
         */
        const val STATE_NONE = 0       // 我们什么都不做
        const val STATE_LISTEN = 1     // 现在监听传入连接
        const val STATE_CONNECTING = 2 // 现在启动一个传出连接
        const val STATE_CONNECTED = 3  // 现在连接到远程设备

        /*
         * 连接状态的常量
         */
        /**
         * 指示连接已丢失
         */
        const val CONNECTION_STATE_LOST = 10
        /**
         * 指示连接尝试失败
         */
        const val CONNECTION_STATE_FAIL = 11
    }

}


interface BluetoothListener {

    /**
     * 搜索到
     */
    fun discovery(bluetoothDevice: BluetoothDevice)

    /**
     * 搜索完成
     */
    fun discoveryFinish()

    /**
     * 可检测到模式发生变化时收到通知
     */
    fun scanModeChanged(scanMode: Int)

    /**
     * 读取到的消息
     */
    fun readMessage(byteArray: ByteArray, size: Int)

    /**
     * 连接状态改变
     */
    fun connectionStateChange(state: Int)
}
