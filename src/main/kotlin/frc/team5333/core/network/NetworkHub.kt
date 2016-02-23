package frc.team5333.core.network

import frc.team5333.core.Core
import frc.team5333.lib.events.EventBus
import java.io.InputStream
import java.math.BigInteger
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.filter
import kotlin.collections.toString
import kotlin.concurrent.thread
import kotlin.text.toByteArray
import kotlin.text.toLowerCase

/**
 * A stripped down version of NetworkDelegate. The reason we're using this instead of delegate is because
 * NetworkDelegate is currently not available in C, and I can't be bothered to rewrite it.
 *
 * @author Jaci
 */
enum class NetworkHub {
    INSTANCE;

    enum class PROCESSORS(var abbrev: String) {
        DRIVER_STATION("DS_CON"), SPLINES("CP_SPL"), KINECT("CP_KIN");
        var active: Socket? = null
        var lock = CountDownLatch(1)

        fun isConnected(): Boolean {
            return active != null && !active!!.isClosed && active!!.isConnected
        }
    }

    lateinit var thread: Thread
    lateinit var server: ServerSocket
    var listening = true

    fun bytesToInt(arr: ByteArray): Int = BigInteger(arr).toInt()
    fun bytesToFloat(arr: ByteArray): Float = java.lang.Float.intBitsToFloat(BigInteger(arr).toInt())

    fun intToBytes(int: Int): ByteArray = ByteBuffer.allocate(4).putInt(int).order(ByteOrder.BIG_ENDIAN).array()
    fun floatToBytes(float: Float): ByteArray = intToBytes(java.lang.Float.floatToIntBits(float))

    fun readInt(input: InputStream): Int {
        var ba = ByteArray(4)
        input.read(ba)
        return bytesToInt(ba)
    }

    fun readFloat(input: InputStream): Float {
        var ba = ByteArray(4)
        input.read(ba)
        return bytesToFloat(ba)
    }

    fun start() {
        thread = Thread({
            server = ServerSocket(5802)
            while(listening) {
                var socket = server.accept()

                Thread({ handleSocket(socket) }).start()
            }
        })
        thread.name = "Network Hub"
        thread.start()
    }

    fun handleSocket(socket: Socket) {
        var ba = ByteArray(6)
        socket.inputStream.read(ba, 0, 6)
        var s = ba.toString("ASCII")

        var proc = PROCESSORS.values().filter {
            it.abbrev.toLowerCase().equals(s.toLowerCase())
        }[0]

        proc.active = socket

        Thread.currentThread().name = "Network Hub Client ${s}"
        Core.logger.info("Network Client Connected: ${s}")
        proc.lock.countDown()
        EventBus.INSTANCE.raiseEvent(NetworkHubEvent(proc))
    }

    fun waitFor(processor: PROCESSORS) {
        if (processor.isConnected()) return
        processor.lock.await()
    }
}