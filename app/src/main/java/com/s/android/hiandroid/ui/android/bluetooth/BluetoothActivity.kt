package com.s.android.hiandroid.ui.android.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.BaseRecyclerAdapter
import com.s.android.hiandroid.common.utils.logD
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_bluetooth.*


/**
 * https://github.com/Alex-Jerry/Android-BLE
 * https://mp.weixin.qq.com/s?__biz=MzIxNzU1Nzk3OQ==&mid=2247484750&idx=1&sn=b857c650ada19b00880d9b006296e403&chksm=97f6bbfaa08132ec3b58ab1818de3b33c63ed0787428733f17554d2e671c6175ae09565ff70b&scene=38#wechat_redirect
 */
class BluetoothActivity : BaseActivity(), BluetoothListener {

    private var recyclerAdapter: BluetoothRecyclerAdapter? = null
    private var boundDeviceRecyclerAdapter: BluetoothRecyclerAdapter? = null
    private val bluetoothHelper: BluetoothImpl by lazy {
        if (intent.getStringExtra("type") == "classic bluetooth") {
            ClassicBluetoothHelper(this, this)
        } else {
            BLEHelper(this, this)
        }
    }
    private val stringBuffer = StringBuffer()

    override fun getLayoutResID(): Int? {
        return R.layout.activity_bluetooth
    }

    @SuppressLint("HardwareIds")
    override fun init(savedInstanceState: Bundle?) {
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
            if (bluetoothHelper.state == BluetoothImpl.STATE_NONE) {
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
        showToastAndLog("scanMode:$scanMode")
        tv_scan_mode.text = bluetoothHelper.getScanModel(scanMode)
    }

    override fun readMessage(byteArray: ByteArray, size: Int) {
        val message = String(byteArray, 0, size)
        logD("read message: number:$size   string:$message")
        setMessage("${bluetoothHelper.connectBluetoothDevice?.name} : $message")
    }

    override fun connectionStateChange(state: Int) {
        when (state) {
            BluetoothImpl.STATE_NONE,
            BluetoothImpl.STATE_LISTEN -> {
                setMessage("state : 未连接")
            }
            BluetoothImpl.STATE_CONNECTING -> {
                setMessage("state : 连接中")
            }
            BluetoothImpl.STATE_CONNECTED -> {
                setMessage("state : 连接成功 : ${bluetoothHelper.connectBluetoothDevice?.name}")
            }
            BluetoothImpl.CONNECTION_STATE_LOST -> {
                showToastAndLog("设备连接丢失")
            }
            BluetoothImpl.CONNECTION_STATE_FAIL -> {
                showToastAndLog("无法连接设备")
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
