package com.s.android.hiandroid.ui.android.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ExpandableListAdapter
import android.widget.SimpleExpandableListAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.BaseRecyclerAdapter
import com.s.android.hiandroid.common.utils.logD
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_bluetooth.*
import java.util.*
import kotlin.collections.ArrayList

class BluetoothActivity : BaseActivity(), BluetoothListener {

    private var recyclerAdapter: BluetoothRecyclerAdapter? = null
    private var boundDeviceRecyclerAdapter: BluetoothRecyclerAdapter? = null
    private val bluetoothHelper: BluetoothImpl by lazy {
        if (bluetoothType == "Classic Bluetooth") {
            ClassicBluetoothHelper(this, this)
        } else {
            BLEHelper(this, this)
        }
    }
    private val stringBuffer = StringBuffer()
    private val bluetoothType: String by lazy {
        val type = intent.getStringExtra("type")
        type ?: "Classic Bluetooth"
    }
    private var mNotifyCharacteristic: BluetoothGattCharacteristic? = null

    override fun getLayoutResID(): Int? {
        return R.layout.activity_bluetooth
    }

    @SuppressLint("HardwareIds")
    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.title = bluetoothType
        if (!bluetoothHelper.isBluetooth()) {
            showToastAndLog("该设备不支持蓝牙")
            finish()
        }
        tv_discovery.setOnClickListener {
            showToastAndLog("开始搜索")
            bluetoothHelper.startDiscovery()
        }
        tv_time_title.setOnClickListener {
            bluetoothHelper.settingDiscoverable(1002, et_time.text.toString().toIntOrNull())
        }
        tv_scan_mode.text = bluetoothHelper.getScanModel()
        // 已配对设备
        rv_bound_device.layoutManager = LinearLayoutManager(this)
        boundDeviceRecyclerAdapter = BluetoothRecyclerAdapter()
        rv_bound_device.adapter = boundDeviceRecyclerAdapter
        // 搜索设备
        recycler_view.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = BluetoothRecyclerAdapter()
        recycler_view.adapter = recyclerAdapter
        // 发送消息，传输数据
        bt_send.setOnClickListener {
            val message = et_message.text.toString()
            if (message.isNotEmpty()) {
                bluetoothHelper.write(message.toByteArray())
                setMessage("Me: $message")
                et_message.text = null
            }
        }
        gatt_services_list.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            if (mGattCharacteristics.isNotEmpty()) {
                val characteristic = mGattCharacteristics[groupPosition][childPosition]
                val charaProp = characteristic.properties
                if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
                    // If there is an active notification on a characteristic, clear
                    // it first so it doesn't update the data field on the user interface.
                    if (mNotifyCharacteristic != null) {
                        bluetoothHelper.setCharacteristicNotification(
                            mNotifyCharacteristic!!, false
                        )
                        mNotifyCharacteristic = null
                    }
                    bluetoothHelper.readCharacteristic(characteristic)
                }
                if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                    mNotifyCharacteristic = characteristic
                    bluetoothHelper.setCharacteristicNotification(
                        characteristic, true
                    )
                }
                return@setOnChildClickListener true
            }
            return@setOnChildClickListener false
        }
    }

    override fun onResume() {
        super.onResume()
        //判断是否打开蓝牙
        if (bluetoothHelper.isEnabled()) {
            getLocalBluetooth()
        } else {
            bluetoothHelper.enable(1001)
        }
    }

    inner class BluetoothRecyclerAdapter : BaseRecyclerAdapter<BluetoothDevice>(R.layout.item_bluetooth) {

        init {
            setOnItemClickListener { _, _, position ->
                val item = getItem(position)
                showToastAndLog("name:${item?.name}")
                bluetoothHelper.connect(item!!)
            }
        }

        override fun convert(helper: BaseViewHolder, item: BluetoothDevice) {
            helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_address, item.address)
                .setText(R.id.tv_bound, bluetoothHelper.getBondState(item.bondState))
        }
    }

    /**
     * 获取本地蓝牙信息
     */
    @SuppressLint("HardwareIds")
    private fun getLocalBluetooth() {
        if (bluetoothHelper.isEnabled()) {
            //获取本地蓝牙名称
            tv_name.text = bluetoothHelper.getLocalName()
            //获取本地蓝牙地址
            tv_address.text = bluetoothHelper.getLocalAddress()
            if (bluetoothHelper.connectionState == BluetoothImpl.STATE_NONE) {
                bluetoothHelper.start()
            }
            this@BluetoothActivity.getBondedDevices()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothHelper.onDestroy()
    }

    /**
     * 获取已经配对的设备
     */
    private fun getBondedDevices() {
        bluetoothHelper.getBondedDevices()?.forEach { bluetoothDevice ->
            boundDeviceRecyclerAdapter?.apply {
                // 避免重复
                var contain = -1
                data.forEachIndexed { index, device ->
                    if (device.address == bluetoothDevice.address) {
                        contain = index
                        return@forEachIndexed
                    }
                }
                if (contain == -1) {
                    addData(bluetoothDevice)
                } else {
                    data[contain] = bluetoothDevice
                    notifyItemChanged(contain)
                }
            }
        }
    }

    override fun discovery(bluetoothDevice: BluetoothDevice) {
        recyclerAdapter?.apply {
            // 避免重复
            var contain = -1
            data.forEachIndexed { index, device ->
                if (device.address == bluetoothDevice.address) {
                    contain = index
                    return@forEachIndexed
                }
            }
            if (contain == -1) {
                addData(bluetoothDevice)
            } else {
                data[contain] = bluetoothDevice
                notifyItemChanged(contain)
            }
        }
    }

    override fun discoveryFinish() {
        showToastAndLog("搜索完成")
    }

    override fun scanModeChanged(scanMode: Int) {
        showToastAndLog("scanMode:${bluetoothHelper.getScanModel(scanMode)}")
        tv_scan_mode.text = bluetoothHelper.getScanModel(scanMode)
    }

    override fun readMessage(message: String) {
        logD("read message: number:   string:$message")
        setMessage("${bluetoothHelper.connectBluetoothDevice?.name} : $message")
    }

    private val listName = "NAME"
    private val listUuid = "UUID"
    private var mGattCharacteristics: ArrayList<ArrayList<BluetoothGattCharacteristic>> = ArrayList()

    override fun onServicesDiscovered(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        var uuid: String?
        val unknownServiceString = "unknown_service"
        val unknownCharaString = "unknown_characteristic"
        val gattServiceData = ArrayList<HashMap<String, String>>()
        val gattCharacteristicData = ArrayList<ArrayList<HashMap<String, String>>>()
        mGattCharacteristics = ArrayList()

        // Loops through available GATT Services.
        for (gattService in gattServices) {
            val currentServiceData = HashMap<String, String>()
            uuid = gattService.uuid.toString()
            currentServiceData[listName] = SampleGattAttributes.lookup(uuid, unknownServiceString)
            currentServiceData[listUuid] = uuid
            gattServiceData.add(currentServiceData)

            val gattCharacteristicGroupData = ArrayList<HashMap<String, String>>()
            val gattCharacteristics = gattService.characteristics
            val charas = ArrayList<BluetoothGattCharacteristic>()

            // Loops through available Characteristics.
            for (gattCharacteristic in gattCharacteristics) {
                charas.add(gattCharacteristic)
                val currentCharaData = HashMap<String, String>()
                uuid = gattCharacteristic.uuid.toString()
                currentCharaData[listName] = SampleGattAttributes.lookup(uuid, unknownCharaString)
                currentCharaData[listUuid] = uuid
                gattCharacteristicGroupData.add(currentCharaData)
            }
            mGattCharacteristics.add(charas)
            gattCharacteristicData.add(gattCharacteristicGroupData)
        }

        val gattServiceAdapter = SimpleExpandableListAdapter(
            this,
            gattServiceData,
            android.R.layout.simple_expandable_list_item_2,
            arrayOf(listName, listUuid),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            gattCharacteristicData,
            android.R.layout.simple_expandable_list_item_2,
            arrayOf(listName, listUuid),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        gatt_services_list.setAdapter(gattServiceAdapter)
    }

    override fun connectionStateChange(state: Int) {
        when (state) {
            BluetoothImpl.STATE_NONE,
            BluetoothImpl.STATE_LISTEN -> {
                setMessage("connectionState : 未连接")
            }
            BluetoothImpl.STATE_CONNECTING -> {
                setMessage("connectionState : 连接中")
            }
            BluetoothImpl.STATE_CONNECTED -> {
                setMessage("connectionState : 连接成功 : ${bluetoothHelper.connectBluetoothDevice?.name}")
            }
            BluetoothImpl.STATE_DISCONNECTED -> {
                setMessage("connectionState : 设备断开连接")
                gatt_services_list.setAdapter(null as ExpandableListAdapter?)
            }
            BluetoothImpl.CONNECTION_STATE_LOST -> {
                setMessage("设备连接丢失")
            }
            BluetoothImpl.CONNECTION_STATE_FAIL -> {
                setMessage("无法连接设备")
            }
            else -> {
            }
        }
    }

    private fun setMessage(message: String) {
        stringBuffer.append("$message  \n")
        runOnUiThread {
            tv_message.text = stringBuffer.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> {
                // 打开蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    getLocalBluetooth()
                }
            }
            1002 -> {
                // 将本地设备设为可被其他设备检测到
                logD("data : ${data?.toString()}")
            }
        }

    }
}
