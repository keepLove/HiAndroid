package com.s.android.hiandroid.ui.android.vpn

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.info.OptionsMenu
import kotlinx.android.synthetic.main.activity_vpn.*

class VPNActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu(
                "VPN 实现原理介绍",
                "https://www.infoq.cn/article/book-android-vpn"
            ),
            OptionsMenu(
                "反射实现VPN连接",
                "https://blog.csdn.net/cyj88jyc/article/details/87982313"
            ),
            OptionsMenu(
                "系统自带的VPN服务框架",
                "https://blog.csdn.net/roland_sun/article/details/46337171"
            ),
            OptionsMenu("监听本地网络连接", "https://github.com/hexene/LocalVPN")
        )

    private var vpnStart = false

    private var extraAddress: String? = null
    private var extraRoute: String? = null

    override fun getLayoutResID(): Int? {
        return R.layout.activity_vpn
    }

    override fun init(savedInstanceState: Bundle?) {
        bt_start.setOnClickListener {
            startVPN()
        }
        bt_start_local.setOnClickListener {
            extraAddress = "10.0.0.2"
            extraRoute = "0.0.0.0"
            prepare()
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            vpnStateReceiver,
            IntentFilter(MyVpnService.BROADCAST_VPN_STATE)
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(
            vpnStateReceiver,
            IntentFilter(MyVpnService.BROADCAST_VPN_REVOKE)
        )
        tv_local_ip.text = getLocalIpAddress()
    }

    private val vpnStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MyVpnService.BROADCAST_VPN_STATE -> {
                    vpnStart = true
                }
                MyVpnService.BROADCAST_VPN_REVOKE -> {
                    vpnStart = false
                }
                else -> {
                }
            }
            enableButton()
        }
    }

    private fun startVPN() {
        val address: String? = et_ip_address.text.toString()
        if (address.isNullOrEmpty()) {
            showToastAndLog("address can't null.")
            return
        }
        val name: String? = et_ip_name.text.toString()
        if (name.isNullOrEmpty()) {
            showToastAndLog("name can't null.")
            return
        }
        extraAddress = address
        extraRoute = name
        prepare()
    }

    private fun prepare() {
        // VpnService.prepare函数的目的，主要是用来检查当前系统中是不是已经存在一个VPN连接了，
        // 如果有了的话，是不是就是本程序创建的。
        // 如果当前系统中没有VPN连接，或者存在的VPN连接不是本程序建立的，则VpnService.prepare函数会
        // 返回一个intent。这个intent就是用来触发确认对话框的，程序会接着调用startActivityForResult将
        // 对话框弹出来等用户确认。如果用户确认了，则会关闭前面已经建立的VPN连接，并重置虚拟端口。
        // 该对话框返回的时候，会调用onActivityResult函数，并告之用户的选择。
        // 如果当前系统中有VPN连接，并且这个连接就是本程序建立的，则函数会返回null，就不需要用户再确认了。
        // 因为用户在本程序第一次建立VPN连接的时候已经确认过了，就不要再重复确认了。
        val prepare = VpnService.prepare(this)
        if (prepare != null) {
            startActivityForResult(prepare, 1000)
        } else {
            startVpnService()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            startVpnService()
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, MyVpnService::class.java)
        intent.putExtra(MyVpnService.EXTRA_ADDRESS, extraAddress)
        intent.putExtra(MyVpnService.EXTRA_ROUTE, extraRoute)
        startService(intent)
    }

    private fun enableButton() {
        if (vpnStart) {
            bt_start.isEnabled = false
            bt_start.text = "Stop from Notification Bar"
        } else {
            bt_start.isEnabled = true
            bt_start.text = "Start VPN"
        }
    }

    /**
     * 获取本地ip地址
     */
    private fun getLocalIpAddress(): String? {
        //获取wifi服务
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return intToIp(ipAddress)
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(vpnStateReceiver)
    }
}
