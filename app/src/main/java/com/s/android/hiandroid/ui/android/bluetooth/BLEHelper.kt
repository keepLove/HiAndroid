package com.s.android.hiandroid.ui.android.bluetooth

import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.os.Message
import android.support.v4.app.FragmentActivity
import com.s.android.hiandroid.common.utils.logD

/**
 * Bluetooth low energy
 * 低功耗蓝牙
 * Android 4.3（API Level 18）开始引入Bluetooth Low Energy（BLE，低功耗蓝牙）
 */
class BLEHelper(activity: FragmentActivity, bluetoothListener: BluetoothListener)
    : BluetoothImpl(activity, bluetoothListener) {

    init {
        val bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    private val handler = object : android.os.Handler(Looper.myLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                HANDLER_WHAT_STOP_SCAN -> {
                    cancelDiscovery()
                }
                else -> {
                }
            }
        }
    }

    /**
     * 是否正在扫描
     */
    private var scanning = false

    /**
     * 是否支持BLE蓝牙
     */
    override fun isBluetooth(): Boolean {
        return bluetoothAdapter != null && activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, _, _ ->
        bluetoothListener.discovery(device)
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 扫描蓝牙设备
     * 由于蓝牙扫描的操作比较消耗手机的能量。所以我们不能一直开着蓝牙，必须设置一段时间之后关闭蓝牙扫描。
     */
    override fun startDiscovery() {
        scanning = true
        bluetoothAdapter?.startLeScan(leScanCallback)
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_STOP_SCAN, 1000)
    }

    /**
     * 停止蓝牙扫描
     */
    override fun cancelDiscovery() {
        scanning = false
        bluetoothAdapter?.stopLeScan(leScanCallback)
    }

    private var bluetoothGatt: BluetoothGatt? = null

    /**
     * 连接蓝牙设备
     */
    override fun connect(bluetoothDevice: BluetoothDevice) {
        val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    // 当尝试连接失败的时候调用 disconnect 方法是不会引起这个方法回调的，所以这里
                    // 直接回调就可以了。
                    gatt.close()
                    return
                }
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // 设备已经连接
                    // 搜索服务器
                    bluetoothGatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // 设备断开连接
                    gatt.close()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // 搜索服务器成功
                    bluetoothGatt?.services?.forEach { bluetoothGattService ->
                        bluetoothGattService.characteristics.forEach { bluetoothGattCharacteristic ->
                            // 读取数据
//                            bluetoothGatt?.readCharacteristic(bluetoothGattCharacteristic)
                            // 往蓝牙数据通道的写入数据
//                            bluetoothGattCharacteristic.setValue("")
//                            bluetoothGatt?.writeCharacteristic(bluetoothGattCharacteristic)
                            // 向蓝牙设备注册监听实现实时读取蓝牙设备的数据
                            bluetoothGatt?.setCharacteristicNotification(bluetoothGattCharacteristic, true)
                            bluetoothGattCharacteristic.descriptors.forEach { descriptor ->
                                descriptor.value = "".toByteArray()
                                bluetoothGatt?.writeDescriptor(descriptor)
                            }
                        }
                    }
                }
            }

            override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // 读取characteristic消息
                    logD("message : ${characteristic.value}")
                }
            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
                // 写入的数据是否我们需要发送的数据,如果不是按照项目的需要判断是否需要重发
                // 执行重发策略
                // gatt.writeCharacteristic(characteristic)
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
                // Characteristic notification
            }
        }
        /*
         * 第二个参数表示是否需要自动连接。如果设置为 true, 表示如果设备断开了，会不断的尝试自动连接。
         * 设置为 false 表示只进行一次连接尝试。
         * 第三个参数是连接后进行的一系列操作的回调，例如连接和断开连接的回调，发现服务的回调，成功写入数据，成功读取数据的回调等等。
         */
        bluetoothGatt = bluetoothDevice.connectGatt(activity, false, gattCallback)
    }

    /**
     * 当我们连接蓝牙设备完成一系列的蓝牙操作之后就可以断开蓝牙设备的连接了。
     * 通过 BluetoothGatt#disconnect 可以断开正在连接的蓝牙设备。当这一个方法被调用之后，
     * 系统会异步回调 BluetoothGattCallback#onConnectionStateChange 方法。通过这个方法的 newState
     * 参数可以判断是连接成功还是断开成功的回调。
     * 由于 Android 蓝牙连接设备的资源有限，当我们执行断开蓝牙操作之后必须执行 BluetoothGatt#close
     * 方法释放资源。需要注意的是通过 BluetoothGatt#close 方法也可以执行断开蓝牙的操作，不过
     * BluetoothGattCallback#onConnectionStateChange 将不会收到任何回调。此时如果执行 BluetoothGatt#connect
     * 方法会得到一个蓝牙 API 的空指针异常。所以，我们推荐的写法是当蓝牙成功连接之后，通过
     * BluetoothGatt#disconnect 断开蓝牙的连接，紧接着在 BluetoothGattCallback#onConnectionStateChange
     * 执行 BluetoothGatt#close 方法释放资源。
     */
    fun close() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    override fun write(byteArray: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        /**
         * 停止蓝牙扫描
         */
        const val HANDLER_WHAT_STOP_SCAN = 1001
    }
}