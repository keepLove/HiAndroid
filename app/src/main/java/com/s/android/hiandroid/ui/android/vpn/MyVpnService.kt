package com.s.android.hiandroid.ui.android.vpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.support.v4.content.LocalBroadcastManager
import java.io.Closeable
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.Selector
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MyVpnService : VpnService() {

    private var fileDescriptor: ParcelFileDescriptor? = null

    private var tcpSelector: Selector? = null
    private var udpSelector: Selector? = null

    private var deviceToNetworkTCPQueue: ConcurrentLinkedQueue<Packet>? = null
    private var deviceToNetworkUDPQueue: ConcurrentLinkedQueue<Packet>? = null

    private var networkToDeviceQueue: ConcurrentLinkedQueue<ByteBuffer>? = null

    private var executorService: ExecutorService? = null

    override fun onCreate() {
        super.onCreate()
        tcpSelector = Selector.open()
        udpSelector = Selector.open()
        deviceToNetworkTCPQueue = ConcurrentLinkedQueue()
        deviceToNetworkUDPQueue = ConcurrentLinkedQueue()
        networkToDeviceQueue = ConcurrentLinkedQueue()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            establish(it)
        }
        return START_STICKY_COMPATIBILITY
    }

    private fun establish(intent: Intent) {
        executorService?.shutdownNow()
        closeResources(fileDescriptor, tcpSelector, udpSelector)

        fileDescriptor = Builder()
            // MTU（Maximun Transmission Unit），即表示虚拟网络端口的最大传输单元，如果发送的包长度超过这个数字，则会被分包；
//                .setMtu(102400)
            // 这个虚拟网络端口的IP地址；
            .addAddress(intent.getStringExtra(EXTRA_ADDRESS), 32)
            // 只有匹配上的IP包，才会被路由到虚拟端口上去。如果是0.0.0.0/0的话，则会将所有的IP包都路由到虚拟端口上去；
            .addRoute(intent.getStringExtra(EXTRA_ROUTE), 0)
            // 就是该端口的DNS服务器地址；使用本地dns
//                .addDnsServer("")
            // 就是添加DNS域名的自动补齐。DNS服务器必须通过全域名进行搜索，但每次查找都输入全域名太麻烦了，可以通过配置域名的自动补齐规则予以简化；
//                .addSearchDomain("")
            // 就是你要建立的VPN连接的名字，它将会在系统管理的与VPN连接相关的通知栏和对话框中显示出来；
            .setSession("Hi Android VPN")
            // 这个intent指向一个配置页面，用来配置VPN链接。它不是必须的，如果没设置的话，则系统弹出的VPN相关对话框中不会出现配置按钮。
            .setConfigureIntent(null)
            .establish()
        listenerStream()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROADCAST_VPN_STATE).putExtra("running", true))
    }

    private fun listenerStream() {
        executorService = Executors.newFixedThreadPool(5)
        executorService?.let {
            it.submit(UDPInput(networkToDeviceQueue, udpSelector))
            it.submit(UDPOutput(deviceToNetworkUDPQueue, udpSelector, this@MyVpnService))
            it.submit(TCPInput(networkToDeviceQueue, tcpSelector))
            it.submit(TCPOutput(deviceToNetworkTCPQueue, networkToDeviceQueue, tcpSelector, this@MyVpnService))
            it.submit(
                VPNRunnable(
                    fileDescriptor?.fileDescriptor,
                    deviceToNetworkUDPQueue,
                    deviceToNetworkTCPQueue,
                    networkToDeviceQueue
                )
            )
        }
    }

    private fun cleanup() {
        deviceToNetworkTCPQueue = null
        networkToDeviceQueue = null
        deviceToNetworkUDPQueue = null
        ByteBufferPool.clear()
        closeResources(fileDescriptor, udpSelector, tcpSelector)
    }

    private fun closeResources(vararg resources: Closeable?) {
        for (resource in resources) {
            try {
                resource?.close()
            } catch (e: IOException) {
                // Ignore
            }
        }
    }

    /**
     * VPN 连接因为任何情况被关闭
     */
    override fun onRevoke() {
        super.onRevoke()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROADCAST_VPN_REVOKE).putExtra("running", false))
    }

    override fun onDestroy() {
        super.onDestroy()
        executorService?.shutdownNow()
        cleanup()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROADCAST_VPN_REVOKE).putExtra("running", false))
    }

    companion object {

        const val EXTRA_ADDRESS = "address"
        const val EXTRA_ROUTE = "route"

        const val BROADCAST_VPN_STATE = "VPN_STATE"
        const val BROADCAST_VPN_REVOKE = "VPN_REVOKE"

    }
}