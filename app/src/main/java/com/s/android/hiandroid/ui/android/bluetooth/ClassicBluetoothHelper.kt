package com.s.android.hiandroid.ui.android.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.support.v4.app.FragmentActivity
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * 经典蓝牙
 */
class ClassicBluetoothHelper(activity: FragmentActivity,
                             bluetoothListener: BluetoothListener) : BluetoothImpl(activity, bluetoothListener) {

    /**
     * 蓝牙广播
     */
    private val bluetoothReceiver: BluetoothReceiver

    init {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothReceiver = BluetoothReceiver(bluetoothListener)
        if (isBluetooth()) {
            registerReceiver()
        }
    }

    /**
     * uuid
     */
    private val uuid: UUID by lazy {
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
    }

    /**
     * 注册蓝牙广播
     */
    private fun registerReceiver() {
        // 找到设备的广播
        activity.registerReceiver(bluetoothReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        // 搜索完成的广播
        activity.registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
        // 可检测到模式发生变化时收到通知
        activity.registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED))
    }

    /**
     * 销毁时取消连接
     */
    override fun onDestroy() {
        // 取消注册广播
        activity.unregisterReceiver(bluetoothReceiver)
        // 取消完成连接的线程
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }
        // 取消当前正在运行连接的任何线程
        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }
        // 取消accept线程，因为我们只想连接到一个设备
        if (acceptThread != null) {
            acceptThread?.cancel()
            acceptThread = null
        }
        state = STATE_NONE
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
    }

    /**
     * 开始搜索
     *
     *  注意：执行设备发现对于蓝牙适配器而言是一个非常繁重的操作过程，并且会消耗大量资源。
     * 在找到要连接的设备后，确保始终使用 cancelDiscovery() 停止发现，然后再尝试连接。
     * 此外，如果您已经保持与某台设备的连接，那么执行发现操作可能会大幅减少可用于该连接的带宽，
     * 因此不应该在处于连接状态时执行发现操作。
     * 首先需要取消搜索
     */
    override fun startDiscovery() {
        cancelDiscovery()
        bluetoothAdapter?.startDiscovery()
    }

    /**
     * 取消搜索
     */
    override fun cancelDiscovery() {
        bluetoothAdapter?.apply {
            if (isDiscovering) {
                cancelDiscovery()
            }
        }
    }

    private var acceptThread: AcceptThread? = null
    private var connectedThread: ConnectedThread? = null
    private var connectThread: ConnectThread? = null

    /*
    要在两台设备上的应用之间创建连接，必须同时实现服务器端和客户端机制，因为其中一台设备必须开放服务器套接字，
    而另一台设备必须发起连接（使用服务器设备的 MAC 地址发起连接）。 当服务器和客户端在同一 RFCOMM 通道上分别
    拥有已连接的 BluetoothSocket 时，二者将被视为彼此连接。 这种情况下，每台设备都能获得输入和输出流式传输，并且可以开始传输数据

    服务器设备和客户端设备分别以不同的方法获得需要的 BluetoothSocket。
    服务器将在传入连接被接受时收到套接字。
    客户端将在其打开到服务器的 RFCOMM 通道时收到该套接字。

    一种实现技术是自动将每台设备准备为一个服务器，从而使每台设备开放一个服务器套接字并侦听连接。
    然后任一设备可以发起与另一台设备的连接，并成为客户端。 或者，其中一台设备可显式“托管”连接并
    按需开放一个服务器套接字，而另一台设备则直接发起连接。
     */

    /**
     * 启动聊天服务。具体地说，启动AcceptThread以侦听(服务器)模式开始会话。由 Activity onResume()调用
     */
    @Synchronized
    override fun start() {
        // 取消完成连接的线程
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }
        // 取消当前正在运行连接的任何线程
        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }
        // 启动线程，在BluetoothServerSocket上监听
        if (acceptThread == null) {
            acceptThread = AcceptThread()
            acceptThread?.start()
        }
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
    }

    /**
     * 以非同步的方式写入已连接线程
     */
    override fun write(byteArray: ByteArray) {
        // 同步已连接线程的副本
        val connectedThread = synchronized(this) {
            if (state != STATE_CONNECTED) return
            connectedThread
        }
        // 执行非同步写入
        connectedThread?.write(byteArray)
    }

    /**
     * 启动ConnectThread启动到远程设备的连接。
     */
    @Synchronized
    override fun connect(bluetoothDevice: BluetoothDevice) {
        // 取消任何试图建立连接的线程
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread?.cancel()
                connectThread = null
            }
        }
        // 取消当前正在运行连接的任何线程
        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }
        // 启动线程来连接给定的设备
        connectThread = ConnectThread(bluetoothDevice)
        connectThread?.start()
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
    }

    /**
     * 启动ConnectedThread，开始管理蓝牙连接
     */
    @Synchronized
    private fun connected(bluetoothSocket: BluetoothSocket, bluetoothDevice: BluetoothDevice) {
        // 取消完成连接的线程
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }
        // 取消当前正在运行连接的任何线程
        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }
        // 取消accept线程，因为我们只想连接到一个设备
        if (acceptThread != null) {
            acceptThread?.cancel()
            acceptThread = null
        }
        // 启动线程来管理连接并执行传输
        connectedThread = ConnectedThread(bluetoothSocket)
        connectedThread?.start()
        connectBluetoothDevice = bluetoothDevice
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
    }

    /**
     * 指示连接已丢失，并通知UI Activity。
     */
    private fun connectionLost() {
        activity.runOnUiThread {
            bluetoothListener.connectionStateChange(CONNECTION_STATE_LOST)
        }
        state = STATE_NONE
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
        // 重新启动服务以重新启动监听模式
        start()
    }

    /**
     * 指示连接尝试失败，并通知UI活动。
     */
    private fun connectionFailed() {
        activity.runOnUiThread {
            bluetoothListener.connectionStateChange(CONNECTION_STATE_FAIL)
        }
        state = STATE_NONE
        bluetoothListener.connectionStateChange(this@ClassicBluetoothHelper.state)
        // 重新启动服务以重新启动监听模式
        start()
    }

    /**
     * 连接为服务器
     * 当您需要连接两台设备时，其中一台设备必须通过保持开放的 BluetoothServerSocket 来充当服务器。
     * 服务器套接字的用途是侦听传入的连接请求，并在接受一个请求后提供已连接的 BluetoothSocket。
     * 从 BluetoothServerSocket 获取 BluetoothSocket 后，可以（并且应该）舍弃 BluetoothServerSocket，除非您需要接受更多连接。
     */
    inner class AcceptThread : Thread() {

        /*
         * 该字符串是您的服务的可识别名称，系统会自动将其写入到设备上的新服务发现协议 (SDP) 数据库条目
         * （可使用任意名称，也可直接使用您的应用名称）。
         * UUID 也包含在 SDP 条目中，并且将作为与客户端设备的连接协议的基础。
         * 也就是说，当客户端尝试连接此设备时，它会携带能够唯一标识其想要连接的服务的 UUID。
         * 两个 UUID 必须匹配，在下一步中，连接才会被接受。
         */
        private val bluetoothServerSocket: BluetoothServerSocket? =
                bluetoothAdapter?.listenUsingRfcommWithServiceRecord("BluetoothChatSecure", uuid)

        init {
            this@ClassicBluetoothHelper.state = STATE_LISTEN
        }

        override fun run() {
            var bluetoothSocket: BluetoothSocket?
            while (this@ClassicBluetoothHelper.state != STATE_CONNECTED) {
                try {
                    // 开始侦听连接请求。
                    // 这是一个阻塞调用。它将在连接被接受或发生异常时返回。 仅当远程设备发送的连接请求中所包含的
                    // UUID 与向此侦听服务器套接字注册的 UUID 相匹配时，连接才会被接受。
                    // 操作成功后，accept() 将会返回已连接的 BluetoothSocket。
                    bluetoothSocket = bluetoothServerSocket?.accept()
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
                bluetoothSocket?.let {
                    synchronized(ClassicBluetoothHelper::class.java) {
                        when (this@ClassicBluetoothHelper.state) {
                            STATE_CONNECTING,
                            STATE_LISTEN -> {
                                // 情况正常。启动连接的线程。
                                connected(bluetoothSocket, bluetoothSocket.remoteDevice)
                            }
                            STATE_CONNECTED,
                            STATE_NONE -> {
                                // 要么还没有准备好，要么已经连接好。终止新的套接字。
                                try {
                                    bluetoothSocket.close()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }

        fun cancel() {
            try {
                // 这将释放服务器套接字及其所有资源，但不会关闭 accept() 所返回的已连接的 BluetoothSocket。
                // 与 TCP/IP 不同，RFCOMM 一次只允许每个通道有一个已连接的客户端，
                // 因此大多数情况下，在接受已连接的套接字后立即在 BluetoothServerSocket 上调用 close() 是行得通的。
                bluetoothServerSocket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 此线程在与远程设备连接期间运行。
     * 它处理所有传入和传出的传输。
     */
    inner class ConnectedThread(private val bluetoothSocket: BluetoothSocket) : Thread() {

        private var inputStream: InputStream? = null
        private var outputStream: OutputStream? = null

        init {
            try {
                inputStream = bluetoothSocket.inputStream
                outputStream = bluetoothSocket.outputStream
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this@ClassicBluetoothHelper.state = STATE_CONNECTED
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            // 连接时继续监听InputStream
            while (this@ClassicBluetoothHelper.state == STATE_CONNECTED) {
                try {
                    // 从InputStream中读取
                    bytes = inputStream?.read(buffer) ?: 0
                    activity.runOnUiThread {
                        bluetoothListener.readMessage(buffer, bytes)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    connectionLost()
                }
            }
        }

        /**
         * 写入已连接的输出流。
         */
        fun write(byteArray: ByteArray) {
            try {
                outputStream?.write(byteArray)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun cancel() {
            try {
                bluetoothSocket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 此线程在尝试与设备建立传出连接时运行。它直接穿过;连接成功或失败。
     *
     * 要发起与远程设备（保持开放的服务器套接字的设备）的连接，必须首先获取表示该远程设备的 BluetoothDevice 对象。
     * （在前面有关查找设备的部分介绍了如何获取 BluetoothDevice）。
     * 然后必须使用 BluetoothDevice 来获取 BluetoothSocket 并发起连接。
     */
    inner class ConnectThread(private val bluetoothDevice: BluetoothDevice) : Thread() {

        private var bluetoothSocket: BluetoothSocket? = null

        init {
            try {
                // 这将初始化将要连接到 BluetoothDevice 的 BluetoothSocket。 此处传递的 UUID 必须与服务器设备
                // 在使用 listenUsingRfcommWithServiceRecord(String, UUID) 开放其 BluetoothServerSocket 时所用
                // 的 UUID 相匹配。 要使用相同的 UUID，只需将该 UUID 字符串以硬编码方式编入应用，然后通过
                // 服务器代码和客户端代码引用该字符串。
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this@ClassicBluetoothHelper.state = STATE_CONNECTING
        }

        override fun run() {
            // 总是取消搜索，因为它会减慢连接速度
            cancelDiscovery()
            try {
                // 执行此调用时，系统将会在远程设备上执行 SDP 查找，以便匹配 UUID。 如果查找成功并且远程
                // 设备接受了该连接，它将共享 RFCOMM 通道以便在连接期间使用，并且 connect() 将会返回。
                // 此方法为阻塞调用。 如果由于任何原因连接失败或 connect() 方法超时（大约 12 秒之后），它将会引发异常。
                // 由于 connect() 为阻塞调用，因此该连接过程应始终在主 Activity 线程以外的线程中执行。
                // 注：在调用 connect() 时，应始终确保设备未在执行设备发现。 如果正在进行发现操作，则会
                // 大幅降低连接尝试的速度，并增加连接失败的可能性。
                bluetoothSocket?.connect()
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
                connectionFailed()
                return
            }
            // 重置ConnectThread，因为我们已经完成了
            synchronized(ClassicBluetoothHelper::class.java) {
                connectThread = null
            }
            // 启动连接的线程
            bluetoothSocket?.let { connected(it, bluetoothDevice) }
        }

        fun cancel() {
            try {
                bluetoothSocket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
