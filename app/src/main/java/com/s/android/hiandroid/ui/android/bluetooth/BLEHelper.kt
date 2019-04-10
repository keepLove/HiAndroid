package com.s.android.hiandroid.ui.android.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.os.Message
import android.support.v4.app.FragmentActivity
import com.s.android.hiandroid.common.utils.logD
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*

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
    }

    /**
     * 扫描蓝牙设备
     * 由于蓝牙扫描的操作比较消耗手机的能量。所以我们不能一直开着蓝牙，必须设置一段时间之后关闭蓝牙扫描。
     * 如果应用没有位置权限，蓝牙扫描功能不能使用
     */
    @SuppressLint("CheckResult")
    override fun startDiscovery() {
        RxPermissions(activity)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe {
                    if (it && !scanning) {
                        scanning = true
                        bluetoothAdapter?.startLeScan(leScanCallback)
                        handler.sendEmptyMessageDelayed(HANDLER_WHAT_STOP_SCAN, 5000)
                    }
                }
    }

    /**
     * 停止蓝牙扫描
     */
    override fun cancelDiscovery() {
        scanning = false
        bluetoothAdapter?.stopLeScan(leScanCallback)
        bluetoothListener.discoveryFinish()
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                // 当尝试连接失败的时候调用 disconnect 方法是不会引起这个方法回调的，所以这里
                // 直接回调就可以了。
                this@BLEHelper.closeGatt()
                return
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 设备已经连接
                connectionState = STATE_CONNECTED
                // 搜索服务器
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 设备断开连接
                connectionState = STATE_DISCONNECTED
                this@BLEHelper.closeGatt()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 搜索服务器成功
                // 在用户界面上显示所有受支持的服务和特征。
                bluetoothListener.onServicesDiscovered(bluetoothGatt?.services)
//                bluetoothGatt?.services?.forEach { bluetoothGattService ->
//                    bluetoothGattService.characteristics.forEach { bluetoothGattCharacteristic ->
//                        // 读取数据
////                            bluetoothGatt?.readCharacteristic(bluetoothGattCharacteristic)
//                        // 往蓝牙数据通道的写入数据
////                            bluetoothGattCharacteristic.setValue("")
////                            bluetoothGatt?.writeCharacteristic(bluetoothGattCharacteristic)
//                        // 向蓝牙设备注册监听实现实时读取蓝牙设备的数据
//                        bluetoothGatt?.setCharacteristicNotification(bluetoothGattCharacteristic, true)
//                        bluetoothGattCharacteristic.descriptors.forEach { descriptor ->
//                            descriptor.value = "".toByteArray()
//                            bluetoothGatt?.writeDescriptor(descriptor)
//                        }
//                    }
//                }
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 读取characteristic消息
                onCharacteristicChanged(gatt, characteristic)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            // 写入的数据是否我们需要发送的数据,如果不是按照项目的需要判断是否需要重发
            // 执行重发策略
            // gatt.writeCharacteristic(characteristic)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            // Characteristic notification
            if (characteristic.uuid == UUID_HEART_RATE_MEASUREMENT) {
                // This is special handling for the Heart Rate Measurement profile.  Data parsing is
                // carried out as per profile specifications:
                // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
                val flag = characteristic.properties
                val format: Int
                if (flag and 0x01 != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16
                    logD("Heart rate format UINT16.")
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8
                    logD("Heart rate format UINT8.")
                }
                val heartRate = characteristic.getIntValue(format, 1)!!
                logD(String.format("Received heart rate: %d", heartRate))
                bluetoothListener.readMessage(heartRate.toString())
            } else {
                // For all other profiles, writes the data formatted in HEX.
                val data = characteristic.value
                if (data != null && data.isNotEmpty()) {
                    val stringBuilder = StringBuilder(data.size)
                    for (byteChar in data)
                        stringBuilder.append(String.format("%02X ", byteChar))
                    bluetoothListener.readMessage(String(data) + "\n" + stringBuilder.toString())
                }
            }
        }
    }

    override fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    /**
     * 连接蓝牙设备
     */
    override fun connect(bluetoothDevice: BluetoothDevice) {
        if (scanning) {
            cancelDiscovery()
            handler.removeCallbacksAndMessages(null)
        }
        // 以前连接设备。尝试重新连接。
        if (bluetoothDevice.address == connectBluetoothDevice?.address && bluetoothGatt != null) {
            if (bluetoothGatt?.connect() == true) {
                connectionState = STATE_CONNECTING
                logD("重新连接成功 : ${bluetoothDevice.name}")
            } else {
                logD("重新连接失败 : ${bluetoothDevice.name}")
            }
            return
        }
        /*
         * 第二个参数表示是否需要自动连接。如果设置为 true, 表示如果设备断开了，会不断的尝试自动连接。
         * 设置为 false 表示只进行一次连接尝试。
         * 第三个参数是连接后进行的一系列操作的回调，例如连接和断开连接的回调，发现服务的回调，成功写入数据，成功读取数据的回调等等。
         */
        bluetoothGatt = bluetoothDevice.connectGatt(activity, false, gattCallback)
        connectBluetoothDevice = bluetoothDevice
        connectionState = STATE_CONNECTING
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
    fun closeGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    override fun write(byteArray: ByteArray) {
    }

    override fun onDestroy() {
        super.onDestroy()
        closeGatt()
        handler.removeCallbacksAndMessages(null)
    }

    override fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enabled: Boolean) {
        bluetoothGatt?.setCharacteristicNotification(characteristic, enabled)
        // 这是专门针对心率测量的。
        if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG))
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            bluetoothGatt?.writeDescriptor(descriptor)
        }
    }

    override fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.readCharacteristic(characteristic)
    }

    companion object {

        /**
         * 停止蓝牙扫描
         */
        const val HANDLER_WHAT_STOP_SCAN = 1001

        val UUID_HEART_RATE_MEASUREMENT: UUID = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT)
    }
}